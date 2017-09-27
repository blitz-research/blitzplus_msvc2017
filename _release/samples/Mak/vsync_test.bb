
Graphics 640,480,16,1

Repeat

	While WaitEvent(0)
		If EventID()=$201 RuntimeError "Byeee!"
	Wend

	Cls
	Rect x,0,32,32
	x=x+1
	
	Text 0,0,x
	
	Flip False

Forever

End