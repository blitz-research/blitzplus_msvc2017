
#include "canvas.h"

BBCanvas::BBCanvas( BBGroup *group,int style ):BBGroup(group,style),
_pmx(0),_pmy(0){
}

BBCanvas::~BBCanvas(){
}

BBCanvasDriver *_driver;

void bbSetCanvasDriver( BBCanvasDriver *t ){
	_driver=t;
}

BBCanvas *bbCreateCanvas( int x,int y,int w,int h,BBGroup *group,int style ){
	BBCanvas *gadget=_driver->createCanvas( group,style );
	gadget->setShape( x,y,w,h );
	gadget->setVisible( true );
	gadget->lockLayout();
	return gadget;
}

BBGraphics *bbCanvasGraphics( BBCanvas *canvas ){
	canvas->debug();
	return canvas->graphics();
}

void bbFlipCanvas( BBCanvas *canvas,bool sync ){
	canvas->debug();
	canvas->flip( sync );
}