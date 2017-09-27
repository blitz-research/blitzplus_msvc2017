
#include "htmlview.h"

BBHtmlView::BBHtmlView( BBGroup *group,int style ):BBGadget(group,style){
}

void bbHtmlViewGo( BBHtmlView *t,BBString *url ){
	t->debug();
	t->go( url );
}

void bbHtmlViewRun( BBHtmlView *t,BBString *url ){
	t->debug();
	t->run( url );
}

void bbHtmlViewBack( BBHtmlView *t ){
	t->debug();
	t->back();
}

void bbHtmlViewForward( BBHtmlView *t ){
	t->debug();
	t->forward();
}

int bbHtmlViewStatus( BBHtmlView *t ){
	t->debug();
	return t->getstatus();
}

BBString *bbHtmlViewCurrentURL( BBHtmlView *t ){
	t->debug();
	return t->getcurrenturl();
}

BBString *bbHtmlViewEventURL( BBHtmlView *t ){
	t->debug();
	return t->geteventurl();
}