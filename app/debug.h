
#ifndef BBDEBUG_H
#define BBDEBUG_H

class BBString;

class BBDebugger{
public:
	virtual void debugRun()=0;
	virtual void debugStop()=0;
	virtual void debugStmt( int srcpos,const char *file )=0;
	virtual void debugEnter( void *frame,void *env,const char *func )=0;
	virtual void debugLeave()=0;
	virtual void debugLog( const char *msg )=0;
};

extern BBDebugger *bbDebugger;

void	bbStop();
void	bbRuntimeError( BBString *err );

void	bbDebugStmt( int pos,const char *file );
void	bbDebugEnter( void *frame,void *env,const char *func );
void	bbDebugLeave();
void	bbDebugLog( BBString *msg );

#endif