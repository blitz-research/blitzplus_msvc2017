
#include "graphicsproxy.h"

BBGraphicsProxy::BBGraphicsProxy( BBGraphics *target ):
BBGraphics(target->width(),target->height()),_target(target){
	_target->retain();
}

BBGraphicsProxy::~BBGraphicsProxy(){
	_target->release();
}

void*BBGraphicsProxy::query( BBID *iid ){
	if( void *p=_target->query(iid) ) return p;
	return BBGraphics::query(iid);
}

BBGraphics *BBGraphicsProxy::createCopy(int w,int h){
	return _target->createCopy(w,h);
}

void BBGraphicsProxy::clear( int x,int y,int w,int h,BBColor color ){
	_target->clear( x,y,w,h,color );
}

void BBGraphicsProxy::read( int x,int y,int w,int h,BBColor *colors,int pitch ){
	_target->read( x,y,w,h,colors,pitch );
}

void BBGraphicsProxy::write( int x,int y,int w,int h,const BBColor *colors,int pitch ){
	_target->write( x,y,w,h,colors,pitch );
}

void BBGraphicsProxy::copy( int x,int y,int w,int h,BBGraphics *src,int sx,int sy ){
	_target->copy( x,y,w,h,src,sx,sy );
}

void BBGraphicsProxy::lock( void **pixels,int *pitch,int *format ){
	_target->lock( pixels,pitch,format );
}

void BBGraphicsProxy::unlock(){
	_target->unlock();
}

