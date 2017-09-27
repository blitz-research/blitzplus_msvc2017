
#ifndef RESOURCE_H
#define RESOURCE_H

#include "../object/object.h"

class BBResource : public BBObject{
	struct Node{
		BBResource *resource;
		Node *a_succ,*a_pred,*n_succ,*n_pred;
		Node();
		Node( BBResource *t,Node *a_succ,Node *n_succ );
		~Node();
	};
	Node _lists;

protected:
	void destroy();

public:
	void	attach( BBResource *t );
	void*	findAttached( int qid );
};

#endif