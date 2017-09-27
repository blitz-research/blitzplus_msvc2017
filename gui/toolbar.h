
#ifndef TOOLBAR_H
#define TOOLBAR_H

#include "gadget.h"

class BBToolBar : public BBGadget{
public:
	BBToolBar( BBGroup *group,int style );

	virtual void setItemEnabled( int n,bool enabled );
	virtual void setTips( BBString *tips );

	void debug(){ _debug(this,"ToolBar Gadget"); }
};

void	bbEnableToolBarItem( BBToolBar *t,int n );
void	bbDisableToolBarItem( BBToolBar *t,int n );
void	bbSetToolBarTips( BBToolBar *t,BBString *tips );

#endif