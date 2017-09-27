;----------------------------------------------------------------
;Blitz Alarm Clock
;By Rob Cummings (http://www.redflame.net)
;
;This is only a rough example! DO NOT RELY on this without
;checking the code first...
;----------------------------------------------------------------

AutoSuspend False

Global window, alarmwindow, ringwindow, alertwindow, alertcanvas, togglealert, menuclocktype1, menuclocktype2, test, time_panel, time_canvas, alarmtime_panel, alarmtime_canvas
Global am, pm, ampm=0, alarminput, alarm, filename$, toggle, fullclock, soundchannel, recentlyset=0
Global input_hour, input_min, alarm_hour$="12", alarm_min$="00", hour$, min$
Global defaultalarm$=CurrentDir$()+"/alarm.wav"

; get/set desktop and window dimensions
dw=GadgetWidth(Desktop())
dh=GadgetHeight(Desktop())
w=320 : h=96
x=dw/2 - w/2 : y=dh/2 - h/2

; create our application
window = CreateWindow("Blitz Alarm!",x,y,w,h,0,1+4)

filemenu = CreateMenu ("&File", 0, WindowMenu (window))
	CreateMenu ("About",   1, filemenu)
	CreateMenu ("Exit",    2, filemenu)

displaymenu = CreateMenu ("&Display", 3, WindowMenu (window))
	menuclocktype1=CreateMenu ("12 hour clock",   4, displaymenu)
	menuclocktype2=CreateMenu ("24 hour clock",   5, displaymenu)
	CheckMenu menuclocktype1

alarmmenu = CreateMenu ("&Alarm", 6, WindowMenu (window))
	CreateMenu ("Set alarm",   7, alarmmenu)
	CreateMenu ("Set alert",   8, alarmmenu)
	test=CreateMenu ("Test",   9, alarmmenu)
	
UpdateWindowMenu window

; canvas is needed for drawing on
time_panel = CreatePanel(8,8,144,32,window,1)
time_canvas = CreateCanvas(0,0,144,32,time_panel)

alarmtime_panel = CreatePanel(144+14,8,144,32,window,1)
alarmtime_canvas = CreateCanvas(0,0,144,32,alarmtime_panel)

; setfont will fail unless we set an active buffer for it first
font = LoadFont("ariel",24,1)
SetBuffer (CanvasBuffer(time_canvas))
SetFont font

SetBuffer (CanvasBuffer(alarmtime_canvas))
SetFont font

;----------------------------------------------------------------

; create our input dialogue we will use for putting the alarm time in
alarmwindow = CreateWindow("Set alarm",x,y,w,h,0,4+1)
input_hour=CreateTextField( 40,0,64,24,alarmwindow )
input_min=CreateTextField( 130,0,64,24,alarmwindow )
CreateLabel("Hour",61,25,100,20,alarmwindow)
CreateLabel("Min",154,25,100,20,alarmwindow)
am=CreateButton("am",230,-4,36,24,alarmwindow,3)
SetButtonState am,1
pm=CreateButton("pm",230,16,36,24,alarmwindow,3)
SetGadgetText input_hour,alarm_hour$
SetGadgetText input_min,alarm_min$

;input gadgets need to be activated
ActivateGadget input_hour
ActivateGadget input_min

DisableGadget alarmwindow
HideGadget alarmwindow


;----------------------------------------------------------------

;create the ALARM! display window
alertwindow = CreateWindow("ALARM!",x,y,w,h,0,16+1)
alertcanvas=CreateCanvas(0,0,w,h,alertwindow)
; setfont will fail unless we set an active buffer for it first
bigfont = LoadFont("ariel",48,1)
SetBuffer (CanvasBuffer(alertcanvas))
SetFont bigfont
DisableGadget alertwindow
HideGadget alertwindow

;----------------------------------------------------------------

; the timer will allow our application to refresh periodically
timer=CreateTimer(2)

; main loop
While Not KeyHit(1)
	WaitEvent()
	
	Select EventID()
		Case $401 ; a gadget event, such as alarm input field
			If alarminput
				Menu_SetAlarm()
			EndIf
			If EventSource()=alertcanvas Alert()
			
		Case $803 ; a close window event
			If EventSource()=window Then Menu_Exit()
			If EventSource()=alarmwindow Then Menu_SetAlarm()
			If EventSource()=alertwindow Then Alert()
			
		Case $1001 ; a menu event
			Select EventData()
				Case 1 : Menu_About()
				Case 2 : Menu_Exit()
				Case 4 : Menu_HalfTime()
				Case 5 : Menu_FullTime()
				Case 7 : Show_SetAlarm()
				Case 8 : Menu_SetAlert()
				Case 9 : Menu_TestAlarm()
			End Select
			
		Case $4001 ; a timer tick event
			UpdateClocks()
			KeepInDesktop(window)
			CheckAlarm()
			
			If alarminput
				Menu_SetAlarm()
			EndIf
	End Select
	
Wend
End

;----------------------------------------------------------------

Function Menu_Exit()
	AppTitle "Quit Blitz Alarm?"
	ans=Confirm("This will Quit Blitz Alarm. Do you want to continue?",1)
	If ans=1 Then End
End Function

;----------------------------------------------------------------

Function Menu_About()
	AppTitle "About"
	Notify "Blitz Alarm!"+Chr$(10)+"By Rob Cummings"
End Function

;----------------------------------------------------------------

Function Menu_HalfTime()
	CheckMenu menuclocktype1
	UncheckMenu menuclocktype2
	UpdateWindowMenu window
	fullclock=0
End Function

;----------------------------------------------------------------

Function Menu_FullTime()
	CheckMenu menuclocktype2
	UncheckMenu menuclocktype1
	UpdateWindowMenu window
	fullclock=1
End Function

;----------------------------------------------------------------

Function Show_SetAlarm()
	DisableGadget window
	EnableGadget alarmwindow
	ShowGadget alarmwindow
	SetGadgetShape alarmwindow,GadgetX(window),GadgetY(window)+GadgetHeight(window),GadgetWidth(alarmwindow),GadgetHeight(alarmwindow)
	alarminput=1
	alarm=0
End Function

;----------------------------------------------------------------

Function Menu_SetAlarm()

	; all done with setting alarm?
	If EventID()=$803
		alarminput=0
		HideGadget alarmwindow
		EnableGadget window
		recentlyset=1
	EndIf
	
	; hour
	If EventSource()=input_hour
		num=TextFieldText$(input_hour)
		If num<13
			alarm_hour$=num
		EndIf
		If num SetGadgetText input_hour,alarm_hour$
	EndIf
	
	If EventSource()=input_min
		num=TextFieldText$(input_min)
		If num<60
			alarm_min$=num
		EndIf
		If num SetGadgetText input_min,alarm_min$
	EndIf
	
	If EventSource()=am
		ampm=0
	EndIf
	If EventSource()=pm
		ampm=1
	EndIf
	
	
	KeepInDesktop(alarmwindow)
End Function

;----------------------------------------------------------------

Function Menu_SetAlert()
	filename$=RequestFile$("Choose alarm sound","wav,mp3,mid,ogg")
End Function

;----------------------------------------------------------------

Function Menu_TestAlarm()
	If MenuChecked(test)=0
		alarm=1
		CheckMenu test
	Else
		alarm=0
		UncheckMenu test
	EndIf
	UpdateWindowMenu window
End Function

;----------------------------------------------------------------

Function UpdateClocks()
	
	;----------------------------------------------------------------
	; update the current time
	;----------------------------------------------------------------
	
	SetBuffer (CanvasBuffer(time_canvas))
	Color 255,255,255
	Cls
	
	; parse CurrentTime$()
	t$=CurrentTime$()
	pos = Instr(t$,":")
	hour$ = Left(t$,pos-1)
	remainder$ = Right(t$,Len(t$)-pos)
	pos = Instr(remainder$,":")
	min$= Left(remainder$,pos)
	If hour$>12 cycle$="  pm" Else cycle$="  am"
	If hour$=12 And min$>0 cycle$="  pm"
	
	If fullclock=1
		finaltime$ = t$
	Else
		If Int(hour$)=0
			finaltime$="12:"+remainder$
		Else
			If Int(hour$)<13
				finaltime$ = Int(hour$) +":"+remainder$
			Else
				finaltime$ = (Int(hour$)-12) +":"+remainder$
			EndIf
		EndIf
	EndIf
	
	If fullclock Then cycle$=""
	Text 72,0,finaltime$+cycle$,1
	FlipCanvas time_canvas
	
	

	;----------------------------------------------------------------
	; update the alarm display time
	;----------------------------------------------------------------
	
	SetBuffer (CanvasBuffer(alarmtime_canvas))
	toggle=1-toggle
	If toggle=1
		Color 255,0,0
	Else	
		Color 200,0,0
	EndIf
	Cls

	; parse alarm times
	If ampm=0 cycle$="  am" Else cycle$="  pm"
	num=Int(alarm_min$)
	zero$=""
	If num<10 zero$="0"
	If num=0 Then zero$=""
	
	
	If fullclock=1
		If Int(alarm_hour$)=12
			finaltime$="00:"+zero$+alarm_min$+":00"
		Else
			finaltime$=(Int(alarm_hour$)+12)+":"+alarm_min$+":00"
		EndIf
	Else
		finaltime$=alarm_hour$+":"+zero$+alarm_min$+":00"
	EndIf
	
	If fullclock Then cycle$=""
	Text 72,0,finaltime$+cycle$,1
	FlipCanvas alarmtime_canvas
	
	;----------------------------------------------------------------
	; Calculate and compare the "real times" and differences
	;----------------------------------------------------------------
	
	;realtime
	If Int(hour$)=0
		realhour=12
		realampm=0
	Else
		If Int(hour$)<13
			realhour = Int(hour$)
			realampm=0
		Else
			realhour = (Int(hour$)-12)
			realampm=1
		EndIf
	EndIf

	;real min
	realmin = Int(min$)
	If realampm=0 And realhour12 And realmin>0 Then realampm=1
	
	
	;real alarm time
	realalarmhour=alarm_hour$
	realalarmmin=alarm_min$
	
	
	hourdiff#=Abs(realhour - realalarmhour)
	mindiff#=Abs(realmin - realalarmmin)
	
	; check the alarm!
	If realampm = ampm And mindiff<=2
		If realhour=>realalarmhour And realmin=>realalarmmin
			If recentlyset=1 And hourdiff=0
				alarm=1
			EndIf
		EndIf
	EndIf
		
End Function

;----------------------------------------------------------------

Function KeepInDesktop(window)
	If GadgetX(window)<0
		SetGadgetShape window,0,GadgetY(window),GadgetWidth(window),GadgetHeight(window)
	EndIf
	If GadgetX(window)>GadgetWidth(Desktop())-GadgetWidth(window)
		SetGadgetShape window,GadgetWidth(Desktop())-GadgetWidth(window),GadgetY(window),GadgetWidth(window),GadgetHeight(window)
	EndIf
	If GadgetY(window)<0
		SetGadgetShape window,GadgetX(window),0,GadgetWidth(window),GadgetHeight(window)
	EndIf
	If GadgetY(window)>GadgetHeight(Desktop())-GadgetHeight(window)
		SetGadgetShape window,GadgetX(window),GadgetHeight(Desktop())-GadgetHeight(window),GadgetWidth(window),GadgetHeight(window)
	EndIf
End Function

;----------------------------------------------------------------

Function CheckAlarm()
	If alarm=True
		If filename$<>"" And FileType(filename$)<>0
			If ChannelPlaying(soundchannel)=0 soundchannel=PlayMusic(filename$,1)
		Else
			If ChannelPlaying(soundchannel)=0 soundchannel=PlayMusic(defaultalarm$,1)
		EndIf
		Alert()
	Else
		StopChannel soundchannel
	EndIf
End Function

;----------------------------------------------------------------

Function Alert()
	ShowGadget alertwindow
	EnableGadget alertwindow
	DisableGadget window
	HideGadget window


	; all done with alarm? turn it off?
	If EventID()=$803
		HideGadget alertwindow		
		EnableGadget window
		ShowGadget window
		recentlyset=0
		alarm=0
	EndIf
	
	togglealert=1-togglealert
	SetBuffer CanvasBuffer(alertcanvas)
	
	If togglealert=1
		ClsColor 255,255,255
		Color 0,0,0
	EndIf
	
	Cls
	Text GadgetWidth(alertcanvas)/2,(GadgetHeight(alertcanvas)/2)-16,"ALARM!",1,1
	FlipCanvas alertcanvas
	
	ClsColor 0,0,0
	Color 255,255,255
End Function


;----------------------------------------------------------------
;EOF
;----------------------------------------------------------------