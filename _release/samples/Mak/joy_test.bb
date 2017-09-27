
window=CreateWindow( "window",0,0,640,480 )

canvas=CreateCanvas( 0,0,ClientWidth(window),ClientHeight(window),window )
SetBuffer CanvasBuffer( canvas )

While WaitEvent(10)<>$803

	button$=""
	For k=1 To 16
		button$=button$+JoyDown(k,0)+" "
	Next
	
	Cls
	Text 0,0,  "JoyX:    "+JoyX(0)
	Text 0,12, "JoyY:    "+JoyY(0)
	Text 0,24, "JoyZ:    "+JoyZ(0)
	Text 0,36, "JoyU:    "+JoyU(0)
	Text 0,48, "JoyV:    "+JoyV(0)
	Text 0,60, "JoyXDir: "+JoyXDir(0)
	Text 0,72, "JoyYDir: "+JoyYDir(0)
	Text 0,84, "JoyZDir: "+JoyZDir(0)
	Text 0,96, "JoyUDir: "+JoyUDir(0)
	Text 0,108,"JoyVDir: "+JoyVDir(0)
	Text 0,120,"JoyPitch:"+JoyPitch(0)
	Text 0,132,"JoyYaw:  "+JoyYaw(0)
	Text 0,144,"JoyRoll: "+JoyRoll(0)
	Text 0,156,"JoyHat:  "+JoyHat(0)
	Text 0,168,"Buttons: "+button$
	
	FlipCanvas canvas

Wend

End