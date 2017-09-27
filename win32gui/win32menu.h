
#ifndef WIN32MENU_H
#define WIN32MENU_H
#define WIN32_LEAN_AND_MEAN

#include "../gui/menu.h"

#include <windows.h>

HMENU win32CreateMenu( const BBMenu *menu,bool popup );

#endif