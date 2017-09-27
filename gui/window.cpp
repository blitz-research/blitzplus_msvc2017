
#include "window.h"

static BBWindow *_active;

BBWindow::BBWindow( BBGroup *group,int style ):BBGroup(group,style),
_minimized(false),_maximized(false){
	_menu=new BBMenu( BBString::null(),-1,0 );
}

BBWindow::~BBWindow(){
	if( _active==this ) _active=0;
	_menu->release();
}

void BBWindow::performLayout(){
}

void BBWindow::updateMenu(){
}

void BBWindow::activateWindow(){
	_active=this;
}

void BBWindow::setMinSize( int w,int h ){
}

void BBWindow::setStatusText( BBString *text ){
}

void BBWindow::minimize(){
	_minimized=true;
	_maximized=false;
}

void BBWindow::maximize(){
	_maximized=true;
	_minimized=false;
}

void BBWindow::restore(){
	_minimized=_maximized=false;
}

BBWindow *bbActiveWindow(){
	return _active;
}

void		bbUpdateWindowMenu( BBWindow *window ){
	window->debug();
	window->updateMenu();
}

void		bbSetStatusText( BBWindow *window,BBString *text ){
	window->debug();
	window->setStatusText( text );
}

void		bbSetMinWindowSize( BBWindow *window,int w,int h ){
	window->debug();
	window->setMinSize( w>=0 ? w : window->width(),h>=0 ? h : window->height() );
}

void		bbActivateWindow( BBWindow *window ){
	window->debug();
	window->activateWindow();
}

void		bbMinimizeWindow( BBWindow *window ){
	window->debug();
	window->minimize();
}

void		bbMaximizeWindow( BBWindow *window ){
	window->debug();
	window->maximize();
}

void		bbRestoreWindow( BBWindow *window ){
	window->debug();
	window->restore();
}

int			bbWindowMinimized( BBWindow *window ){
	window->debug();
	return window->minimized();
}

int			bbWindowMaximized( BBWindow *window ){
	window->debug();
	return window->maximized();
}

BBMenu*		bbWindowMenu( BBWindow *window ){
	window->debug();
	return window->menu();
}
