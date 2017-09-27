; ***********************
; *   Blitzanoid v1.0   *
; ***********************
; * Coding by: Tracer   *
; * Gfx    by: Tracer   *
; * Music/sfx: Various  *
; ***********************
; * This game was coded * 
; * for the Blitz       *
; * Release CD          *
; * verified 1.48 4/18  *
; ***********************
;
; There are still a few bugs in here and not all powerups i have in mind are in there.
; Also, a help screen is still lacking showing what each powerup does.
;
; For updates of this game, go to the files section on http://www.blitzbasic.com
;
; E-mail me at: tracerbb@hotmail.com
;
; Tracer

AppTitle "Blitzanoid"

Graphics 640,480
SetBuffer BackBuffer()

; Level graphics
Global playfield = LoadImage("gfx\field.bmp")
Global backgrnd1 = LoadImage("gfx\back1.bmp")
Global blocks    = LoadAnimImage("gfx\blocks.bmp",32,16,0,14)

; Player graphics
Global playerbat = LoadImage("gfx\bat.bmp")
Global plrsmlbat = LoadImage("gfx\batsmall.bmp")
Global plrbigbat = LoadImage("gfx\batbig.bmp")

; Ball graphics
Global playerbal = LoadImage("gfx\ball.bmp")
Global dball     = LoadImage("gfx\dball.bmp")

; Powerup graphics
Global powerups  = LoadAnimImage("gfx\powerups.bmp",32,16,0,20)
Global safetybar = LoadImage("gfx\bar.bmp")
Global batgun    = LoadImage("gfx\gun.bmp")
Global gbullet   = LoadImage("gfx\bullet.bmp")

; Numbers to display the score
Global scorenrs	 = LoadAnimImage("gfx\score numbers.bmp",8,15,0,11)

; Information graphics
Global getready  = LoadImage("gfx\getready.bmp")
Global complete  = LoadImage("gfx\level complete.bmp")

; Font for main menu texts
Global font16 = LoadAnimImage("gfx\font.bmp",16,16,0,60)

; Lots of variables for the game.
Global bat_x, destructor = False
Global bar_loc = -512, movebar = False, existbar = False, bartimer = 0, baraway = False
Global guns = False, bat_size = 2, shot_counter, block_counter, ball_counter = 0
Global death = False
Global score = 0, lives = 3

; Start level
Global level = 1
; Amount of defined levels
Global num_levels = 20

; Globals for hiscores.
Global name$												; Used to store players name after hiscore entry.

; Globals for the 3d.
Global numpoints = 44										; Number of points in the point table.
Global numconn   = 33										; Number of connections in the connect table.
Global distance  = 400										; Needed for perspective.
Global vx#													; X location.
Global vy#													; Y location.
Global vz#													; Z location.

; Variables for main menu.
Global txts, txtl
Global sloc1 = -650,sloc2 = 650,sloc3 = -650,sloc4 = 650,sloc5 = -650, sloc
Global num_stars = 200

; Lots of sounds.
Global hitbat   = LoadSound("sfx\bathit.wav")
Global hitwall  = LoadSound("sfx\wallhit.wav")
Global hitblock = LoadSound("sfx\blockhit.wav")
Global teleport = LoadSound("sfx\teleport.wav")
Global laser    = LoadSound("sfx\laser.wav")
Global explode  = LoadSound("sfx\explode.wav")
Global hitshld  = LoadSound("sfx\shieldhit.wav")
Global hitsteel = LoadSound("sfx\steelhit.wav")

; Contains all the levels.
Dim levels(num_levels,20 * 320)

; Arrays used by hiscore code.
Dim hiscore$(10,2)											;Holds the hiscore table (1 = names, 2=scores).
Dim hiname$(12)												;Needed for entering a name into the table.

; Arrays used by 3d code.
Dim points(numpoints, 3)									; Holds the point locations of the game over 3d.
Dim rotated(numpoints, 2)									; Holds rotated points for the 3d.
Dim connect(numconn, 2)										; Holds the connections of the 3d.

; Main menu arrays.
Dim x(num_stars,3)
Dim txt$(100)
Dim scroller(100)

; lots of types
Type block
	Field x,y,bframe
	Field hits
End Type

Type ball
	Field x#,y#
	Field xspeed#,yspeed#
	Field xaccel#,yaccel#
	Field ball_timer,bat_hit
End Type

Type powerup
	Field x,y
	Field frame
End Type

Type bullet
	Field x,y
End Type

SeedRnd MilliSecs()														; Randomize Rnd function.

intro()																	; Display JAW's intro picture.
setup()																	; Set up stuff for the game.
mainmenu()																; Go to the main menu

Function setup()
	load_hiscores()														; Load hiscores file from disk.

	Restore leveldata													; Put all the levels in an array
	For numlev = 1 To num_levels
		For numblk = 1 To 320
			Read bl
			levels(numlev,numblk) = bl
		Next
	Next

	For i=1 To num_stars												; Generate the stars
		x(i,1)=Rnd(639)+1
		x(i,2)=Rnd(479)+ 1
		x(i,3)=Rnd(5)+1
	Next

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

	; The texts that appear on the main menu.
	txts = 35
	;          "1234567890123456789012345678901234567890"
	txt$(0)  = "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
	txt$(1)  = "@                                      @"
	txt$(2)  = "@    HEYA AND WELCOME TO BLITZANOID    @"
	txt$(3)  = "@                                      @"
	txt$(4)  = "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"

	txt$(5)  = "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
	txt$(6)  = "@                                      @"
	txt$(7)  = "@     CODE AND GRAPHICS BY TRACER!     @"
	txt$(8)  = "@                                      @"
	txt$(9)  = "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"

	txt$(10) = " 139 SHEEP WERE EXAMINED CLOSELY DURING "
	txt$(11) = "       THE CREATION OF THIS GAME.       "
	txt$(12) = "                                        "
	txt$(13) = "  AS FAR AS KNOWN NO LLAMA'S HAVE BEEN  "
	txt$(14) = "SEXUALLY INTIMIDATED DURING DEVELOPMENT."

	txt$(15) = "#&#&#&#&#&#&#&#&#&#&#&#&#&#&#&#&#&#&#&#&"
	txt$(16) = "&  BLITZANOID WAS CODED FOR THE BLITZ  #"
	txt$(17) = "#   BASIC RELEASE CD AND FINISHED IN   &"
	txt$(18) = "&          THE NICK OF TIME..          #"
	txt$(19) = "#&#&#&#&#&#&#&#&#&#&#&#&#&#&#&#&#&#&#&#&"
	
	txt$(20) = "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
	txt$(21) = "@ LEVEL DESIGNING BY TRACER AND RACHEL @"
	txt$(22) = "@       TITLE PICTURE MADE BY JAW      @"
	txt$(23) = "@           ENJOY THE GAME!!           @"
	txt$(24) = "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"

	txt$(25) = " MANY THANKS GO TO MARK SIBLY FOR HELP  "
	txt$(26) = "    WITH FIXING THE STICK IN THE WALL   "
	txt$(27) = "                PROBLEM.                "
	txt$(28) = "                                        "
	txt$(29) = "                                        "

	txt$(30) = "            GREETZ FLY OUT TO           "
	txt$(31) = "MARK SIBLY, GEORGE BRAY, WARPY, WAGBERT,"
	txt$(32) = "JAW, JAIME, RIK, JAMES, REQUIEM, RACHEL,"
	txt$(33) = "  IID, AND ALL THE REST OF MY FRIENDS!  "
	txt$(34) = "                                        "

	; Here these texts are stuck on images to be scrolled on the screen.
	; The code looks up the characters and converts it to a frame number
	; of the font file, then pastes it on the new image.
	For cn = 1 To txts
		scroller(cn - 1) = CreateImage(40 * 16,16)
		SetBuffer ImageBuffer(scroller(cn - 1))
		For ct = 1 To 40
			l$ = Mid$(txt$(cn - 1),ct,1)
			as = Asc(l$)
			as = as - 65
			Select l$
				Case "@"
					as = 51
				Case "!"
					as = 40
				Case "0"
					as = 27
				Case "1"
					as = 28
				Case "2"
					as = 29
				Case "3"
					as = 30
				Case "4"
					as = 31
				Case "5"
					as = 32
				Case "6"
					as = 33
				Case "7"
					as = 34
				Case "8"
					as = 35
				Case "9"
					as = 36
				Case "."
					as = 37
				Case "'"
					as = 43
				Case ","
					as = 48
				Case "#"
					as = 52
				Case "&"
					as = 53
			End Select
			If l$ <> " "
				DrawImage font16,(ct * 16) - 16,0,as
			EndIf	
		Next
	Next
	SetBuffer BackBuffer()
End Function

Function intro()
	letitel = LoadImage("gfx\title.bmp")
	
	outta = False
	While Not outta = True
		DrawImage letitel,0,0
		If KeyHit(1) Or KeyHit(57) Or MouseHit(1)
			outta = True
		EndIf
		Flip
		Cls
	Wend
End Function

; The main game loop.
; It's cluttered and not very structured.. oh well :)
Function game()
	While Not KeyDown(1)
		update_background()									; Draw background
		update_bullets()									; Draw bullets of laser
		update_playfield()									; Draw the frame around the field.
		update_bat()										; Move the bat
		update_level()										; Draw the level blocks
		update_ball()										; Move balls, check collisions, etc
		update_powerups()									; Move any powerups on screen

;If KeyHit(88) Then SaveBuffer(FrontBuffer(),"d:\screenshot.bmp") 
		
;		Text 100,100,level
		
		; Checks to see if all the block are gone.
		; If so it will display a message and
		; go to the next level.
		If block_counter = 0
			level = level + 1
			If level > num_levels
				level = 1
			EndIf
			level_complete()
			clear_vars(False)
			start_game(False)
		EndIf

		; Player dead?
		If lives < 1										; No more lives?
			lowest = hiscore$(9,2)							; Get lowest hiscore.
			If score < lowest								; Score lower than that?
				game_over()									; 3d game over scene.
				show_hiscores()								; Show current hiscores.
				clear_vars(True)
				mainmenu()									; Go to the main menu.
			Else											; Score higher than lowest hiscore?
				game_over()									; 3d game over scene.
				enter_hiscore()								; Let the player enter his/hers name.
				clear_vars(True)
				mainmenu()									; Main menu.
			EndIf
		EndIf

		Flip
		Cls
	Wend
End Function

; Displays the cute 3d gameover.
Function game_over()
	cntr = 0
	While Not KeyDown(1) Or KeyDown(57) Or cntr = 4000
		threed()												; Show cool wireframe 3d.
		update_stars()
		cntr = cntr + 1											; Update counter.
		Flip
		Cls
	Wend
End Function

; Starts the actual game,
; builds the level, waits
; for the user to select
; a spot to launch the ball
; makes the first ball, etc
Function start_game(death)
	If death = False
		block_counter = 0
		counter = 1
		For q = 1 To 20
			For t = 1 To 16
				fblk = levels(level,counter)
				bl.block = New block
				bl\x = t * 32 - 16
				bl\y = q * 16

				Select fblk
					Case 8
						bl\hits = 6
					Case 9
						bl\hits = 5
					Case 10
						bl\hits = 4
					Case 11
					bl\hits = 3
					Case 12
						bl\hits = 2
					Default
						bl\hits = 1
				End Select
				
				bl\bframe = fblk - 1
	
				If bl\bframe <> 6 And bl\bframe <> -1
					block_counter = block_counter + 1
				EndIf
				counter = counter + 1
			Next
		Next
	EndIf

	timer = 0
	While timer <> 800
		update_background()
		update_playfield()
		update_bat()
		update_level()
		DrawImage getready,16 + ((512 - 278) / 2),350

		bal_x = (MouseX() - bal_x) / 3 + bal_x
		If bal_x < 16
			bal_x = 16
		Else 
			If bal_x > 450
				bal_x = 450
			EndIf
		EndIf
		DrawImage playerbal,bal_x + 31,424

		Flip
		Cls
		timer = timer + 1
		If MouseHit(1)
			timer = 800
		EndIf
		If KeyDown(1)
			End
		EndIf
	Wend

	b.ball = New ball
	b\x# = bal_x + 31
	b\y# = 424
	b\xspeed# = -2
	b\yspeed# = -2
	b\xaccel# = 1
	b\yaccel# = 1
	ball_counter = ball_counter + 1
	
	destructor = False
End Function

; Level complete? displays a message.
Function level_complete()
	timer = 0
	While timer <> 800
		update_background()
		update_playfield()
		update_bat()
		DrawImage complete,16 + ((512 - 433) / 2),350

		Flip
		Cls
		timer = timer + 1
		If MouseHit(1)
			timer = 800
		EndIf
		If KeyDown(1)
			End
		EndIf
	Wend
End Function

; Draws the pattern behind the blocks
; Also moves the shielding bar.
Function update_background()
	TileImage backgrnd1,0,0

	If movebar = True And existbar = False And baraway = False
		DrawImage safetybar,16 + bar_loc,465
		bar_loc = bar_loc + 6
		If bar_loc => 0
			bar_loc = 0
			movebar = False
			existbar = True
		EndIf
	EndIf
	If existbar = True
		DrawImage safetybar,16,465
		bartimer = bartimer + 1
	EndIf
	If bartimer > 600
		bar_away = True
		existbar = False
		DrawImage safetybar,16 + bar_loc,465
		bar_loc = bar_loc - 6
		If bar_loc <= -512
			bar_loc = -512
			bartimer = 0
			baraway = False
			movebar = False
		EndIf
	EndIf
End Function

; Shows the frame, score, lives.
Function update_playfield()
	DrawImage playfield,0,0

	scr$ = score
	slen = Len(scr$)
	lloc = 611
	For t = slen To 1 Step -1
		sc$ = Mid$(score,t,1)
		sn = sc$
		DrawImage scorenrs,lloc,35,sn
		lloc = lloc - 9
	Next
	
	lifeloc = 70
	For q = 1 To lives - 1
		DrawImage playerbat,545,lifeloc
		lifeloc = lifeloc + 22
	Next
End Function

; Move the bat, resize it where needed,
; Shows the guns on the sides.
Function update_bat()
	bat_x = (MouseX() - bat_x) / 3 + bat_x
	
	limit_x_left  = 16
	Select bat_size
		Case 1
			limit_x_right = 480
		Case 2
			limit_x_right = 450
		Case 3
			limit_x_right = 422
	End Select

	If bat_x < limit_x_left
		bat_x = limit_x_left
	Else 
		If bat_x > limit_x_right
			bat_x = limit_x_right
		EndIf
	EndIf
	
	If guns = True
		DrawImage batgun,bat_x + 4,430
		Select bat_size
			Case 1
				DrawImage batgun,bat_x + 30,430
			Case 2
				DrawImage batgun,bat_x + 60,430
			Case 3
				DrawImage batgun,bat_x + 88,430
		End Select
		
		If MouseHit(1)
			PlaySound laser

			gun_left = bat_x + 10
			Select bat_size
				Case 1
					gun_right = bat_x + 36
				Case 2
					gun_right = bat_x + 66
				Case 3
					gun_right = bat_x + 94
			End Select
				
			bu.bullet = New bullet
			bu\x = gun_left
			bu\y = 430
			bu.bullet = New bullet
			bu\x = gun_right
			bu\y = 430
		EndIf
	EndIf
	
	Select bat_size
		Case 1
		    DrawImage plrsmlbat,bat_x,440
		Case 2
		    DrawImage playerbat,bat_x,440
		Case 3
		    DrawImage plrbigbat,bat_x,440
	End Select
End Function

; Draw the bullets and detect collisions
; on blocks.
Function update_bullets()
	For bu.bullet = Each bullet
		bu\y = bu\y - 5
		DrawImage gbullet,bu\x,bu\y

		If bu\y < 0
			Delete bu
		Else
			For bl.block = Each block
				If bl\bframe <> -1
					If ImagesCollide(gbullet,bu\x,bu\y,0,blocks,bl\x,bl\y,bl\bframe)
						If bl\bframe <> 6
							block_counter = block_counter - 1
							PlaySound explode
							generate_powerup(bl\x,bl\y)
							score = score + 10
							If bl\hits = 1
								bl\bframe = -1
								Delete bu
								Exit
							Else
								bl\hits = bl\hits - 1
							EndIf
						Else
							PlaySound hitsteel
							Delete bu
							Exit
						EndIf
					EndIf
				EndIf
			Next
		EndIf
	Next
End Function

; This is the monster that does
; all the work for the ball,
; Very large function, i will document
; it for the next version of the game.
Function update_ball()
	For b.ball = Each ball
		; check collisions on blocks.
		b\x# = b\x# + (b\xspeed# * b\xaccel#)
		For bl.block = Each block
			If bl\bframe <> -1
				If ImagesCollide(playerbal,b\x#,b\y#,0,blocks,bl\x,bl\y,bl\bframe)
					If destructor = False
						b\xspeed# = -b\xspeed#	
						b\x# = b\x# + (b\xspeed# * b\xaccel#)
					EndIf
					If bl\hits > 1
						bl\hits = bl\hits - 1
						bl\bframe = bl\bframe + 1						
					EndIf
					If bl\bframe <> 6 And bl\hits = 1
						block_counter = block_counter - 1
						bl\bframe = -1
						score = score + 10
						PlaySound hitblock
						generate_powerup(bl\x,bl\y)
					Else
						PlaySound hitsteel
					EndIf
				EndIf
			EndIf
		Next
		b\y# = b\y# + (b\yspeed# * b\yaccel#)
		For bl.block = Each block
			If bl\bframe <> -1
				If ImagesCollide(playerbal,b\x#,b\y#,0,blocks,bl\x,bl\y,bl\bframe)
					If destructor = False
						b\yspeed# = -b\yspeed#
						b\y# = b\y# + (b\yspeed# * b\yaccel#)
					EndIf
					If bl\hits > 1
						bl\hits = bl\hits - 1
						bl\bframe = bl\bframe + 1						
					EndIf
					If bl\bframe <> 6 And bl\hits = 1
						block_counter = block_counter - 1
						bl\bframe = -1
						score = score + 10
						PlaySound hitblock
						generate_powerup(bl\x,bl\y)
					Else
						PlaySound hitsteel
					EndIf
				EndIf
			EndIf
		Next

		; check collisions on walls.
		If b\y# < 16
			PlaySound hitwall
			b\yspeed# = Abs(b\yspeed#)
			b\y# = b\y# + (b\yspeed# * b\yaccel#)
		EndIf
		If b\x# < 16 Or b\x# > 512
			PlaySound hitwall
			If b\x# < 16
				b\xspeed#=Abs(b\xspeed#)
			Else
				b\xspeed#=-Abs(b\xspeed#)
			EndIf
			b\x# = b\x# + (b\xspeed# * b\xaccel#)
		EndIf
		If b\y# > 480
			Delete b
			ball_counter = ball_counter - 1
			If ball_counter = 0
				destructor = False
				bar_loc = -512
				movebar = False
				existbar = False
				bartimer = 0
				baraway = False
				guns = False
				bat_size = 2
				ball_counter = 0
				lives = lives - 1
				If lives > 0
					start_game(True)
				EndIf
			EndIf
			Exit
		EndIf

		; check collisions on bat.
		Select bat_size
			Case 1
				bat_image = plrsmlbat
			Case 2
				bat_image = playerbat
			Case 3
				bat_image = plrbigbat
		End Select
		
		If ImagesCollide(bat_image,bat_x,440,0,playerbal,b\x#,b\y#,0)
			If Not b\bat_hit
				PlaySound hitbat
	
				Select bat_size
					Case 1
						bat_step = 8
					Case 2
						bat_step = 13
					Case 3
						bat_step = 17
				End Select
				
				If b\x# < bat_x + (bat_step + 1)
					b\yspeed# = -1
					b\xspeed# = -3
				ElseIf b\x# >= bat_x + (bat_step + 1) And b\x# < bat_x + ((bat_step * 2) + 1)
					b\yspeed# = -2
					b\xspeed# = -2
				ElseIf b\x# >= bat_x + ((bat_step * 2) + 1) And b\x# < bat_x + ((bat_step * 3) + 1)
					b\yspeed# = -3
					b\xspeed# = -1
				ElseIf b\x# >= bat_x + ((bat_step * 3) + 1) And b\x# < bat_x + ((bat_step * 4) + 1)
					b\yspeed# = -3
					b\xspeed# = 1
				ElseIf b\x# >= bat_x + ((bat_step * 4) + 1) And b\x# < bat_x + ((bat_step * 5) + 1)
					b\yspeed# = -2
					b\xspeed# = 2
				ElseIf b\x# >= bat_x + ((bat_step * 5) + 1)
					b\yspeed# = -1
					b\xspeed# = 3
				EndIf	
				b\y# = b\y# + (b\yspeed# * b\yaccel#)
				b\x# = b\x# + (b\xspeed# * b\xaccel#)
				b\bat_hit=True
			EndIf
		Else
			b\bat_hit=False
		EndIf

		; check collisions on shielding bar
		If ImagesCollide(playerbal,b\x#,b\y#,0,safetybar,16 + bar_loc,465,0)
			PlaySound hitshld
			b\yspeed# = -b\yspeed#
			b\y# = b\y# + (b\yspeed# * b\yaccel#)
		EndIf
		
		If b\ball_timer > 200
			b\xaccel# = b\xaccel# + .1
			b\yaccel# = b\yaccel# + .1
			b\ball_timer = 0
		EndIf
		b\ball_timer = b\ball_timer + 1
		
		If destructor = False
			DrawImage playerbal,b\x#,b\y#
		Else
			DrawImage dball,b\x#,b\y#
		EndIf			
	Next
End Function

; Draw the level on the screen.
Function update_level()
	For bl.block = Each block
		If bl\bframe <> -1
			DrawImage blocks,bl\x,bl\y,bl\bframe
		EndIf
	Next
End Function

; Generates a random powerup.
Function generate_powerup(bx,by)
	pw = Rnd(1,100)
	If pw > 90
		p.powerup = New powerup
		p\x = bx
		p\y = by
		p\frame = Rnd(0,9)
	EndIf
End Function

; Moves the powerups down the screen
; and checks for collisions with the bat.
Function update_powerups()
	For p.powerup = Each powerup
		p\y = p\y + 2
		DrawImage powerups,p\x,p\y,p\frame
		If p\y > 500
			Delete p
		Else
			If ImagesCollide(powerups,p\x,p\y,p\frame,playerbat,bat_x,440,0)
				give_powerup(p\frame)
				Delete p
			EndIf
		EndIf
	Next
End Function

; Assigns a powerup to the player
; if it's caught.
; The teleport on seems to be bugged.
; Sometimes it hangs when taking a teleport
; powerup.
Function give_powerup(pow)
	empty = False
	Select pow
		; Teleport
		Case 0
			PlaySound teleport	
			While empty = False
				For b.ball = Each ball
					For bl.block = Each block 
						If bl\bframe <> -1
							px = Rnd(16,16 + 512)
							py = Rnd(16,400)
							If ImagesCollide(playerbal,b\x#,b\y#,0,blocks,bl\x,bl\y,bl\bframe)
								empty = False
							Else
								empty = True
								b\x# = px
								b\y# = py
							EndIf
						EndIf
					Next ; b.ball
				Next ; bl.block
			Wend
		; double ball
		Case 1
			b.ball = First ball
			b2.ball = New ball
			b2\x# = b\x#
			b2\y# = b\y#
			b2\xspeed# = -b\xspeed#
			b2\yspeed# = -b\yspeed#
			b2\xaccel# = 1
			b2\yaccel# = 1
			ball_counter = ball_counter + 1
		; multi ball
		Case 2
			For balls = 1 To 4
				b.ball = First ball
				b2.ball = New ball
				b2\x# = b\x#
				b2\y# = b\y#
				ba1 = Rnd(0,1)
				ba2 = Rnd(0,1)
				If ba1 = 0
					bal1 = -2
				Else
					bal1 = 2
				EndIf
				If ba2 = 0
					bal2 = -2
				Else
					bal2 = 2
				EndIf
				b2\xspeed# = bal1
				b2\yspeed# = bal2
				b2\xaccel# = 1
				b2\yaccel# = 1
				ball_counter = ball_counter + 1
			Next
		; slower
		Case 3
			For b.ball = Each ball
				b\xaccel# = b\xaccel# / 2
				b\yaccel# = b\yaccel# / 2
			Next
		; faster
		Case 4
			For b.ball = Each ball
				b\xaccel# = b\xaccel# * 2
				b\yaccel# = b\yaccel# * 2
			Next
		; destructor
		Case 5
			destructor = True
		; shielding bar
		Case 6
			If baraway = False
				movebar = True
			EndIf
		; lasers
		Case 7
			guns = True
		; smaller bat
		Case 8
			bat_size = bat_size - 1
			If bat_size < 1
				bat_size = 1
			EndIf
		; bigger bat
		Case 9
			bat_size = bat_size + 1
			If bat_size > 3
				bat_size = 3
			EndIf
	End Select
End Function

; Reset all the crap.
Function clear_vars(new_game)
	destructor = False
	bar_loc = -512
	movebar = False
	existbar = False
	bartimer = 0
	baraway = False
	guns = False
	bat_size = 2
	ball_counter = 0
	Delete Each block
	Delete Each ball
	Delete Each bullet
	Delete Each powerup
	If new_game = True
		score = 0
		lives = 3
		level = 1
	EndIf
End Function

; Nice main menu is shown here.
Function mainmenu()
	logo  = LoadAnimImage("gfx\logo1.bmp",1,36,0,335)
	title = LoadImage("gfx\logo2.bmp")
	menu  = LoadImage("gfx\menu.bmp")
	part1 = 1
	part2 = 55
	part3 = 1
	part4 = 55
	smi   = 204
	item  = 1
	While Not KeyDown(16)
		update_stars()

		DrawImage playerbal,140,smi + Sin(teller * 4) * 3
		DrawImage playerbal,480,smi + Sin(teller * 4) * 3
		If KeyHit(200)
			item = item - 1
			If item = 0 Then item = 4
		EndIf
		If KeyHit(208)
			item = item + 1
			If item = 5 Then item = 1
		EndIf
		Select item
			Case 1
				smi = 204
			Case 2
				smi = 254
			Case 3
				smi = 304
			Case 4
				smi = 354
		End Select
		If KeyHit(28)
			Select item
				Case 1
					clear_vars(True)
					start_game(False)
					game()
				Case 2
				Case 3
					show_hiscores()								; Show the highest scores.
				Case 4
					End
			End Select
		EndIf

		font()

		DrawImage title,(640 - 267) / 2,120 - Abs(Sin(teller * 2) * 120)
		DrawImage menu,170,200
		For d = 0 To 334
			DrawImage logo,60 + Sin(teller - (d * part1)) * part2,d,d
	    Next 
		For d = 0 To 334
			DrawImage logo,580 - Sin(teller - (d * part3)) * part4,d,d
	    Next 
		teller = teller + 1
		Flip
		Cls
	Wend
End Function

; Shows the starfield.
Function update_stars()
	For i = 1 To num_stars
		x(i,1) = x(i,1) - x(i,3)
		If x(i,1) <= 1
			x(i,1) = 640
			x(i,2) = Rnd(479) +1
		EndIf
		col = x(i,3) * 30
		Color col,col,col
		Rect x(i,1),x(i,2),2,2
	Next 
End Function

; Draws the cool text on the screen.
Function font()
	DrawImage scroller(txtl    ),0 + sloc1,395
	DrawImage scroller(txtl + 1),0 + sloc2,412
	DrawImage scroller(txtl + 2),0 + sloc3,429
	DrawImage scroller(txtl + 3),0 + sloc4,446
	DrawImage scroller(txtl + 4),0 + sloc5,463
	If sloc1 < 0
		sloc1 = sloc1 + 5
		sloc2 = sloc2 - 5
		sloc3 = sloc3 + 5
		sloc4 = sloc4 - 5
		sloc5 = sloc5 + 5
	Else
		sloc = sloc + 1		
	EndIf
	If sloc > 500 And sloc1 < 650
		sloc1 = sloc1 + 5
		sloc2 = sloc2 - 5
		sloc3 = sloc3 + 5
		sloc4 = sloc4 - 5
		sloc5 = sloc5 + 5
	EndIf
	If sloc1 = 650
		sloc1 = -650
		sloc2 = 650
		sloc3 = -650
		sloc4 = 650
		sloc5 = -650
		sloc = 0
		If txts > 5
			txtl = txtl + 5
		EndIf
		If txtl + 1 > txts
			txtl = 0
		EndIf
	EndIf
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
		sl = 8 - Len(hiscore$(q,2))								; Get length of score.
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
		hiscore$(0,1) = "BLITZANOID"								; If not then we fill the hiscore table with these.
		hiscore$(0,2) = "10000"
		hiscore$(1,1) = "BY"
		hiscore$(1,2) = "9000"
		hiscore$(2,1) = "TRACER"
		hiscore$(2,2) = "8000"
		hiscore$(3,1) = "CODED"
		hiscore$(3,2) = "7000"
		hiscore$(4,1) = "FOR"
		hiscore$(4,2) = "6000"
		hiscore$(5,1) = "THE"
		hiscore$(5,2) = "5000"
		hiscore$(6,1) = "BLITZ"
		hiscore$(6,2) = "4000"
		hiscore$(7,1) = "BASIC"
		hiscore$(7,2) = "3000"
		hiscore$(8,1) = "RELEASE"
		hiscore$(8,2) = "2000"
		hiscore$(9,1) = "CD"  
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
			For p = 1 To 8										; Read all bytes of the score.
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
	back = LoadImage("gfx\hiback.bmp")								; Load background.
	cool = CreateImage(800,10)									; Make an image for the wavey background.
	hfnt = LoadAnimImage("gfx\hifont.bmp",32,27,0,60)				; Load the font and cut it.
	hlgo = LoadImage("gfx\hilogo.bmp")								; Load big hiscore logo.
	
	SetBuffer ImageBuffer(cool)									; Switch to the newly made image.
	TileImage back,0,0											; Tile the background on it.
	SetBuffer BackBuffer()										; Go back to the back buffer for drawing.

	While Not KeyDown(57) Or KeyDown(1)							; Esc or Space pressed? leave!
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
					DrawImage hfnt,(let * 32) - 32,140 + (32 * hx),as - 32
				EndIf
			Next
			For let = 1 To 8									; Draw all 8 score characters.
				a$ = Mid$(hiscore$(hx,2),let,1)					; Get character.
				as = Asc(a$)									; Get ascii code.
				If a$ <> " "
					; Draw the character on screen..
					; The as - 32 will make sure the
					; correct character is taken from
					; the font.
					DrawImage hfnt,354 + (let * 32),140 + (32 * hx),as - 32
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
	enter = LoadImage("gfx\highscore.bmp")							; Load the hiscore logo.
	hfnt  = LoadAnimImage("gfx\hifont.bmp",32,27,0,60)				; Load the font, cut it.

	fntcnt = 33													; Start of font parts needed. (frame in image)
	lets = 1													; Number of entered characters.
	done = False												; Set check for player done to false.
	name$ = ""													; Empty the name.
	While done = False
		update_stars()

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


.leveldata
; level 1
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1
Data 2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2
Data 3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3
Data 4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4
Data 3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3
Data 2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2
Data 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
; level 2
Data 0,0,0,0,0,0,0,2,2,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,2,2,0,0,0,0,0,0,0
Data 0,7,1,1,7,0,0,2,2,0,0,7,2,2,7,0
Data 0,7,1,1,7,0,0,2,2,0,0,7,2,2,7,0
Data 0,7,7,7,7,0,0,2,2,0,0,7,7,7,7,0
Data 0,0,0,0,0,0,0,2,2,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,2,2,0,0,0,0,0,0,0
Data 1,1,1,1,1,1,1,2,1,1,1,1,1,1,1,1
Data 1,1,1,1,1,1,1,1,2,1,1,1,1,1,1,1
Data 8,8,8,8,8,8,8,2,2,8,8,8,8,8,8,8
Data 0,0,0,0,0,0,0,2,2,0,0,0,0,0,0,0
Data 0,7,4,4,7,0,0,2,2,0,0,7,3,3,7,0
Data 0,7,4,4,7,0,0,2,2,0,0,7,3,3,7,0
Data 0,7,7,7,7,0,0,2,2,0,0,7,7,7,7,0
Data 0,0,0,0,0,0,0,2,2,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,2,2,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
; level 3
Data 1,0,0,0,4,3,2,1,1,2,3,4,0,0,0,1
Data 2,1,0,0,0,4,3,2,2,3,4,0,0,0,1,2
Data 3,2,1,0,0,0,4,3,3,4,0,0,0,1,2,3
Data 4,3,2,1,0,0,0,4,4,0,0,0,1,2,3,4
Data 5,4,3,2,1,0,0,0,0,0,0,1,2,3,4,5
Data 6,5,4,3,2,1,0,0,0,0,1,2,3,4,5,6
Data 0,6,5,4,3,2,1,0,0,1,2,3,4,5,6,0
Data 0,0,6,5,4,3,2,1,1,2,3,4,5,6,0,0
Data 0,0,0,6,5,4,3,2,2,3,4,5,6,0,0,0
Data 4,0,0,0,6,5,4,3,3,4,5,6,0,0,0,4
Data 3,4,0,0,0,6,5,4,4,5,6,0,0,0,4,3
Data 2,3,4,0,0,0,6,5,5,6,0,0,0,4,3,2
Data 1,2,3,4,0,0,0,6,6,0,0,0,4,3,2,1
Data 1,2,1,2,1,2,1,2,1,2,1,2,1,2,1,2
Data 3,4,3,4,3,4,3,4,3,4,3,4,3,4,3,4
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
; level 4
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,7,1,0,0,0,0,0,0,0,0,1,7,0,0
Data 0,0,7,2,1,0,0,0,0,0,0,1,2,7,0,0
Data 0,0,7,3,2,1,0,0,0,0,1,2,3,7,0,0
Data 0,0,7,4,3,2,1,0,0,1,2,3,4,7,0,0
Data 0,0,7,5,4,3,2,1,1,2,3,4,5,7,0,0
Data 0,0,7,6,5,4,3,2,2,3,4,5,6,7,0,0
Data 0,0,7,5,6,5,4,3,3,4,5,6,5,7,0,0
Data 0,0,7,4,5,6,5,4,4,5,6,5,4,7,0,0
Data 0,0,7,3,4,5,6,5,5,6,5,4,3,7,0,0
Data 0,0,7,2,3,4,5,6,6,5,4,3,2,7,0,0
Data 0,0,7,1,2,3,4,5,5,4,3,2,1,7,0,0
Data 0,0,7,7,7,7,7,7,7,7,7,7,7,7,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
; level 5
Data 7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7
Data 7,1,7,3,7,5,7,1,1,7,6,7,4,7,2,7
Data 7,1,7,3,7,5,7,2,2,7,6,7,4,7,2,7
Data 7,1,7,3,7,5,7,3,3,7,6,7,4,7,2,7
Data 7,1,7,3,7,0,7,4,4,7,0,7,4,7,2,7
Data 7,1,7,3,7,0,7,5,5,7,0,7,4,7,2,7
Data 7,1,7,0,7,0,7,6,6,7,0,7,0,7,2,7
Data 7,1,7,0,7,0,7,5,5,7,0,7,0,7,2,7
Data 7,0,7,0,7,0,7,4,4,7,0,7,0,7,0,7
Data 7,0,7,0,0,0,7,3,3,7,0,0,0,7,0,7
Data 7,0,7,0,0,0,7,2,2,7,0,0,0,7,0,7
Data 7,0,7,0,0,0,7,1,1,7,0,0,0,7,0,7
Data 7,0,0,0,0,0,7,8,8,7,0,0,0,0,0,7
Data 7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7
Data 7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7
Data 7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
; level 6
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,1,1,1,1,0,0,0,0,0,0
Data 0,0,0,0,1,1,2,2,2,2,1,1,0,0,0,0
Data 0,0,0,1,2,2,3,3,3,3,2,2,1,0,0,0
Data 0,0,1,2,3,3,4,4,4,4,3,3,2,1,0,0
Data 0,1,2,3,4,4,5,5,5,5,4,4,3,2,1,0
Data 0,1,2,3,4,5,6,6,6,6,5,4,3,2,1,0
Data 1,2,3,4,5,6,5,5,5,5,6,5,4,3,2,1
Data 1,2,3,4,5,6,5,8,8,5,6,5,4,3,2,1
Data 1,2,3,4,5,6,5,8,8,5,6,5,4,3,2,1
Data 1,2,3,4,5,6,5,5,5,5,6,5,4,3,2,1
Data 0,1,2,3,4,5,6,6,6,6,5,4,3,2,1,0
Data 0,1,2,3,4,4,5,5,5,5,4,4,3,2,1,0
Data 0,0,1,2,3,3,4,4,4,4,3,3,2,1,0,0
Data 0,0,0,1,2,2,3,3,3,3,2,2,1,0,0,0
Data 0,0,0,0,1,1,2,2,2,2,1,1,0,0,0,0
Data 0,0,0,0,0,0,1,1,1,1,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
; level 7
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 7,7,0,3,3,3,3,0,0,3,3,3,3,0,7,7
Data 7,1,0,3,4,4,3,0,0,3,5,5,3,0,1,7
Data 7,7,0,3,4,4,3,0,0,3,5,5,3,0,7,7
Data 7,1,0,3,3,3,3,0,0,3,3,3,3,0,1,7
Data 7,7,0,0,0,0,0,6,6,0,0,0,0,0,7,7
Data 7,1,0,0,0,0,0,6,6,0,0,0,0,0,1,7
Data 7,7,0,3,3,3,3,0,0,3,3,3,3,0,7,7
Data 7,1,0,3,5,5,3,0,0,3,4,4,3,0,1,7
Data 7,7,0,3,5,5,3,0,0,3,4,4,3,0,7,7
Data 7,1,0,3,3,3,3,0,0,3,3,3,3,0,1,7
Data 7,7,0,0,0,0,0,0,0,0,0,0,0,0,7,7
Data 7,1,0,0,0,0,0,0,0,0,0,0,0,0,1,7
Data 7,7,8,8,8,8,8,2,2,8,8,8,8,8,7,7
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
; level 8
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 1,1,0,0,5,5,0,0,4,4,0,0,3,3,0,0
Data 1,1,0,0,5,5,0,0,4,4,0,0,3,3,0,0
Data 0,0,1,1,0,0,5,5,0,0,4,4,0,0,3,3
Data 0,0,1,1,0,0,5,5,0,0,4,4,0,0,3,3
Data 2,2,0,0,1,1,0,0,5,5,0,0,4,4,0,0
Data 2,2,0,0,1,1,0,0,5,5,0,0,4,4,0,0
Data 0,0,2,2,0,0,1,1,0,0,5,5,0,0,4,4
Data 0,0,2,2,0,0,1,1,0,0,5,5,0,0,4,4
Data 3,3,0,0,2,2,0,0,1,1,0,0,5,5,0,0
Data 3,3,0,0,2,2,0,0,1,1,0,0,5,5,0,0
Data 0,0,3,3,0,0,2,2,0,0,1,1,0,0,5,5
Data 0,0,3,3,0,0,2,2,0,0,1,1,0,0,5,5
Data 4,4,0,0,3,3,0,0,2,2,0,0,1,1,0,0
Data 4,4,0,0,3,3,0,0,2,2,0,0,1,1,0,0
Data 0,0,4,4,0,0,3,3,0,0,2,2,0,0,1,1
Data 0,0,4,4,0,0,3,3,0,0,2,2,0,0,1,1
Data 5,5,0,0,4,4,0,0,3,3,0,0,2,2,0,0
Data 5,5,0,0,4,4,0,0,3,3,0,0,2,2,0,0
; level 9
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 5,5,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 5,5,5,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 4,5,5,5,0,0,0,0,0,0,0,0,0,0,0,0
Data 4,4,5,5,5,0,0,0,0,0,0,0,0,0,0,0
Data 4,4,4,5,5,5,0,0,0,0,0,0,0,0,0,0
Data 3,4,4,4,5,5,5,0,0,0,0,0,0,0,0,0
Data 3,3,4,4,4,5,5,5,0,0,0,0,0,0,0,0
Data 3,3,3,4,4,4,5,5,5,0,0,0,0,0,0,0
Data 2,3,3,3,4,4,4,5,5,5,0,0,0,0,0,0
Data 2,2,3,3,3,4,4,4,5,5,5,0,0,0,0,0
Data 2,2,2,3,3,3,4,4,4,5,5,5,0,0,0,0
Data 1,2,2,2,3,3,3,4,4,4,5,5,5,0,0,0
Data 1,1,2,2,2,3,3,3,4,4,4,5,5,5,0,0
Data 1,1,1,2,2,2,3,3,3,4,4,4,5,5,5,0
Data 7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,8
; level 10
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,1,1,0,0,4,4,0,0,0
Data 0,0,0,0,0,0,1,2,1,1,0,4,4,0,0,0
Data 0,0,0,0,0,1,2,1,2,1,1,4,4,0,0,0
Data 0,0,0,0,1,2,1,2,1,2,1,1,4,0,0,0
Data 0,0,0,1,2,1,2,1,2,1,2,1,1,0,0,0
Data 0,0,1,2,1,2,1,2,1,2,1,2,1,1,0,0
Data 0,1,2,1,2,1,2,1,2,1,2,1,2,1,1,0
Data 1,3,3,3,3,3,3,3,3,3,3,3,3,3,3,1
Data 1,3,5,5,3,5,5,3,5,5,3,3,3,3,3,1
Data 1,3,5,5,3,5,5,3,5,5,3,8,8,8,3,1
Data 1,3,5,5,3,5,5,3,5,5,3,8,8,8,3,1
Data 1,3,3,3,3,3,3,3,3,3,3,8,8,8,3,1
Data 1,3,3,3,3,3,3,3,3,3,3,8,8,8,3,1
Data 1,3,3,3,3,3,3,3,3,3,3,8,8,8,3,1
Data 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
; level 11
Data 1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1
Data 1,0,0,0,3,0,0,0,0,3,0,0,0,0,0,1
Data 1,0,0,3,8,3,0,0,3,8,3,0,0,0,0,1
Data 1,0,0,0,3,0,0,0,0,3,0,0,0,0,0,1
Data 1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1
Data 1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1
Data 1,0,0,0,6,0,0,0,0,6,0,0,0,0,0,1
Data 1,0,0,6,8,6,0,0,6,8,6,0,0,0,0,1
Data 1,0,0,0,6,0,0,0,0,6,0,0,0,0,0,1
Data 2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2
Data 2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
; level 12
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,4,4,4,0,0,0,0,2,2,2,0,0,0,0,0
Data 0,4,4,4,0,0,0,0,2,2,2,0,0,0,0,0
Data 0,4,4,4,0,0,0,0,2,2,2,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,3,3,3,0,0,0,0,0,0,1,1,1,0,0,0
Data 0,3,3,3,0,0,0,0,0,0,1,1,1,0,0,0
Data 0,3,3,3,0,0,5,5,5,0,1,1,1,0,0,0
Data 0,0,0,0,0,0,5,5,5,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,5,5,5,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,4,4,4,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,4,4,4,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,4,4,4,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
; level 13
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,8,3,5,3,5,3,5,3,5,3,5,8,0,0
Data 0,0,8,4,6,4,6,4,6,4,6,4,6,8,0,0
Data 0,0,8,8,8,8,8,8,8,8,8,8,8,8,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,8,2,2,8,0,0,0,8,2,2,8,0,0,0
Data 0,0,8,2,2,8,0,0,0,8,2,2,8,0,0,0
Data 0,0,8,8,8,8,0,0,0,8,8,8,8,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,8,1,1,1,8,0,0,0,0,0,0
Data 0,0,0,0,0,8,8,8,8,8,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
; level 14
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,3,2,1,0,8,8,8,8,0,0,1,2,3,0,0
Data 0,0,3,2,1,0,0,0,0,0,1,2,3,0,0,0
Data 0,0,0,3,2,1,0,0,0,1,2,3,0,0,0,0
Data 0,0,0,0,3,2,1,0,1,2,3,0,0,0,0,0
Data 0,0,0,0,0,3,2,1,2,3,0,0,0,0,0,0
Data 0,0,0,0,0,0,3,2,3,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,3,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,3,2,3,0,0,0,0,0,0,0
Data 0,0,0,0,0,3,2,1,2,3,0,0,0,0,0,0
Data 0,0,0,0,3,2,1,0,1,2,3,0,0,0,0,0
Data 0,0,0,3,2,1,0,0,0,1,2,3,0,0,0,0
Data 0,0,3,2,1,0,0,0,0,0,1,2,3,0,0,0
Data 0,3,2,1,0,0,0,0,0,0,0,1,2,3,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
; level 15
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,8,8,8,8,0,0,8,8,8,8,0,0,0,0
Data 0,0,8,3,5,8,0,0,8,4,6,8,0,0,0,0
Data 0,0,8,3,5,8,0,0,8,4,6,8,0,0,0,0
Data 0,0,8,8,8,8,0,0,8,8,8,8,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,8,8,8,8,0,0,0,0,0,0,0
Data 0,0,0,0,0,8,1,1,8,0,0,0,0,0,0,0
Data 0,0,0,0,0,8,1,1,8,0,0,0,0,0,0,0
Data 0,0,0,0,0,8,8,8,8,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
; level 16
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,1,1,1,0,0,0,0,1,1,1,0,0,0
Data 0,0,0,1,1,1,0,0,0,0,1,1,1,0,0,0
Data 0,0,0,1,1,1,0,0,0,0,1,1,1,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,2,2,2,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,2,2,2,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,2,2,2,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,3,0,0,0,0,0,0,0,0,0,3,0,0,0
Data 0,0,0,3,0,0,0,0,0,0,0,3,0,0,0,0
Data 0,0,0,0,3,3,3,3,3,3,3,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
; level 17
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,2,0,0,3,0,0,4,0,0,0,0,0
Data 0,0,0,2,7,2,3,7,3,4,7,4,0,0,0,0
Data 0,0,0,0,2,0,0,3,0,0,4,0,0,0,0,0
Data 0,0,0,0,1,0,0,6,0,0,5,0,0,0,0,0
Data 0,0,0,1,7,1,6,7,6,5,7,5,0,0,0,0
Data 0,0,0,0,1,0,0,6,0,0,5,0,0,0,0,0
Data 0,0,0,0,2,0,0,3,0,0,4,0,0,0,0,0
Data 0,0,0,2,7,2,3,7,3,4,7,4,0,0,0,0
Data 0,0,0,0,2,0,0,3,0,0,4,0,0,0,0,0
Data 0,0,0,0,1,0,0,6,0,0,5,0,0,0,0,0
Data 0,0,0,1,7,1,6,7,6,5,7,5,0,0,0,0
Data 0,0,0,0,1,0,0,6,0,0,5,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
; level 18
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1
Data 2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2
Data 3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4
Data 5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5
Data 6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
; level 19
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,3,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,3,3,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,3,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,3,3,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,3,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,1,1,1,1,1,1,0,0,0,4,0,0
Data 2,2,0,0,1,1,1,1,1,1,0,4,4,4,0,0
Data 0,2,2,0,1,1,7,7,1,1,4,4,0,0,0,0
Data 0,0,2,2,1,1,7,7,1,1,0,0,0,0,0,0
Data 0,0,0,0,1,1,1,1,1,1,0,0,0,0,0,0
Data 0,0,0,0,1,1,1,1,1,1,0,0,0,0,0,0
Data 0,0,0,0,0,0,5,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,5,5,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,5,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,5,5,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
; level 20
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,0
Data 0,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0
Data 0,0,0,1,0,0,8,0,0,1,0,0,0,0,0,0
Data 0,0,1,0,0,8,2,8,0,0,1,0,0,0,0,0
Data 0,0,0,1,0,0,8,0,0,1,0,0,0,0,0,0
Data 0,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0
Data 0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,7,0,0,0,7,0,0,0,7,0,0,0,7,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0


; empty level
;Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
;Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
;Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
;Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
;Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
;Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
;Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
;Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
;Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
;Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
;Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
;Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
;Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
;Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
;Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
;Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
;Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
;Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
;Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
;Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0

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