
#include "win32panel.h"

#include "win32gui.h"

Win32Panel::Win32Panel( BBGroup *group,int style ):BBPanel(group,style),
_brush(0),_bitmap(0){

	HWND parent=(HWND)group->query( BBQID_WIN32CLIENTHWND );

	int xstyle=0;
	int wstyle=WS_CHILD|WS_CLIPCHILDREN;

	if( style&1 ) xstyle|=WS_EX_CLIENTEDGE;

	HWND hwnd=CreateWindowEx( xstyle,Win32Hwnd::className(),0,wstyle,0,0,0,0,parent,0,GetModuleHandle(0),0 );

	_panel.setHwnd( hwnd );
	_panel.setWndProc( this );
	_panel.setEventMask( BBEvent::KEY_MASK,this );
}

Win32Panel::~Win32Panel(){
	if( _brush ) DeleteObject( _brush );
	if( _bitmap ) DeleteObject( _bitmap );
}

void *Win32Panel::query( int qid ){
	if( void *p=_panel.query( qid ) ) return p;
	return BBPanel::query( qid );
}

void Win32Panel::setFont( BBFont *font ){
	_panel.setFont(font);
	BBPanel::setFont(font);
}

void Win32Panel::setText( BBString *text ){
	_panel.setText(text);
	BBPanel::setText(text);
}

void Win32Panel::setShape( int x,int y,int w,int h ){
	_panel.setShape(x,y,w,h);
	BBPanel::setShape(x,y,w,h); 
}

void Win32Panel::setVisible( bool visible ){
	_panel.setVisible(visible);
	BBPanel::setVisible(visible);
}

void Win32Panel::setEnabled( bool enabled ){
	_panel.setEnabled(enabled);
	BBPanel::setEnabled(enabled);
}

void Win32Panel::activate(){
	_panel.activate();
	BBPanel::activate();
}

void Win32Panel::clientShape( int *x,int *y,int *w,int *h ){
	_panel.clientShape( x,y,w,h );
}

void Win32Panel::setBackgroundImage( BBGraphics *g ){

	if( _brush ){ DeleteObject( _brush );_brush=0; }
	if( _bitmap ){ DeleteObject( _bitmap );_bitmap=0; }

	_bgw=g->width();
	_bgh=g->height();
	_bitmap=win32GuiDriver.createBitmap( g );

	InvalidateRect( _panel.hwnd(),0,true );
}

void Win32Panel::setBackgroundColor( int r,int g,int b ){

	if( _brush ){ DeleteObject( _brush );_brush=0; }
	if( _bitmap ){ DeleteObject( _bitmap );_bitmap=0; }

	_brush=CreateSolidBrush( (b<<16)|(g<<8)|r );

	InvalidateRect( _panel.hwnd(),0,true );
}

LRESULT Win32Panel::wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc ){
	switch( msg ){
	case WM_ERASEBKGND:
		if( _bitmap ){
			RECT r;
			GetClientRect( _panel.hwnd(),&r );
			HDC hdc=CreateCompatibleDC((HDC)wp);
			HBITMAP _bm=(HBITMAP)SelectObject( hdc,_bitmap );
			for( int y=0;y<r.bottom;y+=_bgh ){
				for( int x=0;x<r.right;x+=_bgw ){
					BitBlt( (HDC)wp,x,y,_bgw,_bgh,hdc,0,0,SRCCOPY );
				}
			}
			SelectObject( hdc,_bm );
			DeleteDC( hdc );
			return 1;
		}else if( _brush ){
			RECT r;
			GetClientRect( _panel.hwnd(),&r );
			FillRect( (HDC)wp,&r,_brush );
			return 1;
		}
		break;
	}
	return CallWindowProc( proc,hwnd,msg,wp,lp );
}
