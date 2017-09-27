
; Terrible code, but shows what can be done with BlitzPlus...

; {Note: the timer shows minutes and seconds, but I didn't
; get around to adding a separating colon :)

Const EVENT_None		= $0		; No event (eg. a WaitEvent timeout)
Const EVENT_KeyDown		= $101		; Key pressed
Const EVENT_KeyUp		= $102		; Key released
Const EVENT_ASCII		= $103		; ASCII key pressed
Const EVENT_MouseDown	= $201		; Mouse button pressed
Const EVENT_MouseUp		= $202		; Mouse button released
Const EVENT_MouseMove	= $203		; Mouse moved
Const EVENT_Gadget		= $401		; Gadget clicked
Const EVENT_Move		= $801		; Window moved
Const EVENT_Size		= $802		; Window resized
Const EVENT_Close		= $803		; Window closed
Const EVENT_Menu		= $1001		; Menu item selected
Const EVENT_LostFocus	= $2001		; Window lost focus
Const EVENT_GotFocus	= $2002		; Window got focus
Const EVENT_Timer		= $4001		; Timer event occurred

Global sw = 640
Global sh = 480

Global highscore = 0

window = CenterWindow ("Lava Palaver - use the mouse...", sw, sh, 0, 7 + 32)

menu = CreateMenu ("&Game", 0, WindowMenu (window))
CreateMenu "&About", 1, menu
CreateMenu "", 2, menu
CreateMenu "E&xit", 3, menu
UpdateWindowMenu window

canvas = CreateCanvas (0, 0, ClientWidth (window), ClientHeight (window), window)
SetGadgetLayout canvas, 1, 1, 1, 1

SetBuffer CanvasBuffer (canvas)

Dim number (9)
For a = 0 To 9
	number (a) = LoadImage (a + ".bmp")
	MaskImage number (a), 255, 0, 255
Next

engine = LoadImage ("engine.png")
MaskImage engine, 255, 0, 255

volcano = LoadImage ("volcano.png")
MaskImage volcano, 255, 0, 255

Global droplet1 = LoadImage ("drop1.bmp")
MaskImage droplet1, 255, 0, 255

Global droplet2 = LoadImage ("drop2.bmp")
MaskImage droplet2, 255, 0, 255

Global droplet3 = LoadImage ("drop3.bmp")
MaskImage droplet3, 255, 0, 255

Global droplet4 = LoadImage ("drop4.bmp")
MaskImage droplet4, 255, 0, 255

Global hose = LoadImage ("hose.png")
MaskImage hose, 255, 0, 255

Global player1 = LoadImage ("fireman-alone1.bmp")
MaskImage player1, 255, 0, 255

Global player2 = LoadImage ("fireman-alone2.bmp")
MaskImage player2, 255, 0, 255

Global shot1 = LoadImage ("shot1.bmp")
MaskImage shot1, 255, 0, 255

Global shot2 = LoadImage ("shot2.bmp")
MaskImage shot2, 255, 0, 255

Global shot3 = LoadImage ("shot3.bmp")
MaskImage shot3, 255, 0, 255

Global foreground = LoadImage ("foregrass.png")
MaskImage foreground, 255, 0, 255

Global lava = LoadImage ("lava.png")

Type Timer
	Field TimerInit
	Field TimeOut
End Type

Type Drop
	Field image
	Field layer
	Field x#
	Field y#
	Field xs#
	Field ys#
End Type

Type HoseShot
	Field image
	Field x#
	Field y#
End Type

.start

If fps Then FreeTimer (fps)

ClsColor 120, 140, 220

enw = ImageWidth (engine)

Cls
DrawImage volcano, 0, -25
DrawImage foreground, 0, -25
FlipCanvas canvas

fps = CreateTimer (60)
	
For fx = 640 To -enw Step -4
	
	Repeat
	
		e = WaitEvent ()
		
		Select e
			Case EVENT_Close
				End
			Case EVENT_Menu
				Select EventData ()
					Case 1
						Notify "Lava Palaver" + Chr (10) + Chr (10) + "A crap game by james @ hi - toro . com"
					Case 3
						If Confirm ("Do you really want to quit?") Then End
				End Select
		End Select

	Until e = EVENT_Timer
	
	If KeyHit (1) Then End
	Cls
	DrawImage volcano, 0, -25
	DrawImage foreground, 0, -25
	DrawImage engine, fx, 340
	FlipCanvas canvas
Next

Delay 1000

p.Timer = SetTimer (200)
player = player1

For fx = -32 To 32

	Repeat
	
		e = WaitEvent ()
		
		Select e
			Case EVENT_Close
				End
			Case EVENT_Menu
				Select EventData ()
					Case 1
						Notify "Lava Palaver" + Chr (10) + Chr (10) + "A crap game by james @ hi - toro . com"
					Case 3
						If Confirm ("Do you really want to quit?") Then End
				End Select
		End Select

	Until e = EVENT_Timer
			
	If KeyHit (1) Then End
	
	Cls
	DrawImage volcano, 0, -25
	DrawImage foreground, 0, -25

	If TimeOut (p)
		If player = player1 Then player = player2 Else player = player1
		p = SetTimer (200)
	EndIf

	DrawImage player, fx - 32, 410
	
	FlipCanvas canvas
Next

MoveMouse 32, MouseY (canvas), canvas

Global gravity# = 0.025
Global level = 0

speed# = 1

mins = 0
secs = 0
Global realsecs = 0

clock.Timer = SetTimer (1000)

FlushMouse
FlushKeys

Repeat

	Repeat
	
		e = WaitEvent ()
		
		Select e
			Case EVENT_Close
				End
			Case EVENT_Menu
				Select EventData ()
					Case 1
						Notify "Lava Palaver" + Chr (10) + Chr (10) + "A crap game by james @ hi - toro . com"
					Case 3
						If Confirm ("Do you really want to quit?") Then End
				End Select
		End Select
		
	Until e = EVENT_Timer
	
	If TimeOut (clock)
		clock = SetTimer (1000)
		secs = secs + 1
		realsecs = realsecs + 1
	EndIf
	
	If level > 93
		dead$ = "Doh! The town's dead!"
		Delete Each Drop
		Notify dead$
		Goto start
	EndIf
	
	Cls
	
	speed = speed + 0.0005
	
	If Rand (1000) > 990 Then CreateDrops (speed)
	
	d.Drop = UpdateDrops (): If d <> Null Then FreeImage d\image: Delete d
	UpdateShots ()
	
	DrawDrops (0)
	DrawImage volcano, 0, -25
	DrawDrops (1)

	DrawImage lava, 0, 480 - level
	
	DrawImage foreground, 0, -25

	x = MouseX (canvas)
	If x > 600 Then x = 600

	If MouseXSpeed (canvas)
		If TimeOut (p)
			If player = player1 Then player = player2 Else player = player1
			p = SetTimer (200)
		EndIf
	EndIf
		
	DrawImage hose, x - 610, 410
	DrawImage player, x - 32, 410

	DrawShots ()
	
	If MouseHit (1) Then CreateShot (x - 24)

	While secs => 60
		secs = secs - 60
		mins = mins + 1
	Wend

	sc$ = secs
	If Len (sc$) < 2 Then sc$ = "0" + sc$
	sc$ = Str (mins) + sc$
	
	nx = 20
	For a = 1 To Len (sc$)
		DrawImage number ((Mid (sc$, a, 1))), nx, 20
		nx = nx + 64
	Next
	
	FlipCanvas canvas
	
Until KeyHit (1)

End

Function CreateShot (x)
	h.HoseShot = New HoseShot
	Select Rand (3)
		Case 1
			shot = shot1
		Case 2
			shot = shot2
		Case 3
			shot = shot3
	End Select
	h\image = CopyImage (shot)
	h\x = x + 42
	h\y = 400
End Function

Function DrawShots ()
	For h.HoseShot = Each HoseShot
		DrawImage h\image, h\x, h\y
	Next
End Function

Function UpdateShots ()
	For h.HoseShot = Each HoseShot
		killShot = False
		h\y = h\y - 4
		For d.Drop = Each Drop
			If ImagesCollide (h\image, h\x, h\y, 0, d\image, d\x, d\y, 0)
				FreeImage d\image: Delete d: killShot = True
			EndIf
		Next
		If h\y < -ImageHeight (h\image) Then FreeImage h\image: Delete h: Goto skipKill
		If killShot Then FreeImage h\image: Delete h
		.skipKill
	Next
End Function

Function DrawDrops (layer)
	For d.Drop = Each Drop
		If d\layer = layer
			DrawImage d\image, d\x, d\y
		EndIf
	Next
End Function

Function UpdateDrops.Drop ()

	For d.Drop = Each Drop

		d\x = d\x + d\xs
		d\y = d\y + d\ys
		d\ys = d\ys + gravity

		If Not d\layer
			If d\ys > 0 Then d\layer = 1
		EndIf
		
		dropWidth = ImageWidth (d\image)
		
		If (d\x < -dropWidth)
			d\x = sw
		Else
			If (d\x > sw)
				d\x = -dropWidth
			EndIf
		EndIf

		If (d\y > sh)
			kill = True
			level = level + 1
		EndIf
	
		If kill
			Return d
		EndIf

	Next

End Function

Function CreateDrops (num = 1)
	For a = 1 To num
		droplets.Drop = New Drop
		Select Rand (4)
			Case 1
				droplet = droplet1
			Case 2
				droplet = droplet2
			Case 3
				droplet = droplet3
			Case 4
				droplet = droplet4
		End Select
		droplets\image = CopyImage (droplet)
		droplets\layer = 0
		droplets\x = Rnd (290, 350)
		droplets\y = Rnd (230, 250)
		droplets\xs = Rnd (-1.5, 1.5)
		droplets\ys = Rnd (-2, -4)
	Next
End Function

Function SetTimer.Timer (tout)

	t.Timer=New Timer
	t\TimerInit=MilliSecs ()						; Reset this Timer object to system ticks
	t\TimeOut=tout									; This Timer object's timeout
	Return t

End Function

Function TimeOut (t.Timer)

	If t <> Null
		If MilliSecs () > t\TimerInit + t\TimeOut	; Check if Timer has run out
			Delete t								; Delete Timer object
			Return 1								; Timer has run out
		EndIf
	EndIf

End Function

Function SecsToMin$ (secs)
	While secs => 60
		secs = secs - 60
		mins = mins + 1
	Wend
	sc$ = secs
	If Len (sc$) < 2 Then sc$ = "0" + sc$
	Return mins + ":" + sc$
End Function

Function CenterWindow (title$, width, height, group = 0, style = 15)
	Return CreateWindow (title$, (ClientWidth (Desktop ()) / 2) - (width / 2), (ClientHeight (Desktop ()) / 2) - (height / 2), width, height, group, style)
End Function