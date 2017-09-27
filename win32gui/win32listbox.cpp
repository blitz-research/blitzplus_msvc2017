
#include "win32listbox.h"

#include "win32iconstrip.h"

#include <commctrl.h>

Win32ListBox::Win32ListBox( BBGroup *group,int style ):BBListBox(group,style){

	HWND parent=(HWND)group->query( BBQID_WIN32CLIENTHWND );

	int xstyle=WS_EX_CLIENTEDGE;

	int wstyle=
		WS_CHILD|
		LVS_REPORT|
		LVS_NOCOLUMNHEADER|
		LVS_SHOWSELALWAYS|
		LVS_SINGLESEL|
		LVS_SHAREIMAGELISTS;

	HWND hwnd=CreateWindowEx( xstyle,WC_LISTVIEW,0,wstyle,0,0,0,0,parent,0,GetModuleHandle(0),0 );

	LVCOLUMN lc={0};
	SendMessage( hwnd,LVM_INSERTCOLUMN,0,(LPARAM)&lc );

	_gadget.setHwnd(hwnd);
	_gadget.setWndProc(this);
}

int Win32ListBox::updateSelected(){
	int index=SendMessage( _gadget.hwnd(),LVM_GETNEXTITEM,-1,MAKELPARAM(LVNI_SELECTED,0) );
	BBListBox::select(index);
	return index;
}

void *Win32ListBox::query( int qid ){
	if( void *p=_gadget.query( qid ) ) return p;
	return BBListBox::query( qid );
}

void Win32ListBox::setFont( BBFont *font ){
	_gadget.setFont(font);
	BBListBox::setFont(font);
}

void Win32ListBox::setText( BBString *text ){
	_gadget.setText(text);
	BBListBox::setText(text);
}

void Win32ListBox::setShape( int x,int y,int w,int h ){
	_gadget.setShape(x,y,w,h);
	BBListBox::setShape(x,y,w,h); 
}

void Win32ListBox::setVisible( bool visible ){
	_gadget.setVisible(visible);
	BBListBox::setVisible(visible);
}

void Win32ListBox::setEnabled( bool enabled ){
	_gadget.setEnabled(enabled);
	BBListBox::setEnabled(enabled);
}

void Win32ListBox::activate(){
	_gadget.activate();
	BBListBox::activate();
}

void Win32ListBox::setIconStrip( BBIconStrip *t ){
	if( Win32IconStrip *p=dynamic_cast<Win32IconStrip*>(t ) ){
		SendMessage( _gadget.hwnd(),LVM_SETIMAGELIST,(WPARAM)LVSIL_SMALL,(LPARAM)p->himagelist() );
		BBListBox::setIconStrip(t);
	}
}

void Win32ListBox::clear(){
	SendMessage( _gadget.hwnd(),LVM_DELETEALLITEMS,0,0 );
	BBListBox::clear();
	updateSelected();
}

void Win32ListBox::add( BBString *item,int icon ){
	LVITEM it={LVIF_TEXT};
	it.iItem=BBListBox::items()->size();
	it.pszText=(char*)item->c_str();
	if( iconStrip() && icon>=0 ){
		it.mask|=LVIF_IMAGE;
		it.iImage=icon;
	}
	SendMessage( _gadget.hwnd(),LVM_INSERTITEM,0,(LPARAM)&it );
	BBListBox::add( item,icon );
}

void Win32ListBox::insert( int index,BBString *item,int icon ){
	LVITEM it={LVIF_TEXT};
	it.iItem=index;
	it.pszText=(char*)item->c_str();
	if( iconStrip() && icon>=0 ){
		it.mask|=LVIF_IMAGE;
		it.iImage=icon;
	}
	SendMessage( _gadget.hwnd(),LVM_INSERTITEM,0,(LPARAM)&it );
	BBListBox::insert( index,item,icon );
	updateSelected();
}

void Win32ListBox::modify( int index,BBString *item,int icon ){
	LVITEM it={LVIF_TEXT};
	it.iItem=index;
	it.pszText=(char*)item->c_str();
	if( iconStrip() && icon>=0 ){
		it.mask|=LVIF_IMAGE;
		it.iImage=icon;
	}
	SendMessage( _gadget.hwnd(),LVM_SETITEM,0,(LPARAM)&it );
	BBListBox::modify( index,item,icon );
}

void Win32ListBox::remove( int index ){
	SendMessage( _gadget.hwnd(),LVM_DELETEITEM,(WPARAM)index,0 );
	BBListBox::remove( index );
	updateSelected();
}

void Win32ListBox::select( int index ){
	LVITEM it={LVIF_STATE};
	it.iItem=index;
	it.state=it.stateMask=LVIS_SELECTED;
	SendMessage( _gadget.hwnd(),LVM_SETITEMSTATE,index,(LPARAM)&it );
	SendMessage( _gadget.hwnd(),LVM_ENSUREVISIBLE,index,false );
	BBListBox::select( index );
}

LRESULT Win32ListBox::wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc ){
	switch( msg ){
	case WM_SIZE:
		SendMessage( _gadget.hwnd(),LVM_SETCOLUMNWIDTH,0,LOWORD(lp) );
		break;
	case WM_NOTIFY:
		switch( ((NMHDR*)lp)->code ){
//		case NM_RETURN:case NM_CLICK:case NM_DBLCLK:case NM_RCLICK:case NM_RDBLCLK:
		case LVN_ITEMCHANGED:
			{
				NMLISTVIEW *p=(NMLISTVIEW*)(lp);
				if( !(p->uNewState & LVIS_SELECTED) ){
					return CallWindowProc( proc,hwnd,msg,wp,lp );
				}
			}
		case NM_RETURN:case NM_DBLCLK:
			{
				int index=SendMessage( _gadget.hwnd(),LVM_GETNEXTITEM,-1,MAKELPARAM(LVNI_SELECTED,0) );
				if( index==-1 ){
					select( items()->selected() );
				}else{
					BBListBox::select(index);
					emit( BBEvent::GADGET_ACTION,index );
				}
			}
			return 0;
		}
	}
	return CallWindowProc( proc,hwnd,msg,wp,lp );
}
