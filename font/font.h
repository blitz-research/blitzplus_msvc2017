
#ifndef FONT_H
#define FONT_H

#include "../string/string.h"

class BBFont : public BBResource{
public:
	enum{
		FONT_BOLD=1,
		FONT_ITALIC=2,
		FONT_UNDERLINE=4
	};

	virtual int  width()=0;
	virtual int  height()=0;
	virtual int	 ascent()=0;
	virtual int  descent()=0;
	virtual void measure( BBString *string,int *w,int *h )=0;

	virtual BBString *name()=0;
	virtual int size()=0;
	virtual int style()=0;

	void debug(){ _debug(this,"Font"); }
};

class BBFontDriver : public BBModule{
public:
	virtual BBFont *loadFont( BBString *name,int height,int flags )=0;
};

void		bbSetFontDriver( BBFontDriver *t );

BBFont*		bbLoadFont( BBString *name,int height,bool bold,bool italic,bool underline );
void		bbFreeFont( BBFont *font );

//int			bbFontWidth( BBFont *font );
//int			bbFontHeight( BBFont *font );

BBString*	bbFontName( BBFont *font );
int			bbFontSize( BBFont *font );
int			bbFontStyle ( BBFont *font );
int			bbFontFlags( BBFont *font );
int			bbFontAscent( BBFont *font );
int			bbFontDescent( BBFont *font );

#endif