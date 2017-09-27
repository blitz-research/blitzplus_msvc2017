
#include "win32treeview.h"

#include "win32iconstrip.h"

#include <commctrl.h>

Win32TreeView::Win32TreeView( BBGroup *group,int style ):BBTreeView(group,style),
_selected(0){

	HWND parent=(HWND)group->query( BBQID_WIN32CLIENTHWND );

	int xstyle=WS_EX_CLIENTEDGE;
	int wstyle=WS_CHILD|TVS_HASLINES|TVS_HASBUTTONS|TVS_LINESATROOT|TVS_SHOWSELALWAYS;

	HWND hwnd=CreateWindowEx( xstyle,WC_TREEVIEW,"",wstyle,0,0,0,0,parent,0,GetModuleHandle(0),0 );

	_root=new Win32TreeViewNode( hwnd,TVI_ROOT,0 );

	_gadget.setHwnd(hwnd);
	_gadget.setWndProc(this);
}

void *Win32TreeView::query( int qid ){
	if( void *p=_gadget.query( qid ) ) return p;
	return BBTreeView::query( qid );
}

void Win32TreeView::setFont( BBFont *font ){
	_gadget.setFont(font);
	BBTreeView::setFont(font);
}

void Win32TreeView::setText( BBString *text ){
	_gadget.setText(text);
	BBTreeView::setText(text);
}

void Win32TreeView::setShape( int x,int y,int w,int h ){
	_gadget.setShape(x,y,w,h);
	BBTreeView::setShape(x,y,w,h); 
}

void Win32TreeView::setVisible( bool visible ){
	_gadget.setVisible(visible);
	BBTreeView::setVisible(visible);
}

void Win32TreeView::setEnabled( bool enabled ){
	_gadget.setEnabled(enabled);
	BBTreeView::setEnabled(enabled);
}

void Win32TreeView::activate(){
	_gadget.activate();
	BBTreeView::activate();
}


void Win32TreeView::setIconStrip( BBIconStrip *t ){
	if( Win32IconStrip *p=dynamic_cast<Win32IconStrip*>(t) ){
		SendMessage( _gadget.hwnd(),TVM_SETIMAGELIST,0,(LPARAM)p->himagelist() );
		BBTreeView::setIconStrip(t);
	}
}


BBTreeViewNode *Win32TreeView::root(){
	return _root;
}

BBTreeViewNode *Win32TreeView::selected(){
	return _selected;
}

LRESULT Win32TreeView::wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc ){

	if( msg!=WM_NOTIFY ) return CallWindowProc( proc,hwnd,msg,wp,lp );

	NMTREEVIEW *nm=(NMTREEVIEW*)lp;
	if( nm->hdr.code!=TVN_SELCHANGED ) return 0;

	if( nm->itemNew.hItem==TVI_ROOT ) _selected=_root;
	else _selected=(Win32TreeViewNode*)nm->itemNew.lParam;

	emit( BBEvent::GADGET_ACTION );
	return 0;
}

Win32TreeViewNode::Win32TreeViewNode( HWND tree,HTREEITEM item,Win32TreeViewNode *parent ):
BBTreeViewNode(parent),_tree(tree),_item(item),_parent(parent){
}

Win32TreeViewNode::~Win32TreeViewNode(){
	if( _item ) TreeView_DeleteItem( _tree,_item );
}

void Win32TreeViewNode::select(){
	if( _item ) TreeView_SelectItem( _tree,_item );
}

void Win32TreeViewNode::modify( BBString *str,int icon ){

	TVITEM it;
	it.mask=TVIF_TEXT;
	it.pszText=(char*)str->c_str();
	it.hItem=_item;
	if (icon>-1) {it.mask|=TVIF_IMAGE|TVIF_SELECTEDIMAGE;it.iImage=icon;it.iSelectedImage=icon+0;}
//	if (icon>-1) {it.mask|=TVIF_IMAGE;it.iImage=icon;}
	TreeView_SetItem( _tree,&it );
}

BBTreeViewNode *Win32TreeViewNode::add( BBString *str,int icon ){

	TVINSERTSTRUCT it;
	it.hParent=_item;
	it.hInsertAfter=TVI_LAST;
	it.item.mask=TVIF_TEXT;
	it.item.pszText=(char*)str->c_str();

	HTREEITEM item=TreeView_InsertItem( _tree,&it );
	if( !item ) return 0;

	Win32TreeViewNode *node=new Win32TreeViewNode( _tree,item,this );

	TVITEM tv={TVIF_PARAM};
	tv.lParam=(LPARAM)node;
	tv.hItem=item;
	if (icon>-1) {tv.mask|=TVIF_IMAGE|TVIF_SELECTEDIMAGE;tv.iImage=icon;tv.iSelectedImage=icon+0;}
	TreeView_SetItem( _tree,&tv );

	return node;
}

BBTreeViewNode *Win32TreeViewNode::insert( int index,BBString *str,int icon ){

	HTREEITEM p;

	if( index<=0 ){
		p=TVI_FIRST;
	}else if( index>=countKids() ){
		p=TVI_LAST;
	}else{
		p=TreeView_GetChild( _tree,_item );
		while( --index ){
			p=TreeView_GetNextSibling( _tree,p ); 
		}
	}

	TVINSERTSTRUCT it;
	it.hParent=_item;
	it.hInsertAfter=p;
	it.item.mask=TVIF_TEXT;
	it.item.pszText=(char*)str->c_str();

	HTREEITEM item=TreeView_InsertItem( _tree,&it );
	if( !item ) return 0;

	Win32TreeViewNode *node=new Win32TreeViewNode( _tree,item,this );

	TVITEM tv={TVIF_PARAM};
	tv.hItem=item;
	tv.lParam=(LPARAM)node;
	if (icon>-1) {tv.mask|=TVIF_IMAGE|TVIF_SELECTEDIMAGE;tv.iImage=icon;tv.iSelectedImage=icon+0;}
	TreeView_SetItem( _tree,&tv );

	return node;
}


void Win32TreeViewNode::expand(){
	TreeView_Expand( _tree,_item,TVE_EXPAND );
}

void Win32TreeViewNode::collapse(){
	TreeView_Expand( _tree,_item,TVE_COLLAPSE );
}

BBString *Win32TreeViewNode::treeViewNodeText(){
	char buf[1024];
	TVITEM tv={TVIF_TEXT};
	tv.hItem=_item;
	tv.pszText=buf;
	tv.cchTextMax=1024;
	TreeView_GetItem( _tree,&tv );
	return new BBString( buf,strlen(buf) );
}
