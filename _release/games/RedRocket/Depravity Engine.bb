
; -----------------------------------------------------------------------------
; Depravity Engine - last updated 7 September 2000
; -----------------------------------------------------------------------------
; Public Domain (james@thesurfaces.net)
; -----------------------------------------------------------------------------
; No credits or permission needed for use of this engine!
; -----------------------------------------------------------------------------

; This is the basic engine I'm using for a game I'm writing, tidied up a litte
; and put in the Public Domain for anyone to use for any purpose whatsoever.

; Don't try and understand it, cos I don't!

; I don't recommend trying to hack it into existing code either! Just alter
; the images used and build your stuff into it as you see fit (see QUICK START,
; below)...

; Known problems:
; ---------------

; Physics not totally authentic! If anyone improves it, send me a copy, will ya? :)

; Image wrapping not quite right when you're in a corner of the screen, but this is
; not generally noticeable for "average size" rockets!

; -----------------------------------------------------------------------------
; QUICK START:
; -----------------------------------------------------------------------------
; Change image$ to whatever image you want to use;
; Change number of players (remember to set their keys though!);
; Scroll down to the main loop and add scenery, etc, as per (simple) instructions!
; -----------------------------------------------------------------------------

; Depravity Engine?! Depravity rhymes with gravity and sounds cool, that's all!

; -----------------------------------------------------------------------------
; Global and constant variables (and various other set-up stuff):
; -----------------------------------------------------------------------------

AppTitle "Depravity Engine"							; Windows taskbar title

Const sw				= 1024						; Screen width
Const sh				= 768						; Screen height

Graphics sw, sh										; Open display
SetBuffer BackBuffer ()								; ALL graphics commands work on hidden buffer [ BackBuffer () ]

Global players			= 1							; Number of players
image$					= "redrocketup.bmp"			; Image of upward-pointing rocket

; Remember to change the MaskImage settings further down if you load your own image here!

; -----------------------------------------------------------------------------
; Player keys
; -----------------------------------------------------------------------------

Dim ukey (players)									; Up (boost) key for each player
Dim lkey (players)									; Left key for each player
Dim rkey (players)									; Right key for each player

; Some example keys (need to be defined for however many players you have):

; Player 1:				
ukey (1)				= 200						; Up cursor
lkey (1)				= 203						; Left cursor
rkey (1)				= 205						; Right cursor

; Player 2:
;ukey (2)				= 16						; q		}
;lkey (2)				= 37						; k		} Don't ask! ;)
;rkey (2)				= 38						; l		}

; -----------------------------------------------------------------------------
; Gravity, inertia and friction hackery
; -----------------------------------------------------------------------------

; Gravity, inertia, friction constants. WARNING!!! Because I don't know what I'm doing,
; messing with these constants can really screw things up! At least make a commented-out
; copy before touching them!

; Example "outer space" settings:

;Global gravity#		= 0
;Global maxxacc#		= 15
;Global maxyacc#		= 15
;Global yfriction#		= 0
;Global xfriction#		= 0
;Global xacceleration#	= 0.2
;Global yaccelerationu#	= 0.2
;Global yaccelerationd#	= 0.2
;Global downlimit#		= 1

; Uncomment the block above and comment out the block below to try outer space!

Global gravity#			= 0.04						; Gravity applied (also friction -- told you this wasn't real physics!)
Global maxxacc#			= 15						; Max horizontal acceleration
Global maxyacc#			= 10						; Max vertical acceleration (basically, sets terminal velocity)
Global yfriction#		= 0.1						; Vertical friction applied when dropping down
Global xfriction#		= 0.02						; Horizontal friction applied
Global xacceleration#	= 0.2						; How fast we boost in the x direction (mixed with Sin/Cos though ;)
Global yaccelerationu#	= 0.2						; Upwards boosting (again, with Sin and Cos applied)
Global yaccelerationd#	= 0.15						; Downwards boosting (less applied because of gravity! Sin and Cos, blah blah blah...)
Global downlimit#		= 1.5						; Max downwards acceleration (gets multiplied by maxxyacc...Sin/Cos)

; Explanation of gravity stuff:

; gravity			: Applied each frame to the y position of each rocket
; maxxacc			: This is the limit of acceleration in the x direction
; maxyacc			: This is the limit of acceleration in the y direction
; yfriction			: Air resistance as rocket falls (air resistance upwards is covered in the gravity value!)
; xfriction			: Air resistance in the x direction (ie. you'll gradually go across less-and-less)
; xacceleration		: How much boost is applied in the x direction
; yaccelerationu	: How much boost is applied when pointing upwards
; yaccelerationd	: How much boost is applied when going down (honk). This lets us go faster than the pull of gravity when pointing downwards (which is checked by air resistance when we stop boosting down!). What a long description!
; downlimit			: maxxyacc is increased (multiplied by) this amount when pointing down, to allow us to go faster than the pull of gravity (see line above)

; Like I say, best not to touch it, cos Nasty Things (tm) can happen! No, WILL happen!
; Fixed physics welcome, as long as they take ALL of the above parameters/situations into account! :)

; -----------------------------------------------------------------------------
; "Advanced Rocket Buildery 101"
; -----------------------------------------------------------------------------

Global totalframes		= 64						; Number of rotation frames
Dim myimage (totalframes)							; Array of images

AutoMidHandle True									; Centre future image handles
myimage (0)				= LoadImage (image$)		; Rocket image, defined at top!

Const rocketredmask		= 75						; Red, green and blue values
Const rocketgreenmask	= 170						; of rocket image's background
Const rocketbluemask	= 250						; (will be transparent)

; Set image transparency mask accoring to above values:
MaskImage myimage (0), rocketredmask, rocketgreenmask, rocketbluemask

; -----------------------------------------------------------------------------
; Create rocket objects
; -----------------------------------------------------------------------------

Type Ship											; Ship object, holds rocket data (duh)
	Field x#										; Rocket's x position
	Field y#										; Rocket's y position
	Field shipxacc#									; Rocket's horizontal acceleration
	Field shipyacc#									; Rocket's vertical acceleration
	Field frame										; Rocket's current frame
End Type

Dim rocket.Ship (players)							; Array of players' rockets

For s = 1 To players								; One for each
	rocket.Ship (s)		= New Ship					; Create new rocket object
	rocket (s)\x 		= Rnd (sw)					; x position on screen
	rocket (s)\y 		= 0							; y position on screen
	rocket (s)\shipxacc = 0							; x acceleration
	rocket (s)\shipyacc = 0							; y acceleration
	rocket (s)\frame	= 0							; Current image frame
Next

; -----------------------------------------------------------------------------
; Render images
; -----------------------------------------------------------------------------

; This section renders the full 360 degrees of rocket images, using the
; global variable "totalframes", defined above, for the number of frames
; to render. It also pre-calculates the trigonometry stuff used later...

; You don't have to read this ;)

Global rotationangle# 	= 0							; Used to work out rocket's angle
Global totalframestep# 	= 360.0/totalframes			; ...along with this...also for rendering

astep# = Float (360.0) / (totalframes)				; Used below for pre-calc'ing angles

totalframes = totalframes - 1						; Accounts for frame 0

For frame = 1 To totalframes						; Render frames
	myimage (frame) = CopyImage (myimage (0))		; Copy loaded image
	rotationangle = rotationangle + totalframestep	; Next frame
	TFormFilter False								; No dithering!
	RotateImage myimage (frame),rotationangle		; Rotate the copy by whatever
Next

Dim shipheight (totalframes)						; Pre-calculate sizes of...
Dim shipwidth  (totalframes)						; ...all frames (probably no speed increase, but I haven't checked ;)

Dim tx# (totalframes), ty# (totalframes)			; Used later for acceleration calculation

For b = 0 To totalframes							; Doing 2 jobs at once here!
	shipwidth  (b) = ImageWidth  (myimage (b))		; Width of frame
	shipheight (b) = ImageHeight (myimage (b))		; Height of frame
	rotationangle# = b * astep						; Pre-calc angles
	tx# (b) = Sin (rotationangle)					; Pre-calc trig stuff (Sin)
	ty# (b) = Cos (rotationangle)					; Pre-calc trig stuff (Cos)
Next

; -----------------------------------------------------------------------------
; Play! This is our main loop...
; -----------------------------------------------------------------------------

Repeat

	Cls												; Clear hidden buffer

	CheckPlayers ()									; Get keypresses, do stuff

	PositionRocket ()								; Calculate rockets' positions
	OhNatureThouArtACruelMistressOrSomething ()		; Apply friction and gravity

; -----------------------------------------------------------------------------
; Draw stuff behind rocket
; -----------------------------------------------------------------------------
; This is where you add background scenery and anything else that goes
; behind the rocket(s). For example, if you've previously loaded a background
; image with " background = LoadImage ("background.bmp") ", you'd just put
; " DrawImage background, 0, 0 " in here.
; -----------------------------------------------------------------------------

	DrawRockets ()									; Draw rockets

; -----------------------------------------------------------------------------
; Draw stuff in front of rocket
; -----------------------------------------------------------------------------
; This is where you add foreground scenery and anything else that goes
; in front of the rocket(s).
; -----------------------------------------------------------------------------
	
	Flip											; Show result

Until KeyDown (1) = 1								; [ESC] key quits game

End

; -----------------------------------------------------------------------------
; Functions used
; -----------------------------------------------------------------------------

; Checks keypresses for all players, calls appropriate routines:

Function CheckPlayers ()

	For player = 1 To players

		If KeyDown (lkey (player))					; Left
			rocket (player)\frame = rocket (player)\frame - 1
			If rocket (player)\frame < 0 Then rocket (player)\frame = totalframes
		EndIf

		If KeyDown (rkey (player))					; Right
			rocket (player)\frame = rocket (player)\frame + 1
			If rocket (player)\frame > totalframes Then rocket (player)\frame = 0
		EndIf

		If KeyDown (ukey (player))					; Up
			Power (player)
		EndIf

	Next

End Function

; -----------------------------------------------------------------------------

; Draws all rockets with appropriate frames:

Function DrawRockets ()

	For a.Ship = Each Ship
		RocketWrap (a)
		DrawImage myimage (a\frame), a\x, a\y
	Next

End Function

; Apply velocities to rocket positions:

Function PositionRocket ()

	For a.Ship = Each Ship
		a\x = a\x + a\shipxacc
		If weather Then a\x = a\x + wind			; Raining or snowing? Windy too, then!
		a\y = a\y + a\shipyacc
	Next

End Function

; -----------------------------------------------------------------------------

; Work out x and y velocities when boost key pressed (uses pre-calc'd angles):

Function Power (player)

		If tx (rocket (player)\frame) > 0
			If rocket (player)\shipxacc < maxxacc
				rocket (player)\shipxacc = rocket (player)\shipxacc + xacceleration * tx (rocket (player)\frame)	; Right
			EndIf
		Else
			If rocket (player)\shipxacc > -maxxacc
				rocket (player)\shipxacc = rocket (player)\shipxacc + xacceleration * tx (rocket (player)\frame)	; Left
			EndIf
		EndIf

		If ty (rocket (player)\frame) > 0
			If rocket (player)\shipyacc > -maxyacc
				rocket (player)\shipyacc = rocket (player)\shipyacc - yaccelerationu * ty (rocket (player)\frame)	; Up   (ty is positive)
			EndIf
		Else
			If rocket (player)\shipyacc < maxyacc * downlimit
				rocket (player)\shipyacc = rocket (player)\shipyacc - yaccelerationd * ty (rocket (player)\frame)	; Down (ty is negative)
			EndIf
		EndIf

End Function

; -----------------------------------------------------------------------------

; Gradually applies "friction" and gravity to rocket's x and y velocities:

Function OhNatureThouArtACruelMistressOrSomething ()

	For a.Ship = Each Ship
		If a\shipxacc > 0 Then a\shipxacc = a\shipxacc - xfriction
		If a\shipxacc < 0 Then a\shipxacc = a\shipxacc + xfriction
		If a\shipyacc < maxyacc Then a\shipyacc = a\shipyacc + gravity
		If a\shipyacc > maxyacc Then a\shipyacc = a\shipyacc - yfriction
	Next

End Function

; -----------------------------------------------------------------------------

; Wrap rocket round edges of screen:

Function RocketWrap (a.Ship)

		If a\x + shipwidth (a\frame) > sw
			DrawImage myimage (a\frame), a\x - sw, a\y
		EndIf

		If a\x > sw
			a\x = 0
		EndIf

		If a\x - (shipwidth (a\frame) / 2) < 0
			DrawImage myimage (a\frame), sw+a\x, a\y
		EndIf

		If a\x + shipwidth (a\frame) < 0
			a\x = sw - shipwidth (a\frame)
		EndIf

		If a\y + shipheight (a\frame) > sh
			DrawImage myimage (a\frame), a\x, a\y - sh
		EndIf

		If a\y > sh
			a\y = 0
		EndIf

		If a\y - (shipheight (a\frame) / 2) < 0
			DrawImage myimage (a\frame), a\x, sh+a\y
		EndIf

		If a\y + shipheight (a\frame) < 0
			a\y = sh - shipheight (a\frame)
		EndIf

; It's not 100% perfect -- if you get a rocket balancing at the corner of the
; screen, so it appears in all four corners, you get a little jump, but most
; people won't ever see it!

End Function