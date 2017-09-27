
#ifndef WIN32GDIGRDRIVER2D_H
#define Win32GDIGRDRIVER2D_H

#include "../win32gdi/win32gdi.h"

#include "../grdriver2d/grdriver2d.h"

class Win32GDIGrDriver2D : public BBGrDriver2D{
	struct Context;
	Context *_context;
	Win32GDIGraphics *_graphics;
	HDC _hdc;

public:
	Win32GDIGrDriver2D();

	bool			startup();
	void			shutdown();

	void			setContext( BBGrContext *t );
	BBGrContext*	createContext( BBGraphics *g );

	void			setFont( BBFont *font );
	void			setColor( BBColor color );
	void			setClipRect( const BBRect& clip );

	BBFont*			font();
	BBColor			color();
	const BBRect&	clipRect();

	void			plot( int x,int y );
	void			line( int x1,int y1,int x2,int y2 );
	void			rect( int x,int y,int w,int h,bool solid );
	void			oval( int x,int y,int w,int h,bool solid );
	void			blit( int x,int y,int w,int h,BBGraphics *src,int sx,int sy,int flags );
	void			text( int x,int y,BBString *text );

	BBGraphics*		createGraphics( int w,int h,int fmt,int flags );
};

extern Win32GDIGrDriver2D win32GDIGrDriver2D;
static Win32GDIGrDriver2D *_win32GDIGrDriver2D=&win32GDIGrDriver2D;

#endif
