
#ifdef WIN32
#define WIN32_LEAN_AND_MEAN

#include "win32time.h"

#include <windows.h>
#include <mmsystem.h>

void bbDelay( int ms ){
	if (ms<0) ms=0;
	Sleep(ms);
}

int bbMilliSecs(){
	return timeGetTime();
}

#endif