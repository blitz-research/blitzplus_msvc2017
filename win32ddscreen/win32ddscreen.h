
#ifndef WIN32DDSCREEN_H
#define WIN32DDSCREEN_H

#include "../win32dd/win32dd.h"
#include "../win32screen/win32screen.h"

class Win32DDScreen : public Win32Screen{
	DDGAMMARAMP _gammaRamp;
public:
	Win32DDScreen();

	void setGamma( int r,int g,int b,int dr,int dg,int db );
	void getGamma( int r,int g,int b,int *dr,int *dg,int *db );
	void updateGamma( bool calibrate );

	virtual IDirectDrawSurface *primarySurface()=0;
};

#include "win32ddfullscreen.h"
#include "win32ddvirtualscreen.h"
#include "win32dddesktopscreen.h"

class Win32DDScreenDriver : public BBScreenDriver{
public:
	Win32DDScreenDriver();

	int			scanLine();
	void		vwait( int frames );
	int			availVidMem();
	int			totalVidMem();

	int			screenModes();
	void		enumScreenMode( int n,int *w,int *h,int *fmt );
	BBScreen*	createScreen( int w,int h,int fmt,int flags );
	BBScreen*	desktopScreen();

	bool		startup();
	void		shutdown();
};

extern Win32DDScreenDriver win32DDScreenDriver;
static Win32DDScreenDriver *_win32DDScreenDriver=&win32DDScreenDriver;

#endif