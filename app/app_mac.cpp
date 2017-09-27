
#include "../stdc/stdc.h"

#ifdef HOST_MAC

#include "app_mac.h"

void bbAbortf( const char *msg,... ){

	char buff[256];

	va_list args;
	va_start( args,msg );
	vsprintf( buff,msg,args );

	puts(buff);
	exit(0);
}

#endif