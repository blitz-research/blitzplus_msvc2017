
#ifndef EVENT_H
#define EVENT_H

#include "../app/app.h"

struct BBEvent{
	enum{
		KEY_MASK=0x100,KEY_DOWN,KEY_UP,KEY_CHAR,KEY_OKAY,KEY_CANCEL,
		MOUSE_MASK=0x200,MOUSE_DOWN,MOUSE_UP,MOUSE_MOVE,MOUSE_WHEEL,MOUSE_ENTER,MOUSE_LEAVE,
		GADGET_MASK=0x400,GADGET_ACTION,GADGET_ACTIVATE,
		WINDOW_MASK=0x800,WINDOW_MOVE,WINDOW_SIZE,WINDOW_CLOSE,WINDOW_ACTIVATE,
		MENU_MASK=0x1000,MENU_ACTION,
		APP_MASK=0x2000,APP_SUSPEND,APP_RESUME,APP_DISPLAYCHANGE,APP_BEGINMODAL,APP_ENDMODAL,
		TIMER_MASK=0x4000,TIMER_TICK,

		DEBUG_MASK=0x40000000,DEBUG_STOP,DEBUG_RUN,DEBUG_END,
		SYSTEM_MASK=0x80000000,SYSTEM_WAIT,SYSTEM_EVENT
	};
	int kind,data,x,y,z;
	void *source;
};

class BBEventFilter : public BBModule{
	int _pri;
	BBEventFilter *_succ;

protected:
	void insert( int pri );
	void remove();

public:
	void emit( const BBEvent *event );

	virtual void filter( const BBEvent *event )=0;
};

class BBEventSource : public BBResource{
protected:
	~BBEventSource();
	void destroy();
public:
	void emit( int kind,int data=0,int x=0,int y=0,int z=0 );
};

class BBEventDriver : public BBModule{
public:
	virtual void wait( int timeout )=0;
};

void	bbSetEventDriver( BBEventDriver *t );

void	bbPostEvent( const BBEvent *event );
void	bbEmitEvent( const BBEvent *event );
int		bbNextEvent( BBEvent *event,int timout,bool remove );
void	bbPollEvent();

void	bbAutoSuspend( int enable );
void	bbFlushEvents( int mask,BBEventSource *src );

void	bbHotKeyEvent( int scan,int mod,int ev_kind,int ev_data=0,int ev_x=0,int ev_y=0,int ev_z=0,BBEventSource *ev_src=0 );
int		bbEmitHotKeyEvent( int scan,int mod );

#endif
