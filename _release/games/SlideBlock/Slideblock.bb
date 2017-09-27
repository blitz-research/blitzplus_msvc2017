;*********************************
;*	    						 *
;*   SLIDING BLOCK PUZZLE GAME   *
;*	 							 *									
;*  WRITTEN BY  DARRYL BARTLETT  *
;*								 *
;*       USING  BLITZ BASIC      *
;*								 *
;*********************************

;Set up the game screen
Const width=640, height=480
Graphics width, height
SetBuffer BackBuffer()

;Load all the game graphics
Global cursor = LoadImage("cursor.bmp")
Global button = LoadImage("button.bmp")
Global empty_button = LoadImage("emptybtn.bmp")
Global background = LoadImage("backgrnd.bmp")
Global banner = LoadImage("logo.bmp")
Global btn1_1 = LoadImage("btn1_1.bmp")
Global btn1_2 = LoadImage("btn1_2.bmp")
Global btn1_3 = LoadImage("btn1_3.bmp")
Global btn2_1 = LoadImage("btn2_1.bmp")
Global btn2_2 = LoadImage("btn2_2.bmp")
Global btn2_3 = LoadImage("btn2_3.bmp")
Global screen_grab

;Define the global variables
Dim grid(6,6)
Dim temp_grid(36)
Global a$
Global xm, ym, zone, x_zone, y_zone
Global mouse_used, quit_game, moves
Global up_move, down_move, left_move, right_move

;Scrolling message variables
Type HorizontalScroll
	Field x
	Field c$
End Type
Global chars_on_screen
Global str_pos
Global tt$
Global scrolling_message$ = "WELCOME TO SLIDING BLOCK PUZZLE GAME WRITTEN BY DARRYL BARTLETT USING BLITZ BASIC.  THE AIM OF THE GAME IS TO PUT ALL THE BLOCKS IN ORDER (A..Z AND THEN 0..1)    "
Global pause_counter

;Set the in-game font
Global font = LoadFont ("times", 32, 1, 0, 0)
SetFont font

;Set up the scrolling message font
Global font2 = LoadFont("times", 16, 0, 0, 0)

;Set up moves made font
Global font3 = LoadFont("times", 24, 0, 0 ,0)

Gosub CreateGrid			;Create the intial random grid layout

str_pos = 1
chars_on_screen = 0
pause_counter = 5
quit_game = 0

;Repeat until the quit game option is select
While quit_game = 0
	Cls
	
	Gosub DrawScreen		;Draw the screen
		
	UpdateHorizontalScroll()	;Update the scrolling message on the screen

	;Work out if a new character in the scrolling message is to be 
	;added to the screen
	If pause_counter = 0 Then	
		If chars_on_screen < 41 
			tt$ = Mid$(scrolling_message$, str_pos, 1)
			CreateHorizontalScroll(tt$)
			str_pos = str_pos + 1
		
			;If end of scrolling message found, loop back to the beginning
			If str_pos > Len(scrolling_message)
				str_pos = 1
			End If
		End If
		
		pause_counter = 5
	End If
	
	If pause_counter > 0 Then
		pause_counter = pause_counter - 1
	End If
	
	;Get the current x and y mouse co-ordinates
	xm = MouseX()
	ym = MouseY()
		
	Gosub CheckButtons		;Handle user pressing on one of the two option buttons	
	Gosub CheckZones		;Handle the mouse being in a tile zone
	Gosub CheckWin			;Check if all the tiles are in the correct position
	
	;Draw the mouse cursor
	DrawImage cursor, xm, ym
	
	Flip
Wend

;End of game routine - grabs the whole screen and scrolls it down the screen
screen_grab = CreateImage(640,480)
GrabImage screen_grab, 0, 0

For counter = 0 To 480 Step 3
	Cls	
	DrawImage screen_grab, 0, counter
	Flip
Next


;------------------------------
;CREATE THE INITIAL GRID LAYOUT
.CreateGrid

moves = 0

;Create initial grid using a one-dimensional array
For counter = 0 To 35
	temp_grid(counter) = counter
Next

;Jumble up the grid
SeedRnd MilliSecs()
For counter = 1 To 500
	a = Rnd(0,35)
	b = Rnd(0,35)

	c = temp_grid(a)
	d = temp_grid(b)

	temp_grid(a) = d
	temp_grid(b) = c
Next

;Convert the temporary grid to a 6x6 two-dimensional array arrangement
grid_ref = 0
For counter1 = 0 To 5
	For counter2 = 0 To 5
		grid(counter2, counter1) = temp_grid(grid_ref)		
		grid_ref = grid_ref + 1
	Next
Next

Return


;---------------
;DRAW THE SCREEN
.DrawScreen

;Draw the game background tile
TileImage background

;Draw game logo
DrawImage banner, 450, 180
	
;Draw the two option buttons
DrawImage btn1_1, 470, 50
DrawImage btn2_1, 470, 100

;Display number of moves taken so far
SetFont font3
Text 450, 400, "MOVES MADE"
Text 450, 420, moves
SetFont font

;Draw the game tiles
x_pos = 50
y_pos = 50
For counter1 = 0 To 5
	For counter2 = 0 To 5
		If grid(counter2, counter1) = 35 Then
			;Draw the empty box tile
			DrawImage empty_button, x_pos, y_pos
		Else
			;Draw a filled-in tile
			DrawImage button, x_pos, y_pos
		End If
		
		;Draw the letter on the tile
		Select grid(counter2, counter1)
			Case 0 : a$ = "A"
			Case 1 : a$ = "B"
			Case 2 : a$ = "C"
			Case 3 : a$ = "D"
			Case 4 : a$ = "E"			
			Case 5 : a$ = "F"
			Case 6 : a$ = "G"
			Case 7 : a$ = "H"
			Case 8 : a$ = "I"
			Case 9 : a$ = "J"			
			Case 10 : a$ = "K"
			Case 11 : a$ = "L"
			Case 12 : a$ = "M"
			Case 13 : a$ = "N"
			Case 14 : a$ = "O"		
			Case 15 : a$ = "P"
			Case 16 : a$ = "Q"
			Case 17 : a$ = "R"
			Case 18 : a$ = "S"
			Case 19 : a$ = "T"		
			Case 20 : a$ = "U"
			Case 21 : a$ = "V"
			Case 22 : a$ = "W"
			Case 23 : a$ = "X"
			Case 24 : a$ = "Y"			
			Case 25 : a$ = "Z"
			Case 26 : a$ = "1"
			Case 27 : a$ = "2"
			Case 28 : a$ = "3"
			Case 29 : a$ = "4"
			Case 30 : a$ = "5"
			Case 31 : a$ = "6"
			Case 32 : a$ = "7"
			Case 33 : a$ = "8"
			Case 34 : a$ = "9"
			Case 35 : a$ = ""
		End Select

		;Make sure the letter is centred within the tile (a tile is 60x60 pixels)
		w = StringWidth(a$)
		h = FontHeight()	;StringHeight(a$)
		xx = x_pos + ((60 - w) / 2)
		yy = y_pos + ((60 - h) / 2)
		Text xx, yy, a$
		
		x_pos = x_pos + 65
	Next
	x_pos = 50
	y_pos = y_pos + 65
Next

Return



;--------------
;CHECK BUTTONS
;Check if the mouse is over and/or the mouse button is down over one of the two option buttons
.CheckButtons

;If the x mouse co-ordinate lies between 450 and 620
If xm > 469 And xm < 621 

	;If the cursor has a y co-ordinate between 50 and 100
	If ym > 49 And ym < 100
		;If the left mouse button is not down
		If mouse_used = 1 Or MouseDown(1) = 0
			DrawImage btn1_2, 470, 50
		Else
			;If the left mouse button is pressed down while over the option
			If mouse_used = 0
				DrawImage btn1_3, 470, 50
				mouse_used = 1
				
				;Make sure user really wants to start a new game
				Color 255, 255, 255
				Text 0, 0, "Are you sure you want to start a new game? (Y or N)"			
				ok_key = 0 			
				Flip
			
				;Repeat until either Y or N is pressed
				Repeat
					;If Y pressed
					If KeyDown(21) Then
						ok_key = 1
						Gosub CreateGrid
					End If
				
					;If N pressed
					If KeyDown(49) Then
						ok_key = 1						
					End If
				Until ok_key <> 0				
			End If
		End If
	End If
	
	;If the cursor has a y co-ordinate between 100 and 150
	If ym > 99 And ym < 150
		;If the left mouse button is not down
		If mouse_used = 1 Or MouseDown(1) = 0
			DrawImage btn2_2, 470, 100
		Else
			;If the left mouse button is pressed down while over the option
			If mouse_used = 0 
				DrawImage btn2_3, 470, 100
				
				mouse_used = 1

				;Make sure user really wants to quit
				Color 255, 255, 255
				Text 0, 0, "Are you sure you want to quit? (Y or N)"
			
				ok_key = 0 
			
				Flip
			
				;Repeat until either Y or N is pressed
				Repeat
					;If Y pressed
					If KeyDown(21) Then
						ok_key = 1
						quit_game = 1
					End If

					;If N pressed
					If KeyDown(49) Then
						ok_key = 1
						quit_game = 0
					End If
				Until ok_key <> 0
			End If
		End If
	End If
	
	If MouseDown(1) = 0
		mouse_used = 0 
	End If
End If

Return


;-----------
;CHECK ZONES
;Check if the mouse pointer is currently over one of the 36 tiles
.CheckZones

found_zone = 0
zone = 0

;Loop through each zone to see if the mouse cursor is currently within it
temp_x = 50
temp_y = 50
For counter1 = 0 To 5
	For counter2 = 0 To 5
		found_zone = found_zone + 1
		
		If xm >= temp_x And xm <= temp_x + 65
			If ym >= temp_y And ym <= temp_y + 65
				zone = found_zone
				x_zone = counter2
				y_zone = counter1
			End If
		End If
		
		temp_x = temp_x + 65
	Next
	temp_x = 65
	temp_y = temp_y + 65
Next

;Handle the mouse clicking the left mouse button when cursor is over a tile
If MouseDown(1) = 1 Then
	;Work out the movement available for each individual tile
	;0 = cannot move in that direction    1 = can move in that direction
	If zone > 0 Then
		Select zone
			Case 1 : up_move = 0 : down_move = 1 : left_move = 0 : right_move = 1
			Case 2 : up_move = 0 : down_move = 1 : left_move = 1 : right_move = 1
			Case 3 : up_move = 0 : down_move = 1 : left_move = 1 : right_move = 1
			Case 4 : up_move = 0 : down_move = 1 : left_move = 1 : right_move = 1
			Case 5 : up_move = 0 : down_move = 1 : left_move = 1 : right_move = 1
			Case 6 : up_move = 0 : down_move = 1 : left_move = 1 : right_move = 0		
			Case 7 : up_move = 1 : down_move = 1 : left_move = 0 : right_move = 1
			Case 8 : up_move = 1 : down_move = 1 : left_move = 1 : right_move = 1
			Case 9 : up_move = 1 : down_move = 1 : left_move = 1 : right_move = 1
		 	Case 10 : up_move = 1 : down_move = 1 : left_move = 1 : right_move = 1
			Case 11 : up_move = 1 : down_move = 1 : left_move = 1 : right_move = 1
			Case 12 : up_move = 1 : down_move = 1 : left_move = 1 : right_move = 0		
			Case 13 : up_move = 1 : down_move = 1 : left_move = 0 : right_move = 1
			Case 14 : up_move = 1 : down_move = 1 : left_move = 1 : right_move = 1
			Case 15 : up_move = 1 : down_move = 1 : left_move = 1 : right_move = 1
			Case 16 : up_move = 1 : down_move = 1 : left_move = 1 : right_move = 1
			Case 17 : up_move = 1 : down_move = 1 : left_move = 1 : right_move = 1
			Case 18 : up_move = 1 : down_move = 1 : left_move = 1 : right_move = 0
			Case 19 : up_move = 1 : down_move = 1 : left_move = 0 : right_move = 1
		 	Case 20 : up_move = 1 : down_move = 1 : left_move = 1 : right_move = 1
			Case 21 : up_move = 1 : down_move = 1 : left_move = 1 : right_move = 1
			Case 22 : up_move = 1 : down_move = 1 : left_move = 1 : right_move = 1
			Case 23 : up_move = 1 : down_move = 1 : left_move = 1 : right_move = 1
			Case 24 : up_move = 1 : down_move = 1 : left_move = 1 : right_move = 0		
			Case 25 : up_move = 1 : down_move = 1 : left_move = 0 : right_move = 1
			Case 26 : up_move = 1 : down_move = 1 : left_move = 1 : right_move = 1
			Case 27 : up_move = 1 : down_move = 1 : left_move = 1 : right_move = 1
			Case 28 : up_move = 1 : down_move = 1 : left_move = 1 : right_move = 1
			Case 29 : up_move = 1 : down_move = 1 : left_move = 1 : right_move = 1
			Case 30 : up_move = 1 : down_move = 1 : left_move = 1 : right_move = 0
			Case 31 : up_move = 1 : down_move = 0 : left_move = 0 : right_move = 1
			Case 32 : up_move = 1 : down_move = 0 : left_move = 1 : right_move = 1
			Case 33 : up_move = 1 : down_move = 0 : left_move = 1 : right_move = 1
			Case 34 : up_move = 1 : down_move = 0 : left_move = 1 : right_move = 1
			Case 35 : up_move = 1 : down_move = 0 : left_move = 1 : right_move = 1
			Case 36 : up_move = 1 : down_move = 0 : left_move = 1 : right_move = 0
		End Select
	
		;Work out if desired move is possible
		If up_move = 1
			If grid(x_zone, y_zone - 1) = 35
				temp_a = grid(x_zone, y_zone)
				grid(x_zone, y_zone) = 35
				grid(x_zone, y_zone - 1) = temp_a
				moves = moves + 1
			End If
		End If
	
		If down_move = 1
			If grid(x_zone, y_zone + 1) = 35
				temp_a = grid(x_zone, y_zone)
				grid(x_zone, y_zone) = 35
				grid(x_zone, y_zone + 1) = temp_a
				moves = moves + 1
			End If
		End If
	
		If left_move = 1
			If grid(x_zone - 1, y_zone) = 35
				temp_a = grid(x_zone, y_zone)
				grid(x_zone, y_zone) = 35
				grid(x_zone - 1, y_zone) = temp_a
				moves = moves + 1
			End If
		End If
	
		If right_move = 1
			If grid(x_zone + 1, y_zone) = 35
				temp_a = grid(x_zone, y_zone)
				grid(x_zone, y_zone) = 35
				grid(x_zone + 1, y_zone) = temp_a
				moves = moves + 1
			End If
		End If	
	End If
End If

Return



;------------------------------------------------------
;CHECKWIN - Check if all tiles are in the correct order
.CheckWin
	in_order = 1
	counter = 0
	
	For counter1 = 0 To 5
		For counter2 = 0 To 5
			If grid(counter2, counter1) <> counter Then
				in_order = 0
			End If
			counter = counter + 1
		Next
	Next
	
	;Congratulations routine
	If in_order = 1 Then
		ok_key = 0
		
		;Repeat until either Y or N is pressed
		Repeat
			Cls
			TileImage background
			Text 320, 10,"GAME COMPLETED", 1, 0
			Text 320, 70, "YOU COMPLETED THE GAME USING", 1, 0
			Text 320, 100, moves, 1, 0
			Text 320, 130, "MOVES", 1, 0			
			Text 320, 200, "DO YOU WANT TO PLAY AGAIN?", 1, 0
			Text 320, 230, "(Y or N)", 1, 0
			
			;If Y pressed
			If KeyDown(21) Then
				ok_key = 1
				quit_game = 0
				Gosub CreateGrid
			End If

			;If N pressed
			If KeyDown(49) Then
				ok_key = 1
				quit_game = 1
			End If
			
			Flip			
		Until ok_key <> 0
	End If
Return




;-----------------------
;Text scroller functions

;Adds a new character to the scrolling message
Function CreateHorizontalScroll(tt$)
	hs.HorizontalScroll = New HorizontalScroll
	hs\x = 640
	hs\c = tt$
End Function

;Updates the scrolling message letters on the screen
Function UpdateHorizontalScroll()
	SetFont font2
	
	;Loop through each of the current scrolling characters
	For hs.HorizontalScroll = Each HorizontalScroll
		If pause_counter = 0 Then
			hs\x = hs\x - 16
		End If
		
		Text hs\x, 460, hs\c
		
		;If the character has gone off the left hand side of the screen
		If hs\x < 0
			Delete hs
			chars_on_screen = chars_on_screen - 1
		End If
	Next
	
	SetFont font
End Function 