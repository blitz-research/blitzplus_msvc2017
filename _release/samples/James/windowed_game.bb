
; -----------------------------------------------------------------------------
; Example quick-start "windowed-game with timing" template...
; -----------------------------------------------------------------------------
; james @ hi-toro.com
; -----------------------------------------------------------------------------

; -----------------------------------------------------------------------------
; Event constants...
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

; -----------------------------------------------------------------------------
; Create a window...
; -----------------------------------------------------------------------------

; FLAGS...

; 1  : window has titlebar 
; 2  : window is resizable 
; 4  : window has a menu 
; 8  : window has a status bar 
; 16 : window is a 'tool' window - smaller title bar etc. 
; 32 : window uses size given for *client* area (inner area)...

window = CreateWindow ("Game Window (use cursors)...", 0, 0, 640, 480)

; -----------------------------------------------------------------------------
; Create a menu...
; -----------------------------------------------------------------------------

; Note that the & sign in these examples underlines the next letter, and a
; blank menu name creates a 'bar'...

mainmenu = CreateMenu ("&Game", 0, WindowMenu (window))
CreateMenu "&About", 1, mainmenu
CreateMenu "", 2, mainmenu
CreateMenu "E&xit", 3, mainmenu

UpdateWindowMenu window ; This MUST be called after creating your menu!

; -----------------------------------------------------------------------------
; Create a drawing area and 'pin' all edges to parent window...
; -----------------------------------------------------------------------------

canvas = CreateCanvas (0, 0, ClientWidth (window), ClientHeight (window), window)
SetGadgetLayout canvas, 1, 1, 1, 1 ; Use zero to leave an edge 'unpinned'...

; -----------------------------------------------------------------------------
; All future drawing goes into the drawing area...
; -----------------------------------------------------------------------------

SetBuffer CanvasBuffer (canvas)

; -----------------------------------------------------------------------------
; Frame timing stuff...
; -----------------------------------------------------------------------------

fps = CreateTimer (60)

; -----------------------------------------------------------------------------
; Main loop...
; -----------------------------------------------------------------------------

Repeat

	; -------------------------------------------------------------------------
	; Event processing loop (repeats until 'fps' timer event occurs)...
	; -------------------------------------------------------------------------
	
	Repeat
	
		e = WaitEvent (1) ; The 'wait' value comes from above section...

		; ---------------------------------------------------------------------
		; Got events, so process them...
		; ---------------------------------------------------------------------
		
		Select e

			; Close gadget hit...		
			Case EVENT_Close
				End
			
			; Menu item selected...
			Case EVENT_Menu
			
				; -------------------------------------------------------------
				; After an EVENT_Menu event, you get the menu item number from
				; EventData ()...
				; -------------------------------------------------------------
				
				item = EventData ()
				
				Select item
					Case 1
						Notify "Game template"
					Case 3
						If Confirm ("Are you sure you want to quit?")
							End
						EndIf
				End Select
				
		End Select
		
	Until e = EVENT_Timer

	; -------------------------------------------------------------------------
	; Drawing code example...
	; -------------------------------------------------------------------------
	
	Cls

	For a = 1 To 100
		Color Rnd (100, 255), Rnd (100, 255), Rnd (100, 255)
		Rect Rand (640), Rand (480), 64, 64
	Next
	
	Color 255, 255, 255

	If KeyDown (203)
		x = x - 2
	EndIf

	If KeyDown (205)
		x = x + 2
	EndIf
	
	If KeyDown (200)
		y = y - 2
	EndIf

	If KeyDown (208)
		y = y + 2
	EndIf

	Rect x, y, 64, 64

	; -------------------------------------------------------------------------
	; The Canvas version of Flip...
	; -------------------------------------------------------------------------
			
	FlipCanvas canvas

Until (KeyHit (1))
	
End

