
#include "win32slider.h"

#include <stdio.h>

Win32Slider::Win32Slider( BBGroup *group,int style ):BBSlider(group,style){

	HWND parent=(HWND)group->query( BBQID_WIN32CLIENTHWND );

	int xstyle=0;
	int wstyle=WS_CHILD;
	tracking=0;

	switch( style&3 ){
	case 1:wstyle|=SBS_HORZ;break;
	default:wstyle|=SBS_VERT;break;
	}

	HWND hwnd=CreateWindowEx( xstyle,"SCROLLBAR","",wstyle,0,0,0,0,parent,0,GetModuleHandle(0),0 );

	_gadget.setHwnd( hwnd );
	_gadget.setWndProc( this );
}

void *Win32Slider::query( int qid ){
	if( void *p=_gadget.query( qid ) ) return p;
	return BBSlider::query( qid );
}

void Win32Slider::setFont( BBFont *font ){
	_gadget.setFont(font);
	BBSlider::setFont(font);
}

void Win32Slider::setText( BBString *text ){
	_gadget.setText(text);
	BBSlider::setText(text);
}

void Win32Slider::setShape( int x,int y,int w,int h ){
	_gadget.setShape(x,y,w,h);
	BBSlider::setShape(x,y,w,h); 
}

void Win32Slider::setVisible( bool visible ){
	_gadget.setVisible(visible);
	BBSlider::setVisible(visible);
}

void Win32Slider::setEnabled( bool enabled ){
	_gadget.setEnabled(enabled);
	BBSlider::setEnabled(enabled);
}

void Win32Slider::activate(){
	_gadget.activate();
	BBSlider::activate();
}

int  Win32Slider::value(){
	return GetScrollPos( _gadget.hwnd(),SB_CTL );
}

void Win32Slider::setRange( int visible,int total ){
	SCROLLINFO info={sizeof(info)};
	info.fMask=SIF_PAGE|SIF_RANGE;
	info.nMin=0;
	info.nMax=total-1;
	info.nPage=visible;
	int enabled=IsWindowEnabled( _gadget.hwnd() );
	SetScrollInfo( _gadget.hwnd(),SB_CTL,&info,enabled );
	BBSlider::setRange( visible,total );
}

void Win32Slider::setValue( int value ){
	SCROLLINFO info={sizeof(info)};
	info.fMask=SIF_POS;
	info.nPos=value;
	SetScrollInfo( _gadget.hwnd(),SB_CTL,&info,true );
}

LRESULT Win32Slider::wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc ){
	if( msg!=WM_HSCROLL && msg!=WM_VSCROLL ){
		return CallWindowProc( proc,hwnd,msg,wp,lp );
	}

	SCROLLINFO info={sizeof(info)};

	if( LOWORD(wp)==SB_THUMBTRACK && !tracking ){
		tracking=1;
		emit( BBEvent::APP_BEGINMODAL );
	}
	if( LOWORD(wp)==SB_THUMBPOSITION && tracking ){
		emit( BBEvent::APP_ENDMODAL );
		tracking=0;
		return 0;
	}
	if( LOWORD(wp)==SB_THUMBTRACK || LOWORD(wp)==SB_THUMBPOSITION ){
		info.fMask=SIF_TRACKPOS;
		GetScrollInfo( _gadget.hwnd(),SB_CTL,&info );
		SetScrollPos( _gadget.hwnd(),SB_CTL,info.nTrackPos,true );
		emit( BBEvent::GADGET_ACTION,value() );
		return 0;
	}

	info.fMask=SIF_POS|SIF_PAGE|SIF_RANGE;
	GetScrollInfo( _gadget.hwnd(),SB_CTL,&info );
	int pos=info.nPos;
	int vis=info.nPage;
	int max=info.nMax+1;

	switch( LOWORD(wp) ){
	case SB_LINEUP:--pos;break;
	case SB_LINEDOWN:++pos;break;
	case SB_PAGEUP:pos-=vis;break;
	case SB_PAGEDOWN:pos+=vis;break;
	default:return 0;
	}

	SetScrollPos( _gadget.hwnd(),SB_CTL,pos,true );
	emit( BBEvent::GADGET_ACTION,value() );
	return 0;
}
