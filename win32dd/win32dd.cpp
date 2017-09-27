
#include "win32dd.h"

#include <initguid.h>

Win32DD win32DD;

static Win32DDGraphics *_desktop,*_primary;

DEFINE_GUID( _IID_IDirectDraw, 0x6C14DB80,0xA733,0x11CE,0xA5,0x21,0x00,0x20,0xAF,0x0B,0xE5,0x60 );
DEFINE_GUID( _IID_IDirectDraw2,0xB3A6F3E0,0x2B43,0x11CF,0xA2,0xDE,0x00,0xAA,0x00,0xB9,0x33,0x56 );
DEFINE_GUID( _IID_IDirectDraw4,0x9c59509a,0x39bd,0x11d1,0x8c,0x4a,0x00,0xc0,0x4f,0xd9,0x30,0xc5 );
DEFINE_GUID( _IID_IDirectDraw7,0x15e65ec0,0x3b9c,0x11d2,0xb9,0x2f,0x00,0x60,0x97,0x97,0xea,0x5b );
DEFINE_GUID( _IID_IDirectDrawClipper,0x6C14DB85,0xA733,0x11CE,0xA5,0x21,0x00,0x20,0xAF,0x0B,0xE5,0x60 );
DEFINE_GUID( _IID_IDirectDrawGammaControl,0x69C11C3E,0xB46B,0x11D1,0xAD,0x7A,0x00,0xC0,0x4F,0xC2,0x9B,0x4E );

typedef HRESULT(WINAPI * DDCREATE)( GUID*, LPDIRECTDRAW*, IUnknown* );
typedef HRESULT(WINAPI * DDCREATEEX)( GUID*, VOID**, REFIID, IUnknown* );

Win32DD::Win32DD(){
	reg( "Win32DD","Graphics","DirectDraw" );
}

bool Win32DD::startup(){
	_desktop=0;
	_primary=0;
	_version=0;
	_dirDraw=0;
	_dirDraw2=0;
	_dirDraw4=0;
	_dirDraw7=0;
	if( _mod=LoadLibrary( "ddraw.dll" ) ){
		if(	DDCREATE create=(DDCREATE)GetProcAddress(_mod,"DirectDrawCreate") ){
			if( create(0,&_dirDraw,0)>=0 ){
				_version=1;
				if( _dirDraw->QueryInterface(_IID_IDirectDraw2,(void**)&_dirDraw2)>=0 ){
					_version=2;
					if( _dirDraw->QueryInterface(_IID_IDirectDraw4,(void**)&_dirDraw4)>=0 ){
						_version=4;
						if( DDCREATEEX createEx=(DDCREATEEX)GetProcAddress(_mod,"DirectDrawCreateEx") ){
							if( createEx(0,(void**)&_dirDraw7,_IID_IDirectDraw7,0)>=0 ){
								_version=7;
							}
						}
					}
				}
			}
		}
	}
	if( _version ){
		_dirDraw->SetCooperativeLevel(0,DDSCL_NORMAL);
		return true;
	}
	if( _mod ) FreeLibrary( _mod );
	return false;
}

void Win32DD::shutdown(){
	if( _desktop ) _desktop->release();
	if( _dirDraw7 ) _dirDraw7->Release();
	if( _dirDraw4 ) _dirDraw4->Release();
	if( _dirDraw2 ) _dirDraw2->Release();
	if( _dirDraw ) _dirDraw->Release();
	if( _mod ) FreeLibrary( _mod );
}

Win32DDGraphics *Win32DD::desktopGraphics(){
	if( _desktop ){
		if( _desktop->primarySurface() ) return _desktop;
		_desktop->release();
		_desktop=0;
	}

	RECT r;
	GetWindowRect( GetDesktopWindow(),&r );
	_desktop=new Win32DDGraphics( r.right-r.left,r.bottom-r.top,Win32DDGraphics::TYPE_PRIMARY );

	_desktop->restore();
	return _desktop;
}

int Win32DD::pixelFormat( const DDPIXELFORMAT *fmt,bool fail ){

	int n=fmt->dwRGBBitCount;
	int a=fmt->dwRGBAlphaBitMask;
	int r=fmt->dwRBitMask;
	int g=fmt->dwGBitMask;
	int b=fmt->dwBBitMask;

	switch( n ){
	case 16:
		if( r==0xf800 && g==0x7e0 && b==0x1f ) return BB_RGB565;
		if( r==0x7c00 && g==0x3e0 && b==0x1f ) return BB_XRGB1555;
		break;
	case 24:
		if( r==0xff0000 && g==0xff00 && b==0xff ) return BB_RGB888;
		break;
	case 32:
		if( r==0xff0000 && g==0xff00 && b==0xff ) return BB_XRGB8888;
		break;
	}
	if( fail ) bbError( "Unrecognized DirectDraw pixel format: bpp=%i rmask=%x gmask=%x bmask=%x amask=%x",n,r,g,b,a );
	return 0;
}

void Win32DD::pixelFormat( int fmt,DDPIXELFORMAT *p ){
	int n=0,r=0,g=0,b=0;
	switch( fmt ){
	case BB_RGB565:
		n=16;r=0xf800;g=0x7e0;b=0x1f;break;
	case BB_XRGB1555:
		n=16;r=0x7c00;g=0x3e0;b=0x1f;break;
	case BB_RGB888:
		n=24;r=0xff0000;g=0xff00;b=0xff;break;
	case BB_XRGB8888:
		n=32;r=0xff0000;g=0xff00;b=0xff;break;
	default:
		bbError( "Unrecognized Blitz pixel format: %i",fmt );
	}
	memset(p,0,sizeof(*p));
	p->dwSize=sizeof(*p);
	p->dwFlags=DDPF_RGB;
	p->dwRGBBitCount=n;
	p->dwRBitMask=r;
	p->dwGBitMask=g;
	p->dwBBitMask=b;
}

