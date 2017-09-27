
#include "app.h"

struct BBNullDebugger : public BBDebugger{
public:
	void debugRun(){}
	void debugStop(){}
	void debugStmt( int srcpos,const char *file ){}
	void debugEnter( void *frame,void *env,const char *func ){}
	void debugLeave(){}
	void debugLog( const char *msg ){}
};

static BBNullDebugger _nullDebugger;

BBDebugger *bbDebugger=&_nullDebugger;

void bbStop(){
	BBEvent ev={BBEvent::DEBUG_STOP};
	bbEmitEvent( &ev );
	bbPollEvent();
}

void bbRuntimeError( BBString *err ){
	bbError( err->c_str() );
}

void bbDebugStmt( int pos,const char *file ){
	bbDebugger->debugStmt( pos,file );
	bbNextEvent( 0,0,false );
}

void bbDebugEnter( void *frame,void *env,const char *func ){
	bbDebugger->debugEnter( frame,env,func );
}

void bbDebugLeave(){
	bbDebugger->debugLeave();
}

void bbDebugLog( BBString *msg ){
	bbDebugger->debugLog( msg->c_str() );
}
