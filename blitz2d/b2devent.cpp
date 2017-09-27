
#include "b2devent.h"

#include <string.h>

#include <stdio.h>

static BBEvent _curEvent;

static const int QUE_SIZE=32;
static const int QUE_MASK=QUE_SIZE-1;

B2DEventFilter b2dEventFilter;

static char key_state[256];
static int key_hits[256],key_que[QUE_SIZE],key_put,key_get;

static int _mz,_pz;
static char mouse_state[4];
static int mouse_hits[4],mouse_que[QUE_SIZE],mouse_put,mouse_get;

B2DEventFilter::B2DEventFilter(){
	reg( "B2DEventFilter" );
}

bool B2DEventFilter::startup(){
	insert( 1 );
	return true;
}

void B2DEventFilter::shutdown(){
	remove();
}

void B2DEventFilter::filter( const BBEvent *ev ){
	switch( ev->kind ){
	case BBEvent::KEY_DOWN:
		++key_hits[ev->data&255];
		key_state[ev->data&255]=1;
		break;
	case BBEvent::KEY_UP:
		key_state[ev->data&255]=0;
		break;
	case BBEvent::KEY_CHAR:
		if( key_put-key_get<QUE_SIZE ) key_que[key_put++&QUE_MASK]=ev->data;
		break;
	case BBEvent::MOUSE_DOWN:
		++mouse_hits[ev->data&3];
		mouse_state[ev->data&3]=1;
		if( mouse_put-mouse_get<QUE_SIZE ) mouse_que[mouse_put++&QUE_MASK]=ev->data;
		break;
	case BBEvent::MOUSE_UP:
		mouse_state[ev->data&3]=0;
		break;
	case BBEvent::MOUSE_WHEEL:
		_mz+=ev->data;
		break;
	}
	emit(ev);
}

int		b2dPeekEvent(){
	return bbNextEvent( 0,0,false );
}

int		b2dWaitEvent( int timeout ){
	memset( &_curEvent,0,sizeof(_curEvent) );
	return bbNextEvent( &_curEvent,timeout,true );
}

int		b2dEventID(){
	return _curEvent.kind;
}

int		b2dEventData(){
	return _curEvent.data;
}

int		b2dEventX(){
	return _curEvent.x;
}

int		b2dEventY(){
	return _curEvent.y;
}

int		b2dEventZ(){
	return _curEvent.z;
}

void*	b2dEventSource(){
	return _curEvent.source;
}

int		b2dKeyDown( int n ){
	bbPollEvent();
	return key_state[n&255];
}

int		b2dKeyHit( int n ){
	bbPollEvent();
	int t=key_hits[n&255];
	key_hits[n&255]=0;
	return t;
}

int		b2dGetKey(){
	bbPollEvent();
	return key_get==key_put ? 0 : key_que[key_get++&QUE_MASK];
}

int		b2dWaitKey(){
	bbPollEvent();
	while( key_put==key_get ){
		bbNextEvent( 0,-1,true );
	}
	return b2dGetKey();
}

void	b2dFlushKeys(){
	bbPollEvent();
	key_put=key_get=0;
	memset( key_hits,0,sizeof(key_hits) );
	memset( key_state,0,sizeof(key_state) );
}

int		b2dMouseZ(){
	bbPollEvent();
	return _mz;
}

int		b2dMouseZSpeed(){
	bbPollEvent();
	int n=_mz-_pz;
	_pz=_mz;
	return n;
}

int		b2dMouseDown( int n ){
	bbPollEvent();
	return mouse_state[n&3];
}

int		b2dMouseHit( int n ){
	bbPollEvent();
	int t=mouse_hits[n&3];
	mouse_hits[n&255]=0;
	return t;
}

int		b2dGetMouse(){
	bbPollEvent();
	return mouse_get==mouse_put ? 0 : mouse_que[mouse_get++&QUE_MASK];
}

int		b2dWaitMouse(){
	bbPollEvent();
	while( mouse_put==mouse_get ){
		bbNextEvent( 0,-1,true );
	}
	return b2dGetMouse();
}

void	b2dFlushMouse(){
	bbPollEvent();
	mouse_put=mouse_get=0;
	memset( mouse_hits,0,sizeof(mouse_hits) );
	memset( mouse_state,0,sizeof(mouse_state) );
}

