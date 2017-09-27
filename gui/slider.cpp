
#include "slider.h"

BBSlider::BBSlider( BBGroup *group,int style ):BBGadget(group,style),
_visible(0),_total(0){
}

void BBSlider::setRange( int visible,int total ){
	_visible=visible;
	_total=total;
}

void bbSetSliderRange( BBSlider *slider,int visible,int total ){
	slider->debug();
	slider->setRange( visible,total );
}

void bbSetSliderValue( BBSlider *slider,int value ){
	slider->debug();
	slider->setValue( value );
}

int bbSliderValue( BBSlider *slider ){
	slider->debug();
	return slider->value();
}
