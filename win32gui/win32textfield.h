
#ifndef WIN32TEXTFIELD_H
#define WIN32TEXTFIELD_H

#include "../gui/textfield.h"

#include "win32gadget.h"

class Win32TextField : public BBTextField,public Win32WndProc{
	Win32Gadget _gadget;
public:
	Win32TextField( BBGroup *group,int style );

	void *query( int qid );

	void setFont( BBFont *font );
	void setText( BBString *text );
	void setShape( int x,int y,int w,int h );
	void setVisible( bool visible );
	void setEnabled( bool enabled );
	void activate();

	BBString *textFieldText();

	LRESULT wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc );
};

#endif
