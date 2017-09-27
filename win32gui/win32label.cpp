
#include "win32label.h"

Win32Label::Win32Label( BBGroup *group,int style ):BBLabel(group,style){

	HWND parent=(HWND)group->query( BBQID_WIN32CLIENTHWND );

	int xstyle=0;
	int wstyle=WS_CHILD;

	switch( style&3 ){
	case 1:wstyle|=WS_BORDER;break;
	case 2:xstyle|=WS_EX_WINDOWEDGE;break;
	case 3:xstyle|=WS_EX_CLIENTEDGE;break;
	}

	HWND hwnd=CreateWindowEx( xstyle,"STATIC","",wstyle,0,0,0,0,parent,0,GetModuleHandle(0),0 );

	_gadget.setHwnd(hwnd);
	_gadget.setWndProc(this);
}

void *Win32Label::query( int qid ){
	if( void *p=_gadget.query( qid ) ) return p;
	return BBLabel::query( qid );
}

void Win32Label::setFont( BBFont *font ){
	_gadget.setFont(font);
	BBLabel::setFont(font);
}

void Win32Label::setText( BBString *text ){
	_gadget.setText(text);
	BBLabel::setText(text);
}

void Win32Label::setShape( int x,int y,int w,int h ){
	_gadget.setShape(x,y,w,h);
	BBLabel::setShape(x,y,w,h); 
}

void Win32Label::setVisible( bool visible ){
	_gadget.setVisible(visible);
	BBLabel::setVisible(visible);
}

void Win32Label::setEnabled( bool enabled ){
	_gadget.setEnabled(enabled);
	BBLabel::setEnabled(enabled);
}

void Win32Label::activate(){
	_gadget.activate();
	BBLabel::activate();
}

LRESULT Win32Label::wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc ){
	return CallWindowProc( proc,hwnd,msg,wp,lp );
}
