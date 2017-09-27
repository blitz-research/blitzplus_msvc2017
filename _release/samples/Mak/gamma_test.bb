
Graphics 640,480,16,1

For k=1 To 1000
	Color Rand(255),Rand(255),Rand(255)
	Rect Rand(640)-32,Rand(480)-32,64,64
Next

Flip

i#=0
is#=.01

While Not KeyHit(1)

	VWait
	
	For j=0 To 255
		SetGamma j,j,j,j*i,j*i,j*i
	Next
	
	UpdateGamma False
	
	i=i+is
	If i<0 Or i>1
		is=-is
		i=i+is
	EndIf
	
Wend
	
End