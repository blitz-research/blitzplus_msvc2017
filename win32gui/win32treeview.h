
#ifndef WIN32TREEVIEW_H
#define WIN32TREEVIEW_H

#include "../gui/treeview.h"

#include "win32gadget.h"

#include <commctrl.h>

class Win32TreeViewNode;

class Win32TreeView : public BBTreeView,public Win32WndProc{
	Win32Gadget _gadget;
	Win32TreeViewNode *_root,*_selected;

public:
	Win32TreeView( BBGroup *group,int style );

	void *query( int qid );

	void setFont( BBFont *font );
	void setText( BBString *text );
	void setShape( int x,int y,int w,int h );
	void setVisible( bool visible );
	void setEnabled( bool enabled );
	void activate();

	void setIconStrip( BBIconStrip *t );

	BBTreeViewNode *root();
	BBTreeViewNode *selected();

	LRESULT wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc );
};

class Win32TreeViewNode : public BBTreeViewNode{
	HWND _tree;
	HTREEITEM _item;
	Win32TreeViewNode *_parent;
protected:
	~Win32TreeViewNode();
public:
	Win32TreeViewNode( HWND tree,HTREEITEM item,Win32TreeViewNode *parent );

	void select();
	void expand();
	void collapse();
	void modify( BBString *text,int icon );

	BBTreeViewNode *add( BBString *item,int icon );
	BBTreeViewNode *insert( int index,BBString *item,int icon );

	BBString *treeViewNodeText();
};

#endif