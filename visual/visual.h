
#ifndef VISUAL_H
#define VISUAL_H

#include "../event/event.h"
#include "../graphics/graphics.h"

class BBVisual : public BBEventSource{
	int _mx,_my;
public:
	BBVisual();

	enum{
		POINTER_NULL=0,
		POINTER_ARROW=1,
		POINTER_CROSS=2
	};

	virtual int			width();
	virtual int			height();
	virtual BBGraphics*	graphics();
	virtual void		setPointer( int n );
	virtual void		moveMouse( int x,int y );

	virtual int			mouseX();
	virtual int			mouseY();
};

#endif