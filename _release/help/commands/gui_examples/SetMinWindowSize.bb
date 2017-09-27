; Create a window
win=CreateWindow("Test SetMinWindowSize",100,100,200,90,0,19)

; Set the minimum window size
SetMinWindowSize win,200,90

; now a very small event loop
Repeat
	If WaitEvent() = $803 Then Exit ; exit on window close
Forever

End ; bye!