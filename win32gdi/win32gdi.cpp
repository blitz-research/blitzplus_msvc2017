
#include "win32gdi.h"

Win32GDI win32GDI;

Win32GDI::Win32GDI(){
	reg( "Win32GDI" );
}

bool Win32GDI::startup(){
	_nullPen=CreatePen( PS_NULL,0,0 );
	_nullBrush=(HBRUSH)GetStockObject( NULL_BRUSH );
	return true;
}

void Win32GDI::shutdown(){
}
