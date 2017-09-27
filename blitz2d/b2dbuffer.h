
#ifndef B2DBUFFER_H
#define B2DBUFFER_H

#include "../grdriver2d/grdriver2d.h"

#include "../bank/bank.h"

extern const int BBQID_B2DBUFFER;

class B2DBuffer : public BBResource{
protected:
	~B2DBuffer();
public:
	int locked;
	BBBank *lockedpixels;
	int lockedpitch,lockedformat;
	BBGraphics *graphics;
	BBGrContext *context;
	int ox,oy;
	unsigned color,clsColor;

	B2DBuffer( BBGraphics *g );

	void *query( int qid );

#ifdef _DEBUG
	void debug(){ _debug(this,"Blitz2D Buffer"); }
#else
	void debug(){}
#endif
};

#endif