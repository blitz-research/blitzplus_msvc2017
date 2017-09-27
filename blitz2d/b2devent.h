
#ifndef B2DEVENT_H
#define B2DEVENT_H

#include "b2dgraphics.h"

#include "../timer/timer.h"

int		b2dPeekEvent();
int		b2dWaitEvent( int timeout );
int		b2dEventID();
int		b2dEventData();
int		b2dEventX();
int		b2dEventY();
int		b2dEventZ();
void*	b2dEventSource();

int		b2dKeyDown( int n );
int		b2dKeyHit( int n );
int		b2dGetKey();
int		b2dWaitKey();
void	b2dFlushKeys();

int		b2dMouseZ();
int		b2dMouseZSpeed();
int		b2dMouseDown( int n );
int		b2dMouseHit( int n );
int		b2dGetMouse();
int		b2dWaitMouse();
void	b2dFlushMouse();

class B2DEventFilter : public BBEventFilter{
public:
	B2DEventFilter();

	bool startup();
	void shutdown();

	void filter( const BBEvent *ev );
};

extern B2DEventFilter b2dEventFilter;
static B2DEventFilter *_b2dEventFilter=&b2dEventFilter;

#endif