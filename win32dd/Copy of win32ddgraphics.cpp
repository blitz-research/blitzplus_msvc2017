
#include "win32dd.h"

#include "../event/event.h"

#include <set>

static Win32DDGraphics *_desktop,*_primary;

using namespace std;

static set<Win32DDGraphics*> _surfSet;

Win32DDGraphics::Win32DDGraphics( int w,int h,int type ):
BBGraphics(w,h),_type(type),_surf(0),_prim(0),
_format(0),_locked(0),_managed(0),_dummy(0),_dirty(true){
	switch( _type ){
	case TYPE_DYNAMIC:
	case TYPE_MANAGED:
		_surfSet.insert(this);
		break;
	}
}

Win32DDGraphics::~Win32DDGraphics(){
	while( _locked ) unlock();
	releaseSurface();
	switch( _type ){
	case TYPE_DYNAMIC:
	case TYPE_MANAGED:
		_surfSet.erase(this);
		break;
	}
}

void *Win32DDGraphics::query( int qid ){
	if( qid==BBQID_WIN32DDGRAPHICSSURFACE ) return _surf;
	if( qid==BBQID_WIN32DDPRIMARYSURFACE ) return _prim;
	return BBGraphics::query( qid );
}

BBGraphics *Win32DDGraphics::createCopy( int w,int h ){
	Win32DDGraphics *g=new Win32DDGraphics( w,h,_type );
	if( !g->createSurface() ){
		bbError( "Failed to create DirectDraw surface" );
	}

	return g;
}

void Win32DDGraphics::clear( int x,int y,int w,int h,BBColor color ){
	_dirty=true;
	RECT rect={x,y,x+w,y+h};
	DDBLTFX bltfx={sizeof(bltfx)};
	bltfx.dwFillColor=color.toPixel(_format);
	Blt( &rect,0,0,DDBLT_WAIT|DDBLT_COLORFILL,&bltfx );
}

void Win32DDGraphics::write( int x,int y,int w,int h,const BBColor *colors,int pitch ){
	_dirty=true;
	BBGraphics::write(x,y,w,h,colors,pitch);
}

void Win32DDGraphics::blit( int x,int y,int w,int h,BBGraphics *src,int sx,int sy,int flags ){
	_dirty=true;
	Win32DDGraphics *_src=dynamic_cast<Win32DDGraphics*>( src );

	if( !_src ){
		BBGraphics::blit( x,y,w,h,src,sx,sy,flags );
		return;
	}

	RECT dst_rect={x,y,x+w,y+h};
	RECT src_rect={sx,sy,sx+w,sy+h};

	if( flags & BLIT_KEYSOURCE ){

		static DDBLTFX bltfx={sizeof(bltfx)};
		bltfx.ddckSrcColorkey.dwColorSpaceLowValue=
		bltfx.ddckSrcColorkey.dwColorSpaceHighValue=
		_src->colorKey().toPixel( _format ) & 0xffffff;

		if( Blt( 
			&dst_rect,_src,
			&src_rect,DDBLT_WAIT|DDBLT_KEYSRCOVERRIDE,&bltfx )>=0 ) return;
	}else{

		if( Blt(
			&dst_rect,_src,
			&src_rect,DDBLT_WAIT,0 )>=0 ) return;
	}
	BBGraphics::blit( x,y,w,h,src,sx,sy,flags );
}

void Win32DDGraphics::lock( void **pixels,int *pitch,int *format ){
	_dirty=true;
	if( !_locked++ ){
		DDSURFACEDESC desc={sizeof(desc)};
		if( _surf->Lock( 0,&desc,DDLOCK_WAIT,0 )>=0 ){
			_lockedPixels=desc.lpSurface;
			_lockedPitch=desc.lPitch;
		}else{
			_dummy=new BBPixmap( width(),height() );
			_lockedPixels=_dummy->pixels();
			_lockedPitch=_dummy->pitch();
			_format=_dummy->format();
		}
	}
	*pixels=_lockedPixels;
	*pitch=_lockedPitch;
	*format=_format;
}

void Win32DDGraphics::unlock(){
	if( --_locked ) return;
	if( !_dummy ){
		_surf->Unlock( _lockedPixels );
		return;
	}
	_dummy->release();
	_dummy=0;
}

void Win32DDGraphics::releaseSurface(){
	if( _primary==this ){
		_primary=0;
	}
	if( _managed ){
		_managed->release();
		_managed=0;
	}
	if( _prim ){
		_prim->Release();
	}else if( _surf ){
		_surf->Release();
	}
	_prim=_surf=0;
	_format=0;
}

bool Win32DDGraphics::createSurface(){
	releaseSurface();

	if( _type==TYPE_PRIMARY || _type==TYPE_PRIMARY_DB ){
		if( _primary && _primary!=this ) _primary->releaseSurface();
	}

	DDSURFACEDESC desc={sizeof(desc),DDSD_CAPS};
	
	switch( _type ){
	case TYPE_SYSMEM:
	case TYPE_MANAGED:
		desc.dwFlags|=DDSD_WIDTH|DDSD_HEIGHT|DDSD_PIXELFORMAT;
		desc.ddsCaps.dwCaps|=DDSCAPS_OFFSCREENPLAIN|DDSCAPS_SYSTEMMEMORY;
		desc.dwWidth=width();desc.dwHeight=height();
		Win32DD::pixelFormat( BB_RGB888,&desc.ddpfPixelFormat );
		break;
	case TYPE_VIDMEM:
	case TYPE_DYNAMIC:
		desc.dwFlags|=DDSD_WIDTH|DDSD_HEIGHT;
		desc.ddsCaps.dwCaps|=DDSCAPS_OFFSCREENPLAIN;
		desc.dwWidth=width();desc.dwHeight=height();
		break;
	case TYPE_PRIMARY:
		desc.ddsCaps.dwCaps|=DDSCAPS_PRIMARYSURFACE;
		break;
	case TYPE_PRIMARY_DB:
		desc.dwFlags|=DDSD_BACKBUFFERCOUNT;
		desc.ddsCaps.dwCaps|=DDSCAPS_PRIMARYSURFACE|DDSCAPS_COMPLEX|DDSCAPS_FLIP;
		desc.dwBackBufferCount=1;
		break;
	}

	if( win32DD.directDraw()->CreateSurface( &desc,&_surf,0 )<0 ){
		if( _type!=TYPE_VIDMEM && _type!=TYPE_DYNAMIC ) return false;
		desc.ddsCaps.dwCaps|=DDSCAPS_SYSTEMMEMORY;
		if( win32DD.directDraw()->CreateSurface( &desc,&_surf,0 )<0 ) return false;
	}

	switch( _type ){
	case TYPE_PRIMARY:
		_prim=_surf;
		_primary=this;
		break;
	case TYPE_PRIMARY_DB:
		_prim=_surf;
		{
			DDSCAPS caps={sizeof caps};
			caps.dwCaps=DDSCAPS_BACKBUFFER;
			if( _prim->GetAttachedSurface( &caps,&_surf )<0 ){
				bbError( "Failed to get backbuffer surface" );
			}
		}
		_primary=this;
		break;
	}

	DDPIXELFORMAT pf={sizeof(pf)};
	_surf->GetPixelFormat( &pf );
	_format=Win32DD::pixelFormat( &pf,true );

	return true;
}

bool Win32DDGraphics::restoreSurface(){
	switch( _type ){
	case TYPE_MANAGED:
		_dirty=true;
		if( _managed ){
			_managed->release();
			_managed=0;
		}
		return true;
	default:
		if( _surf->Restore()>=0 ) return true;
		return createSurface();
	}
	return false;
}

HRESULT Win32DDGraphics::GetDC( HDC *hdc ){
	_dirty=true;
	return _surf->GetDC( hdc );
}

HRESULT Win32DDGraphics::ReleaseDC( HDC hdc ){
	return _surf->ReleaseDC( hdc );
}

HRESULT Win32DDGraphics::Blt( RECT *dst_rect,Win32DDGraphics *src,RECT *src_rect,DWORD flags,DDBLTFX *fx ){
	_dirty=true;
	if( src ){
		if( src->_type==TYPE_MANAGED ){
			if( !src->_managed ){
				src->_managed=new Win32DDGraphics( src->width(),src->height(),TYPE_VIDMEM );
				if( !src->_managed->createSurface() ){
					src->_managed->release();
					src->_managed=0;
					return -1;
				}
			}
			if( src->_dirty ){
				src->_managed->BBGraphics::blit( 0,0,src->width(),src->height(),src,0,0,0 );
				src->_dirty=false;
			}
			src=src->_managed;
		}
		if( _format!=src->_format ) return -1;
	}
	return _surf->Blt( dst_rect,src ? src->_surf : 0,src_rect,flags,fx );
}

void Win32DDGraphics::restoreAll(){

	set<Win32DDGraphics*>::const_iterator it;

	for( it=_surfSet.begin();it!=_surfSet.end();++it ){
		if( (*it)->restoreSurface() ) continue;
		bbError( "Failed to restore surface" );
	}
}
