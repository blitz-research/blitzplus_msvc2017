
#include "combobox.h"

BBComboBox::BBComboBox( BBGroup *group,int style ):BBGadget(group,style){
	_items=new BBItemArray();
}

BBComboBox::~BBComboBox(){
	_items->release();
}

void BBComboBox::clear(){
	_items->clear();
}

void BBComboBox::add( BBString *item,int icon ){
	_items->add(item);
}

void BBComboBox::insert( int index,BBString *item,int icon ){
	_items->insert(index,item);
}

void BBComboBox::modify( int index,BBString *item,int icon ){
	_items->modify(index,item);
}

void BBComboBox::remove( int index ){
	_items->remove(index);
}

void BBComboBox::select( int index ){
	_items->select(index);
}

