
#ifdef _DEBUG
//#define _DEBUG_DELETE
#endif

#include "../app/app.h"

#include <stdio.h>

#include <stdlib.h>

#pragma warning(disable:4786)

#include <typeinfo>
#include <set>
#include <map>

using namespace std;

static set<const BBObject*> _objs;

#ifdef _DEBUG_DELETE
	static map<const BBObject*,const char *> _delObjs;
#endif

struct QueryID{
	const char *str;
	int id;
};

static QueryID _qids[256];
static int n_objs,_id=10000;

BBObject::BBObject():_refs(1){
	_objs.insert(this);
	++n_objs;
}

BBObject::~BBObject(){
	if( _refs ){
		bbAbortf( "Attempt to delete referenced object" );
	}
	--n_objs;
	_objs.erase(this);
}

#ifdef _DEBUG

void BBObject::retain(){
	if( !valid() ){
		bbAbortf( "Attempt to retain <unknown> object" );
	}
	++_refs;
}

void BBObject::release(){
	if( !this ) return;
	if( !valid() ){
#ifdef _DEBUG_DELETE
		std::map<const BBObject*,const char *>::iterator it=_delObjs.find(this);
		if( it!=_delObjs.end() ){
			bbAbortf( "Attempt to release deleted '%s' object",it->second );
		}
#endif
		bbAbortf( "Attempt to release <unknown> object" );
	}
	if( !--_refs ) destroy();
}

#endif

void BBObject::destroy(){
#ifdef _DEBUG_DELETE
	_delObjs.insert( make_pair(this,typeid(*this).name()) );
	this->~BBObject();
#else
	delete this;
#endif
}

void *BBObject::query( int qid ){
	return 0;
}

bool BBObject::valid()const{
	return _objs.count(this)>0;
}

void BBObject::bad_handle( const char *ty ){
	bbError( "Invalid %s handle",ty );
}

void bbDebugObjects(){

	map<const char*,int> tys;

	set<const BBObject*>::const_iterator it;
	for( it=_objs.begin();it!=_objs.end();++it ){
		const type_info &ty=typeid(**it);
		++tys[ty.name()];
	}

	char buf[64];

	bbLogf( "****************************************************" );
	bbLogf( "* Object type                             Count    *" );
	bbLogf( "* ------------------------------------------------ *" );
	map<const char*,int>::const_iterator tyit;
	int total=0;
	for( tyit=tys.begin();tyit!=tys.end();++tyit ){

		memset( buf,' ',52 );
		memcpy( buf+2,tyit->first,strlen(tyit->first) );
		sprintf( buf+42,"%i",tyit->second );

		buf[strlen(buf)]=' ';
		buf[0]=buf[51]='*';
		buf[52]=0;
		bbLogf(buf);

		total+=tyit->second;
	}
	bbLogf( "* ------------------------------------------------ *" );

	memset( buf,' ',52 );
	sprintf( buf,
		    "* Total Objects                           %i",total );
	buf[strlen(buf)]=' ';
	buf[0]=buf[51]='*';
	buf[52]=0;
	bbLogf( buf );

	bbLogf( "****************************************************" );
}

int bbActiveObjects(){
	return n_objs;
}

int bbAllocQueryID( const char *str ){
	int n;
	for( n=0;n<256 && _qids[n].str;++n ){
		if( !strcmp( _qids[n].str,str ) ) return _qids[n].id;
	}

	if( n==256 ) bbError( "Out of QIDS!" );

	_qids[n].str=str;
	_qids[n].id=++_id;

	return _id;
}

void *bbQueryObject( BBObject *o,int qid ){
	return o->query(qid);
}
