
#include "win32toolbar.h"

#include "win32iconstrip.h"

#include <commctrl.h>

Win32ToolBar::Win32ToolBar( BBGroup *group,int style ):BBToolBar(group,style),
_tooltips(0){

	HWND parent=(HWND)group->query( BBQID_WIN32CLIENTHWND );

	int xstyle=0;
	int wstyle=TBSTYLE_FLAT|TBSTYLE_TRANSPARENT|WS_CHILD;

	HWND hwnd=CreateWindowEx( xstyle,TOOLBARCLASSNAME,"",wstyle,0,0,0,0,parent,0,GetModuleHandle(0),0 );

	SendMessage( hwnd,TB_BUTTONSTRUCTSIZE,sizeof(TBBUTTON),0 );

	_gadget.setHwnd(hwnd);
	_gadget.setWndProc(this);
}

Win32ToolBar::~Win32ToolBar(){
	if( _tooltips ) DestroyWindow(_tooltips);
}

void *Win32ToolBar::query( int qid ){
	if( void *p=_gadget.query( qid ) ) return p;
	return BBToolBar::query( qid );
}

void Win32ToolBar::setFont( BBFont *font ){
	_gadget.setFont(font);
	BBToolBar::setFont(font);
}

void Win32ToolBar::setText( BBString *text ){
	_gadget.setText(text);
	BBToolBar::setText(text);
}

void Win32ToolBar::setShape( int x,int y,int w,int h ){
	_gadget.setShape(x,y,w,h);
	BBToolBar::setShape(x,y,w,h); 
}

void Win32ToolBar::setVisible( bool visible ){
	_gadget.setVisible(visible);
	BBToolBar::setVisible(visible);
}

void Win32ToolBar::setEnabled( bool enabled ){
	_gadget.setEnabled(enabled);
	BBToolBar::setEnabled(enabled);
}

void Win32ToolBar::activate(){
	_gadget.activate();
	BBToolBar::activate();
}

void Win32ToolBar::setIconStrip( BBIconStrip *t ){

	Win32IconStrip *p=dynamic_cast<Win32IconStrip*>(t);
	if( !p ) return;

	HIMAGELIST i=p->himagelist();
	if( !i ) return;

	SendMessage( _gadget.hwnd(),TB_SETIMAGELIST,0,(LPARAM)i );
	BBToolBar::setIconStrip(t);

	int n=t->icons();

	TBBUTTON *buts=new TBBUTTON[n];
	memset( buts,0,sizeof(TBBUTTON)*n );
	int k,nButtons=0;
	for( k=0;k<n;++k ){
		buts[k].iBitmap=k;
		buts[k].fsState=TBSTATE_ENABLED;
		if( p->iconIsBlank(k) ){
			buts[k].idCommand=0;
			buts[k].fsStyle=TBSTYLE_SEP;
		}else{
			buts[k].idCommand=nButtons++;
			buts[k].fsStyle=TBSTYLE_BUTTON;
		}
	}
	SendMessage( _gadget.hwnd(),TB_ADDBUTTONS,n,(LPARAM)buts );
	delete[] buts;

	SendMessage( _gadget.hwnd(),TB_AUTOSIZE,0,0 );

	RECT rect;
	GetWindowRect( _gadget.hwnd(),&rect );
	BBToolBar::setShape( rect.left,rect.top,rect.right-rect.left,rect.bottom-rect.top );
}

void Win32ToolBar::setItemEnabled( int n,bool e ){
	SendMessage( _gadget.hwnd(),TB_ENABLEBUTTON,n+1,(LPARAM)MAKELONG(e,0) );
}

void Win32ToolBar::setTips( BBString *tips ){
	if( _tooltips ){
		DestroyWindow( _tooltips );
		_tooltips=0;
	}

	_tooltips=CreateWindowEx( 0,TOOLTIPS_CLASS,0,0,0,0,0,0,0,0,GetModuleHandle(0),0 );

	char tip[256];
	TOOLINFO ti={sizeof(ti)};
	ti.uFlags=TTF_SUBCLASS;
	ti.hwnd=_gadget.hwnd();
	ti.lpszText=tip;

	Win32IconStrip *p=dynamic_cast<Win32IconStrip*>(iconStrip());

	int button=0;
	int nButtons=p->icons();
	char *str_p=strdup( tips->c_str() );
	const char *str=str_p;

	while( *str ){

		while( button<nButtons && p->iconIsBlank(button) ) ++button;
		if( button>=nButtons ) break;

		if( const char *p=strchr(str,',') ){
			int sz=p-str;
			if( sz>255 ) break;
			memcpy(tip,str,sz);
			tip[sz]=0;
			str=p+1;
		}else{
			int sz=strlen(str);
			if( sz>255 ) break;
			strcpy(tip,str);
			str+=sz;
		}

		SendMessage( _gadget.hwnd(),TB_GETITEMRECT,button,(LPARAM)&ti.rect );
		SendMessage( _tooltips,TTM_ADDTOOL,0,(LPARAM)&ti );

		++button;
	}
	free( str_p );
}

LRESULT Win32ToolBar::wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc ){
	switch( msg ){
	case WM_COMMAND:
		emit( BBEvent::GADGET_ACTION,LOWORD(wp)-1 );
		return 0;
	}
	return CallWindowProc( proc,hwnd,msg,wp,lp );
}
