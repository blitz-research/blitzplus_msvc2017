
#ifndef MENU_H
#define MENU_H

#include "../app/app.h"

class BBMenu : public BBObject{
	BBString *_text;
	int _tag;
	bool _checked,_enabled;
	BBMenu *_children,*_succ;
protected:
	~BBMenu();
public:
	BBMenu( BBString *text,int tag,BBMenu *parent );

	void setText( BBString *text );
	void setChecked( bool checked );
	void setEnabled( bool enabled );

	int tag()const{ return _tag; }
	bool checked()const{ return _checked; }
	bool enabled()const{ return _enabled; }
	BBString *text()const{ return _text->copy(); }

	BBMenu *children()const{ return _children; }
	BBMenu *successor()const{ return _succ; }

	void debug(){ _debug(this,"Menu"); }
};

BBMenu*		bbCreateMenu( BBString *text,int tag,BBMenu *parent );

void		bbFreeMenu( BBMenu *menu );

void		bbSetMenuText( BBMenu *menu,BBString *text );
void		bbCheckMenu( BBMenu *menu );
void		bbUncheckMenu( BBMenu *menu );
void		bbEnableMenu( BBMenu *menu );
void		bbDisableMenu( BBMenu *menu );

BBString*	bbMenuText( BBMenu *menu );
int			bbMenuChecked( BBMenu *menu );
int			bbMenuEnabled( BBMenu *menu );

#endif