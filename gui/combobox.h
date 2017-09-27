
#ifndef COMBOBOX_H
#define COMBOBOX_H

#include "gadget.h"

#include "itemarray.h"

class BBComboBox : public BBGadget{
	BBItemArray *_items;
protected:
	~BBComboBox();
public:
	BBComboBox( BBGroup *group,int style );

	virtual void clear();
	virtual void add( BBString *item,int icon );
	virtual void insert( int index,BBString *item,int icon );
	virtual void modify( int index,BBString *item,int icon );
	virtual void remove( int index );
	virtual void select( int index );

	const BBItemArray *items()const{ return _items; }
};

#endif
