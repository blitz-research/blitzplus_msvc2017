
window=CreateWindow( "Size me to ZOOM!",ClientWidth(Desktop())/2-96,ClientHeight(Desktop())/2-96,192,192 )

width=ClientWidth(window)
height=ClientHeight(window)

canvas=CreateCanvas( 0,0,width,height,window )
SetGadgetLayout canvas,1,1,1,1
SetBuffer CanvasBuffer(canvas)

While WaitEvent(10)<>$803

	mx=MouseX()-width/2
	my=MouseY()-height/2
	
	Cls
	
	CopyRect mx,my,width,height,0,0,DesktopBuffer()
	
	FlipCanvas canvas
Wend

End