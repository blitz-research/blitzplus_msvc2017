; Example provided by Mag, added to documentation by Mark Tiffany
win=CreateWindow ("Test SetTextAreaFont",0,0,300,300) ; create a window first 
txtbox=CreateTextArea(0,0,200,200,win) ; create text area in the above window 
SetGadgetText txtbox,"Some text goes here" ; put some text in text area 
chg=CreateButton("Change Font",200,0,80,20,win) ; create button 
Repeat 
 id=WaitEvent() ; wait for user action (in the form of an event) 
 If id=$803 Then End ; quit the program when we receive a Window close event 
 If id=$401 And EventSource()=chg Then ; when Change Font is pressed 
 SetTextAreaFont txtbox,RequestFont() ; <--- CHANGE FONT FOR TEXTAREA 
 End If
Forever 
End