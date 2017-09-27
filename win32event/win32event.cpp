
#include "win32event.h"
#include "../app/app.h"
#include <stdlib.h>
#include <wownt32.h>

#include <stdio.h>
#include <vector>

Win32EventDriver win32EventDriver;
Win32CatchFilter win32CatchFilter;

static int _cur_thread;
static void *_thread_esps[2];

static _declspec(naked) void _swap_thread(){
	_asm{
		pushad

		mov		eax,[_cur_thread]
		mov		[_thread_esps+eax*4],esp

		xor		eax,1

		mov		[_cur_thread],eax
		mov		esp,[_thread_esps+eax*4]

		popad
		ret
	}
}

static void _init_thread( void *pc ){

	void **stp;

	_asm{
		mov		ecx,0x10000	;skip 256K stack
zloop:	push	eax
		dec		ecx
		jnz		zloop
		mov		eax,esp
		add		esp,0x40000
		mov		[stp],eax
	}

	*--stp=pc;
	for( int k=0;k<8;++k ) *--stp=0;

	_thread_esps[1]=stp;
}

//************************* Catch filter ******************************

static bool _resume;
static int  _sent,_timeout;

Win32CatchFilter::Win32CatchFilter(){
	reg( "Win32CatchFilter" );
}

bool Win32CatchFilter::startup(){
	insert(10);
	return true;
}

void Win32CatchFilter::shutdown(){
	remove();
}

void Win32CatchFilter::filter( const BBEvent *ev ){

	if( !_cur_thread ) return;

	if( ev->kind==BBEvent::APP_RESUME ){
		_resume=true;
		return;
	}

	emit(ev);
	++_sent;
	_swap_thread();
}

//************************** Event Driver ******************************

static void _dispatch_thread(){

	BBEvent debug_ev={0};
	BBEvent timer_ev={BBEvent::TIMER_TICK};
	BBEvent resume_ev={BBEvent::APP_RESUME};
	BBEvent suspend_ev={BBEvent::APP_SUSPEND};

	for(;;){

		if( !_cur_thread ){
//			ExitProcess( 0 );
			bbError( "Wrong thread!" );
		}

		if( int tm=_timeout ){
			if( tm<0 ) tm=INFINITE;
			MsgWaitForMultipleObjects( 0,0,false,tm,QS_ALLEVENTS );
		}

		_sent=0;

		MSG msg;
		while( PeekMessage( &msg,0,0,0,PM_REMOVE ) ){
			if( GetWindowLong( msg.hwnd,GWL_STYLE ) & WS_TABSTOP ){
				if( IsDialogMessage( GetParent(msg.hwnd),&msg ) ){
					continue;
				}
			}
			switch( msg.message ){
			case WM_BBSTOP:
				debug_ev.kind=BBEvent::DEBUG_STOP;
				bbPostEvent(&debug_ev);
				continue;
			case WM_BBRUN:
				debug_ev.kind=BBEvent::DEBUG_RUN;
				bbPostEvent(&debug_ev);
				continue;
			case WM_BBEND: 
				debug_ev.kind=BBEvent::DEBUG_END;
				bbPostEvent(&debug_ev);
				continue;
			case WM_BBTIMER:
				timer_ev.data=msg.wParam;
				timer_ev.source=(void*)msg.lParam;
				bbPostEvent(&timer_ev);
				continue;
			}

			TranslateMessage( &msg );
			DispatchMessage( &msg );
		}

		if( _resume ){
			_resume=false;
			bbPostEvent(&resume_ev);
			++_sent;
			_swap_thread();
		}

		if( !_sent ){
			_swap_thread();
		}
	}
}

Win32EventDriver::Win32EventDriver(){
	reg( "Win32EventDriver" );
}

bool Win32EventDriver::startup(){
	_init_thread( _dispatch_thread );
	bbSetEventDriver(this);
	return true;
}

void Win32EventDriver::shutdown(){
}

void Win32EventDriver::wait( int timeout ){

	if( _cur_thread ) return;

	_timeout=timeout;

	_swap_thread();
}
