
window=CreateWindow( "Window",0,0,640,480 )

canvas=CreateCanvas( 0,0,ClientWidth(window),ClientHeight(window),window )

font=RequestFont()

If Not font RuntimeError "No font selected"

SetBuffer CanvasBuffer(canvas)

SetFont font

Text 0,0,"This is a FONT!"

FlipCanvas canvas

While WaitEvent()<>$803
Wend

End