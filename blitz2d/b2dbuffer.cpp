
#include "b2dgraphics.h"

const int BBQID_B2DBUFFER=bbAllocQueryID( "B2DBuffer" );

B2DBuffer::B2DBuffer( BBGraphics *g ):locked(0),graphics(g),context(0),ox(0),oy(0),color(~0),clsColor(0){
	context=bbGrDriver2D()->createContext( g );
	lockedpixels=new BBBank();
}

B2DBuffer::~B2DBuffer(){
	lockedpixels->release();
	context->release();
}

void *B2DBuffer::query( int qid ){
	return qid==BBQID_B2DBUFFER ? (B2DBuffer*)this : 0;
}