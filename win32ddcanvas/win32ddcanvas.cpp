
#include "win32ddcanvas.h"

#include "../win32ddscreen/win32ddscreen.h"

#include <stdio.h>

Win32DDCanvasDriver win32DDCanvasDriver;

Win32DDCanvas::Win32DDCanvas( BBGroup *group,int style ):Win32Canvas( group,style ),_clipper(0),_graphics(0){

	HWND parent=(HWND)group->query( BBQID_WIN32CLIENTHWND );

	int wstyle=WS_CHILD|WS_CLIPCHILDREN|WS_CLIPSIBLINGS;
	int xstyle=0;

	HWND hwnd=CreateWindowEx( xstyle,Win32Hwnd::className(),"",wstyle,0,0,0,0,parent,0,GetModuleHandle(0),0 );

	if( win32DD.directDraw()->CreateClipper(0,&_clipper,0)<0 ){
		bbError( "Failed to create DirectDraw clipper" );
	}

	_clipper->SetHWnd( 0,hwnd );

	_gadget.setHwnd(hwnd);
	_gadget.setWndProc(this);
	_gadget.setEventMask(BBEvent::KEY_MASK|BBEvent::MOUSE_MASK,this);
}

Win32DDCanvas::~Win32DDCanvas(){
	if( _graphics ) _graphics->release();
	if( _clipper ) _clipper->Release();
}

BBGraphics *Win32DDCanvas::graphics(){

	if( _graphics ) return _graphics;

	_graphics=new Win32DDGraphics( width(),height(),Win32DDGraphics::TYPE_VIDMEM );

	setGraphics( _graphics );

	return _graphics;
}

void Win32DDCanvas::flip( bool sync ){

	if( !_graphics ) return;

	if( !screen() ) return;

	Win32DDScreen *t=dynamic_cast<Win32DDScreen*>(screen());
	if( !t ) return;

	IDirectDrawSurface *p=t->primarySurface();
	if( !p ) return;

	_graphics->restore();
	IDirectDrawSurface *g=_graphics->graphicsSurface();
	if( !g ) return;

	RECT r;
	GetClientRect( _gadget.hwnd(),&r );

	POINT pt={0,0};
	ClientToScreen( _gadget.hwnd(),&pt );

	RECT dst_rect={pt.x,pt.y,pt.x+r.right,pt.y+r.bottom};

	p->SetClipper( _clipper );
	p->Blt( &dst_rect,g,0,DDBLT_WAIT,0 );
	p->SetClipper(0);
}

LRESULT Win32DDCanvas::wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc ){
	switch( msg ){
	case WM_PAINT:
		if( !IsIconic( _gadget.hwnd() ) ){
			flip( false );
		}
		ValidateRect( hwnd,0 );
		return 0;
	}
	return Win32Canvas::wndProc( hwnd,msg,wp,lp,proc );
}

Win32DDCanvasDriver::Win32DDCanvasDriver(){
	reg( "Win32DDCanvasDriver","Canvas","DirectDraw" );
}

bool Win32DDCanvasDriver::startup(){

	BBModule::startDrivers( "Gui" );

	bbSetCanvasDriver( this );
	return true;
}

void Win32DDCanvasDriver::shutdown(){
}

BBCanvas *Win32DDCanvasDriver::createCanvas( BBGroup *group,int style ){
	return new Win32DDCanvas( group,style );
}
