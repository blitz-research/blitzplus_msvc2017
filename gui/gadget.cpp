
#include "gadget.h"
#include "combobox.h"
#include "listbox.h"
#include "tabber.h"

BBGadget::BBGadget( BBGroup *group,int style ):_group(group),_style(style),
_x(0),_y(0),_w(0),_h(0),_visible(false),_enabled(true),
_font(0),_text(0),_strip(0),
_left(0),_right(0),_top(0),_bottom(0){
	setText( BBString::null() );
	if( _group ){
		_group->add(this);
		_screen=_group->_screen;
	}else{
		_screen=bbActiveScreen();
	}
	lockLayout();
}

BBGadget::~BBGadget(){
	if( _group ) _group->remove(this);
	setIconStrip(0);
	setText(0);
	setFont(0);
}

void BBGadget::setFont( BBFont *font ){
	if( _font ) _font->release();
	if( _font=font ) _font->retain();
}

void BBGadget::setText( BBString *text ){
	if( _text ) _text->release();
	if( _text=text ) _text->retain();
}

void BBGadget::setIconStrip( BBIconStrip *strip ){
	if( _strip ) _strip->release();
	if( _strip=strip ) _strip->retain();
}

void BBGadget::setShape( int x,int y,int w,int h ){
	_x=x;_y=y;_w=w;_h=h;
}

void BBGadget::setVisible( bool visible ){
	_visible=visible;
}

void BBGadget::setEnabled( bool enabled ){
	_enabled=enabled;
}

void BBGadget::activate(){
}

void BBGadget::lockLayout(){
	_ox=_x;_oy=_y;_ow=_w;_oh=_h;
	if( _group ){
		_gw=_group->clientWidth();
		_gh=_group->clientHeight();
		if( _gw<1 ) _gw=1;
		if( _gh<1 ) _gh=1;
	}
}

void BBGadget::setLayout( int left,int right,int top,int bottom ){
	_left=left;_right=right;
	_top=top;_bottom=bottom;
	lockLayout();
}

void BBGadget::performLayout(){
	if( !_group ) return;

	int gw=_group->clientWidth(),gh=_group->clientHeight();
	if( gw<1 ) gw=1;
	if( gh<1 ) gh=1;

	int x=_x,x2=_x+_w;

	if( _left || _right ){

		if( _left==LAYOUT_ABSOLUTE ) x=_ox;
		else if( _left==LAYOUT_PROPORTIONAL ) x=gw*_ox/_gw;

		if( _right==LAYOUT_ABSOLUTE ) x2=_ox+_ow-_gw+gw;
		else if( _right==LAYOUT_PROPORTIONAL ) x2=gw*(_ox+_ow)/_gw;

		if( !_left ) x=x2-_ow;
		else if( !_right ) x2=x+_ow;

	}else{
		x=gw*(_ox+_ow/2)/_gw-_ow/2;
		x2=x+_ow;
	}

	int y=_y,y2=_y+_h;

	if( _top || _bottom ){

		if( _top==LAYOUT_ABSOLUTE ) y=_oy;
		else if( _top==LAYOUT_PROPORTIONAL ) y=gh*_oy/_gh;

		if( _bottom==LAYOUT_ABSOLUTE ) y2=_oy+_oh-_gh+gh;
		else if( _bottom==LAYOUT_PROPORTIONAL ) y2=gh*(_oy+_oh)/_gh;

		if( !_top ) y=y2-_oh;
		else if( !_bottom ) y2=y+_oh;

	}else{
		y=gh*(_oy+_oh/2)/_gh-_oh/2;
		y2=y+_oh;
	}

	setShape( x,y,x2-x,y2-y );
}

void bbFreeGadget( BBGadget *gad ){
	if( !gad ) return;
	gad->debug();
	gad->release();
}

void bbSetGadgetShape( BBGadget *gad,int x,int y,int w,int h ){
	gad->debug();
	gad->setShape(x,y,w,h);
	gad->lockLayout();
}

void bbSetGadgetFont( BBGadget *gad,BBFont *font ){
	gad->debug();
	font->debug();
	gad->setFont(font);
}

void bbSetGadgetText( BBGadget *gad,BBString *text ){
	gad->debug();
	gad->setText( text );
}

BBGroup *bbGadgetGroup( BBGadget *gad ){
	gad->debug();
	return gad->group();
}

void		bbSetGadgetLayout( BBGadget *gad,int left,int right,int top,int bottom ){
	gad->debug();
	gad->setLayout( left,right,top,bottom );
	gad->lockLayout();
}

void		bbShowGadget( BBGadget *gad ){
	gad->debug();
	gad->setVisible(true);
}

void		bbHideGadget( BBGadget *gad ){
	gad->debug();
	gad->setVisible(false);
}

void		bbEnableGadget( BBGadget *gad ){
	gad->debug();
	gad->setEnabled(true);
}

void		bbDisableGadget( BBGadget *gad ){
	gad->debug();
	gad->setEnabled(false);
}

void		bbActivateGadget( BBGadget *gad ){
	gad->debug();
	gad->activate();
}

void		bbSetGadgetIconStrip( BBGadget *g,BBIconStrip *t ){
	g->debug();
	t->debug();
	g->setIconStrip(t);
}

int			bbGadgetX( BBGadget *gad ){
	gad->debug();
	return gad->x();
}

int			bbGadgetY( BBGadget *gad ){
	gad->debug();
	return gad->y();
}

int			bbGadgetWidth( BBGadget *gad ){
	gad->debug();
	return gad->width();
}

int			bbGadgetHeight( BBGadget *gad ){
	gad->debug();
	return gad->height();
}

BBFont*		bbGadgetFont( BBGadget *gad ){
	gad->debug();
	return gad->font();
}

BBString* bbGadgetText( BBGadget *gad ){
	gad->debug();
	return gad->text();
}

void		bbClearGadgetItems( BBGadget *g ){
	g->debug();
	if( BBComboBox *t=dynamic_cast<BBComboBox*>(g) ){
		t->clear();
	}else if( BBListBox *t=dynamic_cast<BBListBox*>(g) ){
		t->clear();
	}else if( BBTabber *t=dynamic_cast<BBTabber*>(g) ){
		t->clear();
	}else{
		bbError( "Invalid Gadget type" );
	}
}

void		bbAddGadgetItem( BBGadget *g,BBString *text,int select,int icon ){
	g->debug();
	if( BBComboBox *t=dynamic_cast<BBComboBox*>(g) ){
		t->add( text,icon );
		if( select ) t->select( t->items()->size()-1 );
	}else if( BBListBox *t=dynamic_cast<BBListBox*>(g) ){
		t->add( text,icon );
		if( select ) t->select( t->items()->size()-1 );
	}else if( BBTabber *t=dynamic_cast<BBTabber*>(g) ){
		t->add( text,icon );
		if( select ) t->select( t->items()->size()-1 );
	}else{
		bbError( "Invalid Gadget type" );
	}
}

void		bbInsertGadgetItem( BBGadget *g,int index,BBString *text,int icon ){
	g->debug();
	if( BBComboBox *t=dynamic_cast<BBComboBox*>(g) ){
		t->insert( index,text,icon );
	}else if( BBListBox *t=dynamic_cast<BBListBox*>(g) ){
		t->insert( index,text,icon );
	}else if( BBTabber *t=dynamic_cast<BBTabber*>(g) ){
		t->insert( index,text,icon );
	}else{
		bbError( "Invalid Gadget type" );
	}
}

void		bbModifyGadgetItem( BBGadget *g,int index,BBString *text,int icon ){
	g->debug();
	if( BBComboBox *t=dynamic_cast<BBComboBox*>(g) ){
		t->modify( index,text,icon );
	}else if( BBListBox *t=dynamic_cast<BBListBox*>(g) ){
		t->modify( index,text,icon );
	}else if( BBTabber *t=dynamic_cast<BBTabber*>(g) ){
		t->modify( index,text,icon );
	}else{
		bbError( "Invalid Gadget type" );
	}
}

void		bbRemoveGadgetItem( BBGadget *g,int index ){
	g->debug();
	if( BBComboBox *t=dynamic_cast<BBComboBox*>(g) ){
		t->remove( index );
	}else if( BBListBox *t=dynamic_cast<BBListBox*>(g) ){
		t->remove( index );
	}else if( BBTabber *t=dynamic_cast<BBTabber*>(g) ){
		t->remove( index );
	}else{
		bbError( "Invalid Gadget type" );
	}
}

void		bbSelectGadgetItem( BBGadget *g,int index ){
	g->debug();
	if( BBComboBox *t=dynamic_cast<BBComboBox*>(g) ){
		t->select( index );
	}else if( BBListBox *t=dynamic_cast<BBListBox*>(g) ){
		t->select( index );
	}else if( BBTabber *t=dynamic_cast<BBTabber*>(g) ){
		t->select( index );
	}else{
		bbError( "Invalid Gadget type" );
	}
}

BBString*	bbGadgetItemText( BBGadget *g,int index ){
	g->debug();
	if( BBComboBox *t=dynamic_cast<BBComboBox*>(g) ){
		return t->items()->item(index);
	}else if( BBListBox *t=dynamic_cast<BBListBox*>(g) ){
		return t->items()->item(index);
	}else if( BBTabber *t=dynamic_cast<BBTabber*>(g) ){
		return t->items()->item(index);
	}
	bbError( "Invalid Gadget type" );
	return BBString::null();
}

int			bbCountGadgetItems( BBGadget *g ){
	g->debug();
	if( BBComboBox *t=dynamic_cast<BBComboBox*>(g) ){
		return t->items()->size();
	}else if( BBListBox *t=dynamic_cast<BBListBox*>(g) ){
		return t->items()->size();
	}else if( BBTabber *t=dynamic_cast<BBTabber*>(g) ){
		return t->items()->size();
	}
	bbError( "Invalid Gadget type" );
	return -1;
}

int			bbSelectedGadgetItem( BBGadget *g ){
	g->debug();
	if( BBComboBox *t=dynamic_cast<BBComboBox*>(g) ){
		return t->items()->selected();
	}else if( BBListBox *t=dynamic_cast<BBListBox*>(g) ){
		return t->items()->selected();
	}else if( BBTabber *t=dynamic_cast<BBTabber*>(g) ){
		return t->items()->selected();
	}
	bbError( "Invalid Gadget type" );
	return -1;
}
