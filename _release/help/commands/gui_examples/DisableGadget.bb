; Create a window
win=CreateWindow("Test Hide / Show / Enable / Disable Gadget",100,100,200,90,0,17)

; and create a label to hide and show
label=CreateLabel("Here I am!",100,30,90,20,win)
HideGadget label ; make sure it's hidden initially

; and two buttons to control our label
buttonShow=CreateButton("Show",10,10,80,20,win)
buttonHide=CreateButton("Hide",10,40,80,20,win)
DisableGadget buttonHide ; disbale Hide initially

; now a small event loop
Repeat
	id=WaitEvent()
	Select id
	
		; exit on window close
		Case $803 : Exit
		
		; dependent on the button hit, hide or show the label
		Case $401
			If EventSource()=buttonHide Then
				HideGadget label
				DisableGadget buttonHide
				EnableGadget buttonShow
			ElseIf EventSource()=buttonShow Then
				ShowGadget label
				EnableGadget buttonHide
				DisableGadget buttonShow
			End If
	End Select
Forever

End ; bye!