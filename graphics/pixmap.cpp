
#include "pixmap.h"

BBPixmap::BBPixmap( int w,int h,int fmt,int align ):BBGraphics(w,h),
_fmt(fmt),_align(align){
	_pitch=(w*bbBytesPerPixel(_fmt)+_align-1)/_align*_align;
	_pixels=new BBColor[_pitch*h];
}

BBPixmap::~BBPixmap(){
	delete[] _pixels;
}

BBGraphics *BBPixmap::createCopy( int w,int h ){
	return new BBPixmap( w,h,_fmt,_align );
}

void BBPixmap::lock( void **pixels,int *pitch,int *format ){
	*pixels=_pixels;
	*pitch=_pitch;
	*format=_fmt;
}

void BBPixmap::unlock(){
}

