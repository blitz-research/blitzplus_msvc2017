
#ifndef WIN32GL_H
#define WIN32GL_H

#include "../opengl/opengl.h"

#include "win32gltex.h"
#include "win32glgraphics.h"

class Win32GLContext : public BBGLContext{
	HDC _hdc;
	HGLRC _hglrc;
public:
	Win32GLContext( HDC hdc );
	~Win32GLContext();

	void	makeCurrent();
	void	swapBuffers();

	HDC		hdc()const{ return _hdc; }
	HGLRC	hglrc()const{ return _hglrc; }
};

class Win32GLDriver : public BBGLDriver{
public:
	Win32GLDriver();

	bool startup();
	void shutdown();

	void *extension( const char *ext );
};

extern Win32GLDriver win32GLDriver;
static Win32GLDriver *_win32GLDriver=&win32GLDriver;

#endif