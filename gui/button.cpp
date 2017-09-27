
#include "button.h"

BBButton::BBButton( BBGroup *group,int style ):BBGadget(group,style){
}

void bbSetButtonState( BBButton *button,int state ){
	button->debug();
	button->setState(state);
}

int bbButtonState( BBButton *button ){
	button->debug();
	return button->state();
}