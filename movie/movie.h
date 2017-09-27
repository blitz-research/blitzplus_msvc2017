
#ifndef MOVIE_H
#define MOVIE_H

#include "../graphics/graphics.h"

class BBMovie : public BBResource{
public:

	virtual int  width()=0;
	virtual int  height()=0;
	virtual bool render( BBGraphics *dst,int x,int y,int w,int h )=0;
	virtual bool playing()=0;

	void debug(){ _debug(this,"Movie Graphics"); }
};

class BBMovieDriver : public BBModule{
public:
	virtual BBMovie *loadMovie( BBString *file )=0;
};

void		bbSetMovieDriver( BBMovieDriver *t );

BBMovie*	bbLoadMovie( BBString *file );

#endif