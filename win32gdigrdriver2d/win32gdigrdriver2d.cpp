
#include "win32gdigrdriver2d.h"

#include "../win32font/win32font.h"

static Win32Font *_font;

Win32GDIGrDriver2D win32GDIGrDriver2D;

struct Win32GDIGrDriver2D::Context : public BBGrContext{
	Win32GDIGraphics *graphics;
	Win32Font *font;
	BBRect cliprect;
	BBColor color;
	HPEN pen;
	HBRUSH brush;

	Context( Win32GDIGraphics *g );
	~Context();
};

Win32GDIGrDriver2D::Context::Context( Win32GDIGraphics *g ):BBGrContext(g),
graphics(g),font(_font),cliprect(0,0,g->width(),g->height()),color(BBColor::white()){

	pen=CreatePen( PS_SOLID,1,0xffffff );
	brush=CreateSolidBrush( 0xffffff );

	font->retain();
}

Win32GDIGrDriver2D::Context::~Context(){

	DeleteObject( brush );
	DeleteObject( pen );
	font->release();
}

Win32GDIGrDriver2D::Win32GDIGrDriver2D(){
	reg( "Win32GDIGrDriver2D","GrDriver2D","Native" );
}

bool Win32GDIGrDriver2D::startup(){

	startModule( "Win32GDI" );
	startModule( "Win32FontDriver" );

	_context=0;
	_graphics=0;
	_hdc=0;

	_font=new Win32Font( BBTMPSTR("courier"),10,0 );

	autoRelease( _font );

	bbSetGrDriver2D( this );

	return true;
}

void Win32GDIGrDriver2D::shutdown(){
}

void Win32GDIGrDriver2D::setContext( BBGrContext *t ){

	if( _hdc ){

		_graphics->unlockHdc();

		_hdc=0;
	}

	if( !t ){
		_context=0;
		_graphics=0;
		BBGrDriver2D::setContext(0);
		return;
	}

	Context *p=dynamic_cast<Context*>(t);

	if( !p ) bbError( "Incompatible graphics context" );

	_context=p;

	_graphics=_context->graphics;

	_hdc=_graphics->lockHdc();

	BBGrDriver2D::setContext( t );
}

BBGrContext *Win32GDIGrDriver2D::createContext( BBGraphics *g ){

	Win32GDIGraphics *p=dynamic_cast<Win32GDIGraphics*>(g);

	if( !p ) bbError( "Incompatible graphics" );

	Context *t=new Context(p);

	autoRelease(t);

	return t;
}

void Win32GDIGrDriver2D::setFont( BBFont *f ){

	Win32Font *p=dynamic_cast<Win32Font*>(f);

	if( !p ) bbError( "Incompatible font" );

	_context->font->release();

	_context->font=p;

	_context->font->retain();
}

void Win32GDIGrDriver2D::setClipRect( const BBRect &r ){

	_context->cliprect=r;
}

void Win32GDIGrDriver2D::setColor( BBColor c ){

	COLORREF cr=(c.b<<16)|(c.g<<8)|c.r;

	_context->color=c;

	DeleteObject( _context->pen );

	DeleteObject( _context->brush );

	_context->pen=CreatePen( PS_SOLID,1,cr );

	_context->brush=CreateSolidBrush( cr );
}

BBFont *Win32GDIGrDriver2D::font(){
	return _context->font;
}

const BBRect &Win32GDIGrDriver2D::clipRect(){
	return _context->cliprect;
}

BBColor Win32GDIGrDriver2D::color(){
	return _context->color;
}

void Win32GDIGrDriver2D::plot( int x,int y ){
	BBGrDriver2D::plot( x,y );
}

void Win32GDIGrDriver2D::line( int x1,int y1,int x2,int y2 ){
	POINT ps[2];
	ps[0].x=x1;ps[0].y=y1;
	ps[1].x=x2;ps[1].y=y2;

	SelectObject( _hdc,_context->pen );

	Polyline( _hdc,ps,2 );
}

void Win32GDIGrDriver2D::rect( int x,int y,int w,int h,bool solid ){

	if( solid ){

		RECT r={x,y,x+w,y+h};

		FillRect( _hdc,&r,_context->brush );

		return;
	}

	SelectObject( _hdc,_context->pen );

	SelectObject( _hdc,win32GDI.nullBrush() );

	Rectangle( _hdc,x,y,x+w,y+h );
}

void Win32GDIGrDriver2D::oval( int x,int y,int w,int h,bool solid ){

	if( solid ){
		SelectObject( _hdc,win32GDI.nullPen() );
		SelectObject( _hdc,_context->brush );
	}else{
		SelectObject( _hdc,_context->pen );
		SelectObject( _hdc,win32GDI.nullBrush() );
	}

	Ellipse( _hdc,x,y,x+w,y+h );
}

void Win32GDIGrDriver2D::blit( int x,int y,int w,int h,BBGraphics *src,int sx,int sy,int flags ){
	BBGrDriver2D::blit( x,y,w,h,src,sx,sy,flags );
}

void Win32GDIGrDriver2D::text( int x,int y,BBString *str ){

	unsigned argb=color().argb;

	SetBkMode( _hdc,TRANSPARENT );

	SelectObject( _hdc,_context->font->hfont() );

	SetTextColor( _hdc,((argb&255)<<16)|(argb&0xff00)|((argb>>16)&255) );

	ExtTextOut( _hdc,x,y,ETO_CLIPPED,(RECT*)&_context->cliprect,str->c_str(),str->size(),0 );
}

BBGraphics *Win32GDIGrDriver2D::createGraphics( int w,int h,int fmt,int flags ){

	Win32GDIGraphics *g=new Win32GDIGraphics( w,h,24 );

	autoRelease(g);

	return g;
}
