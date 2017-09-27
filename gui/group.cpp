
#include "group.h"

BBGroup::BBGroup( BBGroup *group,int style ):BBGadget(group,style){
}

BBGroup::~BBGroup(){
	while( !_kids.empty() ){
		BBGadget *t=*_kids.begin();
		if( t->refs()>1 ){
			bbError( "Error removing child gadget" );
		}
		t->release();
	}
}

void BBGroup::add( BBGadget *gadget ){
	_kids.push_back( gadget );
}

void BBGroup::remove( BBGadget *gadget ){
	std::list<BBGadget*>::iterator it;
	for( it=_kids.begin();it!=_kids.end();++it ){
		if( *it==gadget ){
			_kids.erase(it);
			return;
		}
	}
	bbError( "Error removing gadget from group" );
}

void BBGroup::clientShape( int *x,int *y,int *w,int *h ){
	*x=_cx;*y=_cy;*w=_cw;*h=_ch;
}

void BBGroup::layoutChildren(){
	std::list<BBGadget*>::iterator it;
	for( it=_kids.begin();it!=_kids.end();++it ){
		(*it)->performLayout();
	}
}

void BBGroup::setShape( int x,int y,int w,int h ){
	BBGadget::setShape( x,y,w,h );

	int pw=_cw,ph=_ch;
	clientShape( &_cx,&_cy,&_cw,&_ch );
	if( pw==_cw && ph==_ch ) return;

	layoutChildren();
}

int bbClientWidth( BBGroup *group ){
	group->debug();
	return group->clientWidth();
}

int bbClientHeight( BBGroup *group ){
	group->debug();
	return group->clientHeight();
}