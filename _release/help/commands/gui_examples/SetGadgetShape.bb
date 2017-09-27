; Create a window, and button
win = CreateWindow("Play The Click Me Game",100,100,200,200,0,57)
button = CreateButton("Click Me!",50,50,100,100,win)

; Set everything up to start our little game
SetStatusText win,"Click the button to start the game!"
clicks = 0

; A small event loop
Repeat

	; Wait for events
	id = WaitEvent()
	Select id
	
		; Exit on window close
		Case $803 : Exit 
		
		; Button clicked
		Case $401
			; move the button
			SetGadgetShape button,Rnd(150),Rnd(180),50+Rnd(50),20+Rnd(80)
			; update game information
			If clicks=0 Then time = MilliSecs()
			clicks = clicks + 1
			SetStatusText win, clicks + " clicks in " + (MilliSecs()-time)/1000.0 + " seconds"
			
	End Select
	
Forever

End ; bye!