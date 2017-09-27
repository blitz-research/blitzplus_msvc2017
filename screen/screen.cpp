
#include "screen.h"

BBScreen *_active;
BBScreenDriver *_driver;

BBScreen::BBScreen():_pmx(0),_pmy(0){
}

BBScreen::~BBScreen(){
	if( _active==this ) _active=0;
}

void BBScreen::setGamma( int r,int g,int b,int dr,int dg,int db ){
}

void BBScreen::getGamma( int r,int g,int b,int *dr,int *dg,int *db ){
	*dr=r;
	*dg=g;
	*db=b;
}

void BBScreen::updateGamma( bool calibrate ){
}

void BBScreen::activateScreen(){
	_active=this;
}

void BBScreen::setTitle( BBString *title ){
}

int BBScreenDriver::scanLine(){
	return 0;
}

int BBScreenDriver::availVidMem(){
	return 0;
}

int BBScreenDriver::totalVidMem(){
	return 0;
}

void BBScreenDriver::vwait( int frames ){
}

void bbSetScreenDriver( BBScreenDriver *f ){
	_driver=f;
	_active=_driver->desktopScreen();
}

int			bbScanLine(){
	return _driver->scanLine();
}

void		bbVWait( int count ){
	_driver->vwait( count );
}

int			bbAvailVidMem(){
	return _driver->availVidMem();
}

int			bbTotalVidMem(){
	return _driver->totalVidMem();
}

BBScreen*	bbActiveScreen(){
	return _active;
}

BBScreen*	bbDesktopScreen(){
	return _driver->desktopScreen();
}

int bbScreenModes(){
	return _driver->screenModes();
}

void bbEnumScreenMode( int n,int *w,int *h,int *fmt ){
	_driver->enumScreenMode( n,w,h,fmt );
}

BBScreen *bbCreateScreen( BBString *title,int w,int h,int fmt,int flags ){
	BBScreen *screen=_driver->createScreen( w,h,fmt,flags );
	if( screen ) screen->setTitle( title );
	return screen;
}

void bbFreeScreen( BBScreen *screen ){
	if( !screen ) return;
	screen->debug();
	screen->release();
}

void		bbActivateScreen( BBScreen *screen ){
	if( !screen ){ _active=0;return; }
	screen->debug();
	screen->activateScreen();
}

void		bbSetScreenTitle( BBScreen *screen,BBString *title ){
	screen->debug();
	screen->setTitle( title );
}

void		bbFlipScreen( BBScreen *screen,bool sync ){
	screen->debug();
	screen->flip( sync );
}

BBGraphics*	bbScreenGraphics( BBScreen *screen ){
	screen->debug();
	return screen->graphics();
}
