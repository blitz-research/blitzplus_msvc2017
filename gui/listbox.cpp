
#include "listbox.h"

BBListBox::BBListBox( BBGroup *group,int style ):BBGadget(group,style){
	_items=new BBItemArray();
}

BBListBox::~BBListBox(){
	_items->release();
}

void BBListBox::clear(){
	_items->clear();
}

void BBListBox::add( BBString *item,int icon ){
	_items->add(item);
}

void BBListBox::insert( int index,BBString *item,int icon ){
	_items->insert(index,item);
}

void BBListBox::modify( int index,BBString *item,int icon ){
	_items->modify(index,item);
}

void BBListBox::remove( int index ){
	_items->remove(index);
}

void BBListBox::select( int index ){
	_items->select(index);
}

