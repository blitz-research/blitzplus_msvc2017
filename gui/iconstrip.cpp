
#include "iconstrip.h"

void bbFreeIconStrip( BBIconStrip *t ){
	t->debug();
	t->release();
}