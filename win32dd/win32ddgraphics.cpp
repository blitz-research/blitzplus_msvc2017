
#include "win32dd.h"

#include "../event/event.h"

static Win32DDGraphics *_primary;

Win32DDGraphics::Win32DDGraphics( int w,int h,int type ):
BBGraphics(w,h),
_type(type),_format(0),_surf(0),_prim(0),
_dirty(true),_managed(0),_locked(0),_dummy(0),_hdc(0){
	if( type==TYPE_MANAGED ) _managed=new Win32DDGraphics( w,h,TYPE_VIDMEM );
}

Win32DDGraphics::~Win32DDGraphics(){
	if( _managed ) _managed->release();
	releaseSurface();
}

void Win32DDGraphics::releaseSurface(){
	if( _hdc ){
		unlockHdc();
	}
	if( _locked ){
		_locked=1;
		unlock();
	}
	if( _primary==this ){
		_primary=0;
	}
	if( _prim ){
		_prim->Release();
	}else if( _surf ){
		_surf->Release();
	}
	_prim=_surf=0;
	_format=0;
}

bool Win32DDGraphics::valid(){
	return _surf && _surf->IsLost()>=0;
}

bool Win32DDGraphics::restore(){

	if( valid() ) return true;

	releaseSurface();

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
		desc.dwFlags|=DDSD_WIDTH|DDSD_HEIGHT;
		desc.ddsCaps.dwCaps|=DDSCAPS_OFFSCREENPLAIN;
		desc.dwWidth=width();desc.dwHeight=height();
		break;
	case TYPE_PRIMARY:
		desc.ddsCaps.dwCaps|=DDSCAPS_PRIMARYSURFACE;
		if( _primary ) _primary->releaseSurface();
		break;
	case TYPE_PRIMARY_DB:case TYPE_PRIMARY_COPYDB:
		desc.dwFlags|=DDSD_BACKBUFFERCOUNT;
		desc.ddsCaps.dwCaps|=DDSCAPS_PRIMARYSURFACE|DDSCAPS_COMPLEX|DDSCAPS_FLIP;
		desc.dwBackBufferCount=1;
		if( _primary ) _primary->releaseSurface();
		break;
	}

	if( win32DD.directDraw()->CreateSurface( &desc,&_surf,0 )<0 ){
		if( _type!=TYPE_VIDMEM ) return false;
		desc.ddsCaps.dwCaps|=DDSCAPS_SYSTEMMEMORY;
		if( win32DD.directDraw()->CreateSurface( &desc,&_surf,0 )<0 ) return false;
	}

	switch( _type ){
	case TYPE_PRIMARY:
		_prim=_surf;
		_primary=this;
		break;
	case TYPE_PRIMARY_DB:
	case TYPE_PRIMARY_COPYDB:
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

void *Win32DDGraphics::query( int qid ){
	if( qid==BBQID_WIN32DDGRAPHICSSURFACE ) return _surf;
	if( qid==BBQID_WIN32DDPRIMARYSURFACE ) return _prim;
	return BBGraphics::query( qid );
}

BBGraphics *Win32DDGraphics::createCopy( int w,int h ){
	Win32DDGraphics *g=new Win32DDGraphics( w,h,_type );
	return g;
}

void Win32DDGraphics::clear( int x,int y,int w,int h,BBColor color ){
	_dirty=true;

	RECT rect={x,y,x+w,y+h};
	DDBLTFX bltfx={sizeof(bltfx)};

	if( _surf ){
		bltfx.dwFillColor=color.toPixel(_format);
		if( _surf->Blt( &rect,0,0,DDBLT_WAIT|DDBLT_COLORFILL,&bltfx )>=0 ) return;
	}

	if( !restore() ) return;

	bltfx.dwFillColor=color.toPixel(_format);
	_surf->Blt( &rect,0,0,DDBLT_WAIT|DDBLT_COLORFILL,&bltfx );
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

	if( _src->_type==TYPE_MANAGED ){
		switch( _type ){
		case TYPE_SYSMEM:
		case TYPE_MANAGED:
			break;
		default:
			if( _src->_dirty ){
				_src->_managed->BBGraphics::blit( 0,0,_src->width(),_src->height(),_src,0,0,0 );
				_src->_dirty=false;
			}
			_src=_src->_managed;
		}
	}


	static DDBLTFX bltfx={sizeof(bltfx)};

	RECT dst_rect={x,y,x+w,y+h};
	RECT src_rect={sx,sy,sx+w,sy+h};
	DWORD bltflags=DDBLT_WAIT;
	if( flags & BLIT_KEYSOURCE ) bltflags|=DDBLT_KEYSRCOVERRIDE;

	if( _surf && _src->_surf && _format==_src->_format ){
		bltfx.ddckSrcColorkey.dwColorSpaceLowValue=
		bltfx.ddckSrcColorkey.dwColorSpaceHighValue=src->colorKey().toPixel(_format);
		if( _surf->Blt( &dst_rect,_src->_surf,&src_rect,bltflags,&bltfx )>=0 ) return;
	}

	if( !restore() ) return;

	if( !_src->valid() ){
		if( !_src->restore() ) return;
		if( _src!=src ){
			_src->BBGraphics::blit( 0,0,_src->width(),_src->height(),src,0,0,0 );
		}
	}

	if( _format==_src->_format ){
		bltfx.ddckSrcColorkey.dwColorSpaceLowValue=
		bltfx.ddckSrcColorkey.dwColorSpaceHighValue=src->colorKey().toPixel(_format);
		_surf->Blt( &dst_rect,_src->_surf,&src_rect,bltflags,&bltfx );
	}else{
		BBGraphics::blit( x,y,w,h,src,sx,sy,flags );
	}
}

void Win32DDGraphics::lock( void **pixels,int *pitch,int *format ){
	_dirty=true;
	if( !_locked ){

		DDSURFACEDESC desc={sizeof(desc)};

		bool ok=_surf && _surf->Lock( 0,&desc,DDLOCK_WAIT,0 )>=0;

		if( !ok ) ok=restore() && _surf->Lock( 0,&desc,DDLOCK_WAIT,0)>=0;

		if( ok ){
			_lockedPixels=desc.lpSurface;
			_lockedPitch=desc.lPitch;
		}else{
			_dummy=new BBPixmap( width(),height() );
			_lockedPixels=_dummy->pixels();
			_lockedPitch=_dummy->pitch();
			_format=_dummy->format();
		}
	}
	++_locked;
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
	_format=0;
}

HDC Win32DDGraphics::lockHdc(){
	_dirty=true;
	if( _hdc ){
		bbError( "Failed to lock HDC" );
	}
	if( _surf && _surf->GetDC( &_hdc )>=0 ) return _hdc;
	if( restore() && _surf->GetDC(&_hdc)>=0 ) return _hdc;
	return _hdc=0;
}

void Win32DDGraphics::unlockHdc(){
	if( _hdc ) _surf->ReleaseDC( _hdc );
	_hdc=0;
}

IDirectDrawSurface *Win32DDGraphics::primarySurface(){
	return _prim && _prim->IsLost()>=0 ? _prim : 0;
}

IDirectDrawSurface *Win32DDGraphics::graphicsSurface(){
	return _surf && _surf->IsLost()>=0 ? _surf : 0;
}
