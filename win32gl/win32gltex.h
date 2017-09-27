
#ifndef WIN32GLTEX_H
#define WIN32GLTEX_H

#include "win32glgraphics.h"

class Win32GLTex : public BBObject{
	GLuint _id;
	int _flags;
	float _texrect[4];
protected:
	~Win32GLTex();
public:
	enum{
		TEX_RGB=1,
		TEX_ALPHA=2,
		TEX_COLORKEY=4,
		TEX_MIPMAP=8
	};

	Win32GLTex( BBGraphics *g,int flags );

	int		id()const{ return _id; }
	void	bind()const{ glBindTexture( GL_TEXTURE_2D,_id ); }

	const float *texRect()const{ return _texrect; }
};

#endif
