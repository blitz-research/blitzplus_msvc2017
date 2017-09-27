
window=CreateWindow( "Window",32,64,640,480,0,32+7 )

canvas=CreateCanvas( 0,0,ClientWidth(window),ClientHeight(window),window )

SetGadgetLayout canvas,1,1,1,1

SetBuffer CanvasBuffer(canvas)

While WaitEvent()<>$803

	Cls
	
	xs=MouseXSpeed( canvas )
	ys=MouseYSpeed( canvas )
	zs=MouseZSpeed()
	
	If EventID()=$101 MoveMouse 320,240,canvas
	
	Text 0,0,MouseX(canvas)+","+MouseY(canvas)+","+MouseZ()
	Text 0,FontHeight(),xs+","+ys+","+zs
	
	FlipCanvas canvas

Wend

End