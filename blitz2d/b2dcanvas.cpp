
#include "b2dcanvas.h"

BBCanvas*	b2dCreateCanvas( int x,int y,int w,int h,BBGroup *group,int style ){
	group->debug();

	BBCanvas *c=bbCreateCanvas( x,y,w,h,group,style );
	BBGraphics *g=c->graphics();

	B2DBuffer *t=new B2DBuffer( g );
	c->attach( t );

	g->clear( 0,0,g->width(),g->height(),0xff000000 );
	c->flip(false);
	g->clear( 0,0,g->width(),g->height(),0xff000000 );

	return c;
}

B2DBuffer*	b2dCanvasBuffer( BBCanvas *c ){
	c->debug();
	B2DBuffer *t=(B2DBuffer*)c->findAttached( BBQID_B2DBUFFER );
	return t;
}

void		b2dFlipCanvas( BBCanvas *c,bool sync ){
	c->debug();
	c->flip( sync );
}

