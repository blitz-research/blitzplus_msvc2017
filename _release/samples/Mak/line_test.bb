
window=CreateWindow( "Window!",0,0,640,480 )

canvas=CreateCanvas( 0,0,ClientWidth(window),ClientHeight(window),window )

HidePointer canvas

SetBuffer CanvasBuffer(canvas)

Repeat

	While WaitEvent(0)
		If EventID()=$803 End
	Wend

	Cls
	
	For k=1 To 100
		Color Rand(255),Rand(255),Rand(255)
		Line Rand(GadgetWidth(canvas)),Rand(GadgetHeight(canvas)),Rand(GadgetWidth(canvas)),Rand(GadgetHeight(canvas))
	Next
	
	VWait
	FlipCanvas canvas
	
Forever

End