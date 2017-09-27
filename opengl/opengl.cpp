
#include "opengl.h"

#include <vector>

static BBGLContext *_cur;
static std::vector<BBGLContext*> _stack;
static BBGLDriver *_driver;

const int BBQID_BBGLCONTEXT=bbAllocQueryID( "BBGLContext" );

BBGLContext *bbGLContext(){
	return _cur;
}

void bbPushGLContext( BBGLContext *t ){
	_stack.push_back( _cur );
	_cur=t;
	if( _cur ) _cur->makeCurrent();
}

void bbPopGLContext(){
	_cur=_stack.back();
	_stack.pop_back();
	if( _cur ) _cur->makeCurrent();
}

void bbSetGLDriver( BBGLDriver *t ){
	_driver=t;
}

void *bbGLExtension( const char *ext ){
	return _driver->extension( ext );
}