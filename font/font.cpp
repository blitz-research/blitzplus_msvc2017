
#include "font.h"

static BBFontDriver *_driver;

void bbSetFontDriver( BBFontDriver *p ){
	_driver=p;
}

BBFont *bbLoadFont( BBString *name,int height,bool bold,bool italic,bool underline ){
	int flags=0;
	if( bold ) flags|=BBFont::FONT_BOLD;
	if( italic ) flags|=BBFont::FONT_ITALIC;
	if( underline ) flags|=BBFont::FONT_UNDERLINE;
	return _driver->loadFont( name,height,flags );
}

void bbFreeFont( BBFont *font ){
	if( !font ) return;
	font->debug();
	font->release();
}

/*
arrrgh b2d name collision!

int bbFontWidth( BBFont *font ){
	font->debug();
	return font->width();
}

int bbFontHeight( BBFont *font ){
	font->debug();
	return font->height();
}
*/

BBString *bbFontName( BBFont *font ){
	font->debug();
	return font->name();
}

int bbFontSize( BBFont *font ){
	font->debug();
	return font->size();
}

int bbFontStyle( BBFont *font ){
	font->debug();
	return font->style();
}

int bbFontAscent( BBFont *font ){
	font->debug();
	return font->ascent();
}

int bbFontDescent( BBFont *font ){
	font->debug();
	return font->descent();
}
