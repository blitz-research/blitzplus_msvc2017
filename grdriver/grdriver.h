
#ifndef GRDRIVER_H
#define GRDRIVER_H

#include "../graphics/graphics.h"

class BBGrContext : public BBResource{
	BBGraphics *_graphics;
protected:
	~BBGrContext();
public:
	BBGrContext( BBGraphics *g );

	BBGraphics*				graphics()const{ return _graphics; }
};

class BBGrDriver : public BBModule{
	BBGrContext *_context;
public:
	virtual void			setContext( BBGrContext *t );

	virtual BBGrContext*	createContext( BBGraphics *g )=0;

	BBGrContext*			context()const{ return _context; }
};

#endif