
#ifndef WIN32DIB_H
#define WIN32DIB_H

#include "win32dibgraphics.h"

class Win32DIB : public BBModule{
public:
	Win32DIB();

	bool startup();
	void shutdown();
};

extern Win32DIB win32DIB;
static Win32DIB *_win32DIB=&win32DIB;

#endif