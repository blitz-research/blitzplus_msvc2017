
#include "b2dgraphics.h"

#include <math.h>

static const float dtor=0.0174532925199432957692369076848861f;

static bool filter=true;
static bool autoMid=false;

static unsigned FWMS[]={
	0xffffffff,0x7fffffff,0x3fffffff,0x1fffffff,
	0x0fffffff,0x07ffffff,0x03ffffff,0x01ffffff,
	0x00ffffff,0x007fffff,0x003fffff,0x001fffff,
	0x000fffff,0x0007ffff,0x0003ffff,0x0001ffff,
	0x0000ffff,0x00007fff,0x00003fff,0x00001fff,
	0x00000fff,0x000007ff,0x000003ff,0x000001ff,
	0x000000ff,0x0000007f,0x0000003f,0x0000001f,
	0x0000000f,0x00000007,0x00000003,0x00000001};

static unsigned LWMS[]={
	0x80000000,0xc0000000,0xe0000000,0xf0000000,
	0xf8000000,0xfc000000,0xfe000000,0xff000000,
	0xff800000,0xffc00000,0xffe00000,0xfff00000,
	0xfff80000,0xfffc0000,0xfffe0000,0xffff0000,
	0xffff8000,0xffffc000,0xffffe000,0xfffff000,
	0xfffff800,0xfffffc00,0xfffffe00,0xffffff00,
	0xffffff80,0xffffffc0,0xffffffe0,0xfffffff0,
	0xfffffff8,0xfffffffc,0xfffffffe,0xffffffff};

static void deleteBitMask( B2DImage *img,int frame ){
	delete[] img->bitmasks[frame];
	img->bitmasks[frame]=0;
}

static void deleteBitMasks( B2DImage *img ){
	for( int k=0;k<img->frames;++k ){
		delete[] img->bitmasks[k];
		img->bitmasks[k]=0;
	}
}

static unsigned *bitMask( B2DImage *img,int frame ){

	if( img->bitmasks[frame] ) return img->bitmasks[frame];

	//alloc and clear bitmask
	int maskPitch=(img->width+31)/32+1;
	unsigned *mask=img->bitmasks[frame]=new unsigned[maskPitch*img->height];
	memset( mask,0,maskPitch*img->height*sizeof(unsigned) );

	//OK, detect pixel format of source
	void *t_pixels;
	int t_pitch,t_fmt;
	img->buffers[frame]->graphics->lock( &t_pixels,&t_pitch,&t_fmt );
	img->buffers[frame]->graphics->unlock();

	//convert image mask color down then back up
	unsigned t_mask=img->mask.clamp(t_fmt).argb & 0xffffff;

	//create tmp fixed format version
	BBPixmap *tmp=new BBPixmap( img->width,img->height,BB_XRGB8888 );
	tmp->blit( 0,0,img->width,img->height,img->buffers[frame]->graphics,0,0,0 );

	//lock tmp version
	unsigned *src;
	int src_pitch,src_fmt;
	tmp->lock( (void**)&src,&src_pitch,&src_fmt );

	//build bitmask
	for( int y=0;y<img->height;++y ){
		for( int x=0;x<img->width;++x ){
			if( (src[x] & 0xffffff)!=t_mask ){
				mask[x/32]|=(unsigned)0x80000000>>(x&31);
			}
		}
		mask+=maskPitch;
		src=(unsigned*)((char*)src+src_pitch);
	}

	//discard tmp
	tmp->unlock();
	tmp->release();

	return img->bitmasks[frame];
}

B2DImage::B2DImage( int w,int h,int frms,int flgs ):
width(w),height(h),hx(0),hy(0),mask(0),frames(frms),flags(flgs){
	buffers=new B2DBuffer*[frames];
	bitmasks=new unsigned*[frames];
	for( int k=0;k<frames;++k ){
		bitmasks[k]=0;
		BBGraphics *g=bbGrDriver2D()->createGraphics( width,height,0,flags );
		g->clear( 0,0,width,height,0xff000000 );
		buffers[k]=new B2DBuffer(g);
		g->release();
	}
	if( autoMid ){
		hx=width/2;
		hy=height/2; 
	}
}

B2DImage::~B2DImage(){
	deleteBitMasks(this);
	for( int k=0;k<frames;++k ) buffers[k]->release();
	delete[] bitmasks;
	delete[] buffers;
}

void B2DImage::transform( float a,float b,float c,float d ){
	float m[2][2];
	m[0][0]=a;m[1][0]=b;m[0][1]=c;m[1][1]=d;

	BBGraphics *g=bbTransformGraphics( buffers[0]->graphics,m,filter,mask,&hx,&hy );
	buffers[0]->release();
	buffers[0]=new B2DBuffer(g);
	g->setColorKey( mask );
	g->release();

	width=buffers[0]->graphics->width();
	height=buffers[0]->graphics->height();

	for( int k=1;k<frames;++k ){
		BBGraphics *g=bbTransformGraphics( buffers[k]->graphics,m,filter,mask,0,0 );
		buffers[k]->release();
		buffers[k]=new B2DBuffer(g);
		g->setColorKey( mask );
		g->release();
	}

	deleteBitMasks(this);
}

B2DImage *B2DGraphicsDriver::createImage( int w,int h,int frames,int flags ){
	if( w<=0 || h<=0 || frames<=0 ){
		bbError( "Illegal Parameter for CreateImage" );
	}
	B2DImage *img=new B2DImage( w,h,frames,flags );
	autoRelease(img);
	return img;
}

B2DImage *b2dLoadImage( BBString *file,int flags ){
	BBGraphics *g=bbLoadGraphics( file );
	if( !g ) return 0;
	B2DImage *img=b2dGraphicsDriver.createImage( g->width(),g->height(),1,flags );
	img->buffers[0]->graphics->blit( 0,0,g->width(),g->height(),g,0,0,0 );
	g->release();
	return img;
}

B2DImage* b2dLoadAnimImage( BBString *file,int w,int h,int first,int cnt,int flags ){
	BBGraphics *g=bbLoadGraphics( file );
	if( !g ) return 0;

	B2DImage *img=b2dGraphicsDriver.createImage( w,h,cnt,flags );

	//frames per row, per picture
	int fpr=g->width()/w;
	int fpp=g->height()/h*fpr;
	if( first+cnt>fpp ){ g->release();return 0; }
	int src_x=first%fpr*w,src_y=first/fpr*h;

	for( int k=0;k<cnt;++k ){
		img->buffers[k]->graphics->blit( 0,0,w,h,g,src_x,src_y,0 );
		src_x+=w;
		if( src_x+w>g->width() ){
			src_x=0;
			src_y+=h;
		}
	}
	g->release();
	return img;
}

B2DImage *b2dCreateImage( int w,int h,int frames,int flags ){
	return b2dGraphicsDriver.createImage( w,h,frames,flags );
}

B2DImage *b2dCopyImage( B2DImage *img ){
	img->debug();
	B2DImage *i=b2dGraphicsDriver.createImage( img->width,img->height,img->frames,img->flags );
	i->hx=img->hx;
	i->hy=img->hy;
	i->mask=img->mask;
	for( int k=0;k<i->frames;++k ){
		i->buffers[k]->graphics->blit( 0,0,i->width,i->height,img->buffers[k]->graphics,0,0,0 );
		i->buffers[k]->graphics->setColorKey( i->mask );
	}
	return i;
}

void b2dFreeImage( B2DImage *img ){
	if( !img ) return;
	img->debug();
	img->release();
}

void b2dMaskImage( B2DImage *img,int r,int g,int b ){
	img->debug();
	img->mask=(r<<16)|(g<<8)|b;
	for( int k=0;k<img->frames;++k ){
		img->buffers[k]->graphics->setColorKey( img->mask );
	}
}

void b2dHandleImage( B2DImage *img,int x,int y ){
	img->debug();
	img->hx=x;img->hy=y;
}

void b2dMidHandle( B2DImage *img ){
	img->debug();
	img->hx=img->width/2;
	img->hy=img->height/2;
}

void b2dAutoMidHandle( bool enable ){
	autoMid=enable;
}

int b2dImageWidth( B2DImage *img ){
	img->debug();
	return img->width;
}

int b2dImageHeight( B2DImage *img ){
	img->debug();
	return img->height;
}

int b2dImageXHandle( B2DImage *img ){
	img->debug();
	return img->hx;
}

int b2dImageYHandle( B2DImage *img ){
	img->debug();
	return img->hy;
}

B2DBuffer *b2dImageBuffer( B2DImage *img,int frame ){
	img->debug(frame);
	deleteBitMask(img,frame);
	return img->buffers[frame];
}

int b2dSaveImage( B2DImage *img,BBString *file,int frame ){
	img->debug(frame);
	return b2dSaveBuffer( img->buffers[frame],file );
}

void b2dTFormFilter( bool enable ){
	filter=enable;
}

void b2dTFormImage( B2DImage *img,float a,float b,float c,float d ){
	img->debug();
	img->transform(a,b,c,d);
}

void b2dScaleImage( B2DImage *img,float w,float h ){
	img->debug();
	img->transform(w,0,0,h);
}

void b2dResizeImage( B2DImage *img,float w,float h ){
	img->debug();
	img->transform(w/(float)img->width,0,0,h/(float)img->height);
}

void b2dRotateImage( B2DImage *img,float d ){
	d*=-dtor;
	img->debug();
	img->transform((float)cos(d),(float)-sin(d),(float)sin(d),(float)cos(d));
}

void b2dDrawImage( B2DImage *img,int x,int y,int frame ){
	b2dBuffer->debug();
	img->debug( frame );
	bbGrDriver2D()->blit( 
		x+b2dBuffer->ox-img->hx,
		y+b2dBuffer->oy-img->hy,
		img->width,img->height,
		img->buffers[frame]->graphics,
		0,0,BBGraphics::BLIT_KEYSOURCE );
}

void b2dDrawBlock( B2DImage *img,int x,int y,int frame ){
	b2dBuffer->debug();
	img->debug( frame );
	bbGrDriver2D()->blit( 
		x+b2dBuffer->ox-img->hx,
		y+b2dBuffer->oy-img->hy,
		img->width,img->height,
		img->buffers[frame]->graphics,
		0,0,0 );
}

void b2dTileImage( B2DImage *img,int x,int y,int frame ){
	b2dBuffer->debug();
	img->debug(frame);
	x+=b2dBuffer->ox-img->hx;
	y+=b2dBuffer->oy-img->hy;

	BBRect rect=bbGrDriver2D()->clipRect();

	int w=img->width,h=img->height;
	int ox=rect.left-w+1;
	int oy=rect.top-h+1;

	x=(x-=ox)>=0 ? (x%w)+ox : w-(-x%w)+ox;
	y=(y-=oy)>=0 ? (y%h)+oy : h-(-y%h)+oy;

	BBGraphics *src=img->buffers[frame]->graphics;

	for( int yy=y;yy<rect.bottom;yy+=h ){
		for( int xx=x;xx<rect.right;xx+=w ){
			BBRect src_rect(0,0,w,h);
			BBRect dst_rect(xx,yy,xx+w,yy+h);
			if( !rect.clip(&dst_rect,&src_rect) ){
				bbError( "TileImage clip error" );
			}
			bbGrDriver2D()->blit(
				dst_rect.x(),dst_rect.y(),
				dst_rect.width(),dst_rect.height(),
				src,src_rect.x(),src_rect.y(),BBGraphics::BLIT_KEYSOURCE );
		}
	}
}

void b2dTileBlock( B2DImage *img,int x,int y,int frame ){
	b2dBuffer->debug();
	img->debug(frame);
	x+=b2dBuffer->ox-img->hx;
	y+=b2dBuffer->oy-img->hy;

	BBRect rect=bbGrDriver2D()->clipRect();

	int w=img->width,h=img->height;
	int ox=rect.left-w+1;
	int oy=rect.top-h+1;

	x=(x-=ox)>=0 ? (x%w)+ox : w-(-x%w)+ox;
	y=(y-=oy)>=0 ? (y%h)+oy : h-(-y%h)+oy;

	BBGraphics *src=img->buffers[frame]->graphics;

	for( int yy=y;yy<rect.bottom;yy+=h ){
		for( int xx=x;xx<rect.right;xx+=w ){
			BBRect src_rect(0,0,w,h);
			BBRect dst_rect(xx,yy,xx+w,yy+h);
			if( !rect.clip(&dst_rect,&src_rect) ){
				bbError( "TileBlock clip error" );
			}
			bbGrDriver2D()->blit(
				dst_rect.x(),dst_rect.y(),
				dst_rect.width(),dst_rect.height(),
				src,src_rect.x(),src_rect.y(),0 );
		}
	}
}

void b2dDrawImageRect( B2DImage *img,int x,int y,int sx,int sy,int w,int h,int frame ){
	img->debug(frame);
	b2dBuffer->debug();

	x+=b2dBuffer->ox-img->hx;
	y+=b2dBuffer->oy-img->hy;

	BBRect dst_rect(x,y,x+w,y+h);
	BBRect src_rect(sx,sy,sx+w,sy+h);
	BBRect img_rect(0,0,img->width,img->height);

	if( !img_rect.clip(&src_rect,&dst_rect) ) return;

	bbGrDriver2D()->blit( 
		x,y,
		dst_rect.width(),dst_rect.height(),
		img->buffers[frame]->graphics,
		src_rect.x(),src_rect.y(),BBGraphics::BLIT_KEYSOURCE );
}

void b2dDrawBlockRect( B2DImage *img,int x,int y,int sx,int sy,int w,int h,int frame ){
	img->debug(frame);
	b2dBuffer->debug();

	x+=b2dBuffer->ox-img->hx;
	y+=b2dBuffer->oy-img->hy;

	BBRect dst_rect(x,y,x+w,y+h);
	BBRect src_rect(sx,sy,sx+w,sy+h);
	BBRect img_rect(0,0,img->width,img->height);

	if( !img_rect.clip(&src_rect,&dst_rect) ) return;

	bbGrDriver2D()->blit( 
		x,y,
		dst_rect.width(),dst_rect.height(),
		img->buffers[frame]->graphics,
		src_rect.x(),src_rect.y(),0 );
}

void b2dGrabImage( B2DImage *img,int x,int y,int frame ){
	img->debug(frame);
	b2dBuffer->debug();

	x+=b2dBuffer->ox-img->hx;
	y+=b2dBuffer->oy-img->hy;

	BBRect src_rect( 0,0,img->width,img->height );
	BBRect dst_rect( x,y,x+img->width,y+img->height );

	if( !bbGrDriver2D()->clipRect().clip( &dst_rect,&src_rect ) ) return;

	img->buffers[frame]->graphics->blit( 
		src_rect.left,src_rect.top,src_rect.width(),src_rect.height(),
		b2dGfx,dst_rect.left,dst_rect.top,0 );
}

int b2dRectsOverlap( int x1,int y1,int w1,int h1,int x2,int y2,int w2,int h2 ){
	if( x1+w1<=x2 || x1>=x2+w2 ) return false;
	if( y1+h1<=y2 || y1>=y2+h2 ) return false;
	return true;
}

int b2dImagesOverlap( B2DImage *i1,int x1,int y1,B2DImage *i2,int x2,int y2 ){
	i1->debug();
	i2->debug();

	x1-=i1->hx;y1-=i1->hy;
	x2-=i2->hx;y2-=i2->hy;
	BBRect r1(x1,y1,x1+i1->width,y1+i1->height);
	BBRect r2(x2,y2,x2+i2->width,y2+i2->height);

	return r1.overlaps(r2);
}

int b2dImagesCollide( B2DImage *i1,int x1,int y1,int f1,B2DImage *i2,int x2,int y2,int f2 ){
	i1->debug(f1);
	i2->debug(f2);

	x1-=i1->hx;x2-=i2->hx;
	if( x1+i1->width<=x2 || x1>=x2+i2->width ) return false;
	y1-=i1->hy;y2-=i2->hy;
	if( y1+i1->height<=y2 || y1>=y2+i2->height ) return false;

	//to keep me sane!
	if( x1>x2 ){
		int tx=x1;x1=x2;x2=tx;
		int ty=y1;y1=y2;y2=ty;
		int tf=f1;f1=f2;f2=tf;
		B2DImage *ti=i1;i1=i2;i2=ti;
	}

	BBRect r1,r2,ir;
	r1.left=x1;r1.top=y1;r1.right=x1+i1->width;r1.bottom=y1+i1->height;
	r2.left=x2;r2.top=y2;r2.right=x2+i2->width;r2.bottom=y2+i2->height;
	ir.left=r1.left>r2.left ? r1.left : r2.left;
	ir.right=r1.right<r2.right ? r1.right : r2.right;
	ir.top=r1.top>r2.top ? r1.top : r2.top;
	ir.bottom=r1.bottom<r2.bottom ? r1.bottom : r2.bottom;

	unsigned *s1=bitMask(i1,f1);
	unsigned *s2=bitMask(i2,f2);
	int i1_pitch=(i1->width+31)/32+1;
	int i2_pitch=(i2->width+31)/32+1;

	s1+=(ir.top-r1.top)*i1_pitch;
	s2+=(ir.top-r2.top)*i2_pitch;

	int startx=ir.left-r1.left;
	int stopx=ir.right-r1.left-1;
	int shr=startx&31;
	int shl=32-shr;
	int cnt=stopx/32-startx/32;
	unsigned lwm=LWMS[stopx&31];

	s1+=startx/32;
	for( int y=ir.top;y<ir.bottom;++y ){
		unsigned p=0;
		unsigned *row1=s1,*row2=s2;
		for( int x=0;x<cnt;++x ){
			unsigned n=*row2++;
			if( ((n>>shr)|p) & *row1++ ) return true;
			p=shl<32 ? n<<shl : 0;
		}
		if( lwm && ( ((*row2>>shr)|p) & *row1 & lwm) ) return true;
		s1+=i1_pitch;s2+=i2_pitch;
	}
	return false;
}

int b2dImageRectOverlap( B2DImage *i1,int x1,int y1,int rx,int ry,int rw,int rh ){
	i1->debug();

	x1-=i1->hx;y1-=i1->hy;
	BBRect r1(x1,y1,x1+i1->width,y1+i1->height);
	return r1.overlaps( BBRect(rx,ry,rx+rw,ry+rh) );
}

int b2dImageRectCollide( B2DImage *i1,int x1,int y1,int f1,int x2,int y2,int w2,int h2 ){
	i1->debug(f1);

	x1-=i1->hx;if( x1+i1->width<=x2 || x1>=x2+w2 ) return false;
	y1-=i1->hy;if( y1+i1->height<=y2 || y1>=y2+h2 ) return false;

	BBRect r1(x1,y1,x1+i1->width,y1+i1->height);
	BBRect r2(x2,y2,x2+w2,y2+h2);
	BBRect ir(
		r1.left>r2.left ? r1.left : r2.left,
		r1.top>r2.top ? r1.top : r2.top,
		r1.right<r2.right ? r1.right : r2.right,
		r1.bottom<r2.bottom ? r1.bottom : r2.bottom );

	int maskPitch=(i1->width+31)/32+1;
	unsigned *s1=bitMask(i1,f1)+(ir.top-r1.top)*maskPitch;

	int startx=ir.left-r1.left;
	int stopx=ir.right-r1.left-1;
	int cnt=stopx/32-startx/32;
	unsigned fwm=FWMS[startx&31];
	unsigned lwm=LWMS[stopx&31];

	if( !cnt ) {fwm&=lwm;lwm=0;}

	s1+=startx/32;
	for( int h=ir.top;h<ir.bottom;++h ){
		unsigned *row=s1;
		if( *row & fwm ) return true;
		for( int x=1;x<cnt;++x ){
			if( *++row ) return true;
		}
		if( lwm && (*++row & lwm) ) return true;
		s1+=maskPitch;
	}
	return false;
}
