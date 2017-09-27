
#ifndef WIN32DDFULLSCREEN_H
#define WIN32DDFULLSCREEN_H

#include "win32ddscreen.h"

class Win32DDFullScreen : public Win32DDScreen,public Win32WndProc{
	int _fmt,_flags;
	Win32DDGraphics *_graphics;
	IDirectDrawClipper *_clipper;
protected:
	~Win32DDFullScreen();
public:
	Win32DDFullScreen( int w,int h,int fmt,int flags );

	BBGraphics *graphics();
	void activateScreen();
	void flip( bool sync );

	IDirectDrawSurface *primarySurface();

	LRESULT wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc );
};

#endif