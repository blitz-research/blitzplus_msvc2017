
#ifndef GROUP_H
#define GROUP_H

#include "gadget.h"

#include <list>

class BBGroup : public BBGadget{
	int _cx,_cy,_cw,_ch;
	std::list<BBGadget*> _kids;
protected:
	BBGroup( BBGroup *group,int style );
	~BBGroup();

	virtual void layoutChildren();
	virtual void clientShape( int *x,int *y,int *w,int *h )=0;

public:

	void setShape( int x,int y,int w,int h );

	int clientX()const{ return _cx; }
	int clientY()const{ return _cy; }
	int clientWidth()const{ return _cw; }
	int clientHeight()const{ return _ch; }

	//only for Gadget use!
	void add( BBGadget *gadget );
	void remove( BBGadget *gadget );

	void debug(){ _debug(this,"Group Gadget"); }
};

int		bbClientWidth( BBGroup *group );
int		bbClientHeight( BBGroup *group );

#endif