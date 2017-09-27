
#ifndef WIN32DESKTOP_H
#define WIN32DESKTOP_H

#include "../gui/desktop.h"

#include "win32gadget.h"

class Win32Desktop : public BBDesktop{
	HWND _hwnd;
public:
	Win32Desktop();

	void *query( int qid );

	void setFont( BBFont *font );
	void setText( BBString *text );
	void setShape( int x,int y,int w,int h );
	void setVisible( bool visible );
	void setEnabled( bool enabled );
	void activate();

	void clientShape( int *x,int *y,int *w,int *h );
};

#endif