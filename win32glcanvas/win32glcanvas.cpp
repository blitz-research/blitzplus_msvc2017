
#include "win32glcanvas.h"

Win32GLCanvasDriver win32GLCanvasDriver;

Win32GLCanvas::Win32GLCanvas( BBGroup *group,int style ):Win32Canvas( group,style ),_graphics(0){

	HWND parent=(HWND)group->query( BBQID_WIN32CLIENTHWND );

	int wstyle=WS_CHILD|WS_CLIPCHILDREN|WS_CLIPSIBLINGS;
	int xstyle=0;

	HWND hwnd=CreateWindowEx( xstyle,Win32Hwnd::className(),"",wstyle,0,0,0,0,parent,0,GetModuleHandle(0),0 );

	_gadget.setHwnd(hwnd);
	_gadget.setWndProc(this);
	_gadget.setEventMask(BBEvent::KEY_MASK|BBEvent::MOUSE_MASK,this);
}

Win32GLCanvas::~Win32GLCanvas(){
	if( _graphics ) _graphics->release();
}

BBGraphics *Win32GLCanvas::graphics(){
	if( _graphics ) return _graphics;

	Win32GLContext *t=new Win32GLContext( GetDC( _gadget.hwnd() ) );
	_graphics=new Win32GLGraphics( width(),height(),t );
	t->release();

	setGraphics( _graphics );

	return _graphics;
}

void Win32GLCanvas::flip( bool sync ){
	_graphics->glContext()->swapBuffers();
}

LRESULT Win32GLCanvas::wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc ){
	switch( msg ){
	case WM_SIZE:
		if( _graphics ){
			bbPushGLContext( _graphics->glContext() );
			glViewport( 0,0,LOWORD(lp),HIWORD(lp) );
			bbPopGLContext();
		}
		return 0;
	case WM_ERASEBKGND:
		return 1;
	}
	return Win32Canvas::wndProc( hwnd,msg,wp,lp,proc );
}

Win32GLCanvasDriver::Win32GLCanvasDriver(){
	reg( "Win32GLCanvasDriver","Canvas","OpenGL" );
}

bool Win32GLCanvasDriver::startup(){

	BBModule::startDrivers( "Gui" );

	bbSetCanvasDriver( this );
	return true;
}

void Win32GLCanvasDriver::shutdown(){
}

BBCanvas *Win32GLCanvasDriver::createCanvas( BBGroup *group,int style ){
	return new Win32GLCanvas( group,style );
}