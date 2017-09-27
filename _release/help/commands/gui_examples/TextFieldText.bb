; TextFieldText Example by Mag

win=CreateWindow ("",0,0,300,100) ;create window
txtbox=CreateTextField(0,0,200,20,win) ;create textfield in that window
SetGadgetText txtbox,"Type anything and press OK" ;set text in that textfield for info
ok=CreateButton("OK",200,0,80,20,win) ;create button

Repeat
	id=WaitEvent() ;wait for user action (in a form of event)
	If id=$803 Then End ;to quit program when we receive Window close event 
	If id=$401 And EventSource()=ok Then ; when ok is pressed
		Notify "This is your text in TextField:"+Chr$(13)+TextFieldText$(txtbox); <---TO GET TEXT FROM TEXTFIELD
	End If
Forever
End