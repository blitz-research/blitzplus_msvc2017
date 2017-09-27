
#include "stdc.h"

#if HOST_MAC
char*	itoa( int n,char *buf,int radix ){
	sprintf( buf,"%i",n );
	return buf;
}
int		stricmp( const char *string1, const char *string2 ){
}
#endif
