
#include "win32canvas.h"

Win32Canvas::Win32Canvas( BBGroup *group,int style ):BBCanvas( group,style ),_graphics(0){
}

Win32Canvas::~Win32Canvas(){
	if( _graphics ) _graphics->release();
}

void Win32Canvas::setGraphics( BBGraphics *g ){
	if( _graphics ) _graphics->release();
	if( _graphics=g ){
		_graphics->retain();
		_gadget.setMouseArea( _graphics->width(),_graphics->height() );
	}else{
		_gadget.setMouseArea( 0,0 );
	}
}

void *Win32Canvas::query( int qid ){
	if( void *p=_gadget.query( qid ) ) return p;
	return BBCanvas::query( qid );
}

void Win32Canvas::setFont( BBFont *font ){
	_gadget.setFont(font);
	BBCanvas::setFont(font);
}

void Win32Canvas::setText( BBString *text ){
	_gadget.setText(text);
	BBCanvas::setText(text);
}

void Win32Canvas::setShape( int x,int y,int w,int h ){
	_gadget.setShape(x,y,w,h);
	BBCanvas::setShape(x,y,w,h); 
}

void Win32Canvas::setVisible( bool visible ){
	_gadget.setVisible(visible);
	BBCanvas::setVisible(visible);
}

void Win32Canvas::setEnabled( bool enabled ){
	_gadget.setEnabled(enabled);
	BBCanvas::setEnabled(enabled);
}

void Win32Canvas::activate(){
	_gadget.activate();
	BBCanvas::activate();
}

void Win32Canvas::clientShape( int *x,int *y,int *w,int *h ){
	_gadget.clientShape(x,y,w,h);
}

BBGraphics *Win32Canvas::graphics(){
	return _graphics;
}

void Win32Canvas::setPointer( int n ){
	_gadget.setPointer( n );
}

void Win32Canvas::moveMouse( int x,int y ){
	_gadget.moveMouse( x,y );
}

int  Win32Canvas::mouseX(){
	return _gadget.mouseX();
}

int  Win32Canvas::mouseY(){
	return _gadget.mouseY();
}

LRESULT Win32Canvas::wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc ){
	switch( msg ){
	case WM_CLOSE:
		return 0;
	case WM_ERASEBKGND:
		return 1;
	}
	return CallWindowProc( proc,hwnd,msg,wp,lp );
}
