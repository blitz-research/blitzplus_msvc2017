
#include "treeview.h"

BBTreeView::BBTreeView( BBGroup *group,int style ):BBGadget(group,style){
}

BBTreeViewNode::BBTreeViewNode( BBTreeViewNode *parent ):_parent(parent){
	if( _parent ) _parent->_kids.push_back(this);
}

BBTreeViewNode::~BBTreeViewNode(){
	std::list<BBTreeViewNode*>::const_iterator it;
	while( _kids.begin()!=_kids.end() ) (*_kids.begin())->release();
	if( _parent ) _parent->_kids.remove(this);
}

BBTreeViewNode*	bbTreeViewRoot( BBTreeView *tree ){
	tree->debug();
	return tree->root();
}

BBTreeViewNode*	bbSelectedTreeViewNode( BBTreeView *tree ){
	tree->debug();
	return tree->selected();
}

int				bbCountTreeViewNodes( BBTreeViewNode *node ){
	node->debug();
	return node->countKids();
}

BBTreeViewNode*	bbAddTreeViewNode( BBString *text,BBTreeViewNode *node,int icon ){
	node->debug();
	return node->add( text,icon );
}

BBTreeViewNode*	bbInsertTreeViewNode( int index,BBString *text,BBTreeViewNode *node,int icon ){
	node->debug();
	return node->insert( index,text,icon );
}

void			bbModifyTreeViewNode( BBTreeViewNode *node,BBString *text,int icon ){
	node->debug();
	node->modify( text,icon );
}

void			bbSelectTreeViewNode( BBTreeViewNode *node ){
	node->debug();
	node->select();
}

void			bbExpandTreeViewNode( BBTreeViewNode *node ){
	node->debug();
	node->expand();
}

void			bbCollapseTreeViewNode( BBTreeViewNode *node ){
	node->debug();
	node->collapse();
}

void			bbFreeTreeViewNode( BBTreeViewNode *node ){
	if( !node ) return;
	node->debug();
	node->release();
}

BBString *bbTreeViewNodeText( BBTreeViewNode *node ){
	node->debug();
	return node->treeViewNodeText();
}
