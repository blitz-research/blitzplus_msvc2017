
#ifndef SCREEN_H
#define SCREEN_H

#include "../visual/visual.h"

#include "../grdriver/grdriver.h"

class BBScreen : public BBEventSource{
	int _pmx,_pmy;
protected:
	~BBScreen();
public:
	enum{
		SCREEN_VIRTUAL=4,
		SCREEN_DOUBLEBUFFER=8,
		SCREEN_TRIPLEBUFFER=16,
		SCREEN_GUICOMPATIBLE=32
	};

	BBScreen();

	virtual BBGraphics *graphics()=0;
	virtual void flip( bool sync )=0;
	virtual void setPointer( int n )=0;
	virtual void moveMouse( int x,int y )=0;
	virtual int  mouseX()=0;
	virtual int  mouseY()=0;

	virtual void activateScreen();
	virtual void setTitle( BBString *title );

	virtual void setGamma( int r,int g,int b,int dr,int dg,int db );
	virtual void getGamma( int r,int g,int b,int *dr,int *dg,int *db );
	virtual void updateGamma( bool calibrate );

	int mouseXSpeed(){ int x=mouseX();int t=x-_pmx;_pmx=x;return t; }
	int mouseYSpeed(){ int y=mouseY();int t=y-_pmy;_pmy=y;return t; }

	void debug(){ _debug(this,"Screen"); }
};

class BBScreenDriver : public BBModule{
public:
	enum{
		POINTER_NULL=0,
		POINTER_DEFAULT=1,
		POINTER_BUSY=2,
		POINTER_SIZEX=3,
		POINTER_SIZEY=4,
		POINTER_SIZEXY=5,
		POINTER_CROSSHAIR=6
	};

	virtual int			scanLine();
	virtual int			availVidMem();
	virtual int			totalVidMem();
	virtual void		vwait( int frames );

	virtual int			screenModes()=0;
	virtual void		enumScreenMode( int n,int *w,int *h,int *fmt )=0;

	virtual BBScreen*	createScreen( int w,int h,int fmt,int flags )=0;
	virtual BBScreen*	desktopScreen()=0;
};

void		bbSetScreenDriver( BBScreenDriver *t );
BBScreen*	bbActiveScreen();
BBScreen*	bbDesktopScreen();

int			bbScanLine();
void		bbVWait( int count );
int			bbAvailVidMem();
int			bbTotalVidMem();

int			bbScreenModes();
void		bbEnumScreenMode( int n,int *w,int *h,int *fmt );
BBScreen*	bbCreateScreen( BBString *title,int w,int h,int fmt,int flags );
void		bbFreeScreen( BBScreen *screen );
void		bbActivateScreen( BBScreen *screen );
void		bbSetScreenTitle( BBScreen *screen,BBString *title );
void		bbFlipScreen( BBScreen *screen,bool sync );
BBGraphics*	bbScreenGraphics( BBScreen *screen );

#endif
