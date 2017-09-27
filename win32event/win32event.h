
#ifndef WIN32EVENT_H
#define WIN32EVENT_H
#define WIN32_LEAN_AND_MEAN

#include <windows.h>

#include "../event/event.h"

enum{
	WM_BBEVENT=WM_APP,
	WM_BBSTOP,WM_BBRUN,WM_BBEND,WM_BBTIMER
};

class Win32CatchFilter : public BBEventFilter{
public:
	Win32CatchFilter();

	bool startup();
	void shutdown();

	void filter( const BBEvent *ev );
};

class Win32EventDriver : public BBEventDriver{
public:
	Win32EventDriver();

	bool startup();
	void shutdown();

	void wait( int timeout );
};

extern Win32EventDriver win32EventDriver;
extern Win32CatchFilter win32CatchFilter;

static Win32EventDriver *_win32EventDriver=&win32EventDriver;
static Win32CatchFilter *_win32CatchFilter=&win32CatchFilter;

#endif
