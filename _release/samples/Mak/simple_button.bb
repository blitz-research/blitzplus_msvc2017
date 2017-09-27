
;create main window
window=CreateWindow( "Main window",0,0,400,300 )

;create a centred button
button=CreateButton( "Click Me",ClientWidth(window)/2-32,ClientHeight(window)/2-12,64,24,window )

;wait for an event...
While WaitEvent()<>$803

	;was it a gadget action event?
	If EventID()=$401
	
		;yes! was it from the button?
		If EventSource()=button
		
			;Yep!
			Notify "Right on!"
			
		EndIf
	EndIf
	
Wend

End