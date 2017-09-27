
#ifndef BASIC_H
#define BASIC_H

#include "../app/app.h"

#include "userlibs.h"

void basic_link( void (*sym)( const char *t_sym,void *pc ) );

enum{
	BBTYPE_END=0,
	BBTYPE_INT=1,BBTYPE_FLT=2,
	BBTYPE_STR=3,BBTYPE_CSTR=4,
	BBTYPE_OBJ=5,BBTYPE_VEC=6
};

#pragma pack( push,1 )

struct BBObj;
struct BBType;
struct BBObjType;
struct BBVecType;
union  BBField;
struct BBArray;

struct BBObj{
	BBField *fields;
	BBObj *next,*prev;
	BBObjType *type;
	int ref_cnt;
};

struct BBType{
	int type;
	BBType( int n ):type(n){}
};

struct BBObjType : public BBType{
	BBObj used,free;
	int fieldCnt;
	BBType *fieldTypes[1];
};

struct BBVecType : public BBType{
	int size;
	BBType *elementType;
};

union BBField{
	int INT;
	float FLT;
	BBString *STR;
	char *CSTR;
	BBObj *OBJ;
	void *VEC;
};

struct BBArray{
	void *data;
	int elementType,dims,scales[1];
};

struct BBData{
	int fieldType;
	BBField field;
};

#pragma pack( pop )

extern BBType _bbIntType;
extern BBType _bbFltType;
extern BBType _BBStringType;
extern BBType _bbCStrType;

void		_bbInitCStrs( char **table );

BBString*	_bbStrTmp( BBString *str );
void		_bbStrTmpFree( int n );
int			_bbIStrTmpFree( int q,int n );
float		_bbFStrTmpFree( float q,int n );
BBString*	_bbStrRetain( BBString *str );
BBString*	_bbStrRelease( BBString *str );
BBString*	_bbStrLoad( BBString **var );
BBString*	_bbStrStore( BBString **var,BBString *str );

int			_bbStrCompare( BBString *lhs,BBString *rhs );
BBString*	_bbStrConcat( BBString *lhs,BBString *rhs );

int			_bbStrToInt( BBString *s );
BBString*	_bbStrFromInt( int n );
float		_bbStrToFloat( BBString *s );
BBString*	_bbStrFromFloat( float n );
const char*	_bbStrToCStr( BBString *str );
BBString*	_bbStrFromCStr( const char *str );

void		_bbDimArray( BBArray *array );
void		_bbUndimArray( BBArray *array );
void		_bbArrayBoundsEx();

void*		_bbVecAlloc( BBVecType *type );
void		_bbVecFree( void *vec,BBVecType *type );
void		_bbVecBoundsEx();

BBObj*		_bbObjNew( BBObjType *t );
void		_bbObjDelete( BBObj *obj );
void		_bbObjDeleteEach( BBObjType *type );
void		_bbObjRelease( BBObj *obj );
void		_bbObjStore( BBObj **var,BBObj *obj );
BBObj*		_bbObjNext( BBObj *obj );
BBObj*		_bbObjPrev( BBObj *obj );
BBObj*		_bbObjFirst( BBObjType *t );
BBObj*		_bbObjLast( BBObjType *t );
void		_bbObjInsBefore( BBObj *o1,BBObj *o2 );
void		_bbObjInsAfter( BBObj *o1,BBObj *o2 );
int			_bbObjEachFirst( BBObj **var,BBObjType *type );
int			_bbObjEachNext( BBObj **var );
int			_bbObjCompare( BBObj *o1,BBObj *o2 );
BBString*	_bbObjToStr( BBObj *obj );
int			_bbObjToHandle( BBObj *obj );
BBObj*		_bbObjFromHandle( int handle,BBObjType *type );
void		_bbNullObjEx();

void		_bbRestore( BBData *data );
int			_bbReadInt();
float		_bbReadFloat();
BBString*	_bbReadStr();

int			_bbAbs( int n );
int			_bbSgn( int n );
int			_bbMod( int x,int y );
float		_bbFAbs( float n );
float		_bbFSgn( float n );
float		_bbFMod( float x,float y );
float		_bbFPow( float x,float y );

void		bbRuntimeStats();

#endif
