
#include "win32dddesktopscreen.h"

Win32DDDesktopScreen::Win32DDDesktopScreen(){
	_hwnd.setHwnd( GetDesktopWindow() );
}

Win32DDDesktopScreen::~Win32DDDesktopScreen(){
	_hwnd.setHwnd( 0 );
}

BBGraphics *Win32DDDesktopScreen::graphics(){
	return win32DD.desktopGraphics();
}

void Win32DDDesktopScreen::flip( bool sync ){
}

int  Win32DDDesktopScreen::mouseX(){
	POINT p;
	GetCursorPos( &p );
	return p.x;
}

int  Win32DDDesktopScreen::mouseY(){
	POINT p;
	GetCursorPos( &p );
	return p.y;
}

IDirectDrawSurface *Win32DDDesktopScreen::primarySurface(){
	return win32DD.desktopGraphics()->primarySurface();
}
