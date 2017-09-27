
#ifndef WIN32GLCANVAS_H
#define WIN32GLCANVAS_H

#include "../win32gl/win32gl.h"

#include "../win32canvas/win32canvas.h"

class Win32GLCanvas : public Win32Canvas,public Win32WndProc{
	Win32GLGraphics *_graphics;
protected:
	~Win32GLCanvas();
public:
	Win32GLCanvas( BBGroup *group,int style );

	BBGraphics *graphics();
	void flip( bool sync );

	LRESULT wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc );
};

class Win32GLCanvasDriver : public BBCanvasDriver{
public:
	Win32GLCanvasDriver();

	bool startup();
	void shutdown();

	BBCanvas *createCanvas( BBGroup *group,int style );
};

extern Win32GLCanvasDriver win32GLCanvasDriver;
static Win32GLCanvasDriver *_win32GLCanvasDriver=&win32GLCanvasDriver;

#endif