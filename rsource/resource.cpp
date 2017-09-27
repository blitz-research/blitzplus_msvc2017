
#include "../app/app.h"

BBResource::Node::Node():resource(0){
	a_succ=a_pred=n_succ=n_pred=this;
}

BBResource::Node::Node( BBResource *t,Node *a_suc,Node *n_suc ):resource(t),a_succ(a_suc),n_succ(n_suc){
	a_pred=a_succ->a_pred;a_succ->a_pred=a_pred->a_succ=this;
	n_pred=n_succ->n_pred;n_succ->n_pred=n_pred->n_succ=this;
}

BBResource::Node::~Node(){
	a_pred->a_succ=a_succ;a_succ->a_pred=a_pred;
	n_pred->n_succ=n_succ;n_succ->n_pred=n_pred;
}

void BBResource::destroy(){
	while( BBResource *t=_lists.a_succ->resource ){
		delete _lists.a_succ;
		t->release();
	}
	while( _lists.n_succ->resource ) delete _lists.n_succ;

	BBObject::destroy();
}

void BBResource::attach( BBResource *t ){
	new Node( t,&_lists,&t->_lists );
}

void *BBResource::findAttached( int qid ){
	for( Node *p=_lists.a_succ;p!=&_lists;p=p->a_succ ){
		if( void *q=p->resource->query(qid) ) return q;
	}
	return 0;
}
