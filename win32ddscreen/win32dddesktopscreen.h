
#ifndef WIN32DDDESKTOPSCREEN_H
#define WIN32DDDESKTOPSCREEN_H

#include "win32ddscreen.h"

class Win32DDDesktopScreen : public Win32DDScreen{
protected:
	~Win32DDDesktopScreen();
public:
	Win32DDDesktopScreen();

	BBGraphics *graphics();
	void flip( bool sync );
	int  mouseX();
	int  mouseY();

	IDirectDrawSurface *primarySurface();
};

#endif