
#include "debug.h"

#include "../event/event.h"

void bbDebugStop(){
#ifdef _DEBUG
	BBEvent ev={BBEvent::DEBUG_STOP};
	bbPostEvent( &ev );
	bbPollEvent();
#endif
}

void bbDebugStmt( int pos,const char *file ){
#ifdef _DEBUG
	bbDebugger->debugStmt( pos,file );
	bbPollEvent();
#endif
}

void bbDebugEnter( void *frame,void *env,const char *func ){
#ifdef _DEBUG
	bbDebugger->debugEnter( frame,env,func );
#endif
}

void bbDebugLeave(){
#ifdef _DEBUG
	bbDebugger->debugLeave();
#endif
}

void bbDebugLog( BBString *msg ){
#ifdef _DEBUG
	bbDebugger->debugLog( msg->c_str() );
#endif
}
