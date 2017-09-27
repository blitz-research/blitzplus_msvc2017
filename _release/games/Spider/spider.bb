;
; Spider v1.0
; 
; A little game written in BlitzPC
;
; Last modification 16-8-2000 by Jurgen Valks (jvalks@home.nl)
;

Graphics 640,480
SetBuffer BackBuffer()



Type SpiderData
	Field xpos, ypos, animframe, animcount, animpause, speed, hit
End Type

Const  Enemy_Ypos_Start = 460
Const  Enemy_Anim_Pause = 4
Global spiders          = LoadAnimImage("gfx/spider_shapes.bmp",16,16,0,9)
Global shotgun          = LoadSound("sfx/shotgun.wav")
Global spiderhit        = LoadSound("sfx/toyduck.wav")
Global logo             = LoadImage("gfx/spiderlogo_small.bmp")
Global theend           = LoadImage("gfx/game_over.bmp")
Global score            = 0
Global Enemy.SpiderData
Global GameOver         = False

SoundPan spiderhit,-1
SoundPan shotgun,1

IntroScreen()
InitEnemy()

Repeat
	If GameOver = True Then
		GameoverScreen()
		IntroScreen()
		InitEnemy()
		GameOver = False
		score = 0
	Else	
		DrawScreen()
		DrawEnemies()
		DrawImage spiders,MouseX(),MouseY(),8
		If MouseHit(1) Then CheckCollision()
		Flip
	EndIf
Until KeyDown(1)
TerminateProgram()

Function InitEnemy()
	Enemy                   = New SpiderData
	Enemy\xpos              = Rnd(32,620)
	Enemy\ypos              = Enemy_Ypos_Start
	Enemy\speed             = 1
	Enemy\animframe         = 0
	Enemy\animcount         = 1
	Enemy\animpause         = 4
	Enemy\hit               = False
End Function

Function IntroScreen()
	logo_y         = 100
	logo_direction = 0
	counter        = 1
	SetBuffer BackBuffer()
	Cls
	Text 300,260,"A simple game written in Blitz PC by Jurgen Valks",1,1
	Text 300,290,"Press spacebar to continue",1,1
	SetBuffer FrontBuffer()
	Cls
	Text 300,260,"A simple game written in Blitz PC by Jurgen Valks",1,1
	Text 300,290,"Press spacebar to continue",1,1
	Viewport 0,0,640,250
	Repeat	
		Cls
		logo_x = 60 * Cos(counter)
		logo_y = 60 * Sin(counter)
		counter = counter + 2
		If counter > 360 Then counter=1
		DrawImage logo,180+logo_x,80+logo_y
		Flip
	Until KeyDown(57)
	ClearScreens()
End Function

Function GameoverScreen()
	ClearScreens()
	SetBuffer FrontBuffer()
	DrawImage theend,50,80
	Color 255,255,255
	Text 200,200,"You reached a score of "+score
	Text 300,310,"Press spacebar to continue",1,1
	Repeat
	Until KeyDown(57)
	ClearScreens()
End Function

Function DrawScreen()
	Viewport 0,0,640,480
	Cls
	Color 255,255,255
	Text 0,0,"Score "+score
	Color 0,255,0
	Line 0,399,640,399
	Line 0,15,640,15
	Viewport 0,15,640,385
End Function

Function TerminateProgram()
	FreeSound shotgun
	FreeSound spiderhit
	FreeImage spiders
	FreeImage logo
	FreeImage theend
	End
End Function

Function DrawEnemies()
	For Enemy = Each SpiderData
		DrawImage spiders,Enemy\xpos,Enemy\ypos,Enemy\animframe
		Enemy\animcount = Enemy\animcount + 1	
		If Enemy\animcount > Enemy_Anim_Pause Then
			Enemy\animcount = 1
			If Enemy\hit = False Then
				Enemy\animframe = 1 - Enemy\animframe
			Else
				Enemy\animframe = Enemy\animframe + 1
				If Enemy\animframe >7 Then
					Delete Enemy
				EndIf
			EndIf
		EndIf
		If Enemy <> Null Then
			Enemy\ypos = Enemy\ypos - Enemy\speed
			If Enemy\ypos <15 Then
				GameOver = True
				Delete Each SpiderData
			EndIf
		EndIf
	Next
End Function

Function CheckCollision()
	PlaySound shotgun
	;Stop
	For Enemy = Each SpiderData
		If ImagesCollide(spiders,MouseX(),MouseY(),8,spiders,Enemy\xpos,Enemy\ypos,Enemy\animframe) Then
			If Enemy\hit = False Then
				PlaySound spiderhit
				score = score + 1
				Enemy\hit       = True
				Enemy\animcount = 2
				Enemy\speed     = 0
				InitEnemy()
				If score Mod 10 = 0 Then InitEnemy()
			EndIf
		EndIf 
	Next
End Function

Function ClearScreens()
	Viewport 0,0,640,480
	SetBuffer FrontBuffer()
	Cls
	SetBuffer BackBuffer()
	Cls
End Function