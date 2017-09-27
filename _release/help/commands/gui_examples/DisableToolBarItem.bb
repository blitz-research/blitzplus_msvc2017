; get bitmap icon from the Blitz directory 
appdir$=SystemProperty("appdir") 
blitzdir$=Left(appdir,Len(appdir)-5) 
BMP$=blitzdir$+"\cfg\dbg_toolbar.bmp" 
Notify "We will use this bitmap for our ToolBar: "+Chr$(13)+BMP$ 

; create a window to put the toolbar in
WinHandle=CreateWindow("Test Enable / Disable ToolbarItem",0,0,400,200) 
; and create the toolbar
toolHandle=CreateToolBar(BMP$,0,0,0,0,WinHandle) 

; and now a loop to demonstrate it in action
Repeat 
	event=WaitEvent() 
	If event=$803 Then End ; exit when we receive a Window Close event
	If event=$401 Then ; Button action event.  EventData contains the toolbar button hit.
		; Note that if we had more than just the toolbar on the form, we would check EventSource too
		If EventData()=0 Then 
			SetStatusText WinHandle,"Red Light selected"
			EnableToolBarItem toolHandle,1
			DisableToolBarItem toolHandle,0
		End If
		If EventData()=1 Then
			SetStatusText WinHandle,"Green Light selected" 
			EnableToolBarItem toolHandle,0
			DisableToolBarItem toolHandle,1
		End If			
		If EventData()=2 Then SetStatusText WinHandle,"Step Next selected" 
		If EventData()=3 Then SetStatusText WinHandle,"Step Into selected" 
		If EventData()=4 Then SetStatusText WinHandle,"Step Over selected" 
		If EventData()=5 Then SetStatusText WinHandle,"Boom selected" 
	End If 
Forever

End ; bye!