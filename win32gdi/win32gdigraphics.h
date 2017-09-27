
#ifndef WIN32GDIGRAPHICS_H
#define WIN32GDIGRAPHICS_H

#include "../win32dib/win32dib.h"

#include "../graphics/graphics.h"

class Win32GDIGraphics : public BBGraphics{
	int _bits;
	Win32DIBGraphics *_dib;
protected:
	~Win32GDIGraphics();
public:
	Win32GDIGraphics( int w,int h,int bits );

	BBGraphics *createCopy( int w,int h );

	void	setColorKey( BBColor key );

	void	lock( void **pixels,int *pitch,int *format );
	void	unlock();

	HDC		lockHdc();
	void	unlockHdc();
};

#endif
