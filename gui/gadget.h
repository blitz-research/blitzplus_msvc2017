
#ifndef GADGET_H
#define GADGET_H

#include "../app/app.h"
#include "../font/font.h"
#include "../event/event.h"
#include "../screen/screen.h"

#include "iconstrip.h"

class BBGroup;

class BBGadget : public BBEventSource{
	BBGroup *_group;
	BBScreen *_screen;
	int _style,_x,_y,_w,_h;
	bool _visible,_enabled;
	BBFont *_font;
	BBString *_text;
	BBIconStrip *_strip;
	int _ox,_oy,_ow,_oh,_gw,_gh;
	int _left,_top,_right,_bottom;

	friend class BBGadget;	//VC - d'oh!
protected:
	~BBGadget();
public:
	BBGadget( BBGroup *group,int style );

	enum{
		LAYOUT_NONE,LAYOUT_ABSOLUTE,LAYOUT_PROPORTIONAL
	};

	virtual void setFont( BBFont *font )=0;

	virtual void setText( BBString *text );	//simon was here - was pure virtual ???
	
	virtual void setShape( int x,int y,int w,int h )=0;
	virtual void setVisible( bool visible )=0;
	virtual void setEnabled( bool enabled )=0;
	virtual void activate()=0;

	virtual void setIconStrip( BBIconStrip *strip );

	void lockLayout();
	void setLayout( int left,int right,int top,int bottom );
	virtual void performLayout();

	int x()const{ return _x; }
	int y()const{ return _y; }
	int width()const{ return _w; }
	int height()const{ return _h; }
	int style()const{ return _style; }
	bool visible()const{ return _visible; }
	bool enabled()const{ return _enabled; }
	BBGroup *group()const{ return _group; }
	BBScreen *screen()const{ return _screen; }
	BBString *text()const{ return _text->copy(); }
	BBFont *font()const{ return _font; }
	BBIconStrip *iconStrip()const{ return _strip; }

	void debug(){ _debug(this,"Gadget"); }
};

void		bbFreeGadget( BBGadget *gad );

void		bbSetGadgetShape( BBGadget *gad,int x,int y,int w,int h );
void		bbSetGadgetFont( BBGadget *gad,BBFont *font );
void		bbSetGadgetText( BBGadget *gad,BBString *text );
void		bbSetGadgetLayout( BBGadget *gad,int left,int right,int top,int bottom );
void		bbShowGadget( BBGadget *gad );
void		bbHideGadget( BBGadget *gad );
void		bbEnableGadget( BBGadget *gad );
void		bbDisableGadget( BBGadget *gad );
void		bbActivateGadget( BBGadget *gad );
void		bbSetGadgetIconStrip( BBGadget *g,BBIconStrip *t );

int			bbGadgetX( BBGadget *gad );
int			bbGadgetY( BBGadget *gad );
int			bbGadgetWidth( BBGadget *gad );
int			bbGadgetHeight( BBGadget *gad );
BBFont*		bbGadgetFont( BBGadget *gad );
BBString*	bbGadgetText( BBGadget *gad );
BBGroup*	bbGadgetGroup( BBGadget *gad );

void		bbClearGadgetItems( BBGadget *gadget );
void		bbAddGadgetItem( BBGadget *gadget,BBString *text,int select,int icon );
void		bbInsertGadgetItem( BBGadget *gadget,int index,BBString *item,int icon );
void		bbModifyGadgetItem( BBGadget *gadget,int index,BBString *item,int icon );
void		bbRemoveGadgetItem( BBGadget *gadget,int index );
void		bbSelectGadgetItem( BBGadget *gadget,int index );
BBString*	bbGadgetItemText( BBGadget *gadget,int index );
int			bbCountGadgetItems( BBGadget *gadget );
int			bbSelectedGadgetItem( BBGadget *gadget );

#endif
