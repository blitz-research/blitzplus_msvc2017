
#include "win32gui.h"

Win32IconStrip::Win32IconStrip( BBGraphics *graphics ):_imagelist(0){

	int sz=graphics->height();

	_w=_h=sz;
	_n=graphics->width()/sz;

	BBColor col00;
	graphics->read( 0,0,1,1,&col00,0 );
	unsigned trans=col00.toPixel( BB_RGB888 );
	trans&=0xffffff;

	_blanks.resize( _n );
	for( int i=0;i<_n;++i ){
		_blanks[i]=false;
		for( int x=i*_w;x<(i+1)*_w;++x ){
			for( int y=0;y<_h;++y ){
				BBColor col;
				graphics->read( x,y,1,1,&col,0 );
				unsigned rgb=col.toPixel( BB_RGB888 ) & 0xffffff;
				if( rgb!=trans ) goto out;
			}
		}
		_blanks[i]=true;
		out:;
	}

	HBITMAP bm=win32GuiDriver.createBitmap( graphics );

	_imagelist=ImageList_Create( sz,sz,ILC_COLOR24|ILC_MASK,0,1 );

	ImageList_AddMasked( _imagelist,bm,trans );

	DeleteObject( bm );
}

Win32IconStrip::~Win32IconStrip(){
	if( _imagelist ) ImageList_Destroy( _imagelist );
}

int Win32IconStrip::icons(){
	return _n;
}

int Win32IconStrip::iconWidth(){
	return _w;
}

int Win32IconStrip::iconHeight(){
	return _h;
}

bool Win32IconStrip::iconIsBlank( int i ){
	return _blanks[i];
}