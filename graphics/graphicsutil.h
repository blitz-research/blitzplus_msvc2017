
#ifndef GRAPHICSUTIL_H
#define GRAPHICSUTIL_H

#include "graphics.h"

void		bbFlipVertical( BBGraphics *g );

BBGraphics*	bbMipHalve( BBGraphics *g );
BBGraphics*	bbTransformGraphics( BBGraphics *g,float m[2][2],bool filter,BBColor key,int *x_handle,int *y_handle );
BBGraphics*	bbLoadGraphics( BBString *file );
BBGraphics*	bbCreateKeyAlpha( BBGraphics *g );

#endif