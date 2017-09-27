
#include "win32gdi.h"

Win32GDIGraphics::Win32GDIGraphics( int w,int h,int bits ):BBGraphics( w,h ),_bits(bits),_dib(0){

	_dib=new Win32DIBGraphics( w,h,_bits );
}

Win32GDIGraphics::~Win32GDIGraphics(){

	_dib->release();
}

BBGraphics *Win32GDIGraphics::createCopy( int w,int h ){

	return new Win32GDIGraphics( w,h,_bits );
}

void Win32GDIGraphics::setColorKey( BBColor key ){

	BBGraphics::setColorKey( key );
}

void Win32GDIGraphics::lock( void **pixels,int *pitch,int *format ){

	*pixels=_dib->pixels();
	*pitch=_dib->pitch();
	*format=_dib->format();
}

void Win32GDIGraphics::unlock(){
}

HDC  Win32GDIGraphics::lockHdc(){

	return _dib->lockHdc();
}

void Win32GDIGraphics::unlockHdc(){

	_dib->unlockHdc();
}
