
AppTitle "My app!"

Graphics 320,240,0,3

While Not KeyHit(1)

	If WaitEvent(0)=$803 End

	Cls
	For k=1 To 1000
		Color Rand(255),Rand(255),Rand(255)
		Rect Rand(320),Rand(240)+12,64,64
	Next
	
	Color 255,255,255
	Text 0,0,MouseX()+","+MouseY()
	
	Flip
	
Wend

End