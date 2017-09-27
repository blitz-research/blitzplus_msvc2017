window=CreateWindow( "Window",0,0,640,480 )

canvas=CreateCanvas( 0,0,ClientWidth(window),ClientHeight(window),window )

font=RequestFont()

If Not font RuntimeError "No font selected"

SetBuffer CanvasBuffer(canvas)

SetFont font

name$=FontName$(font)
size=FontSize(font)
style=FontStyle(font)

Text 0,0,"Font name: "+name
Text 0,size+4,"Font size: "+size
Text 0,(size+4)*2,"Font style: "+style

FlipCanvas canvas

While WaitEvent()<>$803
Wend

End