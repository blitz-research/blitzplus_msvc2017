
#ifndef ITEMARRAY_H
#define ITEMARRAY_H

#include "../app/app.h"

#include <vector>

class BBItemArray : public BBObject{
	int _selected;
	std::vector<BBString*> _items;
protected:
	~BBItemArray();
public:
	BBItemArray();

	void clear();
	void add( BBString *item );
	void insert( int index,BBString *item );
	void modify( int index,BBString *item );
	void remove( int index );
	void select( int index );

	int	size()const;
	int selected()const;
	BBString *item( int index )const;

	void debug()const{ _debug(this,"Gadget Item List"); }
	void debug( int index,int off )const{
		debug();
		if( index>=0 && index<_items.size()+off ) return;
		bbError( "Gadget item list index out of range" );
	}
};

#endif