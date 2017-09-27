
#ifndef WIN32LISTBOX_H
#define WIN32LISTBOX_H

#include "../gui/listbox.h"

#include "win32gadget.h"

class Win32ListBox : public BBListBox,public Win32WndProc{
	Win32Gadget _gadget;
	int  updateSelected();
public:
	Win32ListBox( BBGroup *group,int style );

	void *query( int qid );

	void setFont( BBFont *font );
	void setText( BBString *text );
	void setShape( int x,int y,int w,int h );
	void setVisible( bool visible );
	void setEnabled( bool enabled );
	void activate();

	void setIconStrip( BBIconStrip *t );

	void clear();
	void add( BBString *item,int icon );
	void insert( int index,BBString *item,int icon );
	void modify( int index,BBString *item,int icon );
	void remove( int index );
	void select( int index );

	LRESULT wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc );
};

#endif
