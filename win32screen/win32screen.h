
#ifndef WIN32SCREEN_H
#define WIN32SCREEN_H

#include "../screen/screen.h"

#include "../win32hwnd/win32hwnd.h"

class Win32Screen : public BBScreen{
protected:
	Win32Hwnd _hwnd;
	bool _pointerVis;
	LRESULT wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc );

public:
	Win32Screen();

	void *query( int qid );

	BBGraphics *graphics();
	void flip( bool sync );

	void setTitle( BBString *title );
	void setPointer( int n );
	void moveMouse( int x,int y );
	int  mouseX();
	int  mouseY();
};

#endif