
#include "win32dib.h"

Win32DIBGraphics::Win32DIBGraphics( int w,int h,int bits ):BBGraphics( w,h ),_hlocks(0),_bits(bits){

	switch( _bits ){
	case 16:_format=BB_XRGB1555;break;
	case 24:_format=BB_RGB888;break;
	case 32:_format=BB_ARGB8888;break;
	default:bbError( "Illegal DIB bits" );
	}

	BITMAPINFOHEADER bi={sizeof(bi)};

	bi.biWidth=w;
	bi.biHeight=-h;
	bi.biPlanes=1;
	bi.biBitCount=_bits;
	bi.biCompression=BI_RGB;

	_hbitmap=CreateDIBSection( GetDC(0),(BITMAPINFO*)&bi,DIB_RGB_COLORS,&_pixels,0,0 );

	if( !_hbitmap )	bbError( "CreateDIBSection failed:%i %i",w,h );

	_pitch=(w*(_bits/8)+3)&~3;
}

Win32DIBGraphics::~Win32DIBGraphics(){
	if( _hlocks ) DeleteDC( _hdc );
	DeleteObject( _hbitmap );
}

BBGraphics *Win32DIBGraphics::createCopy( int w,int h ){
	return new Win32DIBGraphics( w,h,_bits );
}

void Win32DIBGraphics::lock( void **pixels,int *pitch,int *format ){
	*pixels=_pixels;
	*pitch=_pitch;
	*format=_format;
}

void Win32DIBGraphics::unlock(){
}

HDC Win32DIBGraphics::lockHdc(){
	if( !_hlocks++ ){
		_hdc=CreateCompatibleDC( 0 );
		SelectObject( _hdc,_hbitmap );
	}
	return _hdc;
}

void Win32DIBGraphics::unlockHdc(){
	if( !--_hlocks ){
		DeleteDC( _hdc );
	}
}