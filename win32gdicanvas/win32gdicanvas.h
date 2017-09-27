
#ifndef WIN32GDICANVAS_H
#define WIN32GDICANVAS_H

#include "../win32gdi/win32gdi.h"

#include "../win32canvas/win32canvas.h"

class Win32GDICanvas : public Win32Canvas,public Win32WndProc{
	Win32GDIGraphics *_graphics;
protected:
	~Win32GDICanvas();
public:
	Win32GDICanvas( BBGroup *group,int style );

	BBGraphics *graphics();
	void flip( bool sync );

	LRESULT wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc );
};

class Win32GDICanvasDriver : public BBCanvasDriver{
public:
	Win32GDICanvasDriver();

	bool startup();
	void shutdown();

	BBCanvas *createCanvas( BBGroup *group,int style );
};

extern Win32GDICanvasDriver win32GDICanvasDriver;
static Win32GDICanvasDriver *_win32GDICanvasDriver=&win32GDICanvasDriver;

#endif