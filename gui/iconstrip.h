
#ifndef ICONSTRIP_H
#define ICONSTRIP_H

#include "../graphics/graphics.h"

class BBIconStrip : public BBResource{
public:

	virtual int	icons()=0;
	virtual int iconWidth()=0;
	virtual int iconHeight()=0;

	void debug(){ _debug(this,"IconStrip"); }
};

void bbFreeIconStrip( BBIconStrip *t );

#endif
