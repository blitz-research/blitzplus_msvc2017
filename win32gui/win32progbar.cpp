
#include "win32progbar.h"

#include <commctrl.h>

Win32ProgBar::Win32ProgBar( BBGroup *group,int style ):BBProgBar(group,style){

	HWND parent=(HWND)group->query( BBQID_WIN32CLIENTHWND );

	int xstyle=0;
	int wstyle=WS_CHILD|PBS_SMOOTH;

	HWND hwnd=CreateWindowEx( xstyle,PROGRESS_CLASS,"",wstyle,0,0,0,0,parent,0,GetModuleHandle(0),0 );

	_gadget.setHwnd( hwnd );
	_gadget.setWndProc( this );
}

void *Win32ProgBar::query( int qid ){
	if( void *p=_gadget.query( qid ) ) return p;
	return BBProgBar::query( qid );
}

void Win32ProgBar::setFont( BBFont *font ){
	_gadget.setFont(font);
	BBProgBar::setFont(font);
}

void Win32ProgBar::setText( BBString *text ){
	_gadget.setText(text);
	BBProgBar::setText(text);
}

void Win32ProgBar::setShape( int x,int y,int w,int h ){
	_gadget.setShape(x,y,w,h);
	BBProgBar::setShape(x,y,w,h); 
}

void Win32ProgBar::setVisible( bool visible ){
	_gadget.setVisible(visible);
	BBProgBar::setVisible(visible);
}

void Win32ProgBar::setEnabled( bool enabled ){
	_gadget.setEnabled(enabled);
	BBProgBar::setEnabled(enabled);
}

void Win32ProgBar::activate(){
	_gadget.activate();
	BBProgBar::activate();
}

void Win32ProgBar::setProgress( float t ){
	SendMessage( _gadget.hwnd(),PBM_SETPOS,(WPARAM)(t*100),0 );
	BBProgBar::setProgress(t);
}


LRESULT Win32ProgBar::wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc ){
	return CallWindowProc( proc,hwnd,msg,wp,lp );
}


