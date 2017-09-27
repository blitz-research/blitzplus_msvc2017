
#include "process.h"

#include <stdlib.h>

BBString*	bbGetEnv( BBString *var ){
	const char *p=getenv( var->c_str() );
	return p ? new BBString(p)  : BBString::null();
}

void		bbSetEnv( BBString *var,BBString *val ){
	BBString *t=var->concat( BBTMPSTR("=") );
	BBString *q=t->concat(val);
	putenv( q->c_str() );
	q->release();
	t->release();
}

