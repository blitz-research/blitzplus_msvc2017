
#include "gui.h"

BBSplitter::BBSplitter( BBGroup *group,int style ):BBGroup( group,style ),
_divx(0),_divy(0),_divw(0),_divh(0){
	_panels[0]=_panels[1]=0;
}

void BBSplitter::layoutChildren(){

	if( (style()&3)==1 ){
		panel(0)->setShape( 0,0,_divx,clientHeight() );
		panel(1)->setShape( _divx+_divw,0,clientWidth()-_divx-_divw,clientHeight() );
	}else if( (style()&3)==2 ){
		panel(0)->setShape( 0,0,clientWidth(),_divy );
		panel(1)->setShape( 0,_divy+_divh,clientWidth(),clientHeight()-_divy-_divh );
	}else if( (style()&3)==3 ){
		panel(0)->setShape( 0,0,_divx,_divy );
		panel(1)->setShape( _divx+_divw,0,clientWidth()-_divx-_divw,_divy );
		panel(2)->setShape( 0,_divy+_divh,_divx,clientHeight()-_divy-_divh );
		panel(3)->setShape( _divx+_divw,_divy+_divh,clientWidth()-_divx-_divw,clientHeight()-_divy-_divh );
	}
}

void BBSplitter::setDivShape( int x,int y,int w,int h ){
	_divx=x;_divy=y;_divw=w;_divh=h;
	layoutChildren();
}

BBPanel *BBSplitter::panel( int n ){
	if( !_panels[n] ){
		_panels[n]=bbGuiDriver()->createPanel(this,0);
		_panels[n]->setVisible(true);
	}
	return _panels[n];
}

BBPanel *bbSplitterPanel( BBSplitter *t,int n ){
	t->debug();
	return t->panel(n);
}