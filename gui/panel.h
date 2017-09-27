
#ifndef PANEL_H
#define PANEL_H

#include "group.h"

#include "../graphics/graphics.h"

class BBPanel : public BBGroup{
public:
	BBPanel( BBGroup *group,int style );

	virtual void setBackgroundImage( BBGraphics *g );
	virtual void setBackgroundColor( int r,int g,int b );

	void debug(){ _debug(this,"Panel Gadget"); }
};

void		bbSetPanelImage( BBPanel *panel,BBString *image );
void		bbSetPanelColor( BBPanel *panel,int r,int g,int b );

#endif
