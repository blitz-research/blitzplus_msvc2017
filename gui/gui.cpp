
#include "gui.h"

static unsigned _reqrgb=~0;
static BBGuiDriver *_driver;

void bbSetGuiDriver( BBGuiDriver *p ){
	_driver=p;
}

BBGuiDriver *bbGuiDriver(){
	return _driver;
}

BBLabel* bbCreateLabel( BBString *text,int x,int y,int w,int h,BBGroup *group,int style ){
	group->debug();
	BBLabel *gadget=_driver->createLabel( group,style );
	gadget->setShape(x,y,w,h);
	gadget->setText( text );
	gadget->setVisible(true);
	gadget->lockLayout();
	return gadget;
}

BBButton* bbCreateButton( BBString *text,int x,int y,int w,int h,BBGroup *group,int style ){
	group->debug();
	BBButton *gadget=_driver->createButton( group,style );
	gadget->setShape(x,y,w,h);
	gadget->setText( text );
	gadget->setVisible(true);
	gadget->lockLayout();
	return gadget;
}

BBProgBar* bbCreateProgBar( int x,int y,int w,int h,BBGroup *group,int style ){
	group->debug();
	BBProgBar *gadget=_driver->createProgBar( group,style );
	gadget->setShape(x,y,w,h);
	gadget->setVisible(true);
	gadget->lockLayout();
	return gadget;
}

BBTextField* bbCreateTextField( int x,int y,int w,int h,BBGroup *group,int style ){
	group->debug();
	BBTextField *gadget=_driver->createTextField( group,style );
	gadget->setShape(x,y,w,h);
	gadget->setVisible(true);
	gadget->lockLayout();
	return gadget;
}

BBSlider* bbCreateSlider( int x,int y,int w,int h,BBGroup *group,int style ){
	group->debug();
	BBSlider *gadget=_driver->createSlider( group,style );
	gadget->setShape(x,y,w,h);
	gadget->setVisible(true);
	gadget->lockLayout();
	return gadget;
}

BBComboBox* bbCreateComboBox( int x,int y,int w,int h,BBGroup *group,int style ){
	group->debug();
	BBComboBox *gadget=_driver->createComboBox( group,style );
	gadget->setShape(x,y,w,h);
	gadget->setVisible(true);
	gadget->lockLayout();
	return gadget;
}

BBListBox* bbCreateListBox( int x,int y,int w,int h,BBGroup *group,int style ){
	group->debug();
	BBListBox *gadget=_driver->createListBox( group,style );
	gadget->setShape(x,y,w,h);
	gadget->setVisible(true);
	gadget->lockLayout();
	return gadget;
}

BBTreeView* bbCreateTreeView( int x,int y,int w,int h,BBGroup *group,int style ){
	group->debug();
	BBTreeView *gadget=_driver->createTreeView( group,style );
	gadget->setShape(x,y,w,h);
	gadget->setVisible(true);
	gadget->lockLayout();
	return gadget;
}

BBTextArea* bbCreateTextArea( int x,int y,int w,int h,BBGroup *group,int style ){
	group->debug();
	BBTextArea *gadget=_driver->createTextArea( group,style );
	gadget->setShape(x,y,w,h);
	gadget->setVisible(true);
	gadget->lockLayout();
	return gadget;
}

BBToolBar* bbCreateToolBar( BBString *icons,int x,int y,int w,int h,BBGroup *group,int style ){
	group->debug();
	BBToolBar *gadget=_driver->createToolBar( group,style );

	gadget->setShape(x,y,w,h);
	gadget->setVisible(true);
	gadget->lockLayout();

	BBIconStrip *t=bbLoadIconStrip(icons);
	gadget->setIconStrip(t);
	t->release();

	return gadget;
}

BBHtmlView* bbCreateHtmlView( int x,int y,int w,int h,BBGroup *group,int style ){
	group->debug();
	BBHtmlView *gadget=_driver->createHtmlView( group,style );
	gadget->setShape(x,y,w,h);
	gadget->setVisible(true);
	gadget->lockLayout();
	return gadget;
}

BBWindow* bbCreateWindow( BBString *text,int x,int y,int w,int h,BBGroup *group,int style ){
	if( group ) group->debug();
	BBWindow *gadget=_driver->createWindow( group,style );
	gadget->setShape(x,y,w,h);
	gadget->setText( text );
	gadget->setVisible(true);
	gadget->lockLayout();
	return gadget;
}

BBTabber* bbCreateTabber( int x,int y,int w,int h,BBGroup *group,int style ){
	if( group ) group->debug();
	BBTabber *gadget=_driver->createTabber( group,style );
	gadget->setShape(x,y,w,h);
	gadget->setVisible(true);
	gadget->lockLayout();
	return gadget;
}

BBPanel* bbCreatePanel( int x,int y,int w,int h,BBGroup *group,int style ){
	if( group ) group->debug();
	BBPanel *gadget=_driver->createPanel( group,style );
	gadget->setShape(x,y,w,h);
	gadget->setVisible(true);
	gadget->lockLayout();
	return gadget;
}

BBSplitter* bbCreateSplitter( int x,int y,int w,int h,BBGroup *group,int style ){
	if( group ) group->debug();
	BBSplitter *gadget=_driver->createSplitter( group,style );
	gadget->setShape(x,y,w,h);
	gadget->setDivShape( x/2-4,y/2-4,8,8 );
	gadget->setVisible(true);
	gadget->lockLayout();
	return gadget;
}

BBDesktop *bbDesktop(){
	return _driver->desktop();
}

BBIconStrip*bbLoadIconStrip( BBString *f ){
	BBGraphics *g=bbLoadGraphics( f );
	if( !g ) return 0;
	BBIconStrip *t=_driver->createIconStrip( g );
	g->release();
	return t;
}

void		bbNotify( BBString *msg,bool serious ){
	_driver->notify( msg,serious );
}

int			bbConfirm( BBString *msg,bool serious ){
	return _driver->confirm( msg,serious );
}

int			bbProceed( BBString *msg,bool serious ){
	return _driver->proceed( msg,serious );
}

BBFont*		bbRequestFont(){
	return _driver->requestFont();
}

int			bbRequestColor( int r,int g,int b ){
	unsigned rgb=(r<<16)|(g<<8)|b;
	if( !_driver->requestColor( &rgb ) ) return false;
	_reqrgb=rgb;
	return true;
}

int			bbRequestedRed(){
	return (_reqrgb>>16)&0xff;
}

int			bbRequestedGreen(){
	return (_reqrgb>>8)&0xff;
}

int			bbRequestedBlue(){
	return (_reqrgb>>0)&0xff;
}

BBString*	bbRequestDir( BBString *prompt , BBString *path ){
	BBString *t=_driver->requestDir( prompt,path );
	return t ? t : BBString::null();
}

BBString*	bbRequestFile( BBString *prompt,BBString *exts,bool save,BBString *defname ){
	BBString *t=_driver->requestFile( prompt,exts,save,defname );
	return t ? t : BBString::null();
}

