
;an example of monitoring input as its entered...
window=CreateWindow( "Int field demo",0,0,160,120 )

textfield=CreateTextField( ClientWidth(window)/2-64,ClientHeight(window)/2-12,128,24,window )

SetGadgetLayout textfield,1,1,0,0

ActivateGadget textfield

While WaitEvent()<>$803

	If EventID()=$401
	
		If EventData()=13 End
	
		If EventSource()=textfield
		
			n=TextFieldText$(textfield)
			If n>100
				Notify "A number from 1 to 100 must be entered"
				n=100
			EndIf
			If n SetGadgetText textfield,n Else SetGadgetText textfield,""
		EndIf
	EndIf
Wend

End