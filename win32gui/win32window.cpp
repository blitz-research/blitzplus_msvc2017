
#include "win32window.h"

#include "win32menu.h"

#include "../screen/screen.h"

#include <commctrl.h>

Win32Window::Win32Window( BBGroup *group,int style ):BBWindow(group,style),
_minw(0),_minh(0),_status(0){

	HWND parent=0;

	int xstyle=0;
	int wstyle=WS_CLIPCHILDREN;
	HMENU hmenu=0;

	if( group ){
		parent=(HWND)group->query( BBQID_WIN32CLIENTHWND );
	}else if( screen() ){
		if( parent=(HWND)screen()->query( BBQID_WIN32CLIENTHWND ) ){
			screen()->attach( this );
		}
	}

	//1=title
	if( style&1 ){
		wstyle|=WS_CAPTION|WS_SYSMENU;
		if( style&2 ) wstyle|=WS_MINIMIZEBOX|WS_MAXIMIZEBOX;
	}else{
		wstyle|=WS_POPUP;
	}
	//2=sizable
	if( style&2 ){
		wstyle|=WS_SIZEBOX;
	}
	//4=menu
	if( style&4 ){
		hmenu=CreateMenu();
		AppendMenu( hmenu,MF_STRING,0,"" );
	}
	//16=tool window
	if( style&16 ){
		xstyle|=WS_EX_TOOLWINDOW;
	}

	HWND hwnd=CreateWindowEx( xstyle,Win32Hwnd::className(),0,wstyle,0,0,0,0,parent,hmenu,GetModuleHandle(0),0 );

	if( style&8 ){
		HWND st_hwnd=CreateWindowEx( 0,STATUSCLASSNAME,0,WS_CHILD|WS_VISIBLE,0,0,0,0,hwnd,0,GetModuleHandle(0),0 );
		HWND cl_hwnd=CreateWindowEx( 0,Win32Hwnd::className(),0,WS_CHILD|WS_VISIBLE|WS_CLIPCHILDREN,0,0,0,0,hwnd,0,GetModuleHandle(0),0 );
		_status=st_hwnd;
		_client.setHwnd(cl_hwnd);
		_client.setEventMask( BBEvent::KEY_MASK,this );
	}

	_window.setHwnd(hwnd);
	_window.setWndProc(this);
	_window.setEventMask( BBEvent::KEY_MASK,this );
}

Win32Window::~Win32Window(){
	_client.setHwnd(0);
}

void *Win32Window::query( int qid ){
	if( _client.hwnd() ){
		if( void *p=_client.query( qid ) ) return p;
	}else{
		if( void *p=_window.query( qid ) ) return p;
	}
	return BBWindow::query( qid );
}

void Win32Window::setFont( BBFont *font ){
	_window.setFont(font);
	BBWindow::setFont(font);
}

void Win32Window::setText( BBString *text ){
	_window.setText(text);
	BBWindow::setText(text);
}

void Win32Window::setShape( int x,int y,int w,int h ){
	if( style()&32 ){
		if( _status ){
			RECT sb_rect;
			::GetWindowRect( _status,&sb_rect );
			h+=sb_rect.bottom-sb_rect.top;
		}
		RECT rect={x,y,x+w,y+h};
		int wstyle=GetWindowLong( _window.hwnd(),GWL_STYLE );
		bool menu=GetMenu( _window.hwnd() ) ? true : false;
		AdjustWindowRect( &rect,wstyle,menu );
		x=rect.left;
		y=rect.top;
		w=rect.right-rect.left;
		h=rect.bottom-rect.top;
	}
	_window.setShape(x,y,w,h);
	BBWindow::setShape(x,y,w,h); 
}

void Win32Window::setVisible( bool visible ){
	_window.setVisible(visible);
	BBWindow::setVisible(visible);
}

void Win32Window::setEnabled( bool enabled ){
	_window.setEnabled(enabled);
	BBWindow::setEnabled(enabled);
}

void Win32Window::activate(){
	_window.activate();
	BBWindow::activate();
}

void Win32Window::clientShape( int *x,int *y,int *w,int *h ){
	if( _client.hwnd() ){
		_client.clientShape(x,y,w,h);
	}else{
		_window.clientShape(x,y,w,h);
	}
}

void Win32Window::updateMenu(){
	if( !menu() ) return;
	HMENU t=GetMenu( _window.hwnd() );
	SetMenu( _window.hwnd(),win32CreateMenu( menu(),false ) );
	DestroyMenu( t );
}

void Win32Window::activateWindow(){
	if( !SetActiveWindow( _window.hwnd() ) ) return;
	BBWindow::activateWindow();
}

void Win32Window::setMinSize( int w,int h ){
	_minw=w;
	_minh=h;
}

void Win32Window::setStatusText( BBString *text ){
	if( _status ){
		SetWindowText( _status,text->c_str() );
	}
}

void Win32Window::minimize(){
	ShowWindow( _window.hwnd(),SW_MINIMIZE );
	BBWindow::minimize();
}

void Win32Window::maximize(){
	ShowWindow( _window.hwnd(),SW_MAXIMIZE );
	BBWindow::maximize();
}

void Win32Window::restore(){
	ShowWindow( _window.hwnd(),SW_RESTORE );
	BBWindow::restore();
}

LRESULT Win32Window::wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc ){
	RECT rect;
	switch( msg ){
	case WM_CLOSE:
		emit( BBEvent::WINDOW_CLOSE );
		return 0;
	case WM_MOVE:
		GetWindowRect( _window.hwnd(),&rect );
		BBWindow::setShape( rect.left,rect.top,rect.right-rect.left,rect.bottom-rect.top );
		emit( BBEvent::WINDOW_MOVE,0,x(),y() );
		return 0;
	case WM_ACTIVATE:
		if( LOWORD(wp)!=WA_INACTIVE ){
			BBWindow::activateWindow();
			emit( BBEvent::WINDOW_ACTIVATE );
		}
		return 0;
	case WM_GETMINMAXINFO:
		if( MINMAXINFO *p=(MINMAXINFO*)lp ){
			p->ptMinTrackSize.x=_minw;
			p->ptMinTrackSize.y=_minh;
		}
		return 0;
	case WM_SIZE:
		if( hwnd==_client.hwnd() ) return 0;
		switch( wp ){
		case SIZE_MINIMIZED:
			BBWindow::minimize();
			break;
		case SIZE_MAXIMIZED:
			BBWindow::maximize();
			break;
		case SIZE_RESTORED:
			BBWindow::restore();
			break;
		}
		if( !minimized() ){
			if( _client.hwnd() ){
				SendMessage( _status,WM_SIZE,0,0 );
				RECT rect;
				GetClientRect( _window.hwnd(),&rect );
				RECT sb_rect;
				GetWindowRect( _status,&sb_rect );
				rect.bottom-=sb_rect.bottom-sb_rect.top;
				_client.setShape( 0,0,rect.right,rect.bottom );
			}
			RECT rect;
			GetWindowRect( _window.hwnd(),&rect );
			BBWindow::setShape( rect.left,rect.top,rect.right-rect.left,rect.bottom-rect.top );
		}
		emit( BBEvent::WINDOW_SIZE,0,width(),height() );
		return 0;
	}
	return CallWindowProc( proc,hwnd,msg,wp,lp );
}
