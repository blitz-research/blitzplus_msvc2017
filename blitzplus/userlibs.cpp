
#include "userlibs.h"

#include <windows.h>

#include <vector>

BBUserLibs bbUserLibs;

static std::vector<HMODULE> _mods;

static void libNotFound(){
	bbError( "User lib not found" );
}

static void procNotFound(){
	bbError( "User lib function not found" );
}

void _bbLoadLibs( char *p ){

	const char *home=getenv( "blitzpath" );

	while( *p ){
		HMODULE mod=LoadLibrary( p );
		if( !mod && home ){
			char buff[MAX_PATH];
			strcpy( buff,home );
			strcat( buff,"/userlibs/" );
			strcat( buff,p );
			mod=LoadLibrary( buff );
		}
		p+=strlen(p)+1;
		if( mod ){
			_mods.push_back( mod );
			while( *p ){
				void *proc=GetProcAddress( mod,p );
				p+=strlen(p)+1;
				void *ptr=*(void**)p;
				p+=4;
				*(void**)ptr=proc ? proc : procNotFound;
			}
		}else{
			while( *p ){
				p+=strlen(p)+1;
				void *ptr=*(void**)p;
				p+=4;
				*(void**)ptr=libNotFound;
			}
		}
		++p;
	}
}

BBUserLibs::BBUserLibs(){
	reg( "BBUserLibs" );
}

bool BBUserLibs::startup(){
	return true;
}

void BBUserLibs::shutdown(){
	for( ;_mods.size();_mods.pop_back() ) FreeLibrary( _mods.back() );
}

