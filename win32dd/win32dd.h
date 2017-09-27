
#ifndef WIN32DD_H
#define WIN32DD_H

#include "win32ddgraphics.h"

class Win32DD : public BBModule{
	HMODULE _mod;
	int _version;
	IDirectDraw *_dirDraw;
	IDirectDraw2 *_dirDraw2;
	IDirectDraw4 *_dirDraw4;
	IDirectDraw7 *_dirDraw7;
public:
	Win32DD();

	bool			startup();
	void			shutdown();

	IDirectDraw*	directDraw()const{ return _dirDraw; }
	IDirectDraw2*	directDraw2()const{ return _dirDraw2; }
	IDirectDraw4*	directDraw4()const{ return _dirDraw4; }
	IDirectDraw7*	directDraw7()const{ return _dirDraw7; }

	Win32DDGraphics*desktopGraphics();

	int dd_version()const{ return _version; }

	static void		pixelFormat( int fmt,DDPIXELFORMAT *pf );
	static int		pixelFormat( const DDPIXELFORMAT *pf,bool fail );
};

extern Win32DD win32DD;
static Win32DD *_win32DD=&win32DD;

#endif