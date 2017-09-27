
#include "win32gui.h"

Win32Splitter::Win32Splitter( BBGroup *group,int style ):
BBSplitter(group,style),
_style(style),_changing(0),_mx(0),_my(0),_divx(0),_divy(0),_divw(0),_divh(0){

	HWND parent=(HWND)group->query( BBQID_WIN32CLIENTHWND );

	int xstyle=0;
	int wstyle=WS_CHILD|WS_CLIPCHILDREN;

	HWND hwnd=CreateWindowEx( xstyle,Win32Hwnd::className(),"",wstyle,0,0,0,0,parent,0,GetModuleHandle(0),0 );

	_gadget.setHwnd(hwnd);
	_gadget.setWndProc(this);
	_gadget.setEventMask(BBEvent::KEY_MASK,this);
}

Win32Splitter::~Win32Splitter(){
}

void *Win32Splitter::query( int qid ){
	if( void *p=_gadget.query( qid ) ) return p;
	return BBSplitter::query( qid );
}

void Win32Splitter::setFont( BBFont *font ){
	_gadget.setFont(font);
	BBSplitter::setFont(font);
}

void Win32Splitter::setText( BBString *text ){
	_gadget.setText(text);
	BBSplitter::setText(text);
}

void Win32Splitter::setShape( int x,int y,int w,int h ){
	_gadget.setShape(x,y,w,h);
	BBSplitter::setShape(x,y,w,h); 
}

void Win32Splitter::setVisible( bool visible ){
	_gadget.setVisible(visible);
	BBSplitter::setVisible(visible);
}

void Win32Splitter::setEnabled( bool enabled ){
	_gadget.setEnabled(enabled);
	BBSplitter::setEnabled(enabled);
}

void Win32Splitter::activate(){
	_gadget.activate();
	BBSplitter::activate();
}

void Win32Splitter::clientShape( int *x,int *y,int *w,int *h ){
	_gadget.clientShape( x,y,w,h );
}

void Win32Splitter::setDivShape( int x,int y,int w,int h ){
	_divx=x;_divy=y;_divw=w;_divh=h;
	BBSplitter::setDivShape( x,y,w,h );
}

LRESULT Win32Splitter::wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc ){

	PAINTSTRUCT ps;

	switch( msg ){
	case WM_PAINT:
		if( HDC hdc=BeginPaint( hwnd,&ps ) ){

			if( (_style&3)==1 ){
				RECT r0={_divx,0,_divx+_divw,clientHeight()};
				DrawEdge( hdc,&r0,EDGE_RAISED,BF_LEFT|BF_RIGHT|BF_MIDDLE );
			}else if( (_style&3)==2 ){
				RECT r0={0,_divy,clientWidth(),_divy+_divh};
				DrawEdge( hdc,&r0,EDGE_RAISED,BF_TOP|BF_BOTTOM|BF_MIDDLE );
			}else if( (_style&3)==3 ){
				RECT r0={_divx,_divy,_divx+_divw,_divy+_divh};
				DrawEdge( hdc,&r0,EDGE_RAISED,BF_MIDDLE );
				RECT r1={_divx,0,_divx+_divw,_divy+1};
				DrawEdge( hdc,&r1,EDGE_RAISED,BF_LEFT|BF_RIGHT|BF_MIDDLE );
				RECT r2={_divx,_divy+_divh-1,_divx+_divw,clientHeight()};
				DrawEdge( hdc,&r2,EDGE_RAISED,BF_LEFT|BF_RIGHT|BF_MIDDLE );
				RECT r3={0,_divy,_divx+1,_divy+_divh};
				DrawEdge( hdc,&r3,EDGE_RAISED,BF_TOP|BF_BOTTOM|BF_MIDDLE );
				RECT r4={_divx+_divw-1,_divy,clientWidth(),_divy+_divh};
				DrawEdge( hdc,&r4,EDGE_RAISED,BF_TOP|BF_BOTTOM|BF_MIDDLE );
			}
			EndPaint( hwnd,&ps );
		}
		ValidateRect( hwnd,0 );
		return 0;
	case WM_LBUTTONDOWN:
		_changing=0;
		if( (_style&1) && _mx>=_divx && _mx<_divx+_divw ) _changing|=1;
		if( (_style&2) && _my>=_divy && _my<_divy+_divh ) _changing|=2;
		return 0;
	case WM_LBUTTONUP:
		_changing=0;
		return 0;
	case WM_SETCURSOR:
		{
			HCURSOR cursor;
			bool h=(_style&1) && _mx>=_divx && _mx<_divx+_divw;
			bool v=(_style&2) && _my>=_divy && _my<_divy+_divh;

			if( h && v ) cursor=LoadCursor( 0,IDC_SIZENESW );
			else if( h ) cursor=LoadCursor( 0,IDC_SIZEWE );
			else if( v ) cursor=LoadCursor( 0,IDC_SIZENS );
			else cursor=LoadCursor( 0,IDC_ARROW );

			SetCursor( cursor );
			return 1;
		}
		break;
	case WM_MOUSEMOVE:
		if( _changing ){
			int dx=(_changing&1) ? (short)LOWORD(lp)-_mx : 0;
			int dy=(_changing&2) ? (short)HIWORD(lp)-_my : 0;
			if( dx || dy ){
				setDivShape( _divx+dx,_divy+dy,_divw,_divh );
				InvalidateRect( hwnd,0,false );
			}
		}
		_mx=(short)LOWORD(lp);
		_my=(short)HIWORD(lp);
		return 0;
	case WM_ERASEBKGND:
		return 1;
	}
	return CallWindowProc( proc,hwnd,msg,wp,lp );
}

