
#ifndef WIN32WINDOW_H
#define WIN32WINDOW_H

#include "../gui/window.h"

#include "win32gadget.h"

class Win32Window : public BBWindow,public Win32WndProc{
	int _minw,_minh;
	Win32Gadget _window,_client;
	HWND _status;
	BBScreen *_screen;
protected:
	~Win32Window();
public:
	Win32Window( BBGroup *group,int style );

	void *query( int qid );

	void setFont( BBFont *font );
	void setText( BBString *text );
	void setShape( int x,int y,int w,int h );
	void setVisible( bool visible );
	void setEnabled( bool enabled );
	void activate();

	void clientShape( int *x,int *y,int *w,int *h );

	void updateMenu();
	void activateWindow();
	void setMinSize( int w,int h );
	void setStatusText( BBString *text );
	void minimize();
	void maximize();
	void restore();

	LRESULT wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc );
};

#endif
