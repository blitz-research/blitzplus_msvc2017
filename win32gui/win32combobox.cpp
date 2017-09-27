
#include "win32combobox.h"

#include "win32iconstrip.h"

Win32ComboBox::Win32ComboBox( BBGroup *group,int style ):BBComboBox(group,style){

	HWND parent=(HWND)group->query( BBQID_WIN32CLIENTHWND );

	int xstyle=0;
	int wstyle=WS_CHILD|CBS_DROPDOWNLIST;

	HWND hwnd=CreateWindowEx( xstyle,WC_COMBOBOXEX,0,wstyle,0,0,0,128,parent,0,GetModuleHandle(0),0 );

	if( !hwnd ){
		bbError( "Unable to create combobox" );
	}

	_gadget.setHwnd(hwnd);
	_gadget.setWndProc(this);
}

int Win32ComboBox::updateSelected(){
	int index=SendMessage( _gadget.hwnd(),CB_GETCURSEL,0,0 );
	if( index==CB_ERR ) index=-1;
	BBComboBox::select(index);
	return index;
}

void *Win32ComboBox::query( int qid ){
	if( void *p=_gadget.query( qid ) ) return p;
	return BBComboBox::query( qid );
}

void Win32ComboBox::setFont( BBFont *font ){
	_gadget.setFont(font);
	BBComboBox::setFont(font);
}

void Win32ComboBox::setText( BBString *text ){
	_gadget.setText(text);
	BBComboBox::setText(text);
}

void Win32ComboBox::setShape( int x,int y,int w,int h ){
	_gadget.setShape(x,y,w,128);
	BBComboBox::setShape(x,y,w,h); 
}

void Win32ComboBox::setVisible( bool visible ){
	_gadget.setVisible(visible);
	BBComboBox::setVisible(visible);
}

void Win32ComboBox::setEnabled( bool enabled ){
	_gadget.setEnabled(enabled);
	BBComboBox::setEnabled(enabled);
}

void Win32ComboBox::activate(){
	_gadget.activate();
	BBComboBox::activate();
}

void Win32ComboBox::setIconStrip( BBIconStrip *t ){
	if( Win32IconStrip *p=dynamic_cast<Win32IconStrip*>(t) ){
		SendMessage( _gadget.hwnd(),CBEM_SETIMAGELIST,0,(LPARAM)p->himagelist() );
		BBComboBox::setIconStrip(t);
	}
}

void Win32ComboBox::clear(){
	SendMessage( _gadget.hwnd(),CB_RESETCONTENT,0,0 );
	BBComboBox::clear();
}

void Win32ComboBox::add( BBString *item,int icon ){
	COMBOBOXEXITEM it={CBEIF_TEXT};
	it.iItem=-1;
	it.pszText=(char*)item->c_str();
	if( iconStrip() && icon>=0 ){
		it.mask|=CBEIF_IMAGE|CBEIF_SELECTEDIMAGE;
		it.iImage=it.iSelectedImage=icon;
	}
	SendMessage( _gadget.hwnd(),CBEM_INSERTITEM,0,(LPARAM)&it );
	BBComboBox::add( item,icon );
}

void Win32ComboBox::insert( int index,BBString *item,int icon ){
	COMBOBOXEXITEM it={CBEIF_TEXT};
	it.iItem=index;
	it.pszText=(char*)item->c_str();
	if( iconStrip() && icon>=0 ){
		it.mask|=CBEIF_IMAGE|CBEIF_SELECTEDIMAGE;
		it.iImage=it.iSelectedImage=icon;
	}
	SendMessage( _gadget.hwnd(),CBEM_INSERTITEM,0,(LPARAM)&it );
	BBComboBox::insert( index,item,icon );
	updateSelected();
}

void Win32ComboBox::modify( int index,BBString *item,int icon ){
	COMBOBOXEXITEM it={CBEIF_TEXT};
	it.iItem=index;
	it.pszText=(char*)item->c_str();
	if( iconStrip() && icon>=0 ){
		it.mask|=CBEIF_IMAGE|CBEIF_SELECTEDIMAGE;
		it.iImage=it.iSelectedImage=icon;
	}
	SendMessage( _gadget.hwnd(),CBEM_SETITEM,0,(LPARAM)&it );
	BBComboBox::modify( index,item,icon );
}

void Win32ComboBox::remove( int index ){
	SendMessage( _gadget.hwnd(),CBEM_DELETEITEM,(WPARAM)index,0 );
	BBComboBox::remove( index );
	updateSelected();
}

void Win32ComboBox::select( int index ){
	SendMessage( _gadget.hwnd(),CB_SETCURSEL,(WPARAM)index,0 );
	BBComboBox::select( index );
}

LRESULT Win32ComboBox::wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc ){
	switch( msg ){
	case WM_COMMAND:
		if( HIWORD(wp)==CBN_SELCHANGE ){
			int index=updateSelected();
			emit( BBEvent::GADGET_ACTION,index );
		}
		return 0;
	}
	return CallWindowProc( proc,hwnd,msg,wp,lp );
}
