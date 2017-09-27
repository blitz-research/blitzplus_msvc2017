; 
; Super hello world example 
; Coded by Stephen Greener (aka Shagwana) 
; Sublime Games 
; 
;__/ Constants \_______________________________________________________________________________________________________; 

Const WINDOW_WIDTH = 340 
Const WINDOW_HEIGHT = 100 

Const WINDOW_WIDTH2 = 140 
Const WINDOW_HEIGHT2 = 30 

;Event flags 
Const EVENT_KEYDOWN = $101 ; Key pressed 
Const EVENT_KEYUP = $102 ; Key released 
Const EVENT_KEYCHAR = $103 ; Key generated ASCII character 
Const EVENT_MOUSEDOWN = $201 ; Mouse button pressed 
Const EVENT_MOUSEUP = $202 ; Mouse button released 
Const EVENT_MOUSEMOVE = $203 ; Mouse moved 
Const EVENT_GADGET = $401 ; Gadget clicked 
Const EVENT_MOVE = $801 ; Window moved 
Const EVENT_SIZE = $802 ; Window resized 
Const EVENT_CLOSE = $803 ; Window closed 
Const EVENT_MENU = $1001 ; Menu item selected 
Const EVENT_APP_PAUSED = $2001 ; The app has been suspended (loss windows focus) 
Const EVENT_APP_STARTED = $2002 ; The app has been resumed (regained windows focus) 


;Window creation flags 
Const APP_WINDOW_CREATION_FLAGS = %00001 ;standard window 
Const TOOL_WINDOW_CREATION_GLAGS = %10001 

;__/ Varibles \________________________________________________________________________________________________________; 


Global iDesktopWidth=ClientWidth(Desktop()) 
Global iDesktopHeight=ClientHeight(Desktop()) 




;__/ Functions \_______________________________________________________________________________________________________; 

;Show the hello world window, make sure user wants to quit/exit 
Function HelloWorld() 

;The bg image 
pMainWindow=CreateWindow("A blitz plus example",(iDesktopWidth-WINDOW_WIDTH)/2,(iDesktopHeight-WINDOW_HEIGHT)/2,WINDOW_WIDTH,WINDOW_HEIGHT,0,APP_WINDOW_CREATION_FLAGS) 

;Show some text in the window 
pText=CreateLabel( "Hello world!",(ClientWidth(pMainWindow)/2)-84,(ClientHeight(pMainWindow)/2)-18,168,32,pMainWindow,0) 

bDone=False ; This loop is not read to be exited yet 
While bDone=False 
Ev=WaitEvent() ; Wait till the 'x' is tapped in the window 
Es=EventSource() 

Select Es ; Source of the event 
Case pMainWindow: 
If Ev=$803 ; X tapped in the main window 
If Confirm("Do you want to quit?")=1 
If Confirm("Do you really, really, really want to quit?",True)=1 
If Proceed("Now look, are you 100% sure you want to "+Chr(10)+"exit this wonderfull Blitz plus example?",True)=1 
bDone=True ; Exit the outer loop 
EndIf 
EndIf 
EndIf 
EndIf 
End Select 

Wend 

FreeGadget pText 
FreeGadget pMainWindow 

End Function 


;This will wait for 10 seconds, while the user forgets about this app :) 
Function WaitSomeTime() 
t=MilliSecs()+(1000*23) ;it is 23 seconds from now 
Repeat 
VWait 5 
Until MilliSecs()>t 
End Function 


;Bet you thought you got rid of me :-) 
Function HeHe1() 

;The bg image 
pMainWindow=CreateWindow("Bet you thought you got rid of me!",(iDesktopWidth-WINDOW_WIDTH)/2,(iDesktopHeight-WINDOW_HEIGHT)/2,WINDOW_WIDTH,WINDOW_HEIGHT,0,APP_WINDOW_CREATION_FLAGS) 

;Show some text in the window 
pButton=CreateButton("Go away now!",(ClientWidth(pMainWindow)/2)-84,(ClientHeight(pMainWindow)/2)-18,168,32,pMainWindow,0) 

bDone=False ; This loop is not read to be exited yet 
While bDone=False 
Ev=WaitEvent() ; Wait till the 'x' is tapped in the window 
Es=EventSource() 

Select Es ; Source of the event 
Case pMainWindow: 
If Ev=$803 ; X tapped in the main window 
Notify("Tap the 'go away now' button to exit") 

EndIf 
Case pButton: 
bDone=True 
End Select 

Wend 

FreeGadget pButton 
FreeGadget pMainWindow 

End Function 


;Chase the window around the desktop time 
Function BiteMe() 

;The bg image 

wxp=((iDesktopWidth-WINDOW_WIDTH2)/2)+(WINDOW_WIDTH2/4) 
wyp=((iDesktopHeight-WINDOW_HEIGHT2)/2)+(WINDOW_HEIGHT2/4) 

pMainWindow=CreateWindow("Bite me!",wxp,wyp,WINDOW_WIDTH2,WINDOW_HEIGHT2,0,%10000 Or APP_WINDOW_CREATION_FLAGS) 


bDone=False ; This loop is not read to be exited yet 
While bDone=False 
Ev=WaitEvent(2) ; Wait till the 'x' is tapped in the window 


xp=(MouseX()-(WINDOW_WIDTH2/2)) 
yp=(MouseY()-(WINDOW_HEIGHT2/2)) 

dx=Abs(xp-wxp) 
dy=Abs(yp-wyp) 
dist=Sqr(dx*dx + dy*dy) 

If dist<120 

If Abs(wyp-yp)>5 
If wxp<xp ;Window is to the left of the mouse pointer 
wxp=wxp-2 
If wxp<0 Then wxp=0 
Else 
wxp=wxp+2 
If wxp>(iDesktopWidth-WINDOW_WIDTH2) Then wxp=(iDesktopWidth-WINDOW_WIDTH2) 
EndIf 
EndIf 
If wyp<yp ;Window is to the above of the mouse pointer 
wyp=wyp-2 
If wyp<0 Then wyp=0 
Else 
wyp=wyp+2 
If wyp>(iDesktopHeight-WINDOW_HEIGHT2) Then wyp=(iDesktopHeight-WINDOW_HEIGHT2) 
EndIf 

SetGadgetShape pMainWindow,wxp,wyp,WINDOW_WIDTH2,WINDOW_HEIGHT2 
EndIf 

Es=EventSource() 

Select Es ; Source of the event 
Case pMainWindow: 
If Ev=$803 ; X tapped in the main window 
bDone=True 
EndIf 

End Select 

Wend 


FreeGadget pMainWindow 

End Function 


;__/ Main program \____________________________________________________________________________________________________; 

.MainProgram: 
AppTitle "Hello world!" 

HelloWorld() 
WaitSomeTime() 
HeHe1() 
BiteMe() 

End 
