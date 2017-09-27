
#include "win32screen.h"

Win32Screen::Win32Screen():_pointerVis(true){
}

void *Win32Screen::query( int qid ){
	if( void *p=_hwnd.query(qid) ) return p;
	return BBScreen::query( qid );
}

BBGraphics *Win32Screen::graphics(){
	return 0;
}

void Win32Screen::flip( bool sync ){
}

void Win32Screen::setTitle( BBString *title ){
	SetWindowText( _hwnd.hwnd(),title->c_str() );
}

void Win32Screen::setPointer( int n ){
	_hwnd.setPointer(n);
}

void Win32Screen::moveMouse( int x,int y ){
	_hwnd.moveMouse(x,y);
}

int  Win32Screen::mouseX(){
	return _hwnd.mouseX();
}

int  Win32Screen::mouseY(){
	return _hwnd.mouseY();
}

LRESULT Win32Screen::wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc ){
	switch( msg ){
	case WM_CLOSE:
		return 0;
	case WM_ERASEBKGND:
		return 1;
	}
	return CallWindowProc( proc,hwnd,msg,wp,lp );
}
