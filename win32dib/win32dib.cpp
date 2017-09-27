
#include "win32dib.h"

Win32DIB win32DIB;

Win32DIB::Win32DIB(){
	reg( "Win32DIB" );
}

bool Win32DIB::startup(){
	return true;
}

void Win32DIB::shutdown(){
}
