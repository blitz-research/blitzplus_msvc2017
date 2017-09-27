
window=CreateWindow( "Multi canvas demo",0,0,640,480 )

canvas1=CreateCanvas( 0,0,ClientWidth(window),ClientHeight(window)/2,window )
canvas2=CreateCanvas( 0,ClientHeight(window)/2,ClientWidth(window),ClientHeight(window)/2,window )

SetBuffer CanvasBuffer(canvas1)
ClsColor 0,0,255:Color 255,0,0

SetBuffer CanvasBuffer(canvas2)
ClsColor 255,0,0:Color 0,0,255

timer=CreateTimer(10)

While WaitEvent()<>$803

	If EventID()=$4001

		SetBuffer CanvasBuffer(canvas1)
	
		Cls
		For k=1 To 100
			Rect Rand(640),Rand(240),Rand(64),Rand(32)
		Next
		
		FlipCanvas canvas1
		
		SetBuffer CanvasBuffer(canvas2)
		
		Cls
		For k=1 To 100
			Rect Rand(640),Rand(240),Rand(64),Rand(32)
		Next
		
		FlipCanvas canvas2
		
	EndIf
	
Wend

End