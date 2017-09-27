
#ifndef WIN32TEXTAREA_H
#define WIN32TEXTAREA_H

#include "../gui/textarea.h"

#include "win32gadget.h"

#include <richedit.h>

class Win32TextArea : public BBTextArea,public Win32WndProc{
	Win32Gadget _gadget;
	bool _prot;
	int _locked;
	CHARRANGE _lockedcr;

	void lock( int pos,int len );

	int adjustRange( int &pos,int &len,int units );

public:
	Win32TextArea( BBGroup *group,int style );

	void *query( int qid );

	void setFont( BBFont *font );
	void setText( BBString *text );
	void setShape( int x,int y,int w,int h );
	void setVisible( bool visible );
	void setEnabled( bool enabled );
	void activate();

	void	setTabs( int tabs );
	void	setTextColor( int r,int g,int b );
	void	setBackgroundColor( int r,int g,int b );

	int		findChar( int lin );
	int		findLine( int chr );

	int		length( int units );
	int		selLength( int units );
	int		cursor( int units );
	void	addText( BBString *text );
	BBString*getText( int pos,int len,int units );
	void	setText( BBString *text,int pos,int len,int units );
	void	formatText( int r,int g,int b,int flags,int pos,int len,int units );

	void	lock();
	void	unlock();
	void	lockAll();
	void	selectAll();

	LRESULT wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc );
};

#endif
