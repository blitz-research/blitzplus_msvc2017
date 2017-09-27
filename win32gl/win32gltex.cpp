
#include "win32gl.h"

static int roundpow2( int n ){
	int c=0,t=0;
	for( ;n;++c,n>>=1 ) t+=(n&1);
	return t>1 ? 1<<c : 1<<(c-1);
}

Win32GLTex::Win32GLTex( BBGraphics *g,int flags ):_flags(flags){

	int w=roundpow2( g->width() );
	int h=roundpow2( g->height() );

	_texrect[0]=0;
	_texrect[1]=0;
	_texrect[2]=(float)g->width()/(float)w;
	_texrect[3]=(float)g->height()/(float)h;

	BBGraphics *p=bbCreateKeyAlpha( g );
	BBPixmap *t=new BBPixmap( w,h,BB_ARGB8888,4 );
	t->clear( 0,0,w,h,g->colorKey() );
	t->blit( 0,0,g->width(),g->height(),p,0,0,0 );
	p->release();

	glGenTextures( 1,&_id );
	glBindTexture( GL_TEXTURE_2D,_id );
	glTexImage2D( GL_TEXTURE_2D,0,4,w,h,0,GL_BGRA_EXT,GL_UNSIGNED_BYTE,t->pixels() );
	glTexParameteri( GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_NEAREST );
	glTexParameteri( GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_NEAREST );

	t->release();
}

Win32GLTex::~Win32GLTex(){
	glDeleteTextures( 1,&_id );
}
