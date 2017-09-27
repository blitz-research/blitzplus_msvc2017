; Create a window, and progress bar
AppTitle "Format brain"
win = CreateWindow("Formatting brain...",100,100,200,40,0,57)
pb = CreateProgBar(10,10,180,20,win)

; Start a timer to drive our progress bar
timer = CreateTimer(5)
progress# = 0

; A small event loop
Repeat

	; Wait for events
	id = WaitEvent()
	Select id
	
		; Exit on window close
		Case $803 : Exit 
		
		; Timer tick
		Case $4001
			; Update the progress bar
			progress# = progress# + 0.01
			If progress#>0.97 Then Notify "Formatting error encountered on block 'Frontal Lobe' of brain."+Chr$(13)+"Please insert a new brain.",True : Exit
			UpdateProgBar pb,progress#
			SetStatusText win,Int(progress#*100)+"% Complete"
			
	End Select
	
Forever

End ; bye!