
#include "win32tabber.h"

#include "win32iconstrip.h"

#include <commctrl.h>

Win32Tabber::Win32Tabber( BBGroup *group,int style ):BBTabber(group,style){

	HWND parent=(HWND)group->query( BBQID_WIN32CLIENTHWND );

	int xstyle=0;
	int wstyle=WS_CHILD|WS_CLIPCHILDREN|TCS_FOCUSNEVER|TCS_HOTTRACK;

	HWND hwnd=CreateWindowEx( xstyle,WC_TABCONTROL,"",wstyle,0,0,0,0,parent,0,GetModuleHandle(0),0 );

	_tabber.setHwnd(hwnd);
	_tabber.setWndProc(this);

	HWND client=CreateWindowEx( 0,Win32Hwnd::className(),"",WS_CHILD|WS_VISIBLE|WS_CLIPCHILDREN,0,0,0,0,hwnd,0,GetModuleHandle(0),0 );
	_client.setHwnd(client);

	clear();
}

Win32Tabber::~Win32Tabber(){
	_client.setHwnd(0);
}

int Win32Tabber::updateSelected(){
	int index=items()->size() ? SendMessage( _tabber.hwnd(),TCM_GETCURSEL,0,0 ) : -1;
	BBTabber::select(index);
	return index;
}

void *Win32Tabber::query( int qid ){
	if( void *p=_client.query( qid ) ) return p;
	return BBTabber::query( qid );
}

void Win32Tabber::setFont( BBFont *font ){
	_tabber.setFont(font);
	BBTabber::setFont(font);
}

void Win32Tabber::setText( BBString *text ){
	_client.setText(text);
	BBTabber::setText(text);
}

void Win32Tabber::setShape( int x,int y,int w,int h ){
	_tabber.setShape( x,y,w,h );
	RECT r,t;
	GetClientRect( _tabber.hwnd(),&r );
	SendMessage( _tabber.hwnd(),TCM_GETITEMRECT,0,(LPARAM)&t );
	r.left+=2;r.right-=2;
	r.top+=t.bottom+2;r.bottom-=2;
	_client.setShape( r.left,r.top,r.right-r.left,r.bottom-r.top );
	BBTabber::setShape( x,y,w,h );
}

void Win32Tabber::setVisible( bool visible ){
	_tabber.setVisible(visible);
	BBTabber::setVisible(visible);
}

void Win32Tabber::setEnabled( bool enabled ){
	_tabber.setEnabled(enabled);
	BBTabber::setEnabled(enabled);
}

void Win32Tabber::activate(){
	_tabber.activate();
	BBTabber::activate();
}

void Win32Tabber::clientShape( int *x,int *y,int *w,int *h ){
	_client.clientShape( x,y,w,h );
}

void Win32Tabber::setIconStrip( BBIconStrip *t ){
	if( Win32IconStrip *p=dynamic_cast<Win32IconStrip*>(t) ){
		SendMessage( _tabber.hwnd(),TCM_SETIMAGELIST,0,(LPARAM)p->himagelist() );
		BBTabber::setIconStrip( t );
	}
}

void Win32Tabber::clear(){
	SendMessage( _tabber.hwnd(),TCM_DELETEALLITEMS,0,0 );
	SendMessage( _tabber.hwnd(),TCM_INSERTITEM,0,(LPARAM)" " );
	BBTabber::clear();
}

void Win32Tabber::add( BBString *item,int icon ){
	insert( items()->size(),item,icon );
}

void Win32Tabber::insert( int index,BBString *item,int icon ){
	if( !items()->size() ){
		SendMessage( _tabber.hwnd(),TCM_DELETEALLITEMS,0,0 );
	}
	TCITEM t={0};
	t.mask=TCIF_TEXT;
	t.pszText=(char*)item->c_str();
	if( iconStrip() && icon>=0 ){
		t.mask|=TCIF_IMAGE;
		t.iImage=icon;
	}
	SendMessage( _tabber.hwnd(),TCM_INSERTITEM,index,(LPARAM)&t );
	BBTabber::insert( index,item,icon );
	updateSelected();
}

void Win32Tabber::modify( int index,BBString *item,int icon ){
	TCITEM t={0};
	t.mask=TCIF_TEXT;
	t.pszText=(char*)item->c_str();
	if( iconStrip() && icon>=0 ){
		t.mask|=TCIF_IMAGE;
		t.iImage=icon;
	}
	SendMessage( _tabber.hwnd(),TCM_SETITEM,index,(LPARAM)&t );
	BBTabber::modify( index,item,icon );
}

void Win32Tabber::remove( int index ){
	if( items()->size()==1 ){
		clear();
		return;
	}
	int sel=items()->selected();
	SendMessage( _tabber.hwnd(),TCM_DELETEITEM,index,0 );
	BBTabber::remove( index );
	if( sel!=index ) return;
	if( sel==items()->size() ) --sel;
	SendMessage( _tabber.hwnd(),TCM_SETCURSEL,sel,0 );
	BBTabber::select( sel );
}

void Win32Tabber::select( int index ){
	SendMessage( _tabber.hwnd(),TCM_SETCURSEL,index,0 );
	BBTabber::select( index );
}

LRESULT Win32Tabber::wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc ){
	NMHDR *nm;
	switch( msg ){
	case WM_NOTIFY:
		nm=(NMHDR*)lp;
		if( nm->code==TCN_SELCHANGE ){
			int index=updateSelected();
			emit( BBEvent::GADGET_ACTION,index );
			return 0;
		}
	}
	return CallWindowProc(proc,hwnd,msg,wp,lp);
}
