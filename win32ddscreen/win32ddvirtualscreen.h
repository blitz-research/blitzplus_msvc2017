
#ifndef WIN32DDVIRTUALSCREEN_H
#define WIN32DDVIRTUALSCREEN_H

#include "win32ddscreen.h"

class Win32DDVirtualScreen : public Win32DDScreen,public Win32WndProc{
	Win32DDGraphics *_graphics;
	IDirectDrawClipper *_clipper;
protected:
	~Win32DDVirtualScreen();
public:
	Win32DDVirtualScreen( int w,int h,int fmt,int flags );

	BBGraphics *graphics();

	void flip( bool sync );

	IDirectDrawSurface *primarySurface();

	LRESULT wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc );
};

#endif