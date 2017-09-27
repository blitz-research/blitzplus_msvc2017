
#include "menu.h"

BBMenu::BBMenu( BBString *text,int tag,BBMenu *parent ):
_text(text),_tag(tag),_checked(false),_enabled(true),_children(0),_succ(0){
	_text->retain();

	if( !parent ) return;

	BBMenu **prev=&parent->_children,*curr;
	while( curr=*prev ) prev=&curr->_succ;
	*prev=this;
}

BBMenu::~BBMenu(){
	while( BBMenu *t=_children ){
		_children=t->_succ;
		if( t->refs()>1 ){
			bbError( "Unable to release menu child" );
		}
		t->release();
	}
	_text->release();
}

void BBMenu::setText( BBString *text ){
	_text->release();
	if( _text=text ) _text->retain();
}

void BBMenu::setChecked( bool checked ){
	_checked=checked;
}

void BBMenu::setEnabled( bool enabled ){
	_enabled=enabled;
}

BBMenu*		bbCreateMenu( BBString *text,int tag,BBMenu *parent ){
	parent->debug();
	return new BBMenu( text,tag,parent );
}

void		bbFreeMenu( BBMenu *menu ){
	if( !menu ) return;
	menu->debug();
	menu->release();
}

void		bbSetMenuText( BBMenu *menu,BBString *text ){
	menu->debug();
	menu->setText( text );
}

void		bbCheckMenu( BBMenu *menu ){
	menu->debug();
	menu->setChecked(true);
}

void		bbUncheckMenu( BBMenu *menu ){
	menu->debug();
	menu->setChecked(false);
}

void		bbEnableMenu( BBMenu *menu ){
	menu->debug();
	menu->setEnabled(true);
}

void		bbDisableMenu( BBMenu *menu ){
	menu->debug();
	menu->setEnabled(false);
}

BBString*	bbMenuText( BBMenu *menu ){
	menu->debug();
	return menu->text();
}

int			bbMenuChecked( BBMenu *menu ){
	menu->debug();
	return menu->checked();
}

int			bbMenuEnabled( BBMenu *menu ){
	menu->debug();
	return menu->enabled();
}
