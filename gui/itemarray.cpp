
#include "itemarray.h"

BBItemArray::BBItemArray():_selected(-1){
}

BBItemArray::~BBItemArray(){
	clear();
}

void BBItemArray::clear(){
	debug();
	for( int k=0;k<_items.size();++k ) _items[k]->release();
	_items.clear();
	_selected=-1;
}

void BBItemArray::add( BBString *item ){
	debug();
	item->retain();
	_items.push_back(item);
}

void BBItemArray::insert( int index,BBString *item ){
	debug(index,1);
	item->retain();
	_items.insert( _items.begin()+index,item );
}

void BBItemArray::modify( int index,BBString *item ){
	debug(index,0);
	item->retain();
	_items[index]->release();
	_items[index]=item;
}

void BBItemArray::remove( int index ){
	debug(index,0);
	_items.erase( _items.begin()+index );
}

void BBItemArray::select( int index ){
	if( index>=0 ) debug(index,0);
	else index=-1;
	_selected=index;
}

int	 BBItemArray::size()const{
	debug();
	return _items.size();
}

int BBItemArray::selected()const{
	debug();
	return _selected;
}

BBString *BBItemArray::item( int index )const{
	debug(index,0);
	return _items[index]->copy();
}

