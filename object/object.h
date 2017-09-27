
#ifndef OBJECT_H
#define OBJECT_H

#include "stdqid.h"

#define DEBUG_OBJ

class BBObject{
	int _refs;
	BBObject(const BBObject&);
	BBObject &operator=(const BBObject&);

	static void bad_handle( const char *ty );

protected:
	virtual ~BBObject();

	virtual void destroy();

public:
	BBObject();

	virtual void *query( int qid );

#ifdef _DEBUG
	void retain();
	void release();
#else
	void retain(){ ++_refs; }
	void release(){ if( this && !--_refs ) destroy(); }
#endif

	int  refs()const{ return _refs; }
	bool valid()const;

	template<class T>static void _debug( T *p,const char *ty ){
		if( p->valid() && dynamic_cast<T*>(p) ) return;
		bad_handle( ty );
	}
};

void	bbDebugObjects();
int		bbActiveObjects();
int		bbAllocQueryID( const char *str_id );
void*	bbQueryObject( BBObject *t,int qid );

#endif
