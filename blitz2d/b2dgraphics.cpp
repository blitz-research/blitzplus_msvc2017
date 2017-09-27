
#include "b2dgraphics.h"
#include "b2devent.h"

#include <stdio.h>

B2DGraphicsDriver b2dGraphicsDriver;

BBScreen *b2dScreen;
BBGrContext *b2dContext;
BBGraphics *b2dGfx;
B2DBuffer *b2dBuffer;

static int _screenFlags;
static B2DBuffer *backBuffer;
static B2DBuffer *b2dDesktop;

B2DGraphicsDriver::B2DGraphicsDriver(){
	reg( "B2DGraphicsDriver","Graphics" );
}

bool B2DGraphicsDriver::startup(){
	BBModule::startDrivers( "Screen" );
	BBModule::startDrivers( "GrDriver2D" );
	b2dScreen=bbDesktopScreen();
	return true;
}

void B2DGraphicsDriver::shutdown(){
	b2dEndGraphics();
	if( b2dDesktop ) b2dDesktop->release();
}

//1=fullscreen,
//2=windowed
//3=fullscreen, GUI compatible

void b2dGraphics( int w,int h,int d,int flags ){
	b2dEndGraphics();

#ifdef _DEBUG
	if( !flags ) flags=2;
#else
	if( !flags ) flags=1;
#endif

	_screenFlags=BBScreen::SCREEN_DOUBLEBUFFER;

	switch( flags ){
	case 1:_screenFlags=BBScreen::SCREEN_DOUBLEBUFFER;break;
	case 2:_screenFlags=BBScreen::SCREEN_DOUBLEBUFFER|BBScreen::SCREEN_VIRTUAL;break;
	case 3:_screenFlags=BBScreen::SCREEN_DOUBLEBUFFER|BBScreen::SCREEN_GUICOMPATIBLE;break;
	default:bbError( "Illegal Graphics flags" );
	}

	int t_fmt=BB_RGB565;
	switch( d ){
	case 24:t_fmt=BB_RGB888;break;
	case 32:t_fmt=BB_XRGB8888;break;
	}

	BBString *t=bbAppTitle();
	b2dScreen=bbCreateScreen( t,w,h,t_fmt,_screenFlags );
	t->release();

	BBGraphics *g=b2dScreen->graphics();
	backBuffer=new B2DBuffer( g );
	b2dSetBuffer( backBuffer );

	b2dCls();
	b2dFlip(false);
	b2dCls();

	if( !(_screenFlags & BBScreen::SCREEN_VIRTUAL) ){
		b2dScreen->setPointer(0);
		b2dScreen->moveMouse(0,0);
		bbAutoSuspend(true);
	}
}

void b2dEndGraphics(){
	if( backBuffer ){

		bbGrDriver2D()->setContext(0);

		backBuffer->release();
		backBuffer=0;

		b2dScreen->release();
		b2dScreen=bbDesktopScreen();

		b2dScreen->activateScreen();

		if( !(_screenFlags & BBScreen::SCREEN_VIRTUAL) ){
			bbAutoSuspend(false);
		}
	}
}

int b2dCountGfxDrivers(){
	return 3;
}

BBString*b2dGfxDriverName( int n ){
	switch( n ){
	case 1:return new BBString( "Native" );
	case 2:return new BBString( "OpenGL" );
	case 3:return new BBString( "DirectDraw" );
	default:bbError( "Illegal graphics driver index" );
	}
	return BBString::null();
}

void b2dSetGfxDriver( int n ){
	const char *cfg=0;
	switch( n ){
	case 1:cfg="Native";break;
	case 2:cfg="OpenGL";break;
	case 3:cfg="DirectDraw";break;
	default:bbError( "Illegal graphics driver index" );
	}

	BBModule::setDriver( "Screen",cfg );
	BBModule::setDriver( "Canvas",cfg );
	BBModule::setDriver( "Graphics",cfg );
	BBModule::setDriver( "GrDriver2D",cfg );

	BBModule::stopDrivers();
	BBModule::startDrivers();
}

int b2dCountGfxModes(){
	return bbScreenModes();
}

int b2dGfxModeExists( int w,int h,int d,int fmt ){
	for( int k=1;k<=bbScreenModes();++k ){
		if( w && w!=b2dGfxModeWidth(k) ) continue;
		if( h && h!=b2dGfxModeHeight(k) ) continue;
		if( d && d!=b2dGfxModeDepth(k) ) continue;
		if( fmt && fmt!=b2dGfxModeFormat(k) ) continue;
		return 1;
	}
	return 0;
}

int b2dGfxModeWidth( int n ){
	if( n<1 || n>bbScreenModes() ) bbError( "Illegal graphics mode index" );
	int w,h,fmt;
	bbEnumScreenMode( n-1,&w,&h,&fmt );
	return w;
}

int b2dGfxModeHeight( int n ){
	if( n<1 || n>bbScreenModes() ) bbError( "Illegal graphics mode index" );
	int w,h,fmt;
	bbEnumScreenMode( n-1,&w,&h,&fmt );
	return h;
}

int b2dGfxModeFormat( int n ){
	if( n<1 || n>bbScreenModes() ) bbError( "Illegal graphics mode index" );
	int w,h,fmt;
	bbEnumScreenMode( n-1,&w,&h,&fmt );
	return fmt;
}

int b2dGfxModeDepth( int n ){
	if( n<1 || n>bbScreenModes() ) bbError( "Illegal graphics mode index" );
	int w,h,fmt;
	bbEnumScreenMode( n-1,&w,&h,&fmt );
	return bbBytesPerPixel( fmt )*8;
}

B2DBuffer *b2dBackBuffer(){
	return backBuffer;
}

B2DBuffer *b2dSetBuffer( B2DBuffer *buff ){

	buff->debug();

	B2DBuffer *t=b2dBuffer;
	b2dBuffer=buff;
	b2dGfx=b2dBuffer->graphics;
	b2dContext=b2dBuffer->context;

	bbGrDriver2D()->setContext( b2dContext );

	return t;
}

B2DBuffer *b2dGraphicsBuffer(){
	return b2dBuffer;
}

B2DBuffer *b2dDesktopBuffer(){
	if( b2dDesktop ) b2dDesktop->release();
	b2dDesktop=new B2DBuffer( bbDesktopScreen()->graphics() );
	return b2dDesktop;
}

int b2dLoadBuffer( B2DBuffer *buff,BBString *file ){

	if( !buff ) buff=b2dBuffer;
	buff->debug();

	BBGraphics *g=bbLoadGraphics( file );
	if( !g ) return 0;

	int w=buff->graphics->width();
	int h=buff->graphics->height();

	if( g->width()!=w || g->height()!=h ){
		float m[2][2];
		m[0][0]=(float)w/(float)g->width();
		m[1][1]=(float)h/(float)g->height();
		m[1][0]=m[0][1]=0;
		BBGraphics *t=bbTransformGraphics( g,m,true,0,0,0 );
		g->release();
		g=t;
	}

	buff->graphics->blit( 0,0,w,h,g,0,0,0 );

	g->release();

	return 1;
}

#pragma pack(push,1)

typedef struct tagBITMAPINFOHEADER{ // bmih 
	unsigned		biSize;
	unsigned		biWidth;
	unsigned		biHeight;
	unsigned short	biPlanes;
	unsigned short	biBitCount;
	unsigned		biCompression;
	unsigned		biSizeImage;
	unsigned		biXPelsPerMeter;
	unsigned		biYPelsPerMeter;
	unsigned		biClrUsed;
	unsigned		biClrImportant;
}BITMAPINFOHEADER;

typedef struct tagBITMAPFILEHEADER { // bmfh 
	unsigned short	bfType;
    unsigned		bfSize;
    unsigned short	bfReserved1;
	unsigned short	bfReserved2;
	unsigned		bfOffBits;
}BITMAPFILEHEADER;

#pragma pack(pop)

int b2dSaveBuffer( B2DBuffer *buff,BBString *file ){
	if( !buff ) buff=b2dBuffer;
	buff->debug();

	FILE *f=fopen( file->c_str(),"wb" );
	if( !f ) return 0;

	int w=buff->graphics->width();
	int h=buff->graphics->height();

	BBPixmap *tmp=new BBPixmap( w,h,BB_RGB888,4 );
	tmp->blit( 0,0,w,h,buff->graphics,0,0,0 );

	void *src;
	int src_pitch,src_fmt;
	tmp->lock( &src,&src_pitch,&src_fmt );

	BITMAPINFOHEADER bi={sizeof(bi),w,h,1,24};

	BITMAPFILEHEADER bf={'MB'};
	bf.bfSize=sizeof(bf)+sizeof(bi)+src_pitch*h;
	bf.bfOffBits=sizeof(bf)+sizeof(bi);

	fwrite( &bf,sizeof(bf),1,f );
	fwrite( &bi,sizeof(bi),1,f );

	src=(char*)src+src_pitch*h;
	for( int y=0;y<h;++y ){
		src=(char*)src-src_pitch;
		fwrite( src,src_pitch,1,f );
	}

	tmp->unlock();
	tmp->release();

	fclose(f);
	return 1;
}

int b2dGraphicsWidth(){
	b2dScreen->debug();
	return b2dScreen->graphics()->width();
}

int b2dGraphicsHeight(){
	b2dScreen->debug();
	return b2dScreen->graphics()->height();
}

int b2dGraphicsDepth(){
	b2dScreen->debug();
	return bbBytesPerPixel(b2dGraphicsFormat())*8;
}

int b2dGraphicsFormat(){
	b2dScreen->debug();
	void *p;
	int t,f=0;
	b2dScreen->graphics()->lock( &p,&t,&f );
	b2dScreen->graphics()->unlock();
	return f;
}

void b2dFlip( bool sync ){
	b2dScreen->debug();
	b2dScreen->flip( sync );
}

void b2dLockBuffer( B2DBuffer *buff ){
	if( !buff ) buff=b2dBuffer;
	buff->debug();
	if( !buff->locked++ ){
		void *data;
		buff->graphics->lock( &data,&buff->lockedpitch,&buff->lockedformat );
		buff->lockedpixels->setData(data);
	}
}

void b2dUnlockBuffer( B2DBuffer *buff ){
	if( !buff ) buff=b2dBuffer;
	buff->debug();
	if( !--buff->locked ){
		buff->graphics->unlock();
	}
}

int b2dLockedPitch( B2DBuffer *buff ){
	if( !buff ) buff=b2dBuffer;
	buff->debug();
	if( !buff->locked ) bbError( "Buffer is not locked" );
	return buff->lockedpitch;
}

int b2dLockedFormat( B2DBuffer *buff ){
	if( !buff ) buff=b2dBuffer;
	buff->debug();
	if( !buff->locked ) bbError( "Buffer is not locked" );
	return buff->lockedformat;
}

BBBank* b2dLockedPixels( B2DBuffer *buff ){
	if( !buff ) buff=b2dBuffer;
	buff->debug();
	if( !buff->locked ) bbError( "Buffer is not locked" );
	return buff->lockedpixels;
}

unsigned b2dReadPixel( int x,int y,B2DBuffer *buff ){
	if( !buff ) buff=b2dBuffer;
	buff->debug();

	x+=buff->ox;if( x<0 || x>=buff->graphics->width() ) return buff->clsColor;
	y+=buff->oy;if( y<0 || y>=buff->graphics->height() ) return buff->clsColor;

	b2dLockBuffer( buff );
	void *p=(char*)buff->lockedpixels->data()+y*buff->lockedpitch+x*bbBytesPerPixel(buff->lockedformat);
	unsigned t=BBColor( buff->lockedformat,p ).argb;
	b2dUnlockBuffer( buff );
	return t;
}

unsigned b2dReadPixelFast( int x,int y,B2DBuffer *buff ){
	if( !buff ) buff=b2dBuffer;
	void *p=(char*)buff->lockedpixels->data()+y*buff->lockedpitch+x*bbBytesPerPixel(buff->lockedformat);
	return BBColor( buff->lockedformat,p ).argb;
}

void b2dWritePixel( int x,int y,unsigned argb,B2DBuffer *buff ){
	if( !buff ) buff=b2dBuffer;
	buff->debug();

	x+=buff->ox;
	if( x<0 || x>=buff->graphics->width() ) return;
	y+=buff->oy;
	if( y<0 || y>=buff->graphics->height() ) return;

	b2dLockBuffer( buff );
	void *p=(char*)buff->lockedpixels->data()+y*buff->lockedpitch+x*bbBytesPerPixel(buff->lockedformat);
	BBColor( argb ).toPixel( buff->lockedformat,p );
	b2dUnlockBuffer( buff );
}

void b2dWritePixelFast( int x,int y,unsigned argb,B2DBuffer *buff ){
	if( !buff ) buff=b2dBuffer;
	void *p=(char*)buff->lockedpixels->data()+y*buff->lockedpitch+x*bbBytesPerPixel(buff->lockedformat);
	BBColor( argb ).toPixel( buff->lockedformat,p );
}

void b2dCopyPixel( int sx,int sy,B2DBuffer *sbuf,int dx,int dy,B2DBuffer *dbuf ){
	if( !sbuf ) sbuf=b2dBuffer;
	if( !dbuf ) dbuf=b2dBuffer;

	sbuf->debug();
	dbuf->debug();

	sx+=sbuf->ox;
	if( sx<0 || sx>=sbuf->graphics->width() ) return;
	sy+=sbuf->oy;
	if( sy<0 || sy>=sbuf->graphics->height() ) return;

	dx+=dbuf->ox;
	if( dx<0 || dx>=dbuf->graphics->width() ) return;
	dy+=dbuf->oy;
	if( dy<0 || dy>=dbuf->graphics->height() ) return;

	b2dLockBuffer( sbuf );
	b2dLockBuffer( dbuf );
	void *sp=(char*)sbuf->lockedpixels->data()+sy*sbuf->lockedpitch+sx*bbBytesPerPixel(sbuf->lockedformat);
	void *dp=(char*)dbuf->lockedpixels->data()+dy*dbuf->lockedpitch+dx*bbBytesPerPixel(dbuf->lockedformat);
	BBColor( sbuf->lockedformat,sp ).toPixel( dbuf->lockedformat,dp );
	b2dUnlockBuffer( dbuf );
	b2dUnlockBuffer( sbuf );
}

void b2dCopyPixelFast( int sx,int sy,B2DBuffer *sbuf,int dx,int dy,B2DBuffer *dbuf ){
	if( !sbuf ) sbuf=b2dBuffer;
	if( !dbuf ) dbuf=b2dBuffer;
	void *sp=(char*)sbuf->lockedpixels->data()+sy*sbuf->lockedpitch+sx*bbBytesPerPixel(sbuf->lockedformat);
	void *dp=(char*)dbuf->lockedpixels->data()+dy*dbuf->lockedpitch+dx*bbBytesPerPixel(dbuf->lockedformat);
	BBColor( sbuf->lockedformat,sp ).toPixel( dbuf->lockedformat,dp );
}

void b2dCopyRect( int src_x,int src_y,int w,int h,int x,int y,B2DBuffer *src,B2DBuffer *dst ){
	if( !src ) src=b2dBuffer;
	if( !dst ) dst=b2dBuffer;
	src->debug();
	dst->debug();

	BBRect dst_rect(x,y,x+w,y+h);
	BBRect src_rect(src_x,src_y,src_x+w,src_y+h);

	BBRect dst_img_rect(0,0,dst->graphics->width(),dst->graphics->height());
	if( !dst_img_rect.clip( &dst_rect,&src_rect ) ) return;

	BBRect src_img_rect(0,0,src->graphics->width(),src->graphics->height());
	if( !src_img_rect.clip( &src_rect,&dst_rect ) ) return;

	dst->graphics->blit( 
		dst_rect.left,dst_rect.top,
		dst_rect.width(),dst_rect.height(),
		src->graphics,src_rect.left,src_rect.top,0 );
}

void b2dColor( int r,int g,int b ){
	b2dBuffer->debug();
	bbGrDriver2D()->setColor( b2dBuffer->color=(0xff000000|(r<<16)|(g<<8)|b) );
}

void b2dClsColor( int r,int g,int b ){
	b2dBuffer->debug();
	b2dBuffer->clsColor=(0xff000000|(r<<16)|(g<<8)|b);
}

void b2dSetFont( BBFont *font ){
	font->debug();
	b2dBuffer->debug();
	bbGrDriver2D()->setFont( font );
}

void b2dOrigin( int x,int y ){
	b2dBuffer->debug();
	b2dBuffer->ox=x;
	b2dBuffer->oy=y;
}

void b2dViewport( int x,int y,int w,int h ){
	b2dBuffer->debug();

	int gw=b2dBuffer->graphics->width();
	int gh=b2dBuffer->graphics->height();

	if( x<0 ) x=0;
	else if( x>gw ) x=gw;
	if( w<0 ) w=0;
	else if( x+w>gw ) w=gw-x;

	if( y<0 ) y=0;
	else if( y>gh ) y=gh;
	if( h<0 ) h=0;
	else if( y+h>gh ) h=gh-y;

	bbGrDriver2D()->setClipRect( BBRect(x,y,x+w,y+h) );
}

void b2dCls(){
	b2dBuffer->debug();
	BBRect rect=bbGrDriver2D()->clipRect();
	bbGrDriver2D()->setColor( b2dBuffer->clsColor );
	bbGrDriver2D()->rect( rect.x(),rect.y(),rect.width(),rect.height(),true );
	bbGrDriver2D()->setColor( b2dBuffer->color );
}

void b2dPlot( int x,int y ){
	b2dBuffer->debug();
	bbGrDriver2D()->plot( x+b2dBuffer->ox,y+b2dBuffer->oy );
}

void b2dLine( int x,int y,int x2,int y2 ){
	b2dBuffer->debug();
	bbGrDriver2D()->line( x+b2dBuffer->ox,y+b2dBuffer->oy,x2+b2dBuffer->ox,y2+b2dBuffer->oy );
}

void b2dRect( int x,int y,int w,int h,bool solid ){
	b2dBuffer->debug();
	bbGrDriver2D()->rect( x+b2dBuffer->ox,y+b2dBuffer->oy,w,h,solid );
}

void b2dOval( int x,int y,int w,int h,bool solid ){
	b2dBuffer->debug();
	bbGrDriver2D()->oval( x+b2dBuffer->ox,y+b2dBuffer->oy,w,h,solid );
}

void b2dText( int x,int y,BBString *text,bool center_x,bool center_y ){
	b2dBuffer->debug();
	if( center_x || center_y ){
		int w,h;
		bbGrDriver2D()->font()->measure( text,&w,&h );
		if( center_x ) x-=w/2;
		if( center_y ) y-=h/2;
	}
	bbGrDriver2D()->text( x+b2dBuffer->ox,y+b2dBuffer->oy,text );
}

void b2dGetColor( int x,int y ){
	b2dBuffer->debug();
	x+=b2dBuffer->ox;y+=b2dBuffer->oy;
	const BBRect &r=bbGrDriver2D()->clipRect();
	if( x<r.left || x>=r.right || y<r.top || y>=r.bottom ){
		b2dBuffer->color=b2dBuffer->clsColor;
	}else{
		b2dGfx->read( x,y,1,1,(BBColor*)&b2dBuffer->color,0 );
	}
	bbGrDriver2D()->setColor( b2dBuffer->color );
}

int b2dColorRed(){
	b2dBuffer->debug();
	return (b2dBuffer->color>>16)&255;
}

int b2dColorGreen(){
	b2dBuffer->debug();
	return (b2dBuffer->color>>8)&255;
}

int b2dColorBlue(){
	b2dBuffer->debug();
	return (b2dBuffer->color)&255;
}

int b2dStringWidth( BBString *str ){
	b2dBuffer->debug();
	int w,h;
	bbGrDriver2D()->font()->measure( str,&w,&h );
	return w;
}

int b2dStringHeight( BBString *str ){
	b2dBuffer->debug();
	int w,h;
	bbGrDriver2D()->font()->measure( str,&w,&h );
	return h;
}

int b2dFontWidth(){
	b2dBuffer->debug();
	return bbGrDriver2D()->font()->width();
}

int b2dFontHeight(){
	b2dBuffer->debug();
	return bbGrDriver2D()->font()->height();
}

BBMovie* b2dOpenMovie( BBString *file ){
	return bbLoadMovie( file );
}

void b2dCloseMovie( BBMovie *movie ){
	if( !movie ) return;
	movie->debug();
	movie->release();
}

int b2dMovieWidth( BBMovie *movie ){
	movie->debug();
	return movie->width();
}

int b2dMovieHeight( BBMovie *movie ){
	movie->debug();
	return movie->height();
}

int b2dMoviePlaying( BBMovie *movie ){
	movie->debug();
	return movie->playing();
}

int b2dDrawMovie( BBMovie *movie,int x,int y,int w,int h ){
	movie->debug();
	b2dBuffer->debug();
	if( w<0 ) w=movie->width();
	if( h<0 ) h=movie->height();
	movie->render( b2dGfx,x,y,w,h );
	return movie->playing();
}

int	b2dMouseX( BBCanvas *canvas ){
	bbPollEvent();
	if( canvas ){
		canvas->debug();
		return canvas->mouseX();
	}else if( b2dScreen ){
		return b2dScreen->mouseX();
	}
	return 0;
}

int b2dMouseY( BBCanvas *canvas ){
	bbPollEvent();
	if( canvas ){
		canvas->debug();
		return canvas->mouseY();
	}else if( b2dScreen ){
		return b2dScreen->mouseY();
	}
	return 0;
}

int	b2dMouseXSpeed( BBCanvas *canvas ){
	bbPollEvent();
	if( canvas ){
		canvas->debug();
		return canvas->mouseXSpeed();
	}else if( b2dScreen ){
		return b2dScreen->mouseXSpeed();
	}
	return 0;
}

int b2dMouseYSpeed( BBCanvas *canvas ){
	bbPollEvent();
	if( canvas ){
		canvas->debug();
		return canvas->mouseYSpeed();
	}else if( b2dScreen ){
		return b2dScreen->mouseYSpeed();
	}
	return 0;
}

void b2dMoveMouse( int x,int y,BBCanvas *canvas ){
	if( canvas ){
		canvas->debug();
		canvas->moveMouse( x,y );
	}else if( b2dScreen ){
		b2dScreen->moveMouse( x,y );
	}
}

void b2dSetPointer( int id,BBCanvas *canvas ){
	if( canvas ){
		canvas->debug();
		canvas->setPointer(id);
	}else if( b2dScreen ){
		b2dScreen->setPointer(id);
	}
}

void b2dShowPointer( BBCanvas *canvas ){
	b2dSetPointer(1,canvas);
}

void b2dHidePointer( BBCanvas *canvas ){
	b2dSetPointer(0,canvas);
}

void b2dSetGamma( int r,int g,int b,int dr,int dg,int db ){
	b2dScreen->setGamma( r,g,b,dr,dg,db );
}

void b2dUpdateGamma( int calibrate ){
	b2dScreen->updateGamma( !!calibrate );
}

int			b2dGammaRed( int r ){
	int dr,dg,db;
	b2dScreen->getGamma( r,r,r,&dr,&dg,&db );
	return dr;
}

int			b2dGammaGreen( int g ){
	int dr,dg,db;
	b2dScreen->getGamma( g,g,g,&dr,&dg,&db );
	return dg;
}

int			b2dGammaBlue( int b ){
	int dr,dg,db;
	b2dScreen->getGamma( b,b,b,&dr,&dg,&db );
	return db;
}
