
#ifndef RUNTIME_H
#define RUNTIME_H

class Debugger;

class Runtime{
public:
	virtual int version();
	virtual const char *nextSym();
	virtual int symValue( const char *sym );
	virtual void startup( HINSTANCE hinst );
	virtual void shutdown();
	virtual void asyncStop();
	virtual void asyncRun();
	virtual void asyncEnd();
	virtual void checkmem( std::streambuf *buf );

	virtual void execute( void (*pc)(),const char *args,Debugger *dbg );
};

#endif