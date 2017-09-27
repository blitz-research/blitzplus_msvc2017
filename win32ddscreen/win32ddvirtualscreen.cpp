
#include "win32ddvirtualscreen.h"

#include <stdio.h>

Win32DDVirtualScreen::Win32DDVirtualScreen( int w,int h,int fmt,int flags ):_graphics(0),_clipper(0){

	_graphics=new Win32DDGraphics( w,h,Win32DDGraphics::TYPE_VIDMEM );

	if( win32DD.directDraw()->CreateClipper(0,&_clipper,0)<0 ){
		bbError( "Failed to create DD clipper" );
	}

	_graphics->clear( 0,0,w,h,BBColor::black() );

	int style=WS_CAPTION|WS_BORDER;

	RECT rect={0,0,w,h};
	AdjustWindowRect( &rect,style,false );
	int ww=rect.right-rect.left;
	int wh=rect.bottom-rect.top;

	RECT drect;
	GetWindowRect( GetDesktopWindow(),&drect );
	int dw=drect.right-drect.left;
	int dh=drect.bottom-drect.top;
	int wx=(dw-ww)/2,wy=(dh-wh)/2;

	HWND hwnd=CreateWindowEx( 
		0,Win32Hwnd::className(),0,style,
		wx,wy,ww,wh,0,0,GetModuleHandle(0),0 );

	_clipper->SetHWnd( 0,hwnd );

	ShowWindow( hwnd,SW_SHOW );

	_hwnd.setHwnd(hwnd);
	_hwnd.setWndProc(this);
	_hwnd.setEventMask( BBEvent::KEY_MASK|BBEvent::MOUSE_MASK,this );
}

Win32DDVirtualScreen::~Win32DDVirtualScreen(){
	_clipper->Release();
	_graphics->release();
}

void Win32DDVirtualScreen::flip( bool sync ){

	IDirectDrawSurface *d=primarySurface();
	if( !d ) return;

	IDirectDrawSurface *g=_graphics->graphicsSurface();
	if( !g ) return;

	POINT p={0,0};
	ClientToScreen( _hwnd.hwnd(),&p );
	RECT rect={p.x,p.y,p.x+_graphics->width(),p.y+_graphics->height()};

	if( sync ){
		BOOL vb;
		while( win32DD.directDraw()->GetVerticalBlankStatus(&vb)>=0 && vb ){}
		win32DD.directDraw()->WaitForVerticalBlank( DDWAITVB_BLOCKBEGIN,0 );
	}

	d->SetClipper( _clipper );
	d->Blt( &rect,g,0,DDBLT_WAIT,0 );
	d->SetClipper(0);
}

BBGraphics *Win32DDVirtualScreen::graphics(){
	return _graphics;
}

IDirectDrawSurface *Win32DDVirtualScreen::primarySurface(){
	return win32DD.desktopGraphics()->primarySurface();
}

LRESULT Win32DDVirtualScreen::wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc ){
	switch( msg ){
	case WM_PAINT:
		if( !IsIconic( _hwnd.hwnd() ) ){
			flip( false );
		}
		ValidateRect( hwnd,0 );
		return 0;
	case WM_ERASEBKGND:
		return 1;
	}
	return Win32DDScreen::wndProc( hwnd,msg,wp,lp,proc );
}

