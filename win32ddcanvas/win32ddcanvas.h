
#ifndef WIN32DDCANVAS_H
#define WIN32DDCANVAS_H

#include "../win32dd/win32dd.h"

#include "../win32canvas/win32canvas.h"

class Win32DDCanvas : public Win32Canvas,public Win32WndProc{
	Win32DDGraphics *_graphics;
	IDirectDrawClipper *_clipper;
protected:
	~Win32DDCanvas();
public:
	Win32DDCanvas( BBGroup *group,int style );

	BBGraphics *graphics();
	void flip( bool sync );

	LRESULT wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc );
};

class Win32DDCanvasDriver : public BBCanvasDriver{
public:
	Win32DDCanvasDriver();

	bool startup();
	void shutdown();

	BBCanvas *createCanvas( BBGroup *group,int style );
};

extern Win32DDCanvasDriver win32DDCanvasDriver;
static Win32DDCanvasDriver *_win32DDCanvasDriver=&win32DDCanvasDriver;

#endif