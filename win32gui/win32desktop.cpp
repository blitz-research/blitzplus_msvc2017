
#include "win32desktop.h"

Win32Desktop::Win32Desktop(){
	_hwnd=GetDesktopWindow();
	RECT r;
	GetWindowRect( _hwnd,&r );
	BBDesktop::setShape( r.left,r.top,r.right-r.left,r.bottom-r.top );
}

void *Win32Desktop::query( int qid ){
	if( qid==BBQID_WIN32HWND || qid==BBQID_WIN32CLIENTHWND ) return _hwnd;
	return BBDesktop::query( qid );
}

void Win32Desktop::setFont( BBFont *font ){
}

void Win32Desktop::setText( BBString *text ){
}

void Win32Desktop::setShape( int x,int y,int w,int h ){
}

void Win32Desktop::setVisible( bool visible ){
}

void Win32Desktop::setEnabled( bool enabled ){
}

void Win32Desktop::activate(){
}

void Win32Desktop::clientShape( int *x,int *y,int *w,int *h ){
	RECT r;
	GetClientRect(_hwnd,&r);
	*x=r.left;
	*y=r.top;
	*w=r.right-r.left;
	*h=r.bottom-r.top;
}
