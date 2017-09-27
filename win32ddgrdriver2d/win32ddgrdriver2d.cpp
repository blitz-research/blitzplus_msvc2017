
#include "win32ddgrdriver2d.h"

#include "../win32font/win32font.h"

static Win32Font *_font;

Win32DDGrDriver2D win32DDGrDriver2D;

struct Win32DDGrDriver2D::Context : public BBGrContext{
	Win32DDGraphics *graphics;
	Win32Font *font;
	BBRect cliprect;
	BBColor color;

	Context( Win32DDGraphics *g ):BBGrContext(g),
	graphics(g),font(_font),cliprect(0,0,g->width(),g->height()),color(BBColor::white()){
		font->retain();
	}

	~Context(){
		font->release();
	}
};

Win32DDGrDriver2D::Win32DDGrDriver2D(){
	reg( "Win32DDGrDriver2D","GrDriver2D","DirectDraw" );
}

bool Win32DDGrDriver2D::startup(){

	startModule( "Win32DD" );
	startModule( "Win32FontDriver" );

	_context=0;
	_graphics=0;

	_font=new Win32Font( BBTMPSTR("courier"),10,0 );

	bbSetGrDriver2D( this );

	return true;
}

void Win32DDGrDriver2D::shutdown(){

	_font->release();
}

void Win32DDGrDriver2D::setContext( BBGrContext *t ){

	if( !t ){
		_context=0;
		_graphics=0;
		BBGrDriver2D::setContext( 0 );
		return;
	}

	Context *p=dynamic_cast<Context*>(t);

	if( !p ) bbError( "Incompatible graphics context" );

	_context=p;

	_graphics=_context->graphics;

	BBGrDriver2D::setContext( t );
}

BBGrContext *Win32DDGrDriver2D::createContext( BBGraphics *g ){

	Win32DDGraphics *p=dynamic_cast<Win32DDGraphics*>(g);

	if( !p ) bbError( "Incompatible graphics" );

	Context *t=new Context(p);

	autoRelease(t);

	return t;
}

void Win32DDGrDriver2D::setFont( BBFont *f ){

	Win32Font *p=dynamic_cast<Win32Font*>(f);

	if( !p ) bbError( "Incompatible font" );

	_context->font->release();

	_context->font=p;

	_context->font->retain();
}

void Win32DDGrDriver2D::setClipRect( const BBRect &r ){

	_context->cliprect=r;
}

void Win32DDGrDriver2D::setColor( BBColor c ){

	_context->color=c;
}

BBFont *Win32DDGrDriver2D::font(){
	return _context->font;
}

const BBRect &Win32DDGrDriver2D::clipRect(){
	return _context->cliprect;
}

BBColor Win32DDGrDriver2D::color(){
	return _context->color;
}

void Win32DDGrDriver2D::text( int x,int y,BBString *string ){

	HDC hdc=_graphics->lockHdc();

	unsigned argb=color().argb;
	BBRect rect=_context->cliprect;

	SetBkMode( hdc,TRANSPARENT );
	SelectObject( hdc,_context->font->hfont() );
	SetTextColor( hdc,((argb&255)<<16)|(argb&0xff00)|((argb>>16)&255) );
	ExtTextOut( hdc,x,y,ETO_CLIPPED,(RECT*)&rect,string->c_str(),string->size(),0 );

	_graphics->unlockHdc();
}

BBGraphics *Win32DDGrDriver2D::createGraphics( int w,int h,int fmt,int flags ){

	int ty;

	if( flags & BBGraphics::GRAPHICS_MANAGED ) ty=Win32DDGraphics::TYPE_MANAGED;
	else if( flags & BBGraphics::GRAPHICS_DYNAMIC ) ty=Win32DDGraphics::TYPE_VIDMEM;
	else ty=Win32DDGraphics::TYPE_SYSMEM;

	Win32DDGraphics *g=new Win32DDGraphics( w,h,ty );

	autoRelease(g);

	return g;
}

