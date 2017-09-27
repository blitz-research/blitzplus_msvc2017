
#ifndef WIN32GLSCREEN_H
#define WIN32GLSCREEN_H

#include "../win32gl/win32gl.h"

#include "../win32screen/win32screen.h"

class Win32GLScreen : public Win32Screen,public Win32WndProc{
	Win32GLGraphics *_graphics;
	DEVMODE _devmode;
	int _flags;
protected:
	~Win32GLScreen();
public:
	Win32GLScreen( int w,int h,int fmt,int flags );

	BBGraphics *graphics();

	void flip( bool sync );

	LRESULT wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc );
};

class Win32GLScreenDriver : public BBScreenDriver{
public:
	Win32GLScreenDriver();

	bool		startup();
	void		shutdown();

	int			screenModes();
	void		enumScreenMode( int n,int *w,int *h,int *fmt );

	BBScreen*	createScreen( int w,int h,int fmt,int flags );
	BBScreen*	desktopScreen();
};

extern Win32GLScreenDriver win32GLScreenDriver;
static Win32GLScreenDriver *_win32GLScreenDriver=&win32GLScreenDriver;

#endif