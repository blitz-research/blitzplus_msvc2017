
#ifndef CANVAS_H
#define CANVAS_H

#include "../gui/gui.h"

#include "../graphics/graphics.h"

class BBCanvas : public BBGroup{
	int _pmx,_pmy;

protected:
	~BBCanvas();
public:
	BBCanvas( BBGroup *group,int style );

	virtual BBGraphics *graphics()=0;
	virtual void flip( bool sync )=0;
	virtual void setPointer( int n )=0;
	virtual void moveMouse( int x,int y )=0;
	virtual int  mouseX()=0;
	virtual int  mouseY()=0;

	int mouseXSpeed(){ int x=mouseX();int t=x-_pmx;_pmx=x;return t; }
	int mouseYSpeed(){ int y=mouseY();int t=y-_pmy;_pmy=y;return t; }

	void debug(){ _debug(this,"Canvas Gadget"); }
};

class BBCanvasDriver : public BBModule{
public:
	virtual BBCanvas *createCanvas( BBGroup *group,int style )=0;
};

void		bbSetCanvasDriver( BBCanvasDriver *t );

BBCanvas*	bbCreateCanvas( int x,int y,int w,int h,BBGroup *group,int style );
BBGraphics*	bbCanvasGraphics( BBCanvas *canvas );
void		bbFlipCanvas( BBCanvas *canvas,bool sync );

#endif
