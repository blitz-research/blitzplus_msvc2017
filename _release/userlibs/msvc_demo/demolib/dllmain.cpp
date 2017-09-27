
//demo.dll
//
//Simple example of a Blitz extension lib.
//
//Built with MSVC 6.0

#include <string.h>
#include <stdlib.h>

#define BBDECL extern "C" _declspec(dllexport)

#define BBCALL _stdcall

//1st function: inverts bits in a bank
//
BBDECL void BBCALL InvertBits( char *bank,int offset,int sz ){

	bank+=offset;

	//int align (for speed!)...
	while( sz && ((int)bank & 3) ){
		*bank++=~*bank;
		--sz;
	}

	//do ints (for speed!)...
	if( int n=sz/4 ){
		int *p=(int*)bank;
		while( n-- ) *p++=~*p;
		bank=(char*)p;
		sz&=3;
	}

	//do left over bytes...
	while( sz-- ) *bank++=~*bank;
}

//2nd function: randomly rearranges characters in a string
//
BBDECL const char * BBCALL ShuffleString( const char *str ){
	static char *_buf;

	//cache strlen
	int sz=strlen(str);

	//alloca
	delete[] _buf;
	_buf=new char[ sz+1 ];
	strcpy( _buf,str );

	for( int k=0;k<sz;++k ){
		int n=rand()%sz;
		int t=_buf[k];_buf[k]=_buf[n];_buf[n]=t;
	}

	return _buf;
}
