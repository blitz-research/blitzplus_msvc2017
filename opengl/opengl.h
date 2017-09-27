
#ifndef OPENGL_H
#define OPENGL_H

#define WIN32_MEAN_AND_LEAN
#include <windows.h>
#include <gl/gl.h>

#include "../graphics/graphics.h"

extern const int BBQID_BBGLCONTEXT;

class BBGLContext : public BBResource{
public:
	virtual void makeCurrent()=0;
	virtual void swapBuffers()=0;
};

class BBGLDriver : public BBModule{
public:
	virtual void *extension( const char *ext )=0;
};

void			bbSetGLDriver( BBGLDriver *t );

BBGLContext*	bbGLContext();
void			bbPushGLContext( BBGLContext *t );
void			bbPopGLContext();
void*			bbGLExtension( const char *ext );

#endif