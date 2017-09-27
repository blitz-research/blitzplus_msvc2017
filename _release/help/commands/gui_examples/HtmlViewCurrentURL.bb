; A Basic Blitz Web Browser!
win=CreateWindow("Basic Blitz Web Browser",100,100,500,500,0,35)

; Create htmlview gadget
html = CreateHtmlView(0,0,500,480,win)

; Ensure the HTML view will stretch to meet the size of the window when it is resized
SetGadgetLayout html,1,1,1,1

; Go to URL
HtmlViewGo html,"http:\\www.blitzbasic.com"

; Create label gadget that will display current url
label=CreateLabel("",0,480,500,20,win,3)

; Ensure that the label stays at the bottom of the window
SetGadgetLayout label,1,1,0,1

Repeat

	id = WaitEvent()
	If id=$803 Then Exit
	
	; If gadget event occurs...
	If id=$401
	
		; Set label's text to that of the htmlview current url. This is the url of the page currently displayed by the htmlview gadget
		SetGadgetText label,HtmlViewCurrentURL$(html)
		
	EndIf

Forever