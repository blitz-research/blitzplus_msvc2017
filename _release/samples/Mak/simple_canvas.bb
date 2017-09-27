
window=CreateWindow( "Graphics window",0,0,640,480 )

canvas=CreateCanvas( 0,0,ClientWidth(window),ClientHeight(window),window )

SetBuffer CanvasBuffer(canvas)

Repeat

	;flush events..
	While WaitEvent(0)
		If EventID()=$803 End
	Wend
	
	Cls
	
	For x=0 To GadgetWidth(canvas)-1 Step 32
		For y=0 To GadgetHeight(canvas)-1 Step 32
			Color Rand(255),Rand(255),Rand(255)
			Rect x,y,32,32
		Next
	Next
	
	FlipCanvas canvas
	
Forever