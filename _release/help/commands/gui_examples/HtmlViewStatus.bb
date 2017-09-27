; A Basic Blitz Web Browser!
win=CreateWindow("Basic Blitz Web Browser",100,100,500,500,0,35)

; Create htmlview gadget
html = CreateHtmlView(0,0,500,480,win)

; Ensure the HTML view will stretch to meet the size of the window when it is resized
SetGadgetLayout html,1,1,1,1

; Go to URL
HtmlViewGo html,"http:\\www.blitzbasic.com"

; Create label gadget that will display htmlview status
label=CreateLabel("",0,480,500,20,win,3)

; Ensure that the label stays at the bottom of the window
SetGadgetLayout label,1,1,0,1

Repeat

	oldstatus=status
	
	; Get htmlview status
	status=HtmlViewStatus(html)
	
	; If status value has changed then update label text to show htmlview status
	If status<>oldstatus Then SetGadgetText label,"HtmlViewStatus: "+status+" (0=loaded, 1=loading)"
	
	id = PeekEvent()
	If id<>0
		If id=$803 Then Exit
		FlushEvents
	EndIf

Forever