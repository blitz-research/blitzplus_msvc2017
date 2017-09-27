
/*

  Simple HWND wrapper.

  Achieves 2 main things:

  * identifies real msg source.

  * subclasses window - you receive all events!

*/

#ifndef WIN32HWND_H
#define WIN32HWND_H
#define WIN32_LEAN_AND_MEAN

#include "../event/event.h"

#include <windows.h>

class Win32WndProc{
public:
	virtual LRESULT wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc )=0;
};

class Win32Hwnd{
	HWND _hwnd;				//HWND
	WNDPROC _proc;			//sub classed proc
	Win32WndProc *_wndProc;
	int _eventmask;
	BBEventSource *_eventsrc;
	int _pointer,_mx,_my,_mw,_mh;

	bool handleEvent( UINT msg,WPARAM wp,LPARAM lp );

	static LRESULT _stdcall classWndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp );

public:
	Win32Hwnd();
	virtual ~Win32Hwnd();

	void *query( int qid );

	void setHwnd( HWND hwnd );
	void setWndProc( Win32WndProc *proc );
	void setEventMask( int mask,BBEventSource *src );

	void setPointer( int n );			//set pointer
	void moveMouse( int x,int y );		//to move mouse
	void setMouseArea( int w,int h );	//to scale mouse
	int  mouseX();						//updated by MOUSE_MOVE msgs
	int  mouseY();

	HWND hwnd()const{ return _hwnd; }

	static const char *className();

	static Win32Hwnd *findHwnd( HWND hwnd );
};

void bbWin32GetChildRect( HWND hwnd,RECT *rect );

#endif