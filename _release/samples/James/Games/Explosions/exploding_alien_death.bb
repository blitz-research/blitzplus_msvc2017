
; -----------------------------------------------------------------------------
; Explosions demo...
; -----------------------------------------------------------------------------

; Not the cleanest code you'll ever see, but gives some idea of what
; you can do with BlitzPlus!

; Objective: Get to the highest level you can before you get bored!

; -----------------------------------------------------------------------------
; james @ hi - toro . com
; -----------------------------------------------------------------------------

; -----------------------------------------------------------------------------
; Event definitions...
; -----------------------------------------------------------------------------

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

; Badly commented example, just for show...

Type Alien
	Field x
	Field y
	Field xs
	Field ys
	Field image
End Type

Type PShot
	Field x
	Field y
	Field image
End Type

Type AShot
	Field x
	Field y
	Field image
End Type

Include "explosions_include.bb"

w = 800: h = 600 ;w = 640: h = 480

Function CenterWindow (title$, width, height, group = 0, style = 0)
	Return CreateWindow (title$, (ClientWidth (Desktop ()) / 2) - (width / 2), (ClientHeight (Desktop ()) / 2) - (height / 2), width, height)
End Function

Global window = CenterWindow ("Explosions -- Use LEFT/RIGHT CURSORS plus SPACE", w, h)

menu = CreateMenu ("&Game", 0, WindowMenu (window))
CreateMenu ("E&xit", 1, menu)
UpdateWindowMenu window

Global canvas = CreateCanvas (0, 0, ClientWidth (window), ClientHeight (window) - 25, window)
SetBuffer CanvasBuffer (canvas)
SetGadgetLayout canvas, 1, 1, 1, 1

Global button = CreateButton ("Play again...", 0, ClientHeight (window) - 25, ClientWidth (window), 25, window)
SetGadgetLayout button, 1, 1, 0, 1
DisableGadget button

ClsColor 64, 64, 128

InitExplosions (255, 0, 255)

Global player = LoadImage ("rocket.bmp")
MaskImage player, 255, 0, 255
Global rocket.RenderedExplosion = RenderExplosion (player, 4, 4)

pshot = LoadImage ("pshot.bmp")
MaskImage pshot, 255, 0, 255

enemy = LoadImage ("ufo.bmp")
MaskImage enemy, 255, 0, 255

enemydeath = LoadImage ("ufodeath.bmp")
MaskImage enemydeath, 255, 0, 255

; Loaded an 'explosion' image of the same size as the UFO, which will be
; triggered at the same time as the UFO explosion...

Global alien.RenderedExplosion = RenderExplosion (enemy, 16, 16)
Global fierydeath.RenderedExplosion = RenderExplosion (enemydeath, 8, 8)

SetBuffer CanvasBuffer (canvas)

Global aliencount

Global plx#, ply#, plxs#, plxy#

ply = ClientHeight (window) - 100

Global dead

HidePointer canvas

numStars = 100

Dim starx (numStars), stary (numStars)

For stars = 1 To numStars
	starx (stars) = Rnd (w)
	stary (stars) = Rnd (h)
Next

ticks = CreateTimer (60)

Global gw = GadgetWidth (canvas)
Global gh = GadgetHeight (canvas)

Repeat

	Repeat
		e = WaitEvent ()
		Select e
			Case EVENT_Close
				End
			Case EVENT_Gadget
				If dead
					If EventSource () = button
						plx = gw / 2
						dead = 0
						DisableGadget button
						HidePointer canvas
						FlushKeys
						morealiens = 0
						Delete Each Alien
						aliencount = 0
					EndIf
				EndIf
			Case EVENT_Menu
				Select EventData ()
					Case 1
						End
				End Select
		End Select
		
	Until e = EVENT_Timer
	
	Cls

	If Not dead
		If KeyDown (203) Then plx = plx - 4
		If KeyDown (205) Then plx = plx + 4
		If KeyHit (57) Then PShoot (pshot, plx + 18, ply - 16)
		DrawImage player, plx, ply
	EndIf

	For stars = 1 To numStars
		Color Rnd (220, 255), Rnd (220, 255), Rnd (220, 255)
		Plot starx (stars), stary (stars)
		starx (stars) = starx (stars) + 1: If starx (stars) > w Then starx (stars) = 0
		stary (stars) = stary (stars) + 1: If stary (stars) > h Then stary (stars) = 0
	Next
	
	UpdateAliens ()	
	UpdatePShots ()
	UpdateExplosions ()
	
	If aliencount = 0 Then morealiens = morealiens + 1: CreateAliens (morealiens, enemy)

	Text 20, 20, "LEVEL: " + morealiens
	
	FlipCanvas canvas

Until KeyHit (1)

End

Function PShoot (image, x, y)
	s.PShot = New PShot
	s\x = x
	s\y = y
	s\image = image
End Function

Function AShoot (image, x, y)
	s.AShot = New AShot
	s\x = x
	s\y = y
	s\image = image
End Function

Function UpdatePShots ()
	For s.PShot = Each PShot
		s\y = s\y - 8
		DrawImage s\image, s\x, s\y
		
		If s\y < 0
			Delete s
		Else
			For a.Alien = Each Alien
				If ImagesCollide (s\image, s\x, s\y, 0, a\image, a\x, a\y, 0)
					StartRenderedExplosion (alien, a\x, a\y, a\xs, a\ys, 0)
					StartRenderedExplosion (fierydeath, a\x, a\y, a\xs, a\ys, 0.1)
					Delete a
					Delete s
					aliencount = aliencount - 1
					Exit
				EndIf
			Next
		EndIf
		
	Next
End Function

Function UpdateAliens ()
	For a.Alien = Each Alien
		a\x = a\x + a\xs
		a\y = a\y + a\ys
		If a\x < 0 Then a\xs = -a\xs
		If a\y < 0 Then a\ys = -a\ys
		If a\x > gw Then a\xs = -a\xs
		If a\y > gh Then a\ys = -a\ys
		DrawImage a\image, a\x, a\y
		If dead = 0
			If ImagesCollide (a\image, a\x, a\y, 0, player, plx, ply, 0)
				StartRenderedExplosion (rocket, plx, ply, a\xs, -Abs (a\ys))
				dead = 1
				EnableGadget button
				ShowPointer canvas
			EndIf
		EndIf
	Next
End Function

Function CreateAliens (num, image)
	For n = 1 To num
		a.Alien = New Alien
		a\x = Rand (0, gw - 1)
		a\y = Rand (0, gh / 2)
		a\xs = Rnd (-8, 8)
		a\ys = Rnd (-4, 4)
		a\image = image
		aliencount = aliencount + 1
	Next
End Function

