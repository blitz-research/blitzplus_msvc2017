;********************************
;* A S T E R O I D  S H O W E R *
;********************************
;*   Example game for BlitzPC   *
;********************************
;* Coding by Tracer             *
;* Email: tracerbb@hotmail.com  *
;********************************
;* Last change: 15-09-2000      *
;* Verified 1.48: 4/18/2001     *
;********************************

; This game is very simple, not very addictive and such :) As an example game for Blitz it serves a good
; purpose. The code is in some places not very good sadly, i will be updating this so be on the
; look out for new versions!. Have fun!

AppTitle "Asteroid Shower"									; Attach a name to the application.

Graphics 640,480											; Set graphics mode.

SetBuffer BackBuffer()										; Turn on double buffering.

; Load sound.
Global kaboom = LoadSound("explsion.wav")
Global launch = LoadSound("missile.wav")

; Load graphics.
Global stars1 = LoadImage("stars1.bmp")						; Load layer of stars.
Global stars2 = LoadImage("stars2.bmp")						; Load layer of stars.
Global stars3 = LoadImage("stars3.bmp")						; Load layer of stars.
Global stars4 = LoadImage("stars4.bmp")						; Load layer of stars.
Global rocket = LoadImage("rocket.bmp")						; Load player craft.
Global firebt = LoadImage("bullet.bmp")						; Load the bullet.
Global fireb2 = LoadImage("bullet2.bmp")					; Load the reverse bullet.
Global rock1  = LoadImage("rock1.bmp")						; Load an asteroid.
Global rock2  = LoadImage("rock2.bmp")						; Load an asteroid.
Global rock3  = LoadImage("rock3.bmp")						; Load an asteroid.
Global rock4  = LoadImage("rock4.bmp")						; Load an asteroid.
Global rock5  = LoadImage("rock5.bmp")						; Load an asteroid.
Global scores = LoadImage("score.bmp")						; Load the 'score' picture.
Global energi = LoadImage("energy.bmp")						; Load the 'energy' picture.
Global rlife  = LoadImage("rlife.bmp")						; Load the little rocket.
Global life   = LoadImage("lives.bmp")						; Load the 'lives' picture.
Global expl   = LoadAnimImage("explode.bmp",30,24,0,6)		; Load the explosion animation.
Global nums   = LoadAnimImage("nums.bmp",10,11,0,10)		; Load the numbers used to show score.
Global powup1 = LoadAnimImage("powerup1.bmp",20,20,0,18)	; Load a power-up.
Global powup2 = LoadAnimImage("powerup2.bmp",20,20,0,18)	; Load a power-up.

; Globals for in-game background.
Global starfield											; Needed for movement of stars.

;Globals for player.
Global rx	= 0												; Player X location.
Global ry = 218												; Player Y location.
Global score = 0											; Total score of the player.
Global energy	= 100										; Player energy.
Global lives = 3											; Amount of lives left.
Global death = False										; Boolean used to see if player is dead.
Global deathtimer											; Needed for explosions upon death.
Global damtimer												; Needed for explosions because of damage.
Global shotlevel = 1										; Determines the amount of gun power-ups collected.
Global difficulty = 2										; Holds the set difficulty.
Global rocks_percent
Global diff_cnt

; Globals for main menu.
Global no_stars = 200										; Amount of stars used in the starfield.
Global sx#													; Speed X for stars.
Global sy#													; Speed Y for stars.
Global tln													; Character counter for scrolling text.
Global txt$													; Holds the scrolling text.
Global scx													; X location of scrolling text.

; Globals for hiscores.
Global name$												; Used to store players name after hiscore entry.

; Globals for keyboard input.
Global upkey												; Holds the scancode of the key to go up.
Global downkey												; Holds the scancode of the key to go down.
Global leftkey												; Holds the scancode of the key to go left.
Global rightkey												; Holds the scancode of the key to go right.
Global firekey												; Holds the scancode of the key to fire a bullet.
Global bombkey												; Holds the scancode of the key to launch a bomb.
Global quitkey												; Holds the scancode of the key to quit the game.

; Globals for the 3d.
Global numpoints = 44										; Number of points in the point table.
Global numconn   = 33										; Number of connections in the connect table.
Global distance  = 400										; Needed for perspective.
Global vx#													; X location.
Global vy#													; Y location.
Global vz#													; Z location.

; Arrays used by 3d code.
Dim points(numpoints, 3)									; Holds the point locations of the game over 3d.
Dim rotated(numpoints, 2)									; Holds rotated points for the 3d.
Dim connect(numconn, 2)										; Holds the connections of the 3d.

; Arrays used by hiscore code.
Dim hiscore$(10,2)											;Holds the hiscore table (1 = names, 2=scores).
Dim hiname$(12)												;Needed for entering a name into the table.

; Arrays for 3d startfield behind menu.
Dim x#(no_stars) 											;Array for 3d starfield X locations.
Dim y#(no_stars)											;Array for 3d starfield Y locations.

; Initialization code for scroller.
txt$  = "HEY AND WELCOME TO * ASTEROID SHOWER *, A GAME BY TRACER. GREETZ FLY TO MARK, GEORGE, JAMES AND THE REST! WRAP  "
tln = Len(txt$)
Global scroller = CreateImage(tln * 32,32)

; Initialization code for credit scroller.
Global creditlen = 45
Global credscrll = CreateImage(640,creditlen * 20)
Dim credit$(creditlen)
;               01234567890123456789012345678901
credit$(0)   = "          @^^^^^^^^^#          "
credit$(1)   = "          & CREDITS &          "
credit$(2)   = "          {^^^^^^^^^}          "
credit$(3)   = "                               "
credit$(4)   = "          PROGRAMMING          "
credit$(5)   = "          ^^^^^^^^^^^          "
credit$(6)   = "                               "
credit$(7)   = "            TRACER             "
credit$(8)   = "                               "
credit$(9)   = "                               "
credit$(10)  = "           GRAPHICS            "
credit$(11)  = "           ^^^^^^^^            "
credit$(12)  = "                               "
credit$(13)  = "            TRACER             "
credit$(14)  = "                               "
credit$(15)  = "                               "
credit$(16)  = "        MUSIC AND SOUND        "
credit$(17)  = "        ^^^^^^^^^^^^^^^        "
credit$(18)  = "                               "
credit$(19)  = "        VARIOUS  PEOPLE        "
credit$(20)  = "                               "
credit$(21)  = "                               "
credit$(22)  = "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^"
credit$(23)  = " ASTEROID SHOWER IS WRITTEN IN "
credit$(24)  = "    BLITZ BASIC FOR THE PC.    "
credit$(25)  = "                               "
credit$(26)  = "       CHECK IT OUT AT :       "
credit$(27)  = "                               "
credit$(28)  = "      WWW.BLITZBASIC.COM       "
credit$(29)  = "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^"
credit$(30)  = "                               "
credit$(31)  = "                               "
credit$(32)  = "       @^^^^^^^^^^^^^^^#       "
credit$(33)  = "       & GREETZ GO TO: &       "
credit$(34)  = "       {^^^^^^^^^^^^^^^}       "
credit$(35)  = "                               "
credit$(36)  = "MARK SIBLY, GEORGE BRAY, JAMES "
credit$(37)  = "   L BOYD, WARPY, DREAMSOFT,   "
credit$(38)  = "  RICHARDF, RICHARDK, JAVAKID, "
credit$(39)  = " SI, THE BAMBER BROTHERS, JAW, "
credit$(40)  = "ASMPROGRAMMER, WAGBERT, RAIAK, "
credit$(41)  = "    REQUIEM, BARAK, HUBDULE    "
credit$(42)  = "  AND ALL THE FOLKS I FORGOT!  "
credit$(43)  = "                               "
credit$(44)  = "         TIME TO WRAP!         "

; Bullet structure
Type bullet
	Field x,y,tp
End Type

; Structure for the rocks
Type rock
	Field x,y,tp,sp
End Type

; Explosion structure
Type explode
	Field x,y,frame
End Type

; Powerup structure
Type powerup
	Field x,y,tp,sp,f
End Type

; Read data for 3d.
Restore points
For n = 1 To numpoints
	Read x3d, y3d, z3d
	points(n, 1) = x3d
	points(n, 2) = y3d
	points(n, 3) = z3d
Next
Restore conns
For n = 1 To numconn
	Read a3d, b3d
	connect(n, 1) = a3d
	connect(n, 2) = b3d
Next 

load_hiscores()														; Load hiscores file from disk.
intro()																; Show intro screen.
main_menu()															; Show the main menu.

; The below function contains the actual game loop.
Function game()
	FlushKeys()
	While Not KeyHit(1)											; Pressing 'Esc' will quit to the menu.
		update_background()											; Draw starfield background.
		update_player()												; Draw player rocket and read keys.
		update_bullet()												; Move bullets and check for collisions.
		update_rock()												; Update the on screen rocks and create new ones.
		update_explode()											; Update any explosions on screen.
		update_info()												; Show info bar.
		update_score()												; Construct and show score.
		update_damage()												; Check damage on player and anim this.
		update_powerups()											; Anim any powerups on screen.
		update_death()												; Check is player died and do anims.
		Flip														; Swap screens.
		Cls															; Clear the screen.
	Wend
	main_menu()														; Go to main menu.
End Function

; This function is the actual main menu.
Function main_menu()
	title = LoadAnimImage("logo.bmp",522,1,0,145)					; Load logo (in pieces for disting).
	menu  = LoadAnimImage("menu.bmp",301,20,0,8)					; Load the menu itself.
	make_stars()													; Generate some stars for the starfield.
	make_scroller()													; Generate the scroller.
	sel = 1															; Set menu item 1 as the start item.
	go = False														; The game is only started when this is true.
	For k=1 To 255													; Clear keyboard buffer.
		KeyHit(key)
	Next									

	FlushKeys()
	While Not go = True
		move_stars()												; draw starfield.
		; The routine below wobbles the logo and draws it aswell.
		For tc = 0 To 144
			part2 = 1
			part3 = 50
			DrawImage title,52 + Sin(cnt - (tc * part2)) * part3,10 + tc,tc
		Next
		cnt = cnt + 1
		; The below select looks at the selected menu item and
		; makes it blink.
		Select sel
			Case 1
				If red = True
					DrawImage menu,170,200,0
				Else
					DrawImage menu,170,200,1
				EndIf
				DrawImage menu,170,240,2
				DrawImage menu,170,280,4
				DrawImage menu,170,320,6
			Case 2
				DrawImage menu,170,200,0
				If red = True
					DrawImage menu,170,240,3
				Else
					DrawImage menu,170,240,2
				EndIf
				DrawImage menu,170,280,4
				DrawImage menu,170,320,6
			Case 3
				DrawImage menu,170,200,0
				DrawImage menu,170,240,2
				If red = True
					DrawImage menu,170,280,5
				Else
					DrawImage menu,170,280,4
				EndIf
				DrawImage menu,170,320,6
			Case 4
				DrawImage menu,170,200,0
				DrawImage menu,170,240,2
				DrawImage menu,170,280,4
				If red = True
					DrawImage menu,170,320,7
				Else
					DrawImage menu,170,320,6
				EndIf
		End Select
		If KeyHit(200)											; Check for key up.
			sel = sel - 1										; Move selected item one up.
			If sel < 1 Then sel = 4								; Moving up from top item goes to last item.
		EndIf
		If KeyHit(208)											; Check for down key.
			sel = sel + 1										; Move selected item one down.
			If sel > 4 Then sel = 1								; Moving down from the last item goes to the top item.
		EndIf

		If KeyHit(28)											; Check for 'return' key.
			Select sel											; What item was selected?
				Case 1
					Select difficulty							; Set difficulty of game.
						Case 1
							rocks_percent = 90
						Case 2
							rocks_percent = 70
						Case 3
							rocks_percent = 50
						Case 4
							rocks_percent = 30
					End Select
					go = True									; Start the game!
				Case 2
					options()									; Go to the options screen.
				Case 3
					show_hiscores()								; Show the highest scores.
				Case 4
					credits()									; Show the credits.
					End											; Quit!
			End Select
		EndIf
		; The routine below makes the selected menu
		; item blink red.
		If flashtimer = 20
			If red = True
				red = False
			Else
				red = True
			EndIf
			flashtimer =0
		EndIf
		flashtimer = flashtimer + 1
		
		show_scroller()											; Display the scroller.

		dummy = MouseX()										; For the cheapo/old gfx cards.. removes the mouse.
		Flip
		Cls
	Wend
	get_ready()													; Show the amazing get ready screen.
	game()														; go to the game.
End Function

; This function puts the stars at random screen positions.
Function make_stars()
	sx#=1.03985													; X speed of stars.
	sy#=1.03985													; Y speed of stars.
	For i = 1 To no_stars										; Place the stars random on screen locations.
		x#(i) = Rnd(640.0)-320.0
		y#(i) = Rnd(480.0)-240.0
	Next 
End Function


; This function actually displays and moves the stars
; on the screen.
Function move_stars()
	For j = 1 To no_stars
		Color 155,155,155
		Rect x#(j) + 320.0,y#(j) + 240.0,2,2					; Draw the stars.
		x#(j) = x#(j) * sx#										; Move the stars in the X.
		y#(j) = y#(j) * sy#										; Move the stars in the Y.
		If x#(j) > 640.0 Then x#(j)=x#(j)-640.0					; Star off screen? place it back.
		If y#(j) > 480.0 Then y#(j)=y#(j)-480.0
		If x#(j) < -640.0 Then x#(j)=x#(j)+640.0
		If y#(j) < -480.0 Then y#(j)=y#(j)+480.0
	Next
End Function

; This function makes the scroller for the main menu.
; It sees what character is used in the scroller and
; pastes a graphical version of it on a custom screen.
Function make_scroller()
	font = LoadAnimImage("font.bmp",32,32,0,60)					; Load the font.
	SetBuffer ImageBuffer(scroller)								; Switch drawing to the custom image.
	For tcn = 1 To tln
		c$ = Mid$(txt$,tcn,1)									; Extract a character from the string.	
		Select c$												; Check to see if it's one of the other chars.
			Case "?"
				ch = 31
			Case "("
				ch = 8
			Case ")"
				ch = 9
			Case "'"
				ch = 7
			Case ","
				ch = 12
			Case "-"
				ch = 13
			Case "."
				ch = 14
			Case "!"
				ch = 1				
			Case "*"
				ch = 3
			Case "@"
				ch = 2
			Default
				ch = Asc(c$)									; If not, get it's ascii code.
				ch = ch - 32									; make the ascii code into an image number.
		End Select
		If c$ <> " "											; Is it a space?
			DrawImage font,(tcn * 32) - 32,0,ch					; Draw the letter.
		EndIf
	Next
	SetBuffer BackBuffer()										; Switch back to drawing to the backbuffer.
End Function

; This function draws, moves and bounces the scroller.
Function show_scroller()
	sln = Len(txt$) * 32										; Get total length of the image.
	DrawImage scroller,650 - scx,448 - Abs(Sin(scx) * 90)		; Draw the scroller, move it and bounce it.
	scx = scx + 1												; Speed of scrolling.
	If scx > sln + 640											; End of text, end of screen? reset it.
		scx = 0
	EndIf
End Function

; This function makes the background of stars in the game
; move at four different speeds, giving a nice parallax
; effect.
Function update_background()
	TileImage stars1,0-starfield,0								; Draws the first stars layer (speed 1)
	TileImage stars2,0-starfield * 2,0							; Draws the second stars layer (speed 2)
	TileImage stars3,0-starfield * 3,0							; Draws the third stars layer (speed 3)
	TileImage stars4,0-starfield * 4,0							; Draws the fourth stars layer (speed 4)

	starfield = starfield + 1									; Move the stars.
End Function

; This function draws the players rocket
; and lets it be moved using the set keys.
Function update_player()
		DrawImage rocket,0 + rx,ry								; Draw Rocket.
	
		If death = False										; Check if player is dead.
			; up
			If KeyDown(200)
				ry = ry - 4
				If ry < 0
					ry = 0
				EndIf
			EndIf
			; down
			If KeyDown(208)
				ry = ry + 4
				If ry > (479 - 36)
					ry = 479 - 36
				EndIf
			EndIf
			; left
			If KeyDown(203)
			rx = rx - 4
				If rx < 0
					rx = 0
				EndIf
			EndIf
			; right
			If KeyDown(205)
				rx = rx + 4
				If rx > (639 - 99)
					rx = 639 - 99
				EndIf
			EndIf
			; fire
			If KeyHit(57)
				create_bullet()
			EndIf
		EndIf
End Function

; If space is pressed the program will call
; this function to create the bullets.
; There are right now 3 weapon levels, each is
; defined in here.
Function create_bullet()
	PlaySound launch
	If shotlevel = 1											; Check for weapon level.
		b.bullet = New bullet									; Create bullet in type.
		b\x = rx + 90											; X start position of bullet.
		b\y = ry + 14											; Y start position of bullet.
		b\tp = 1												; Type of bullet, determines which direction.
	EndIf
	If shotlevel = 2
		b.bullet = New bullet
		b\x = rx + 50
		b\y = ry + 0
		b\tp = 1
		b.bullet = New bullet
		b\x = rx + 50
		b\y = ry + 28
		b\tp = 1
	EndIf
	If shotlevel = 3
		b.bullet = New bullet
		b\x = rx + 90
		b\y = ry + 14
		b\tp = 1
		b.bullet = New bullet
		b\x = rx + 50
		b\y = ry + 0
		b\tp = 2
		b.bullet = New bullet
		b\x = rx + 50
		b\y = ry + 28
		b\tp = 3
	EndIf
End Function

; Update the bullets that are on screen.
Function update_bullet()
	For b.bullet = Each bullet									; Go through all the bullets.	
		b\x = b\x + 10											; Move it to the right of the screen.
		If b\tp = 2												; Shot type 2? move it upward as well.
			b\y = b\y - 4		
		EndIf
		If b\tp = 3												; Shot type 3? move it downward as well.
			b\y = b\y + 4
		EndIf
		If b\x < 650											; Draw as long as the bullet is on screen.
			DrawImage firebt,b\x,b\y
			For r.rock=Each rock								; Check all the rocks on screen (for collision)
				; This command actually checks all the rocks
				; and bullets to see if they have collided.
				; Test is only on one rock but check all rocks.
				; Not the best way, next version will do it
				; correctly!
				If ImagesCollide(firebt,b\x,b\y,0,rock1,r\x,r\y,0 )
					score = score + 10							; Increase score of player.
					update_score()								; And update it.
					PlaySound kaboom
					e.explode = New explode						; Create a new explosion.
					e\x = r\x									; X location of explosion.
					e\y = r\y									; Y location of explosion.
					a = Rnd(1,100)								; Take a random number for powerup creation.
					If a > 97									; Random number higher than 95?
	 					p.powerup = New powerup					; Create powerup.
						p\x  = r\x								; Powerup X start location.
						p\y  = r\y								; Powerup Y start location.
						p\tp = Rnd(1,2)							; Random to see what type of powerup.
						p\sp = r\sp								; Set moving speed of powerup to speed of rock.
						p\f  = 0								; Frame number start is 0.
					EndIf
					Delete r									; Delete the rock.
					Delete b									; Delete the bullet.
					Exit
				EndIf
			Next
		Else	
			Delete b											; Delete the bullet if it's off screen.
		EndIf
	Next 
End Function

; This function creates a new rock.
Function create_rock()
		r.rock = New rock										; Create rock.
		r\x  = Rnd(640,650)										; Random X location out of screen (not needed really).
		r\y  = Rnd(10,470)										; Random Y location.
		r\tp = Rnd(1,5)											; Which image to use for the rock?
		r\sp = Rnd(4,8) + 1										; Set speed of rock.
End Function

; This function checks all the rocks on screen
; and moves them as well. The difficulty of
; the game is increased every now and then
; here as well, remember that rocks_percent
; works the other way around.. the lower
; the more rocks.
Function update_rock()
	If diff_cnt = 500											; Counter this high?
		rocks_percent = rocks_percent - 2						; Increase difficulty a little.
		If rocks_percent <= 0									; Make sure we don't get negatives.
			rocks_percent = 0
		EndIf
		diff_cnt = 0											; Reset counter.
	EndIf			
	diff_cnt = diff_cnt + 1										; Increase counter.
	
	If Rnd(1,101) > rocks_percent								; Random number bigger than rock_percent?
		create_rock()											; Yep, add a rock.
	EndIf

	For r.rock = Each rock										; Go through each rock.
		r\x = r\x - r\sp										; Move the rock.
		; Check for a collision of the player with
		; a rock. Again, not the right way, will be
		; optimized.
  	    If ImagesCollide(rocket,rx,ry,0,rock1,r\x,r\y,0)
			energy = energy - 4									; Decrease energy of player.
			e.explode = New explode								; Create a new explosion.
			e\x = r\x											; Set X location of explosion to last rock X position.
			e\y = r\y											; Set Y location of explosion to last rock Y position.
			Delete r											; delete the rock.
			If energy <= 0										; No more energy for the player?
				death = True									; He is dead!
			EndIf
		EndIf
		If r <> Null											; Does the rock still exist?
			If r\x < -10										; Rock out of screen?
				Delete r										; Yes, delete it.
			Else				
				Select r\tp										; Select the rock type.
					Case 1										; Type 1?
						DrawImage rock1,r\x,r\y					; Draw rock with rock image 1.
					Case 2										; Etc.
						DrawImage rock2,r\x,r\y
					Case 3
						DrawImage rock3,r\x,r\y
					Case 4
						DrawImage rock4,r\x,r\y
					Case 5
						DrawImage rock5,r\x,r\y
					Default
						DrawImage rocket,r\x,r\y					
				End Select
			EndIf
		EndIf
	Next
End Function

; This function will make the player die if
; the energy is 0.
Function update_death()
	If death = True												; Is the player actually dead?
		For ex = 1 To 2											; We will create 2 random explosions each loop.
			e.explode = New explode								; Create the explosion.
			e\x = (rx - 10) + Rnd(100)							; Set the explosion X location randomly around the rocket.
			e\y = (ry - 10) + Rnd(36)							; Set the explosion Y location randomly around the rocket.
		Next
		deathtimer = deathtimer + 1								; Increase the timer for the nice dead thing.
		If deathtimer = 100										; Timer 100?
			lives = lives - 1									; Decrease lives by 1.
			If lives < 1										; No more lives?
				lowest = hiscore$(9,2)							; Get lowest hiscore.
				If score < lowest								; Score lower than that?
					game_over()									; 3d game over scene.
					show_hiscores()								; Show current hiscores.
					reset_game()								; Reset everything.
					main_menu()									; Go to the main menu.
				Else											; Score higher than lowest hiscore?
					game_over()									; 3d game over scene.
					enter_hiscore()								; Let the player enter his/hers name.
					reset_game()								; Reset the game variables.
					main_menu()									; Main menu.
				EndIf
			EndIf
			reset_game()										; Reset all variables.
			get_ready()											; Cool get ready screen.
		EndIf
	EndIf
End Function

; This function resets all the needed variables
; in the game.
Function reset_game()
	ry = 218
	rx = 0
	Delete Each explode
	Delete Each rock
	Delete Each bullet
	energy = 100
	death = False
	deathtimer = 0
	shotlevel = 1
End Function

; Shows the score, lives, energy during the game.
Function update_info()
	DrawImage scores,0,460										; Draw 'score' word.
	DrawImage life,260,460										; Draw 'lives' word.
	For lc = 1 To lives											; How many lives?
		DrawImage rlife,300 + (lc * 20),455						; Draw them.
	Next
	DrawImage energi,465,460									; Draw 'energy' word.
	If energy > 50												; Set colors for energy.
		Color 255,255,0
	EndIf
	If energy < 51 And energy > 25
		Color 246,124,4
	EndIf
	If energy < 26
		Color 255,0,0
	EndIf
	Rect 535,462,energy,10										; Draw the energy bar.
End Function

; This function will animate any explosions on screen.
Function update_explode()
	For e.explode = Each explode								; Go through all explosions.
		f = e\frame/3											; Slow animing a bit down.
		If f = 6												; All frames gone through?
			Delete e											; Delete the explosion.
		Else
			DrawImage expl,e\x,e\y,f							; Draw the explosion frame.
			e\frame = e\frame + 1								; Increase frame counter.
		EndIf
	Next
End Function

; Draw the score number in nice font.
Function update_score()
	scr$ = score												; Get the score into a string.
	lscr = Len(scr$)											; How long is the score?
	For scn = 1 To lscr
		si$ = Mid$(scr$,scn,1)									; Get a number.
		si = si$												; Make it an integer.
		DrawImage nums,50 + (scn * 10),460,si					; Draw the image version of the number.
	Next
End Function

; This function shows the nice little
; explosions when the player has lost
; more than 50% of his energy. Lower
; amounts of energy will show more
; little explosions.
Function update_damage()
	If energy < 50												; Less than 50% energy?
		If damtimer = (energy / 2)								; Create an explosion? (the lower energy,the bigger chance).
			e.explode = New explode								; Create explosion.
			e\x = (rx - 10) + Rnd(50)							; Place the X randomly at the backside of the rocket.
			e\y = (ry - 10) + Rnd(36)							; Place the Y randomly at the backside of the rocket.
			damtimer = 0										; Reset the timer.
		EndIf
		damtimer = damtimer + 1									; Increase the timer.
		If damtimer > (energy / 2)								; Has damtimer gone above the energy left?
			damtimer = 0										; Reset the timer.
		EndIf
	EndIf
End Function

; Update any powerups on screen.
; Powerup 3 is not used.
Function update_powerups()
	For p.powerup = Each powerup								; Go through all powerups.
		ff = p\f												; Get frame.
		Select p\tp												; Select the powerup type.
			Case 1												; Powerup one?
				DrawImage powup1,p\x,p\y,ff						; Draw powerup image one.
			Case 2												; Etc.
				DrawImage powup2,p\x,p\y,ff
			Case 3
				DrawImage powup3,p\x,p\y,ff
		End Select
		p\f = p\f + 1											; Increase anim frame.
		If p\f > 17												; Bigger than frame 17?
			p\f = 0												; Rest the frame to 0.
		EndIf
		p\x = p\x - (p\sp / 2)									; Move the powerup at half the speed of the rock.
		If p\x < -10											; Powerup off screen?
			Delete p											; Delete it.
		EndIf
  		If p <> Null											; Does the powerup still exist?
			; If the rocket collides with the powerup
			; we do whatever the powerup does.
		    If ImagesCollide(rocket,rx,ry,0,powup1,p\x,p\y,0)
				If p\tp = 1										; Powerup 1?
					energy = energy + 10						; Increase energy by 10 percent.
					If energy > 100								; More than 100% energy?
						energy = 100							; Keep 100%.
						score = score + 50						; Give extra points.
					EndIf
				EndIf
				If p\tp = 2										; Powerup 2?
					shotlevel = shotlevel + 1					; Enchance the cannon.
					If shotlevel > 3							; Cannon better than 3?
						shotlevel = 3							; Reset to 3.
						score = score + 50						; Give extra points.
					EndIf
				EndIf
				Delete p										; Delete powerup.
			EndIf
		EndIf
	Next
End Function

; Now this is an interesting function.
; It generates that nice X and Y wobbling
; and moving get ready logo.
Function get_ready()
	getrdy = LoadAnimImage("getready.bmp",4,4,0,576)			; Load the image and cut it to utter pieces.

	xl = -300													; X location of the logo at start.
	cnt = 0														; location counter at 0.
	FlushKeys()
	While Not cnt = 1100										; repeat until counter = 1100
		TileImage stars4,Sin(cnt) * 200, Cos(cnt) * 180			; Stars layer 1
		TileImage stars3,Sin(cnt) * 180, Cos(cnt) * 160			; Stars layer 2
		TileImage stars2,Sin(cnt) * 160, Cos(cnt) * 140			; Stars layer 3
		TileImage stars1,Sin(cnt) * 140, Cos(cnt) * 120			; Stars layer 4
	
		; This is the beast.
		; The logo was cut in 8 by 72 pieces, these are drawn
		; here and sinussed nicely in X,Y.
		For p = 1 To 8										
			For q = 0 To 71
				DrawImage getrdy, xl + ((q * 3) + (Sin(cnt + p) * 80)), 224 + ((p * 3) + (Sin((cnt * 2)+ q) * 60)), icnt
				icnt = icnt + 1
			Next
		Next 

		xl = xl + 1												; Increase the X location.

		icnt = 0												; Frame counter (frame = piece here)
		cnt = cnt + 1											; increase counter.
		If KeyHit(57) Then cnt = 1100							; Let user press space to continue.
		Flip													; Flip screens.
		Cls
	Wend
End Function

; This functions saves the hiscore
; list to disk.
Function save_hiscores()
	For q = 0 To 9												; Add spaces to fill up empty spots to all 10 entries.
		sl = 12 - Len(hiscore$(q,1))							; Get length of name.
		If sl > 0												; Needs padding?
			For p = 1 To sl										
				z$ = z$ + " "									; Add a space to the name.
			Next
			hiscore$(q,1) = hiscore$(q,1) + z$					; Put the space padded name back in the array.
		EndIf
		z$ = ""													; Clear the temp string.
		sl = 6 - Len(hiscore$(q,2))								; Get length of score.
		If sl > 0												; Needs padding?
			For p = 1 To sl										
				z$ = z$ + " "									; Add a space to the score.
			Next
			hiscore$(q,2) = z$ + hiscore$(q,2)					; Put padded score back in the array.
		EndIf
		z$ = ""													; Clear the temp string.
	Next

	hi = WriteFile("hiscore.dat")								; Open the file for writing.
	For q = 0 To 9												; Go through all 10 entries.
		For p = 1 To Len(hiscore$(q,1))							; Write each letter.
			a$ = Mid$(hiscore$(q,1),p,1)						; Get letter.
			as = Asc(a$)										; Make it ascii format.
			WriteByte hi,as - 20								; Decrease by 20 to make different character.
		Next
		For p = 1 To Len(hiscore$(q,2))							; Write each string number.
			a$ = Mid$(hiscore$(q,2),p,1)						; Get number from string.
			as = Asc(a$)										; Make it ascii format.
			WriteByte hi,as - 20								; Decrease by 20 to make different character.
		Next
	Next
	CloseFile hi												; Close the file.
End Function

; This function loads the hiscores from the
; hiscores file. If the file doesn't exist
; then it will create a new one.
Function load_hiscores()
	hi = OpenFile("hiscore.dat")								; Open the file.
	If hi = 0													; Does the file exist?
		hiscore$(0,1) = "ASTEROID"								; If not then we fill the hiscore table with these.
		hiscore$(0,2) = "10000"
		hiscore$(1,1) = "SHOWER"
		hiscore$(1,2) = "9000"
		hiscore$(2,1) = "BY"
		hiscore$(2,2) = "8000"
		hiscore$(3,1) = "TRACER"
		hiscore$(3,2) = "7000"
		hiscore$(4,1) = "DONE"
		hiscore$(4,2) = "6000"
		hiscore$(5,1) = "IN"
		hiscore$(5,2) = "5000"
		hiscore$(6,1) = "BLITZ"
		hiscore$(6,2) = "4000"
		hiscore$(7,1) = "BASIC"
		hiscore$(7,2) = "3000"
		hiscore$(8,1) = "FOR"
		hiscore$(8,2) = "2000"
		hiscore$(9,1) = "PC"  
		hiscore$(9,2) = "1000"
		save_hiscores()
	Else														; File does exist.
		For q = 0 To 9											; Read all 10 hiscore entries.
			For p = 1 To 12										; Read all bytes of the name.
				a = ReadByte(hi)								; Read a single byte.
				a = a + 20										; Increase by 20 to get good character.
				ac$ = ac$ + Chr$(a)								; Put character in temp string.
			Next
			hiscore$(q,1) = ac$									; Put loaded name in hiscore array.
			ac$ = ""											; Clear temp string.
			For p = 1 To 6										; Read all bytes of the score.
				a = ReadByte(hi)								; Read a single byte.
				a = a + 20										; Increase by 20 to get good character.
				ac$ = ac$ + Chr$(a)								; Put character in temp string.
			Next
			hiscore$(q,2) = ac$									; Put loaded score in hiscore array.
			ac$ = ""											; Clear the temp string.
		Next
	EndIf
End Function

; This function will physically sort the
; hiscore table to put highest on top and
; lowest at the bottom. It uses a simple
; Bubble sort.
Function sort_hiscores()
	lowest = hiscore$(9,2)										; Get lowest score from table.
	If score > lowest											; check the score of the player against it.
		hiscore$(10,1) = name$									; Not saved part of hiscore array gets the entered name.
		hiscore$(10,2) = score									; Not saved part of hiscore array gets the gotten score.
		; A bubble sort!
		; It checks to see if the score below is higher than the one above.
		; If so it will swap them.. after going through the entire
		; array it will have sorted it.. slow but effective.
		For bub1 = 0 To 10
			counter = 0
			For bub2 = 0 To 9
				a = hiscore$(counter,2)
				b = hiscore$(counter + 1, 2)
				If b > a
					n1$ = hiscore$(counter,1)
					n2$ = hiscore$(counter + 1,1)
					hiscore$(counter,2) = b
					hiscore$(counter + 1,2) = a
					hiscore$(counter,1) = n2$
					hiscore$(counter + 1,1) = n1$
				EndIf
				counter = counter + 1
			Next
		Next
	EndIf
End Function

; This function will nicely show the highscores.
; The waving background poses problems on cheaper
; and older videocards as it fills up memory REAL
; quick on those and will eventually start swapping
; to disk. It will take a LONG time to return to
; the main menu if you watch too long and have
; one of those cards. This seems to be a DirectX
; 'feature'.
Function show_hiscores()
	back = LoadImage("hiback.bmp")								; Load background.
	cool = CreateImage(800,10)									; Make an image for the wavey background.
	hfnt = LoadAnimImage("hifont.bmp",32,27,0,60)				; Load the font and cut it.
	hlgo = LoadImage("hilogo.bmp")								; Load big hiscore logo.
	
	SetBuffer ImageBuffer(cool)									; Switch to the newly made image.
	TileImage back,0,0											; Tile the background on it.
	SetBuffer BackBuffer()										; Go back to the back buffer for drawing.
	
	FlushKeys()
	While Not KeyHit(57) Or KeyHit(1)							; Esc or Space pressed? leave!
		For t = 0 To 47											; Show the tiled background image all over the screen.
			part2 = 2											; Sets waving stuff.
			part3 = 80											; More waving stuff :)
			; Draw the background in a wavey manner.
			DrawImage cool,-80 + Sin(cnt - (t * part2)) * part3,t * 10
		Next
		cnt = cnt + 2											; increase counter for SIN.

		For hx = 0 To 9											; Draw all 10 scores and names.
			For let = 1 To 12									; Draw all 12 characters off name.
				a$ = Mid$(hiscore$(hx,1),let,1)					; Get character.
				as = Asc(a$)									; Get ascii code.
				If a$ <> " "									; Not space?
					; Draw the character on screen..
					; The as - 32 will make sure the
					; correct character is taken from
					; the font.
					DrawImage hfnt,(let * 32) - 12,140 + (32 * hx),as - 32
				EndIf
			Next
			For let = 1 To 6									; Draw all 6 score characters.
				a$ = Mid$(hiscore$(hx,2),let,1)					; Get character.
				as = Asc(a$)									; Get ascii code.
				If a$ <> " "
					; Draw the character on screen..
					; The as - 32 will make sure the
					; correct character is taken from
					; the font.
					DrawImage hfnt,400 + (let * 32),140 + (32 * hx),as - 32
				EndIf
			Next
		Next

		; This draws the bouncing logo and moves sideways.
		DrawImage hlgo,68 - (Sin(cnt / 2) * 60),70 - Abs(Sin(cnt) * 60)
		Flip													; Flip screen.
	Wend
End Function

; This function lets the player
; enter his/hers name into the
; hiscore table.
Function enter_hiscore()
	enter = LoadImage("highscore.bmp")							; Load the hiscore logo.
	hfnt  = LoadAnimImage("hifont.bmp",32,27,0,60)				; Load the font, cut it.

	fntcnt = 33													; Start of font parts needed. (frame in image)
	lets = 1													; Number of entered characters.
	done = False												; Set check for player done to false.
	name$ = ""													; Empty the name.
	FlushKeys()
	While done = False
		TileImage stars4,0,cnt * 4								; Stars layer 1.
		TileImage stars3,0,cnt * 3								; Stars layer 2.
		TileImage stars2,0,cnt * 2								; Stars layer 3.
		TileImage stars1,0,cnt * 1								; Stars layer 4.

		DrawImage enter,106,64									; Place the hiscore logo.

		For fy = 1 To 2											; Draw all the font parts needed.
			For fx = 1 To 13
				DrawImage hfnt,(fx * 48) - 30,128 + (fy * 64), fntcnt
				fntcnt = fntcnt + 1
			Next
		Next
		fntcnt = 33
		
		For lt = 1 To 12										; Draw entered letters or dots if not entered.
			If hiname$(lt) <> ""
				If lt = lets
					DrawImage hfnt,lt * 48,400 - Abs(Sin(cnt * 2) * 20),Asc(hiname$(lt)) - 32
				Else
					DrawImage hfnt,lt * 48,400,Asc(hiname$(lt)) - 32
				EndIf
			Else
				If lt = lets
					DrawImage hfnt,lt * 48,400 - Abs(Sin(cnt * 2) * 20),14
				Else
					DrawImage hfnt,lt * 48,400,14
				EndIf
			EndIf
		Next 

		If KeyHit(205)											; Right key.
			lets = lets + 1										; Move one letter to right.
			If lets > 12 Then lets = 1							; Wrap to first letter if past letter 12.
		EndIf
		If KeyHit(203)											; Left key.
			lets = lets - 1										; Move one letter to left.
			If lets < 1 Then lets = 12							; Wrap to last letter if past letter 1.
		EndIf
		If KeyHit(14)											; Backspace key.
			lets = lets - 1										; Delete letter.
			If lets < 1 Then lets = 1							; Keep on first if needed.
			hiname$(lets) = ""									; Clear letter from array.
		EndIf

		; 97 - 122
		ltr = GetKey()											; Get all the keys that can be entered.
		If ltr > 96 And ltr < 123
			If lets < 13
				hiname$(lets) = Upper$(Chr$(ltr))				; Put letter in array.
				lets = lets + 1									; Move to next letter position.
			EndIf
		EndIf
				
		cnt = cnt + 1

		If KeyHit(28)											; Return key.
			For pl = 1 To 12									; Put name in hiscore list.
				If hiname$(pl) = ""
					name$ = name$ + " "
				Else
					name$ = name$ + hiname$(pl)
				EndIf
			Next
			done = True											; Player is ready.
			sort_hiscores()										; Sort hiscore table.
			save_hiscores()										; Save hiscore table.
			load_hiscores()										; Load again just in case.
			show_hiscores()										; Show hiscore list.
		EndIf

		Flip
		Cls
	Wend
End Function

; Another beast!
; 3D wireframe graphics for the
; game over screen.
Function threed()
	vx# = vx# + 0.5
	vy# = vy# + 0.5
;	vz# = vz# + 1

	For n = 1 To numpoints
		x3d = points(n, 1)
		y3d = points(n, 2)
		z3d = points(n, 3)
       
		ty# = ((y3d * Cos(vx#)) - (z3d * Sin(vx#)))
		tz# = ((y3d * Sin(vx#)) + (z3d * Cos(vx#)))
		tx# = ((x3d * Cos(vy#)) - (tz# * Sin(vy#)))
		tz# = ((x3d * Sin(vy#)) + (tz# * Cos(vy#)))
		ox# = tx#
		tx# = ((tx# * Cos(vz#)) - (ty# * Sin(vz#)))
		ty# = ((ox# * Sin(vz#)) + (ty# * Cos(vz#)))
		nx  = Int(512 * (tx#) / (distance - (tz#))) + 320
		ny  = Int(240 - (512 * ty#) / (distance - (tz#)))
      
		rotated(n, 1) = nx
		rotated(n, 2) = ny
	Next

	For n = 1 To numconn
		Color Rnd(100,255),Rnd(100,255),Rnd(100,255)
		Line rotated(connect(n, 1), 1), rotated(connect(n, 1), 2),rotated(connect(n, 2), 1), rotated(connect(n, 2), 2)
	Next
End Function

; The options screen.
; Allows selection of difficulty.
Function options()
	option = LoadImage("options.bmp")							; Load options logo.	
	difftx = LoadImage("difficulty.bmp")						; Load difficulty menu.

	bullx = 0													; Reset indicator X.
	selected = 2												; Normal for default.
	done = False												; Reset done boolean.
	
	FlushKeys()
	While Not done = True
		move_stars()											; Show starfield.							

		DrawImage option,((640-305)/2) - Sin(cnt) * 150,10		; Show and move logo.
		DrawImage difftx,215,130								; Show menu.
		
		If KeyHit(200)											; Key up pressed?
			selected = selected - 1								; Move one item up.
			If selected < 1										; Past highest item? reset to lowest item.
				selected = 4
			EndIf
		EndIf
		If KeyHit(208)											; Key down pressed?
			selected = selected + 1								; Move one item down.
			If selected > 4										; Past lowest item? reset to highest item.
				selected = 1
			EndIf
		EndIf
		If KeyHit(28)											; Enter pressed?
			difficulty = selected								; Set selected difficulty.
			done = True											; Ready, so leave screen.
		EndIf
		
		Select selected											; Which item are we on?
			Case 1
				bullx = 150										; Put moving bullets there.
			Case 2
				bullx = 225
			Case 3
				bullx = 300
			Case 4
				bullx = 375
		End Select
		
		; Draws and moves the arrows made out of bullets.
		DrawImage firebt,120 + Sin(cnt * 2) * 40,bullx - 20
		DrawImage firebt,130 + Sin(cnt * 2) * 40,bullx - 10
		DrawImage firebt,140 + Sin(cnt * 2) * 40,bullx
		DrawImage firebt,130 + Sin(cnt * 2) * 40,bullx + 10
		DrawImage firebt,120 + Sin(cnt * 2) * 40,bullx + 20

		DrawImage fireb2,520 - Sin(cnt * 2) * 40,bullx - 20
		DrawImage fireb2,510 - Sin(cnt * 2) * 40,bullx - 10
		DrawImage fireb2,500 - Sin(cnt * 2) * 40,bullx
		DrawImage fireb2,510 - Sin(cnt * 2) * 40,bullx + 10
		DrawImage fireb2,520 - Sin(cnt * 2) * 40,bullx + 20
		
		
		cnt = cnt + 2
		Flip
		Cls
	Wend
End Function

; Show credits scroller after quit.
Function credits()
	back = LoadImage("credits.bmp")							; Load background.
	create_credits()										; Create credits scroller.
	cloc# = 490
	FlushKeys()												; Start position of scroller.
	While Not KeyHit(1) Or KeyHit(57)
		DrawImage back,0,0									; Draw background.
		DrawImage credscrll,10,cloc#						; Draw scroller.
		cloc# = cloc# - 0.3									; Scroll.
		If cloc# < -1000									; Scrolled out of screen?
			cloc# = 490										; Back to beginning.
		EndIf
		Flip												; Flip
		Cls
	Wend
End Function

; This creates the large image
; used to show credits and stuff.
Function create_credits()
	cfnt = LoadAnimImage("cfont.bmp",20,20,0,52)			; Load font and cut it.
	
	SetBuffer ImageBuffer(credscrll)						; Switch to buffer.
	For	cc = 0 To creditlen - 1								
		For cl = 1 To 31
			cr$ = Mid$(credit$(cc),cl,1)					; Get letter from credits array.
			crs = Asc(cr$)									; Get ascii.
			If crs => 65									; Get letters and numbers.
				cra = crs - 65
			EndIf
			If crs => 48 And crs <= 57
				cra = crs - 22
			EndIf		
			Select cr$										; Special characters?
				Case "!"
					cra = 51
				Case "&"
					cra = 40
				Case "^"
					cra = 41
				Case "@"
					cra = 36
				Case "#"
					cra = 37
				Case "{"
					cra = 38
				Case "}"
					cra = 39
				Case "."
					cra = 42
				Case ","
					cra= 43
				Case ":"
					cra = 44
				Case "("
					cra = 45
				Case ")"
					cra = 46
				Case "?"
					cra = 49
				Case "-"
					cra = 47
				Case "%"
					cra = 50
				Case "*"
					cra = 48
			End Select
			If cr$ <> " "
				; Draw the letters on the image.
				DrawImage cfnt,(cl * 20) - 20,cc * 20,cra	
			EndIf
		Next
	Next
	SetBuffer BackBuffer()										; Switch back to back buffer drawing.
End Function

; Show game over screen.
Function game_over()
	cntr = 0
	While Not KeyHit(1) Or KeyHit(57) Or cntr = 4000
		threed()												; Show cool wireframe 3d.
		move_stars()											; Move the starts.
		cntr = cntr + 1											; Update counter.
		Flip
		Cls
	Wend
End Function

; This function shows the title screen.
Function intro()
	back = LoadImage("credits.bmp")								; Load background.
	title = LoadImage("logo.bmp")								; Load logo.
	
	While Not KeyHit(1) Or KeyHit(57) Or MouseHit(1) Or MouseHit(2)
		DrawImage back,0,0
		DrawImage title,59,167
		Flip
		Cls
	Wend
End Function

; All the data for the 3d game over.. lots of work to make :)

.points
; Points table
;      X,  Y,  Z
Data  40 - 170,  0, 10
Data   0 - 170, 40,-10
Data -40 - 170,  0,-10
Data   0 - 170,-40, 10
Data  20 - 170,-20, 10
Data   0 - 170,  0,  0
Data   0 - 170,  0,-10
Data  40 - 170, 40,-10
Data  80 - 170,  0, 10
Data  40 - 170,-40, 10
Data  20 - 170, 20,-10
Data  61 - 170,-20, 10
Data  40 - 170,  0,-10
Data  80 - 170, 40,-10
Data  80 - 170,  0,  0
Data 120 - 170,  0, 10
Data  80 - 170,-40, 10
Data  80 - 170,  0,-10
Data 120 - 170, 40,-10
Data 160 - 170,  0, 10
Data 120 - 170,-40, 10
Data 100 - 170, 20,-10
Data 120 - 170,  0,  0
Data  10,  0,-10
Data  50, 40,-10
Data  90,  0, 10
Data  50,-40, 10
Data  50,  0,-10
Data  90, 40,-10
Data  90,-40,10
Data 130,  0,10
Data  90,  0,-10
Data 130, 40,-10
Data 170,  0, 10
Data 130,-40, 10
Data 110, 20,-10
Data 130,  0,  0
Data 130,  0,-10
Data 170, 40,-10
Data 210,  0, 10
Data 190,-20, 10
Data 150, 20,-10
Data 170,  0,  0
Data 170,-40, 10

.conns
; Connections  (From,To, From,To, ..)
Data 1,2,2,3,3,4,4,5,5,6
Data 7,8,8,9,9,10,11,12
Data 13,14,14,15,15,16,16,17
Data 18,19,19,20,18,21,22,23
Data 24,25,25,26,26,27,27,24
Data 28,29,28,30,30,31
Data 32,33,33,34,32,35,36,37
Data 38,39,39,40,40,41,41,42,43,44