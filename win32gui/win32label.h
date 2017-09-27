
#ifndef WIN32LABEL_H
#define WIN32LABEL_H

#include "../gui/label.h"

#include "win32gadget.h"

class Win32Label : public BBLabel,public Win32WndProc{
	Win32Gadget _gadget;
public:
	Win32Label( BBGroup *group,int style );

	void *query( int qid );

	void setFont( BBFont *font );
	void setText( BBString *text );
	void setShape( int x,int y,int w,int h );
	void setVisible( bool visible );
	void setEnabled( bool enabled );
	void activate();

	LRESULT wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc );
};

#endif