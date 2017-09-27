
#include "win32hwnd.h"
#include "../win32event/win32event.h"

#include <commctrl.h>
#include <zmouse.h>

#include <stdio.h>

#pragma warning(disable:4786)

#include <map>

using namespace std;

enum{
	MODF_SHIFT=1,
	MODF_CONTROL=2,
	MODF_MENU=4,
};

static int _mods;
static Win32Hwnd *_trackHwnd;
static HCURSOR _cursors[7];
static map<HWND,Win32Hwnd*> hwnd_map;


enum{
    NSUpArrowFunctionKey = 0xF700,
    NSDownArrowFunctionKey = 0xF701,
    NSLeftArrowFunctionKey = 0xF702,
    NSRightArrowFunctionKey = 0xF703,
    NSF1FunctionKey = 0xF704,
    NSF2FunctionKey = 0xF705,
    NSF3FunctionKey = 0xF706,
    NSF4FunctionKey = 0xF707,
    NSF5FunctionKey = 0xF708,
    NSF6FunctionKey = 0xF709,
    NSF7FunctionKey = 0xF70A,
    NSF8FunctionKey = 0xF70B,
    NSF9FunctionKey = 0xF70C,
    NSF10FunctionKey = 0xF70D,
    NSF11FunctionKey = 0xF70E,
    NSF12FunctionKey = 0xF70F,
    NSF13FunctionKey = 0xF710,
    NSF14FunctionKey = 0xF711,
    NSF15FunctionKey = 0xF712,
    NSF16FunctionKey = 0xF713,
    NSF17FunctionKey = 0xF714,
    NSF18FunctionKey = 0xF715,
    NSF19FunctionKey = 0xF716,
    NSF20FunctionKey = 0xF717,
    NSF21FunctionKey = 0xF718,
    NSF22FunctionKey = 0xF719,
    NSF23FunctionKey = 0xF71A,
    NSF24FunctionKey = 0xF71B,
    NSF25FunctionKey = 0xF71C,
    NSF26FunctionKey = 0xF71D,
    NSF27FunctionKey = 0xF71E,
    NSF28FunctionKey = 0xF71F,
    NSF29FunctionKey = 0xF720,
    NSF30FunctionKey = 0xF721,
    NSF31FunctionKey = 0xF722,
    NSF32FunctionKey = 0xF723,
    NSF33FunctionKey = 0xF724,
    NSF34FunctionKey = 0xF725,
    NSF35FunctionKey = 0xF726,
    NSInsertFunctionKey = 0xF727,
    NSDeleteFunctionKey = 0xF728,
    NSHomeFunctionKey = 0xF729,
    NSBeginFunctionKey = 0xF72A,
    NSEndFunctionKey = 0xF72B,
    NSPageUpFunctionKey = 0xF72C,
    NSPageDownFunctionKey = 0xF72D,
    NSPrintScreenFunctionKey = 0xF72E,
    NSScrollLockFunctionKey = 0xF72F,
    NSPauseFunctionKey = 0xF730,
    NSSysReqFunctionKey = 0xF731,
    NSBreakFunctionKey = 0xF732,
    NSResetFunctionKey = 0xF733,
    NSStopFunctionKey = 0xF734,
    NSMenuFunctionKey = 0xF735,
    NSUserFunctionKey = 0xF736,
    NSSystemFunctionKey = 0xF737,
    NSPrintFunctionKey = 0xF738,
    NSClearLineFunctionKey = 0xF739,
    NSClearDisplayFunctionKey = 0xF73A,
    NSInsertLineFunctionKey = 0xF73B,
    NSDeleteLineFunctionKey = 0xF73C,
    NSInsertCharFunctionKey = 0xF73D,
    NSDeleteCharFunctionKey = 0xF73E,
    NSPrevFunctionKey = 0xF73F,
    NSNextFunctionKey = 0xF740,
    NSSelectFunctionKey = 0xF741,
    NSExecuteFunctionKey = 0xF742,
    NSUndoFunctionKey = 0xF743,
    NSRedoFunctionKey = 0xF744,
    NSFindFunctionKey = 0xF745,
    NSHelpFunctionKey = 0xF746,
    NSModeSwitchFunctionKey = 0xF747
};

static int vkey_map[256];

static struct InitVkeys{

	InitVkeys(){
		vkey_map[VK_INSERT]=NSInsertFunctionKey;
		vkey_map[VK_DELETE]=NSDeleteFunctionKey;
		vkey_map[VK_HOME]=NSHomeFunctionKey;
		vkey_map[VK_END]=NSEndFunctionKey;
		vkey_map[VK_PRIOR]=NSPageUpFunctionKey;
		vkey_map[VK_NEXT]=NSPageDownFunctionKey;
		vkey_map[VK_UP]=NSUpArrowFunctionKey;
		vkey_map[VK_DOWN]=NSDownArrowFunctionKey;
		vkey_map[VK_LEFT]=NSLeftArrowFunctionKey;
		vkey_map[VK_RIGHT]=NSRightArrowFunctionKey;
		vkey_map[VK_F1]=NSF1FunctionKey;
		vkey_map[VK_F2]=NSF2FunctionKey;
		vkey_map[VK_F3]=NSF3FunctionKey;
		vkey_map[VK_F4]=NSF4FunctionKey;
		vkey_map[VK_F5]=NSF5FunctionKey;
		vkey_map[VK_F6]=NSF6FunctionKey;
		vkey_map[VK_F7]=NSF7FunctionKey;
		vkey_map[VK_F8]=NSF8FunctionKey;
		vkey_map[VK_F9]=NSF9FunctionKey;
		vkey_map[VK_F10]=NSF10FunctionKey;
		vkey_map[VK_F11]=NSF11FunctionKey;
		vkey_map[VK_F12]=NSF12FunctionKey;
		vkey_map[VK_F13]=NSF13FunctionKey;
		vkey_map[VK_F14]=NSF14FunctionKey;
		vkey_map[VK_F15]=NSF15FunctionKey;
		vkey_map[VK_F16]=NSF16FunctionKey;
		vkey_map[VK_F17]=NSF17FunctionKey;
		vkey_map[VK_F18]=NSF18FunctionKey;
		vkey_map[VK_F19]=NSF19FunctionKey;
		vkey_map[VK_F20]=NSF20FunctionKey;
		vkey_map[VK_F21]=NSF21FunctionKey;
		vkey_map[VK_F22]=NSF22FunctionKey;
		vkey_map[VK_F23]=NSF23FunctionKey;
		vkey_map[VK_F24]=NSF24FunctionKey;
	}
}_initVkeys;

Win32Hwnd::Win32Hwnd():_hwnd(0),_proc(0),_wndProc(0),
_eventmask(0),_eventsrc(0),_pointer(1),_mx(0),_my(0),_mw(0),_mh(0){
}

Win32Hwnd::~Win32Hwnd(){

	if( _trackHwnd==this ){
		_trackHwnd=0;
	}

	if( HWND hwnd=_hwnd ){
		setHwnd(0);
		DestroyWindow( hwnd );
	}
}

void *Win32Hwnd::query( int qid ){
	return qid==BBQID_WIN32HWND || qid==BBQID_WIN32CLIENTHWND ? (HWND)_hwnd : 0;
}

void Win32Hwnd::setHwnd( HWND hwnd ){
	if( _hwnd ){
		hwnd_map.erase( _hwnd );
		if( _proc!=DefWindowProc ) SetWindowLong( _hwnd,GWL_WNDPROC,(LONG)_proc );
	}
	if( _hwnd=hwnd ){
		_proc=(WNDPROC)SetWindowLong( _hwnd,GWL_WNDPROC,(LONG)classWndProc );
		if( _proc==classWndProc ) _proc=DefWindowProc;
		hwnd_map.insert( make_pair(_hwnd,this) );
	}
}

void Win32Hwnd::setWndProc( Win32WndProc *proc ){
	_wndProc=proc;
}

void Win32Hwnd::setEventMask( int mask,BBEventSource *src ){
	_eventmask=mask;
	_eventsrc=src;
}

void Win32Hwnd::setPointer( int n ){
	if( n<0 ) n=0;
	else if( n>6 ) n=6;
	_pointer=n;

	if( !_hwnd ) return;

	POINT p;GetCursorPos( &p );
	int ht=SendMessage( _hwnd,WM_NCHITTEST,0,(p.x<<16)|p.y );
	if( ht!=HTCLIENT ) return;

	SendMessage( _hwnd,WM_SETCURSOR,(WPARAM)_hwnd,(LPARAM)ht );
}

void Win32Hwnd::moveMouse( int x,int y ){
	if( _mw && _mh ){
		RECT r;
		GetClientRect( _hwnd,&r );
		x=x*r.right/_mw;
		y=y*r.bottom/_mh;
	}
	POINT p={x,y};
	ClientToScreen( _hwnd,&p );
	SetCursorPos( p.x,p.y );
}

void Win32Hwnd::setMouseArea( int w,int h ){
	_mw=w;_mh=h;
}

int  Win32Hwnd::mouseX(){
	return _mx;
}

int  Win32Hwnd::mouseY(){
	return _my;
}

bool Win32Hwnd::handleEvent( UINT msg,WPARAM wp,LPARAM lp ){

	int n,scan;

	switch( msg ){
	case WM_KEYDOWN:case WM_SYSKEYDOWN:
		if( lp&0x40000000 ) scan=0;
		else{
			scan=((lp>>17)&0x80)|((lp>>16)&0x7f);
			switch( wp ){
			case 0x10:_mods|=MODF_SHIFT;break;
			case 0x11:_mods|=MODF_CONTROL;break;
			case 0x12:_mods|=MODF_MENU;break;
			default:if( bbEmitHotKeyEvent( scan,_mods ) ) return true;
			}
		}
		break;
	case WM_KEYUP:case WM_SYSKEYUP:
		scan=((lp>>17)&0x80)|((lp>>16)&0x7f);
		switch( wp ){
		case 0x10:_mods&=~MODF_SHIFT;break;
		case 0x11:_mods&=~MODF_CONTROL;break;
		case 0x12:_mods&=~MODF_MENU;break;
		}
		break;
	}

	if( _eventmask & BBEvent::KEY_MASK ){
		switch( msg ){
		case WM_KEYDOWN:case WM_SYSKEYDOWN:
			if( scan ){
				_eventsrc->emit( BBEvent::KEY_DOWN,scan );
			}
			if( wp>0 && wp<256 && (n=vkey_map[wp]) ){
				_eventsrc->emit(BBEvent::KEY_CHAR,n);
			}
			return true;
		case WM_KEYUP:case WM_SYSKEYUP:
			if( scan ){
				_eventsrc->emit(BBEvent::KEY_UP,scan);
			}
			return true;
		case WM_CHAR:case WM_SYSCHAR:
			_eventsrc->emit(BBEvent::KEY_CHAR,wp);
			return true;
		case WM_LBUTTONDOWN:case WM_RBUTTONDOWN:case WM_MBUTTONDOWN:
			SetCapture( _hwnd );
			n=msg==WM_LBUTTONDOWN ? 1 : (msg==WM_RBUTTONDOWN ? 2 : 3);
			_eventsrc->emit(BBEvent::MOUSE_DOWN,n,_mx,_my,_mods);
			return true;
		case WM_LBUTTONUP:case WM_RBUTTONUP:case WM_MBUTTONUP:
			if( !(wp & (MK_LBUTTON|MK_RBUTTON|MK_MBUTTON)) ){
				ReleaseCapture();
			}
			n=msg==WM_LBUTTONUP ? 1 : (msg==WM_RBUTTONUP ? 2 : 3);
			_eventsrc->emit(BBEvent::MOUSE_UP,n,_mx,_my,_mods);
			return true;
		case WM_MOUSEWHEEL:
			_eventsrc->emit(BBEvent::MOUSE_WHEEL,(short)HIWORD(wp)/120 );
			return true;
		}
	}

	if( _eventmask & BBEvent::MOUSE_MASK ){
		switch( msg ){
		case WM_MOUSEMOVE:
			{
				int mx=(short)LOWORD(lp);
				int my=(short)HIWORD(lp);
				if( _mw && _mh ){
					RECT r;
					GetClientRect( _hwnd,&r );
					mx=mx*_mw/r.right;
					my=my*_mh/r.bottom;
				}
				bool change=mx!=_mx || my!=_my;
				_mx=mx;_my=my;
				if( _trackHwnd!=this ){
					if( _trackHwnd ) _trackHwnd->_eventsrc->emit( BBEvent::MOUSE_LEAVE );

					_trackHwnd=this;
					TRACKMOUSEEVENT tm={sizeof(tm),TME_LEAVE,_hwnd,0};
					_TrackMouseEvent( &tm );
					_eventsrc->emit( BBEvent::MOUSE_ENTER,0,_mx,_my );
				}
				if( change ) _eventsrc->emit( BBEvent::MOUSE_MOVE,0,_mx,_my );
			}
			return true;
		case WM_MOUSELEAVE:
			if( _trackHwnd ) _trackHwnd->_eventsrc->emit( BBEvent::MOUSE_LEAVE );
			_trackHwnd=0;
			return true;
		}
	}

	return false;
}

static void _emit( int kind,int data=0,int x=0,int y=0,int z=0,BBEventSource *src=0 ){
	BBEvent ev={kind,data,x,y,z,src};
	bbEmitEvent( &ev );
}

HRESULT _stdcall Win32Hwnd::classWndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp ){

	Win32Hwnd *dst=findHwnd( hwnd );
	if( !dst ) return DefWindowProc( hwnd,msg,wp,lp );

	Win32Hwnd *src=dst;

	switch( msg ){
	case WM_COMMAND:
		if( !lp ){
			int tag=LOWORD(wp);
			if( tag==1 ){
				_emit( BBEvent::KEY_OKAY );
			}else if( tag==2 ){
				_emit( BBEvent::KEY_CANCEL );
			}else if( tag>=100 ){
				_emit( BBEvent::MENU_ACTION,tag-100 );
			}
			return 0;
		}
		src=findHwnd( (HWND)lp );
		break;
	case WM_NOTIFY:
		src=lp ? findHwnd( ((NMHDR*)lp)->hwndFrom ) : 0;
		break;
	case WM_HSCROLL:case WM_VSCROLL:
		src=findHwnd( (HWND)lp );
		break;
	case WM_SETCURSOR:
		if( hwnd==(HWND)wp && LOWORD(lp)==HTCLIENT && dst->_pointer!=1 ){
			SetCursor( _cursors[dst->_pointer] );
			return 1;
		}
		break;
	case WM_ACTIVATEAPP:
		_emit( wp ? BBEvent::APP_RESUME : BBEvent::APP_SUSPEND );
		break;
	case WM_ENTERSIZEMOVE:case WM_ENTERMENULOOP:
		_emit( BBEvent::APP_BEGINMODAL );
		return 0;
	case WM_EXITSIZEMOVE:case WM_EXITMENULOOP:
		_emit( BBEvent::APP_ENDMODAL );
		return 0;
	default:
		dst->handleEvent( msg,wp,lp );
	}

	if( !src || !src->_wndProc ) return CallWindowProc( dst->_proc,hwnd,msg,wp,lp );

	return src->_wndProc->wndProc( hwnd,msg,wp,lp,dst->_proc );
}

const char *Win32Hwnd::className(){

	static WNDCLASS _wc;

	static const char *name="BLITZMAX_WINDOW_CLASS";

	if( !_wc.lpfnWndProc ){
		_wc.style=CS_OWNDC|CS_HREDRAW|CS_VREDRAW;
		_wc.lpfnWndProc=classWndProc;
		_wc.hInstance=GetModuleHandle(0);
		_wc.hIcon=LoadIcon( NULL,IDI_APPLICATION );
		_wc.hCursor=LoadCursor( 0,MAKEINTRESOURCE( IDC_ARROW ) );
		_wc.hbrBackground=(HBRUSH)(COLOR_BTNFACE+1);
		_wc.lpszMenuName=0;
		_wc.lpszClassName=name;
		RegisterClass(&_wc);
	}

	_cursors[0]=0;
	_cursors[1]=LoadCursor( 0,IDC_ARROW );
	_cursors[2]=LoadCursor( 0,IDC_WAIT );
	_cursors[3]=LoadCursor( 0,IDC_SIZEWE );
	_cursors[4]=LoadCursor( 0,IDC_SIZENS );
	_cursors[5]=LoadCursor( 0,IDC_SIZENWSE );
	_cursors[6]=LoadCursor( 0,IDC_CROSS );

	return name;
}

Win32Hwnd *Win32Hwnd::findHwnd( HWND hwnd ){
	map<HWND,Win32Hwnd*>::const_iterator it=hwnd_map.find( hwnd );
	return it==hwnd_map.end() ? 0 : it->second;
}

void bbWin32GetChildRect( HWND hwnd,RECT *rect ){

	GetWindowRect( hwnd,rect );

	HWND p=GetParent(hwnd);
	if( !p ) return;

	POINT pt={0,0};
	ClientToScreen( p,&pt );
	rect->left-=pt.x;
	rect->right-=pt.x;
	rect->top-=pt.y;
	rect->bottom-=pt.y;
}
