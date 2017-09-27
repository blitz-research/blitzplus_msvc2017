
#include "win32button.h"

Win32Button::Win32Button( BBGroup *group,int style ):BBButton(group,style){

	HWND parent=(HWND)group->query( BBQID_WIN32CLIENTHWND );

	int id=0;
	int xstyle=0;
	int wstyle=WS_CHILD|WS_TABSTOP;

	switch( style ){
	case 1:wstyle|=BS_PUSHBUTTON;break;
	case 2:wstyle|=BS_AUTOCHECKBOX;break;
	case 3:wstyle|=BS_AUTORADIOBUTTON;break;
	case 4:wstyle|=BS_DEFPUSHBUTTON;id=IDOK;break;
	case 5:wstyle|=BS_PUSHBUTTON;id=IDCANCEL;break;
	default:wstyle|=BS_PUSHBUTTON;break;
	}

	HWND hwnd=CreateWindowEx( xstyle,"BUTTON","",wstyle,0,0,0,0,parent,(HMENU)id,GetModuleHandle(0),0 );

	_gadget.setHwnd( hwnd );
	_gadget.setWndProc( this );
}

void *Win32Button::query( int qid ){
	if( void *p=_gadget.query( qid ) ) return p;
	return super::query( qid );
}

void Win32Button::setFont( BBFont *font ){
	_gadget.setFont(font);
	BBButton::setFont(font);
}

void Win32Button::setText( BBString *text ){
	_gadget.setText(text);
	BBButton::setText(text);
}

void Win32Button::setShape( int x,int y,int w,int h ){
	_gadget.setShape(x,y,w,h);
	BBButton::setShape(x,y,w,h); 
}

void Win32Button::setVisible( bool visible ){
	_gadget.setVisible(visible);
	BBButton::setVisible(visible);
}

void Win32Button::setEnabled( bool enabled ){
	_gadget.setEnabled(enabled);
	BBButton::setEnabled(enabled);
}

void Win32Button::activate(){
	_gadget.activate();
	BBButton::activate();
}

int  Win32Button::state(){
	return SendMessage( _gadget.hwnd(),BM_GETCHECK,0,0 )==BST_CHECKED;
}

void Win32Button::setState( int state ){
	SendMessage( _gadget.hwnd(),BM_SETCHECK,state ? BST_CHECKED : BST_UNCHECKED,0 );
}

LRESULT Win32Button::wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc ){

	switch( msg ){
	case WM_COMMAND:
		switch( HIWORD(wp) ){
		case BN_CLICKED:
			emit( BBEvent::GADGET_ACTION,state() );
		}
		return 0;
	}

	return CallWindowProc( proc,hwnd,msg,wp,lp );
}
