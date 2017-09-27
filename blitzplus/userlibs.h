
#ifndef USERLIBS_H
#define USERLIBS_H

#include "../app/app.h"

class BBUserLibs : public BBModule{
public:
	BBUserLibs();

	bool startup();
	void shutdown();
};

void _bbLoadLibs( char *table );

extern BBUserLibs bbUserLibs;
static BBUserLibs *_bbUserLibs=&bbUserLibs;

#endif