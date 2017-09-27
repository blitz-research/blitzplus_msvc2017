
#ifndef WIN32GLGRAPHICS_H
#define WIN32GLGRAPHICS_H
#define WIN32_LEAN_AND_MEAN

#include "../win32dib/win32dib.h"

#include <windows.h>
#include <gl/gl.h>

class Win32GLTex;
class Win32GLContext;

class Win32GLGraphics : public BBGraphics{
	Win32GLContext *_glContext;
	Win32DIBGraphics *_dib;
	Win32GLTex *_tex;
	int _locked;
	bool _dirty;
protected:
	~Win32GLGraphics();
public:
	Win32GLGraphics( int w,int h );
	Win32GLGraphics( int w,int h,Win32GLContext *glContext );

	void*	query( int qid );

	BBGraphics *createCopy( int w,int h );

	void	setColorKey( BBColor key );

	void	clear( int x,int y,int w,int h,BBColor color );
	void	write( int x,int y,int w,int h,const BBColor *colors,int pitch );
	void	blit( int x,int y,int w,int h,BBGraphics *src,int sx,int sy,int flags );
	void	lock( void **pixels,int *pitch,int *format );
	void	unlock();

	Win32GLContext *glContext(){ return _glContext; }

	HDC		lockHdc();
	void	unlockHdc();

	Win32GLTex *tex();
};

#endif