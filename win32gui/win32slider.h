
#ifndef WIN32SLIDER_H
#define WIN32SLIDER_H

#include "../gui/slider.h"

#include "win32gadget.h"

class Win32Slider : public BBSlider,public Win32WndProc{
	Win32Gadget _gadget;
	int	tracking;
	int  updateValue();
public:
	Win32Slider( BBGroup *group,int style );

	void *query( int qid );

	void setFont( BBFont *font );
	void setText( BBString *text );
	void setShape( int x,int y,int w,int h );
	void setVisible( bool visible );
	void setEnabled( bool enabled );
	void activate();

	int  value();
	void setRange( int visible,int total );
	void setValue( int value );

	LRESULT wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc );
};



#endif
