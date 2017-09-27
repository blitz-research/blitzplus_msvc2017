
window=CreateWindow( "My Window",0,0,640,480 )

panel=CreatePanel( 0,0,ClientWidth(window),ClientHeight(window),window )
SetPanelImage panel,"blitzlogo.bmp"

SetGadgetLayout panel,1,1,1,1

While WaitEvent()<>$803
Wend

End