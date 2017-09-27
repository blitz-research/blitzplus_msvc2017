
#ifndef PIXMAP_H
#define PIXMAP_H

#include "graphics.h"

class BBPixmap : public BBGraphics{
	void *_pixels;
	int _pitch,_fmt,_align;
protected:
	~BBPixmap();
public:
	BBPixmap( int w,int h,int fmt=BB_XRGB8888,int align=4 );

	BBGraphics *createCopy( int w,int h );

	void	lock( void **pixels,int *pitch,int *format );
	void	unlock();

	void*	pixels(){ return _pixels; }
	int		pitch()const{ return _pitch; }
	int		format()const{ return _fmt; }
};

#endif