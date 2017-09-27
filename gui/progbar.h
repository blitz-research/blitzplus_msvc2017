
#ifndef PROGBAR_H
#define PROGBAR_H

#include "gadget.h"

class BBProgBar : public BBGadget{
	float _progress;
public:
	BBProgBar( BBGroup *group,int style );

	virtual void setProgress( float t );

	float progress()const{ return _progress; }

	void debug(){ _debug(this,"ProgBar Gadget"); }
};

void		bbUpdateProgBar( BBProgBar *prog,float progress );

#endif
