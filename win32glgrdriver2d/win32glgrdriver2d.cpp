
#include "win32glgrdriver2d.h"

#include "../win32font/win32font.h"

static Win32Font *_font;

Win32GLGrDriver2D win32GLGrDriver2D;

static int GLFontQid=bbAllocQueryID( "GLFont" );

class GLFont : public BBResource{
protected:
	~GLFont();
public:
	int listbase;

	GLFont( Win32Font *p );

	void *query( int qid );
};

GLFont::GLFont( Win32Font *p ){

	listbase=glGenLists( 256 );

	HDC hdc=CreateCompatibleDC(0);
	HFONT t_font=(HFONT)SelectObject( hdc,p->hfont() );

	wglUseFontBitmaps( hdc,0,255,listbase );

	SelectObject( hdc,t_font );
	DeleteDC( hdc );

	p->attach( this );
}

GLFont::~GLFont(){
	glDeleteLists( listbase,256 );
}

void *GLFont::query( int qid ){
	if( qid==GLFontQid ) return (GLFont*)this;
	return 0;
}

struct Win32GLGrDriver2D::Context : public BBGrContext{
	Win32GLGraphics *graphics;
	Win32Font *font;
	GLFont *glFont;
	BBRect cliprect;
	BBColor color;

	Context( Win32GLGraphics *g );
	~Context();
};

Win32GLGrDriver2D::Context::Context( Win32GLGraphics *g ):BBGrContext(g),
graphics(g),font(_font),glFont(0),cliprect(0,0,g->width(),g->height()),color(BBColor::white()){

	font->retain();

	if( !graphics->glContext() ) return;

	bbPushGLContext( graphics->glContext() );

	glViewport( 0,0,g->width(),g->height() );

	glMatrixMode( GL_PROJECTION );
	glLoadIdentity();
	glOrtho( 0,g->width(),g->height(),0,0,1 );

	glMatrixMode( GL_MODELVIEW );
	glLoadIdentity();

	glAlphaFunc( GL_GREATER,.5f );

	glColor3ub( 255,255,255 );

	float pm[2]={0,1};

	glPixelMapfv( GL_PIXEL_MAP_I_TO_R,2,pm );
	glPixelMapfv( GL_PIXEL_MAP_I_TO_G,2,pm );
	glPixelMapfv( GL_PIXEL_MAP_I_TO_B,2,pm );

	bbPopGLContext();
}

Win32GLGrDriver2D::Context::~Context(){

	font->release();
}

Win32GLGrDriver2D::Win32GLGrDriver2D(){
	reg( "Win32GLGrDriver2D","GrDriver2D","OpenGL" );
}

bool Win32GLGrDriver2D::startup(){

	startModule( "Win32GL" );
	startModule( "Win32FontDriver" );

	_context=0;
	_graphics=0;
	_glContext=0;

	_font=new Win32Font( BBTMPSTR("courier"),10,0 );

	autoRelease( _font );

	bbSetGrDriver2D( this );

	return true;
}

void Win32GLGrDriver2D::shutdown(){
}

void Win32GLGrDriver2D::setContext( BBGrContext *t ){

	if( !t ){
		_context=0;
		_graphics=0;
		_glContext=0;
		BBGrDriver2D::setContext( 0 );
		return;
	}

	Context *p=dynamic_cast<Context*>(t);

	if( !p ) bbError( "Incompatible graphics context" );

	_context=p;

	_graphics=_context->graphics;

	_glContext=_graphics->glContext();

	if( _glContext ) _glContext->makeCurrent();

	BBGrDriver2D::setContext( t );
}

BBGrContext *Win32GLGrDriver2D::createContext( BBGraphics *g ){

	Win32GLGraphics *p=dynamic_cast<Win32GLGraphics*>(g);

	if( !p ) bbError( "Incompatible graphics" );

	Context *t=new Context(p);

	autoRelease(t);

	return t;
}

void Win32GLGrDriver2D::setFont( BBFont *f ){

	Win32Font *p=dynamic_cast<Win32Font*>(f);

	if( !p ) bbError( "Incompatible font" );

	_context->font->release();

	_context->font=p;

	_context->font->retain();

	_context->glFont=(GLFont*)p->findAttached( GLFontQid );
}

void Win32GLGrDriver2D::setClipRect( const BBRect &r ){

	_context->cliprect=r;

	if( !_glContext ) return;

	if( r.left==0 && r.top==0 && r.right==_graphics->width() && r.bottom==_graphics->height() ){
		glDisable( GL_SCISSOR_TEST );
		return;
	}
	glScissor( r.left,_graphics->height()-r.bottom,r.width(),r.height() );
	glEnable( GL_SCISSOR_TEST );
}

void Win32GLGrDriver2D::setColor( BBColor c ){

	_context->color=c;

	if( !_glContext ) return;

	glColor3ub( c.r,c.g,c.b );

	float pm_r[]={0,c.r/255.0f};
	float pm_g[]={0,c.g/255.0f};
	float pm_b[]={0,c.b/255.0f};

	glPixelMapfv( GL_PIXEL_MAP_I_TO_R,2,pm_r );
	glPixelMapfv( GL_PIXEL_MAP_I_TO_G,2,pm_g );
	glPixelMapfv( GL_PIXEL_MAP_I_TO_B,2,pm_b );
}

BBFont *Win32GLGrDriver2D::font(){
	return _context->font;
}

const BBRect &Win32GLGrDriver2D::clipRect(){
	return _context->cliprect;
}

BBColor Win32GLGrDriver2D::color(){
	return _context->color;
}

void Win32GLGrDriver2D::plot( int x,int y ){

	if( !_glContext ){
		BBGrDriver2D::plot(x,y);
		return;
	}

	glBegin( GL_POINTS );

	glVertex2i( x,y );

	glEnd();
}

void Win32GLGrDriver2D::line( int x1,int y1,int x2,int y2 ){

	if( !_glContext ){
		BBGrDriver2D::line( x1,y1,x2,y2 );
		return;
	}

	glBegin( GL_LINES );

	glVertex2i( x1,y1 );
	glVertex2i( x2,y2 );

	glEnd();
}

void Win32GLGrDriver2D::rect( int x,int y,int w,int h,bool solid ){

	if( !_glContext ){
		BBGrDriver2D::rect( x,y,w,h,solid );
		return;
	}

	if( solid ){
		glBegin( GL_QUADS );
		glVertex2i( x,y );
		glVertex2i( x+w,y );
		glVertex2i( x+w,y+h );
		glVertex2i( x,y+h );
		glEnd();
	}else{
		glBegin( GL_LINE_LOOP );
		glVertex2i( x,y );
		glVertex2i( x+w-1,y );
		glVertex2i( x+w-1,y+h-1 );
		glVertex2i( x,y+h-1 );
		glEnd();
	}
}

void Win32GLGrDriver2D::blit( int x,int y,int w,int h,BBGraphics *src,int sx,int sy,int flags ){

	if( !_glContext ){
		BBGrDriver2D::blit( x,y,w,h,src,sx,sy,flags );
		return;
	}

	Win32GLGraphics *g=dynamic_cast<Win32GLGraphics*>(src);

	Win32GLTex *t=g ? g->tex() : 0;

	if( !t ){
		BBGrDriver2D::blit( x,y,w,h,src,sx,sy,flags );
		return;
	}

	const float *tr=t->texRect();

	float s0=(float)sx/(float)src->width()*tr[2];
	float t0=(float)sy/(float)src->height()*tr[3];
	float s1=(float)(sx+w)/(float)src->width()*tr[2];
	float t1=(float)(sy+h)/(float)src->height()*tr[3];

	glEnable( GL_TEXTURE_2D );

	if( flags & BBGraphics::BLIT_KEYSOURCE ) glEnable( GL_ALPHA_TEST );

	t->bind();

	glTexEnvi( GL_TEXTURE_ENV,GL_TEXTURE_ENV_MODE,GL_REPLACE );

	glBegin( GL_QUADS );

	glTexCoord2f( s0,t0 );
	glVertex2i( x,y );

	glTexCoord2f( s1,t0 );
	glVertex2i( x+w,y );

	glTexCoord2f( s1,t1 );
	glVertex2i( x+w,y+h );

	glTexCoord2f( s0,t1 );
	glVertex2i( x,y+h );

	glEnd();

	if( flags & BBGraphics::BLIT_KEYSOURCE ) glDisable( GL_ALPHA_TEST );

	glDisable( GL_TEXTURE_2D );
}

void Win32GLGrDriver2D::text( int x,int y,BBString *str ){

	if( !_glContext ){

		HDC hdc=_graphics->lockHdc();
		unsigned argb=color().argb;
		BBRect rect=_context->cliprect;
		SetBkMode( hdc,TRANSPARENT );
		SelectObject( hdc,_context->font->hfont() );
		SetTextColor( hdc,((argb&255)<<16)|(argb&0xff00)|((argb>>16)&255) );
		ExtTextOut( hdc,x,y,ETO_CLIPPED,(RECT*)&rect,str->c_str(),str->size(),0 );
		_graphics->unlockHdc();
		return;
	}

	GLFont *g=_context->glFont;

	if( !g ){
		g=_context->glFont=new GLFont( _context->font );
	}

	glListBase( g->listbase );

	glRasterPos2i( x,y+_context->font->ascent() );

	glCallLists( str->size(),GL_UNSIGNED_BYTE,str->c_str() );

	glRasterPos2i( 0,_graphics->height() );
}

BBGraphics *Win32GLGrDriver2D::createGraphics( int w,int h,int fmt,int flags ){

	Win32GLGraphics *g=new Win32GLGraphics( w,h );

	autoRelease(g);

	return g;
}
