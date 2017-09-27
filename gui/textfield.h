
#ifndef TEXTFIELD_H
#define TEXTFIELD_H

#include "gadget.h"

class BBTextField : public BBGadget{
public:
	BBTextField( BBGroup *group,int style );

	virtual BBString *textFieldText()=0;

	void debug(){ _debug(this,"TextField Gadget"); }
};

BBString*	bbTextFieldText( BBTextField *tf );

#endif
