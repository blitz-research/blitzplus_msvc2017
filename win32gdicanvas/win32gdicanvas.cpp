
#include "win32gdicanvas.h"

Win32GDICanvasDriver win32GDICanvasDriver;

Win32GDICanvas::Win32GDICanvas( BBGroup *group,int style ):Win32Canvas( group,style ),_graphics(0){

	HWND parent=(HWND)group->query( BBQID_WIN32CLIENTHWND );

	int wstyle=WS_CHILD|WS_CLIPCHILDREN;
	int xstyle=0;

	HWND hwnd=CreateWindowEx( xstyle,Win32Hwnd::className(),"",wstyle,0,0,0,0,parent,0,GetModuleHandle(0),0 );

	_gadget.setHwnd(hwnd);
	_gadget.setWndProc(this);
	_gadget.setEventMask(BBEvent::KEY_MASK|BBEvent::MOUSE_MASK,this);
}

Win32GDICanvas::~Win32GDICanvas(){
	if( _graphics ) _graphics->release();
}

BBGraphics *Win32GDICanvas::graphics(){

	if( _graphics ) return _graphics;

	_graphics=new Win32GDIGraphics( width(),height(),24 );

	setGraphics( _graphics );

	return _graphics;
}

void Win32GDICanvas::flip( bool sync ){

	if( !_graphics ) return;

	HDC src_hdc=_graphics->lockHdc();

	HDC dst_hdc=GetDC( _gadget.hwnd() );

	BitBlt( dst_hdc,0,0,_graphics->width(),_graphics->height(),src_hdc,0,0,SRCCOPY );

	ReleaseDC( _gadget.hwnd(),dst_hdc );

	_graphics->unlockHdc();
}

LRESULT Win32GDICanvas::wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc ){
	switch( msg ){
	case WM_PAINT:
		if( _graphics && !IsIconic( hwnd ) ){
			flip( false );
		}
		ValidateRect( hwnd,0 );
		return 0;
	}
	return Win32Canvas::wndProc( hwnd,msg,wp,lp,proc );
}

Win32GDICanvasDriver::Win32GDICanvasDriver(){
	reg( "Win32GDICanvasDriver","Canvas","Native" );
}

bool Win32GDICanvasDriver::startup(){

	BBModule::startDrivers( "Gui" );

	bbSetCanvasDriver( this );

	return true;
}

void Win32GDICanvasDriver::shutdown(){
}

BBCanvas *Win32GDICanvasDriver::createCanvas( BBGroup *group,int style ){
	return new Win32GDICanvas( group,style );
}
