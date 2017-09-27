
;create main window
window=CreateWindow( "Event viewer",0,0,400,480 )

SetMinWindowSize window

;create some test menus
testmenu=CreateMenu( "Test",0,WindowMenu(window) )
CreateMenu "Menu1",1,testmenu
CreateMenu "Menu2",2,testmenu
CreateMenu "Menu3",3,testmenu
UpdateWindowMenu window

;create a test textfield
textfield=CreateTextField( 8,0,ClientWidth(window)-80,20,window )
SetGadgetLayout textfield,1,0,1,0

timertext=CreateLabel( "Timer:0",ClientWidth(window)-64,4,56,20,window )
SetGadgetLayout timertext,0,1,1,0

timer=CreateTimer( 1 )

;create a test canvas
canvas=CreateCanvas( 8,28,ClientWidth(window)-16,ClientHeight(window)/2-32,window )
SetGadgetLayout canvas,1,1,1,0

;draw something on it!
SetBuffer CanvasBuffer(canvas)
For k=1 To 1000
	Color Rand(255),Rand(255),Rand(255)
	Plot Rand(GadgetWidth(canvas)),Rand(GadgetHeight(canvas))
Next
Color 255,255,0
Text GadgetWidth(canvas)/2,GadgetHeight(canvas)/2,"Canvas",True,True
FlipCanvas canvas

;create a test button
okay=CreateButton( "OKAY!",8,ClientHeight(window)-32,64,24,window )
SetGadgetLayout okay,1,0,0,1

;create another test button
cancel=CreateButton( "CANCEL!",ClientWidth(window)-72,ClientHeight(window)-32,64,24,window )
SetGadgetLayout cancel,0,1,0,1

;create a listbox to display events in
textarea=CreateTextArea( 8,ClientHeight(window)/2,ClientWidth(window)-16,ClientHeight(window)/2-40,window )
SetGadgetLayout textarea,1,1,1,1
SetTextAreaFont textarea,LoadFont( "helvetica",16,True )
DisableGadget textarea

While WaitEvent()

	Select EventID()
	Case $401
		If EventSource()=textfield And EventData()=13
			ActivateGadget window
		EndIf
	Case $803
		If Confirm( "Really Quit?",True ) End
	Case $4001
		SetGadgetText timertext,"Timer:"+EventData()
	End Select
	
	n=n+1
	t$="Event "+n+":$"+Hex$(EventID())+" ("+EventDesc$()+") "
	
	AddTextAreaText textarea,Chr$(10)+t$
	
Wend

Function EventDesc$()
	Select EventID()
	;key events
	Case $101:Return "Key Down:"+EventData()
	Case $102:Return "Key Up:"+EventData()
	Case $103:Return "Key Char:"+EventData()
	;mouse events
	Case $201:Return "Mouse Down:"+EventData()
	Case $202:Return "Mouse Up:"+EventData()
	Case $203:Return "Mouse Move:"+EventX()+","+EventY()
	Case $204:Return "Mouse Wheel:"+EventData()
	Case $205:Return "Mouse Enter"
	Case $206:Return "Mouse Leave"
	;gadget events
	Case $401:Return "Gadget Action:"+EventData()
	Case $402:Return "Gadget Activate"
	;window events
	Case $801:Return "Window Move:"+EventX()+","+EventY()
	Case $802:Return "Window Size:"+EventX()+","+EventY()
	Case $803:Return "Window Close"
	Case $804:Return "Window Activate"
	;menu events
	Case $1001:Return "Menu Action:"+EventData()
	;app events
	Case $2001:Return "App Suspend"
	Case $2002:Return "App Resume"
	Case $2003:Return "App DisplayChange:"+EventX()+","+EventY()
	;timer events
	Case $4001:Return "Timer Tick:"+EventData()
	End Select
	Return "<unknown>"
End Function