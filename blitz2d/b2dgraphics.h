
#ifndef B2DGRAPHICS_H
#define B2DGRAPHICS_H

#include "../screen/screen.h"
#include "../movie/movie.h"

#include "b2dimage.h"
#include "b2dbuffer.h"
#include "b2dcanvas.h"

extern		BBScreen *b2dScreen;		//current screen
extern		BBGraphics *b2dGfx;			//current graphics
extern		BBGrContext *b2dContext;	//current context
extern		B2DBuffer *b2dBuffer;		//current buffer

int			b2dCountGfxDrivers();
BBString*	b2dGfxDriverName( int n );
void		b2dSetGfxDriver( int n );
int			b2dCountGfxModes();
int			b2dGfxModeWidth( int n );
int			b2dGfxModeHeight( int n );
int			b2dGfxModeFormat( int n );
int			b2dGfxModeDepth( int n );
int			b2dGfxModeExists( int w,int h,int d,int fmt );

void		b2dGraphics( int w,int h,int d,int mode );
void		b2dEndGraphics();
B2DBuffer*	b2dBackBuffer();
B2DBuffer*	b2dSetBuffer( B2DBuffer *buff );
B2DBuffer*	b2dGraphicsBuffer();
B2DBuffer*	b2dDesktopBuffer();
int			b2dLoadBuffer( B2DBuffer *buff,BBString *file );
int			b2dSaveBuffer( B2DBuffer *buff,BBString *file );
int			b2dGraphicsWidth();
int			b2dGraphicsHeight();
int			b2dGraphicsDepth();
int			b2dGraphicsFormat();
void		b2dFlip( bool sync );

void		b2dLockBuffer( B2DBuffer *buff );
void		b2dUnlockBuffer( B2DBuffer *buff );
int			b2dLockedPitch( B2DBuffer *buff );
int			b2dLockedFormat( B2DBuffer *buff );
BBBank*		b2dLockedPixels( B2DBuffer *buff );

unsigned	b2dReadPixel( int x,int y,B2DBuffer *buff );
unsigned	b2dReadPixelFast( int x,int y,B2DBuffer *buff );
void		b2dWritePixel( int x,int y,unsigned argb,B2DBuffer *buff );
void		b2dWritePixelFast( int x,int y,unsigned argb,B2DBuffer *buff );
void		b2dCopyPixel( int sx,int sy,B2DBuffer *sbuf,int dx,int dy,B2DBuffer *dbuf );
void		b2dCopyPixelFast( int sx,int sy,B2DBuffer *sbuf,int dx,int dy,B2DBuffer *dbuf );
void		b2dCopyRect( int sx,int sy,int sw,int sh,int dx,int dh,B2DBuffer *src,B2DBuffer *dst );

void		b2dColor( int r,int g,int b );
void		b2dClsColor( int r,int g,int b );
void		b2dSetFont( BBFont *font );
void		b2dOrigin( int x,int y );
void		b2dViewport( int x,int y,int w,int h );

void		b2dCls();
void		b2dPlot( int x,int y );
void		b2dLine( int x,int y,int x2,int y2 );
void		b2dRect( int x,int y,int w,int h,bool solid );
void		b2dOval( int x,int y,int w,int h,bool solid );
void		b2dText( int x,int y,BBString *text,bool center_x,bool center_y );

void		b2dGetColor( int x,int y );
int			b2dColorRed();
int			b2dColorGreen();
int			b2dColorBlue();

int			b2dFontWidth();
int			b2dFontHeight();
int			b2dStringWidth( BBString *str );
int			b2dStringHeight( BBString *str );

BBMovie*	b2dOpenMovie( BBString *file );
void		b2dCloseMovie( BBMovie *movie );
int			b2dMovieWidth( BBMovie *movie );
int			b2dMovieHeight( BBMovie *movie );
int			b2dMoviePlaying( BBMovie *movie );
int			b2dDrawMovie( BBMovie *movie,int x,int y,int w,int h );

int			b2dMouseX( BBCanvas *canvas );
int			b2dMouseY( BBCanvas *canvas );
int			b2dMouseXSpeed( BBCanvas *canvas );
int			b2dMouseYSpeed( BBCanvas *canvas );
void		b2dMoveMouse( int x,int y,BBCanvas *canvas );
void		b2dShowPointer( BBCanvas *canvas );
void		b2dHidePointer( BBCanvas *canvas );
void		b2dSetPointer( int id,BBCanvas *canvas );

void		b2dSetGamma( int r,int g,int b,int dr,int dg,int db );
void		b2dUpdateGamma( int calibrate );
int			b2dGammaRed( int r );
int			b2dGammaGreen( int g );
int			b2dGammaBlue( int b );

class B2DGraphicsDriver : public BBModule{
public:
	B2DGraphicsDriver();

	bool		startup();
	void		shutdown();

	B2DImage*	createImage( int w,int h,int frames,int flags );
};

extern B2DGraphicsDriver b2dGraphicsDriver;
static B2DGraphicsDriver *_b2dGraphicsDriver=&b2dGraphicsDriver;

#endif
