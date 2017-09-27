window=CreateWindow( "My Window",0,0,640,480 )

hwnd=QueryObject( window,1 )	;returns windows HWND

Notify "Win32 HWND="+Hex$(hwnd)