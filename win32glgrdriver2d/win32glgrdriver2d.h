
#ifndef WIN32GLGRDRIVER2D_H
#define WIN32GLGRDRIVER2D_H

#include "../win32gl/win32gl.h"

#include "../grdriver2d/grdriver2d.h"

class Win32GLGrDriver2D : public BBGrDriver2D{
	struct Context;
	Context *_context;
	Win32GLGraphics *_graphics;
	Win32GLContext *_glContext;

public:
	Win32GLGrDriver2D();

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
	void			blit( int x,int y,int w,int h,BBGraphics *src,int sx,int sy,int flags );
	void			text( int x,int y,BBString *text );

	BBGraphics*		createGraphics( int w,int h,int fmt,int flags );
};

extern Win32GLGrDriver2D win32GLGrDriver2D;
static Win32GLGrDriver2D *_win32GLGrDriver2D=&win32GLGrDriver2D;

#endif