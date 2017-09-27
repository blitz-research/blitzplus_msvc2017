
#ifndef WIN32DDGRAPHICS_H
#define WIN32DDGRAPHICS_H
#define WIN32_LEAN_AND_MEAN

#include <ddraw.h>

#include "../graphics/graphics.h"

class Win32DDGraphics : public BBGraphics{
	int _type;
	IDirectDrawSurface *_surf,*_prim;
	int _format,_locked,_lockedPitch;
	void *_lockedPixels;
	Win32DDGraphics *_managed;
	BBPixmap *_dummy;
	bool _dirty;

	void	releaseSurface();

public:
	enum{
		TYPE_SYSMEM,			//sysmem surface
		TYPE_VIDMEM,			//vidmem surface
		TYPE_DYNAMIC,			//dynamic surface (auto restored)
		TYPE_MANAGED,			//managed surface (auto restored)
		TYPE_PRIMARY,			//standard primary surface
		TYPE_PRIMARY_DB,		//double buffered primary surface
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

	bool	createSurface();
	bool	restoreSurface();

	//Use these to write to surfaces instead
	HRESULT GetDC( HDC *hdc );
	HRESULT ReleaseDC( HDC hdc );
	HRESULT Blt( RECT *dst,Win32DDGraphics *src,RECT *src_rect,DWORD flags,DDBLTFX *fx );

	int		format()const{ return _format; }

	IDirectDrawSurface *primarySurface()const{ return _prim; }
	IDirectDrawSurface *graphicsSurface()const{ return _surf; }

	static void restoreAll();
};

#endif