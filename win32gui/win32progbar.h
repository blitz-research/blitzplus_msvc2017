
#ifndef WIN32PROGBAR_H
#define WIN32PROGBAR_H

#include "../gui/progbar.h"

#include "win32gadget.h"

class Win32ProgBar : public BBProgBar,public Win32WndProc{
	Win32Gadget _gadget;
public:
	Win32ProgBar( BBGroup *group,int style );

	void *query( int qid );

	void setFont( BBFont *font );
	void setText( BBString *text );
	void setShape( int x,int y,int w,int h );
	void setVisible( bool visible );
	void setEnabled( bool enabled );
	void activate();

	void setProgress( float t );

	LRESULT wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc );
};

#endif
