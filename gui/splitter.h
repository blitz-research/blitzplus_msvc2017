
#ifndef SPLITTER_H
#define SPLITTER_H

#include "panel.h"

class BBSplitterPane;

class BBSplitter : public BBGroup{
	BBPanel *_panels[4];
	int _divx,_divy,_divw,_divh;

	void layoutChildren();

public:
	BBSplitter( BBGroup *group,int style );

	virtual void setDivShape( int x,int y,int w,int h )=0;

	BBPanel *panel( int n );

	void debug(){ _debug( this,"Splitter" ); }
};

BBPanel *bbSplitterPanel( BBSplitter *t,int n );

#endif