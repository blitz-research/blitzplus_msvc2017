
#ifndef GRAPHICS_H
#define GRAPHICS_H

#include "../app/app.h"

#include "color.h"

class BBGraphics : public BBResource{
	int _w,_h;
	BBColor _key;

public:
	enum{
		GRAPHICS_MANAGED=1,
		GRAPHICS_DYNAMIC=2,
		GRAPHICS_SCRATCH=4,
	};

	enum{
		BLIT_KEYSOURCE=1
	};

	BBGraphics( int w,int h );

	virtual BBGraphics *createCopy( int w,int h );

	virtual void	setColorKey( BBColor key );

	virtual void	clear( int x,int y,int w,int h,BBColor color );
	virtual void	read( int x,int y,int w,int h,BBColor *color,int pitch );
	virtual void	write( int x,int y,int w,int h,const BBColor *colors,int pitch );
	virtual void	blit( int x,int y,int w,int h,BBGraphics *src,int sx,int sy,int flags );

	virtual void	lock( void **pixels,int *pitch,int *format )=0;
	virtual void	unlock()=0;

	int				width()const{ return _w; }
	int				height()const{ return _h; }
	BBColor			colorKey()const{ return _key; }
};

#include "pixmap.h"
#include "graphicsutil.h"

#endif