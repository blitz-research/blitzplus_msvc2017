
#ifndef TIMER_H
#define TIMER_H

#include "../event/event.h"

class BBTimer : public BBEventSource{
	int _dticks;
	bool _paused;
public:
	BBTimer();

	virtual int		ticks()=0;
	virtual void	reset()=0;
	virtual void	setPaused( bool paused )=0;

	int				deltaTicks();

	bool			paused(){ return _paused; }

	void debug(){ _debug(this,"Timer"); }
};

class BBTimerDriver : public BBModule{
public:
	virtual BBTimer *createTimer( float freq )=0;
};

void		bbSetTimerDriver( BBTimerDriver *t );

BBTimer*	bbCreateTimer( float freq );
void		bbFreeTimer( BBTimer *timer );
int			bbWaitTimer( BBTimer *timer );
void		bbResetTimer( BBTimer *timer );
void		bbPauseTimer( BBTimer *timer );
void		bbResumeTimer( BBTimer *timer );
int			bbTimerTicks( BBTimer *timer );

#endif