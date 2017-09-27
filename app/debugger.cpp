
#include "app.h"
#include <stdio.h>

#ifdef WIN32
#define WIN32_LEAN_AND_MEAN
#include <windows.h>
#endif

static BBDebugger _debugger;

BBDebugger *bbDebugger=&_debugger;

void BBDebugger::debugRun(){
}

void BBDebugger::debugStop(){
}

void BBDebugger::debugStmt( int srcpos,const char *file ){
}

void BBDebugger::debugEnter( void *frame,void *env,const char *func ){
}

void BBDebugger::debugLeave(){
}

void BBDebugger::debugLog( const char *msg ){
}

void BBDebugger::debugMsg( const char *msg,bool serious ){

#ifdef WIN32
	BBString *title=bbAppTitle();
	MessageBox( GetActiveWindow(),msg,title->c_str(),MB_OK|MB_TOPMOST|MB_SETFOREGROUND );
	title->release();
#endif
}


