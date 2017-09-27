
#include "win32gdiscreen.h"

Win32GDIScreenDriver win32GDIScreenDriver;

static Win32GDIScreen *_desktop;

Win32GDIScreen::Win32GDIScreen( int w,int h,int fmt,int type ):_graphics(0),_type(type){

	if( _type==DESKTOP ){

		_hwnd.setHwnd( GetDesktopWindow() );

		return;
	}

	int tx=0,ty=0,tw=w,th=h,style=0;

	if( _type==FULLSCREEN ){

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

	_graphics=new Win32GDIGraphics( w,h,24 );
	ShowWindow( _hwnd.hwnd(),SW_SHOW );
	Win32Screen::activateScreen();
}

Win32GDIScreen::~Win32GDIScreen(){
	_graphics->release();

	if( _type==FULLSCREEN ) ChangeDisplaySettings( 0,0 );
}

BBGraphics *Win32GDIScreen::graphics(){

	return _graphics;
}

void Win32GDIScreen::flip( bool sync ){

	if( _type==DESKTOP ) return;

	HDC src_hdc=_graphics->lockHdc();

	HDC dst_hdc=GetDC( _hwnd.hwnd() );

	BitBlt( dst_hdc,0,0,_graphics->width(),_graphics->height(),src_hdc,0,0,SRCCOPY );

	ReleaseDC( _hwnd.hwnd(),dst_hdc );

	_graphics->unlockHdc();
}

int  Win32GDIScreen::mouseX(){
	if( _type!=DESKTOP ) return Win32Screen::mouseX();
	POINT p;
	GetCursorPos( &p );
	return p.x;
}

int  Win32GDIScreen::mouseY(){
	if( _type!=DESKTOP ) return Win32Screen::mouseY();
	POINT p;
	GetCursorPos( &p );
	return p.y;
}

LRESULT Win32GDIScreen::wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc ){
	switch( msg ){
	case WM_ACTIVATEAPP:
		if( wp ){
			if( _type==FULLSCREEN ){
				ChangeDisplaySettings( &_devmode,CDS_FULLSCREEN );
			}
		}else{
			if( _type==FULLSCREEN ){
				ChangeDisplaySettings( 0,0 );
				ShowWindow( _hwnd.hwnd(),SW_MINIMIZE );
			}
		}
		return 0;
	case WM_PAINT:
		if( IsIconic( _hwnd.hwnd() ) ){
		}else{
			flip(false);
		}
		ValidateRect( _hwnd.hwnd(),0 );
		return 0;
	case WM_ERASEBKGND:
		return 1;
	}
	return Win32Screen::wndProc( hwnd,msg,wp,lp,proc );
}

Win32GDIScreenDriver::Win32GDIScreenDriver(){
	reg( "Win32GDIScreenDriver","Screen","Native" );
}

bool Win32GDIScreenDriver::startup(){

	startModule( "Win32GDI" );

	_desktop=new Win32GDIScreen( 0,0,0,Win32GDIScreen::DESKTOP );
	autoRelease( _desktop );

	bbSetScreenDriver(this);
	return true;
}

void Win32GDIScreenDriver::shutdown(){
}

int Win32GDIScreenDriver::screenModes(){
	return 1;
}

void Win32GDIScreenDriver::enumScreenMode( int n,int *w,int *h,int *fmt ){
	*w=640;*h=480;*fmt=BB_RGB565;
}

BBScreen *Win32GDIScreenDriver::createScreen( int w,int h,int fmt,int flags ){

	int type=
		(flags & BBScreen::SCREEN_VIRTUAL) ? Win32GDIScreen::VIRTUAL : Win32GDIScreen::FULLSCREEN;

	Win32GDIScreen *t=new Win32GDIScreen( w,h,fmt,type );

	autoRelease(t);

	return t;
}

BBScreen *Win32GDIScreenDriver::desktopScreen(){
	return _desktop;
}

