
#ifndef TABBER_H
#define TABBER_H

#include "group.h"

#include "itemarray.h"

class BBTabber : public BBGroup{
	BBItemArray *_items;
protected:
	~BBTabber();
public:
	BBTabber( BBGroup *group,int style );

	virtual void clear();
	virtual void add( BBString *item,int icon );
	virtual void insert( int index,BBString *item,int icon );
	virtual void modify( int index,BBString *item,int icon );
	virtual void remove( int index );
	virtual void select( int index );

	const BBItemArray *items()const{ return _items; }
};

#endif
