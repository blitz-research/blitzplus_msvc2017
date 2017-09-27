
#ifndef SLIDER_H
#define SLIDER_H

#include "gadget.h"

class BBSlider : public BBGadget{
	int _visible,_total;
public:
	BBSlider( BBGroup *group,int style );

	virtual int		value()=0;
	virtual void	setValue( int val )=0;

	virtual void	setRange( int visible,int total );

	int				rangeVisible()const{ return _visible; }
	int				rangeTotal()const{ return _total; }
};

void	bbSetSliderRange( BBSlider *slider,int visible,int total );
void	bbSetSliderValue( BBSlider *slider,int value );
int		bbSliderValue( BBSlider *slider );

#endif
