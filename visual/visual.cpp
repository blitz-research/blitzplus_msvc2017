
#include "visual.h"

BBVisual::BBVisual():_mx(0),_my(0){
}

int BBVisual::width(){
	if( BBGraphics *g=graphics() ) return g->width();
	return 1;
}

int BBVisual::height(){
	if( BBGraphics *g=graphics() ) return g->height();
	return 1;
}

BBGraphics *BBVisual::graphics(){
	return 0;
}

void BBVisual::setPointer( int n ){
}

void BBVisual::moveMouse( int x,int y ){
	_mx=x;_my=y;
}

int BBVisual::mouseX(){
	return _mx;
}

int BBVisual::mouseY(){
	return _my;
}