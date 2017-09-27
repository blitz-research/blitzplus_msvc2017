
#include "grdriver2d.h"

#include <math.h>
#include <string.h>

static BBGrDriver2D *_driver;

bool BBRect::clip( BBRect *d )const{
	if( d->right<=d->left ||
		d->bottom<=d->top ||
		d->left>=right ||
		d->right<=left ||
		d->top>=bottom ||
		d->bottom<=top ) return false;
	if( d->left<left ) d->left=left;
	if( d->right>right ) d->right=right;
	if( d->top<top ) d->top=top;
	if( d->bottom>bottom ) d->bottom=bottom;
	return true;
}

bool BBRect::clip( BBRect *d,BBRect *s )const{
	if( d->right<=d->left ||
		d->bottom<=d->top ||
		d->left>=right ||
		d->right<=left ||
		d->top>=bottom ||
		d->bottom<=top ) return false;
	int dx,dy;
	if( (dx=left-d->left)>0 ){ d->left+=dx;s->left+=dx; }
	if( (dx=right-d->right)<0 ){ d->right+=dx;s->right+=dx; }
	if( (dy=top-d->top)>0 ){ d->top+=dy;s->top+=dy; }
	if( (dy=bottom-d->bottom)<0 ){ d->bottom+=dy;s->bottom+=dy; }
	return true;
}

void BBGrDriver2D::setContext( BBGrContext *t ){

	_graphics=t ? t->graphics() : 0;

	BBGrDriver::setContext(t);
}

void BBGrDriver2D::plot( int x,int y ){

	rect( x,y,1,1,true );
}

void BBGrDriver2D::line( int x0,int y0,int x1,int y1 ){

	int ddf,padj,sadj;
	int dx,dy,sx,sy,ax,ay;
	int cx0,cx1,cy0,cy1,clip0,clip1;

	const BBRect &rect=clipRect();

	cx0=rect.left;
	cx1=rect.right-1;
	cy0=rect.top;
	cy1=rect.bottom-1;

	for(;;){
		clip0=0;clip1=0;
		
		if(y0>cy1)clip0|=1;else if(y0<cy0)clip0|=2;
		if(x0>cx1)clip0|=4;else if(x0<cx0)clip0|=8;
		if(y1>cy1)clip1|=1;else if(y1<cy0)clip1|=2;
		if(x1>cx1)clip1|=4;else if(x1<cx0)clip1|=8;

		if((clip0|clip1)==0) break;		//draw line
		if((clip0&clip1)!=0) return;	//outside

		if((clip0&1)==1) {x0=x0+((x1-x0)*(cy1-y0))/(y1-y0);y0=cy1;continue;}
		if((clip0&2)==2) {x0=x0+((x1-x0)*(cy0-y0))/(y1-y0);y0=cy0;continue;}
		if((clip0&4)==4) {y0=y0+((y1-y0)*(cx1-x0))/(x1-x0);x0=cx1;continue;}
		if((clip0&8)==8) {y0=y0+((y1-y0)*(cx0-x0))/(x1-x0);x0=cx0;continue;}

		if((clip1&1)==1) {x1=x0+((x1-x0)*(cy1-y0))/(y1-y0);y1=cy1;continue;}
		if((clip1&2)==2) {x1=x0+((x1-x0)*(cy0-y0))/(y1-y0);y1=cy0;continue;}
		if((clip1&4)==4) {y1=y0+((y1-y0)*(cx1-x0))/(x1-x0);x1=cx1;continue;}
		if((clip1&8)==8) {y1=y0+((y1-y0)*(cx0-x0))/(x1-x0);x1=cx0;continue;}
	}

	dx=x1-x0;dy=y1-y0;
	if( (dx|dy)==0 ){
		plot( x0,y0 );
		return;
	}

	if (dx>=0) {sx=1;ax=dx;} else {sx=-1;ax=-dx;}
	if (dy>=0) {sy=1;ay=dy;} else {sy=-1;ay=-dy;}

	void *dst;
	int pitch,fmt;
	_graphics->lock( &dst,&pitch,&fmt );
	int sz=bbBytesPerPixel(fmt);
	unsigned t_color=color().toPixel( fmt );

	if( ax>ay ){
		ddf=-ax;sadj=ax+ax;padj=ay+ay;
		while( ax-->=0 ){
			memcpy( (char*)dst+y0*pitch+x0*sz,&t_color,sz );
			x0+=sx;ddf+=padj;if( ddf>=0 ){ y0+=sy;ddf-=sadj; }
		}
	}else{
		ddf=-ay;sadj=ay+ay;padj=ax+ax;
		while( ay-->=0 ){
			memcpy( (char*)dst+y0*pitch+x0*sz,&t_color,sz );
			y0+=sy;ddf+=padj;if( ddf>=0 ){ x0+=sx;ddf-=sadj; }
		}
	}
	_graphics->unlock();
}

void BBGrDriver2D::rect( int x,int y,int w,int h,bool solid ){

	BBRect crect( x,y,x+w,y+h );
	if( !clipRect().clip( &crect ) ) return;

	if( !solid ){
		rect( x,y,w,1,true );
		rect( x,y,1,h,true );
		rect( x+w-1,y,1,h,true );
		rect( x,y+h-1,w,1,true );
		return;
	}

	_graphics->clear( crect.x(),crect.y(),crect.width(),crect.height(),color() );
}

void BBGrDriver2D::oval( int x1,int y1,int w,int h,bool solid ){

	BBRect dest(x1,y1,x1+w,y1+h);
	if( !clipRect().clip( &dest ) ) return;

	float xr=w*.5f,yr=h*.5f,ar=(float)w/(float)h;
	float cx=x1+xr+.5f,cy=y1+yr-.5f,rsq=yr*yr,y;

	if( solid ){
		y=dest.top-cy;
		for( int t=dest.top;t<dest.bottom;++y,++t ){
			float x=(float)sqrt( rsq-y*y )*ar;
			int xa=(int)floor( cx-x ),xb=(int)floor( cx+x );
			rect( xa,t,xb-xa,1,true );
		}
		return;
	}

	int l,r,p_xa,p_xb,t,hh=(int)floor(cy);

	p_xa=p_xb=(int)cx;
	t=dest.top;y=t-cy;
	if( dest.top>y1 ){ --t;--y; }
	for( ;t<=hh;++y,++t ){
		float x=(float)sqrt( rsq-y*y )*ar;
		int xa=(int)floor( cx-x ),xb=(int)floor( cx+x );

		l=xa;r=p_xa;if(r<=l)r=l+1;
		rect( l,t,r-l,1,true );
		l=p_xb;r=xb;if(l>=r)l=r-1;
		rect( l,t,r-l,1,true );

		p_xa=xa;p_xb=xb;
	}

	p_xa=p_xb=(int)cx;
	t=dest.bottom-1;y=t-cy;
	if( dest.bottom<y1+h ){ ++t;++y; }
	for( ;t>hh;--y,--t ){
		float x=(float)sqrt( rsq-y*y )*ar;
		int xa=(int)floor( cx-x ),xb=(int)floor( cx+x );

		l=xa;r=p_xa;if(r<=l)r=l+1;
		rect( l,t,r-l,1,true );
		l=p_xb;r=xb;if(l>=r)l=r-1;
		rect( l,t,r-l,1,true );

		p_xa=xa;p_xb=xb;
	}
}

void BBGrDriver2D::blit( int x,int y,int w,int h,BBGraphics *src_g,int sx,int sy,int flags ){

	BBRect dst_rect(x,y,x+w,y+h),src_rect(sx,sy,sx+w,sy+h);

	if( !clipRect().clip( &dst_rect,&src_rect ) ) return;

	_graphics->blit( 
		dst_rect.x(),dst_rect.y(),
		dst_rect.width(),dst_rect.height(),
		src_g,src_rect.x(),src_rect.y(),flags );
}

void BBGrDriver2D::text( int x,int y,BBString *text ){
	bbError( "BBGrDriver2D::Text not implemented" );
}

void bbSetGrDriver2D( BBGrDriver2D *t ){
	_driver=t;
}

BBGrDriver2D *bbGrDriver2D(){
	return _driver;
}
