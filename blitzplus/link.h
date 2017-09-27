
#ifndef LINK_H
#define LINK_H

#include "../app/app.h"
#include "../debug/debug.h"
#include "../math/math.h"
#include "../string/string.h"
#include "../stream/stream.h"
#include "../filesys/filesys.h"
#include "../bank/bank.h"
#include "../time/time.h"
#include "../timer/timer.h"
#include "../process/process.h"
#include "../tcpip/tcpip.h"
#include "../joystick/joystick.h"
#include "../audio/audio.h"

#include "../blitz2d/blitz2d.h"

#include "../win32gui/win32gui.h"
#include "../win32font/win32font.h"
#include "../win32event/win32event.h"
#include "../win32timer/win32timer.h"

#include "../win32ddcanvas/win32ddcanvas.h"
#include "../win32ddscreen/win32ddscreen.h"
#include "../win32ddgrdriver2d/win32ddgrdriver2d.h"

#include "../win32glcanvas/win32glcanvas.h"
#include "../win32glscreen/win32glscreen.h"
#include "../win32glgrdriver2d/win32glgrdriver2d.h"

#include "../win32gdicanvas/win32gdicanvas.h"
#include "../win32gdiscreen/win32gdiscreen.h"
#include "../win32gdigrdriver2d/win32gdigrdriver2d.h"

#include "../win32dsmovie/win32dsmovie.h"

//#include "../win32dpnetgame/win32dpnetgame.h"

void bpLink( void (*sym)( const char *t_sym,void *pc ) );

#endif
