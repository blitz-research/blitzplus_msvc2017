
#ifndef GRAPHICSPROXY_H
#define GRAPHICSPROXY_H

#include "graphics.h"

class BBGraphicsProxy : public BBGraphics{
	BBGraphics *_target;

protected:
	BBGraphics *target()const{ return _target; }

public:
	BBGraphicsProxy( BBGraphics *target );
	~BBGraphicsProxy();

	virtual void*	query( BBID *iid );

	virtual BBGraphics* createCopy(int w,int h);
	virtual void	clear( int x,int y,int w,int h,BBColor color );
	virtual void	read( int x,int y,int w,int h,BBColor *color,int pitch );
	virtual void	write( int x,int y,int w,int h,const BBColor *colors,int pitch );
	virtual void	copy( int x,int y,int w,int h,BBGraphics *src,int sx,int sy );
	virtual void	lock( void **pixels,int *pitch,int *format );
	virtual void	unlock();
};

#endif