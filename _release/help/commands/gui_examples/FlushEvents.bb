; create a basic window to play with
win=CreateWindow("PeekEvent Example",100,100,200,200,0,49)
button=CreateButton("Hit me! I do nothing!",10,10,180,20,win)
Repeat
	id=PeekEvent() ; are there any events waiting to be processed?
	If id<>0 Then
		If id=$803 Then Exit 	; exit on window close	
		FlushEvents()			; clear out any remaining events
	End If
	VWait 
Forever
End ; bye!