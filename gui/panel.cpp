
#include "panel.h"

BBPanel::BBPanel( BBGroup *group,int style ):BBGroup(group,style){
}

void BBPanel::setBackgroundImage( BBGraphics *g ){
}

void BBPanel::setBackgroundColor( int r,int g,int b ){
}

void	bbSetPanelImage( BBPanel *panel,BBString *image ){
	panel->debug();
	BBGraphics *g=bbLoadGraphics( image );
	if( !g ) return;
	panel->setBackgroundImage( g );
	g->release();
}

void	bbSetPanelColor( BBPanel *panel,int r,int g,int b ){
	panel->debug();
	panel->setBackgroundColor( r,g,b );
}

