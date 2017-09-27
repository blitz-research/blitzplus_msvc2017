
#include "textfield.h"

BBTextField::BBTextField( BBGroup *group,int style ):BBGadget(group,style){
}

BBString *bbTextFieldText( BBTextField *tf ){
	tf->debug();
	return tf->textFieldText();
}
