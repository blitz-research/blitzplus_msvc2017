
#ifdef WIN32

#define WIN32_LEAN_AND_MEAN

#define DIRECTINPUT_VERSION 0x500

#include <math.h>
#include <dinput.h>
#include <mmsystem.h>

#include "joystick.h"

static DIOBJECTDATAFORMAT c_rgodfDIJoystick[44] = {
	{ &GUID_XAxis, 0, 0x80FFFF03, 256 },{ &GUID_YAxis, 4, 0x80FFFF03, 256 },
	{ &GUID_ZAxis, 8, 0x80FFFF03, 256 },{ &GUID_RxAxis, 12, 0x80FFFF03, 256 },
	{ &GUID_RyAxis, 16, 0x80FFFF03, 256 },{ &GUID_RzAxis, 20, 0x80FFFF03, 256 },
	{ &GUID_Slider, 24, 0x80FFFF03, 256 },{ &GUID_Slider, 28, 0x80FFFF03, 256 },
	{ &GUID_POV, 32, 0x80FFFF10, 0 },{ &GUID_POV, 36, 0x80FFFF10, 0 },
	{ &GUID_POV, 40, 0x80FFFF10, 0 },{ &GUID_POV, 44, 0x80FFFF10, 0 },
	{ NULL, 48, 0x80FFFF0C, 0 },{ NULL, 49, 0x80FFFF0C, 0 },
	{ NULL, 50, 0x80FFFF0C, 0 },{ NULL, 51, 0x80FFFF0C, 0 },
	{ NULL, 52, 0x80FFFF0C, 0 },{ NULL, 53, 0x80FFFF0C, 0 },
	{ NULL, 54, 0x80FFFF0C, 0 },{ NULL, 55, 0x80FFFF0C, 0 },
	{ NULL, 56, 0x80FFFF0C, 0 },{ NULL, 57, 0x80FFFF0C, 0 },
	{ NULL, 58, 0x80FFFF0C, 0 },{ NULL, 59, 0x80FFFF0C, 0 },
	{ NULL, 60, 0x80FFFF0C, 0 },{ NULL, 61, 0x80FFFF0C, 0 },
	{ NULL, 62, 0x80FFFF0C, 0 },{ NULL, 63, 0x80FFFF0C, 0 },
	{ NULL, 64, 0x80FFFF0C, 0 },{ NULL, 65, 0x80FFFF0C, 0 },
	{ NULL, 66, 0x80FFFF0C, 0 },{ NULL, 67, 0x80FFFF0C, 0 },
	{ NULL, 68, 0x80FFFF0C, 0 },{ NULL, 69, 0x80FFFF0C, 0 },
	{ NULL, 70, 0x80FFFF0C, 0 },{ NULL, 71, 0x80FFFF0C, 0 },
	{ NULL, 72, 0x80FFFF0C, 0 },{ NULL, 73, 0x80FFFF0C, 0 },
	{ NULL, 74, 0x80FFFF0C, 0 },{ NULL, 75, 0x80FFFF0C, 0 },
	{ NULL, 76, 0x80FFFF0C, 0 },{ NULL, 77, 0x80FFFF0C, 0 },
	{ NULL, 78, 0x80FFFF0C, 0 },{ NULL, 79, 0x80FFFF0C, 0 }
};
const DIDATAFORMAT c_dfDIJoystick = { 24, 16, 0x1, 80, 44, c_rgodfDIJoystick };

static const int MAX_JOYS=32;
typedef HRESULT(WINAPI*DICREATE)(HINSTANCE,DWORD,IDirectInput**,IUnknown*);

struct Joystate{
	IDirectInputDevice2 *dev;
	int type,mins[12],maxs[12];
	float x,y,z,u,v,yaw,pitch,roll,hat;
	int hit[32];
	char down[32];
};

static int _joys,_pollTime;
static Joystate _states[MAX_JOYS];

static IDirectInput2 *_directInput;

static void poll(){
	if( !_joys ) return;

	int tm=timeGetTime();
	if( tm-_pollTime<3 ) return;
	_pollTime=tm;

	for( int n=0;n<_joys;++n ){

		Joystate *st=_states+n;

		IDirectInputDevice2 *dev=st->dev;

		if( dev->Poll()<0 ){
			dev->Acquire();
			if( dev->Poll()<0 ){
				continue;
			}
		}

		DIJOYSTATE state;
		if( dev->GetDeviceState( sizeof(state),&state )<0 ) continue;

		st->x=(state.lX-st->mins[0])/(float)st->maxs[0]*2-1;
		st->y=(state.lY-st->mins[1])/(float)st->maxs[1]*2-1;
		st->z=(state.lZ-st->mins[2])/(float)st->maxs[2]*2-1;
		st->u=(state.rglSlider[0]-st->mins[6])/(float)st->maxs[6]*2-1;
		st->v=(state.rglSlider[1]-st->mins[7])/(float)st->maxs[7]*2-1;
		st->pitch=((state.lRx-st->mins[3])/(float)st->maxs[3]*2-1)*180;
		st->yaw=((state.lRy-st->mins[4])/(float)st->maxs[4]*2-1)*180;
		st->roll=((state.lRz-st->mins[5])/(float)st->maxs[5]*2-1)*180;
		if( (state.rgdwPOV[0]&0xffff)==0xffff ) st->hat=-1;
		else st->hat=(float)floor(state.rgdwPOV[0]/100.0f+.5f);

		for( int k=1;k<32;++k ){
			bool down=!!( state.rgbButtons[(k-1)&15] & 0x80 );
			if( down && !st->down[k] ) ++st->hit[k];
			st->down[k]=down;
		}
	}
}

static BOOL CALLBACK enumJoystick( LPCDIDEVICEINSTANCE devinst,LPVOID pvRef ){

	if( (devinst->dwDevType&0xff)!=DIDEVTYPE_JOYSTICK ) return DIENUM_CONTINUE;

	IDirectInputDevice *t_dev;
	if( _directInput->CreateDevice( devinst->guidInstance,&t_dev,0 )<0 ) return DIENUM_CONTINUE;

	IDirectInputDevice2 *dev;
	if( t_dev->QueryInterface( IID_IDirectInputDevice2,(void**)&dev )<0 ){
		t_dev->Release();return DIENUM_CONTINUE;
	}

	t_dev->Release();

	if( dev->SetCooperativeLevel( 0,DISCL_BACKGROUND|DISCL_NONEXCLUSIVE )>=0 ){
		if( dev->SetDataFormat( &c_dfDIJoystick )>=0 ){

			Joystate *st=_states+_joys;

			st->dev=dev;
			st->type=((devinst->dwDevType>>8)&0xff)==DIDEVTYPEJOYSTICK_GAMEPAD ? 1 : 2;

			for( int k=0;k<12;++k ){
				//initialize joystick axis ranges (d'oh!)
				DIPROPRANGE range;
				range.diph.dwSize=sizeof(DIPROPRANGE);
				range.diph.dwHeaderSize=sizeof(DIPROPHEADER);
				range.diph.dwObj=k*4+12;
				range.diph.dwHow=DIPH_BYOFFSET;
				if( dev->GetProperty( DIPROP_RANGE,&range.diph )<0 ){
					st->mins[k]=0;
					st->maxs[k]=65535;
					continue;
				}
				st->mins[k]=range.lMin;
				st->maxs[k]=range.lMax-range.lMin;
			}

			++_joys;

			return _joys<MAX_JOYS ? DIENUM_CONTINUE : DIENUM_STOP;
		}
	}
	dev->Release();
	return DIENUM_CONTINUE;
}

static int dir( float n ){
	return n<(-1.0f/3.0f) ? -1 : (n>(1.0f/3.0f) ? 1 : 0);
}

int		 bbJoyType( int port ){
	if( port<0 || port>=_joys ) return 0;
	poll();
	return _states[port].type;
}

int		 bbJoyDown( int n,int port ){
	if( port<0 || port>=_joys ) return 0;
	poll();
	return _states[port].down[n&31];
}

int		 bbJoyHit( int n,int port ){
	if( port<0 || port>=_joys ) return 0;
	poll();
	int t=_states[port].hit[n&31];
	_states[port].hit[n&31]=0;
	return t;
}

float	 bbJoyX( int port ){
	if( port<0 || port>=_joys ) return 0;
	poll();
	return _states[port].x;
}

float	 bbJoyY( int port ){
	if( port<0 || port>=_joys ) return 0;
	poll();
	return _states[port].y;
}

float	 bbJoyZ( int port ){
	if( port<0 || port>=_joys ) return 0;
	poll();
	return _states[port].z;
}

float	 bbJoyU( int port ){
	if( port<0 || port>=_joys ) return 0;
	poll();
	return _states[port].u;
}

float	 bbJoyV( int port ){
	if( port<0 || port>=_joys ) return 0;
	poll();
	return _states[port].v;
}

float	 bbJoyPitch( int port ){
	if( port<0 || port>=_joys ) return 0;
	poll();
	return _states[port].pitch;
}

float	 bbJoyYaw( int port ){
	if( port<0 || port>=_joys ) return 0;
	poll();
	return _states[port].yaw;
}

float	 bbJoyRoll( int port ){
	if( port<0 || port>=_joys ) return 0;
	poll();
	return _states[port].roll;
}

int		 bbJoyHat( int port ){
	if( port<0 || port>=_joys ) return 0;
	poll();
	return (int)_states[port].hat;
}

int		 bbJoyXDir( int port ){
	if( port<0 || port>=_joys ) return 0;
	poll();
	return dir( _states[port].x );
}

int		 bbJoyYDir( int port ){
	if( port<0 || port>=_joys ) return 0;
	poll();
	return dir( _states[port].y );
}

int		 bbJoyZDir( int port ){
	if( port<0 || port>=_joys ) return 0;
	poll();
	return dir( _states[port].z );
}

int		 bbJoyUDir( int port ){
	if( port<0 || port>=_joys ) return 0;
	poll();
	return dir( _states[port].u );
}

int		 bbJoyVDir( int port ){
	if( port<0 || port>=_joys ) return 0;
	poll();
	return dir( _states[port].v );
}

void	 bbFlushJoy(){
	if( !_joys ) return;

	poll();
	for( int k=0;k<_joys;++k ){
		Joystate *st=_states+k;
		memset( st->hit,0,sizeof(st->hit) );
		memset( st->down,0,sizeof(st->down) );
	}
}

class Win32Joystick : public BBModule{
	HMODULE _module;
public:
	Win32Joystick(){
		reg( "Win32Joystick" );
	}

	bool startup(){
		_joys=0;
		_module=0;
		_directInput=0;
		memset( &_states,0,sizeof(_states) );
		if( _module=LoadLibrary( "dinput.dll" ) ){
			if( DICREATE create=(DICREATE)GetProcAddress( _module,"DirectInputCreateA" ) ){
				IDirectInput *di;
				if( create( GetModuleHandle(0),DIRECTINPUT_VERSION,&di,0 )>=0 ){
					if( di->QueryInterface( IID_IDirectInput2,(void**)&_directInput )>=0 ){
						di->Release();
						_directInput->EnumDevices( DIDEVTYPE_JOYSTICK,enumJoystick,0,DIEDFL_ATTACHEDONLY );
						return true;
					}
					di->Release();
				}
			}
			FreeLibrary( _module );
		}
		return true;
	}

	void shutdown(){
		if( !_joys ) return;
		FreeLibrary( _module );
	}
}win32Joystick;

#endif