
#ifndef BUTTON_H
#define BUTTON_H

#include "gadget.h"

class BBButton : public BBGadget{
public:
	BBButton( BBGroup *group,int style );

	virtual int		state()=0;
	virtual	void	setState( int state )=0;
};

void		bbSetButtonState( BBButton *button,int state );
int			bbButtonState( BBButton *button );

#endif