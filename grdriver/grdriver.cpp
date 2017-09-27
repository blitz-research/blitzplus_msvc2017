
#include "grdriver.h"

BBGrContext::BBGrContext( BBGraphics *g ):_graphics(g){
	_graphics->retain();
}

BBGrContext::~BBGrContext(){
	_graphics->release();
}

void BBGrDriver::setContext( BBGrContext *t ){
	if( _context ) _context->release();
	if( _context=t ) _context->retain();
}