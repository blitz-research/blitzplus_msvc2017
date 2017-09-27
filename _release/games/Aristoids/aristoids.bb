; aristoids
; by aristides mytaras
; aristid@freemail.gr
; Fixed to run under 1.48

; TURN DEBUG OFF or you will get a lousy frame rate

AppTitle "aristoids"	
Global width=1024,height=768,depth=16		
Graphics width,height;,depth		
SetBuffer BackBuffer()	
SeedRnd MilliSecs()

Global chnEngine, chnbuzz

Global smallfont,bigfont,hugefont,music
Global fire_sound=LoadSound( "machineg.wav" )
Global buzz_sound=LoadSound( "blast4_.wav" )
Global smexp_sound=LoadSound( "blast4_.wav" )
Global lowsmexp_sound=LoadSound( "blast4_.wav" )
Global exp_sound=LoadSound( "explode4.wav" ):SoundVolume exp_sound,0.95
Global engine_sound=LoadSound( "engn002_.wav" )
Global click_sound=LoadSound("clicktwo.wav")
Global powerup_sound=LoadSound( "flaskgone.wav" ):SoundVolume powerup_sound,0.85
SoundPitch buzz_sound,4000:LoopSound buzz_sound:chnbuzz=PlaySound(buzz_sound):PauseChannel chnbuzz
LoopSound engine_sound:chnEngine=PlaySound(engine_Sound):PauseChannel chnEngine:SoundVolume engine_sound,0.93
SoundPitch lowsmexp_sound,10000
Global level,bulletdistance,bulletnumber,bulletsfire,bulletshit,bulletspeed,enemybulletspeed,hiscore
Global poweruptimer,pupx,pupy,pupcount,puptype,pupprobability,puptext$,gameovercounter,clearlevelcounter
Global targetscorefornextlife,alienswelcome,alienswelcomecounter
Global x#,y#,xvel#,yvel#,an#,bulletsfired,audioseperation,controltype
Global joystick=JoyType()
Global yourname$ ;bug with string-return?
audioseperation=0.85:controltype=0 ;asteroids movement


smallfont=LoadFont( "Arial",18,True,False,False )
bigfont=LoadFont( "Arial",26,True,False,False )
hugefont=LoadFont( "Arial",65,True,False,False )
SetFont smallfont


Dim hiscores(11)
Dim hinames$(11)
Dim hidates$(11)
Dim levelreached(11)
Dim control(11)

Type playership							
	Field x#,y#,xvel#,yvel#					
	Field an#,anvel#,antarg#
	Field Lastx#,Lasty#							
	Field speed#
	Field bulletsfired,shield,autoshield,shieldflag
	Field score,life,lives
End Type

Type enemy
	Field x#,y#,xvel#,yvel#	
	Field targx,targy,targcount
	Field shooter
	Field life
	Field agression,aim
	Field g
End Type

Type asteroid
	Field x#,y#,xvel#,yvel#	
	Field an,speed#
	Field life
	Field size
	Field r,g,b
End Type

Type bullet
	Field x#,y#,xvel#,yvel#
	Field life
	Field fromwho
End Type

Type Frag
	;particles
	Field x#,y#,xs#,ys#
	Field r,g,b
End Type

Global p1.playership=New playership		; Create player's ship

titlescreen()

;-------------------------------------------------------------------------
; Main loop:
While Not KeyDown(1)						; Do until ESC is pressed
	Cls										; Clear the screen.
	
	debug=0
	If debug<>0 And KeyDown(2)
	For p.asteroid=Each asteroid
	Delete p
	Next
	
	For g.enemy=Each enemy
	Delete g
	Next
	
	For h.bullet=Each bullet
	Delete h
	Next
	EndIf
	
	If KeyDown(25)=1
		; pause function
		Repeat
		Until KeyDown(25)=0
		Repeat:Until KeyDown(25)=1 Or KeyDown(29)=1 Or KeyDown(57)=1
		Repeat:Until KeyDown(25)=0 Or KeyDown(29)=0 Or KeyDown(57)=0
	EndIf
	
	If gameovercounter<=0
		; main player input
		If controltype=0
		;=======   control type 1: asteroids
		;-------------------------------------
		If KeyDown(200) Or KeyDown(56) Or KeyDown(44) Or (joystick<>0 And JoyYDir())
			p1\speed=p1\speed+0.002
			enginepitch=enginepitch+1000
		Else
			enginepitch=enginepitch-600
		EndIf
		enginepitch=enginepitch*0.95
		If enginepitch>70 Then
			;revs
			ResumeChannel chnEngine
			SoundPitch engine_sound,enginepitch
		Else
			PauseChannel chnEngine
		EndIf
		
		If KeyDown(203) Or KeyDown(75) Or KeyDown(51) Or (joystick<>0 And JoyXDir()=-1)Then p1\an=p1\an-3	
		If KeyDown(205) Or KeyDown(77) Or KeyDown(52) Or(joystick<>0 And JoyXDir()=1)Then p1\an=p1\an+3
		If bulfired=0 And (KeyDown(57) Or KeyDown(29) Or (joystick<>0 And JoyDown(1))) And clearlevelcounter<=0 Then firebullet(250,bulletspeed):bulfired=bulletdistance
		If bulfired>0 Then bulfired=bulfired-1
		If KeyDown(208) Or KeyDown(42) Or (joystick<>0 And JoyDown(2)) Then
			If p1\shield>1 Then p1\shieldflag=1:p1\shield=p1\shield-1
		Else
			p1\shieldflag=0
		EndIf
		EndIf
		If controltype>=1
		;=======   control type 2: beginner
		;-----------------------------------
		revup=0
		If controltype=1 Then revver=1
		If controltype=2 Then revver=3
		If KeyDown(200) Or KeyDown(72)  Or (joystick<>0 And JoyYDir())
			;up
			revup=revver
			p1\antarg=270
		EndIf
		If KeyDown(203) Or KeyDown(75) Or (joystick<>0 And JoyXDir()=-1)
			;left
			revup=revver
			p1\antarg=180
		EndIf
		If KeyDown(205) Or KeyDown(77) Or(joystick<>0 And JoyXDir())
			;Right
			revup=revver
			p1\antarg=0
		EndIf
		If KeyDown(208) Or KeyDown(80) Or (joystick<>0 And JoyYDir()=-1)
			;down
			revup=revver
			p1\antarg=90
		EndIf
	
		;diagonals
		If (KeyDown(200) Or KeyDown(72) Or (joystick<>0 And JoyYDir())) And (KeyDown(205) Or KeyDown(77) Or(joystick<>0 And JoyXDir()))
			;up+right
			revup=revver
			p1\antarg=315
		EndIf
		If (KeyDown(200) Or KeyDown(72) Or (joystick<>0 And JoyYDir())) And (KeyDown(203) Or KeyDown(75) Or (joystick<>0 And JoyXDir()=-1))
			;up+left
			revup=revver
			p1\antarg=225
		EndIf
		If (KeyDown(208) Or KeyDown(80) Or (joystick<>0 And JoyYDir()=-1)) And (KeyDown(205) Or KeyDown(77) Or(joystick<>0 And JoyXDir()))
			;down+Right
			revup=revver
			p1\antarg=45
		EndIf
		If (KeyDown(208) Or KeyDown(80) Or (joystick<>0 And JoyYDir()=-1)) And (KeyDown(203) Or KeyDown(75) Or (joystick<>0 And JoyXDir()=-1))
			;down+left
			revup=revver
			p1\antarg=135
		EndIf	
		
		;thrust
		If KeyDown(56) Then revup=2:If revver=1 Then p1\speed=p1\speed+0.00065
		
		Lastan=p1\an
		If p1\an<>p1\antarg And (revup=1 Or revup=3)
		dif1=p1\antarg-p1\an
		dif2=p1\an-p1\antarg
		If p1\antarg=0 And p1\an>180 Then p1\an=p1\an+6
		If p1\antarg=270 And p1\an<90 Then p1\an=p1\an-6
		If p1\antarg=90 And (p1\an>270) Then p1\an=p1\an+6
		If p1\antarg=45 And (p1\an>225) Then p1\an=p1\an+6
		If p1\antarg=135 And (p1\an>315) Then p1\an=p1\an+6
		If p1\antarg=315 And (p1\an<135) Then p1\an=p1\an-6
		If dif1>dif2 Then 
			p1\an=p1\an+3
		Else
			p1\an=p1\an-3
		EndIf
		EndIf
			
		If revup=>1
			If revup<>3 Then
				p1\speed=p1\speed+0.002
				If revver=1 Then p1\speed=p1\speed-0.00135
				enginepitch=enginepitch+1000:revup=0
			EndIf
		Else
			enginepitch=enginepitch-600
			p1\an=Lastan
		EndIf					
			
		enginepitch=enginepitch*0.95
		If enginepitch>70 Then
			;revs
			ResumeChannel chnEngine
			SoundPitch engine_sound,enginepitch
		Else
			PauseChannel chnEngine
		EndIf
		
		
		;If KeyDown(203) Or KeyDown(75) Or (joystick<>0 And JoyXDir()=-1)Then p1\an=p1\an-3	
		;If KeyDown(205) Or KeyDown(77) Or(joystick<>0 And JoyXDir()=1)Then p1\an=p1\an+3
	
		
		If bulfired=0 And (KeyDown(57) Or KeyDown(29) Or (joystick<>0 And JoyDown(1))) And clearlevelcounter<=0 Then firebullet(250,bulletspeed):bulfired=bulletdistance
		If bulfired>0 Then bulfired=bulfired-1
		If KeyDown(42) Or (joystick<>0 And JoyDown(2)) Then
			If p1\shield>1 Then p1\shieldflag=1:p1\shield=p1\shield-1
		Else
			p1\shieldflag=0
		EndIf
		EndIf
	Else
		gameovercounter=gameovercounter-1
		PauseChannel chnEngine
		
		If gameovercounter=0 Then
			If checkhighscore("aristoidsprefs",p1\score)<11
				;highscore achieved
				PlaySound powerup_sound
				;WaitKey
				entername("",p1\score)
				sorthighscores("aristoidsprefs",yourname$,p1\score,level)
				savehighscores("aristoidsprefs")
			EndIf
			titlescreen()
		EndIf
	EndIf
	If p1\autoshield>0 Then p1\autoshield=p1\autoshield-1:p1\shieldflag=2
	
	UpdateShip()
	updatebullets()
	UpdateExplosion()
	enemyships=UpdateEnemy()
	asters=UpdateAsteroid(0)
	
	If poweruptimer<=0 And pupcount<=0 And Rnd(0,pupprobability)<=1 And p1\score>500
		;generate powerup
		pupx=Rnd(width-40)+20
		pupy=Rnd(height-40)+20
		poweruptimer=800
		pupdecide=Rnd(1,100)
		If pupdecide>85 Then puptype=1:puptext="maniac's machinegun"
		If pupdecide<30 Then puptype=2:puptext="sniper"
		If pupdecide>=30 And pupdecide<=60 Then puptype=3:puptext="machinegun"	
		If pupdecide>60 And pupdecide<=70 Then puptype=4:puptext="1000 points"
		If pupdecide>70 And pupdecide<=78 Then puptype=5:puptext="full shield"
		If pupdecide>78 And pupdecide<=85 Then puptype=6:puptext="full energy"	
	EndIf
	
	If pupcount>0 Then
		;pupcount=pupcount-1
	Else
		bulletdistance=20 ;distance between the bullets
		bulletnumber=10	;number of bullets allowed on screen
		bulletspeed=3	
	EndIf
	
	If poweruptimer>0
		Color 50,(poweruptimer)*0.3,Rnd(0,15)
		Oval pupx-10,pupy-10,15,15,1
		Text pupx,pupy,puptext
		poweruptimer=poweruptimer-1
		If p1\x+5>pupx-(15/2) And p1\x-5<pupx+(15/2) And p1\y+5>pupy-(15/2) And p1\y-5<pupy+(15/2)
			poweruptimer=0
			PlaySound powerup_sound
			If puptype=1 Then bulletdistance=4:bulletnumber=50:bulletspeed=4:pupcount=220+Rnd(100)
			If puptype=2 Then bulletdistance=35:bulletnumber=4:bulletspeed=7:pupcount=50+Rnd(20)
			If puptype=3 Then bulletdistance=10:bulletnumber=20:bulletspeed=5:pupcount=100+Rnd(100)
			If puptype=4 Then p1\score=p1\score+1000:pupcount=0
			If puptype=5 Then p1\shield=700:p1\autoshield=1000:pupcount=0
			If puptype=6 Then p1\life=130:pupcount=0
		EndIf		
	EndIf
	
	If enemyships=0 And asters=0 And clearlevelcounter<=0 And level>0 Then clearlevelcounter=430:PlaySound click_sound
	
	If alienswelcome>0
		;unexpected guests!
		If alienswelcomecounter>0
			alienswelcomecounter=alienswelcomecounter-1
		Else
			e.enemy=New enemy
			e\x=Rnd(width)
			If Rnd(0,2)<1 Then e\y=-200 Else e\y=height+200
			e\xvel=Rnd(4)-2
			e\yvel=0
			e\targcount=0
			e\shooter=1
			e\agression=999-(level*1.3)
			e\aim=3
			e\life=30+Rnd(50)
			alienswelcome=Int(Rnd(0,1.9))
			alienswelcomecounter=4000+Rnd(10000)
		EndIf
	EndIf
	
	;SetFont smallfont
	If clearlevelcounter>0
	clearlevelcounter=clearlevelcounter-1
		If bulletsfire>0 Then
			percent=(bulletshit*100)/bulletsfire
		Else
			percent=0
		EndIf
		Color 0,10,70
		;SetFont hugefont
		Text (width/2)-70,height/2-50,"level "+(level+1)
		;SetFont smallfont
		Color 200,200,255
		Text (width/2)-50,(height/2)-40,"level "+level+" cleared"
		Text (width/2)-50,height/2,"bullets fired "+bulletsfire
		Text (width/2)-50,(height/2)+15,"hits "+bulletshit
		Text (width/2)-50,height/2+30,"hit percentage "+percent+"%"
		If percent>75
			If percent>99
				Text (width/2)-50,height/2+60,"perfect shooting! 3000 bonus"
			Else	
				Text (width/2)-50,height/2+60,"excellent shooting! 600 bonus"	
			EndIf
		EndIf
	EndIf
	If clearlevelcounter=0 Then clearlevelcounter=-1:levelcleared()
	
	Color 0,10,70
	;SetFont hugefont
	Text width-150,20,level
	;SetFont smallfont
	Color 0,50,190
	For t=1 To p1\lives
		;draw lives
		Oval (width-170)+(t*20),32,10,10
	Next	
	If p1\score>=targetscorefornextlife Then
		;extra life earned
		targetscorefornextlife=targetscorefornextlife+10000
		PlaySound powerup_sound
		p1\lives=p1\lives+1
	EndIf

	Color 200,200,255
	Nowtimer=MilliSecs()-Lasttimer
	If nowtimer<9
		Repeat:Until MilliSecs()-Lasttimer>=9
	Else
;		Text width/2-10,20,"- low fps -"
	EndIf
	If debug=2
		;debug stuff
		Text 0,0,p1\life
		Text 0,20,puptype
		Text 0,50,alienswelcomecounter+","+alienswelcome
		Text 0,40,MilliSecs()-Lasttimer
		Text 0,60,p1\an
	EndIf
	Lasttimer=MilliSecs()
	Text width-150,0,"score:"+p1\score
	If p1\score>hiscore Then hiscore=p1\score
	Text width-150,15,"hiscore:"+hiscore

	;VWait
	Flip									; Flip the buffers, to double buffer.
Wend
End	


;---------------------------------------------------------

; Functions:
Function UpdateShip()
	For p.playership=Each playership					; Visit all entries in the list with this loop.

		p\anvel=p\anvel*0.91
		p\an=p\an+p\anvel
		If p\an>360 Then p\an=p\an-360
		If p\an<0 Then p\an=p\an+360
		
		p\xvel=p\xvel+(p\speed*Cos(p\an))
		p\yvel=p\yvel+(p\speed*Sin(p\an))
		p\xvel=p\xvel*0.995	
		p\yvel=p\yvel*0.995	
		p\Lastx=p\x
		p\Lasty=p\y
		p\x=p\x+p\xvel : p\y=p\y+p\yvel
		p\speed=p\speed*0.95
	
		If gameovercounter<=0 Then 
			If p\x<-10 Then p\x=width+10
			If p\x>width+10 Then p\x=-10
			If p\y<-10 Then p\y=height+10
			If p\y>height+10 Then p\y=-10
		EndIf
			
		; Draw Ship
		If p\shieldflag>0 Then
			Color 0,10,Rnd(100)+70
			Oval p\x-(28/2),p\y-(28/2),28,28,1
			Color 0,10,Rnd(100)+20
			Oval p\x-(20/2),p\y-(20/2),20,20,1
			ResumeChannel chnbuzz
			p\xvel=p\xvel*0.98
			p\yvel=p\yvel*0.98
		Else
			PauseChannel chnbuzz
		EndIf
		
		tempg=160:tempr=160
		If pupcount>0 Then tempg=255
		If p1\life<20 Then tempr=255
		Color tempr,tempg,160
		headx=p\x+(Cos(p\an)*8)
		heady=p\y+(Sin(p\an)*8)
		Leftx=p\x+(Cos(p\an-140)*8)
		Lefty=p\y+(Sin(p\an-140)*8)
		Rightx=p\x+(Cos(p\an+140)*8)
		Righty=p\y+(Sin(p\an+140)*8)
		Line headx,heady,Rightx,Righty
		Line headx,heady,Leftx,Lefty
		Line Rightx,Righty,Leftx,Lefty	
			
		If p\life<0 Then 
			;life lost!
			PlaySound exp_sound
			makeexplosion(p\x,p\y,120,300)
			p\x=Rnd(width-200)+100
			p\y=Rnd(height-200)+100
			p\xvel=0
			p\yvel=0
			p\life=100
			p\shield=700
			p\lives=p\lives-1
			pupcount=0
			bulletdistance=20 ;distance between the bullets
			bulletnumber=10	;number of bullets allowed on screen
			bulletspeed=3
			p\autoshield=300
			If p\lives<=0 Then
				;game over
				gameovercounter=1000
				p\x=-1000
				p\y=-1000
			EndIf
		EndIf	
	Next
End Function

Function UpdateAsteroid(fla)
	;do the asteroids
	count=0
	For p.asteroid=Each asteroid

		p\x=p\x+p\xvel : p\y=p\y+p\yvel
		;off screen maybe?
		If p\x<(p\size/2)*-1 Then p\x=width+(p\size/2)
		If p\x>width+(p\size/2) Then p\x=(p\size/2)*-1
		If p\y<(p\size/2)*-1 Then p\y=height+(p\size/2)
		If p\y>height+(p\size/2) Then p\y=(p\size/2)*-1
		
		If fla=0
			For f.playership=Each playership
			;check for collision with player ship
			If f\x+5>p\x-(p\size/2) And f\x-5<p\x+(p\size/2) And f\y+5>p\y-(p\size/2) And f\y-5<p\y+(p\size/2)
			;If RectsOverlap(f\x,f\y,10,10,p\x,p\y,p\size,p\size)
				If f\shieldflag<1
					f\life=f\life-p\size/2
					makeexplosion(f\x,f\y,60,20)
					;SoundPan smexp_sound,(((f\x)-(width/2))/(width/2))*audioseperation
					PlaySound smexp_sound
				Else
					makeexplosion(f\x,f\y,30,10)
					;SoundPan lowsmexp_sound,(((f\x)-(width/2))/(width/2))*audioseperation
					PlaySound lowsmexp_sound
				EndIf
				oldxvel#=f\xvel
				oldyvel#=f\yvel
				f\xvel=f\xvel+(p\xvel)/1
				f\yvel=f\yvel+(p\yvel)/1
				p\xvel=p\xvel+(oldxvel/20)
				p\yvel=p\yvel+(oldyvel/20)				
			EndIf
			Next
		EndIf	
		; draw asteroid	
		Color p\r,p\g,p\b
		Oval p\x-(p\size/2),p\y-(p\size/2),p\size,p\size,1
		;Oval p\x,p\y,p\size,p\size,1
		count=count+1
	Next
	Return count
End Function

Function UpdateEnemy()
	;do the enemies
	count=0
	For p.enemy=Each enemy

		p\x=p\x+p\xvel : p\y=p\y+p\yvel
		If p\shooter=1
			Color 150,70,150
			sp#=0.013
			If p\x>p\targx Then p\xvel=p\xvel-sp
			If p\y>p\targy Then p\yvel=p\yvel-sp
			If p\x<p\targx Then p\xvel=p\xvel+sp
			If p\y<p\targy Then p\yvel=p\yvel+sp
			p\xvel=p\xvel*0.994
			p\yvel=p\yvel*0.994
			p\targcount=p\targcount-1
			If p\targcount<=0 Then p\targcount=200+Rnd(700):p\targx=Rnd(width-100)+50:p\targy=Rnd(height-100)+50
			If Rnd(200,1000)>p\agression Then enemyfirebullet(450,p\x,p\y,p\xvel,p\yvel,1.8,p\aim)
		Else
			Color 250,100,10
			If p\x<0 Or p\x>((width)-5) Then p\xvel=-p\xvel
			If p\y<0 Or p\y>((height)-5) Then p\yvel=-p\yvel
			If Rnd(1000)>p\agression Then enemyfirebullet(450,p\x,p\y,p\xvel,p\yvel,enemybulletspeed,p\aim)
		EndIf
		For f.playership=Each playership
			;check for collision with player ship
			If f\x>p\x-15 And f\x<p\x+15 And f\y>p\y-10 And f\y<p\y+10
				If f\shieldflag<1
					f\life=f\life-10
					makeexplosion(f\x,f\y,60,20)
					;SoundPan smexp_sound,(((f\x)-(width/2))/(width/2))*audioseperation
					PlaySound smexp_sound
				Else
					makeexplosion(f\x,f\y,30,10)
					SoundPan lowsmexp_sound,(((f\x)-(width/2))/(width/2))*audioseperation
					PlaySound lowsmexp_sound
				EndIf
				f\xvel=(f\xvel+p\xvel)/1
				f\yvel=(f\yvel+p\yvel)/1				
			EndIf
		Next		

		Oval p\x,p\y,15,10,1
		
		count=count+1
	Next
	Return count
End Function

Function updatebullets()
	For f.bullet=Each bullet
		f\x=f\x+f\xvel
		f\y=f\y+f\yvel
			If f\x<-10 Then f\x=width+10
			If f\x>width+10 Then f\x=-10
			If f\y<-10 Then f\y=height+10
			If f\y>height+10 Then f\y=-10
		f\life=f\life-1
		Color 255,255,255
		Plot f\x,f\y
		If f\fromwho=1
		;if coming from player...
		Color 255,255,255
		For p.enemy=Each enemy
			If f\x>p\x-10 And f\x<p\x+10 And f\y>p\y-10 And f\y<p\y+10
				bulletshit=bulletshit+1
				makeexplosion(f\x,f\y,70,20)
				f\life=-1
				p\life=p\life-Rnd(50)
				p\xvel=p\xvel+f\xvel/2
				p\yvel=p\yvel+f\yvel/2
				p1\score=p1\score+25
				If p\life<=0 Then
					makeexplosion(f\x,f\y,70,40)
					p1\score=p1\score+50
					SoundPan exp_sound,(((p\x)-(width/2))/(width/2))*audioseperation
					PlaySound exp_sound
					Delete p
				Else
					SoundPan smexp_sound,(((p\x)-(width/2))/(width/2))*audioseperation
					PlaySound smexp_sound
				EndIf
			EndIf
		Next
		For h.asteroid=Each asteroid
			If f\x>h\x-(h\size/2) And f\x<h\x+(h\size/2) And f\y>h\y-(h\size/2) And f\y<h\y+(h\size/2)
				makeexplosion(f\x,f\y,70,20)
				bulletshit=bulletshit+1
				f\life=-1
				h\xvel=h\xvel+f\xvel/15
				h\yvel=h\yvel+f\yvel/15
				h\size=h\size-20
				SoundPan smexp_sound,(((h\x)-(width/2))/(width/2))*audioseperation
				PlaySound smexp_sound
				If h\size<10 Then
					Delete h
					p1\score=p1\score+20
				Else
					p1\score=p1\score+5
					i.asteroid=New asteroid
					i\x=h\x:i\y=h\y
					i\size=h\size+Rnd(6)-3
					i\r=h\r:i\g=h\r:i\b=h\b
					i\an=h\an+(Rnd(6)-3)*6
					If i\an>360 Then i\an=i\an-360
					If i\an<0 Then i\an=i\an+360
					i\speed=h\speed
					i\xvel=(i\speed*Cos(i\an))
					i\yvel=(i\speed*Sin(i\an))
				EndIf
			EndIf
		Next
		Else
		;from enemy
		Color 0,255,255
		For j.playership=Each playership
			If f\x>j\x-10 And f\x<j\x+10 And f\y>j\y-10 And f\y<j\y+10
				If j\shieldflag<1
					j\life=j\life-(30+Rnd(10))
					makeexplosion(f\x,f\y,70,40)
					SoundPan smexp_sound,(((j\x)-(width/2))/(width/2))*audioseperation
					PlaySound smexp_sound
				Else
					makeexplosion(f\x,f\y,30,10)
					SoundPan lowsmexp_sound,(((j\x)-(width/2))/(width/2))*audioseperation
					PlaySound lowsmexp_sound
				EndIf
				makeexplosion(f\x,f\y,70,40)
				f\life=-1

			EndIf
		Next			
		EndIf
		Plot f\x,f\y
		If f\life<0 Then
			Delete f
			bulletsfired=bulletsfired-1
		EndIf
	Next
End Function

Function firebullet(life,speed#)
;fire a bullet if you can
If bulletsfired<bulletnumber
	bulletsfired=bulletsfired+1
	SoundPan fire_sound,(((p1\x)-(width/2))/(width/2))*audioseperation
	PlaySound fire_sound
	f.bullet= New bullet
	bulletsfire=bulletsfire+1
	If pupcount>0 Then pupcount=pupcount-1
	f\x=p1\x
	f\y=p1\y
	f\xvel=p1\xvel+(speed*Cos(p1\an))
	f\yvel=p1\yvel+(speed*Sin(p1\an))
	f\life=life
	f\fromwho=1
EndIf
End Function

Function enemyfirebullet(life,x#,y#,xvel#,yvel#,bulspeed#,aim#)
	angle#=ATan2(p1\x-x,p1\y-y)
	ang#=(90-angle#)+(Rnd(aim)-(aim/2))
	f.bullet= New bullet
	SoundPan fire_sound,(((x)-(width/2))/(width/2))*audioseperation
	PlaySound fire_sound
	f\x=x
	f\y=y
	f\xvel=(Cos(ang)*bulspeed)
	f\yvel=(Sin(ang)*bulspeed)
	f\life=life
	f\fromwho=2
End Function

Function makeexplosion(xe,ye,Inten,part)
	For k=1 To part
		f.Frag=New Frag
		f\x=xe
		an=Rnd(0,359)
		f\y=ye
		f\xs=Cos( an ) * Rnd( Inten-50,Inten )/10
		f\ys=Sin( an ) * Rnd( Inten-50,Inten )/10
		f\r=255:f\g=255:f\b=255
	Next
End Function

Function UpdateExplosion()
	;update the explosion particles
	For f.Frag=Each Frag
		f\xs=f\xs*0.99
		f\ys=f\ys*0.99
		f\x=f\x+f\xs
		f\y=f\y+f\ys
		f\ys=f\ys+0.06
		Color f\r,f\g,f\b
		Plot f\x,f\y
		If f\x<0 Or f\x>=width Or f\y>=height
			Delete f
		Else If f\b>10
			f\b=f\b-7
		Else If f\g>10
			f\g=f\g-4
		Else If f\r>0
			f\r=f\r-2
			If f\r<=0 Then Delete f
		EndIf
	Next
End Function

Function levelcleared()
		; the level is done, so make a new one!
		If level=0
			For en=1 To 3
			g.asteroid=New asteroid
			g\x=Rnd(width)
			g\y=Int(Rnd(0,1))*height
			g\an=Rnd(359)
			g\speed=Rnd(200)/100
			g\xvel=g\xvel+(g\speed*Cos(g\an))
			g\yvel=g\yvel+(g\speed*Sin(g\an))
			;g\xvel=(Rnd(400)-200)/100
			;g\yvel=(Rnd(400)-200)/100
			g\size=Rnd(60,80)
			g\r=Rnd(30,80)
			g\g=Rnd(25,60)
			g\b=0
			Next			
			level=1
		Else
			level=level+1
			pupprobability=5000-(level*150):If pupprobability<100 Then pupprobability=100	;more frequent powerups as time progresses	
		If bulletsfire>0 Then
			percent=(bulletshit*100)/bulletsfire
		Else
			percent=0
		EndIf
		;add bonus
		If percent>75 Then p1\score=p1\score+600
		If percent>99 Then p1\score=p1\score+3000
	
	; make random level	
	Selection=Int(Rnd(1,8))
		
	If Selection<=4 Then 
		;normal level
		numofenemi=1+Int(Rnd(0,level/2))
		For en=1 To numofenemi
		e.enemy=New enemy
		e\x=Rnd(width)
		e\y=Rnd(height)
		e\xvel=Rnd(4)-2
		e\yvel=Rnd(4)-2
		e\agression=999-(level*(Int(Rnd(0,1.5))))
		e\aim=3
		e\life=30+Rnd(50)
		enemybulletspeed=1.2
		If level>=5 Then If Rnd(0,5)>=4 Then e\shooter=1:e\agression=999-(level*0.8)+(numofenemi/2)
		If level>=12 Then If Rnd(0,2)>=1 Then e\shooter=1:e\agression=999-(level*0.8)+(numofenemi/2)
		Next
	
		For en=1 To 1+Int(Rnd(level/2,level/1.3))
		g.asteroid=New asteroid
		g\x=Rnd(width)
		g\y=Int(Rnd(0,1))*height
		g\an=Rnd(359)
		g\speed=Rnd(150)/100
		g\xvel=g\xvel+(g\speed*Cos(g\an))
		g\yvel=g\yvel+(g\speed*Sin(g\an))
		g\size=Rnd(60,80)
		g\r=Rnd(30,80)
		g\g=Rnd(25,60)
		g\b=0
		life=100
		Next
	
	EndIf
	If Selection>=7 Then
		;only enemies level
		numofenemi=2+Int(Rnd(level/2.5))
		For en=1 To numofenemi
		e.enemy=New enemy
		e\x=Rnd(width)
		e\y=Rnd(height)
		e\xvel=Rnd(4)-2
		e\yvel=Rnd(4)-2
		e\agression=999-(level*(Int(Rnd(0,1.5))))
		e\aim=3
		e\life=50+Rnd(50)
		enemybulletspeed=1.2+(level/25)
		If level>=5 Then If Rnd(0,5)>=4 Then e\shooter=1:e\agression=1000-(level*1)+(numofenemi/2)
		If level>=12 Then If Rnd(0,2)>=1 Then e\shooter=1:e\agression=1000-(level*1)+(numofenemi/2)	
		Next
	EndIf
	
	If Selection=5 Then	
		;only asteroids level
		For en=1 To 1+Int(Rnd(level/2,level/1.5))
		g.asteroid=New asteroid
		g\x=Rnd(width)
		g\y=Int(Rnd(0,1))*height
		g\an=Rnd(359)
		g\speed=Rnd(200)/100
		g\xvel=g\xvel+(g\speed*Cos(g\an))
		g\yvel=g\yvel+(g\speed*Sin(g\an))
		g\size=Rnd(60,80)
		g\r=Rnd(30,80)
		g\g=Rnd(25,60)
		g\b=0
		life=100
		Next
	EndIf

	If Selection=6 Then	
		;huge asteroids level
		For en=1 To Int(Rnd(0,2))+1
		g.asteroid=New asteroid
		g\x=Rnd(width)
		g\y=Int(Rnd(0,1))*height
		g\an=Rnd(359)
		g\speed=Rnd(120)/100
		g\xvel=g\xvel+(g\speed*Cos(g\an))
		g\yvel=g\yvel+(g\speed*Sin(g\an))
		g\size=Rnd(80,150)
		g\r=Rnd(30,80)
		g\g=Rnd(25,60)
		g\b=0
		life=100
		Next
	EndIf
	
	p1\life=100+(level*3)
	bulletsfire=0:bulletshit=0
	p1\autoshield=300
	p1\shieldflag=0
	p1\shield=600
	If level>=5 Then alienswelcome=Int(Rnd(0,3))
	alienswelcomecounter=1000+Rnd(9000)
	
	EndIf
End Function

Function initgame()
; called at the beginning of each game

p1\x=400
p1\y=300
p1\xvel=0
p1\yvel=0
p1\an=0
p1\life=100
p1\lives=3
p1\shield=700
p1\autoshield=300
p1\shieldflag=0
p1\score=0
pupcount=0
bulletdistance=20 ;distance between the bullets
bulletnumber=10	;number of bullets allowed on screen
bulletspeed=3
enemybulletspeed=1.2

clearlevelcounter=-1
level=0
pupprobability=5000
targetscorefornextlife=10000

;clear all!
	For p.asteroid=Each asteroid
	Delete p
	Next
	
	For g.enemy=Each enemy
	Delete g
	Next
	
	For h.bullet=Each bullet
	Delete h
	Next

End Function


;--------------------------------------- TITLE -----------------
Function titlescreen()
PauseChannel chnbuzz

If Not ChannelPlaying(music)
	music=PlayMusic( "kamelpiano.mid" )
End If


If OpenFile("aristoidsprefs")
	loadhighscores("aristoidsprefs"):;CloseFile("aristoidsprefs")
Else
	blankhighscores("aristoidsprefs")
	loadhighscores("aristoidsprefs")
EndIf
hiscore=hiscores(1)

		; get some asteroids in
		For en=1 To 5+Int(Rnd(15))
		g.asteroid=New asteroid
		g\x=Rnd(width)
		g\y=Int(Rnd(0,1))*height
		g\an=Rnd(359)
		g\speed=(Rnd(200)/100)+0.05
		g\xvel=g\xvel+(g\speed*Cos(g\an))
		g\yvel=g\yvel+(g\speed*Sin(g\an))
		g\size=Rnd(10,80)
		g\r=Rnd(30,80)
		g\g=Rnd(25,60)
		g\b=0
		life=100
		Next
	
Repeat
Cls




Text 30,30,"aristoids"
Text 30,45,"by aristides mytaras"
Text 30,60,"aristid@freemail.gr"


If KeyDown(23)=0

If KeyDown(46) And controlcountdown<=0
	controltype=controltype+1:controlcountdown=30
	If controltype>2 Then controltype=0
EndIf
If controlcountdown>0 Then controlcountdown=controlcountdown-1

Color 0,10,70
;SetFont hugefont
Text 80,100,"score:"+hiscore
Text width-350,height-110,"i for info"

;Text 50,305,"high scores"

Color 200,200,255

Text 30,90,"last score:"+p1\score
Text 30,105,"high score:"+hiscore

;SetFont smallfont
For t=1 To 10
If t<10 Then
	Text 100,(t*15)+160,t+". "+hinames(t)
Else
	Text 91,(t*15)+160,t+". "+hinames(t)
EndIf
Text 250,(t*15)+160,hiscores(t)
Text 340,(t*15)+160,levelreached(t)
Text 420,(t*15)+160,hidates(t)
If control(t)=0 Then tempstr$="ast"
If control(t)=1 Then tempstr$="beg"
If control(t)=2 Then tempstr$="mix"
Text 380,(t*15)+160,tempstr$
Next

Color 0,10,70
;SetFont hugefont
If controltype=0 Then tempstr$="asteroids"
If controltype=1 Then tempstr$="beginner"
If controltype=2 Then tempstr$="mixed"
Text 30,height-80,tempstr$

Color 200,200,255
;SetFont smallfont
Text 30,height-70,"control type:"+tempstr$

Text width-300,height-80,"switch resolutions with f1,f2,f3"
Text width-300,height-65,"press i for more info"
Else
Color 0,10,70
;SetFont hugefont
Text 190,310,width+"x"+height
Color 200,200,255
SetFont smallfont
Text 30,120,"use left/right/</> to turn, up/z for thrust,"
Text 30,135,"down or shift for shield/brake, ctrl or space to fire"
Text 30,155,"or a joystick, or a hotrod - press c to select control type"
Text 30,175,"destroy everything to advance through the levels"
Text 30,195,"your shield and energy replenish at the end of each level"
Text 30,230,"achieve a 75% or better accuracy for bonus"
Text 30,245,"achieve a 100% accuracy for huge bonus"
Text 30,260,"extra life awarded every 10000 points"
Text 30,310,"f1 to switch to 640x480"
Text 30,325,"f2 to switch to 800x600"
Text 30,340,"f3 to switch to 1024x800"
Text 30,355,"resolution has impact on gameplay. normal at 1024x768 (default), frantic at 640x480"
Text 30,390,"music (c)1998 by aristides mytaras"
Text 30,height-65,"this version of aristoids is freeware as long as the code stays intact"
Text 30,height-50,"you ar not allowed to distribute a modified version of aristoids"
Text 30,height-35,"programmed in blitz basic on 18-19 sept 2000 in athens, greece"


EndIf

If KeyDown(1) Then End

If KeyDown(59)
Repeat
nono=0
For t=0 To 255
If KeyDown(t)<>0 Then nono=1
Next
Until nono=0
width=640:height=480:depth=16
Graphics width,height
SetBuffer BackBuffer():savehighscores("aristoidsprefs")
EndIf
If KeyDown(60)
Repeat
nono=0
For t=0 To 255
If KeyDown(t)<>0 Then nono=1
Next
Until nono=0
width=800:height=600:depth=16
Graphics width,height
SetBuffer BackBuffer():savehighscores("aristoidsprefs")
EndIf
If KeyDown(61)
Repeat
nono=0
For t=0 To 255
If KeyDown(t)<>0 Then nono=1
Next
Until nono=0
width=1024:height=768:depth=16
Graphics width,height
SetBuffer BackBuffer():savehighscores("aristoidsprefs")
EndIf

If Rnd(1000)>970 Then makeexplosion(Rnd(width),Rnd(height),Rnd(100)+50,Rnd(300)+80)

asters=UpdateAsteroid(1)
UpdateExplosion()

	Nowtimer=MilliSecs()-Lasttimer
	If nowtimer<9
		Repeat:Until MilliSecs()-Lasttimer>=9
	EndIf
	Lasttimer=MilliSecs()

Flip
Until KeyDown(57) Or KeyDown(29) Or (joystick<>0 And JoyDown(1))

Repeat
nono=0
For t=0 To 255
If KeyDown(t)<>0 Or (joystick<>0 And JoyDown(1)) Then nono=1
Next
Until nono=0

	For p.asteroid=Each asteroid
	Delete p
	Next

savehighscores("aristoidsprefs")
initgame()
levelcleared()

StopChannel Music 

End Function










;--------------highscore functions------------
;----------------------------
Function savehighscores(name$)
;name$=name$+".txt"
savegame=WriteFile(name)
WriteLine savegame,"aristoids save file"
WriteLine savegame,CurrentDate()+" - "+CurrentTime()
For t=1 To 11
WriteLine savegame,hiscores(t)
WriteLine savegame,hinames(t)
WriteLine savegame,hidates(t)
WriteLine savegame,levelreached(t)
WriteLine savegame,control(t)
Next
WriteLine savegame,width
WriteLine savegame,height
WriteLine savegame,controltype
CloseFile(savegame)
End Function

;----------------------------
Function loadhighscores(name$)
;name$=name$+".txt"
savegame=ReadFile(name)
For no=1 To 2:nouse$=ReadLine$(savegame):Next
;While Not Eof(savegame)
;	Print ReadLine$(savegame)
;Wend
For t=1 To 11
hiscores(t)=ReadLine$(savegame):;Print "loader-"+t+hiscores(t)
hinames(t)=ReadLine$(savegame):;Print hinames$(t)
hidates(t)=ReadLine$(savegame)
levelreached(t)=ReadLine$(savegame)
control(t)=ReadLine$(savegame)
Next
width=ReadLine$(savegame)
height=ReadLine$(savegame)
controltype=ReadLine$(savegame)
If GraphicsHeight()<>height And GraphicsWidth()<>width Then Graphics width,height:SetBuffer BackBuffer()
CloseFile savegame
End Function

;-----------------------------
Function blankhighscores(name$)
;name$=name$+".txt"
savegame=WriteFile(name)
WriteLine savegame,"aristoids save file"
WriteLine savegame,CurrentDate()+" - "+CurrentTime()+" - default"
For t=1 To 11
WriteLine savegame,(11-t)*100
If Rnd(1,3)<2 Then WriteLine savegame,"aristides" Else WriteLine savegame,"vasso"
WriteLine savegame," "
WriteLine savegame,"0"
WriteLine savegame,0
Next
WriteLine savegame,width
WriteLine savegame,height
WriteLine savegame,controltype
CloseFile(savegame)
End Function

;-----------------------------
Function sorthighscores(file$,name$,score,levelreach)
loadhighscores(file)

done=0
t=1
Repeat
	If hiscores(t)<score Then
		done=1
		For e=10 To t Step -1
			hiscores(e+1)=hiscores(e)
			hinames(e+1)=hinames(e)
			hidates(e+1)=hidates(e)
			levelreached(e+1)=levelreached(e)
			control(e+1)=control(e)
		Next
		hiscores(t)=score
		hinames(t)=name
		hidates(t)=CurrentDate()
		levelreached(t)=levelreach
		control(t)=controltype
	EndIf
t=t+1
Until t=10 Or done=1

End Function

;-------------------------------
Function checkhighscore(thisfile$,sco)
loadhighscores(thisfile$)
highscoreplace=20
For t=10 To 1 Step -1
	If hiscores(t)<=sco Then highscoreplace=t
	;Print t+" "+hiscores(t)
Next
Return highscoreplace
End Function

;---------------------------------
Function entername(startname$,score)
yourname$=startname
PlaySound buzz_sound

flush=10
Repeat
flush=GetKey()
Print flush
Until flush=0

While Not (KeyDown(1) Or KeyDown(28))
Cls

a=GetKey()
If a>=32 And a<=122 And Len(yourname)<15
	yourname=yourname+Chr$(a)
EndIf
If (KeyHit(14) Or KeyHit(211)) And Len(yourname)>0 Then yourname=Left$(yourname,Len(yourname)-1)

Color 0,10,70
;SetFont hugefont
Text 60,115,score
Text 60,200,yourname


Color 200,200,255

Text 40,100,"you have achieved a high score"
Text 40,115,"please enter your name:"
Text 60,170,yourname

If Rnd(1000)>970 Then makeexplosion(Rnd(width),Rnd(height),Rnd(100)+50,Rnd(300)+80)
UpdateExplosion()
	Nowtimer=MilliSecs()-Lasttimer
	If nowtimer<9
		Repeat:Until MilliSecs()-Lasttimer>=9
	EndIf
	Lasttimer=MilliSecs()

Flip
Wend
;Return yourname ;doesnt work!
End Function