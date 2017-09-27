
#ifndef WIN32GDISCREEN_H
#define WIN32GDISCREEN_H

#include "../Win32GDI/Win32GDI.h"

#include "../win32screen/win32screen.h"

class Win32GDIScreen : public Win32Screen,public Win32WndProc{
	Win32GDIGraphics *_graphics;
	DEVMODE _devmode;
	int _type;
protected:
	~Win32GDIScreen();
public:
	enum{
		DESKTOP=1,
		VIRTUAL=2,
		FULLSCREEN=3
	};

	Win32GDIScreen( int w,int h,int fmt,int type );

	BBGraphics*	graphics();

	void		flip( bool sync );

	int			mouseX();
	int			mouseY();

	LRESULT wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc );
};

class Win32GDIScreenDriver : public BBScreenDriver{
public:
	Win32GDIScreenDriver();

	bool		startup();
	void		shutdown();

	int			screenModes();
	void		enumScreenMode( int n,int *w,int *h,int *fmt );

	BBScreen*	createScreen( int w,int h,int fmt,int flags );
	BBScreen*	desktopScreen();
};

extern Win32GDIScreenDriver win32GDIScreenDriver;
static Win32GDIScreenDriver *_win32GDIScreenDriver=&win32GDIScreenDriver;


#endif