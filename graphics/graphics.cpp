
#include "graphics.h"

#include <stdio.h>

#include <string.h>

BBGraphics::BBGraphics( int w,int h ):_w(w),_h(h),_key(0){
}

BBGraphics *BBGraphics::createCopy( int w,int h ){
	return new BBPixmap( w,h );
}

void BBGraphics::setColorKey( BBColor key ){
	_key=key;
}

void BBGraphics::clear( int x,int y,int w,int h,BBColor color ){

	void *dst;
	int dst_pitch,dst_fmt;
	lock( &dst,&dst_pitch,&dst_fmt );

	int sz=bbBytesPerPixel(dst_fmt);
	unsigned t=color.toPixel(dst_fmt);

	int dst_delta=dst_pitch-w*sz;
	dst=(char*)dst+y*dst_pitch+x*sz;

	while( (h--)>0 ){
		for( int x=0;x<w;++x ){
			memcpy( dst,&t,sz );
			dst=(char*)dst+sz;
		}
		dst=(char*)dst+dst_delta;
	}

	unlock();
}

void BBGraphics::read( int x,int y,int w,int h,BBColor *dst,int dst_pitch ){

	void *src;
	int src_pitch,src_fmt;
	lock( &src,&src_pitch,&src_fmt );
	int sz=bbBytesPerPixel(src_fmt);

	int src_delta=src_pitch-w*sz;
	src=(char*)src+y*src_pitch+x*sz;

	int dst_delta=dst_pitch-w*sizeof(BBColor);

	while( (h--)>0 ){
		for( int x=0;x<w;++x ){
			(dst++)->fromPixel( src_fmt,src );
			src=(char*)src+sz;
		}
		src=(char*)src+src_delta;
		dst=(BBColor*)( (char*)dst+dst_delta );
	}

	unlock();
}

void BBGraphics::write( int x,int y,int w,int h,const BBColor *src,int src_pitch ){

	void *dst;
	int dst_pitch,dst_fmt;
	lock( &dst,&dst_pitch,&dst_fmt );
	int sz=bbBytesPerPixel(dst_fmt);

	int dst_delta=dst_pitch-w*sz;
	dst=(char*)dst+y*dst_pitch+x*sz;

	int src_delta=src_pitch-w*sizeof(BBColor);

	while( (h--)>0 ){
		for( int x=0;x<w;++x ){
			(src++)->toPixel( dst_fmt,dst );
			dst=(char*)dst+sz;
		}
		dst=(char*)dst+dst_delta;
		src=(BBColor*)( (char*)src+src_delta );
	}

	unlock();
}

void BBGraphics::blit( int x,int y,int w,int h,BBGraphics *src_g,int sx,int sy,int flags ){

	BBGraphics *dst_g=this;

	void *src;
	int src_pitch,src_fmt;
	src_g->lock( &src,&src_pitch,&src_fmt );
	int src_sz=bbBytesPerPixel(src_fmt);
	int src_delta=src_pitch-w*src_sz;
	src=(char*)src+sy*src_pitch+sx*src_sz;

	void *dst;
	int dst_pitch,dst_fmt;
	dst_g->lock( &dst,&dst_pitch,&dst_fmt );
	int dst_sz=bbBytesPerPixel(dst_fmt);
	int dst_delta=dst_pitch-w*dst_sz;
	dst=(char*)dst+y*dst_pitch+x*dst_sz;

	if( flags & BLIT_KEYSOURCE ){

		unsigned key_rgb=src_g->colorKey().clamp(src_fmt).argb & 0xffffff;

		for( int ty=0;ty<h;++ty ){
			for( int tx=0;tx<w;++tx ){
				BBColor color( src_fmt,src );
				if( (color.argb & 0xffffff)!=key_rgb ) color.toPixel( dst_fmt,dst );
				src=(char*)src+src_sz;
				dst=(char*)dst+dst_sz;
			}
			src=(char*)src+src_delta;
			dst=(char*)dst+dst_delta;
		}
	}else{

		for( int ty=0;ty<h;++ty ){
			for( int tx=0;tx<w;++tx ){
				BBColor( src_fmt,src ).toPixel( dst_fmt,dst );
				src=(char*)src+src_sz;
				dst=(char*)dst+dst_sz;
			}
			src=(char*)src+src_delta;
			dst=(char*)dst+dst_delta;
		}
	}

	dst_g->unlock();
	src_g->unlock();
}
