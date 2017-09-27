
#ifndef HTMLVIEW_H
#define HTMLVIEW_H

#include "gadget.h"

class BBHtmlView : public BBGadget{
public:
	enum viewstyles {NOCONTEXTMENU=1,NONAVIGATE=2};

	BBHtmlView( BBGroup *group,int style );

	virtual void go( BBString *url )=0;
	virtual void run( BBString *url )=0;
	virtual void back()=0;
	virtual void forward()=0;

	virtual int getstatus()=0;
	virtual BBString *getcurrenturl()=0;
	virtual BBString *geteventurl()=0;

	void debug(){ _debug(this,"HTMLView Gadget"); }
};

void	bbHtmlViewGo( BBHtmlView *t,BBString *url );
void	bbHtmlViewRun( BBHtmlView *t,BBString *url );
void	bbHtmlViewBack( BBHtmlView *t );
void	bbHtmlViewForward( BBHtmlView *t );

void bbHtmlViewForward( BBHtmlView *t );
int bbHtmlViewStatus( BBHtmlView *t );
BBString *bbHtmlViewCurrentURL( BBHtmlView *t );
BBString *bbHtmlViewEventURL( BBHtmlView *t );

#endif