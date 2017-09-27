
#ifndef WIN32GADGET_H
#define WIN32GADGET_H

#include "../gui/group.h"
#include "../gui/gadget.h"
#include "../win32hwnd/win32hwnd.h"
#include "../win32font/win32font.h"

class Win32Gadget : public Win32Hwnd{
public:
	void	setFont( BBFont *font );
	void	setText( BBString *text );
	void	setShape( int x,int y,int w,int h );
	void	setVisible( bool visible );
	void	setEnabled( bool enabled );
	void	activate();
	void	clientShape( int *x,int *y,int *w,int *h );
};

#endif