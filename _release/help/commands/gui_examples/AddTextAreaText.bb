; AddTextAreaText Example by Mag

win=CreateWindow ("",0,0,300,300);create window first
txtbox=CreateTextArea(0,0,200,200,win) ;create textarea in that window
add=CreateButton("Add Text",200,0,80,20,win);create button
Repeat
id=WaitEvent() ;wait for user action (in a form of event)
If id=$803 Then End ;to quit program when receive Window close event 
If id=$401 And EventSource()=add Then ;when ok is press
AddTextAreaText txtbox,Chr$(13)+"Some text added";<<---ADD SOME TEXT IN TEXT AREA
End If
Forever