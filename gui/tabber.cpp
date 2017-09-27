
#include "tabber.h"

BBTabber::BBTabber( BBGroup *group,int style ):BBGroup(group,style){
	_items=new BBItemArray();
}

BBTabber::~BBTabber(){
	_items->release();
}

void BBTabber::clear(){
	_items->clear();
}

void BBTabber::add( BBString *item,int icon ){
	_items->add(item);
}

void BBTabber::insert( int index,BBString *item,int icon ){
	_items->insert(index,item);
}

void BBTabber::modify( int index,BBString *item,int icon ){
	_items->modify(index,item);
}

void BBTabber::remove( int index ){
	_items->remove(index);
}

void BBTabber::select( int index ){
	_items->select(index);
}

