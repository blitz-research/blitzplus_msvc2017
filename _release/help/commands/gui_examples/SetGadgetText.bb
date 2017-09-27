; Create a window and some labels
win=CreateWindow("SetGadgetText Example",100,100,200,60,0,17)
timer=CreateTimer(10)
label=CreateLabel("Starting Up...",10,10,180,20,win)

; A basic event loop
Repeat
	id = WaitEvent()
	Select id
		Case $803
			 Exit
		Case $4001 
			SetGadgetText label,"Running for : " + TimerTicks(timer)/10.0 + " seconds"
	End Select
Forever
End ; bye!