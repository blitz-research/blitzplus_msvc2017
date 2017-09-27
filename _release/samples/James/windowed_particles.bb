EndGraphics

; -----------------------------------------------------------------------------
; Example of a windowed-mode game loop...
; -----------------------------------------------------------------------------

; Event definitions...

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
Const EVENT_Front		= $804		; Window brought to front
Const EVENT_Menu		= $1001		; Menu item selected
Const EVENT_LostFocus	= $2001		; App lost focus
Const EVENT_GotFocus	= $2002		; App got focus
Const EVENT_Timer		= $4001		; Timer event occurred

; Particle type for demo purposes...

Type Particle
	Field x#
	Field y#
	Field ys#
	Field r#
	Field g#
	Field b#
	Field dec#
	Field size#
End Type

; Gravity...

Const grav# = 0.05

; Create a centered game window (see CenterWindow function at bottom)...

window = CenterWindow ("Click and drag below...", 640, 480)

; Create a simple menu...

menu = CreateMenu ("&Game", 0, WindowMenu (window))
CreateMenu "E&xit", 1, menu
UpdateWindowMenu window

; Create a canvas (game display area) and 'pin' all of its edges to the window...

canvas = CreateCanvas (0, 0, ClientWidth (window), ClientHeight (window), window)
SetGadgetLayout canvas, 1, 1, 1, 1

; Draw to the canvas from here on...

SetBuffer CanvasBuffer (canvas)

gh = GadgetHeight (canvas)

; Create a timer to update every 1/60th of a second...

fps = CreateTimer (60)

; Main game loop...

Repeat

	; Event loop:
	
	; Repeat event checking until we get a timer event from
	; the 'fps' timer, every 1/60th of a second...
	
	; A MouseDown or MouseUp event sets a variable used further
	; down in the game loop...
	
	Repeat
		e = WaitEvent ()
		Select e
			Case EVENT_Close
				End
			Case EVENT_MouseDown
				mdown = True
			Case EVENT_MouseUp
				mdown = False
			Case EVENT_Menu
				End
		End Select
	Until e = $4001
	
	; Clear the canvas...
		
	Cls
	
	; Create a particle if the mdown variable was set in the event loop...
	
	If mdown
		mx = MouseX (canvas) + Rand (-8, 8)
		my = MouseY (canvas) + Rand (-8, 8)
		CreateParticle (mx, my)
	EndIf
	
	; Update the particles' positions and draw them...
	
	UpdateParticles (gh)

	; Show the result...
	
	FlipCanvas canvas

Until KeyHit (1)
	
End

; Misc functions...

Function CenterWindow (title$, width, height, group = 0, style = 15)
	Return CreateWindow (title$, (ClientWidth (Desktop ()) / 2) - (width / 2), (ClientHeight (Desktop ()) / 2) - (height / 2), width, height, group, style)
End Function

Function CreateParticle (x, y)
	p.Particle = New Particle
	p\size = 0
	p\x = x
	p\y = y
	p\r = 255
	p\g = 255
	p\b = 255
	p\dec = Rnd (1, 3)
End Function

Function UpdateParticles (maxy)
	For p.Particle = Each Particle
		p\size = p\size + 0.5
		p\ys = p\ys - grav
		p\y = p\y + p\ys
		p\r = p\r - (p\dec / 4)
		p\g = p\g - (p\dec / 2): If p\g < 0 Then p\g = 0
		p\b = p\b - p\dec: If p\b < 0 Then p\b = 0
		Color p\r, p\g, p\b
		If (p\r < 0) Or (p\y > maxy)
			Delete p
		Else
			Oval p\x, p\y, p\size, p\size
		EndIf
	Next
End Function