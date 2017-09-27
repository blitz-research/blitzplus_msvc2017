
#include "event.h"

#include <stdio.h>

#define MAX_HOTKEYS 100

#include "../time/time.h"
#include "../debug/debug.h"

static bool _modal,_suspended,_stopped,_halted,_autoSuspend,_end;

static const int QUE_SIZE=256;
static const int QUE_MASK=QUE_SIZE-1;

static BBEventFilter *filters;

static BBEventDriver *_driver;

struct Hotkey{
	int scan,mod;
	BBEvent event;
};

static int n_hotkeys;
static Hotkey hotkeys[MAX_HOTKEYS];

class QueFilter : public BBEventFilter{
public:
	QueFilter();

	void filter( const BBEvent *ev );
};

void BBEventFilter::insert( int pri ){
	_pri=pri;
	BBEventFilter **prev,*curr;
	for( prev=&filters;curr=*prev;prev=&curr->_succ ){
		if( _pri>curr->_pri ) break;
	}
	_succ=curr;
	*prev=this;
}

void BBEventFilter::remove(){
	BBEventFilter **prev,*curr;
	for( prev=&filters;curr=*prev;prev=&curr->_succ ){
		if( curr==this ){
			*prev=_succ;
			return;
		}
	}
	//ERROR!
}

void BBEventFilter::emit( const BBEvent *ev ){
	(_succ ? _succ : filters)->filter(ev);
}

struct EventQue{

	int _put,_get;
	BBEvent _que[QUE_SIZE];

public:
	EventQue();

	int			size();
	void		clear();
	bool		empty();
	bool		add( const BBEvent *ev );
	int			next( BBEvent *ev,bool remove );
	void		flush( int mask,BBEventSource *src );

	BBEvent*	first();
	BBEvent*	last();
	BBEvent*	find( const BBEvent *ev );
};

EventQue::EventQue():_put(0),_get(0){
}

int  EventQue::size(){
	return _put-_get;
}

void EventQue::clear(){
	_get=_put=0;
}

bool EventQue::empty(){
	return _get==_put;
}

bool EventQue::add( const BBEvent *ev ){
	if( _put-_get==QUE_SIZE ) return false;
	_que[_put++ & QUE_MASK]=*ev;
	return true;
}

int  EventQue::next( BBEvent *ev,bool remove ){
	if( empty() ) return 0;
	BBEvent *q=_que+(_get&QUE_MASK);
	if( ev ) *ev=*q;
	if( remove ) ++_get;
	return q->kind;
}

void EventQue::flush( int mask,BBEventSource *src ){
	int _out=_put;
	if( mask==-1 ) mask=0xffffff00;
	for( int k=_get;k!=_put;++k ){
		BBEvent *q=&_que[k&QUE_MASK];
		if( !src || q->source==src ){
			if( mask & 0xff ){
				if( q->kind==mask ) continue;
			}else{
				if( q->kind&mask ) continue;
			}
		}
		_que[_out++ & QUE_MASK]=*q;
	}
	_get=_put;
	_put=_out;
}

BBEvent *EventQue::first(){
	return _put!=_get ? _que+(_get&QUE_MASK) : 0;
}

BBEvent *EventQue::last(){
	return _put!=_get ? _que+((_put-1)&QUE_MASK) : 0;
}

BBEvent *EventQue::find( const BBEvent *ev ){
	for( int k=_get;k!=_put;++k ){
		BBEvent *q=_que+(k&QUE_MASK);
		if( q->kind==ev->kind && q->source==ev->source ) return q;
	}
	return 0;
}

static QueFilter que_filter;
static EventQue event_que,idle_que;

QueFilter::QueFilter(){
	reg( "BBQueFilter" );
	filters=this;
	_suspended=_stopped=_halted=_autoSuspend=_end=false;
}

void QueFilter::filter( const BBEvent *ev ){
	bbPostEvent( ev );
}

static int nextEvent( BBEvent *ev,int timeout,bool remove ){

	int expire=timeout>0 ? bbMilliSecs()+timeout : 0;

	if( int n=event_que.next( ev,remove ) ) return n;

	if( !idle_que.empty() ){

		_driver->wait(0);

		if( int n=event_que.next( ev,remove ) ) return n;
		return idle_que.next( ev,remove );
	}

	for(;;){

		_driver->wait(timeout);

		if( int n=event_que.next( ev,remove ) ) return n;
		if( int n=idle_que.next( ev,remove ) ) return n;

		if( timeout<0 ) continue;
		if( timeout ) timeout=expire-bbMilliSecs();
		if( timeout<=0 ) return 0;
	}
}

void bbSetEventDriver( BBEventDriver *t ){
	_driver=t;
}

void bbAutoSuspend( int enable ){
	_autoSuspend=!!enable;
}

void bbFlushEvents( int mask,BBEventSource *src ){
	int n;
	do{
		bbPollEvent();
		n=event_que.size()+idle_que.size();
		event_que.flush( mask,src );
		idle_que.flush( mask,src );
	}while( event_que.size()+idle_que.size()!=n );
}

void bbPostEvent( const BBEvent *ev ){
	switch( ev->kind ){
	case BBEvent::DEBUG_END:
		_halted=_end=true;
		event_que.add(ev);
		return;
	case BBEvent::DEBUG_STOP:
		if( _stopped ) return;
		_stopped=true;
		_halted=_stopped || (_suspended && _autoSuspend);
		bbDebugger->debugStop();
		return;
	case BBEvent::DEBUG_RUN:
		if( !_stopped ) return;
		_stopped=false;
		_halted=_stopped || (_suspended && _autoSuspend);
		bbDebugger->debugRun();
		return;
	case BBEvent::APP_SUSPEND:
		if( _suspended ) return;
		_suspended=true;
		_halted=_stopped || (_suspended && _autoSuspend);
		event_que.add(ev);
		return;
	case BBEvent::APP_RESUME:
		if( !_suspended ) return;
		_suspended=false;
		_halted=_stopped || (_suspended && _autoSuspend);
		event_que.add(ev);
		return;
	case BBEvent::MOUSE_MOVE:
		if( BBEvent *q=event_que.last() ){
			if( q->kind==BBEvent::MOUSE_MOVE &&	q->source==ev->source ){
				*q=*ev;
				return;
			}
		}
		event_que.add(ev);
		return;
	case BBEvent::TIMER_TICK:
		if( BBEvent *q=idle_que.find( ev ) ) *q=*ev;
		else idle_que.add(ev);
		return;
	default:
		event_que.add(ev);
	}
}

void bbEmitEvent( const BBEvent *ev ){
	que_filter.emit(ev);
}

void bbPollEvent(){

	if( _modal ) return;

	_driver->wait(0);

	while( _halted ){
		if( _end ) bbEnd();
		_driver->wait(-1);
	}
}

int bbNextEvent( BBEvent *ev,int timeout,bool remove ){

	int kind=nextEvent( ev,timeout,remove );

	switch( kind ){
	case BBEvent::APP_BEGINMODAL:
		_modal=true;
		break;
	case BBEvent::APP_ENDMODAL:
		_modal=false;
		break;
	}

	while( _halted ){
		if( _end ) bbEnd();
		kind=nextEvent( ev,-1,true );
	}

	return kind;
}

BBEventSource::~BBEventSource(){
}

void BBEventSource::destroy(){
	if( bbAppState()==BBAPP_RUNNING ) bbFlushEvents( -1,this );
	BBResource::destroy();
}

void BBEventSource::emit( int kind,int data,int x,int y,int z ){
	BBEvent ev={kind,data,x,y,z,this};
	bbEmitEvent( &ev );
}

void bbHotKeyEvent( int scan,int mod,int kind,int data,int x,int y,int z,BBEventSource *src ){
	BBEvent ev={kind,data,x,y,z,src};
	for( int k=0;k<n_hotkeys;++k ){
		if( hotkeys[k].scan==scan && hotkeys[k].mod==mod ){
			hotkeys[k].event=ev;
			return;
		}
	}
	if( n_hotkeys==MAX_HOTKEYS ){
		bbError( "Too many hotkey events registered" );
	}
	hotkeys[n_hotkeys].scan=scan;
	hotkeys[n_hotkeys].mod=mod;
	hotkeys[n_hotkeys].event=ev;
	++n_hotkeys;
}

int bbEmitHotKeyEvent( int scan,int mod ){
	for( int k=0;k<n_hotkeys;++k ){
		if( hotkeys[k].scan!=scan || hotkeys[k].mod!=mod ) continue;
		bbEmitEvent( &hotkeys[k].event );
		return true;
	}
	return false;
}
