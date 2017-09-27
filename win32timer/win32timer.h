
#ifndef WIN32TIMER_H
#define WIN32TIMER_H
#define WIN32_LEAN_AND_MEAN

#include "../timer/timer.h"

#include "../win32event/win32event.h"

#include <windows.h>
#include <mmsystem.h>

class Win32Timer : public BBTimer{
	int _period;
	int _ticks;
	bool _paused;
	MMRESULT _timer;

	static void CALLBACK proc( UINT id,UINT msg,DWORD user,DWORD u1,DWORD u2 );

protected:
	~Win32Timer();
public:
	Win32Timer( int period );

	int		ticks();
	void	reset();
	void	setPaused( bool paused );
};

class Win32TimerDriver : public BBTimerDriver{
public:
	Win32TimerDriver();

	bool startup();
	void shutdown();

	BBTimer *createTimer( float freq );
};

extern Win32TimerDriver win32TimerDriver;
static Win32TimerDriver *_win32TimerDriver=&win32TimerDriver;

#endif