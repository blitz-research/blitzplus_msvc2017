
#ifndef TREEVIEW_H
#define TREEVIEW_H

//not very helpful wrapper!

#include "gadget.h"

#include <list>

class BBTreeViewNode;

class BBTreeView : public BBGadget{
public:
	BBTreeView( BBGroup *group,int style );

	virtual BBTreeViewNode *root()=0;
	virtual BBTreeViewNode *selected()=0;

	void debug(){ _debug(this,"TreeView Gadget"); }
};

class BBTreeViewNode : public BBObject{
	BBTreeViewNode *_parent;
	std::list<BBTreeViewNode*> _kids;
protected:
	BBTreeViewNode( BBTreeViewNode *parent );
	~BBTreeViewNode();
public:

	virtual void select()=0;
	virtual void expand()=0;
	virtual void collapse()=0;

	virtual void modify( BBString *text,int icon )=0;
	virtual BBTreeViewNode *add( BBString *item,int icon )=0;
	virtual BBTreeViewNode *insert( int index,BBString *item,int icon )=0;

	virtual BBString *treeViewNodeText()=0;

	int		countKids()const{ return _kids.size(); }

	void debug(){ _debug(this,"TreeView Node"); }
};

BBTreeViewNode*	bbTreeViewRoot( BBTreeView *tree );
BBTreeViewNode*	bbSelectedTreeViewNode( BBTreeView *tree );

int				bbCountTreeViewNodes( BBTreeViewNode *node );

BBTreeViewNode*	bbAddTreeViewNode( BBString *text,BBTreeViewNode *node,int icon );
BBTreeViewNode*	bbInsertTreeViewNode( int index,BBString *text,BBTreeViewNode *node,int icon );
void			bbModifyTreeViewNode( BBTreeViewNode *node,BBString *text,int icon );

void			bbSelectTreeViewNode( BBTreeViewNode *node );
void			bbExpandTreeViewNode( BBTreeViewNode *node );
void			bbCollapseTreeViewNode( BBTreeViewNode *node );
void			bbFreeTreeViewNode( BBTreeViewNode *node );

BBString*		bbTreeViewNodeText( BBTreeViewNode *node );

#endif
