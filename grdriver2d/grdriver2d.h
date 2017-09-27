
#ifndef GRDRIVER2D_H
#define GRDRIVER2D_H

#include "../grdriver/grdriver.h"

#include "../font/font.h"

struct BBRect{
	int left,top,right,bottom;

	BBRect(){}
	BBRect( int l,int t,int r,int b ):left(l),top(t),right(r),bottom(b){}

	int x()const{ return left; }
	int y()const{ return top; }
	int width()const{ return right-left; }
	int height()const{ return bottom-top; }

	bool clip( BBRect *dst )const;
	bool clip( BBRect *dst,BBRect *src )const;
	bool overlaps( const BBRect &r )const{ return left<r.right && right>r.left && top<r.bottom && bottom>r.top; }
};

class BBGrDriver2D : public BBGrDriver{
	BBGraphics *_graphics;

public:
	virtual void setContext( BBGrContext *t );

	virtual void setFont( BBFont *font )=0;
	virtual void setColor( BBColor color )=0;
	virtual void setClipRect( const BBRect& clip )=0;

	virtual BBFont*	font()=0;
	virtual BBColor	color()=0;
	virtual const BBRect& clipRect()=0;

	virtual void plot( int x,int y );
	virtual void line( int x1,int y1,int x2,int y2 );
	virtual void rect( int x,int y,int w,int h,bool solid );
	virtual void oval( int x,int y,int w,int h,bool solid );
	virtual void blit( int x,int y,int w,int h,BBGraphics *src,int sx,int sy,int flags );
	virtual void text( int x,int y,BBString *text );

	virtual BBGraphics *createGraphics( int w,int h,int fmt,int flags )=0;
};

void bbSetGrDriver2D( BBGrDriver2D *t );

BBGrDriver2D *bbGrDriver2D();

#endif
