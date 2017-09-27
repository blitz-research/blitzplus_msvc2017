; A Basic Blitz Web Browser!
win=CreateWindow("Basic Blitz Web Browser",100,100,500,500,0,35)

; Create htmlview gadget - for HTMLViewEventURL to return a string, we need to set style to 2 (NONAVIGATE)
html = CreateHtmlView(0,0,500,500,win,2)

; Ensure the HTML view will stretch to meet the size of the window when it is resized
SetGadgetLayout html,1,1,1,1

; Go to URL
HtmlViewGo html,"about:blank"

; Wait until page has loaded before calling HTMLViewRun, otherwise we'll get error
Repeat

	id=WaitEvent()

	; If gadget event occurs...
	If id=$401 And EventSource()=html
		If EventData()=0 Then loaded=True
	EndIf
	
Until loaded

; HTML View Run
HtmlViewRun html,"document.body.innerHTML='Hello world!'"

Repeat

	id = WaitEvent()
	If id=$803 Then Exit
	
Forever