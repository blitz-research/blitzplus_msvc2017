
#include "win32gl.h"

static HGLRC _sharedrc;

Win32GLDriver win32GLDriver;

Win32GLContext::Win32GLContext( HDC hdc ):_hdc(hdc),_hglrc(0){

	PIXELFORMATDESCRIPTOR pf={sizeof(pf)};
	pf.nVersion=1;
	pf.dwFlags=PFD_SUPPORT_OPENGL|PFD_DRAW_TO_WINDOW|PFD_DOUBLEBUFFER;
	pf.cColorBits=24;
	pf.cDepthBits=32;
	pf.iPixelType=PFD_TYPE_RGBA;
	pf.iLayerType=PFD_MAIN_PLANE;

	int n_pf=ChoosePixelFormat( _hdc,&pf );

	if( !n_pf ) bbError( "ChoosePixelFormat failed" );
	if( !SetPixelFormat( _hdc,n_pf,&pf ) ) bbError( "SetPixelFormat failed" );

	_hglrc=wglCreateContext( _hdc );

	if( !_hglrc ) bbError( "wglCreateContext failed" );

	if( _sharedrc ){
//		if( !wglShareLists( _hglrc,_sharedrc ) ) bbError( "wglShareLists failed" );
		wglShareLists( _hglrc,_sharedrc );
	}else{
		_sharedrc=_hglrc;
	}
}

Win32GLContext::~Win32GLContext(){
	wglDeleteContext( _hglrc );
}

void Win32GLContext::makeCurrent(){
	wglMakeCurrent( _hdc,_hglrc );
}

void Win32GLContext::swapBuffers(){
	SwapBuffers( _hdc );
}

Win32GLDriver::Win32GLDriver(){
	reg( "Win32GL","Graphics","OpenGL" );
}

bool Win32GLDriver::startup(){
	bbSetGLDriver(this);
	return true;
}

void Win32GLDriver::shutdown(){
}

void *Win32GLDriver::extension( const char *ext ){
	return wglGetProcAddress( ext );
}
