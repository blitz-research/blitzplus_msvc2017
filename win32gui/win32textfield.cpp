
#include "win32textfield.h"

Win32TextField::Win32TextField( BBGroup *group,int style ):BBTextField(group,style){

	HWND parent=(HWND)group->query( BBQID_WIN32CLIENTHWND );

	int xstyle=WS_EX_CLIENTEDGE;
	int wstyle=WS_CHILD|WS_TABSTOP|ES_AUTOHSCROLL;

	if( style&1 ) wstyle|=ES_PASSWORD;

	HWND hwnd=CreateWindowEx( xstyle,"EDIT","",wstyle,0,0,0,0,parent,0,GetModuleHandle(0),0 );

	_gadget.setHwnd( hwnd );
	_gadget.setWndProc( this );
}

void *Win32TextField::query( int qid ){
	if( void *p=_gadget.query( qid ) ) return p;
	return BBTextField::query( qid );
}

void Win32TextField::setFont( BBFont *font ){
	_gadget.setFont(font);
	BBTextField::setFont(font);
}

void Win32TextField::setText( BBString *text ){
	DWORD from,to;
	SendMessage( _gadget.hwnd(),EM_GETSEL,(WPARAM)&from,(LPARAM)&to );
	_gadget.setText( text );
	SendMessage( _gadget.hwnd(),EM_SETSEL,(WPARAM)from,(LPARAM)to );
	BBTextField::setText(text);
}

void Win32TextField::setShape( int x,int y,int w,int h ){
	_gadget.setShape(x,y,w,h);
	BBTextField::setShape(x,y,w,h); 
}

void Win32TextField::setVisible( bool visible ){
	_gadget.setVisible(visible);
	BBTextField::setVisible(visible);
}

void Win32TextField::setEnabled( bool enabled ){
	_gadget.setEnabled(enabled);
	BBTextField::setEnabled(enabled);
}

void Win32TextField::activate(){
	_gadget.activate();
	BBTextField::activate();
}

BBString *Win32TextField::textFieldText(){
	int len=SendMessage( _gadget.hwnd(),WM_GETTEXTLENGTH,0,0 );
	char *buff=new char[len+1];
	SendMessage( _gadget.hwnd(),WM_GETTEXT,len+1,(LPARAM)buff );
	BBString *t=new BBString( buff,len );
	delete[] buff;
	return t;
}

LRESULT Win32TextField::wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc ){
	static int wm_char;
	switch( msg ){
	case WM_CHAR:
		if( wp=='\r' ){
			emit( BBEvent::GADGET_ACTION,wp );
			return 0;
		}
		wm_char=wp;
		break;
	case WM_COMMAND:
		if( HIWORD(wp)==EN_UPDATE ){
			emit( BBEvent::GADGET_ACTION,wm_char );
		}
		return 0;
	}
	return CallWindowProc( proc,hwnd,msg,wp,lp );
}
