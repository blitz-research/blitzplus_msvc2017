
#ifndef WINDOW_H
#define WINDOW_H

#include "group.h"
#include "menu.h"

class BBWindow : public BBGroup{
	BBMenu *_menu;
	bool _minimized,_maximized;

	virtual void performLayout();
protected:
	~BBWindow();
public:
	BBWindow( BBGroup *group,int style );

	virtual void updateMenu()=0;
	virtual void activateWindow()=0;
	virtual void setMinSize( int w,int h )=0;
	virtual void setStatusText( BBString *text )=0;
	virtual void minimize()=0;
	virtual void maximize()=0;
	virtual void restore()=0;

	BBMenu *menu()const{ return _menu; }
	bool minimized()const{ return _minimized; }
	bool maximized()const{ return _maximized; }

	void debug(){ _debug(this,"Window Gadget"); }
};

BBWindow*	bbActiveWindow();

void		bbUpdateWindowMenu( BBWindow *window );
void		bbActivateWindow( BBWindow *window );
void		bbSetStatusText( BBWindow *window,BBString *text );
void		bbSetMinWindowSize( BBWindow *window,int w,int h );
void		bbMinimizeWindow( BBWindow *window );
void		bbMaximizeWindow( BBWindow *window );
void		bbRestoreWindow( BBWindow *window );

int			bbWindowMinimized( BBWindow *window );
int			bbWindowMaximized( BBWindow *window );
BBMenu*		bbWindowMenu( BBWindow *window );

#endif
