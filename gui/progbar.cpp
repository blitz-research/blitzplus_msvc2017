
#include "progbar.h"

BBProgBar::BBProgBar( BBGroup *group,int style ):BBGadget(group,style){
}

void BBProgBar::setProgress( float t ){
	_progress=t;
}

void bbUpdateProgBar( BBProgBar *prog,float t ){
	prog->debug();
	prog->setProgress(t);
}