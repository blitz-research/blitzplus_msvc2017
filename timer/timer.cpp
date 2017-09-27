
#include "timer.h"

static BBTimerDriver *_driver;

BBTimer::BBTimer():_dticks(0),_paused(false){
}

void BBTimer::reset(){
	_dticks=0;
}

void BBTimer::setPaused( bool t ){
	_paused=t;
}

int BBTimer::deltaTicks(){
	int n=ticks()-_dticks;
	_dticks+=n;
	return n;
}

void		bbSetTimerDriver( BBTimerDriver *t ){
	_driver=t;
}

BBTimer*	bbCreateTimer( float freq ){
	return _driver->createTimer( freq );
}

void		bbFreeTimer( BBTimer *timer ){
	if( !timer ) return;
	timer->debug();
	timer->release();
}

int			bbWaitTimer( BBTimer *timer ){
	timer->debug();

	if( timer->paused() ) bbError( "WaitTimer used with paused timer" );

	bbPollEvent();
	int n=timer->deltaTicks();

	while( !n ){
		bbNextEvent( 0,-1,true );
		n=timer->deltaTicks();
	}

	return n;
}

void		bbResetTimer( BBTimer *timer ){
	timer->debug();
	timer->reset();
}

void		bbPauseTimer( BBTimer *timer ){
	timer->debug();
	timer->setPaused(true);
}

void		bbResumeTimer( BBTimer *timer ){
	timer->debug();
	timer->setPaused(false);
}

int			bbTimerTicks( BBTimer *timer ){
	timer->debug();
	return timer->ticks();
}

