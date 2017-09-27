
#ifndef DEBUGGER_H
#define DEBUGGER_H

class BBDebugger{
public:
	virtual void debugRun();
	virtual void debugStop();
	virtual void debugStmt( int srcpos,const char *file );
	virtual void debugEnter( void *frame,void *env,const char *func );
	virtual void debugLeave();
	virtual void debugLog( const char *msg );
	virtual void debugMsg( const char *msg,bool serious );
};

extern BBDebugger *bbDebugger;

#endif