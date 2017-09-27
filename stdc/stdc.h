
#ifndef STDC_H
#define STDC_H

#if _WIN32
#define HOST_W32 1
#elif __APPLE__
#define HOST_MAC 1
#else
#define HOST_TUX 1
#endif

#if _MSC_VER
#define CC_MSVC 1
#pragma warning(disable:4007)	//cdecl 
#pragma warning(disable:4786)	//debug info>255 chars
#pragma warning(disable:4530)	//exceptions not enabled
#else
#define CC_GCC 1
#endif

#include <ctype.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <stdarg.h>

#include <sys/stat.h>

#if CC_MSVC
#include <direct.h>
#else
#include <unistd.h>
#endif

#define MAXPATHLEN 1024

#if HOST_W32
#define mkdir(X,Y) mkdir(X)
#define realpath(X,Y) _fullpath(Y,X,MAXPATHLEN)

#elif HOST_MAC
char*	itoa( int n,char *buf,int radix );
int		stricmp( const char *str1,const char *str2 );

#endif

typedef const char *cstr;

#endif
