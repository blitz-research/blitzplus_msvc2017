
#ifndef WIN32GDI_H
#define WIN32GDI_H

#include "win32gdigraphics.h"

class Win32GDI : public BBModule{
	HPEN _nullPen;
	HBRUSH _nullBrush;
public:
	Win32GDI();

	bool startup();
	void shutdown();

	HPEN nullPen()const{ return _nullPen; }
	HBRUSH nullBrush()const{ return _nullBrush; }
};

extern Win32GDI win32GDI;
static Win32GDI *_win32GDI=&win32GDI;

#endif