
w=GadgetWidth(Desktop())
h=GadgetHeight(Desktop())

Print "w="+w

Print "h="+h



;grab the desktop!
SetBuffer DesktopBuffer()
image=CreateImage( w,h )
GrabImage image,0,0

;create a little test image
small=CreateImage( 128,128 )
SetBuffer ImageBuffer(small)
For k=0 To 64 Step 4
Color Rand(255),Rand(255),Rand(255)
Oval k,k,128-k*2,128-k*2
Next
MidHandle small

;create a fullscreen canvas
Graphics w,h,16,1

;draw our grabbed desktop
While Not GetMouse()
	mx=MouseX():my=MouseY()
	TileBlock image,mx,my
	DrawImage small,mx,my
	Flip
Wend

End

