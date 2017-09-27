
#ifndef WIN32DDGRAPHICS_H
#define WIN32DDGRAPHICS_H
#define WIN32_LEAN_AND_MEAN

#include <ddraw.h>

#include "../graphics/graphics.h"

class Win32DDGraphics : public BBGraphics{
	int _type,_format;
	IDirectDrawSurface *_surf,*_prim;

	bool _dirty;
	Win32DDGraphics *_managed;

	int _locked,_lockedPitch,_lockedFormat;
	void *_lockedPixels;
	BBPixmap *_dummy;

	HDC _hdc;

	void	releaseSurface();

public:
	enum{
		TYPE_SYSMEM,			//sysmem surface
		TYPE_VIDMEM,			//vidmem surface
		TYPE_MANAGED,			//managed surface (auto restored)
		TYPE_PRIMARY,			//primary surface
		TYPE_PRIMARY_DB,		//double buffered primary surface
		TYPE_PRIMARY_COPYDB		//double buffered primary surface with copy semantics
	};

	Win32DDGraphics( int w,int h,int type );
	~Win32DDGraphics();

	void*	query( int qid );

	BBGraphics *createCopy(int w,int h);

	void	clear( int x,int y,int w,int h,BBColor color );
	void	write( int x,int y,int w,int h,const BBColor *colors,int pitch );
	void	blit( int x,int y,int w,int h,BBGraphics *src,int sx,int sy,int flags );
	void	lock( void **pixels,int *pitch,int *format );
	void	unlock();

	bool	valid();
	bool	restore();

	HDC		lockHdc();
	void	unlockHdc();

	IDirectDrawSurface *primarySurface();
	IDirectDrawSurface *graphicsSurface();
};

#endif