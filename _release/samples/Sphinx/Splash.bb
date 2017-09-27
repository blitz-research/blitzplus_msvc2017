;-------------------------------
;| A simple splash screen demo  |
;|                              |
;| Code : Maher 'Sphinx' Farag  |
;|                              |
;-------------------------------

; Get the desktop width and height
Width=GadgetWidth(Desktop())
Height=GadgetHeight(Desktop())

SetBuffer DesktopBuffer()
image=CreateImage( 395,89 )
;Grab the desktop from where the window will appear (middle)
GrabImage image,Width/2-200,Height/2-45

window=CreateWindow("Splash test",Width/2-200,Height/2-45,395,89,Desktop(),32)
canvas=CreateCanvas(0,0,395,89,window)
Progress=CreateProgBar(128,63,190,15,canvas)

;load the splash image and make White the masked color
Splash=LoadImage("Splash.png")
MaskImage Splash,255,255,255

SetBuffer CanvasBuffer(canvas)
DrawBlock image,0,0
DrawImage Splash,0,0

timer=CreateTimer( 8 )

While WaitEvent()<>$803

	If EventID()=$4001 ;Timer tick
		If KeyHit(1) Or Value#>1 Then End

		value#=value#+.01
		UpdateProgBar Progress,value#

		FlipCanvas canvas
	End If

Wend

End