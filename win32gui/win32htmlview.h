
#ifndef WIN32HTMLVIEW_H
#define WIN32HTMLVIEW_H

#include "../gui/htmlview.h"

#include "win32gadget.h"

struct Win32HtmlViewRep;

class Win32HtmlView : public BBHtmlView,public Win32WndProc{
	Win32Gadget _gadget;
	Win32HtmlViewRep *_rep;
protected:
	~Win32HtmlView();
public:
	Win32HtmlView( BBGroup *group,int style );

	void *query( int qid );

	void setFont( BBFont *font );
	void setText( BBString *text );
	void setShape( int x,int y,int w,int h );
	void setVisible( bool visible );
	void setEnabled( bool enabled );
	void activate();

	void go( BBString *url );
	void run( BBString *url );
	void back();
	void forward();

	int getstatus();
	BBString *getcurrenturl();
	BBString *geteventurl();

	LRESULT wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc );
};

#endif