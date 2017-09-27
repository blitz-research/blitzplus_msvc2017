; create a window...
WinHandle=CreateWindow("WaitEvent Example",0,0,400,200) 
; ...and a single button upon that window
ExitButton=CreateButton("Exit",50,50,300,40,WinHandle) 

; main loop to handle events
Repeat 
  WaitEvent()
  If EventID()=$401 Then 
    If EventSource()=ExitButton Then Exit 
  End If 
Forever
End