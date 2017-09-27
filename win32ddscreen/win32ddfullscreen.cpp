
#include "win32ddfullscreen.h"

#include <stdio.h>

static Win32DDFullScreen *_excl;

Win32DDFullScreen::Win32DDFullScreen( int w,int h,int fmt,int flags ):
_fmt(fmt),_flags(flags),_clipper(0){

	int ty=Win32DDGraphics::TYPE_PRIMARY;

	if( flags & BBScreen::SCREEN_DOUBLEBUFFER ){
		ty=Win32DDGraphics::TYPE_PRIMARY_DB;
	}

	_graphics=new Win32DDGraphics( w,h,ty );

	HWND hwnd=CreateWindowEx( 0,Win32Hwnd::className(),0,WS_POPUP|WS_CLIPCHILDREN|WS_VISIBLE,0,0,0,0,0,0,GetModuleHandle(0),0 );

	_hwnd.setHwnd(hwnd);
	_hwnd.setWndProc(this);
	_hwnd.setEventMask( BBEvent::KEY_MASK|BBEvent::MOUSE_MASK,this );
}

Win32DDFullScreen::~Win32DDFullScreen(){
	if( _excl==this ){
		win32DD.directDraw()->RestoreDisplayMode();
		win32DD.directDraw()->SetCooperativeLevel(0,DDSCL_NORMAL);
		_excl=0;
	}
	if( _clipper ) _clipper->Release();
	_graphics->release();
}

BBGraphics *Win32DDFullScreen::graphics(){
	return _graphics;
}

void Win32DDFullScreen::activateScreen(){
	if( _excl ){
		if( _excl==this ){
			ShowWindow( _hwnd.hwnd(),SW_RESTORE );
			return;
		}
		if( win32DD.directDraw()->SetCooperativeLevel(0,DDSCL_NORMAL)<0 ){
			bbError( "Failed to set normal cooperative level" );
		}
		_excl=0;
	}

	if( win32DD.directDraw()->SetCooperativeLevel( _hwnd.hwnd(),DDSCL_EXCLUSIVE|DDSCL_FULLSCREEN )<0 ){
		bbError( "Failed to set excl cooperative level" );
	}
	_excl=this;

	int depth=bbBytesPerPixel( _fmt ) * 8;
	if( win32DD.directDraw()->SetDisplayMode( _graphics->width(),_graphics->height(),depth )<0 ){
		char buf[256];
		sprintf(buf, "Failed to set display mode %i %i %i", _graphics->width(), _graphics->height(), depth);
		bbError(buf);
		bbError( "Failed to set display mode" );
	}

	while( !primarySurface() ){
		bbPollEvent();
	}

	BBScreen::activateScreen();
}

void Win32DDFullScreen::flip( bool sync ){

	IDirectDrawSurface *p=primarySurface();
	if( !p ) return;

	IDirectDrawSurface *g=_graphics->graphicsSurface();
	if( !g ) return;

	if( sync ){
		BOOL vb;
		while( win32DD.directDraw()->GetVerticalBlankStatus(&vb)>=0 && vb ){}
	}

	if( _flags & BBScreen::SCREEN_GUICOMPATIBLE ){

		if( sync ){
			win32DD.directDraw()->WaitForVerticalBlank( DDWAITVB_BLOCKBEGIN,0 );
		}
		p->SetClipper( _clipper );
		p->Blt( 0,g,0,DDBLT_WAIT,0 );
		p->SetClipper( 0 );

	}else{

		p->Flip( 0,DDFLIP_WAIT|(sync?0:DDFLIP_NOVSYNC) );
	}
}

IDirectDrawSurface *Win32DDFullScreen::primarySurface(){
	return _graphics->primarySurface();
}

LRESULT Win32DDFullScreen::wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc ){
	switch( msg ){
	case WM_PAINT:
		if( _excl!=this ){

		}else if( IsIconic(_hwnd.hwnd()) ){

		}else if( !primarySurface() ){

			//restore primary surface
			if( !_graphics->restore() ){
				//Argh! Don't work if an 'owned' window is active!!!!!
				break;
			}

			//create clipper
			if( _flags & BBScreen::SCREEN_GUICOMPATIBLE ){
				if( _clipper ){ _clipper->Release();_clipper=0; }
				if( win32DD.directDraw()->CreateClipper(0,&_clipper,0)<0 ){
					bbError( "Failed to create DD clipper" );
				}
				_clipper->SetHWnd( 0,_hwnd.hwnd() );
			}

			//fix gamma
//			updateGamma( false );

		}else if( _flags & BBScreen::SCREEN_GUICOMPATIBLE ){

			flip(false);
		}
		ValidateRect(hwnd,0);
		return 0;
	case WM_ERASEBKGND:
		return 1;
	}
	return Win32DDScreen::wndProc( hwnd,msg,wp,lp,proc );
}
