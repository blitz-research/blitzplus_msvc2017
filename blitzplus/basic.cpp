
#include "basic.h"

#include "../math/math.h"

#include <math.h>
#include <stdio.h>

#pragma warning(disable:4786)

#include <map>
#include <list>

using namespace std;

//how many objects new'd but not deleted
static int objCnt;

//how many objects deleted but not released
static int unrelObjCnt;

//how many objects to alloc per block
static const int OBJ_NEW_INC=512;

//how many strings to alloc per block
static const int STR_NEW_INC=512;

//current data ptr
static BBData *dataPtr;

//chunks of mem
//static list<char*> memBlks;

//object handle number
static int next_handle;

//object<->handle maps
static map<int,BBObj*> handle_map;
static map<BBObj*,int> object_map;

static BBType _bbIntType( BBTYPE_INT );
static BBType _bbFltType( BBTYPE_FLT );
static BBType _bbStrType( BBTYPE_STR );
static BBType _bbCStrType( BBTYPE_CSTR );

static void *bbMalloc( int size ){
	return malloc( size );
	/*
	char *c=new char[ size ];
	memBlks.push_back( c );
	return c;
	*/
}

static void bbFree( void *q ){
	free( q );
	/*
	if( !q ) return;
	char *c=(char*)q;
	memBlks.remove( c );
	delete [] c;
	*/
}

static int _nextTmpStr,_maxTmpStrs;
static BBString **_tmpStrs,*_bbNullStr;

void _bbInitCStrs( char **table ){

	_tmpStrs=0;
	_nextTmpStr=_maxTmpStrs=0;
	_bbNullStr=BBString::null();

	while( char *p=*table++ ){
		if( p[4] ){
			*(BBString**)p=new BBString(p+4);
		}else{
			*(BBString**)p=_bbNullStr;
		}
	}
}

BBString *_bbStrTmp( BBString *str ){
	if( _nextTmpStr==_maxTmpStrs ){
		//have to grow tmp string stack
		_maxTmpStrs+=1024;
		BBString **p=new BBString*[_maxTmpStrs];
		memcpy( p,_tmpStrs,_nextTmpStr*sizeof(BBString*) );
		delete[] _tmpStrs;
		_tmpStrs=p;
	}
	_tmpStrs[ _nextTmpStr++ ]=str;
	return str;
}

void _bbStrTmpFree( int n ){
	while( n-- ){
		if( !_nextTmpStr ) bbError( "Illegal TmpStr index" );
		_tmpStrs[ --_nextTmpStr ]->release();
	}
}

int _bbIStrTmpFree( int q,int n ){
	_bbStrTmpFree( n );
	return q;
}

float _bbFStrTmpFree( float q,int n ){
	_bbStrTmpFree( n );
	return q;
}

BBString *_bbStrRetain( BBString *str ){
	str->retain();
	return str;
}

BBString *_bbStrRelease( BBString *str ){
	if( str ) str->release();
	return str;
}

BBString *_bbStrLoad( BBString **var ){
	return *var ? *var : _bbNullStr;
}

BBString *_bbStrStore( BBString **var,BBString *val ){
	if( val ) val->retain();
	if( *var ) (*var)->release();
	*var=val;
	return val;
}

int _bbStrCompare( BBString *lhs,BBString *rhs ){
	return lhs->compare(rhs);
}

BBString *_bbStrConcat( BBString *lhs,BBString *rhs ){
	return _bbStrTmp( lhs->concat(rhs) );
}

int _bbStrToInt( BBString *s ){
	return s->toInt();
}

BBString *_bbStrFromInt( int n ){
	return _bbStrTmp( BBString::fromInt(n) );
}

float _bbStrToFloat( BBString *s ){
	return s->toFloat();
}

BBString *_bbStrFromFloat( float n ){
	return _bbStrTmp( BBString::fromFloat(n) );
}

const char * _bbStrToCStr( BBString *str ){
	return str->c_str();
}

BBString*	_bbStrFromCStr( const char *str ){
	return _bbStrTmp( new BBString( str ) );
}

void * _bbVecAlloc( BBVecType *type ){
	void *vec=bbMalloc( type->size*4 );
	memset( vec,0,type->size*4 );
	return vec;
}

void _bbVecFree( void *vec,BBVecType *type ){
	if( type->elementType->type==BBTYPE_STR ){
		BBString **p=(BBString**)vec;
		for( int k=0;k<type->size;++p,++k ){
			if( *p ) _bbStrRelease( *p );
		}
	}else if( type->elementType->type==BBTYPE_OBJ ){
		BBObj **p=(BBObj**)vec;
		for( int k=0;k<type->size;++p,++k ){
			if( *p ) _bbObjRelease( *p );
		}
	}
	bbFree( vec );
}

void _bbVecBoundsEx(){
	bbError( "Blitz array index out of bounds" );
}

void _bbUndimArray( BBArray *array ){
	if( void *t=array->data ){
		if( array->elementType==BBTYPE_STR ){
			BBString **p=(BBString**)t;
			int size=array->scales[array->dims-1];
			for( int k=0;k<size;++p,++k ){
				if( *p ) _bbStrRelease( *p );
			}
		}else if( array->elementType==BBTYPE_OBJ ){
			BBObj **p=(BBObj**)t;
			int size=array->scales[array->dims-1];
			for( int k=0;k<size;++p,++k ){
				if( *p ) _bbObjRelease( *p );
			}
		}
		bbFree( t );
		array->data=0;
	}
}

void _bbDimArray( BBArray *array ){
	int k;
	for( k=0;k<array->dims;++k ) ++array->scales[k];
	for( k=1;k<array->dims;++k ){
		array->scales[k]*=array->scales[k-1];
	}
	int size=array->scales[array->dims-1];
	array->data=bbMalloc( size*4 );
	memset( array->data,0,size*4 );
}

void _bbArrayBoundsEx(){
	bbError( "Array index out of bounds" );
}

static void unlinkObj( BBObj *obj ){
	obj->next->prev=obj->prev;
	obj->prev->next=obj->next;
}

static void insertObj( BBObj *obj,BBObj *next ){
	obj->next=next;
	obj->prev=next->prev;
	next->prev->next=obj;
	next->prev=obj;
}

BBObj *_bbObjNew( BBObjType *type ){
	if( type->free.next==&type->free ){
		int obj_size=sizeof(BBObj)+type->fieldCnt*4;
		BBObj *o=(BBObj*)bbMalloc( obj_size*OBJ_NEW_INC );
		for( int k=0;k<OBJ_NEW_INC;++k ){
			insertObj( o,&type->free );
			o=(BBObj*)( (char*)o+obj_size );
		}
	}
	BBObj *o=type->free.next;
	unlinkObj( o );
	o->type=type;
	o->ref_cnt=1;
	o->fields=(BBField*)(o+1);
	for( int k=0;k<type->fieldCnt;++k ){
		switch( type->fieldTypes[k]->type ){
		case BBTYPE_VEC:
			o->fields[k].VEC=_bbVecAlloc( (BBVecType*)type->fieldTypes[k] );
			break;
		default:
			o->fields[k].INT=0;
		}
	}
	insertObj( o,&type->used );
	++unrelObjCnt;
	++objCnt;
	return o;
}

void _bbObjDelete( BBObj *obj ){
	if( !obj ) return;
	BBField *fields=obj->fields;
	if( !fields ) return;
	BBObjType *type=obj->type;
	for( int k=0;k<type->fieldCnt;++k ){
		switch( type->fieldTypes[k]->type ){
		case BBTYPE_STR:
			_bbStrRelease( fields[k].STR );
			break;
		case BBTYPE_OBJ:
			_bbObjRelease( fields[k].OBJ );
			break;
		case BBTYPE_VEC:
			_bbVecFree( fields[k].VEC,(BBVecType*)type->fieldTypes[k] );
			break;
		}
	}
	map<BBObj*,int>::iterator it=object_map.find( obj );
	if( it!=object_map.end() ){
		handle_map.erase( it->second );
		object_map.erase( it );
	}
	obj->fields=0;
	_bbObjRelease( obj );
	--objCnt;
}

void _bbObjDeleteEach( BBObjType *type ){
	BBObj *obj=type->used.next;
	while( obj->type ){
		BBObj *next=obj->next;
		if( obj->fields ) _bbObjDelete( obj );
		obj=next;
	}
}

extern void  bbDebugLog( BBString *t );
extern void  bbStop( );

void _bbObjRelease( BBObj *obj ){
	if( !obj || --obj->ref_cnt ) return;
	unlinkObj( obj );
	insertObj( obj,&obj->type->free );
	--unrelObjCnt;
}

void _bbObjStore( BBObj **var,BBObj *obj ){
	if( obj ) ++obj->ref_cnt;	//do this first incase of self-assignment
	_bbObjRelease( *var );
	*var=obj;
}

int _bbObjCompare( BBObj *o1,BBObj *o2 ){
	return (o1 ? o1->fields : 0)!=(o2 ? o2->fields : 0);
}

BBObj *_bbObjNext( BBObj *obj ){
	do{
		obj=obj->next;
		if( !obj->type ) return 0;
	}while( !obj->fields );
	return obj;
}

BBObj *_bbObjPrev( BBObj *obj ){
	do{
		obj=obj->prev;
		if( !obj->type ) return 0;
	}while( !obj->fields );
	return obj;
}

BBObj *_bbObjFirst( BBObjType *type ){
	return _bbObjNext( &type->used );
}

BBObj *_bbObjLast( BBObjType *type ){
	return _bbObjPrev( &type->used );
}

void _bbObjInsBefore( BBObj *o1,BBObj *o2 ){
	if( o1==o2 ) return;
	unlinkObj( o1 );
	insertObj( o1,o2 );
}

void _bbObjInsAfter( BBObj *o1,BBObj *o2 ){
	if( o1==o2 ) return;
	unlinkObj( o1 );
	insertObj( o1,o2->next );
}

int _bbObjEachFirst( BBObj **var,BBObjType *type ){
	_bbObjStore( var,_bbObjFirst( type ) );
	return *var!=0;
}

int _bbObjEachNext( BBObj **var ){
	_bbObjStore( var,_bbObjNext( *var ) );
	return *var!=0;
}

int _bbObjEachFirst2( BBObj **var,BBObjType *type ){
	*var=_bbObjFirst( type );
	return *var!=0;
}

int _bbObjEachNext2( BBObj **var ){
	*var=_bbObjNext( *var );
	return *var!=0;
}

BBString *_bbObjToStr( BBObj *obj ){
	return _bbStrTmp( new BBString( "[OBJECT]" ) );
}

int _bbObjToHandle( BBObj *obj ){
	if( !obj || !obj->fields ) return 0;
	map<BBObj*,int>::const_iterator it=object_map.find( obj );
	if( it!=object_map.end() ) return it->second;
	++next_handle;
	object_map[obj]=next_handle;
	handle_map[next_handle]=obj;
	return next_handle;
}

BBObj *_bbObjFromHandle( int handle,BBObjType *type ){
	map<int,BBObj*>::const_iterator it=handle_map.find( handle );
	if( it==handle_map.end() ) return 0;
	BBObj *obj=it->second;
	return obj->type==type ? obj : 0;
}

void _bbNullObjEx(){
	bbError( "Object does not exist" );
}

void _bbRestore( BBData *data ){
	dataPtr=data;
}

int _bbReadInt(){
	int i;
	BBString *t;
	switch( dataPtr->fieldType ){
	case BBTYPE_END:bbError( "Out of data" );return 0;
	case BBTYPE_INT:return dataPtr++->field.INT;
	case BBTYPE_FLT:return dataPtr++->field.FLT;
	case BBTYPE_CSTR:
		t=new BBString( (dataPtr++)->field.CSTR );
		i=t->toInt();
		t->release();
		return i;
	}
	bbError( "Bad data type" );
	return 0;
}

float _bbReadFloat(){
	float f;
	BBString *t;
	switch( dataPtr->fieldType ){
	case BBTYPE_END:bbError( "Out of data" );return 0;
	case BBTYPE_INT:return dataPtr++->field.INT;
	case BBTYPE_FLT:return dataPtr++->field.FLT;
	case BBTYPE_CSTR:
		t=new BBString( (dataPtr)->field.CSTR );
		f=t->toFloat();
		t->release();
		return f;
	}
	bbError( "Bad data type" );
	return 0;
}

BBString *_bbReadStr(){
	switch( dataPtr->fieldType ){
	case BBTYPE_END:bbError( "Out of data" );return 0;
	case BBTYPE_INT:return _bbStrTmp( BBString::fromInt( dataPtr++->field.INT ) );
	case BBTYPE_FLT:return _bbStrTmp( BBString::fromFloat( dataPtr++->field.FLT ) );
	case BBTYPE_CSTR:return _bbStrTmp( new BBString( dataPtr++->field.CSTR ) );
	}
	bbError( "Bad data type" );
	return 0;
}

int _bbAbs( int n ){
	return n>=0 ? n : -n;
}

int _bbSgn( int n ){
	return n>0 ? 1 : (n<0 ? -1 : 0);
}

int _bbMod( int x,int y ){
	return x%y;
}

float _bbFAbs( float n ){
	return n>=0 ? n : -n;
}

float _bbFSgn( float n ){
	return n>0 ? 1 : (n<0 ? -1 : 0);
}

float _bbFMod( float x,float y ){
	return (float)fmod( x,y );
}

float _bbFPow( float x,float y ){
	return (float)pow( x,y );
}

void bbRuntimeStats(){
}

void basic_link( void (*sym)( const char *t_sym,void *pc ) ){

	sym( "_bbIntType",&_bbIntType );
	sym( "_bbFltType",&_bbFltType );
	sym( "_bbStrType",&_bbStrType );
	sym( "_bbCStrType",&_bbCStrType );

	sym( "_bbLoadLibs",_bbLoadLibs );
	sym( "_bbInitCStrs",_bbInitCStrs );

	sym( "_bbStrTmp",_bbStrTmp );
	sym( "_bbStrTmpFree",_bbStrTmpFree );
	sym( "_bbIStrTmpFree",_bbIStrTmpFree );
	sym( "_bbFStrTmpFree",_bbFStrTmpFree );
	sym( "_bbStrRetain",_bbStrRetain );
	sym( "_bbStrRelease",_bbStrRelease );
	sym( "_bbStrLoad",_bbStrLoad );
	sym( "_bbStrStore",_bbStrStore );

	sym( "_bbStrConcat",_bbStrConcat );
	sym( "_bbStrCompare",_bbStrCompare );

	sym( "_bbStrToInt",_bbStrToInt );
	sym( "_bbStrFromInt",_bbStrFromInt );
	sym( "_bbStrToFloat",_bbStrToFloat );
	sym( "_bbStrFromFloat",_bbStrFromFloat );
	sym( "_bbStrToCStr",_bbStrToCStr );
	sym( "_bbStrFromCStr",_bbStrFromCStr );

	sym( "_bbDimArray",_bbDimArray );
	sym( "_bbUndimArray",_bbUndimArray );
	sym( "_bbArrayBoundsEx",_bbArrayBoundsEx );
	sym( "_bbVecAlloc",_bbVecAlloc );
	sym( "_bbVecFree",_bbVecFree );
	sym( "_bbVecBoundsEx",_bbVecBoundsEx );
	sym( "_bbObjNew",_bbObjNew );
	sym( "_bbObjDelete",_bbObjDelete );
	sym( "_bbObjDeleteEach",_bbObjDeleteEach );
	sym( "_bbObjRelease",_bbObjRelease );
	sym( "_bbObjStore",_bbObjStore );
	sym( "_bbObjCompare",_bbObjCompare );
	sym( "_bbObjNext",_bbObjNext );
	sym( "_bbObjPrev",_bbObjPrev );
	sym( "_bbObjFirst",_bbObjFirst );
	sym( "_bbObjLast",_bbObjLast );
	sym( "_bbObjInsBefore",_bbObjInsBefore );
	sym( "_bbObjInsAfter",_bbObjInsAfter );
	sym( "_bbObjEachFirst",_bbObjEachFirst );
	sym( "_bbObjEachNext",_bbObjEachNext );
	sym( "_bbObjEachFirst2",_bbObjEachFirst2 );
	sym( "_bbObjEachNext2",_bbObjEachNext2 );
	sym( "_bbObjToStr",_bbObjToStr );
	sym( "_bbObjToHandle",_bbObjToHandle );
	sym( "_bbObjFromHandle",_bbObjFromHandle );
	sym( "_bbNullObjEx",_bbNullObjEx );
	sym( "_bbRestore",_bbRestore );
	sym( "_bbReadInt",_bbReadInt );
	sym( "_bbReadFloat",_bbReadFloat );
	sym( "_bbReadStr",_bbReadStr );
	sym( "_bbAbs",_bbAbs );
	sym( "_bbSgn",_bbSgn );
	sym( "_bbMod",_bbMod );
	sym( "_bbFAbs",_bbFAbs );
	sym( "_bbFSgn",_bbFSgn );
	sym( "_bbFMod",_bbFMod );
	sym( "_bbFPow",_bbFPow );
	sym( "RuntimeStats",bbRuntimeStats );
}
