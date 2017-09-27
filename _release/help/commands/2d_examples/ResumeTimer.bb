; create a window
WinHandle=CreateWindow("Timer Functions",100,100,200,150,0,25) 
; and create a couple of buttons
buttonReset=CreateButton("Reset Timer",10,10,180,20,WinHandle)
buttonPause=CreateButton("Pause Timer",10,40,180,20,WinHandle)

; and create our timer (one hundred ticks per second)
timer = CreateTimer(100)

; and now a loop to demonstrate it in action
Repeat 
	event=WaitEvent() 
	
	; exit when we receive a Window Close event
	If event=$803 Then End 
	
	; reset / pause / resume the timer if the user hits the buttons
	If event=$401 Then
		If EventSource()=buttonReset Then ResetTimer timer 
		If EventSource()=buttonPause Then
			If GadgetText(buttonPause)="Pause Timer" Then
				PauseTimer timer
				SetGadgetText buttonPause,"Resume Timer"
			Else
				ResumeTimer timer
				SetGadgetText buttonPause,"Pause Timer"
			End If
		End If
	End If
	
	; update the screen on timer tick
	If event=$4001 Then
		time# = TimerTicks(timer)/100.0
		SetStatusText WinHandle,"Ticks : " + TimerTicks(timer) + ", Time : " + time + " seconds"
	End If 
Forever

End ; bye!