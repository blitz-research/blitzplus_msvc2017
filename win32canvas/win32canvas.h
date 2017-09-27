
#ifndef WIN32CANVAS_H
#define WIN32CANVAS_H

#include "../canvas/canvas.h"

#include "../win32gui/win32gui.h"

class Win32Canvas : public BBCanvas{
	BBGraphics *_graphics;
protected:
	Win32Gadget _gadget;
	LRESULT wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc );
	~Win32Canvas();
	void setGraphics( BBGraphics *g );
public:
	Win32Canvas( BBGroup *group,int style );

	void *query( int qid );

	void setFont( BBFont *font );
	void setText( BBString *text );
	void setShape( int x,int y,int w,int h );
	void setVisible( bool visible );
	void setEnabled( bool enabled );
	void activate();

	void clientShape( int *x,int *y,int *w,int *h );

	BBGraphics *graphics();
	void setPointer( int n );
	void moveMouse( int x,int y );
	int  mouseX();
	int  mouseY();
};

#endif