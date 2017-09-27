; Example provided by Mag, added to documentation by Mark Tiffany
win = CreateWindow ("Test Text Area",0,0,300,300) ; create a window first
txtbox = CreateTextArea(0,0,200,200,win) ; <--- CREATE TEXTAREA (Multi line text field)
Repeat
	id = WaitEvent() ; wait for user action (in the form of an event)
	If id = $803 Then End ; quit the program when we receive a Window close event
Forever
End