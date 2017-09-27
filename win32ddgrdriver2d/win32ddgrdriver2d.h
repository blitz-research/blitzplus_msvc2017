
#ifndef WIN32DDGRDRIVER2D_H
#define WIN32DDGRDRIVER2D_H

#include "../win32dd/win32dd.h"

#include "../grdriver2d/grdriver2d.h"

class Win32Font;
class Win32DDGraphics;

class Win32DDGrDriver2D : public BBGrDriver2D{
	struct Context;
	Context *_context;
	Win32DDGraphics *_graphics;

public:
	Win32DDGrDriver2D();

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

	void			text( int x,int y,BBString *text );

	BBGraphics*		createGraphics( int w,int h,int fmt,int flags );
};

extern Win32DDGrDriver2D win32DDGrDriver2D;
static Win32DDGrDriver2D *_win32DDGrDriver2D=&win32DDGrDriver2D;

#endif