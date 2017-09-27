;*********************
;*					 *
;*    BLITZ DICEY    *
;*	 				 *
;*    Written  By    *
;*  Darryl Bartlett  *
;*					 *
;* Using Blitz Basic *
;*					 *
;*********************


;Set up and initialise the screen resolution
Const width=800,height=600
Graphics width,height
SetBuffer BackBuffer()

;Define all the global variable
Dim hsname$(10)
Dim hsscore%(10)
Dim dice%(6)
Dim rack%(6)
Dim player_individual_score%(4, 14)
Dim score_used%(4, 14)
Dim score_type$(14)
Dim lookupy1%(14)
Dim lookupy2%(14)
Dim overall_score%(4)
Dim player_name$(4)

Global quit_the_game, rackx, dicex, di, r, inplay, no_in_dice, no_in_rack, status
Global quit_game = 0, option_selected = 0, number_of_players = 1, x, y, c
Global originaly, bx1, bx2, movement, redraw, refresh, chars_on_screen, t$
Global rolls_left, player_turn, roll_number, end_turn, x_pos, y_pos
Global tempx, tempy, mouse_used, scoring_category, xm, ym, daz_timer
Global button_selected, score_selected, pn

;LOAD ALL THE GAME GRAPHICS
Global screen_grab

;Title screen graphics
Global Logo = LoadImage("logo.bmp")

;The DICEY logo graphics
Global bitmap1 = LoadImage("bitmap1.bmp")
Global hdrD, hdrI, hdrC, hdrE, hdrY

;Menu screen graphics
Global bitmap2 = LoadImage("bitmap2.bmp")
Global backtile, arrow, font1_a, font1_b, font1_c, font1_d 
Global font1_e, font1_f, font1_g, font1_h, font1_i, font1_j 
Global font1_k, font1_l, font1_m, font1_n, font1_o, font1_p 
Global font1_q, font1_r, font1_s, font1_t, font1_u, font1_v 
Global font1_w, font1_x, font1_y, font1_z, font2_a, font2_b 
Global font2_c, font2_d, font2_e, font2_f, font2_g, font2_h 
Global font2_i, font2_j, font2_k, font2_l, font2_m, font2_n
Global font2_o, font2_p, font2_q, font2_r, font2_s, font2_t
Global font2_u, font2_v, font2_w, font2_x, font2_y, font2_z

;High score table graphics
Global bitmap3 = LoadImage("bitmap3.bmp")
Global hsback = LoadImage("hsback.bmp")
Global hsframe = LoadImage("hsframe.bmp")
Global font3_dot, font3_a, font3_b
Global font3_c, font3_d, font3_e, font3_f, font3_g, font3_h 
Global font3_i, font3_j, font3_k, font3_l, font3_m, font3_n
Global font3_o, font3_p, font3_q, font3_r, font3_s, font3_t
Global font3_u, font3_v, font3_w, font3_x, font3_y, font3_z

;In game graphics
Global bitmap5 = LoadImage("bitmap5.bmp")
Global mainback = LoadImage("mainback.bmp")
Global border1, border2 ,border3, border4, border5
Global border6, border7, border8, border9, border10
Global border11, border12, border13, border14, border15
Global border16, dice1, dice2, dice3, dice4, dice5, dice6
Global cursor, play1_1, play1_2, play2_1, play2_2
Global play3_1, play3_2, play4_1, play4_2, upbut1, upbut2
Global downbut1, downbut2, btn1_1, btn1_2, btn1_3
Global btn2_1, btn2_2, btn2_3, btn3_1, btn3_2, btn3_3
Global btn4_1, btn4_2, btn4_3

;Outro screen #1 graphics
Global bitmap4 = LoadImage("bitmap4.bmp")
Global Outro_Background = LoadImage("out1back.bmp")
Global Line1, Line2, Line3, Line4, Line5, Line6, BlankLine 

;Outro screen #2 graphics
Global Outro2 = LoadImage("outro2.bmp")

;Set up the fonts used throughout the game
Global Font = LoadFont ("times", 32, 1, 0, 0)
Global Font2 = LoadFont("times", 56, 1, 0, 0)
Global Font3 = LoadFont("times", 24, 1, 0, 0)
Global Font4 = LoadFont("times", 16, 1, 0, 0)
SetFont Font

;Load the game sound fx
Global snd1 = LoadSound("snd1.wav")
Global snd2 = LoadSound("snd2.wav")
Global snd3 = LoadSound("snd3.wav")
Global snd4 = LoadSound("snd4.wav")


;Fill in the score_type$ array
score_type$(0) = "ONEs"
score_type$(1) = "TWOs"
score_type$(2) = "THREEs"
score_type$(3) = "FOURs"
score_type$(4) = "FIVEs"
score_type$(5) = "SIXs"
score_type$(6) = "3 OF A KIND"
score_type$(7) = "4 OF A KIND"
score_type$(8) = "5 OF A KIND"
score_type$(9) = "FULL HOUSE"
score_type$(10) = "SMALL STRAIGHT"
score_type$(11) = "MEDIUM STRAIGHT"
score_type$(12) = "LARGE STRAIGHT"
score_type$(13) = "CHANCE"

Gosub CutGraphics
FreeImage bitmap1
FreeImage bitmap2
FreeImage bitmap3
FreeImage bitmap4
FreeImage bitmap5

Gosub LoadHighScores	;Load in the high score table
Gosub IntroScreen		;Display the intro screen
Gosub MainMenu			;Display the main menu
Gosub Outro1			;Display the first outro screen
Gosub Outro2			;Display the second outro screen

;Release all the memory used up by the game graphics
FreeImage Logo
FreeImage hdrD
FreeImage hdrI
FreeImage hdrC
FreeImage hdrE 
FreeImage hdrY
FreeImage backtile
FreeImage arrow
FreeImage font1_a
FreeImage font1_b
FreeImage font1_c
FreeImage font1_d
FreeImage font1_e
FreeImage font1_f
FreeImage font1_g
FreeImage font1_h
FreeImage font1_i
FreeImage font1_j
FreeImage font1_k
FreeImage font1_l
FreeImage font1_m
FreeImage font1_n
FreeImage font1_o
FreeImage font1_p
FreeImage font1_q
FreeImage font1_r
FreeImage font1_s
FreeImage font1_t
FreeImage font1_u
FreeImage font1_v
FreeImage font1_w
FreeImage font1_x
FreeImage font1_y
FreeImage font1_z
FreeImage font2_a
FreeImage font2_b
FreeImage font2_c
FreeImage font2_d
FreeImage font2_e
FreeImage font2_f
FreeImage font2_g
FreeImage font2_h
FreeImage font2_i
FreeImage font2_j
FreeImage font2_k
FreeImage font2_l
FreeImage font2_m
FreeImage font2_n
FreeImage font2_o
FreeImage font2_p
FreeImage font2_q
FreeImage font2_r
FreeImage font2_s
FreeImage font2_t
FreeImage font2_u
FreeImage font2_v
FreeImage font2_w
FreeImage font2_x
FreeImage font2_y
FreeImage font2_z
FreeImage hsframe
FreeImage hsback
FreeImage font3_dot
FreeImage font3_a
FreeImage font3_b
FreeImage font3_c
FreeImage font3_d
FreeImage font3_e
FreeImage font3_f
FreeImage font3_g
FreeImage font3_h
FreeImage font3_i
FreeImage font3_j
FreeImage font3_k
FreeImage font3_l
FreeImage font3_m
FreeImage font3_n
FreeImage font3_o
FreeImage font3_p
FreeImage font3_q
FreeImage font3_r
FreeImage font3_s
FreeImage font3_t
FreeImage font3_u
FreeImage font3_v
FreeImage font3_w
FreeImage font3_x
FreeImage font3_y
FreeImage font3_z
FreeImage mainback
FreeImage border1
FreeImage border2
FreeImage border3
FreeImage border4
FreeImage border5
FreeImage border6
FreeImage border7
FreeImage border8
FreeImage border9
FreeImage border10
FreeImage border11
FreeImage border12
FreeImage border13
FreeImage border14
FreeImage border15
FreeImage border16
FreeImage dice1
FreeImage dice2
FreeImage dice3
FreeImage dice4
FreeImage dice5
FreeImage dice6
FreeImage cursor
FreeImage play1_1
FreeImage play1_2
FreeImage play2_1
FreeImage play2_2
FreeImage play3_1
FreeImage play3_2
FreeImage play4_1
FreeImage play4_2
FreeImage upbut1
FreeImage upbut2
FreeImage downbut1
FreeImage downbut2
FreeImage btn1_1
FreeImage btn1_2
FreeImage btn1_3
FreeImage btn2_1
FreeImage btn2_2
FreeImage btn2_3
FreeImage btn3_1
FreeImage btn3_2
FreeImage btn3_3
FreeImage btn4_1
FreeImage btn4_2
FreeImage btn4_3
FreeImage Outro_Background
FreeImage BlankLine
FreeImage Line1
FreeImage Line2
FreeImage Line3
FreeImage Line4
FreeImage Line5
FreeImage Line6
FreeImage Outro2

;Release memory used by the sound fx
FreeSound snd1
FreeSound snd2
FreeSound snd3
FreeSound snd4

;End the game
End




;************************************************************
;*															*
;* INTRO SCREEN												*
;*															*
;* Display the main game logo when the game first loads		*
;*															*
;************************************************************

.IntroScreen
;Wait until a key is pressed
While GetKey() = 0
	Cls
	DrawImage Logo, 0, 0
	Flip
Wend

;Scroll the logo image up the screen
For counter = 0 To height Step 10
	Cls
	DrawImage Logo, 0, 0-counter
	Flip
	VWait 2
Next

Return

;**************** END OF INTRO SCREEN SOURCE CODE ****************





;************************************************************
;*															*
;* HIGH SCORE TABLE LOADING									*
;*															*
;* Loads the high score table from disk to memory	 		*
;*															*
;************************************************************

.LoadHighScores

;Make sure high score table array is empty
For counter = 0 To 9
	hsname$(counter) = ""
	hsscore%(counter) = 0
Next 

;Make sure high score table text file exists
If FileSize("hs.txt") <> 0
	hsfile = ReadFile("hs.txt")
	
	;Read in each of the 10 entries in the high score table
	For counter = 0 To 9
		hsname$(counter) = ReadString (hsfile)	
		hsscore%(counter) = ReadInt (hsfile)
	Next
	CloseFile hsfile
Else
	;If high score table doesn't exist, create it
	temp_sc = 500
	For counter = 0 To 9
		;hsname$(counter) = "DARRYL BARTLETT"
		hsname$(counter) = "DARRYL"
		hsscore%(counter) = temp_sc
		temp_sc = temp_sc - 50
	Next 
	 
	hsfile = WriteFile("hs.txt")
	For counter = 0 To 9
		WriteString hsfile, hsname$(counter)
		WriteInt hsfile, hsscore%(counter)
	Next
	CloseFile hsfile
End If

Return

;**************** END OF HIGH SCORE TABLE LOADING SOURCE CODE ****************





;************************************************************
;*															*
;* MAIN MENU												*
;*															*
;* Handle displaying and selecting options on the main menu *
;*															*
;************************************************************

.MainMenu

;Define the type structure used by the horizontal text scrolling routine
Type MenuScroll
	Field x
	Field c$
End Type

x=165
y = 315
c = 1
originaly = y
bx1 = x - 10
bx2 = x + 10
update = 0
str_pos = 0
movement = 0
redraw = 1
refresh = 0
scrolling_message$ = "BLITZ DICEY WRITTEN BY DARRYL BARTLETT USING BLITZ BASIC           "
number_of_players = 1

;Clear spaces from keyboard buffer
While KeyDown(57) > 0
Wend
While KeyHit(57) > 0
Wend

;Repeat the whole process until the quit game option is selected
While(quit_game = 0)
	music=PlayMusic("music1.mid")
	
	;Initialise the start of the scrolling message
	str_pos = 1
	chars_on_screen = 0
	
	;Keep refreshing the menu screen until a menu option is selected
	While(option_selected = 0)
		If Not ChannelPlaying(music) 
			music=PlayMusic("music1.mid")
		End If
		
		Cls
		
		;Display the background tile
		TileImage backtile, 0, 0

		;Draw DICEY logo		
		DrawImage hdrD, 260, 60
		DrawImage hdrI, 330, 60
		DrawImage hdrC, 380, 60
		DrawImage hdrE, 440, 60
		DrawImage hdrY, 500, 60

		;Display MAIN MENU menu option
		DrawImage font1_m, 220, 200
		DrawImage font1_a, 265, 200
		DrawImage font1_i, 310, 200
		DrawImage font1_n, 355, 200
		DrawImage font1_m, 445, 200
		DrawImage font1_e, 490, 200
		DrawImage font1_n, 535, 200
		DrawImage font1_u, 580, 200

		;Display START GAME menu option
		DrawImage font1_s, 195, 300
		DrawImage font1_t, 240, 300
		DrawImage font1_a, 285, 300
		DrawImage font1_r, 330, 300
		DrawImage font1_t, 375, 300
		DrawImage font1_g, 465, 300
		DrawImage font1_a, 510, 300
		DrawImage font1_m, 555, 300
		DrawImage font1_e, 600, 300

		;Display NO OF PLAYERS menu option
		DrawImage font1_p, 195, 350
		DrawImage font1_l, 240, 350
		DrawImage font1_a, 285, 350
		DrawImage font1_y, 330, 350
		DrawImage font1_e, 375, 350
		DrawImage font1_r, 420, 350
		DrawImage font1_s, 465, 350
		SetFont Font2
		Color 255,255,0
		Select number_of_players
			Case 1
				Text 538, 338, "[01]"
			Case 2
				Text 538, 338, "[02]"
			Case 3
				Text 538, 338, "[03]"
			Case 4
				Text 538, 338, "[04]"
		End Select
		Color 255,255,255
		SetFont Font

		;Display VIEW HIGH SCORES menu option
		DrawImage font1_v, 60, 400
		DrawImage font1_i, 105, 400
		DrawImage font1_e, 150, 400
		DrawImage font1_w, 195, 400
		DrawImage font1_h, 285, 400
		DrawImage font1_i, 330, 400
		DrawImage font1_g, 375, 400
		DrawImage font1_h, 420, 400
		DrawImage font1_s, 510, 400
		DrawImage font1_c, 555, 400
		DrawImage font1_o, 600, 400
		DrawImage font1_r, 645, 400
		DrawImage font1_e, 690, 400
		DrawImage font1_s, 735, 400

		;Display QUIT GAME menu option
		DrawImage font1_q, 215, 450
		DrawImage font1_u, 265, 450
		DrawImage font1_i, 310, 450
		DrawImage font1_t, 355, 450
		DrawImage font1_g, 445, 450
		DrawImage font1_a, 490, 450
		DrawImage font1_m, 535, 450
		DrawImage font1_e, 580, 450
		
		;Update the horizontal text scroller display
		UpdateMenuScroll()
		VWait(10)

		;If there are less than 50 characters on the screen for the
		;horizontal text scroller, add a New character to the scroll Line
		If chars_on_screen < 51
			t$ = Mid$(scrolling_message$, str_pos, 1)	;Get the next character to be added to the scroll
			CreateMenuScroll(t$)						;Add the next character in the scrolling message to the scroll line
			str_pos = str_pos + 1
			chars_on_screen = chars_on_screen + 1

			;If end of scrolling message found, loop back to beginning
			If str_pos > Len(scrolling_message$)
				str_pos = 1
			End If
		End If

		movement = 0

		;If the UP ARROW key pressed, move to the previous menu option
		If KeyDown(200)
			If y > 350
				y = originaly
				y = y - 50
				originaly = y
				movement = 1
				PlaySound snd2
			End If
		End If

		;If DOWN ARROW key pressed, move to the next menu option
		If KeyDown(208)
			If y < 450
				y = originaly
				y = y + 50
				originaly = y
				movement = 1
				PlaySound snd2
			End If
		End If

		;If LEFT ARROW key is pressed when arrow is by the number of players menu option
		If KeyDown(203)
			If originaly = 365
				number_of_players = number_of_players - 1
				PlaySound snd2
			End If

			If number_of_players < 1 Then
				number_of_players = 4
			End If
		End If

		;If RIGHT ARROW key pressed when arrow is by number of players menu option
		If KeyDown(205)
			If originaly = 365
				number_of_players = number_of_players + 1
				PlaySound snd2
			End If

			If number_of_players > 4 Then
				number_of_players = 1
			End If
		End If

		;If SPACE bar is pressed
		If KeyDown(57)
			option_selected = 1
			PlaySound snd4
		End If

		;If a new menu option is selected move
		;arrow pointer to the new x co-ordinate
		If movement > 0
			If y = 315
				x = 165
			End If

			If y = 365
				x = 165
			End If

			If y = 415
				x = 30
			End If

			If y = 465
				x = 185
			End If

			bx1 = x - 10
			bx2 = x + 10
		End If

		;Move the arrow pointer animation and check if movement direction has hit its boundaries
		x = x + c
		y = y + -c

		If x >= bx2
			c = -1
		End If

		If x < bx1
			c = 1
		End If

		;Draw the arrow pointer
		DrawImage arrow, x, y

		Flip
	Wend

	;Handle which menu option was selected
	Select originaly
		Case 315			;Start Game			
			StopChannel(music)
			Gosub EnterNames			
			Gosub MainGame
			DeleteMenuScroll()
		Case 365			;No of players
			;Ignore option
		Case 415			;View high score table
			Gosub View_High_Score_Table
			DeleteMenuScroll()
			While KeyHit(57) > 0
			Wend
		Case 465			;Quit game
			StopChannel music
			quit_game = 1
			DeleteMenuScroll()
	End Select

	option_selected = 0
Wend

Return


;--------------------------------------------------------
;Function to add a new character to the horizontal scroll
Function CreateMenuScroll(t$)
	d.MenuScroll = New MenuScroll
	d\x = 800
	d\c = t$
End Function


;---------------------------------------------------------------------------------
;Function to display the current characters on the screen of the horizontal scroll
Function UpdateMenuScroll()
	;For each scroll character on the screen
	For d.MenuScroll = Each MenuScroll
		;Display the bitmap font image depending on the character to scroll
		Select d\c
			Case "A"
				DrawImage font2_a, d\x, 580
			Case "B"
				DrawImage font2_b, d\x, 580
			Case "C"
				DrawImage font2_c, d\x, 580
			Case "D"
				DrawImage font2_d, d\x, 580
			Case "E"
				DrawImage font2_e, d\x, 580
			Case "F"
				DrawImage font2_f, d\x, 580
			Case "G"
				DrawImage font2_g, d\x, 580
			Case "H"
				DrawImage font2_h, d\x, 580
			Case "I"
				DrawImage font2_i, d\x, 580
			Case "J"
				DrawImage font2_j, d\x, 580
			Case "K"
				DrawImage font2_k, d\x, 580
			Case "L"
				DrawImage font2_l, d\x, 580
			Case "M"
				DrawImage font2_m, d\x, 580
			Case "N"
				DrawImage font2_n, d\x, 580
			Case "O"
				DrawImage font2_o, d\x, 580
			Case "P"
				DrawImage font2_p, d\x, 580
			Case "Q"
				DrawImage font2_q, d\x, 580
			Case "R"
				DrawImage font2_r, d\x, 580
			Case "S"
				DrawImage font2_s, d\x, 580
			Case "T"
				DrawImage font2_t, d\x, 580
			Case "U"
				DrawImage font2_u, d\x, 580
			Case "V"
				DrawImage font2_v, d\x, 580
			Case "W"
				DrawImage font2_w, d\x, 580
			Case "X"
				DrawImage font2_x, d\x, 580
			Case "Y"
				DrawImage font2_y, d\x, 580
			Case "Z"
				DrawImage font2_z, d\x, 580
		End Select

		;Move each scroll character on the screen 16 pixels to the left
		d\x = d\x - 16

		;If character has gone off the edge of the screen, delete it
		If d\x < 0
			Delete d
			chars_on_screen = chars_on_screen - 1
		End If
	Next
End Function

;---------------------------------------------------------
;Delete all the scroll messages characters from the screen
Function DeleteMenuScroll()
	For d.MenuScroll = Each MenuScroll
		Delete d
	Next
End Function


;**************** END OF MAIN MENU SOURCE CODE ****************





;************************************************************
;*															*
;* OUTRO 1 SCREEN											*
;*															*
;* Display goodbye messages scrolling up the screen 		*
;*															*
;************************************************************

.Outro1

Type UpScroll
	Field y, id
End Type

loop = 0
iterations = 4

;Clear out keyboard buffer
While GetKey() > 0
Wend

While KeyHit(57) > 0
Wend 

music=PlayMusic("music3.mid")
;Repeat until a key is pressed
While GetKey() = 0
	Cls

	If Not ChannelPlaying(music)
		music=PlayMusic("music3.mid")
	End If
	
	;Display the background image
	DrawImage Outro_Background, 0, 0

	;Handle the delay and also adding the next line to be scrolled up the screen
	iterations = iterations + 1
	If iterations > 40
		loop = loop + 1
		If loop > 7 Then
			loop = 1
		End If

		CreateScroll(loop)

		iterations = 0
	End If

	;Update the scrolling message
	UpdateScroll()
	Flip
Wend

Cls

Return


;-----------------------------------------
;Add a new up scroll message to the screen
Function CreateScroll(loop)
	m.UpScroll = New UpScroll
	m\y = height
	m\id = loop
End Function


;----------------------------------------
;Update the scroll messages on the screen
Function UpdateScroll()
	For m.UpScroll = Each UpScroll
		;Display the correct graphical image depending on the scroll message number
		Select m\id
			Case 1
				DrawImage Line1, (width-ImageWidth(Line1))/2, m\y
			Case 2
				DrawImage Line2, (width-ImageWidth(Line2))/2, m\y
			Case 3
				DrawImage Line3, (width-ImageWidth(Line3))/2, m\y
			Case 4
				DrawImage Line4, (width-ImageWidth(Line4))/2, m\y
			Case 5
				DrawImage Line5, (width-ImageWidth(Line5))/2, m\y
			Case 6
				DrawImage Line6, (width-ImageWidth(Line6))/2, m\y
			Case 7
				DrawImage BlankLine, (width-ImageWidth(BlankLine))/2, m\y
		End Select
		
		;Move the message 3 pixels up the screen
		m\y = m\y - 3

		;If the message is off the top of the screen, delete it
		If m\y < -30 Then
			Delete m
		End If
	Next
End Function


;----------------------------------------------
;Delete all the scroll messages from the screen
Function DeleteScroll()
	For m.UpScroll = Each UpScroll
		Delete m
	Next
End Function

;**************** END OF OUTRO SCREEN #1 SOURCE CODE ****************





;************************************************************
;*															*
;* OUTRO 2 SCREEN											*
;*															*
;* Display a goodbye graphical image				 		*
;*															*
;************************************************************

;-----------------------------------------------
;OUTRO2
.Outro2

;Clear out keyboard buffer
While GetKey() > 0
Wend 

While KeyHit(57) > 0
Wend 

;Repeat until a key is pressed
While KeyHit(57) = 0
	Cls
	
	;Display the graphic image
	DrawImage Outro2,0,0	
	Flip
Wend

;Scroll the graphic image up the screen
For counter = 0 To height Step 10
	Cls
	DrawImage Outro2,0,0-counter
	Flip
	VWait 2
Next

Return

;**************** END OF OUTRO 2 SCREEN SOURCE CODE ****************







;************************************************************
;*															*
;* VIEW HIGH SCORE TABLE									*
;*															*
;* Displays the high score table on the screen  	 		*
;*															*
;************************************************************

.View_High_Score_Table

SetFont Font

;Clear out keyboard buffer
While GetKey() <> 0
Wend

While GetKey() = 0
	Cls

	;Display background tile and frame
	TileImage hsback
	DrawImage hsframe, 0, 0

	;Draw DICEY logo
	DrawImage hdrD, 260, 40
	DrawImage hdrI, 330, 40
	DrawImage hdrC, 380, 40
	DrawImage hdrE, 450, 40
	DrawImage hdrY, 500, 40
	
	;Display HIGH SCORE TABLE banner
	DrawImage font1_h, 39, 140
	DrawImage font1_i, 84, 140
	DrawImage font1_g, 129, 140
	DrawImage font1_h, 174, 140	
	DrawImage font1_s, 264, 140
	DrawImage font1_c, 309, 140
	DrawImage font1_o, 354, 140
	DrawImage font1_r, 399, 140
	DrawImage font1_e, 444, 140
	DrawImage font1_t, 534, 140
	DrawImage font1_a, 579, 140
	DrawImage font1_b, 625, 140
	DrawImage font1_l, 664, 140
	DrawImage font1_e, 714, 140

	hsx = 44
	hsy = 210
	;For each of the 10 high score table entries
	For counter = 0 To 9
		hsx=44
		;For each letter in the high score person's name
		For counter2 = 1 To Len(hsname$(counter))
			a$ = Mid$(hsname$(counter), counter2, 1)
			Select a$
				Case "A"
					DrawImage font3_a, hsx, hsy
				Case "B"
					DrawImage font3_b, hsx, hsy
				Case "C"
					DrawImage font3_c, hsx, hsy
				Case "D"
					DrawImage font3_d, hsx, hsy
				Case "E"
					DrawImage font3_e, hsx, hsy
				Case "F"
					DrawImage font3_f, hsx, hsy
				Case "G"
					DrawImage font3_g, hsx, hsy
				Case "H"
					DrawImage font3_h, hsx, hsy
				Case "I"
					DrawImage font3_i, hsx, hsy
				Case "J"
					DrawImage font3_j, hsx, hsy
				Case "K"
					DrawImage font3_k, hsx, hsy
				Case "L"
					DrawImage font3_l, hsx, hsy
				Case "M"
					DrawImage font3_m, hsx, hsy
				Case "N"
					DrawImage font3_n, hsx, hsy
				Case "O"
					DrawImage font3_o, hsx, hsy
				Case "P"
					DrawImage font3_p, hsx, hsy
				Case "Q"
					DrawImage font3_q, hsx, hsy
				Case "R"
					DrawImage font3_r, hsx, hsy
				Case "S"
					DrawImage font3_s, hsx, hsy
				Case "T"
					DrawImage font3_t, hsx, hsy
				Case "U"
					DrawImage font3_u, hsx, hsy
				Case "V"
					DrawImage font3_v, hsx, hsy
				Case "W"
					DrawImage font3_w, hsx, hsy
				Case "X"
					DrawImage font3_x, hsx, hsy
				Case "Y"
					DrawImage font3_y, hsx, hsy
				Case "Z"
					DrawImage font3_z, hsx, hsy
			End Select

			hsx = hsx + 32
		Next
		
		;Display the dots which separate the peron's name from their score
		hsx = 530
		For counter3 = 1 To 5
			DrawImage font3_dot, hsx, hsy
			hsx = hsx + 32
		Next

		;Display the score they achieved
		hsx = hsx - 10
		Color 255, 0, 0
		Text hsx + 26, hsy + 5, hsscore%(counter)

		hsy = hsy + 35
	Next

	Flip
Wend
Color 255, 255, 255

Return

;**************** END OF VIEW HIGH SCORE TABLE SOURCE CODE ****************



;************************************************************
;*															*
;* ENTER PLAYERS NAMES SCREEN								*
;*															*
;* Allow all players to enter their names			 		*
;*															*
;************************************************************
.EnterNames
Dim temp_array$(16)

For counter = 0 To 3
	player_name$(counter) = ""
Next 

For pn = 1 To number_of_players
	letter_no = 1
	For counter = 1 To 15
		temp_array$(counter) = "#"
	Next

	;Clear out keyboard buffer
	While KeyHit(28) > 0
	Wend
	
	While KeyHit(57) > 0
	Wend

	While KeyDown(28) = 0
		Cls
		
		;Display the background tile
		TileImage backtile, 0, 0

		;Draw DICEY logo
		DrawImage hdrD, 260, 50
		DrawImage hdrI, 320, 50
		DrawImage hdrC, 380, 50
		DrawImage hdrE, 440, 50
		DrawImage hdrY, 500, 50
		
		;Draw ENTER YOUR NAME banner
		DrawImage font1_e, 64, 150
		DrawImage font1_n, 106, 150
		DrawImage font1_t, 154, 150
		DrawImage font1_e, 199, 150
		DrawImage font1_r, 247, 150
		DrawImage font1_y, 332, 150
		DrawImage font1_o, 377, 150
		DrawImage font1_u, 426, 150
		DrawImage font1_r, 470, 150
		DrawImage font1_n, 557, 150
		DrawImage font1_a, 602, 150
		DrawImage font1_m, 646, 150
		DrawImage font1_e, 692, 150

		;Draw PLAYER banner
		DrawImage font2_p, 60, 240
		DrawImage font2_l, 76, 240
		DrawImage font2_a, 92, 240
		DrawImage font2_y, 108, 240
		DrawImage font2_e, 124, 240
		DrawImage font2_r, 140, 240		
		Select pn
			Case 1
				DrawImage font2_o, 172, 240
				DrawImage font2_n, 188, 240
				DrawImage font2_e, 204, 240
			Case 2
				DrawImage font2_t, 172, 240
				DrawImage font2_w, 188, 240
				DrawImage font2_o, 204, 240
			Case 3
				DrawImage font2_t, 172, 240
				DrawImage font2_h, 188, 240
				DrawImage font2_r, 204, 240
				DrawImage font2_e, 220, 240
				DrawImage font2_e, 236, 240
			Case 4
				DrawImage font2_f, 172, 240
				DrawImage font2_o, 188, 240
				DrawImage font2_u, 204, 240
				DrawImage font2_r, 220, 240			
		End Select
		
		;Draw the characters already entered into the players name field
		aax = 160
		For counter = 1 To 15			
			Select temp_array$(counter)				
				Case "#" :
					DrawImage font3_dot, aax, 300
				Case " " :
					;Ignore character
				Case "A" : 
					DrawImage font3_a, aax, 300							
				Case "B" : 
					DrawImage font3_b, aax, 300
				Case "C" : 
					DrawImage font3_c, aax, 300
				Case "D" : 
					DrawImage font3_d, aax, 300
				Case "E" : 
					DrawImage font3_e, aax, 300
				Case "F" : 
					DrawImage font3_f, aax, 300
				Case "G" : 
					DrawImage font3_g, aax, 300
				Case "H" : 
					DrawImage font3_h, aax, 300
				Case "I" : 
					DrawImage font3_i, aax, 300
				Case "J" : 
					DrawImage font3_j, aax, 300
				Case "K" : 
					DrawImage font3_k, aax, 300
				Case "L" : 
					DrawImage font3_l, aax, 300
				Case "M" : 
					DrawImage font3_m, aax, 300
				Case "N" : 
					DrawImage font3_n, aax, 300
				Case "O" : 
					DrawImage font3_o, aax, 300
				Case "P" : 
					DrawImage font3_p, aax, 300
				Case "Q" : 
					DrawImage font3_q, aax, 300
				Case "R" : 
					DrawImage font3_r, aax, 300
				Case "S" : 
					DrawImage font3_s, aax, 300
				Case "T" : 
					DrawImage font3_t, aax, 300
				Case "U" : 
					DrawImage font3_u, aax, 300
				Case "V" : 
					DrawImage font3_v, aax, 300
				Case "W" : 
					DrawImage font3_w, aax, 300
				Case "X" : 
					DrawImage font3_x, aax, 300
				Case "Y" : 
					DrawImage font3_y, aax, 300
				Case "Z" : 
					DrawImage font3_z, aax, 300
			End Select
						
			aax = aax + 32
		Next
		
		;Handle if a letter between a and z, del or space was pressed		
		If KeyHit(14) Then
			If letter_no > 1 Then				
				letter_no = letter_no - 1
				temp_array(letter_no) = "#"
			End If
		End If
		
		If letter_no <= 15 Then
			If KeyHit(30) Then temp_array(letter_no) = "A": letter_no = letter_no + 1
			If KeyHit(48) Then temp_array(letter_no) = "B": letter_no = letter_no + 1
			If KeyHit(46) Then temp_array(letter_no) = "C": letter_no = letter_no + 1
			If KeyHit(32) Then temp_array(letter_no) = "D": letter_no = letter_no + 1
			If KeyHit(18) Then temp_array(letter_no) = "E": letter_no = letter_no + 1
			If KeyHit(33) Then temp_array(letter_no) = "F": letter_no = letter_no + 1
			If KeyHit(34) Then temp_array(letter_no) = "G": letter_no = letter_no + 1
			If KeyHit(35) Then temp_array(letter_no) = "H": letter_no = letter_no + 1
			If KeyHit(23) Then temp_array(letter_no) = "I": letter_no = letter_no + 1
			If KeyHit(36) Then temp_array(letter_no) = "J": letter_no = letter_no + 1
			If KeyHit(37) Then temp_array(letter_no) = "K": letter_no = letter_no + 1
			If KeyHit(38) Then temp_array(letter_no) = "L": letter_no = letter_no + 1
			If KeyHit(50) Then temp_array(letter_no) = "M": letter_no = letter_no + 1
			If KeyHit(49) Then temp_array(letter_no) = "N": letter_no = letter_no + 1
			If KeyHit(24) Then temp_array(letter_no) = "O": letter_no = letter_no + 1
			If KeyHit(25) Then temp_array(letter_no) = "P": letter_no = letter_no + 1
			If KeyHit(16) Then temp_array(letter_no) = "Q": letter_no = letter_no + 1
			If KeyHit(19) Then temp_array(letter_no) = "R": letter_no = letter_no + 1
			If KeyHit(31) Then temp_array(letter_no) = "S": letter_no = letter_no + 1
			If KeyHit(20) Then temp_array(letter_no) = "T": letter_no = letter_no + 1
			If KeyHit(22) Then temp_array(letter_no) = "U": letter_no = letter_no + 1
			If KeyHit(47) Then temp_array(letter_no) = "V": letter_no = letter_no + 1
			If KeyHit(17) Then temp_array(letter_no) = "W": letter_no = letter_no + 1
			If KeyHit(45) Then temp_array(letter_no) = "X": letter_no = letter_no + 1
			If KeyHit(21) Then temp_array(letter_no) = "Y": letter_no = letter_no + 1
			If KeyHit(44) Then temp_array(letter_no) = "Z": letter_no = letter_no + 1
			If KeyHit(57) Then temp_array(letter_no) = " ": letter_no = letter_no + 1
		End If
							
		Flip
	Wend
	
	;Construct name string
	player_name$(pn-1) = ""
	For bb = 1 To 15
		If temp_array$(bb) <> "#"
			player_name$(pn-1) = player_name$(pn-1) + temp_array$(bb)
		End If
	Next	
	
	While KeyDown(28) > 0
	Wend
Next
	
Return

;**************** END OF ENTER NAME SOURCE CODE ****************





;************************************************************
;*															*
;* MAIN GAME LOOP											*
;*															*
;* The game itself								  	 		*
;*															*
;************************************************************

.MainGame

;Clear out keyboard buffer
While GetKey() <> 0 
Wend 

;Reset variables from any previous games
next_player = 0


music=PlayMusic("music4.mid")

;Reset player scores
For counter1 = 0 To 3
	overall_score%(counter1) = 0
	For counter2 = 0 To 13
		player_individual_score%(counter1, counter2) = 0
		score_used%(counter1, counter2) = 0
	Next
Next 

;The start of the players turn
For round_number = 1 To 14
	For player_turn = 1 To number_of_players
		;Roll the initial 6 dice
		SeedRnd MilliSecs()
		For counter = 0 To 5
			temp = Rnd(1,6)
			dice%(counter) = temp
			rack%(counter) = 0
		Next

		;Set the maximum number of rolls allowed per turn
		rolls_left = 2

		;Set up the other important initial variables
		no_in_dice = 6
		no_in_rack = 0
		quit_the_game = 0
		mouse_used = 0
		end_turn = 0

		;Repeat until either the turn is completed or the quit option is selected
		Repeat 
			Cls
			
			If Not ChannelPlaying(music)
				music=PlayMusic("music4.mid")
			End If
			
			Gosub DrawScreen			;Draw the screen layout
			Gosub DrawOptionButtons		;Display the four option buttons
			Gosub DrawDiceRack			;Display the main dice display area
			Gosub DrawStorageRack		;Display the dice storage rack area
			Gosub UpdateScores			;Display current game info
						
			;If end of turn detected
			If rolls_left = 0
				;Make sure all the dice are in the storage rack
				Select no_in_rack
					Case 0:
						temp_c = 0
					Default:
						temp_c = no_in_rack
				End Select

				For counter = 0 To no_in_dice - 1
					rack(temp_c) = dice(counter)
					dice(counter) = 0
					temp_c = temp_c + 1
				Next 

				no_in_dice = 0
				no_in_rack = 6
	
				Gosub DrawDice		;Update the dice on the screen
		
				;Grab the whole screen for display purpose in the select scoring category routine
				screen_grab = CreateImage(800,600)
				GrabImage screen_grab, 0, 0
		
				end_turn = 1
			Else
				Gosub DrawDice

				;Get the current mouse co-ordinates
				xm = MouseX()
				ym = MouseY()
		
				;Check if the user has clicked on one of the four main option buttons
				Gosub CheckOptionButtons
		
				;Add a delay so that multiple button pushes are eliminated
				If daz_timer > 0
					daz_timer = daz_timer - 1
				End If

				Gosub CheckUpFromRackButtons	;Check if the user has tried to move a dice up from the storage rack
				Gosub CheckDownToRackButtons	;Check if the user has tried to move a dice down to the storage rack

				;If the mouse button has been released, allow different buttons to be pressed
				If MouseDown(1)=0
					mouse_used = 0 
				End If
				
				;Draw the mouse pointer
				DrawImage cursor, xm, ym
			End If	
	
			Flip	
		Until end_turn > 0 Or quit_the_game > 0

		;If the quit game option button was selected, go back to the main menu screen
		If quit_the_game = 1 
			Cls
			Return
		End If

		Cls

		Gosub GetScoringCategory 
		Gosub CheckScoringCategory
		Gosub DisplayUpdatedScore

		Cls
	Next 
Next 

Gosub EndOfGame

Return


;-------------------------------------
;Draw the main screen background and border
.DrawScreen

;Draw the screen background
DrawImage mainback, 0, 0

;Draw the screen border layout
DrawImage border1, 4, 4
DrawImage border5, 4, 572
DrawImage border7, 772, 572
DrawImage border11, 4, 460
DrawImage border12, 772, 460
x_pos = 28
y_pos = 4
For counter = 1 To 31
	DrawImage border2, x_pos, y_pos
	DrawImage border6, x_pos, 572
	DrawImage border13, x_pos, 460
	x_pos = x_pos + 24
Next

DrawImage border3, x_pos, y_pos
x_pos = 4
y_pos = 28
For counter = 1 To 23
	DrawImage border4, x_pos, y_pos
	DrawImage border8, 772, y_pos
	y_pos = y_pos + 24
Next
x_pos = 484
y_pos=24
For counter = 1 To 18
	DrawImage border14, x_pos, y_pos
	y_pos = y_pos + 24
Next
DrawImage border14, x_pos, y_pos-7

DrawImage border9, x_pos, 4

Return


;------------------------------------------
;Draw the four option buttons on the screen

.DrawOptionButtons

;Draw the main buttons
x_pos = 45
y_pos = 362
DrawImage btn1_1, x_pos, y_pos+3
DrawImage btn2_1, x_pos + 102, y_pos + 3
DrawImage btn3_1, x_pos + 204, y_pos + 3
DrawImage btn4_1, x_pos + 306, y_pos + 3

;Draw border around option buttons
tempx = x_pos 
tempy = y_pos
For counter = 1 To 17
	DrawImage border2, tempx , y_pos - 12  
	DrawImage border6, tempx , y_pos + 28
	tempx = tempx + 24
Next
DrawImage border1, x_pos - 23, y_pos - 12
DrawImage border4, x_pos - 23, y_pos + 12
DrawImage border5, x_pos - 23, y_pos + 28
DrawImage border3, x_pos + 406, y_pos - 12
DrawImage border8, x_pos + 406, y_pos + 12
DrawImage border7, x_pos + 406, y_pos + 28

Return


;-------------------------------------
;Draw the dice rack area of the screen

.DrawDiceRack

;Draw Down To Rack Buttons
x_pos = 47
y_pos = 63
For counter = 1 To 6
	DrawImage downbut1, x_pos, y_pos + 55
	x_pos = x_pos + 74
Next

;Draw border around buttons
x_pos = 52
y_pos = 108
DrawImage border1, x_pos-30, y_pos-80
tempx = x_pos
For counter = 1 To 17
	DrawImage border2, tempx - 6, y_pos - 80
	tempx = tempx + 24
Next

tempx = tempx - 6
DrawImage border3, tempx, y_pos-80
DrawImage border8, tempx, y_pos-56
DrawImage border8, tempx, y_pos-32
DrawImage border8, tempx, y_pos-8
DrawImage border8, tempx, y_pos+16

DrawImage border7, tempx, y_pos+40
DrawImage border4, x_pos-30, y_pos-56
DrawImage border4, x_pos-30, y_pos-32
DrawImage border4, x_pos-30, y_pos-8
DrawImage border4, x_pos-30, y_pos+16
DrawImage border5, x_pos-30, y_pos+40
tempx = x_pos-6
For counter = 1 To 17
	DrawImage border6, tempx, y_pos+40
	tempx = tempx + 24
Next

Return


;---------------------------------
;Draw the main dice display region
.DrawStorageRack

;Draw Up to Dice Rack buttons
x_pos = 47
y_pos= 261
For counter = 1 To 6
	DrawImage upbut1, x_pos, y_pos - 55
	x_pos=x_pos+74
Next

;Draw border around storage rack
x_pos = 52
y_pos = 282 - 24 + 8
DrawImage border1, x_pos - 30, y_pos - 80
tempx = x_pos
For counter = 1 To 17
	DrawImage border2, tempx - 6, y_pos - 80
	tempx = tempx + 24
Next
tempx = tempx - 6
DrawImage border3, tempx, y_pos - 80
DrawImage border8, tempx, y_pos - 56
DrawImage border8, tempx, y_pos - 32
DrawImage border8, tempx, y_pos - 8
DrawImage border8, tempx, y_pos + 16
DrawImage border7, tempx, y_pos + 40
DrawImage border4, x_pos - 30, y_pos - 56
DrawImage border4, x_pos - 30, y_pos - 32
DrawImage border4, x_pos - 30, y_pos - 8
DrawImage border4, x_pos - 30, y_pos + 16
DrawImage border5, x_pos - 30, y_pos + 40
tempx = x_pos - 6
For counter = 1 To 17
	DrawImage border6, tempx, y_pos + 40
	tempx = tempx + 24
Next

x_pos = x_pos + 74
Return


;---------------------------------
;Display the current player scores

.UpdateScores

SetFont Font2
Color 255, 100, 255

;Display the info on the bottom section of the screen

;If player no is involved in game - display blue graphic otherwise display grey graphic
If number_of_players = 1
	DrawImage play1_1, 10, 510
	DrawImage play2_2, 210, 510
	DrawImage play3_2, 410, 510
	DrawImage play4_2, 610, 510		
	Text 10, 540, overall_score(0)
End If

If number_of_players = 2
	DrawImage play1_1, 10, 510
	DrawImage play2_1, 210, 510
	DrawImage play3_2, 410, 510
	DrawImage play4_2, 610, 510
	Text 10, 540, overall_score(0)
	Text 210, 540, overall_score(1)
End If

If number_of_players = 3
	DrawImage play1_1, 10, 510
	DrawImage play2_1, 210, 510
	DrawImage play3_1, 410, 510
	DrawImage play4_2, 610, 510
	Text 10, 540, overall_score(0)
	Text 210, 540, overall_score(1)
	Text 410, 540, overall_score(2)
End If

If number_of_players = 4
	DrawImage play1_1, 10, 510
	DrawImage play2_1, 210, 510
	DrawImage play3_1, 410, 510
	DrawImage play4_1, 610, 510
	Text 10, 540, overall_score(0)
	Text 210, 540, overall_score(1)
	Text 410, 540, overall_score(2)
	Text 610, 540, overall_score(3)
End If

;Display player's turn and rolls left this turn information
Color 255, 255, 0
SetFont Font3
Text 10, 425, "PLAYER'S TURN :"
Text 10, 445, "ROLLS LEFT THIS TURN :"
Text 305, 425, player_turn
Text 305, 445, rolls_left

;Display the players scores down the right hand side of the screen
a$ = "PLAYER SCORES"
Color 200, 200, 200
fl = StringWidth(a$)
Text 500 + ((300 - fl) /2), 10, a$

cy = 40
For counter = 0 To 13
	Text 510, cy, score_type$(counter)
	
	If score_used(player_turn, counter) > 0 Then
		Text 720, cy, player_individual_score%(player_turn, counter)
	End If
	
	cy = cy + 28
Next 

;Work out and display the overall score for the player
Text 510, cy, "TOTAL SCORE"
temp_score = 0
For counter = 0 To 13
	temp_score = temp_score + player_individual_score%(player_turn, counter)
Next
Text 720, cy, temp_score

Return

;-------------------------------
;Draw the six dice on the screen
.DrawDice

rackx = 64-32
dicex = 64-32

;For each of the six dice
For counter=0 To 5
	di = dice(counter)
	r = rack(counter)
	
	;If the current dice number in on the dice rack
	If di>0
		Select di
			Case 1
				DrawImage dice1, dicex, 80-32
			Case 2
				DrawImage dice2, dicex, 80-32
			Case 3
				DrawImage dice3, dicex, 80-32
			Case 4
				DrawImage dice4, dicex, 80-32
			Case 5
				DrawImage dice5, dicex, 80-32
			Case 6
				DrawImage dice6, dicex, 80-32
		End Select
		dicex=dicex+74
	End If
	
	;If the current dice number is held in the storage rack
	If r>0
		Select r
			Case 1
				DrawImage dice1, rackx, 280-32
			Case 2
				DrawImage dice2, rackx, 280-32
			Case 3
				DrawImage dice3, rackx, 280-32
			Case 4
				DrawImage dice4, rackx, 280-32
			Case 5
				DrawImage dice5, rackx, 280-32
			Case 6
				DrawImage dice6, rackx, 280-32			
		End Select
		rackx = rackx + 74
	End If
Next

Return


;-------------------------------------------------------
;Check for user selecting one of the four option buttons

.CheckOptionButtons

;If the y mouse co-ordinates are between 363 and 400
If ym > 362 And ym < 399	

	;Handle the Roll Dice button
	If xm > 37 And xm < 139
		;If the cursor is over the button, but the left mouse button isn't pressed down
		If mouse_used = 1 Or MouseDown(1) = 0
			DrawImage btn1_2, 45, 365
		Else
			;The mouse button is down
			If mouse_used = 0
				DrawImage btn1_3, 45, 365

				;Re-roll the dice left in the dice storage
				SeedRnd MilliSecs()
				For counter = 0 To no_in_dice-1
					dice(counter) = Rnd(1,6)
				Next

				rolls_left = rolls_left - 1
	
				PlaySound snd3
				mouse_used = 1
			End If
		End If
	EndIf

	;To rack button
	If xm > 138 And xm < 241
		;If the cursor is over the button, but the left mouse button isn't pressed down
		If mouse_used = 1 Or MouseDown(1) = 0
			DrawImage btn2_2, 147, 365
		Else
			;The left mouse button is down
			If mouse_used = 0
				DrawImage btn2_3, 147, 365

				;Move each dice from the dice storage area to the storage rack
				Select no_in_rack
					Case 0:
						temp_c = 0
					Default:
						temp_c = no_in_rack
				End Select

				For counter = 0 To no_in_dice - 1
					rack(temp_c) = dice(counter)
					dice(counter) = 0
					temp_c = temp_c + 1
				Next 

				no_in_dice = 0
				no_in_rack = 6
				
				PlaySound snd3
								
				mouse_used = 1
			End If
		End If
	EndIf

	;From rack button
	If xm > 240 And xm < 343
		;If the cursor is over the button, but the left mouse button isn't pressed down
		If mouse_used = 1 Or MouseDown(1) = 0
			DrawImage btn3_2, 249, 365
		Else
			;The left mouse button is down
			If mouse_used = 0
				DrawImage btn3_3, 249, 365
				;Move each dice in the storage rack up to the dice storage area
				Select no_in_dice
					Case 0:
						temp_c = 0
					Default
						temp_c = no_in_dice
				End Select

				For counter = 0 To no_in_rack - 1
					dice(temp_c) = rack(counter)
					rack(counter) = 0
					temp_c = temp_c + 1
				Next

				no_in_dice = 6
				no_in_rack = 0
				
				PlaySound snd3
				
				mouse_used = 1
			End If
		End If
	EndIf

	;Quit button
	If xm > 342 And xm < 446
		;If the left mouse button is not pressed
		If MouseDown(1) = 0 Then
			DrawImage btn4_2, 351, 365
		Else
			;Set the quit game flag to 1
			DrawImage btn4_3, 351, 365
			PlaySound snd3
			
			;Make sure user really wants to quit
			Color 255, 255, 255
			SetFont Font3
			Text 20,480, "Are you sure you want to quit? (Press Y or N)"
			
			ok_key = 0 
			While KeyDown(1) <> 0
			Wend 
			
			Flip
			
			Repeat
				If KeyDown(21) Then
					ok_key = 1
					quit_the_game = 1
				End If
				
				If KeyDown(49) Then
					ok_key = 1
					quit_the_game = 0
				End If
			Until ok_key <> 0
			mouse_used = 1
		End If
	EndIf

	If MouseDown(1) = 0
		mouse_used = 0
	End If
End If

Return


;----------------------------------------------------------
;Check if one of the Up From Rack buttons has been selected
.CheckUpFromRackButtons

If ym > 203 And ym < 241
	;If the left mouse button is pressed while cursor over one of the Up from storage rack buttons
	If mouse_used = 0 And MouseDown(1) = 1 And daz_time = 0
		button_selected = 0
		
		;Work out which of the up from rack buttons was selected
		If xm > 39 And xm < 72								
			button_selected = 1
			mouse_used = 1
			DrawImage upbut2, 47, 206
		End If

		If xm > 113 And xm < 147
			button_selected = 2				
			mouse_used = 1
			DrawImage upbut2, 121, 206
		End If

		If xm > 187 And xm < 221
			button_selected = 3
			mouse_used = 1
			DrawImage upbut2, 195, 206
		End If

		If xm > 261 And xm < 295
			button_selected = 4
			mouse_used = 1
			DrawImage upbut2, 269, 206
		End If
			
		If xm > 335 And xm < 369
			button_selected = 5
			mouse_used = 1
			DrawImage upbut2, 343, 206
		End If

		If xm > 409 And xm < 443
			button_selected = 6
			mouse_used = 1
			DrawImage upbut2, 417, 206
		End If			
		
		;If a up from storage rack button was pressed
		If button_selected > 0 
			button_selected = button_selected - 1
			
			;If there is a dice associated with the up from rack button selected
			If rack(button_selected) > 0
				;Move the dice from the storage rack to the dice storage area
				dice(no_in_dice) = rack(button_selected)
				
				;Shuffle up the remaining dice in the rack
				For counter = button_selected To no_in_rack - 1
					rack(counter) = rack(counter+1)
				Next 
				
				no_in_dice = no_in_dice + 1
				no_in_rack = no_in_rack - 1
				
				rack(no_in_rack) = 0
				
				PlaySound snd1
			
				daz_timer = 60
			End If
		End If
	End If
End If

Return



;----------------------------------------------------------
;Check if one of the Down To Rack buttons has been selected
.CheckDownToRackButtons
			
If ym > 113 And ym < 149
	;If the left mouse button is pressed while cursor over one of the Down to storage rack buttons
	If mouse_used = 0 And MouseDown(1) = 1 And daz_time = 0
		button_selected = 0
		
		;Work out which of the buttons was selected
		If xm > 39 And xm < 72								
			button_selected = 1
			mouse_used = 1
			DrawImage downbut2, 47, 118
		End If

		If xm > 113 And xm < 147
			button_selected = 2				
			mouse_used = 1
			DrawImage downbut2, 121, 118
		End If

		If xm > 187 And xm < 221
			button_selected = 3
			mouse_used = 1
			DrawImage downbut2, 195, 118
		End If

		If xm > 261 And xm < 295
			button_selected = 4
			mouse_used = 1
			DrawImage downbut2, 269, 118
		End If
			
		If xm > 335 And xm < 369
			button_selected = 5
			mouse_used = 1
			DrawImage downbut2, 343, 118
		End If

		If xm > 409 And xm < 443
			button_selected = 6
			mouse_used = 1
			DrawImage downbut2, 417, 118
		End If			
			
		If button_selected > 0 
			button_selected = button_selected - 1
			
			;If there is a dice associated with the down to rack button selected
			If dice(button_selected) > 0
				;Move the dice down to the storage rack
				rack(no_in_rack) = dice(button_selected)

				;Shuffle up the rest of the dice in the dice storage area
				For counter = button_selected To no_in_dice - 1
					dice(counter) = dice(counter+1)
				Next 

				no_in_dice = no_in_dice - 1
				no_in_rack = no_in_rack + 1
				
				dice(no_in_dice) = 0
				
				PlaySound snd1
				
				daz_timer = 60
			End If
		End If
	End If
End If

Return


;-------------------------------------------------------
;Get user to select which category they want to score to

.GetScoringCategory

score_selected = 0
colour_id = 0

;Repeat until a scoring category has been selected
Repeat 
	;Draw the image grabbed earlier on
	DrawImage screen_grab, 0, 0

	;Cycle through some colours for fancy font effect
	SetFont Font3	
	Select colour_id
		Case 0 :
			Color 255, 255, 0			;Set colour to yellow
		Case 1 :
			Color 0, 255, 0				;Set colour to green
		Case 2:
			Color 0, 255, 255			;Set colour to cyan
		Case 3:
			Color 255, 255, 255			;Set colour to white
	End Select
	colour_id = colour_id + 1
	If colour_id > 4 
		colour_id = 0
	End If
	
	;Display "select a category to score against" text message
	a$ = "PLAYER "
	Select player_turn
		Case 1 :
			a$ = a$ + "ONE "
		Case 2 :
			a$ = a$ + "TWO "
		Case 3 :
			a$ = a$ + "THREE "
		Case 4 :
			a$ = a$ + "FOUR "
	End Select
	a$ = a$ + " - PLEASE SELECT YOUR SCORING CATEGORY"
	Text 20, 480, a$

	;Update the current player score on the right hand side of the screen
	a$ = "PLAYER SCORES"
	Color 0,255,255
	fl = StringWidth(a$)
	Text 500+((300-fl)/2),10, a$

	;Initial display all the scoring categories and current scores in white text unless
	;the score has already been used, in which case, display it in grey
	cy = 40
	For counter = 0 To 13
		Color 255,255,255

		If score_used%(player_turn, counter) > 0
			Color 200, 200, 200
			Text 510, cy, score_type$(counter)
			Text 720, cy, player_individual_score%(player_turn, counter)
		Else
			Text 510, cy, score_type$(counter)
		End If
	
		cy = cy + 28
	Next 
	
	;Check if the cursor is over a scoring category
	scoring_category = 0

	;Check if the x mouse co-ordinate is in the correct area on the right hand side of the screen
	If xm > 499 And xm < 781
		Color 255, 255, 0
		
		;Check if cursor is in category region
		ly1 = 40
		ly2 = 56
		For counter = 0 To 13
			lookupy1(counter) = ly1
			lookupy2(counter) = ly2
			ly1 = ly1 + 28
			ly2 = ly2 + 28
		Next 
		
		For counter = 0 To 13
			If ym > lookupy1(counter) - 1 And ym < lookupy2(counter) - 1
				If score_used(player_turn, counter) = 0
					Text 510, lookupy1(counter), score_type$(counter)
					scoring_category = counter + 1
				Else
					scoring_category = 0
				End If
			End If
		Next		
	End If
	
	;If the left mouse button is pressed and the cursor is over a scoring category region
	If MouseDown(1) <> 0 And scoring_category > 0
		PlaySound snd4
		score_selected = 1
	End If

	;Calculate the total score already achieved by the player
	Color 0, 255, 50
	Text 510, cy, "TOTAL SCORE"
	temp_score = 0
	For counter = 0 To 13
		temp_score = temp_score + player_individual_score%(player_turn, counter)
	Next
	Text 720, cy, temp_score

	;Work out the mouse co-ordinates
	xm = MouseX()
	ym = MouseY()
	
	;Draw the mouse pointer
	DrawImage cursor, xm, ym
	
	Flip	
Until score_selected > 0

Return


;---------------------------------------------
;Work out score achieved for selected category

.CheckScoringCategory
ones = 0
twos = 0
threes = 0 
fours = 0
fives = 0 
sixes = 0

;Work out how many of each dice is in the storage rack
For counter = 0 To 5
	Select rack(counter)
		Case 1 :
			ones = ones + 1
		Case 2 :
			twos = twos + 1
		Case 3 :
			threes = threes + 1
		Case 4 :
			fours = fours + 1
		Case 5 :
			fives = fives + 1
		Case 6 :
			sixes = sixes + 1
	End Select
Next

score_achieved = 0


;Work out the actual score achieved 

;If scoring category 1 selected (ones)
If scoring_category = 1
	score_achieved = 1 * ones
End If

;If scoring category 2 selected (twos)
If scoring_category = 2
	score_achieved = 2 * twos
End If

;If scoring category 3 selected (threes)
If scoring_category = 3
	score_achieved = 3 * threes
End If

;If scoring category 4 selected (fours)
If scoring_category = 4
	score_achieved = 4 * fours
End If

;If scoring category 5 selected (fives)
If scoring_category = 5
	score_achieved = 5 * fives
End If

;If scoring category 6 selected (sixes)
If scoring_category = 6
	score_achieved = 6 * sixes
End If

;If scoring category 7 selected (3 of a kind)
If scoring_category = 7
	If ones > 2 Or twos > 2 Or threes > 2 Or fours > 2 Or fives > 2 Or sixes > 2 Then
		score_achieved = 20
	End If
End If

;If scoring category 8 selected (4 of a kind)
If scoring_category = 8
	If ones > 3 Or twos > 3 Or threes > 3 Or fours > 3 Or fives > 3 Or sixes > 3 Then
		score_achieved = 30
	End If
End If

;If scoring category 9 selected (5 of a kind)
If scoring_category = 9
	If ones > 4 Or twos > 4 Or threes > 4 Or fours > 4 Or fives > 4 Or sixes > 4 Then
		score_achieved = 50
	End If
End If

;If scoring category 10 selected (a full house - six of a kind)
If scoring_category = 10
	If ones > 5 Or twos > 5 Or threes > 5 Or fours > 5 Or fives > 5 Or sixes > 5 Then
		score_achieved = 100
	End If
End If

;If scoring category 11 selected (a short straight)
If scoring_category = 11
	If ones > 0 And twos > 0 And threes > 0 And fours > 0 
		score_achieved = 20
	End If
	
	If twos > 0 And threes > 0 And fours > 0 And fives > 0
		score_achieved = 20
	End If
	
	If threes > 0 And fours > 0 And fives > 0 And sixes > 0
		score_achieved = 20
	End If
End If

;If scoring category 12 selected (a medium straight)
If scoring_category = 12
	;Check if run is from 1 to 5
	If ones > 0 And twos > 0 And threes > 0 And fours > 0 And fives > 0 Then
		score_achieved = 50
	End If

	;Check if run is from 2 to 6	
	If twos > 0 And threes > 0 And fours > 0 And fives > 0 And sixes > 0 Then
		score_achieved = 50
	End If
End If

;If scoring category 13 selected (a full straight)
If scoring_category = 13
	If ones = 1 And twos = 1 And threes = 1 And fours = 1 And fives = 1 And sixes = 1 Then
		score_achieved = 100
	End If
End If

;If scoring category 14 selected (chance)
If scoring_category = 14
	score_achieved = 1*ones + 2*twos + 3*threes + 4*fours + 5*fives + 6*sixes
End If

;Put score into correct position in the play score array
player_individual_score(player_turn,  scoring_category - 1) = score_achieved
score_used(player_turn, scoring_category - 1) = 1

Return


;-----------------------------------------------------------
;Display the updated player score after selecting a category

.DisplayUpdatedScore

;Clear out the keyboard buffer
While KeyDown(1) = 1
Wend

colour_id = 0

;Repeat until the space bar is pressed
While KeyDown(57) = 0
	Cls
	DrawImage screen_grab, 0, 0
	
	;Do the fancy text colour effect
	Select colour_id
		Case 0 :
			Color 255, 255, 0
		Case 1 :
			Color 0, 255, 0
		Case 2:
			Color 0, 255, 255
		Case 3:
			Color 255, 255, 255
	End Select
	colour_id = colour_id + 1
	If colour_id > 4 
		colour_id = 0
	End If
	
	;Display the "PRESS SPACE TO CONTINUE" text message
	a$ = "PLAYER "
	Select player_turn
		Case 1 :
			a$ = a$ + "ONE "
		Case 2 :
			a$ = a$ + "TWO "
		Case 3 :
			a$ = a$ + "THREE "
		Case 4 :
			a$ = a$ + "FOUR "
	End Select
	a$ = a$ + " - END OF TURN. PRESS SPACE BAR TO CONTINUE"
	Text 20, 480, a$
	
	;Highlight the score just achieved for the chosen category
	Color 0, 255, 255	
	Select scoring_category
		Case 1 : ty = 40
		Case 2 : ty = 68
		Case 3 : ty = 96
		Case 4 : ty = 124
		Case 5 : ty = 152
		Case 6 : ty = 180
		Case 7 : ty = 208
		Case 8 : ty = 236
		Case 9 : ty = 264
		Case 10 : ty = 292
		Case 11 : ty = 320
		Case 12 : ty = 348
		Case 13 : ty = 376
		Case 14 : ty = 404
	End Select
	
	Text 510, ty, score_type$(scoring_category - 1) 
	Text 720, ty, player_individual_score(player_turn, scoring_category - 1)

	;Update the player's overall score on the screen
	Color 0, 255, 50
	Text 510, cy, "TOTAL SCORE"
	temp_score = 0
	For counter = 0 To 13
		temp_score = temp_score + player_individual_score%(player_turn, counter)
	Next
	Text 720, cy, "    "
	Text 720, cy, temp_score
	overall_score%(player_turn - 1) = temp_score

	Flip
Wend

Return


;Display end of game screen
.EndOfGame

music=PlayMusic("music2.mid")

;Clear out keyboard buffer
VWait 60

While KeyHit(57) > 0
Wend 

While KeyDown(57) > 0
Wend

colour_id = 0
Repeat
	Cls
	
	If Not ChannelPlaying(music)
		music=PlayMusic("music2.mid")
	End If
	
	TileImage backtile, 0, 0
	
	;Draw DICEY logo		
	DrawImage hdrD, 260, 60
	DrawImage hdrI, 330, 60
	DrawImage hdrC, 380, 60
	DrawImage hdrE, 440, 60
	DrawImage hdrY, 500, 60
	
	;Draw END OF GAME banner
	DrawImage font1_e, 156, 140
	DrawImage font1_n, 194, 140
	DrawImage font1_d, 242, 140
	DrawImage font1_o, 342, 140
	DrawImage font1_f, 387, 140
	DrawImage font1_g, 477, 140
	DrawImage font1_a, 522, 140
	DrawImage font1_m, 557, 140
	DrawImage font1_e, 602, 140
	
	;Draw FINAL SCORES banner
	DrawImage font2_f, 30, 220
	DrawImage font2_i, 46, 220
	DrawImage font2_n, 62, 220
	DrawImage font2_a, 78, 220
	DrawImage font2_l, 94, 220
	DrawImage font2_s, 126, 220
	DrawImage font2_c, 142, 220
	DrawImage font2_o, 158, 220
	DrawImage font2_r, 174, 220
	DrawImage font2_e, 190, 220
	DrawImage font2_s, 206, 220
	
	Color 255,255,0
	
	;Display scores
	If number_of_players = 1
		DrawImage play1_1, 10, 260
		DrawImage play2_2, 210, 260
		DrawImage play3_2, 410, 260
		DrawImage play4_2, 610, 260		
		Text 10, 290, overall_score(0)
	End If

	If number_of_players = 2
		DrawImage play1_1, 10, 260
		DrawImage play2_1, 210, 260
		DrawImage play3_2, 410, 260
		DrawImage play4_2, 610, 260
		Text 10, 290, overall_score(0)
		Text 210, 290, overall_score(1)
	End If

	If number_of_players = 3
		DrawImage play1_1, 10, 260
		DrawImage play2_1, 210, 260
		DrawImage play3_1, 410, 260
		DrawImage play4_2, 610, 260
		Text 10, 290, overall_score(0)
		Text 210, 290, overall_score(1)
		Text 410, 290, overall_score(2)
	End If

	If number_of_players = 4
		DrawImage play1_1, 10, 260
		DrawImage play2_1, 210, 260
		DrawImage play3_1, 410, 260
		DrawImage play4_1, 610, 260
		Text 10, 290, overall_score(0)
		Text 210, 290, overall_score(1)
		Text 410, 290, overall_score(2)
		Text 610, 290, overall_score(3)
	End If
		
	;Display PRESS LEFT MOUSE BUTTON TO CONTINUE text
	Select colour_id
		Case 0 :
			Color 255, 255, 0
		Case 1 :
			Color 0, 255, 0
		Case 2:
			Color 0, 255, 255
		Case 3:
			Color 255, 255, 255
	End Select
	colour_id = colour_id + 1
	If colour_id > 4 
		colour_id = 0
	End If
	a$ = "PRESS LEFT MOUSE BUTTON TO CONTINUE"
	al = StringWidth(a$)	
	SetFont Font3
	Text (800-al)/2, 550, a$	

	Flip
Until MouseDown(1) <> 0

;Detect if a high score has been achieved
overall_found = 0
For pln = 0 To number_of_players - 1
	found = 0
	
	For loop = 0 To 9
		If overall_score(pln) > hsscore%(loop) And found = 0 
			For update = 9 To loop Step - 1
				tempscore% = hsscore(update - 1)
				tempname$ = hsname$(update - 1)
				
				hsscore%(update) = tempscore%
				hsname$(update) = tempname$
			Next
			
			hsscore%(loop) = overall_score(pln)
			hsname$(loop) = player_name$(pln)
			
			found = 1
			overall_found = 1
		End If
	Next
Next

;Save new high score table to disk
If overall_found > 0 
	hsfile = WriteFile("hs.txt")
	For counter = 0 To 9
		WriteString hsfile, hsname$(counter)
		WriteInt hsfile, hsscore%(counter)
	Next
	CloseFile hsfile
End If

Return

;**************** END OF CUT MAIN GAME SOURCE CODE ****************




;************************************************************
;*															*
;* CUT GRAPHICS												*
;*															*
;* Splits up individual graphics from the various bitmaps	*
;*															*
;************************************************************

.CutGraphics

;Cut DICEY logo Graphics
DrawImage bitmap1, 0, 0

hdrD = CreateImage(47, 59)
hdrI = CreateImage(20, 59)
hdrC = CreateImage(44, 59)
hdrE = CreateImage(41, 59)
hdrY = CreateImage(48, 62)
GrabImage hdrD,0,0
GrabImage hdrI,50,0
GrabImage hdrC,100,0
GrabImage hdrE,150,0
GrabImage hdrY,200,0

;Cut main menu graphics
Cls
DrawImage bitmap2,0,0

backtile=CreateImage(128,128)
GrabImage backtile,0,0
arrow=CreateImage(32,32)
GrabImage arrow,129,0

x=170
font2_a = CreateImage(16,16): GrabImage font2_a, x, 0: x = x + 16
font2_b = CreateImage(16,16): GrabImage font2_b, x, 0: x = x + 16
font2_c = CreateImage(16,16): GrabImage font2_c, x, 0: x = x + 16
font2_d = CreateImage(16,16): GrabImage font2_d, x, 0: x = x + 16
font2_e = CreateImage(16,16): GrabImage font2_e, x, 0: x = x + 16
font2_f = CreateImage(16,16): GrabImage font2_f, x, 0: x = x + 16
font2_g = CreateImage(16,16): GrabImage font2_g, x, 0: x = x + 16
font2_h = CreateImage(16,16): GrabImage font2_h, x, 0: x = x + 16
font2_i = CreateImage(16,16): GrabImage font2_i, x, 0: x = x + 16
font2_j = CreateImage(16,16): GrabImage font2_j, x, 0: x = x + 16
font2_k = CreateImage(16,16): GrabImage font2_k, x, 0: x = x + 16
font2_l = CreateImage(16,16): GrabImage font2_l, x, 0: x = x + 16
font2_m = CreateImage(16,16): GrabImage font2_m, x, 0: x = x + 16
font2_n = CreateImage(16,16): GrabImage font2_n, x, 0: x = x + 16
font2_o = CreateImage(16,16): GrabImage font2_o, x, 0: x = x + 16
font2_p = CreateImage(16,16): GrabImage font2_p, x, 0: x = x + 16
font2_q = CreateImage(16,16): GrabImage font2_q, x, 0: x = x + 16
font2_r = CreateImage(16,16): GrabImage font2_r, x, 0: x = x + 16
font2_s = CreateImage(16,16): GrabImage font2_s, x, 0: x = x + 16
font2_t = CreateImage(16,16): GrabImage font2_t, x, 0: x = x + 16
font2_u = CreateImage(16,16): GrabImage font2_u, x, 0: x = x + 16
font2_v = CreateImage(16,16): GrabImage font2_v, x, 0: x = x + 16
font2_w = CreateImage(16,16): GrabImage font2_w, x, 0: x = x + 16
font2_x = CreateImage(16,16): GrabImage font2_x, x, 0: x = x + 16
font2_y = CreateImage(16,16): GrabImage font2_y, x, 0: x = x + 16
font2_z = CreateImage(16,16): GrabImage font2_z, x, 0: x = x + 16

x=0
font1_a = CreateImage(43,42): GrabImage font1_a, x, 130: x=x+50
font1_b = CreateImage(43,42): GrabImage font1_b, x, 130: x=x+50
font1_c = CreateImage(43,42): GrabImage font1_c, x, 130: x=x+50
font1_d = CreateImage(43,42): GrabImage font1_d, x, 130: x=x+50
font1_e = CreateImage(43,42): GrabImage font1_e, x, 130: x=x+50
font1_f = CreateImage(43,42): GrabImage font1_f, x, 130: x=x+50
font1_g = CreateImage(43,42): GrabImage font1_g, x, 130: x=x+50
font1_h = CreateImage(46,42): GrabImage font1_h, x, 130: x=x+50
font1_i = CreateImage(33,42): GrabImage font1_i, x, 130: x=x+50
font1_j = CreateImage(46,42): GrabImage font1_j, x, 130: x=x+50
font1_k = CreateImage(46,42): GrabImage font1_k, x, 130: x=x+50
font1_l = CreateImage(48,42): GrabImage font1_l, x, 130: x=x+50
font1_m = CreateImage(46,42): GrabImage font1_m, x, 130: x=x+50
font1_n = CreateImage(46,42): GrabImage font1_n, x, 130: x=x+50
font1_o = CreateImage(46,42): GrabImage font1_o, x, 130: x=x+50
font1_p = CreateImage(46,42): GrabImage font1_p, x, 130: x=x+50

x=0
font1_q = CreateImage(46,41): GrabImage font1_q, x, 180: x=x+50
font1_r = CreateImage(46,41): GrabImage font1_r, x, 180: x=x+50
font1_s = CreateImage(44,41): GrabImage font1_s, x, 180: x=x+50
font1_t = CreateImage(46,41): GrabImage font1_t, x, 180: x=x+50
font1_u = CreateImage(46,41): GrabImage font1_u, x, 180: x=x+50
font1_v = CreateImage(46,41): GrabImage font1_v, x, 180: x=x+50
font1_w = CreateImage(46,41): GrabImage font1_w, x, 180: x=x+50
font1_x = CreateImage(50,41): GrabImage font1_x, x, 180: x=x+50
font1_y = CreateImage(46,41): GrabImage font1_y, x, 180: x=x+50
font1_z = CreateImage(52,41): GrabImage font1_z, x, 180: x=x+50

Cls

;Cut high score table graphics
DrawImage bitmap3, 0, 0

x=0
font3_dot = CreateImage(32,32): GrabImage font3_dot,x,0: x=x+32
font3_a = CreateImage(32,32): GrabImage font3_a, x, 0: x=x+32
font3_b = CreateImage(32,32): GrabImage font3_b, x, 0: x=x+32
font3_c = CreateImage(32,32): GrabImage font3_c, x, 0: x=x+32
font3_d = CreateImage(32,32): GrabImage font3_d, x, 0: x=x+32
font3_e = CreateImage(32,32): GrabImage font3_e, x, 0: x=x+32
font3_f = CreateImage(32,32): GrabImage font3_f, x, 0: x=x+32
font3_g = CreateImage(32,32): GrabImage font3_g, x, 0: x=x+32
font3_h = CreateImage(32,32): GrabImage font3_h, x, 0: x=x+32
font3_i = CreateImage(32,32): GrabImage font3_i, x, 0: x=x+32
font3_j = CreateImage(32,32): GrabImage font3_j, x, 0: x=x+32
font3_k = CreateImage(32,32): GrabImage font3_k, x, 0: x=x+32
font3_l = CreateImage(32,32): GrabImage font3_l, x, 0: x=x+32
font3_m = CreateImage(32,32): GrabImage font3_m, x, 0: x=x+32
font3_n = CreateImage(32,32): GrabImage font3_n, x, 0: x=x+32
font3_o = CreateImage(32,32): GrabImage font3_o, x, 0: x=x+32
font3_p = CreateImage(32,32): GrabImage font3_p, x, 0: x=x+32
font3_q = CreateImage(32,32): GrabImage font3_q, x, 0: x=x+32
font3_r = CreateImage(32,32): GrabImage font3_r, x, 0: x=x+32
font3_s = CreateImage(32,32): GrabImage font3_s, x, 0: x=x+32
font3_t = CreateImage(32,32): GrabImage font3_t, x, 0: x=x+32
font3_u = CreateImage(32,32): GrabImage font3_u, x, 0: x=x+32
font3_v = CreateImage(32,32): GrabImage font3_v, x, 0: x=x+32
font3_w = CreateImage(32,32): GrabImage font3_w, x, 0: x=x+32
font3_x = CreateImage(32,32): GrabImage font3_x, x, 0: x=x+32

x=0
font3_y = CreateImage(32,32): GrabImage font3_y, x, 32: x=x+32
font3_z = CreateImage(32,32): GrabImage font3_z, x, 32: x=x+32

Cls

;Cut outro screen 1 graphics
DrawImage bitmap4, 0, 0

Line1 = CreateImage(726,51):GrabImage Line1,0,0
Line2 = CreateImage(182,51):GrabImage Line2,0,51
Line3 = CreateImage(364,51):GrabImage Line3,0,102
Line4 = CreateImage(561,51):GrabImage Line4,0,153
Line5 = CreateImage(562,51):GrabImage Line5,0,204
Line6 = CreateImage(372,51):GrabImage Line6,0,255
BlankLine = CreateImage(95,51):GrabImage BlankLine,0,306

Cls

;Cut in-game graphics
DrawImage bitmap5, 0, 0

x=0
border1 = CreateImage(24,24):  GrabImage border1, x, 0: x=x+24
border2 = CreateImage(24,24):  GrabImage border2, x, 0: x=x+24
border3 = CreateImage(24,24):  GrabImage border3, x, 0: x=x+24
border4 = CreateImage(24,24):  GrabImage border4, x, 0: x=x+24
border5 = CreateImage(24,24):  GrabImage border5, x, 0: x=x+24
border6 = CreateImage(24,24):  GrabImage border6, x, 0: x=x+24
border7 = CreateImage(24,24):  GrabImage border7, x, 0: x=x+24
border8 = CreateImage(24,24):  GrabImage border8, x, 0: x=x+24
border9 = CreateImage(24,24):  GrabImage border9, x, 0: x=x+24
border10 = CreateImage(24,24):  GrabImage border10, x, 0: x=x+24
border11 = CreateImage(24,24):  GrabImage border11, x, 0: x=x+24
border12 = CreateImage(24,24):  GrabImage border12, x, 0: x=x+24
border13 = CreateImage(24,24):  GrabImage border13, x, 0: x=x+24
border14 = CreateImage(24,24):  GrabImage border14, x, 0: x=x+24
border15 = CreateImage(24,24):  GrabImage border15, x, 0: x=x+24
border16 = CreateImage(24,24):  GrabImage border16, x, 0: x=x+24

x = 400
dice1 = CreateImage(64,64):  GrabImage dice1, x, 0: x=x+64
dice2 = CreateImage(64,64):  GrabImage dice2, x, 0: x=x+64
dice3 = CreateImage(64,64):  GrabImage dice3, x, 0: x=x+64
dice4 = CreateImage(64,64):  GrabImage dice4, x, 0: x=x+64
dice5 = CreateImage(64,64):  GrabImage dice5, x, 0: x=x+64
dice6 = CreateImage(64,64):  GrabImage dice6, x, 0: x=x+64

cursor = CreateImage(32,32):  GrabImage cursor, 0, 24

play1_1 = CreateImage(190,38): GrabImage play1_1, 0, 100
play1_2 = CreateImage(190,38): GrabImage play1_2, 200,100
play2_1 = CreateImage(190,38): GrabImage play2_1, 400, 100
play2_2 = CreateImage(190,38): GrabImage play2_2, 600,100
play3_1 = CreateImage(190,38): GrabImage play3_1, 0, 140
play3_2 = CreateImage(190,38): GrabImage play3_2, 200,140
play4_1 = CreateImage(190,38): GrabImage play4_1, 400, 140
play4_2 = CreateImage(190,38): GrabImage play4_2, 600,140

upbut1 = CreateImage(34,35): GrabImage upbut1, 0, 190
upbut2 = CreateImage(34,35): GrabImage upbut2, 35, 190
downbut1 = CreateImage(34,35): GrabImage downbut1, 0, 230
downbut2 = CreateImage(34,35): GrabImage downbut2, 35, 230

btn1_1 = CreateImage(102,35): GrabImage btn1_1, 0, 300
btn1_2 = CreateImage(102,35): GrabImage btn1_2, 200, 300
btn1_3 = CreateImage(102,35): GrabImage btn1_3, 400, 300
btn2_1 = CreateImage(102,35): GrabImage btn2_1, 600, 300
btn2_2 = CreateImage(102,35): GrabImage btn2_2, 0, 350
btn2_3 = CreateImage(102,35): GrabImage btn2_3, 200, 350
btn3_1 = CreateImage(102,35): GrabImage btn3_1, 400, 350
btn3_2 = CreateImage(102,35): GrabImage btn3_2, 600, 350
btn3_3 = CreateImage(102,35): GrabImage btn3_3, 0, 400
btn4_1 = CreateImage(102,35): GrabImage btn4_1, 200, 400
btn4_2 = CreateImage(102,35): GrabImage btn4_2, 400, 400
btn4_3 = CreateImage(102,35): GrabImage btn4_3, 600, 400

Flip
Cls

Return


;**************** END OF CUT GRAPHICS SOURCE CODE ****************



;**************** END OF SOURCE CODE ****************