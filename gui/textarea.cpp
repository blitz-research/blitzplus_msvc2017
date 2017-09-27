
#include "textarea.h"

BBTextArea::BBTextArea( BBGroup *group,int style ):BBGadget(group,style){
}

int BBTextArea::lineLength( int line ){
	return findChar(line+1)-findChar(line);
}

void		bbSetTextAreaTabs( BBTextArea *t,int tabs ){
	t->debug();
	t->setTabs(tabs);
}

void		bbSetTextAreaFont( BBTextArea *t,BBFont *f ){
	t->debug();
	t->setFont(f);
}

void		bbSetTextAreaColor( BBTextArea *t,int r,int g,int b,bool bg ){
	t->debug();
	if( bg ) t->setBackgroundColor(r,g,b);
	else t->setTextColor(r,g,b);
}

int			bbTextAreaLen( BBTextArea *t,int units ){
	t->debug();
	return t->length(units);
}

int			bbTextAreaSelLen( BBTextArea *t,int units ){
	t->debug();
	return t->selLength(units);
}

int			bbTextAreaCursor( BBTextArea *t,int units ){
	t->debug();
	return t->cursor(units);
}

int			bbTextAreaChar( BBTextArea *t,int lin ){
	t->debug();
	return t->findChar(lin);
}

int			bbTextAreaLine( BBTextArea *t,int chr ){
	t->debug();
	return t->findLine(chr);
}

void		bbAddTextAreaText( BBTextArea *t,BBString *text ){
	t->debug();
	t->addText(text);
}

void		bbSetTextAreaText( BBTextArea *t,BBString *text,int pos,int len,int units ){
	t->debug();
	t->setText( text,pos,len,units );
}

void		bbFormatTextAreaText( BBTextArea *t,int r,int g,int b,int flags,int pos,int len,int units ){
	t->debug();
	t->formatText( r,g,b,flags,pos,len,units );
}

BBString*	bbTextAreaText( BBTextArea *t,int pos,int len,int units ){
	t->debug();
	return t->getText( pos,len,units );
}

int			bbTextAreaLineLen( BBTextArea *t,int line ){
	t->debug();
	return t->lineLength(line);
}

void		bbLockTextArea( BBTextArea *t ){
	t->debug();
	t->lock();
}

void		bbUnlockTextArea( BBTextArea *t ){
	t->debug();
	t->unlock();
}
