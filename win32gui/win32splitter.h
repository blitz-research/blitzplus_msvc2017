
#ifndef WIN32SPLITTER_H
#define WIN32SPLITTER_H

#include "../gui/splitter.h"

#include "win32gadget.h"

class Win32Splitter : public BBSplitter,public Win32WndProc{
	Win32Gadget _gadget;
	int _style,_changing,_mx,_my,_divx,_divy,_divw,_divh;
protected:
	~Win32Splitter();
public:
	Win32Splitter( BBGroup *group,int style );

	void *query( int qid );

	void setFont( BBFont *font );
	void setText( BBString *text );
	void setShape( int x,int y,int w,int h );
	void setVisible( bool visible );
	void setEnabled( bool enabled );
	void activate();

	void clientShape( int *x,int *y,int *w,int *h );

	void setDivShape( int x,int y,int w,int h );

	LRESULT wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc );
};

#endif
