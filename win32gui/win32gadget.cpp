
#include "win32gadget.h"

void Win32Gadget::setFont( BBFont *font ){
	if( Win32Font *p=dynamic_cast<Win32Font*>(font) ){
		SendMessage( hwnd(),WM_SETFONT,(WPARAM)p->hfont(),MAKELPARAM(true,0) );
	}
}

void Win32Gadget::setText( BBString *text ){
	SetWindowText( hwnd(),text->c_str() );
}

void Win32Gadget::setShape( int x,int y,int w,int h ){
	MoveWindow( hwnd(),x,y,w,h,true );
}

void Win32Gadget::setVisible( bool visible ){
	ShowWindow( hwnd(),visible ? SW_SHOW : SW_HIDE );
}

void Win32Gadget::setEnabled( bool enabled ){
	EnableWindow( hwnd(),enabled );
}

void Win32Gadget::activate(){
	SetFocus( hwnd() );
}

void Win32Gadget::clientShape( int *x,int *y,int *w,int *h ){
	RECT rect;
	GetClientRect( hwnd(),&rect );
	*x=rect.left;*y=rect.top;
	*w=rect.right-rect.left;*h=rect.bottom-rect.top;
}
