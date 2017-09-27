
#include "win32ddscreen.h"
#include "win32ddfullscreen.h"
#include "win32ddvirtualscreen.h"
#include "win32dddesktopscreen.h"

#include <stdio.h>

#include <vector>

Win32DDScreenDriver win32DDScreenDriver;

struct ScreenMode{
	int w,h,fmt;
};

static Win32DDDesktopScreen *_desktop;

static std::vector<ScreenMode> _modes;

static HRESULT WINAPI enumModes( DDSURFACEDESC *desc,void *ctxt ){

	int fmt=Win32DD::pixelFormat( &desc->ddpfPixelFormat,false );
	if( !fmt ) return DDENUMRET_OK;

	ScreenMode mode={
		desc->dwWidth,
		desc->dwHeight,
		fmt };

	_modes.push_back( mode );

	return DDENUMRET_OK;
}

Win32DDScreen::Win32DDScreen(){
	for( int k=0;k<256;++k ) setGamma( k,k,k,k,k,k );
}

void Win32DDScreen::setGamma( int r,int g,int b,int dr,int dg,int db ){
	_gammaRamp.red[r&255]=dr*257.0f;
	_gammaRamp.green[g&255]=dg*257.0f;
	_gammaRamp.blue[b&255]=db*257.0f;
}

void Win32DDScreen::getGamma( int r,int g,int b,int *dr,int *dg,int *db ){
	*dr=_gammaRamp.red[r&255]/257.0f;
	*dg=_gammaRamp.green[g&255]/257.0f;
	*db=_gammaRamp.blue[b&255]/257.0f;
}

void Win32DDScreen::updateGamma( bool calibrate ){
	IDirectDrawGammaControl *gamma;
	if( primarySurface()->QueryInterface( IID_IDirectDrawGammaControl,(void**)&gamma )>=0 ){
		gamma->SetGammaRamp( calibrate ? DDSGR_CALIBRATE : 0,&_gammaRamp );
		gamma->Release();
	}
}

Win32DDScreenDriver::Win32DDScreenDriver(){
	reg( "Win32DDScreenDriver","Screen","DirectDraw" );
}

bool Win32DDScreenDriver::startup(){

	startModule( "Win32DD" );

	_modes.clear();
	win32DD.directDraw()->EnumDisplayModes( 0,0,0,enumModes );

	_desktop=new Win32DDDesktopScreen();
	autoRelease(_desktop);

	win32DD.desktopGraphics();

	bbSetScreenDriver(this);
	return true;
}

void Win32DDScreenDriver::shutdown(){
}

int Win32DDScreenDriver::scanLine(){
	DWORD t;
	return win32DD.directDraw()->GetScanLine( &t )>=0 ? t : 0;
}

void Win32DDScreenDriver::vwait( int frames ){
	for( int k=0;k<frames;++k ){
		BOOL vb;
		while( win32DD.directDraw()->GetVerticalBlankStatus(&vb)>=0 && vb ){}
		win32DD.directDraw()->WaitForVerticalBlank( DDWAITVB_BLOCKBEGIN,0 );
	}
}

int Win32DDScreenDriver::availVidMem(){
	DDCAPS caps={sizeof(caps)};
	win32DD.directDraw()->GetCaps( &caps,0 );
	return caps.dwVidMemFree;
}

int Win32DDScreenDriver::totalVidMem(){
	DDCAPS caps={sizeof(caps)};
	win32DD.directDraw()->GetCaps( &caps,0 );
	return caps.dwVidMemTotal;
}

int Win32DDScreenDriver::screenModes(){
	return _modes.size();
}

void Win32DDScreenDriver::enumScreenMode( int n,int *w,int *h,int *fmt ){
	if( n<0 || n>=_modes.size() ){
		*w=*h=*fmt=0;
		return;
	}
	*w=_modes[n].w;
	*h=_modes[n].h;
	*fmt=_modes[n].fmt;
}

BBScreen *Win32DDScreenDriver::createScreen( int w,int h,int fmt,int flags ){

	BBScreen *screen=0;

	if( flags & BBScreen::SCREEN_VIRTUAL ){
		screen=new Win32DDVirtualScreen( w,h,fmt,flags );
	}else{
		screen=new Win32DDFullScreen( w,h,fmt,flags );
	}

	screen->activateScreen();

	autoRelease( screen );

	return screen;
}

BBScreen *Win32DDScreenDriver::desktopScreen(){
	return _desktop;
}
