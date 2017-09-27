; TextAreaText Example by Mag

win=CreateWindow ("",0,0,300,300);create window first
txtbox=CreateTextArea(0,0,200,200,win) ;create textarea in that window
SetGadgetText txtbox,"Type anything (multiline) "+Chr$(13)+"And press Get Text button" ;put some text on that textare for info
gt=CreateButton("Get Text",200,0,80,20,win);create button
Repeat
id=WaitEvent() ;wait for user action (in a form of event)
If id=$803 Then End ;to quit program when receive Window close event 
If id=$401 And EventSource()=gt Then ;when ok is press
Notify "This is your text in TextArea:"+Chr$(13)+TextAreaText$(txtbox);<<--TO GET TEXT FROM TEXTFIELD
End If
Forever