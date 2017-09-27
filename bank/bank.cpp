
#include "bank.h"

#pragma warning(disable:4786)

#include <map>
#include <string>

using namespace std;

BBBank::BBBank():_data(0),_size(0),_capacity(0){
}

BBBank::BBBank( int size ):_size(size),_capacity(size+10){
	_data=new char[_capacity];
	memset( _data,0,_size );
}

BBBank::~BBBank(){
	if( _capacity ) delete[] _data;
}

void BBBank::setData( void *data ){
	if( _capacity ) bbError( "Bank data cannot be modified" );
	_data=data;
}

void BBBank::resize( int size ){
	if( !_capacity ) bbError( "Bank cannot be resized" );
	if( size>_capacity ) reserve( size+size/2+10 );
	if( size>_size ) memset( (char*)_data+_size,0,size-_size );
	_size=size;
}

void BBBank::reserve( int capacity ){
	if( !_capacity ) bbError( "Bank cannot be resized" );
	if( capacity<=_capacity ) return;
	void *data=new char[capacity];
	memcpy( data,_data,_size );
	delete[] _data;
	_capacity=capacity;
	_data=data;
}

BBBank * bbCreateBank( int size ){
	if (size<0) size=0;
	BBBank *b=new BBBank( size );
	return b;
}

void  bbFreeBank( BBBank *b ){
	if( !b ) return;
	b->debug();
	b->release();
}

int  bbBankSize( BBBank *b ){
	b->debug();
	return b->size();
}

void  bbResizeBank( BBBank *b,int size ){
	if (size<0) size=0;
	b->debug();
	b->resize( size );
}

void  bbCopyBank( BBBank *src,int src_p,BBBank *dst,int dst_p,int count ){
	src->debug( src_p,count );
	dst->debug( dst_p,count );
	if (count>0) memmove( (char*)dst->data()+dst_p,(char*)src->data()+src_p,count );
}

int  bbPeekByte( BBBank *b,int offset ){
	b->debug(offset);
	return *((unsigned char*)b->data()+offset);
}

int  bbPeekShort( BBBank *b,int offset ){
	b->debug(offset+1);
	return *(unsigned short*)((char*)b->data()+offset);
}

int  bbPeekInt( BBBank *b,int offset ){
	b->debug(offset+3);
	return *(int*)((char*)b->data()+offset);
}

float  bbPeekFloat( BBBank *b,int offset ){
	b->debug(offset+3);
	return *(float*)((char*)b->data()+offset);
}

void  bbPokeByte( BBBank *b,int offset,int value ){
	b->debug(offset);
	*((char*)b->data()+offset)=value;
}

void  bbPokeShort( BBBank *b,int offset,int value ){
	b->debug(offset+1);
	*(short*)((char*)b->data()+offset)=value;
}

void  bbPokeInt( BBBank *b,int offset,int value ){
	b->debug(offset+3);
	*(int*)((char*)b->data()+offset)=value;
}

void  bbPokeFloat( BBBank *b,int offset,float value ){
	b->debug(offset+3);
	*(float*)((char*)b->data()+offset)=value;
}

int   bbReadBytes( BBBank *b,BBStream *s,int offset,int count ){
	s->debug();
	b->debug(offset+count-1);
	return s->read( (char*)b->data()+offset,count );
}

int   bbWriteBytes( BBBank *b,BBStream *s,int offset,int count ){
	s->debug();
	b->debug( offset+count-1 );
	return s->write( (char*)b->data()+offset,count );
}

//******************** Yucky CallDll Stuff ********************

#define WIN32_LEAN_AND_MEAN

#include <windows.h>

typedef int (*LibFunc)( void *in,int in_sz,void *out,int out_sz );
//typedef int (_stdcall *LibFunc)( void *in,int in_sz,void *out,int out_sz );

struct bbDll{
	HINSTANCE hinst;
	map<string,LibFunc> funcs;
};

static map<string,bbDll*> _libs;

int  bbCallDLL( BBString *_dll,BBString *_func,BBBank *_in,BBBank *_out ){

	if( _in ) _in->debug();
	if( _out ) _out->debug();

	string dll=_dll->c_str();
	string func=_func->c_str();

	map<string,bbDll*>::const_iterator lib_it=_libs.find( dll );

	if( lib_it==_libs.end() ){
		HINSTANCE h=LoadLibrary( dll.c_str() );
		if( !h ) return -1;
		bbDll *t=new bbDll;
		t->hinst=h;
		lib_it=_libs.insert( make_pair( dll,t ) ).first;
	}

	bbDll *t=lib_it->second;
	map<string,LibFunc>::const_iterator fun_it=t->funcs.find( func );

	if( fun_it==t->funcs.end() ){
		LibFunc f=(LibFunc)GetProcAddress( t->hinst,func.c_str() );
		if( !f ) return -1;
		fun_it=t->funcs.insert( make_pair( func,f ) ).first;
	}

	void *in,*out;
	int in_sz,out_sz;
	if( _in ){ in=_in->data();in_sz=_in->size(); }
	if( _out ){ out=_out->data();out_sz=_out->size(); }

	static void *save_esp;

    _asm{ mov [save_esp],esp };

	int n=fun_it->second( in,in_sz,out,out_sz );

    _asm{ mov esp,[save_esp] };

	return n;
}
