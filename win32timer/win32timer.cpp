
#include "win32timer.h"

static DWORD thread_id;

Win32TimerDriver win32TimerDriver;

void Win32Timer::proc( UINT id,UINT msg,DWORD user,DWORD u1,DWORD u2 ){
	Win32Timer *t=(Win32Timer*)user;

	PostThreadMessage( thread_id,WM_BBTIMER,++t->_ticks,(LPARAM)t );
}

Win32Timer::Win32Timer( int period ):_period(period),_ticks(0),_paused(false){
	_timer=timeSetEvent( _period,0,proc,(DWORD)this,TIME_PERIODIC );
}

Win32Timer::~Win32Timer(){
	if( _timer ) timeKillEvent( _timer );
}

int Win32Timer::ticks(){
	return _ticks;
}

void Win32Timer::reset(){
	_ticks=0;
	BBTimer::reset();
}

void Win32Timer::setPaused( bool paused ){
	if( paused==_paused ) return;
	if( _paused=paused ){
		timeKillEvent( _timer );
		_timer=0;
	}else{
		_timer=timeSetEvent( _period,0,proc,(DWORD)this,TIME_PERIODIC );
	}
	BBTimer::setPaused( _paused );
}

Win32TimerDriver::Win32TimerDriver(){
	reg( "Win32TimerDriver" );
}

bool Win32TimerDriver::startup(){

	thread_id=GetCurrentThreadId();

	timeBeginPeriod(1);

	bbSetTimerDriver(this);

	return true;
}

void Win32TimerDriver::shutdown(){

	timeEndPeriod(1);
}

BBTimer *Win32TimerDriver::createTimer( float freq ){
	if( freq<=0 ) return 0;
	Win32Timer *t=new Win32Timer( (int)(1000/freq) );
	autoRelease(t);
	return t;
}

