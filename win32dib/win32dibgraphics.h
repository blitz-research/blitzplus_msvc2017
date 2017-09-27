
#ifndef WIN32DIBGRAPHICS_H
#define WIN32DIBGRAPHICS_H
#define WIN32_LEAN_AND_MEAN

#include <windows.h>

#include "../graphics/graphics.h"

class Win32DIBGraphics : public BBGraphics{
	HDC _hdc;
	int _hlocks;
	HBITMAP _hbitmap;
	void *_pixels;
	int _bits,_pitch,_format;
protected:
	~Win32DIBGraphics();
public:
	Win32DIBGraphics( int w,int h,int bits );

	BBGraphics *createCopy( int w,int h );

	void	lock( void **pixels,int *pitch,int *format );
	void	unlock();

	void*	pixels()const{ return _pixels; }
	int		pitch()const{ return _pitch; }
	int		format()const{ return _format; }

	HDC		lockHdc();
	void	unlockHdc();

	HBITMAP	hbitmap()const{ return _hbitmap; }
};

#endif
