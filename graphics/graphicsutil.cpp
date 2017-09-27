
#include "graphics.h"

#include <math.h>

#include <string.h>

#include "../freeimage/freeimage.h"

#pragma warning(disable:4244)

static BBGraphics *src;
static BBColor src_key;
static void *src_pixels;
static int src_pitch,src_fmt,src_sz;

static BBColor readPixel( int x,int y ){
	if( x<0 || x>=src->width() || y<0 || y>=src->height() ) return src_key;
	return BBColor( src_fmt,(char*)src_pixels + y*src_pitch +  x*src_sz );
}

static BBGraphics *dst;
static void *dst_pixels;
static int dst_pitch,dst_fmt,dst_sz;

static void writePixel( int x,int y,BBColor color ){
	if( x<0 || x>=dst->width() || y<0 || y>=dst->height() ) return;
	color.toPixel( dst_fmt,(char*)dst_pixels + y*dst_pitch + x*dst_sz );
}

void bbFlipVertical( BBGraphics *g ){

	int w=g->width(),h=g->height();

	void *pixels;
	int pitch,format;
	g->lock( &pixels,&pitch,&format );
	int sz=w*bbBytesPerPixel(format);

	void *tbuf=new char[sz];

	char *pix1=(char*)pixels;
	char *pix2=(char*)pixels+h*pitch;

	for( int y=0;y<h/2;++y ){
		pix2-=pitch;
		memcpy( tbuf,pix1,sz );
		memcpy( pix1,pix2,sz );
		memcpy( pix2,tbuf,sz );
		pix1+=pitch;
	}

	delete[] tbuf;

	g->unlock();
}

BBGraphics *bbMipHalve( BBGraphics *g ){

	int w=g->width(),h=g->height();

	if( w==1 && h==1 ) return 0;

	src=g;

	dst=src->createCopy( w==1 ? 1 : w/2,h==1 ? 1 : h/2 );

	src->lock( &src_pixels,&src_pitch,&src_fmt );
	src_sz=bbBytesPerPixel(src_fmt);

	dst->lock( &dst_pixels,&dst_pitch,&dst_fmt );
	dst_sz=bbBytesPerPixel(dst_fmt);

	if( h==1 ){
		w/=2;
		for( int x=0;x<w;++x ){
			BBColor c1=readPixel(x*2,0);
			BBColor c2=readPixel(x*2+1,0);
			BBColor tc( (c1.r+c2.r)/2,(c1.g+c2.g)/2,(c1.b+c2.b)/2,(c1.a+c2.a)/2 );
			writePixel( x,0,tc );
		}
	}else if( w==1 ){
		h/=2;
		for( int y=0;y<h;++y ){
			BBColor c1=readPixel(0,y*2);
			BBColor c2=readPixel(0,y*2+1);
			BBColor tc( (c1.r+c2.r)/2,(c1.g+c2.g)/2,(c1.b+c2.b)/2,(c1.a+c2.a)/2 );
			writePixel( 0,y,tc );
		}
	}else{
		w/=2;
		h/=2;
		for( int y=0;y<h;++y ){
			for( int x=0;x<w;++x ){
				BBColor c1=readPixel(x*2,y*2);
				BBColor c2=readPixel(x*2+1,y*2);
				BBColor c3=readPixel(x*2+1,y*2+1);
				BBColor c4=readPixel(x*2,y*2+1);
				BBColor tc( (c1.r+c2.r+c3.r+c4.r)/4,(c1.g+c2.g+c3.g+c4.g)/4,(c1.b+c2.b+c3.b+c4.b)/4,(c1.a+c2.a+c3.a+c4.a)/4 );
				writePixel( x,y,tc );
			}
		}
	}

	dst->unlock();
	src->unlock();

	return dst;
}

struct vec2{ float x,y; };

static vec2 vrot( float m[2][2],const vec2 &v ){
	vec2 t;t.x=m[0][0]*v.x+m[0][1]*v.y;t.y=m[1][0]*v.x+m[1][1]*v.y;
	return t;
}

static float vmin( float a,float b,float c,float d ){
	float t=a;if( b<t ) t=b;if( c<t ) t=c;if( d<t ) t=d;return t;
}

static float vmax( float a,float b,float c,float d ){
	float t=a;if( b>t ) t=b;if( c>t ) t=c;if( d>t ) t=d;return t;
}

BBGraphics *bbTransformGraphics( BBGraphics *g,float m[2][2],bool filter,BBColor key,int *x_handle,int *y_handle ){

	int src_w=g->width();
	int src_h=g->height();

	vec2 v,v0,v1,v2,v3;

	float i[2][2];
	float dt=1.0f/(m[0][0]*m[1][1]-m[1][0]*m[0][1]);

	i[0][0]=dt*m[1][1];i[1][0]=-dt*m[1][0];
	i[0][1]=-dt*m[0][1];i[1][1]=dt*m[0][0];

	float ox=x_handle ? *x_handle : 0;
	float oy=y_handle ? *y_handle : 0;

	v0.x=-ox;v0.y=-oy;				//tl
	v1.x=src_w-ox;v1.y=-oy;			//tr
	v2.x=src_w-ox;v2.y=src_h-oy;	//br
	v3.x=-ox;v3.y=src_h-oy;			//bl

	v0=vrot(m,v0);v1=vrot(m,v1);v2=vrot(m,v2);v3=vrot(m,v3);

	int minx=(int)floor( vmin( v0.x,v1.x,v2.x,v3.x ) );
	int miny=(int)floor( vmin( v0.y,v1.y,v2.y,v3.y ) );
	int maxx=(int)ceil( vmax( v0.x,v1.x,v2.x,v3.x ) );
	int maxy=(int)ceil( vmax( v0.y,v1.y,v2.y,v3.y ) );

	int iw=maxx-minx,ih=maxy-miny;

	if( x_handle ) *x_handle=-minx;
	if( y_handle ) *y_handle=-miny;

	src=g;
	dst=g->createCopy(iw,ih);

	src->lock( &src_pixels,&src_pitch,&src_fmt );
	src_sz=bbBytesPerPixel(src_fmt);

	src_key=BBColor( key.toPixel( src_fmt ) );

	dst->lock( &dst_pixels,&dst_pitch,&dst_fmt );
	dst_sz=bbBytesPerPixel(dst_fmt);

	v.y=miny+.5f;
	for( int y=0;y<ih;++v.y,++y ){
		v.x=minx+.5f;
		for( int x=0;x<iw;++v.x,++x ){
			vec2 q=vrot(i,v);

			BBColor pixel;

			if( filter ){

				float tx=q.x+ox-.5f;
				float ty=q.y+oy-.5f;
				float fx=floor(tx),fy=floor(ty);
				int px=fx,py=fy;fx=tx-fx;fy=ty-fy;

				BBColor pixels[2][2];

				for( int v=0;v<2;++v ){
					for( int h=0;h<2;++h ){
						pixels[h][v]=readPixel( px+h,py+v );
					}
				}

				BBColor tl=pixels[0][0],tr=pixels[1][0];
				BBColor br=pixels[1][1],bl=pixels[0][1];

				float w1=(1-fx)*(1-fy),w2=fx*(1-fy),w3=(1-fx)*fy,w4=fx*fy;

				float a=tl.a*w1 + tr.a*w2 + bl.a*w3 + br.a*w4;
				float r=tl.r*w1 + tr.r*w2 + bl.r*w3 + br.r*w4;
				float g=tl.g*w1 + tr.g*w2 + bl.g*w3 + br.g*w4;
				float b=tl.b*w1 + tr.b*w2 + bl.b*w3 + br.b*w4;

				pixel=(int(a+.5f)<<24)|(int(r+.5f)<<16)|(int(g+.5f)<<8)|int(b+.5f);

			}else{

				int ix=(int)floor(q.x+ox),iy=(int)floor(q.y+oy );

				pixel=readPixel( ix,iy );
			}

			writePixel( x,y,pixel );
		}
	}

	dst->unlock();
	src->unlock();

	return dst;
}

BBGraphics *bbLoadGraphics( BBString *t ){

	const char *file=t->c_str();

	const char *ext=strrchr( file,'.' );
	if( !ext ) return 0;
	++ext;

	FreeImage_Initialise();

	FREE_IMAGE_FORMAT fmt=FIF_BMP;
	if( !stricmp( ext,"jpg" ) ) fmt=FIF_JPEG;
	else if( !stricmp( ext,"jpeg" ) ) fmt=FIF_JPEG;
	else if( !stricmp( ext,"png" ) ) fmt=FIF_PNG;
	FIBITMAP *t_dib=FreeImage_Load( fmt,file,0 );
	if( !t_dib ) return 0;

	FIBITMAP *dib=FreeImage_ConvertTo32Bits( t_dib );
	if( dib ) FreeImage_Unload( t_dib );
	else dib=t_dib;

	int width=FreeImage_GetWidth(dib);
	int height=FreeImage_GetHeight(dib);
	int pitch=FreeImage_GetPitch(dib);
	const void *pixs=FreeImage_GetBits(dib);

	BBGraphics *g=new BBPixmap( width,height );

	g->write( 0,0,width,height,(BBColor*)pixs,pitch );

	FreeImage_Unload( dib );

	bbFlipVertical( g );

	return g;
}

BBGraphics *bbCreateKeyAlpha( BBGraphics *src_g ){

	int w=src_g->width(),h=src_g->height();

	BBGraphics *dst_g=new BBPixmap( w,h,BB_ARGB8888 );

	void *src;
	int src_pitch,src_fmt;
	src_g->lock( &src,&src_pitch,&src_fmt );
	int src_sz=bbBytesPerPixel(src_fmt);
	int src_delta=src_pitch-w*src_sz;

	void *dst;
	int dst_pitch,dst_fmt;
	dst_g->lock( &dst,&dst_pitch,&dst_fmt );
	int dst_sz=bbBytesPerPixel(dst_fmt);
	int dst_delta=dst_pitch-w*dst_sz;

	//convert key to source format and back
	unsigned key_rgb=src_g->colorKey().clamp(src_fmt).argb & 0xffffff;

	for( int ty=0;ty<h;++ty ){
		for( int tx=0;tx<w;++tx ){
			BBColor color( src_fmt,src );
			color.a=(color.argb&0xffffff)==key_rgb ? 0 : 255;
			color.toPixel( dst_fmt,dst );
			src=(char*)src+src_sz;
			dst=(char*)dst+dst_sz;
		}
		src=(char*)src+src_delta;
		dst=(char*)dst+dst_delta;
	}

	dst_g->unlock();
	src_g->unlock();

	return dst_g;
}
