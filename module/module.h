
#ifndef MODULE_H
#define MODULE_H

#include "../rsource/resource.h"

class BBString;

class BBModule{
	BBModule *_succ;
	const char *_name,*_subsys,*_config,*_desc;
	int _state;
	BBResource *_autorel;

	enum{
		FAILED=-1,STOPPED=0,STARTING=1,RUNNING=2
	};

	bool start();
	void stop();

protected:
	void reg( const char *name,const char *subsys=0,const char *config=0 );

	virtual bool startup();
	virtual void shutdown();

public:
	BBModule();

	void autoRelease( BBResource *res );

	static void debugModules();

	static void startSystem();
	static void stopSystem();

	static bool startModule( const char *name );
	static void stopModule( const char *name );

	static void setDriver( const char *subsys,const char *config );

	static bool startDrivers();
	static bool startDrivers( const char *subsys );

	static void stopDrivers();
	static void stopDrivers( const char *subsys );
};

void		bbDebugModules();

#endif