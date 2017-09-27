; A Basic Blitz Web Browser! 
win=CreateWindow("Basic Blitz Web Browser",100,100,500,500,0,35) 
html = CreateHtmlView(0,20,500,480,win) 
buttonBack=CreateButton("Back",0,0,250,20,win)
buttonForward=CreateButton("Forward",250,0,250,20,win)
SetGadgetLayout html,1,1,1,1 ; ensure the HTML view will stretch to meet the size of the window when it is resized. 
HtmlViewGo html,"http:\\www.blitzbasic.com" 

; a simple loop 
Repeat 
	id = WaitEvent() 
	If id=$803 Then Exit 
	If id=$401 Then
		If EventSource()=buttonBack Then
			HtmlViewBack html
		Else
			HtmlViewForward html
		End If
	End If
Forever 
End ; bye!