
AppTitle "Fullscreen app"

image=LoadImage( "blitzlogo.bmp" )
MidHandle image

w=640:h=480:d=16

Graphics w,h,d,1

Repeat

	While WaitEvent(0)
		If EventID()=$2001
;			While WaitEvent()<>$2002
;			Wend
		EndIf
	Wend
	
	If KeyHit(1)
		End
	EndIf

	If KeyHit(57)
		If w=640 w=1024:h=768 Else w=640:h=480
		Graphics w,h,d,1
	EndIf

	Cls
	
	For k=1 To 10
		DrawImage image,Rand(w),Rand(h)
	Next
	
	Color 0,0,0
	Rect 0,0,w,12
	Color 255,255,0
	Text 0,0,"Mode="+GraphicsWidth()+","+GraphicsHeight()+","+GraphicsDepth()+" - Space To swap modes, Esc To Exit"
	
	Flip
	
Forever

End