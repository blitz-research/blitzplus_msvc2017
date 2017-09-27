
#include "win32glscreen.h"

Win32GLScreenDriver win32GLScreenDriver;

Win32GLScreen::Win32GLScreen( int w,int h,int fmt,int flags ):_flags(flags){

	int tx=0,ty=0,tw=w,th=h,style=0;

	if( !(_flags & BBScreen::SCREEN_VIRTUAL) ){

		style=WS_POPUP|WS_CLIPCHILDREN;

		memset( &_devmode,0,sizeof(_devmode) );

		_devmode.dmSize=sizeof(_devmode);
		_devmode.dmPelsWidth=w;
		_devmode.dmPelsHeight=h;
		_devmode.dmBitsPerPel=bbBytesPerPixel(fmt)*8;
		_devmode.dmFields=DM_PELSWIDTH|DM_PELSHEIGHT|DM_BITSPERPEL;

		ChangeDisplaySettings( &_devmode,CDS_FULLSCREEN );

	}else{

		style=WS_CAPTION|WS_BORDER|WS_CLIPCHILDREN;

		RECT rect={0,0,tw,th};

		AdjustWindowRect( &rect,style,false );

		tw=rect.right-rect.left;
		th=rect.bottom-rect.top;

		GetWindowRect( GetDesktopWindow(),&rect );

		tx=(rect.right-rect.left-tw)/2;
		ty=(rect.bottom-rect.top-th)/2;
	}

	HWND hwnd=CreateWindowEx( 
		0,Win32Hwnd::className(),0,style,
		tx,ty,tw,th,0,0,GetModuleHandle(0),0 );

	_hwnd.setHwnd(hwnd);
	_hwnd.setWndProc(this);
	_hwnd.setEventMask( BBEvent::KEY_MASK|BBEvent::MOUSE_MASK,this );

	Win32GLContext *t=new Win32GLContext( GetDC( _hwnd.hwnd() ) );
	_graphics=new Win32GLGraphics( w,h,t );
	t->release();

	ShowWindow( _hwnd.hwnd(),SW_SHOW );
}

Win32GLScreen::~Win32GLScreen(){
	_graphics->release();

	if( !(_flags & BBScreen::SCREEN_VIRTUAL) ){
		ChangeDisplaySettings( 0,0 );
	}
}

BBGraphics *Win32GLScreen::graphics(){
	return _graphics;
}

void Win32GLScreen::flip( bool sync ){
	_graphics->glContext()->swapBuffers();
}

LRESULT Win32GLScreen::wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc ){
	switch( msg ){
	case WM_ACTIVATEAPP:
		if( wp ){
			if( !(_flags & BBScreen::SCREEN_VIRTUAL) ){
				ChangeDisplaySettings( &_devmode,CDS_FULLSCREEN );
			}
		}else{
			if( !(_flags & BBScreen::SCREEN_VIRTUAL) ){
				ChangeDisplaySettings( 0,0 );
				ShowWindow( _hwnd.hwnd(),SW_MINIMIZE );
			}
		}
		return 0;
	case WM_PAINT:
		if( IsIconic( _hwnd.hwnd() ) ){
		}else{
		}
		ValidateRect( _hwnd.hwnd(),0 );
		return 0;
	case WM_ERASEBKGND:
		return 1;
	}
	return Win32Screen::wndProc( hwnd,msg,wp,lp,proc );
}

Win32GLScreenDriver::Win32GLScreenDriver(){
	reg( "Win32GLScreenDriver","Screen","OpenGL" );
}

bool Win32GLScreenDriver::startup(){

	startModule( "Win32GL" );

	bbSetScreenDriver(this);
	return true;
}

void Win32GLScreenDriver::shutdown(){
}

int Win32GLScreenDriver::screenModes(){
	return 1;
}

void Win32GLScreenDriver::enumScreenMode( int n,int *w,int *h,int *fmt ){
	*w=640;*h=480;*fmt=BB_RGB565;
}

BBScreen *Win32GLScreenDriver::createScreen( int w,int h,int fmt,int flags ){
	return new Win32GLScreen( w,h,fmt,flags );
}

BBScreen *Win32GLScreenDriver::desktopScreen(){
	return 0;
}
