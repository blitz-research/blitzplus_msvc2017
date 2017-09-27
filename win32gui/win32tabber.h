
#ifndef WIN32TABBER_H
#define WIN32TABBER_H

#include "../gui/tabber.h"

#include "win32gadget.h"

class Win32Tabber : public BBTabber,public Win32WndProc{
	Win32Gadget _tabber;
	Win32Gadget _client;
	int  updateSelected();
protected:
	~Win32Tabber();
public:
	Win32Tabber( BBGroup *group,int style );

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

	void clientShape( int *x,int *y,int *w,int *h );

	LRESULT wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc );
};

#endif
