
#ifndef B2DCANVAS_H
#define B2DCANVAS_H

#include "../canvas/canvas.h"

#include "b2dbuffer.h"

BBCanvas*	b2dCreateCanvas( int x,int y,int w,int h,BBGroup *group,int style );
B2DBuffer*	b2dCanvasBuffer( BBCanvas *canvas );
void		b2dFlipCanvas( BBCanvas *canvas,bool sync );

#endif