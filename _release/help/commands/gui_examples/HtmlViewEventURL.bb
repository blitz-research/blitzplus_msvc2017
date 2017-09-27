; A Basic Blitz Web Browser!
win=CreateWindow("Basic Blitz Web Browser",100,100,500,500,0,35)

; Create htmlview gadget - for HTMLViewEventURL to return a string, we need to set style to 2 (NONAVIGATE)
html = CreateHtmlView(0,0,500,480,win,2)

; Ensure the HTML view will stretch to meet the size of the window when it is resized
SetGadgetLayout html,1,1,1,1

; Go to URL
HtmlViewGo html,"http:\\www.blitzbasic.com"

; Create label gadget that will display event url
label=CreateLabel("",0,480,500,20,win,3)

; Ensure that the label stays at the bottom of the window
SetGadgetLayout label,1,1,0,1


Repeat

	id = WaitEvent()
	If id=$803 Then Exit

	; If gadget event occurs...
	If id=$401 And EventSource()=html
		url$=HtmlViewEventURL$(html)
		Select EventData()
			Case 0	;page load event
				; Set label's text to that of the htmlview event url
				SetGadgetText label,"loaded "+url$
			Case 1	;user navigate event
				SetGadgetText label,"navigating to "+url$
				HtmlViewGo html,url$
		End Select
	EndIf

Forever