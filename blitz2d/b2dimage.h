
#ifndef B2DIMAGE_H
#define B2DIMAGE_H

#include "b2dbuffer.h"

class B2DImage : public BBResource{
protected:
	~B2DImage();
public:
	B2DBuffer **buffers;
	unsigned  **bitmasks;
	int width,height,hx,hy,frames,flags;
	BBColor mask;

	B2DImage( int w,int h,int frames,int flags );

	void transform( float a,float b,float c,float d );

#ifdef _DEBUG
	void debug(){ _debug(this,"Image"); }
	void debug( int frame ){
		debug();
		if( frame>=0 && frame<frames ) return;
		bbError( "Illegal Image frame index:%i",frame );
	}
#else
	void debug(){}
	void debug( int frame ){}
#endif
};

B2DImage*	b2dLoadImage( BBString *file,int flags );
B2DImage*	b2dLoadAnimImage( BBString *file,int w,int h,int first,int cnt,int flags );
B2DImage*	b2dCreateImage( int w,int h,int frames,int flags );
B2DImage*	b2dCopyImage( B2DImage *img );
void		b2dFreeImage( B2DImage *img );
void		b2dMaskImage( B2DImage *img,int r,int g,int b );
void		b2dHandleImage( B2DImage *img,int x,int y );
void		b2dMidHandle( B2DImage *img );
void		b2dAutoMidHandle( bool enable );
int			b2dImageWidth( B2DImage *img );
int			b2dImageHeight( B2DImage *img );
int			b2dImageXHandle( B2DImage *img );
int			b2dImageYHandle( B2DImage *img );
B2DBuffer*	b2dImageBuffer( B2DImage *img,int frame );
int			b2dSaveImage( B2DImage *img,BBString *file,int frame );

void		b2dTFormFilter( bool enable );
void		b2dTFormImage( B2DImage *img,float a,float b,float c,float d );
void		b2dScaleImage( B2DImage *img,float w,float h );
void		b2dResizeImage( B2DImage *img,float w,float h );
void		b2dRotateImage( B2DImage *img,float d );

void		b2dDrawImage( B2DImage *img,int x,int y,int frame );
void		b2dDrawBlock( B2DImage *img,int x,int y,int frame );
void		b2dTileImage( B2DImage *img,int x,int y,int frame );
void		b2dTileBlock( B2DImage *img,int x,int y,int frame );
void		b2dDrawImageRect( B2DImage *img,int x,int y,int sx,int sy,int w,int h,int frame );
void		b2dDrawBlockRect( B2DImage *img,int x,int y,int sx,int sy,int w,int h,int frame );
void		b2dGrabImage( B2DImage *img,int x,int y,int frame );

int			b2dRectsOverlap( int x1,int y1,int w1,int h1,int x2,int y2,int w2,int h2 );
int			b2dImagesOverlap( B2DImage *i1,int x1,int y1,B2DImage *i2,int x2,int y2 );
int			b2dImagesCollide( B2DImage *i1,int x1,int y1,int f1,B2DImage *i2,int x2,int y2,int f2 );
int			b2dImageRectOverlap( B2DImage *i1,int x1,int y1,int rx,int ry,int rw,int rh );
int			b2dImageRectCollide( B2DImage *i1,int x1,int y1,int f1,int rx,int ry,int rw,int rh );

#endif