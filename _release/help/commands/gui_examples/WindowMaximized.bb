; Create a window
win=CreateWindow("Test Min / Max Functions",100,100,100,100,0,3)
; create a couple of buttons
buttonMin=CreateButton("Minimise",10,10,80,20,win)
buttonMax=CreateButton("Maximise",10,40,80,20,win)

; now a very small event loop
Repeat
	id = WaitEvent()
	Select id
	
		Case $803 : Exit ; exit on window close
		
		Case $401 ; Attempt to minimise or maximise on button hit
			If EventSource()=buttonMin Then
				If WindowMinimized(win) Then
					Notify "But the window is already minimised!"
				Else
					MinimizeWindow win
				End If
			Else If EventSource()=buttonMax Then
				If WindowMaximized(win) Then
					Notify "But the window is already maximised!"
				Else
					MaximizeWindow win
				End If
			End If
	End Select	
Forever

End ; bye!