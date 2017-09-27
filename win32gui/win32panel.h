
#ifndef WIN32PANEL_H
#define WIN32PANEL_H

#include "../gui/panel.h"

#include "win32gadget.h"

class Win32Panel : public BBPanel,public Win32WndProc{
	Win32Gadget _panel;
	HBRUSH _brush;
	HBITMAP _bitmap;
	int _bgw,_bgh;
protected:
	~Win32Panel();
public:
	Win32Panel( BBGroup *group,int style );
	
	void *query( int qid );

	void setFont( BBFont *font );
	void setText( BBString *text );
	void setShape( int x,int y,int w,int h );
	void setVisible( bool visible );
	void setEnabled( bool enabled );
	void activate();

	void clientShape( int *x,int *y,int *w,int *h );

	void setBackgroundImage( BBGraphics *g );
	void setBackgroundColor( int r,int g,int b );

	LRESULT wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc );
};

#endif
