
#include "app.h"

#include <stdio.h>
#include <stdarg.h>
#include <stdlib.h>
#include <setjmp.h>

static int state=BBAPP_READY;

static BBString *cmd_line;

static void startup(){

	BBModule::setDriver( "Gui","Native" );
	BBModule::setDriver( "Audio","Native" );
	BBModule::setDriver( "Screen","DirectDraw" );
	BBModule::setDriver( "Canvas","DirectDraw" );
	BBModule::setDriver( "Graphics","DirectDraw" );
	BBModule::setDriver( "GrDriver2D","DirectDraw" );

	BBModule::startSystem();
	BBModule::startDrivers();
}

static void shutdown(){
	state=BBAPP_ENDING;
	cmd_line->release();
	BBModule::stopDrivers();
	BBModule::stopSystem();
	exit(0);
}

// setjmp problems on Vista 64? Just shutdown instead...
// static jmp_buf jmp_env;

int bbAppState(){
	return state;
}

void bbRun( void (*pc)(),const char *cmd,BBDebugger *dbg ){

	state=BBAPP_STARTING;

	if( dbg ) bbDebugger=dbg;

//	setjmp problems on Vista 64? Just shutdown instead...
//	if( !setjmp( jmp_env ) ){

		startup();

		cmd_line=cmd ? new BBString(cmd) : BBString::null();

		state=BBAPP_RUNNING;

		pc();

//	setjmp problems on Vista 64? Just shutdown instead...
//	}

	shutdown();
}

BBString *bbCommandLine(){
	return cmd_line->copy();
}

void bbEnd(){
	if( state==BBAPP_ENDING ) bbAbortf( "Recursive bbEnd" );

//	setjmp problems on Vista 64? Just shutdown instead...
//	longjmp( jmp_env,1 );

	shutdown();
}

void bbLogf( const char *msg,... ){

	char buff[256];

	va_list args;
	va_start( args,msg );
	vsprintf( buff,msg,args );

	bbDebugger->debugLog( buff );
}

void bbError( const char *err,... ){

	char buff[256];

	va_list args;
	va_start( args,err );
	vsprintf( buff,err,args );

	bbDebugger->debugStop();
	bbDebugger->debugMsg( buff,true );
	bbEnd();
}

void bbRuntimeError( BBString *err ){
	bbError( err->c_str() );
}
