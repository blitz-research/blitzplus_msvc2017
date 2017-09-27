
#include "toolbar.h"

BBToolBar::BBToolBar( BBGroup *group,int style ):BBGadget(group,style){
}

void BBToolBar::setItemEnabled( int n,bool e ){
}

void BBToolBar::setTips( BBString *tips ){
}

void	bbEnableToolBarItem( BBToolBar *t,int n ){
	t->debug();
	t->setItemEnabled(n,true);
}

void	bbDisableToolBarItem( BBToolBar *t,int n ){
	t->debug();
	t->setItemEnabled(n,false);
}

void	bbSetToolBarTips( BBToolBar *t,BBString *tips ){
	t->debug();
	t->setTips( tips );
}

