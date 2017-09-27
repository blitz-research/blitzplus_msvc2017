;wizard Wars v1.3
;Copyright ©1997-2002 EdzUp
;written by Ed Upton

AppTitle "Wizard Wars 1.4"


Graphics 640, 480, 16, 2

;adjust this value if mouse seems to double click or the keys operate to fast
Global Tier=2

;adjust this value for different maps (256 minimum to 1000 max)
;*********************************************************************************
;PLEASE NOTE SELECTING LARGE MAPS MAY CAUSE UNEXPECTED RESULTS FROM YOUR COMPUTER
;PLEASE USE THIS VALUE CAREFULLY, 1000x1000 MAPS WORK FINE ON MY 128Mb RAM MACHINE
;BUT CAUSE PROBLEMS ON MACHINES WITH 32Mb RAM
;*********************************************************************************
Global Mapsize=256
If Mapsize<256 Then Mapsize=256
If Mapsize>1000 Then Mapsize=1000

;standard tiles (must be in same directory as .bb file)
;base and tiles are 64x32
Global cobble1=LoadImage("graphics\cobble167.bmp")
Global cobble2=LoadImage("graphics\cobble1672.bmp")
Global wallend=LoadImage("graphics\3Dstone1end.bmp")
Global grass1=LoadImage("graphics\grass1.bmp")
Global grass2=LoadImage("graphics\grass2.bmp")
Global grass3=LoadImage("graphics\grass3.bmp")
Global grass4=LoadImage("graphics\grass4.bmp")
Global water1=LoadImage("graphics\water1.bmp")
Global water2=LoadImage("graphics\water2.bmp")
Global water3=LoadImage("graphics\water3.bmp")
Global water4=LoadImage("graphics\water4.bmp")
Global rock=LoadImage("graphics\rock.bmp")
Global rock2=LoadImage("graphics\rock2.bmp")
Global rock3=LoadImage("graphics\rock3.bmp")
Global mousecursor=LoadImage("graphics\pointer.bmp")
Global cursor1=LoadImage("graphics\select1.bmp")
Global cursor2=LoadImage("graphics\select2.bmp")
Global cursor3=LoadImage("graphics\select3.bmp")
Global title=LoadImage("graphics\title.bmp")
Global OptionsTitle=LoadImage("graphics\Options.bmp")
Global square=LoadImage("graphics\townsquare.bmp")
Global house1=LoadImage("graphics\house1.bmp")
Global house2=LoadImage("graphics\house2.bmp")
Global shop1=LoadImage("graphics\shop1.bmp")
Global shop2=LoadImage("graphics\shop2.bmp")
Global inn1=LoadImage("graphics\inn1.bmp")
Global inn2=LoadImage("graphics\inn2.bmp")

Global tree=LoadImage("graphics\Tree.bmp")
Global shadowwood=LoadImage("graphics\shadowwood.bmp")
Global magicwood=LoadImage("graphics\magicwood.bmp")
Global Castle=LoadImage("graphics\castle1.bmp")
Global Citadel=LoadImage("graphics\citadel.bmp")
Global Vortex=LoadImage("graphics\vortex.bmp")
Global MagicBolt=LoadImage("graphics\Bolt.bmp")

Global Zap1=LoadImage("graphics\Zap1.bmp")
Global Zap2=LoadImage("graphics\Zap2.bmp")

;wizard images
Global wizard1=LoadImage("graphics\wizard1.bmp")
Global wizard2=LoadImage("graphics\wizard2.bmp")
Global wizard3=LoadImage("graphics\wizard3.bmp")
Global wizard4=LoadImage("graphics\wizard4.bmp")
Global wizard5=LoadImage("graphics\wizard5.bmp")
Global wizard6=LoadImage("graphics\wizard6.bmp")
Global wizard7=LoadImage("graphics\wizard7.bmp")
Global wizard8=LoadImage("graphics\wizard8.bmp")

Global DeadSmall=LoadImage("graphics\SmallCorpse.bmp")
Global DeadMedium=LoadImage("graphics\MediumCorpse.bmp")
Global DeadLarge=LoadImage("graphics\LargeCorpse.bmp")

Global WingsImage=LoadImage("graphics\Wings.bmp")
Global ShieldImage=LoadImage("graphics\Shield.bmp")
Global SwordImage=LoadImage("graphics\Sword.bmp")
Global ArmourImage=LoadImage("graphics\Armour.bmp")
Global BowImage=LoadImage("graphics\Bow.bmp")

Global Indicate=LoadImage("graphics\alignment.bmp")
Global Chaos=LoadImage("graphics\Chaos.bmp")
Global Law=LoadImage("graphics\law.bmp")

;Load in NPC creatures
Global sheep=LoadImage("graphics\sheep.bmp")
Global Cow=LoadImage("graphics\Cow.bmp")
Global Boar=LoadImage("graphics\Boar.bmp")
Global MaleCiv=LoadImage("graphics\CivMale.bmp")
Global FemaleCiv=LoadImage("graphics\CivFemale.bmp")

;Creatures
Global UndeadDragon=LoadImage("graphics\DragonU.bmp")
Global RedDragon=LoadImage("graphics\DragonR.bmp")
Global GreenDragon=LoadImage("graphics\DragonG.bmp")
Global GoldDragon=LoadImage("graphics\DragonY.bmp")
Global PlatinumDragon=LoadImage("graphics\DragonP.bmp")
Global BlueDragon=LoadImage("graphics\DragonB.bmp")
Global Cobra=LoadImage("graphics\cobra.bmp")
Global Centaur=LoadImage("graphics\Centaur.bmp")
Global Blob=LoadImage("graphics\Blob.bmp")
Global Fire=LoadImage("graphics\Fire.bmp")
Global Fire2=LoadImage("graphics\Fire2.bmp")
Global Giant=LoadImage("graphics\Giant.bmp")
Global Hydra=LoadImage("graphics\Hydra.bmp")
Global Skeleton=LoadImage("graphics\Skeleton.bmp")
Global Spectre=LoadImage("graphics\Spectre.bmp")
Global Wraith=LoadImage("graphics\Wraith.bmp")
Global Knight=LoadImage("graphics\man1.bmp")
Global Eagle=LoadImage("graphics\Eagle.bmp")
Global Faun=LoadImage("graphics\Faun.bmp")
Global Gorilla=LoadImage("graphics\Gorilla.bmp")
Global Orc=LoadImage("graphics\Orc.bmp")
Global Rat=LoadImage("graphics\Rat.bmp")
Global Wolf=LoadImage("graphics\Wolf.bmp")
Global Unicorn=LoadImage("graphics\Unicorn.bmp")
Global Troll=LoadImage("graphics\Troll.bmp")
Global Bear=LoadImage("graphics\Bear.bmp")
Global Bat=LoadImage("graphics\Bat.bmp")
Global Horse=LoadImage("graphics\Horse.bmp")
Global Pegasus=LoadImage("graphics\Pegasus.bmp")
Global Gryphon=LoadImage("graphics\Gryphon.bmp")
Global Ghost=LoadImage("graphics\Ghost.bmp")
Global Goblin=LoadImage("graphics\Goblin.bmp")
Global Vampire=LoadImage("graphics\Vampire.bmp")
Global Elf=LoadImage("graphics\Elf.bmp")
Global Ghoul=LoadImage("graphics\ghoul.bmp")
Global Manticore=LoadImage("graphics\Manticore.bmp")
Global Harpy=LoadImage("graphics\Harpy.bmp")
Global Lion=LoadImage("graphics\Lion.bmp")
Global Ogre=LoadImage("graphics\Ogre.bmp")
Global Zombie=LoadImage("graphics\Zombie.bmp")

;load sounds used in the game
Global Zap=LoadSound("sounds\Zap.wav")
Global Wind=LoadSound("sounds\Wind.wav")
Global Hit1=LoadSound("sounds\oooww1.wav")
Global Hit2=LoadSound("sounds\oooww2.wav")
Global Hit3=LoadSound("sounds\oooww3.wav")
Global Shoot=LoadSound("sounds\throw.wav")
Global Clang=LoadSound("sounds\Swordcla.wav")
Global Thunder=LoadSound("sounds\Thunder1.wav")
Global Heart=LoadSound("sounds\heart.wav")
Global Moo=LoadSound("sounds\Moo.wav")
Global Water=LoadSound("sounds\Water.wav")
Global Baa=LoadSound("sounds\Baa.wav")
Global Firesound=LoadSound("sounds\Fire.wav")
Global BoarSound=LoadSound("sounds\Boar.wav")

Dim cred$(100)

Dim castinfo(18)
Dim filename$(10)

;spell type
Type spel
	Field name$
	Field Cost,award,range,castno,align
	Field hp,mp,ranged,undead,flying,mount,wp,attack,movement
	Field shot,shotrange
End Type

;wizard type (one for each player)
Type player
	Field x,y,tp,align,beattime,wp
	Field xoff,yoff,st
	Field hp,mp,dam
	Field Wings,Sword,Armour,ShadowForm,Shield,Bow
	Field mana,moveleft,maxmove,shotsleft,maxshots
End Type

;player casting type
Type creations
	Field x,y,owner,real
	Field tp,hp,mp,range,wp,shotsleft,maxshots
	Field undead,ranged,flying,mount,magical
	Field moveleft,maxmove,damage
End Type

;civilians that roam the land (there for players to persuade to thier side ;) )
Type civilian
	Field x,y,sex,face,tp
	Field hp,xoff,yoff
	Field st
	Field movetime
End Type

;big map to explore
Dim map%(Mapsize,Mapsize)
Dim height%(Mapsize,Mapsize)
Dim xoff%(Mapsize,Mapsize)
Dim yoff%(Mapsize,Mapsize)
Dim mon%(Mapsize,Mapsize) ;so viewer will know if there is a monster here
Dim playat%(Mapsize,Mapsize)
Dim dead(Mapsize,Mapsize)
Dim MapOp(5)
MapOp(0)=1
MapOp(1)=1
MapOp(2)=1
MapOp(3)=1

Dim li(100) ;logo images (for wavy effect)
Dim lo(100) ;offset (to offset each line) in angles

.RestartGame:

Global plx=Mapsize/2
Global ply=Mapsize/2
Global wx=32
Global wy=16
Global level=1
Global msx
Global msy
Global ang=0
Global selx    ;where the mouse cursor is currently
Global sely
Global selo=-1    ;currently selected object (player monsters etc)
Global selox
Global seloy
Global turn=0 ;turn counter
Global CurrentPlayer=0 ;whos go it is
Global Creature=0 ;This will be 1 for a Wizard and 2 for a creation (for casting system)
Global mousetime=0 ;timer for mouse buttons (to stop quick clicking)
Global keytime=0 ;timer for keys (to stop quick clicking)
Global castrange=-1 ;casting range of spell
Global castspell=-1 ;spell thats being cast (only checked if castrange > -1)
Global castno=-1 ;number of times the spell is cast
Global startcastno=-1 ;starting cast no (to see if you cast anything at all)
Global manaavail=0
Global moveavail=-1
Global shotsavail=-1
Global firearrow=-1
Global StartX=0  ;Start position for Flying creatures(They attack who they are currently over)
Global StartY=0  ;if they are not over an empty square at the end of the return to these coordinates.
Global CanFly=0  ;check for flying ability
Global CanShoot=0;check for ranged attack
Global Below=-1  ;Check To make sure Flying creatures dont Delete other creations
Global MaxPlayers=8 ;Maximum players allowed in this game
Global Sounds=0
Global Riding=-1  ; this will be a creatures ID if wizard has selected to ride a creature otherwise its 0
Global Piece=0
Global heartbeat=0 ;this give a sense of tension to the game ;)
Global comtimer=0 ;time left for message
Global EndGame=0
Global Nop=8      ;number of players
Global Lastmove=-1 ;last creature moved
Global casting=-1
Global PlayerID=-1
Global Health=-1
Global FlyRange=-1 ;range they can fly
Global WindChannel=-1
Global ThunderChannel=-1
Global FireChannel=-1
Global Synctimer = CreateTimer(60)
;message system
Dim com$(5)

Global fpstimer1#,fpstimer2#,fps

Goto Main:

;Data Block for Spells
;Spell Name
;Cost in Mana
;Mana Award for killing it
;spell range
;Starting Hit Points
;Magic Points
;Ranged attack available
;Undead
;Flying     (Can move through units but cannot end turn on another unit)
;Mount      (Wizard can ride on creature)
;Will Power (Resistance to Subversion (10 Max))
;Attack Damage (How much damage is caused by creature when it attacks
;Movement allowance per turn
;Projectile Type (0 none, 1 arrow, 2 Fireball, 3 Gas, 4 Magic Bolt)
;Projectile Range
;Number of casts
;Spell alignment

.Spellblock:
;40 monsters
Data "Bat",5,1,1,10,0,0,0,1,0,5,3,5,0,0,1,-1
Data "Rat",7,1,1,12,0,0,0,0,0,5,4,3,0,0,1,0
Data "Snake",6,1,1,10,0,0,0,0,0,6,4,1,0,0,1,1
Data "Wolf",10,1,1,20,0,0,0,0,0,7,6,3,0,0,1,-1
Data "Knight",40,5,1,50,0,0,0,0,0,8,10,1,0,0,1,0 ;If wizard is Chaos Aligned then this is a Marauder
Data "Unicorn",25,2,1,35,0,0,0,0,1,8,8,3,0,0,1,1
Data "Horse",10,2,1,30,0,0,0,0,1,6,6,3,0,0,1,1
Data "Pegasus",20,2,1,35,0,0,0,1,1,8,7,5,0,0,1,1
Data "Gryphon",20,3,1,40,0,0,0,1,1,9,10,5,0,0,1,1
Data "Giant",55,7,1,85,0,0,0,0,0,10,15,2,0,0,1,1
Data "Wraith",60,12,1,80,0,0,1,0,0,10,15,2,0,0,1,-1
Data "Spectre",45,8,1,75,0,0,1,0,0,10,12,1,0,0,1,-1
Data "Ghost",30,6,1,50,0,0,1,1,0,10,10,2,0,0,1,-1
Data "Orc",25,6,1,55,0,0,0,0,0,7,8,1,0,0,1,-1
Data "Goblin",20,4,1,30,0,0,0,0,0,5,5,1,0,0,1,-1
Data "Ogre",40,6,1,60,0,0,0,0,0,7,12,1,0,0,1,-1
Data "Skeleton",12,4,1,35,0,0,1,0,0,10,8,1,0,0,1,-1
Data "Faun",18,4,1,30,0,0,0,0,0,5,5,1,0,0,1,-1
Data "Shadow Wood",85,7,8,45,0,0,0,0,0,6,6,1,0,0,8,-1 ;Shadow Woods Gain health if create from trees
Data "Magic Fire",60,0,8,10,0,0,0,0,0,100,9999,0,0,0,1,-1
Data "Magic Wood",95,12,8,20,0,0,0,0,0,100,0,0,0,0,8,1
Data "Gooey Blob",55,5,8,10,0,0,0,0,0,100,9999,0,0,0,1,-1
Data "Red Dragon",350,90,1,150,0,1,0,1,1,9,20,4,2,4,1,-1
Data "Green Dragon",500,120,1,250,0,1,0,1,1,9,25,3,2,5,1,-1
Data "Golden Dragon",700,230,1,450,0,1,0,1,1,10,40,5,2,3,1,1
Data "White Dragon",850,450,1,800,0,1,0,1,1,10,45,6,2,5,1,1
Data "Blue Dragon",1000,600,1,1200,0,1,0,1,1,10,60,6,2,6,1,1
Data "Undead Dragon",1500,850,1,1800,0,1,1,1,1,100,90,4,3,5,1,-1
Data "Eagle",15,3,1,30,0,0,0,1,0,5,6,5,0,0,1,1
Data "Vampire",120,35,1,85,0,0,1,1,0,100,25,2,0,0,1,-1
Data "Elf",45,8,1,40,0,1,0,0,0,8,8,1,1,4,1,1
Data "Troll",55,6,1,75,0,0,0,0,0,5,15,1,0,0,1,-1
Data "Ghoul",45,5,1,50,0,0,1,0,0,6,10,1,0,0,1,-1
Data "Manticore",75,8,1,70,0,1,0,1,1,7,15,5,1,3,1,-1
Data "Harpy",60,8,1,55,0,0,0,1,0,8,15,4,0,0,1,-1
Data "Centaur",55,5,1,50,0,1,0,0,1,5,10,3,1,4,1,1
Data "Bear",40,6,1,85,0,0,0,0,0,6,20,2,0,0,1,1
Data "Lion",80,8,1,65,0,0,0,0,0,8,12,3,0,0,1,1
Data "Gorilla",35,4,1,40,0,0,0,0,0,7,10,1,0,0,1,0
Data "Hydra",55,5,1,80,0,0,0,0,0,7,25,1,0,0,1,-1
Data "Zombie",16,5,1,40,0,0,1,0,0,10,8,1,0,0,1,-1

;20 offensive and defensive spells (used differently by the program)
Data "Vengeance",50,0,8,0,0,0,0,0,0,0,10,0,0,0,1,-1 ;If successful destroys players creations
Data "Dark Power",75,0,8,0,0,0,0,0,0,0,15,0,0,0,3,-1
Data "Magic Bolt",75,0,8,0,0,0,0,0,0,0,25,0,0,0,1,0 ;Magic attack
Data "Lightning Bolt",60,0,8,0,0,0,0,0,0,0,20,0,0,0,1,1
Data "Dispell",0,0,20,0,0,0,0,0,0,0,0,0,0,0,1,0   ;destroys illusions outright
Data "Magic Shield",80,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1 ;Adds a shield to the wizard
Data "Magic Sword",75,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1  ;adds sword to wizard (can attack undead)
Data "Magic Wings",120,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0 ;Allows wizard to Fly 5 squares
Data "Shadow Form",60,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0  ;increases Wizards walk speed to 3 squares
Data "Magic Armour",75,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1 ;adds magic armour to wizard
Data "Magic Castle",150,40,8,0,0,0,0,0,0,0,0,0,0,0,1,1 ;Gives spell if wizard hides inside for a time
Data "Dark Citadel",150,40,8,0,0,0,0,0,0,0,0,0,0,0,1,-1 ;As castle but gives Chaos spell
Data "Law",100,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1 ;moves alignment 1 point towards Law
Data "Chaos",100,0,0,0,0,0,0,0,0,0,0,0,0,0,1,-1 ;moves alignment 1 point towards chaos
Data "Subversion",50,0,8,0,0,0,0,0,0,0,0,0,0,0,1,0 ;tests wizard against creature will power (if successful then wizard gains control of creature)
Data "Magic Bow",120,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1 ;Gives wizard a magic bow (range 5, can also attack undead)
Data "Magic Wall",150,0,6,0,0,0,0,0,0,0,0,0,0,0,4,0 ;creates an tough wall (can be destroyed)
Data "Vortex",200,0,8,0,0,0,0,0,0,0,0,0,0,0,1,-1 ;Creates a vortex (if creatures pass through they are changed into something else)
Data "Fireball",100,0,8,0,0,0,0,0,0,0,40,0,0,0,1,-1
Data "Warp Blast",2000,0,2,0,0,0,0,0,0,0,0,0,0,0,1,-1 ;Raises dead bodies into Undead creatures

.Main:

;reads in the Data block of spells above (Spellblock)
Restore Spellblock:
For n=0 To 60
	spell.spel = New spel
	Read spell\name$  
	Read spell\Cost   
	Read spell\award  
	Read spell\range
	Read spell\hp
	Read spell\mp
	Read spell\ranged
	Read spell\undead
	Read spell\flying
	Read spell\mount
	Read spell\wp
	Read spell\attack
	Read spell\movement
	Read spell\shot
	Read spell\shotrange
	Read spell\castno
	Read spell\align
Next

GetSavegameNames()	;retreive savegames if any
capturelogo()		;capture Wizard Wars Logo
Text 0,0,"Creating map please wait..."
Flip
createmap(1,1)		;Create a map for title anim
;Place some Fires and Blobs on Map
For ns=0 To 10		;just for effect
	xx=Rnd(Mapsize)
	yy=Rnd(Mapsize)
	mon(xx,yy)=5
	xx=Rnd(Mapsize)
	yy=Rnd(Mapsize)
	mon(xx,yy)=6
Next
ShowMap( 50, 50, 0, 0, 0 )
Flip
Titlesequence()		;Go into Title sequence
Endmove=0
selo=-1
hearttime=0
;******************************** MAIN LOOP *****************************
While Not KeyDown(1)
;	WaitTimer( synctimer )
	CheckEvent()
	If EndGame=1 Then Goto RestartGame:;if only one player left restart game
	Cls
	If shotsavail<1 Then firearrow=-1	;if no shooting available them firearrow=-1
	hearttime=hearttime-1				;heartbeat timer
	If hearttime<0						;play heartbeat
		PlaySound Heart
		hearttime=heartbeat
	EndIf
	If Windchannel=-1 Then Windchannel=PlaySound(wind);wind sound
	If ChannelPlaying(Windchannel)=0 Then WindChannel=PlaySound(Wind);wind sound
	a=Rnd(65500)						;Thunder sound
	If a<10 							;Thunder sound
		If Thunderchannel=-1 Then Thunderchannel=PlaySound(Thunder)
		If ChannelPlaying(Thunderchannel)=0 Then ThunderChannel=PlaySound(Thunder)
	EndIf
	setsounds()
	If KeyDown(15) 					;Cheat key
		manaavail=65500
		moveavail=65500
	EndIf
	If selo=-1 Then showmap(plx,ply,wx,wy,1) 		;if nothing selected show map where visual cursor is
	If selo<>-1 Then showmap(selox,seloy,wx,wy,1)	;else show map where creature is
	AlignmentIndicator(CurrentPlayer)				;place alignment indicator
	If selo=-1										;if nothing selected move visual cursor
		If KeyDown(72) And keytime<0 Or KeyDown(23) And keytime<0 
			ply=ply-1
			plx=plx-1
			keytime=Tier
		EndIf
		If KeyDown(80) And keytime<0 Or KeyDown(51) And keytime<0 
			ply=ply+1
			plx=plx+1
			keytime=Tier
		EndIf
		If KeyDown(75) And keytime<0 Or KeyDown(36) And keytime<0
			ply=ply+1
			plx=plx-1
			keytime=Tier
		EndIf
		If KeyDown(77) And keytime<0 Or KeyDown(38) And keytime<0
			ply=ply-1
			plx=plx+1
			keytime=Tier	
		EndIf
		If KeyDown(73) And keytime<0 Or KeyDown(24) And keytime<0
			ply=ply-1
			keytime=Tier
		EndIf
		If KeyDown(79) And keytime<0 Or KeyDown(50) And keytime<0
			ply=ply+1
			keytime=Tier
		EndIf 
		If KeyDown(71) And keytime<0 Or KeyDown(22) And keytime<0
			plx=plx-1
			keytime=Tier
		EndIf
		If KeyDown(81) And keytime<0 Or KeyDown(73) And keytime<0
			plx=plx+1
			keytime=Tier
		EndIf
		If plx<0 Then plx=0
		If plx>Mapsize Then plx=Mapsize
		If ply<0 Then ply=0
		If ply>Mapsize Then ply=Mapsize
	Else
		;**********************************
		;IF CREATURE SELECTED
		;THEN MOVE THEM USING MOVEMENT KEYS
		;**********************************
		If Creature>1 Then Lastmove=selo
		;ID whats selected
		If Creature=1 Then Text 320,10,"Wizard selected",1 Else 
		If Creature=2 Then Text 320,10,"Creature selected",1 Else
		If Creature=3 Then Text 320,10,"Wizard and creature selected",1
		Piece=playat(selox,seloy)
		If CanFly=0 Then playat(selox,seloy)=-1 Else playat(selox,seloy)=Below
		ox=selox
		oy=seloy
		If CanFly=0	;if unit cannot fly then move it one square at a time
			If KeyDown(72) And keytime<0 And moveavail>-1 Or KeyDown(23) And keytime<0 And moveavail>-1
				Repeat
				Until KeyDown(72)=0  And KeyDown(23)=0
				seloy=seloy-1
				selox=selox-1
				keytime=Tier
				If moveavail>0 Then moveavail=moveavail-1
			EndIf
   If KeyDown(80) And keytime<0 And moveavail>-1 Or KeyDown(51) And keytime<0 And moveavail>-1
    Repeat
    Until KeyDown(80)=0  And KeyDown(51)=0
    seloy=seloy+1
    selox=selox+1
    keytime=Tier
    If moveavail>0 Then moveavail=moveavail-1
   EndIf
   If KeyDown(75) And keytime<0 And moveavail>-1 Or KeyDown(36) And keytime<0 And moveavail>-1
    Repeat
    Until KeyDown(75)=0  And KeyDown(36)=0
    seloy=seloy+1
    selox=selox-1
    keytime=Tier
    If moveavail>0 Then moveavail=moveavail-1
   EndIf
   If KeyDown(77) And keytime<0 And moveavail>-1 Or KeyDown(38) And keytime<0 And moveavail>-1
    Repeat
    Until KeyDown(77)=0  And KeyDown(38)=0
    seloy=seloy-1
    selox=selox+1
    keytime=Tier
    If moveavail>0 Then moveavail=moveavail-1
   EndIf
   If KeyDown(73) And keytime<0 And moveavail>-1 Or KeyDown(24) And keytime<0 And moveavail>-1
    Repeat
    Until KeyDown(73)=0  And KeyDown(24)=0
    seloy=seloy-1
    keytime=Tier
    If moveavail>0 Then moveavail=moveavail-1
   EndIf
   If KeyDown(79) And keytime<0 And moveavail>-1 Or KeyDown(50) And keytime<0 And moveavail>-1
    Repeat
    Until KeyDown(79)=0  And KeyDown(50)=0
    seloy=seloy+1
    keytime=Timr
    If moveavail>0 Then moveavail=moveavail-1
   EndIf
   If KeyDown(71) And keytime<0 And moveavail>-1 Or KeyDown(22) And keytime<0 And moveavail>-1
    Repeat
    Until KeyDown(71)=0  And KeyDown(22)=0
    selox=selox-1
    keytime=Timr
    If moveavail>0 Then moveavail=moveavail-1
   EndIf
   If KeyDown(81) And keytime<0 And moveavail>-1 Or KeyDown(52) And keytime<0 And moveavail>-1
    Repeat
    Until KeyDown(81)=0  And KeyDown(52)=0
    selox=selox+1
    keytime=Timr
    If moveavail>0 Then moveavail=moveavail-1
   EndIf
  EndIf
	;make sure you havent gone off of the map
  If selox<0 Then selox=0
  If selox>Mapsize Then selox=Mapsize
  If seloy<0 Then seloy=0
  If seloy>Mapsize Then seloy=Mapsize
		;if flying creature make sure where you click there isnt an enemy creature
		;if there is then attack them and return player creature to starting point
		;unless they kill the enemy then they take thier place
		If CanFly=0 And playat(selox,seloy)<>-1 Then CheckAttack(selox,seloy,Creature)
		If moveavail=0 Or Endmove=1
	   ;check to see if movement points have run out (if so end move)
	   ;check wizards movement,unlike creatures wizards can be selected after movement has runout
	   ;(this allows further creatures to be cast from the wizard)
   If Creature=1
    sp=0
    If playat(selox,seloy)>-1 Or Below>-1 Then CheckAttack(selox,seloy,Creature)
    For play.player = Each player
     If play\x=ox And play\y=oy And sp=selo
      play\moveleft=moveavail
      If CanFly=1 And Below>-1
       If Riding=-1
        If playat(selox,seloy)>-1 Then playat(selox,seloy)=Below
        play\x=StartX
        play\y=StartY
        plx=StartX
        ply=StartY
       EndIf
      Else
       If playat(selox,seloy)=-1
        play\x=selox
        play\y=seloy
        plx=selox
        ply=seloy
       Else
        play\x=ox
        play\y=oy
        plx=px
        ply=oy
        playat(ox,oy)=1
       EndIf
      EndIf
      Below=-1
     EndIf
     sp=sp+1
    Next
    If playat(selox,seloy)>-1 Then CheckAttack(selox,seloy,1)
    moveavail=-1
    If shotsavail>0 Then shooting(0,selx,sely,0)
   EndIf
   ;check creatures movement
   ;TO MAKE SURE THERE IS NOTHING THERE ALREADY
   If Creature=2 Or Creature=3
    sp=0
    If CanFly=0 And playat(selox,seloy)>-1 Or CanFly=1 And Below>-1 Then CheckAttack(selox,seloy,Creature)
    For create.creations =Each creations
     If create\x=ox And create\y=oy And sp=selo
      create\moveleft=moveavail      
      If CanFly=1 And Below>-1
       playat(selox,seloy)=Below
       create\x=StartX
       create\y=StartY
       plx=StartX
       ply=StartY
       playat(create\x,create\y)=Creature
      Else
       If playat(selox,seloy)=-1
        create\x=selox
        create\y=seloy
        create\shotsleft=shotsavail
        plx=selox
        ply=seloy
       Else
        create\x=ox
        create\y=oy
        plx=ox
        ply=oy
        playat(ox,oy)=Creature
       EndIf
      EndIf
     EndIf
     sp=sp+1
    Next     
    If Creature=3
     For play.player = Each player
      If play\x=ox And play\y=oy
       play\x=plx
       play\y=ply
      EndIf
     Next
    EndIf
    moveavail=-1
    FlyRange=-1
    If shotsavail>0 Then shooting(0,selx,sely,0)
    If Creature=2 And shotsavail<1
     selo=-1
     Creature=0
     shotsavail=-1
    EndIf
   EndIf
  EndIf
  ;test to make sure Wizard casts spells ONLY
  If KeyDown(46) And keytime<0 And Creature<>2 And Creature>0 And castspell=-1 Then Cast(0)
  If KeyDown(32) And keytime<0 And Creature=3 Then Dismount(selox,seloy,1)
  Endmove=0
  If KeyDown(37) And keytime<0 And Creature>0
   ;if player CANCELS move then remove movement points
   firearrow=-1
   Endmove=1
  EndIf
  If KeyDown(37) And keytime<0 And castspell>-1
   cs=0
   If castno < startcastno
	If castinfo( 17 ) = 0
    	manaavail = manaavail - ( ( castinfo( 0 ) +1 ) /2 )
	Else
		manaavail = manaavail - castinfo( 0 )
	EndIf
    For play.player=Each player
     If cs=CurrentPlayer
      play\mana=manaavail
      play\align=play\align+castinfo(0)
      If play\align>5 Then play\align=5
      If play\align<-5 Then play\align=-5
     EndIf
     cs=cs+1
    Next
   EndIf
   castrange=-1
   castspell=-1
   castno=-1
   startcastno=-1
  EndIf
  If playat(selox,seloy)>-1
   ;check to see if anything is blocking players.
   ok=0
   If Creature=2 And CanFly=1 Or Creature=3 And CanFly=1 Then ok=1
   If ok=0
    If Riding=-1 And Creature=1 Or Creature>1
     selox=ox
     seloy=oy
    EndIf
   EndIf
  EndIf
  si=0
  If Creature=1
   For play.player = Each player
    If play\x=ox And play\y=oy And si=selo
     If moveavail>-1
      play\x=selox
      play\y=seloy
      Below=playat(selox,seloy)
     Else
      selox=ox
      seloy=oy
      Below=playat(selox,seloy)
     EndIf
    EndIf
    si=si+1
   Next
  EndIf
  If Creature=2 Or Creature=3
   For create.creations = Each creations
    If create\x=ox And create\y=oy And si=selo
      create\x=selox
      create\y=seloy
      create\shotsleft=shotsavail
      tpe=create\tp
      Below=playat(selox,seloy)
    EndIf
    si=si+1
   Next
   If Creature=3
    ;if player riding creature update movement
    For play.player = Each player
     If play\x=ox And play\y=oy And si=CurrentPlayer
      play\x=selox
      play\y=seloy
     EndIf
    Next
   EndIf
  EndIf
  playat(selox,seloy)=Piece
 EndIf
 If KeyDown(31)=1 Then DiskAccess(0)
 If KeyDown(2)=1 Then PlayerID=0
 If KeyDown(3)=1 Then PlayerID=1
 If KeyDown(4)=1 Then PlayerID=2
 If KeyDown(5)=1 Then PlayerID=3
 If KeyDown(6)=1 Then PlayerID=4
 If KeyDown(7)=1 Then PlayerID=5
 If KeyDown(8)=1 Then PlayerID=6
 If KeyDown(9)=1 Then PlayerID=7
 If KeyDown(49) And keytime<0 And Creature=0 
  NextUnit(Lastmove)
  keytime=Timr
 EndIf
 If KeyDown(18) 
  If keytime<0 And Creature=0 
   EndTurn(CurrentPlayer)
   PlayerID=-1
  EndIf
 EndIf
 If mousetime>0 Then mousetime=mousetime-1
 If keytime>=0 Then keytime=keytime-1
 If shotsavail<1 Then firearrow=-1
 If MouseDown(1)=1 And mousetime<=0
  Repeat
  Until MouseDown(1)=0
  If CanFly=1 And moveavail>0 And castspell=-1
   If selx>=StartX-FlyRange And selx<=StartX+FlyRange And sely>=StartY-FlyRange And sely<=StartY+FlyRange
    sp=0
    For create.creations=Each creations
     If sp=selo
      StartX=create\x
      StartY=create\y
      create\x=selx
       create\y=sely
      create\moveleft=0
     EndIf
     sp=sp+1
    Next
    If playat(selx,sely)>-1 Then CheckAttack(selx,sely,Creature)
    If playat(selx,sely)>-1
     sp=0
     For create.creations=Each creations
      If sp=selo
        create\x=StartX
      create\y=StartY
        moveavail=0
      EndIf
      sp=sp+1
     Next
    Else
     playat(StartX,StartY)=-1
     playat(selx,sely)=Creature
     plx=selx
     ply=sely
     cp=0
     If Creature=3
      For play.player=Each player
       If cp=CurrentPlayer
        play\x=selx
        play\y=sely
       EndIf
       cp=cp+1
      Next
     EndIf
     moveavail=-1
     EndMove=1
     FlyRange=-1
    EndIf
   EndIf
  EndIf
  If castspell=-1 And firearrow=-1
   ok=0
   If playat(selx,sely)=1
    sp=0
    selo=-1
    For play.player =Each player
     If play\x=selx And play\y=sely And sp=CurrentPlayer
      selo=sp
      selox=play\x
      seloy=play\y
      moveavail=play\moveleft
      shotsavail=play\shotsleft
      StartX=play\x
      StartY=play\y
      CanFly=play\Wings
      Creature=1
      Health=play\hp
      ok=1
     EndIf
     sp=sp+1
    Next  
   EndIf
   If playat(selx,sely)=2 Or playat(selx,sely)=3
    sp=0
    selo=-1    
    For create.creations =Each creations
     If create\x=selx And create\y=sely And create\owner=CurrentPlayer
      If playat(selx,sely)=3 Or create\moveleft>0 Or create\shotsleft>0
       selo=sp
       selox=create\x
       seloy=create\y
       StartX=create\x
       StartY=create\y
       CanFly=create\flying
       FlyRange=create\moveleft
       CanShoot=create\ranged
       shotsavail=create\shotsleft
       moveavail=create\moveleft
       Health=create\hp
       If CanFly=1 And moveavail>0 Then AddCom("Click mouse where you want to fly to!")
       CreateType=create\tp
       Creature=2     
       If playat(selx,sely)=3 Then Creature=3
      EndIf
      ok=1
     EndIf
     sp=sp+1
    Next  
   EndIf
   If ok=0
    CanFly=-1
    selo=-1
    plx=selox
    ply=seloy
    Creature=0
   EndIf
  Else
   If firearrow<>-1 Then castspell=-1
   If castspell<>-1 Then spellcasting(castspell)
   If firearrow<>-1 
    shottype=1 ;arrow
    If CreateType=30 Or CreateType=38 Or CreateType=33 Then shottype=1
    If CreateType>21 And CreateType<28 Then shottype=2
    shooting(1,selx,sely,shottype)
   EndIf
  EndIf
  mousetime=5
 EndIf
 ShowCom()
 If Creature>0 Then Text 0,360,"Health "+Health
 If selo>-1
  If moveavail>0 Then Text 0,380,"MOVES AVAILABLE "+moveavail Else Text 0,380,"NO MOVEMENT LEFT"
 EndIf
 fpstimer2#=MilliSecs()
 fps=(1.0/((fpstimer2-fpstimer1)/1000.0))
 Text 0,0,fps
 fpstimer1=fpstimer2
 If playat(selx,sely)=2 Or playat(selx,sely)=3
  sp=0
  cas=0
  For create.creations = Each creations
   If create\x=selx And create\y=sely
    cas=create\owner
   EndIf
   sp=sp+1
  Next
  cas=cas+1
 EndIf
 showmouse()
 For play.player=Each player
  If playat(play\x,play\y)=2 And play\hp>0 And Creature=0 Then playat(play\x,play\y)=1
 Next
 Flip
Wend
End

Function Anim(x,y,op)
 back=CreateImage(640,480) 
 GrabImage back,0,0
 If op=0
  ;death animations
  For da=1 To 20
   DrawImage back,0,0
   Color da+25,da*25,0
   Oval x-(da/2),y-(da/2),da,da
   Flip
  Next
 EndIf
 If op=1
  ;spellcasting anim
  cr=1
  For da=1 To 10
   DrawImage back,0,0
   For cnt=0 To 5
    Color Rnd(255),Rnd(255),Rnd(255)
    Oval (320+Rnd(16))-8,(210+Rnd(16)-8),3,3
   Next
   Flip
  Next
 EndIf
 If op=2
  ;Wizard death anim
  PlaySound Zap
  For an=0 To 50
   d=Rnd(65535)
   Color 0,0,0
   Rect 0,0,640,480
   DrawImage back,Rnd(20)-10,Rnd(20)-10
   If an=30 Then PlaySound Thunder
   Flip
  Next
 EndIf
 FreeImage back
End Function

Function GetSavegameNames()
 ;get names of savegames
 For n=0 To 9
  filen$="save"+n+".sav"
  file=OpenFile(filen$)
  If file>0
   ;if file exists get the save game name
   filename$(n)=ReadString$(file)
   CloseFile file 
  Else
   ;otherwise change save name to -Empty-
   filename$(n)="-Empty-"
  EndIf
 Next
End Function

Function InstructAim()
	Local Notif$ = ""
	
	Notif$ = Notif$ + "Aim of the game"+Chr$(10)
	Notif$ = Notif$ + ""+Chr$(10)
	Notif$ = Notif$ + "The aim of Wizard Wars is To simply kill all the other wizards,"+Chr$(10)
	Notif$ = Notif$ + "this is done by attacking them with spells or injuring them with"+ Chr$( 10 )
	Notif$ = Notif$ + "creatures created by you or subverted from others."+ Chr$( 10 )
	Notif$ = Notif$ + "You get 25 mana points per turn plus 5 points from each of your"+ Chr$( 10 )
	Notif$ = Notif$ + "creatures, this can be spent on more creatures or saved for the"+ Chr$( 10 )
	Notif$ = Notif$ + "more powerful and expensive spells."+ Chr$( 10 )
	Notif$ = Notif$ + "When one of the wizards is killed by any means all thier spells"+ Chr$( 10 )
	Notif$ = Notif$ + "are destroyed with them as are all thier creatures, this makes"+ Chr$( 10 )
	Notif$ = Notif$ + "it more important to keep your wizard safe from harm as they"+ Chr$( 10 )
	Notif$ = Notif$ + "hold the key to your success."+ Chr$( 10 )
	Notif$ = Notif$ + "Flying creatures or wizards with Magic Wings may fly over other"+ Chr$( 10 )
	Notif$ = Notif$ + "creatures to attack others behind the enemy lines or avoid"+ Chr$( 10 )
	Notif$ = Notif$ + "them altogether, others may be ridden by your wizard into the"+ Chr$( 10 )
	Notif$ = Notif$ + "fray. When riding a creature the creature has to be killed off"+ Chr$( 10 )
	Notif$ = Notif$ + "Before the wizard can be injured at all, this gives a safe way"+Chr$(10)
	Notif$ = Notif$ + "to move around the map unhindered and relatively safe!"+Chr$(10)
	Notify Notif$
	
End Function

Function InstructWizardControl()
	Local Notif$=""

	Notif$ = Notif$ + "Controlling your Wizard"+Chr$(10)
	Notif$ = Notif$ + "Controlling your wizard is simple firstly you will need to click"+Chr$(10)
	Notif$ = Notif$ + "on them with the left mouse button this should bring up the text"+Chr$(10)
	Notif$ = Notif$ + "'Wizard Selected' at the top center of the screen."+Chr$(10)
	Notif$ = Notif$ + "After that you can use Keypad to move around and attack other"+Chr$(10)
	Notif$ = Notif$ + "players or creatures, to de-select you wizard just click on any"+Chr$(10)
	Notif$ = Notif$ + "empty square on the battlefield."+Chr$(10)
	Notif$ = Notif$ + "To get your wizard to ride a creature make sure they are near"+Chr$(10)
	Notif$ = Notif$ + "the creature and then walk into it (rather like attacking it)"+Chr$(10)
	Notif$ = Notif$ + "this should bring up the 'Do you want to ride on the creature'"+Chr$(10)
	Notif$ = Notif$ + "requester. To get off of the creature just press the 'D' key."+Chr$(10)
	Notif$ = Notif$ + "If you want your wizard to attack something that is an Undead"+Chr$(10)
	Notif$ = Notif$ + "creature then you will need to use either spells or equip your"+Chr$(10)
	Notif$ = Notif$ + "wizard with a 'Magic Sword' or 'Magic Bow'!"+Chr$(10)
	Notif$ = Notif$ + "The small box in the bottom left of the screen is your wizard"+Chr$(10)
	Notif$ = Notif$ + "alignment box, this shows what alignment your wizard is (either"+Chr$(10)
	Notif$ = Notif$ + "Law 'arrows' or Chaos 'stars') and the mana you currently have."+Chr$(10)
	Notify Notif$
End Function

Function InstructControls()
	Local Notif$=""

	Notif$ = Notif$ + "Controls"+Chr$(10)
	Notif$ = Notif$ + ""+Chr$(10)
	Notif$ = Notif$ + "The controls are:"+Chr$(10)
	Notif$ = Notif$ + "Keypad - scroll map or move wizards/creatures"+Chr$(10)
	Notif$ = Notif$ + "U,I,O,J,L,M,<,> - alternative Laptop control system"+Chr$(10)
	Notif$ = Notif$ + "C - bring up spells list (wizard only)"+Chr$(10)
	Notif$ = Notif$ + "K - cancel the spell or fire mode"+Chr$(10)
	Notif$ = Notif$ + "D - dismount from a creature (wizard only)"+Chr$(10)
	Notif$ = Notif$ + "S - save the current game"+Chr$(10)
	Notif$ = Notif$ + "1-8 - highlight selected wizards creatures on battlefield."+Chr$(10)
	Notif$ = Notif$ + "N - next creature (if no movement left selects wizard)"+Chr$(10)
	Notif$ = Notif$ + "E - End turn (make sure you want to, you cant go back)"+Chr$(10)
	Notif$ = Notif$ + "PLEASE NOTE"+Chr$(10)
	Notif$ = Notif$ + "There are other keys used throughout the game to perform some"+Chr$(10)
	Notif$ = Notif$ + "of the other tasks, normally answering questions 'Y' and 'N'."+Chr$(10)
	Notif$ = Notif$ + "You can also use 'Esc' to quit the game (back to Blitz Basic)."+Chr$(10)

	Notify Notif$
End Function

Function InstructCasting()
	Local Notif$=""

	Notif$ = Notif$ + "Casting Spells"+Chr$(10)
	Notif$ = Notif$ + "To cast spells you will need to select your wizard or creature"+Chr$(10)
	Notif$ = Notif$ + "if they are riding on one, then press the 'C' key to bring up"+Chr$(10)
	Notif$ = Notif$ + "the spells list."+Chr$(10)
	Notif$ = Notif$ + "Use the mouse to select a spell from the list and then select"+Chr$(10)
	Notif$ = Notif$ + "if you want it to be Real or an Illusion and then click on"+Chr$(10)
	Notif$ = Notif$ + "Cast to fire up the spell."+Chr$(10)
	Notif$ = Notif$ + "The difference between Real and Illusion creatures is that the"+Chr$(10)
	Notif$ = Notif$ + "illusions can be Dispelled (this destroys them), however the"+Chr$(10)
	Notif$ = Notif$ + "bonus is that they cost half as much as normal creatures so its"+Chr$(10)
	Notif$ = Notif$ + "for you to decide if you want to risk it."+Chr$(10)
	Notif$ = Notif$ + "You can see more spells in the list by clicking on the '+' and"+Chr$(10)
	Notif$ = Notif$ + "'-' buttons on the right side of the list. The '*','-' and '^'"+Chr$(10)
	Notif$ = Notif$ + "are the alignment of the spell (this adds to your alignment)!"+Chr$(10)
	Notif$ = Notif$ + "To cast the spell move the cursor around till the selector is"+Chr$(10)
	Notif$ = Notif$ + "green and then click the mouse, note some spells have multiple"+Chr$(10)
	Notif$ = Notif$ + "casts so you may be able to do this more than once."+Chr$(10)
	Notify Notif$
End Function

Function InstructCombat()
	Local Notif$=""

	Notif$ = Notif$ + "Combat"+Chr$(10)
	Notif$ = Notif$ + "Combat is rather simple in Wizard Wars because each creature"+Chr$(10)
	Notif$ = Notif$ + "has an attack rating, this reflects how dangerous they are in"+Chr$(10)
	Notif$ = Notif$ + "combat."+Chr$(10)
	Notif$ = Notif$ + "To initiate combat just walk into or end you move over an"+Chr$(10)
	Notif$ = Notif$ + "enemy creature, this will start the attack (signified by the"+Chr$(10)
	Notif$ = Notif$ + "poor target yelling in pain)."+Chr$(10)
	Notif$ = Notif$ + "If you manage to kill the creature the attack then takes thier"+Chr$(10)
	Notif$ = Notif$ + "place on the battlefield and if they have any further movement"+Chr$(10)
	Notif$ = Notif$ + "they may continue attacking until this has run out or you wish"+Chr$(10)
	Notif$ = Notif$ + "to control something else or end your turn."+Chr$(10)
	Notif$ = Notif$ + "Flying creatures have to end there move over the enemy creature"+Chr$(10)
	Notif$ = Notif$ + "to attack it, after this if the creature is still alive then"+Chr$(10)
	Notif$ = Notif$ + "the attacker is returned to thier starting position."+Chr$(10)
	Notif$ = Notif$ + "Undead creatures can only be attacked with undead creatures,"+Chr$(10)
	Notif$ = Notif$ + "magical weapons or spells. Undead creatures however can attack"+Chr$(10)
	Notif$ = Notif$ + "any type of creatures."+Chr$(10)

	Notify notif$
End Function

Function InstructRanged()
	Local Notif$=""
	Notif$ = Notif$ + "Ranged Attacks"+Chr$(10)
	Notif$ = Notif$ + "Some creatures have a ranged attack that they can use after"+Chr$(10)
	Notif$ = Notif$ + "they have finished thier move, this may be magical fire or"+Chr$(10)
	Notif$ = Notif$ + "some arrows."+Chr$(10)
	Notif$ = Notif$ + "To fire your selected attack use the mouse to select somewhere"+Chr$(10)
	Notif$ = Notif$ + "on the screen and the click the mouse button, you will then"+Chr$(10)
	Notif$ = Notif$ + "see the attack take place."+Chr$(10)
	Notif$ = Notif$ + "As with close combat only a 'Magic Bow' may attack the Undead"+Chr$(10)
	Notif$ = Notif$ + "creatures. You may however fire over other creatures that are"+Chr$(10)
	Notif$ = Notif$ + "in your way to go for another creature that is behind them,"+Chr$(10)
	Notif$ = Notif$ + "this stops wizards hiding behind a line of Bats."+Chr$(10)
	Notif$ = Notif$ + "Some magical ranged attacks can only be used on certain types"+Chr$(10)
	Notif$ = Notif$ + "of creatures (Dispell can only be used on illusions For example)"+Chr$(10)
	Notif$ = Notif$ + "these will have no effect on anything else!"+Chr$(10)
	Notif$ = Notif$ + "Ranged attacks can be fired at any square on the screen giving"+Chr$(10)
	Notif$ = Notif$ + "creatures a rather good advantage, this also means that other"+Chr$(10)
	Notif$ = Notif$ + "creatures have the same range as you so be careful."+Chr$(10)
	Notify Notif$
End Function

Function InstructRequirements()
	Local Notif$=""
	
	Notif$ = Notif$ + "Requirements"+Chr$(10)
	Notif$ = Notif$ + "The minimum spec machines we could test it on is as follows:"+Chr$(10)
	Notif$ = Notif$ + "Intel 233MMX"+Chr$(10)
	Notif$ = Notif$ + "64Mb Ram (also 32Mb)"+Chr$(10)
	Notif$ = Notif$ + "2Mb Harddisk space"+Chr$(10)
	Notif$ = Notif$ + "Voodoo 2 and S3 Virge video cards"+Chr$(10)
	Notif$ = Notif$ + "IMPORTANT PLEASE READ: Some of the larger maps will require"+Chr$(10)
	Notif$ = Notif$ + "more memory, for the small maps 32Mb should suffice but for"+Chr$(10)
	Notif$ = Notif$ + "medium to large maps I would recommend 64Mb-128Mb of ram."+Chr$(10)
	Notif$ = Notif$ + "We have had reports of unexpected result from running the game"+Chr$(10)
	Notif$ = Notif$ + "with insufficient memory."+Chr$(10)
	Notify Notif$
End Function

Function InstructTips()
	Local Notif$=""

	Notif$ = Notif$ + "Helpful Tips"+Chr$(10)
	Notif$ = Notif$ + "At the beginning of the game Bats will give more mana"+Chr$(10)
	Notif$ = Notif$ + "if you cast several each turn, this will enable your wizard to"+Chr$(10)
	Notif$ = Notif$ + "gain access to all those powerful spells earlier."+Chr$(10)
	Notif$ = Notif$ + "Riding a creature not only saves your wizards feet but allows"+Chr$(10)
	Notif$ = Notif$ + "him to get around faster and safer, the creature has to die"+Chr$(10)
	Notif$ = Notif$ + "before they can even injure your wizard."+Chr$(10)
	Notif$ = Notif$ + "Illusions still generate mana but cost half as much so if kept"+Chr$(10)
	Notif$ = Notif$ + "safe could be a rather lucrative mana supply, also if there is"+Chr$(10)
	Notif$ = Notif$ + "no wizards in the local vicinity they also provide adequate"+Chr$(10)
	Notif$ = Notif$ + "defence."+Chr$(10)
	Notif$ = Notif$ + "Magic Castles and Dark Citadels add to your alignment this in"+Chr$(10)
	Notif$ = Notif$ + "turn gives your creatures more hitpoints depending on the value"+Chr$(10)
	Notif$ = Notif$ + "of the alignment you have!"+Chr$(10)
	Notif$ = Notif$ + "Vortex's change creatures into other creatures this can be good"+Chr$(10)
	Notif$ = Notif$ + "if a Bat is changed into a Dragon but not very nice the other"+Chr$(10)
	Notif$ = Notif$ + "way round."+Chr$(10)
	
	Notify Notif$
End Function

Function Instructions()
	;create all the required buttons
	Local IWindow = CreateWindow( "Instructions", ( ClientWidth( Desktop() )/2 )-105, ( ClientHeight( Desktop() )/2 )-230, 210, 460 )
	Local AimButton = CreateButton( "Aim of the game", 0, 0, 200, 40, IWindow )
	Local ControlButton = CreateButton( "Controlling your wizard", 0, 40, 200, 40, IWindow )
	Local CastingButton = CreateButton( "Casting Spells", 0, 80, 200, 40, IWindow )
	Local ControlsButton = CreateButton( "Controls", 0, 120, 200, 40, IWindow )
	Local CombatButton = CreateButton( "Combat", 0, 160, 200, 40, IWindow )
	Local RangedButton = CreateButton( "Ranged attacks", 0, 200, 200, 40, IWindow )
	Local RequirementsButton = CreateButton( "SystemRequirements", 0, 240, 200, 40, IWindow )
	Local HintsButton = CreateButton( "Helpful hints", 0, 280, 200, 40, IWindow )

	Local BackButton = CreateButton( "Back", 0, 350, 200, 40, IWindow )
	Local ok=1

	While ok=1
		e = WaitEvent()
		Select e
		Case $803: ok=1
		End Select
		
		Select EventID()	;see what gadget has been clicked on
		Case $401 : Select EventSource()	;$401 a button has been clicked, check which one
					Case AimButton   : InstructAim()
					Case ControlButton: InstructWizardControl()
					Case CastingButton:InstructCasting()
					Case ControlsButton: InstructControls()
					Case CombatButton: InstructCombat()
					Case RangedButton: InstructRanged()
					Case RequirmentsButton: InstructRequirements()
					Case HintsButton: InstructTips()
					Case BackButton: ok=0
					End Select
		End Select
	Wend

	;a little house cleaning to make sure the memory is released back to blitz for later use
	;this just frees all the local gadgets that were created at the beginning of this function
	FreeGadget AimButton
	FreeGadget ControlButton
	FreeGadget CastingButton
	FreeGadget ControlsButton
	FreeGadget CombatButton
	FreeGadget RangedButton
	FreeGadget RequirementsButton
	FreeGadget HintsButton
	FreeGadget BackButton
	FreeGadget Iwindow

End Function

;Load and Save game functions
Function DiskAccess(op)
 ;get names of other savegames
 For n=0 To 9
  filen$="save"+n+".sav"
  file=OpenFile(filen$)
  If file>0
   filename$(n)=ReadString$(file)
   CloseFile file 
  EndIf
 Next
 mtime=0
 seed=Rnd(65500)
 showmap(Mapsize/2,Mapsize/2,0,0,1)
 back = CreateImage(640,480)
 GrabImage back,0,0
 Repeat
  ok=0
  DrawImage back,0,0
  Color 25,25,25
  Box(50,50,589,439,1)
  Color 255,255,255
  If op=0 Then Text 320,70,"SAVE GAME",1,1 Else Text 320,70,"LOAD GAME",1,1
  For n=0 To 9
   Text 320,(n*30)+100,filename$(n),1
  Next
  Text 320,414,"Cancel",1
  showmouse()
  mx=MouseX()
  my=MouseY()
  Color 255,255,0
  If mx>50 And mx<589
   If my>100 And my<116
    Text 320,100,filename$(0),1
    If MouseDown(1)=1 Then ok=1
   EndIf
   If my>130 And my<146
    Text 320,130,filename$(1),1
    If MouseDown(1)=1 Then ok=2
   EndIf
   If my>160 And my<176
    Text 320,160,filename$(2),1
    If MouseDown(1)=1 Then ok=3
   EndIf
   If my>190 And my<206
    Text 320,190,filename$(3),1
    If MouseDown(1)=1 Then ok=4
   EndIf
   If my>220 And my<236
    Text 320,220,filename$(4),1
    If MouseDown(1)=1 Then ok=5
   EndIf
   If my>250 And my<266
    Text 320,250,filename$(5),1
    If MouseDown(1)=1 Then ok=6
   EndIf
   If my>280 And my<296
    Text 320,280,filename$(6),1
    If MouseDown(1)=1 Then ok=7
   EndIf
   If my>310 And my<326
    Text 320,310,filename$(7),1
    If MouseDown(1)=1 Then ok=8
   EndIf
   If my>340 And my<356
    Text 320,340,filename$(8),1
    If MouseDown(1)=1 Then ok=9
   EndIf
   If my>370 And my<386
    Text 320,370,filename$(9),1
    If MouseDown(1)=1 Then ok=10
   EndIf
   If my>414 And my<430
    Text 320,414,"Cancel",1
    If MouseDown(1)=1 Then ok=-1
   EndIf
  EndIf
  Repeat
  Until MouseDown(1)=0
  Flip
  If op=1 And ok>0 
   If filename$(ok-1)="-Empty-" Then ok=0
  EndIf
 Until ok<>0 Or KeyDown(1)=1

 If ok<>-1 
  If op=0
   filen$="save"+(ok-1)+".sav"
   SaveTitle$=CurrentTime$()+" "+CurrentDate$()+" Players "+Nop
   file=WriteFile(filen$)
   If file>0
    WriteString file,SaveTitle$
    WriteInt file,Mapsize
    WriteInt file,selox
    WriteInt file,seloy
    WriteInt file,plx
    WriteInt file,ply
    WriteInt file,manaavail
    For yy=0 To Mapsize
     For xx=0 To Mapsize
      WriteInt file,map(xx,yy)
      WriteInt file,xoff(xx,yy)
      WriteInt file,yoff(xx,yy)
      WriteInt file,height(xx,yy)
      WriteInt file,mon(xx,yy)  
      WriteInt file,playat(xx,yy)
      WriteInt file,dead(xx,yy)
     Next
    Next
    ;count the number of players (to make sure we dont load in to many and miff the data)
    count=0
    For play.player = Each player
     count=count+1
    Next
    WriteInt file,count
    WriteInt file,Nop
    WriteInt file,CurrentPlayer
    For play.player = Each player
     WriteInt file,play\x
     WriteInt file,play\y
     WriteInt file,play\hp
     WriteInt file,play\Bow
     WriteInt file,play\Armour
     WriteInt file,play\Sword
     WriteInt file,play\Shield
     WriteInt file,play\tp
     WriteInt file,play\beattime
     WriteInt file,play\wp
     WriteInt file,play\xoff
     WriteInt file,play\yoff
     WriteInt file,play\st
     WriteInt file,play\dam
     WriteInt file,play\Wings
     WriteInt file,play\ShadowForm
     WriteInt file,play\mana
     WriteInt file,play\moveleft
     WriteInt file,play\maxmove
     WriteInt file,play\shotsleft
     WriteInt file,play\maxshots
    Next
    count=0
    For create.creations = Each creations
     count=count+1
    Next
    WriteInt file,count
    For create.creations = Each creations
     WriteInt file,create\x
     WriteInt file,create\y
     WriteInt file,create\owner
     WriteInt file,create\real
     WriteInt file,create\tp
     WriteInt file,create\mp
     WriteInt file,create\hp
     WriteInt file,create\range
     WriteInt file,create\wp
     WriteInt file,create\shotsleft
     WriteInt file,create\maxshots
     WriteInt file,create\undead
     WriteInt file,create\ranged
     WriteInt file,create\flying
     WriteInt file,create\mount
     WriteInt file,create\magical
     WriteInt file,create\moveleft
     WriteInt file,create\maxmove
     WriteInt file,create\damage
    Next
    count=0
    For civl.civilian = Each civilian
     count=count+1
    Next
    WriteInt file,count
    For civl.civilian = Each civilian
     WriteInt file,civl\x
     WriteInt file,civl\y
     WriteInt file,civl\face
     WriteInt file,civl\sex
     WriteInt file,civl\tp
     WriteInt file,civl\hp
     WriteInt file,civl\xoff
     WriteInt file,civl\yoff
     WriteInt file,civl\st
     WriteInt file,civl\movetime
    Next
    CloseFile(file)
    AddCom("Game saved")
   EndIf
  Else
   ;free up arrays to stop to many characters on the map at once
   For play.player=Each player
    Delete play
   Next
   For create.creations=Each creations
    Delete create
   Next
   For civl.civilian=Each civilian
    Delete civl
   Next
   filen$="save"+(ok-1)+".sav"
   file=ReadFile(filen$)
   If file>0
    SaveTitle$=ReadString(file)
    Mapsize=ReadInt(file)
    selo=-1
    selox=ReadInt(file)
    seloy=ReadInt(file)
    plx=ReadInt(file)
    ply=ReadInt(file)
    manaavail=ReadInt(file)
    For yy=0 To Mapsize
     For xx=0 To Mapsize
      map(xx,yy)=ReadInt(file)
      xoff(xx,yy)=ReadInt(file)
      yoff(xx,yy)=ReadInt(file)
      height(xx,yy)=ReadInt(file)
      mon(xx,yy)=ReadInt(file)  
      playat(xx,yy)=ReadInt(file)
      dead(xx,yy)=ReadInt(file)
     Next
    Next
    ;count the number of players (to make sure we dont load in to many and miff the data)
    count=ReadInt(file)
    Nop=ReadInt(file)
    CurrentPlayer=ReadInt(file)
    For n=0 To count-1
     play.player = New player
     play\x=ReadInt(file)
     play\y=ReadInt(file)
     play\hp=ReadInt(file)
     play\Bow=ReadInt(file)
     play\Armour=ReadInt(file)
     play\Sword=ReadInt(file)
     play\Shield=ReadInt(file)
     play\tp=ReadInt(file)
     play\beattime=ReadInt(file)
     play\wp=ReadInt(file)
     play\xoff=ReadInt(file)
     play\yoff=ReadInt(file)
     play\st=ReadInt(file)
     play\dam=ReadInt(file)
     play\Wings=ReadInt(file)
     play\ShadowForm=ReadInt(file)
     play\mana=ReadInt(file)
     play\moveleft=ReadInt(file)
     play\maxmove=ReadInt(file)
     play\shotsleft=ReadInt(file)
     play\maxshots=ReadInt(file)
    Next
    count=ReadInt(file)
    For n=0 To count-1
     create.creations = New creations
     create\x=ReadInt(file)
     create\y=ReadInt(file)
     create\owner=ReadInt(file)
     create\real=ReadInt(file)
     create\tp=ReadInt(file)
     create\mp=ReadInt(file)
     create\hp=ReadInt(file)
     create\range=ReadInt(file)
     create\wp=ReadInt(file)
     create\shotsleft=ReadInt(file)
     create\maxshots=ReadInt(file)
     create\undead=ReadInt(file)
     create\ranged=ReadInt(file)
     create\flying=ReadInt(file)
     create\mount=ReadInt(file)
     create\magical=ReadInt(file)
     create\moveleft=ReadInt(file)
     create\maxmove=ReadInt(file)
     create\damage=ReadInt(file)
    Next
    count=ReadInt(file)
    For n=0 To count-1
     civl.civilian = New civilian
     civl\x=ReadInt(file)
     civl\y=ReadInt(file)
     civl\face=ReadInt(file)
     civl\sex=ReadInt(file)
     civl\tp=ReadInt(file)
     civl\hp=ReadInt(file)
     civl\xoff=ReadInt(file)
     civl\yoff=ReadInt(file)
     civl\st=ReadInt(file)
     civl\movetime=ReadInt(file)
    Next
    CloseFile(file)
   EndIf
  EndIf
 Else 
  FreeImage back
  Titlesequence()
 EndIf
 FreeImage back
End Function

Function AddCom(mes$)
 For n=1 To 5
  com$(n-1)=com$(n)
 Next
 com$(5)=mes$
End Function

Function ShowCom()
 For n=0 To 5
  Color 0,0,0
  Text 361,391+(n*16),com$(n),1,1
  Color 255,255,255
  Text 360,390+(n*16),com$(n),1,1
 Next
 comtimer=comtimer-1
 If comtimer<0
  For n=1 To 5
   com$(n-1)=com$(n)
  Next
  com$(5)=""
  comtimer=50
 EndIf
End Function

Function NextUnit(current)
 cfound=-1
 si=0
 For create.creations=Each creations
  If si>current
   If create\owner=CurrentPlayer And create\moveleft>0 And si>current And cfound=-1
    cfound=si
    plx=create\x
    ply=create\y
    selox=create\x
    seloy=create\y
    selo=si
   EndIf
  EndIf
  si=si+1
 Next
 If cfound=-1
  cfound=-1
  si=0
  For create.creations=Each creations
   If create\owner=CurrentPlayer And create\moveleft>0 And cfound=-1
    cfound=si
    plx=create\x
    ply=create\y
    selox=create\x
    seloy=create\y
    selo=si
   EndIf
   si=si+1
  Next
  If cfound=-1
   si=0
   For play.player=Each player
    If si=CurrentPlayer
     plx=play\x
     ply=play\y
     selox=play\x
     seloy=play\y
    EndIf
    si=si+1
   Next
  EndIf
 EndIf
 If cfound<>-1 Then Lastmove=cfound
End Function

Function Dismount(x,y,op)
;function so wizards get off of creatures
 sp=0
 cas=0
 For create.creations = Each creations
  If create\x=x And create\y=y And sp=selo
   cas=create\owner
  EndIf
 Next
 If op=0
  playat(x,y)=1
  si=0
  For play.player=Each player
   If si=cas
    play\x=x
    play\y=y
   EndIf
   si=si+1
  Next
 Else 
  count=0
  Repeat
   nx=x+Rnd(0,1)
   nx=nx-Rnd(0,1)
   ny=y+Rnd(0,1)
   ny=ny-Rnd(0,1)
   If nx<0 Then nx=0
   If ny<0 Then ny=0
   If nx>Mapsize Then nx=Mapsize
   If ny>Mapsize Then ny=Mapsize
   count=count+1
  Until playat(nx,ny)=-1 Or count>50
  If count<50
   si=0
   For play.player=Each player
    If si=CurrentPlayer
     play\x=nx
     play\y=ny
     playat(nx,ny)=1
     playat(x,y)=2
    EndIf
    si=si+1
   Next
   Creature=0
   selo=-1
   moveavail=-1
  EndIf
 EndIf
End Function

;move civilians at the end of the last players turn
Function NPCmove()
 For cn=0 To 50
  movecivs()
 Next
End Function

;test to see if attack possible
Function CheckAttack(x,y,tpe)
 atud=0
 atd=10
 If tpe=1
  si=0
  For play.player=Each player
   If si=CurrentPlayer
    If play\Sword=1
     atd=25
     atud=1
    EndIf
    If play\Bow=1
     atd=15
     atud=1
    EndIf
   EndIf
  Next
  caster=CurrentPlayer
  sp=0
  ok=1
  found=-1
  ;find the poor victim
  If playat(x,y)=2 Or playat(x,y)=3
   For create.creations = Each creations
    If create\x=x And create\y=y
     If create\owner<>caster
      If create\undead=1 And atud=1 Or create\undead=0
       create\hp=create\hp-atd
       found=sp
       a=Rnd(3)
       If a=0 Then PlaySound Hit1
       If a=1 Then PlaySound Hit2
       If a=2 Then PlaySound Hit3
       If CanFly=1
        playat(x,y)=Below
       EndIf
       If create\hp<=0 
        ;add creature death anim here
        Anim(320,240,0)
        If playat(create\x,create\y)=2 Then playat(create\x,create\y)=-1 Else Dismount(create\x,create\y,0)
        If map(create\x,create\y)>2 And map(create\x,create\y<8) And create\real=1
         map(create\x,create\y)=30+create\tp
         dead(create\x,create\y)=create\tp+1
        EndIf
        Delete create
       EndIf
      Else
       AddCom("Undead cannot be attacked without magic")
      EndIf
      ok=0
     Else
      ok=1
     EndIf
    EndIf
    sp=sp+1
   Next
  EndIf
  If found=-1
   sp=0
   For play.player = Each player
    If play\x=x And play\y=y And sp<>CurrentPlayer
     found=sp
     If play\Shield=1 Then atd=atd-2
     If play\Armour=1 Then atd=atd-6
     If atd<0 Then atd=0
     If atd>0
      play\hp=play\hp-atd
      a=Rnd(3)
      If a=0 Then PlaySound Hit1
      If a=1 Then PlaySound Hit2
      If a=2 Then PlaySound Hit3
     EndIf
     If CanFly=1
      playat(x,y)=Below
     EndIf
     ok=0
     If play\hp<=0
      ;player death anim goes here
      Anim(320,240,2)
      AddCom("You have killed an enemy wizard")
      PlayerDeath(sp)
      playat(play\x,play\y)=-1
     EndIf
    EndIf
    sp=sp+1
   Next
  EndIf
  If ok=1
   ;if attacking friendly then STOP
   back=CreateImage(640,480)
   GrabImage back,0,0
   sp=0
   For create.creations = Each creations
    If create\x=x And create\y=y And create\owner=CurrentPlayer And create\mount=1
     Repeat
      DrawImage back,0,0
      Color 0,0,0
      Text 321,241,"Do you want to ride on this creature?",1,1
      Color 255,255,255
      Text 320,240,"Do you want to ride on this creature?",1,1
      Flip
     Until KeyDown(21)=1 Or KeyDown(49)=1
     ;if you press Y then set riding to creature ID
     If KeyDown(21)=1 Then Riding=sp
     If KeyDown(49)=1 Then Riding=-1
    EndIf
    sp=sp+1
   Next
   sp=0
   For play.player=Each player
    If sp=CurrentPlayer
     If CanFly=1 And Riding=-1 And playat(play\x,play\y)=1
      play\x=StartX
      play\y=StartY
      play\moveleft=play\maxmove
     Else
      play\x=x
      play\y=y 
      Piece=3
      playat(x,y)=3
      play\moveleft=0
     EndIf
    EndIf
    sp=sp+1
   Next
   FreeImage back
  EndIf
 EndIf
 If tpe=2 Or tpe=3
  sp=0
  ;get info from attacker
  For create.creations = Each creations
   If sp=selo
    hitvalue=create\damage
    caster=create\owner
    If create\undead=1 Then atud=1 Else atud=0
   EndIf
   sp=sp+1
  Next
  sp=0
  ok=1
  found=-1
  ;find the poor victim
  If playat(x,y)=2 Or playat(x,y)=3
   For create.creations = Each creations
    If create\x=x And create\y=y And sp<>selo
     If create\owner<>caster
      If create\undead=1 And atud=1 Or create\undead=0
       create\hp=create\hp-hitvalue 
       a=Rnd(3)
       If a=0 Then PlaySound Hit1
       If a=1 Then PlaySound Hit2
       If a=2 Then PlaySound Hit3
       If CanFly=1
        playat(x,y)=Below
       EndIf
       found=sp
       If create\hp<=0 
        ;add creature death anim here
        Anim(320,240,0)
        If playat(create\x,create\y)=2 
         playat(create\x,create\y)=-1 
        Else
         Dismount(create\x,create\y,0)
        EndIf
        If map(create\x,create\y)>2 And map(create\x,create\y<8) And create\real=1
         map(create\x,create\y)=30+create\tp
         dead(create\x,create\y)=create\tp+1
        EndIf
        If map(create\x,create\y)>2 And map(create\x,create\y<8) Then map(create\x,create\y)=30+create\tp
        Delete create
       EndIf
      Else
       AddCom("Undead cannot be attacked by this creature")
      EndIf
      ok=0
     Else
      ok=1
     EndIf
    EndIf
    sp=sp+1 
   Next
  EndIf
  ;IF NO CREATURES FOUND CHECK FOR WIZARDS
  If found=-1
   sp=0
   For play.player = Each player
    If play\x=x And play\y=y And sp<>selo And sp<>CurrentPlayer
     found=sp
     If play\Shield=1 Then hitvalue=hitvalue-2
     If play\Armour=1 Then hitvalue=hitvalue-6
     If hitvalue<0 Then hitvalue=0
     If hitvalue>0
      play\hp=play\hp-hitvalue
;      If CanFly=1
;       playat(x,y)=Below
;      EndIf
      a=Rnd(3)
      If a=0 Then PlaySound Hit1
      If a=1 Then PlaySound Hit2
      If a=2 Then PlaySound Hit3
     EndIf
     ok=0
     If play\hp<=0
      AddCom("You have killed an enemy wizard")
      ;player death anim goes here
      PlayerDeath(sp)
      playat(play\x,play\y)=-1
     EndIf
    EndIf
    sp=sp+1
   Next
  EndIf
  If ok=1
   ;if attacking friendly then STOP
   AddCom("The creature refuses to attack")
   sp=0
   For create.creations = Each creations
    If create\x=x And create\y=y And sp=selo
	    If CanFly=0 
         create\moveleft=create\moveleft+1
	    Else
	     create\x=StartX
	     create\y=StartY
	     create\moveleft=create\maxmove
	    EndIf
	EndIf
    sp=sp+1
   Next
  EndIf
 EndIf
End Function

Function shooting(op,x,y,firetype)
 ;arrow firing from Elves etc
 If op=0
  AddCom("Pick a target to shoot at, or press K to cancel")
;  AddCom("Select location to fire at or press K to cancel")
  firearrow=1
 Else
  If firetype=1
   Color 255,255,255
   Line 320,240,MouseX(),MouseY()
   PlaySound Shoot
   If playat(selx,sely)=1
    n=0
    For play.player=Each player
     If play\x=selx And play\y=sely And n<>CurrentPlayer
      play\hp=play\hp-15
      a=Rnd(3)
      If a=0 Then PlaySound Hit1
      If a=1 Then PlaySound Hit2
      If a=2 Then PlaySound Hit3
      If play\hp<=0
       playat(play\x,play\y)=-1
       PlayerDeath(si)
      EndIf
     EndIf
     n=n+1
    Next
   Else
    For create.creations=Each creations
     If create\x=selx And create\y=sely And create\owner<>CurrentPlayer
      create\hp=create\hp-15
      a=Rnd(3)
      If a=0 Then PlaySound Hit1
      If a=1 Then PlaySound Hit2
      If a=2 Then PlaySound Hit3
      If create\hp<0
       If playat(selx,sely)=2 Then playat(selx,sely)=-1 Else Dismount(create\x,create\y,0)
       If map(create\x,create\y)>2 And map(create\x,create\y<8) Then map(create\x,create\y)=30+create\tp
      EndIf
     EndIf
    Next
   EndIf
  Else
   Bolt(20,playat(selx,sely),0)
  EndIf
  firearrow=-1
  shotsavail=shotsavail-1
 EndIf
End Function

;bolt type spells
Function Bolt(damage,enemy,spl)
 ;Wizard who's casting (seeing as you have to be selected to cast this should be correct)
 sx=320
 sy=240

 ;target (who should be under the mouse cursor to cast at them ;) )
 ex=MouseX()
 ey=MouseY()

 ;find out where target is relative to wizard.
 If sx<ex Then dx=1 Else
 If sx>ex Then dx=-1 Else
 If sx=ex Then dx=0
 If sy<ey Then dy=1 Else
 If sy>ey Then dy=-1 Else
 If sy=ey Then dy=0
 xdist#=0
 ydist#=0
 If sx<ex Then xdist#=ex-sx Else
 If sx>ex Then xdist#=sx-ex Else
 If sx=ex Then xdist#=0
 If sy<ey Then ydist#=ey-sy Else
 If sy>ey Then ydist#=sy-ey Else
 If sy=ey Then ydist#=0
 If xdist#>0 Then xdist#=xdist#/15
 If ydist#>0 Then ydist#=ydist#/15

 csx#=sx
 csy#=sy
 ;grab screen For background image
 back = CreateImage(640,480)
 GrabImage back,0,0
 Timr=0
 Repeat
  DrawImage back,0,0 
  DrawImage MagicBolt,csx#,csy#
  Flip
  If dx=1 Then csx#=csx#+xdist#
  If dx=-1 Then csx#=csx#-xdist#
  If dy=1 Then csy#=csy#+ydist#
  If dy=-1 Then csy#=csy#-ydist#
  Timr=Timr+1
 Until KeyDown(1)=1 Or Timr>=15
 If enemy=1
  si=0
  For play.player=Each player
   If play\x=selx And play\y=sely And si<>CurrentPlayer
    play\hp=play\hp-damage
    If play\hp<=0
     playat(play\x,play\y)=-1
     If spl=60 Then AddCom("You totally annihilated the enemy wizard")
     PlayerDeath(si)
    EndIf
   EndIf
   si=si+1
  Next
 EndIf
 If enemy=2 Or enemy=3
  For create.creations=Each creations
   If create\x=selx And create\y=sely And create\owner<>CurrentPlayer
    create\hp=create\hp-damage
    If spl=45 And create\real=0 
     create\hp=0 
     AddCom("You dispell the illusion")
     Anim(selx-16,sely-32,0)
    EndIf
    If create\hp<=0
     If enemy=2 Then playat(create\x,create\y)=-1 Else Dismount(create\x,create\y,0)
    EndIf
   EndIf
  Next
 EndIf
 FreeImage back
End Function

;when a player Dies ALL thier creations die with them (except Fire and Blobs)
Function PlayerDeath(playerno)
 For create.creations = Each creations
  If create\owner=playerno
   playat(create\x,create\y)=-1
   Delete create
  EndIf
 Next
End Function

;casting function (keeping a careful eye to make sure there are no strechy rulers)
Function spellcasting(spell)
 err=0
 If selx>=selox-castrange And selx<=selox+castrange And sely>=seloy-castrange And sely<=seloy+castrange
  If manaavail>=castinfo(0)
   PlaySound Zap
   If castspell<41 Or castspell=57
    If castspell<>19 And castspell<>21
     If playat(selx,sely)=-1
      Anim(MouseX(),MouseY(),1)
      ;creation spells
      create.creations=New creations
      create\x=selx
      create\y=sely
      create\owner=CurrentPlayer
      create\tp=castspell
      create\hp=castinfo(3)
      create\mp=castinfo(4)
      create\range=castinfo(13)
      create\undead=castinfo(6)
      create\ranged=castinfo(5)
      If create\ranged=1 
       create\maxshots=1
      Else 
       create\maxshots=0
      EndIf
      create\shotsleft=create\maxshots
      create\flying=castinfo(7)
      create\mount=castinfo(8)
      create\magical=0 ;not used yet
      create\moveleft=castinfo(11)
      create\maxmove=castinfo(11)
      create\damage=castinfo(15)
      create\wp=castinfo(9)
      If castspell<41 Then create\real=castinfo(17) Else create\real=1
      playat(selx,sely)=2 ;tell map there is a creature here ;)
     Else
      err=1
     EndIf
    Else
     If castspell=19
      mon(selx,sely)=6
      err=0
     Else
      mon(selx,sely)=5
      err=0
     EndIf
    EndIf
   Else
    ;offensive spells
    If castspell=51 Or castspell=52
     If castspell=51 Then mon(selx,sely)=7 Else mon(selx,sely)=8
    EndIf
    If castspell=41 Or castspell=42
     If castspell=41 WizWp=7+Rnd(10) Else WizWp=4+Rnd(10)
     DefWp=7+Rnd(10)
     If playat(selx,sely)=1
      si=0
      uw=-1
      For play.player=Each player
       If play\x=selx And play\y=sely And si<>CurrentPlayer
        If play\Armour=1 Then DefWp=8+Rnd(10)
        If play\Shield=1 Then DefWp=8+Rnd(10)
        If WizWp>DefWp 
         destroy=1
         uw=si
        Else 
         destroy=0
        EndIf
       EndIf
       si=si+1
      Next
      If destroy=1
       For create.creations=Each creations
        If create\owner=uw
         If playat(create\x,create\y)=2 Then playat(create\x,create\y)=-1 Else Dismount(create\x,create\y,0)
         Delete create
        EndIf
       Next
      EndIf
     EndIf
     If playat(selx,sely)=2 Or playat(selx,sely)=3
      If castspell=41 WizWp=7+Rnd(10) Else WizWp=4+Rnd(10)
      For create.creations=Each creations
       If create\x=selx And create\y=sely And create\owner<>CurrentPlayer
        If create\wp<10 Then DefWp=create\wp+Rnd(10) Else DefWp=10+Rnd(10)
        If WizWp>DefWp
         AddCom("You have destroyed the creature")
         If playat(create\x,create\y)=2 Then playat(create\x,create\y)=-1 Else Dismount(create\x,create\y,0)
         Delete create     
        EndIf       
       EndIf
      Next
     EndIf
    EndIf
    If castspell=43
     Bolt(castinfo(10),playat(selx,sely),43)
     err=0
    EndIf
    If castspell=44
     Bolt(castinfo(10),playat(selx,sely),44)
     err=0
    EndIf
    If castspell=45 
     Bolt(castinfo(10),playat(selx,sely),45)
     err=0
    EndIf
    If castspell=46
     cs=0
     For play.player=Each player
      If cs=CurrentPlayer
       AddCom("You create a Magical shield")
       play\Shield=1
       err=0
      EndIf
      cs=cs+1
     Next
    EndIf   
    If castspell=47
     cs=0
     For play.player=Each player
      If cs=CurrentPlayer
       AddCom("You create a Magical Sword")
       play\Sword=1
       err=0
      EndIf
      cs=cs+1
     Next
    EndIf   
    If castspell=48
     cs=0
     For play.player=Each player
      If cs=CurrentPlayer
       AddCom("You create some magical wings")
       play\maxmove=6
       play\Wings=1
       err=0
      EndIf
      cs=cs+1
     Next
    EndIf
    If castspell=49
     cs=0
     For play.player=Each player
      If cs=CurrentPlayer
       AddCom("You feel yourself become lighter")
       play\maxmove=3
       play\ShadowForm=1
       err=0
      EndIf
      cs=cs+1
     Next
    EndIf
    If castspell=50
     cs=0
     For play.player=Each player
      If cs=CurrentPlayer
       AddCom("You create some Magical Armour")
       play\Armour=1
       err=0
      EndIf
      cs=cs+1
     Next
    EndIf
    If castspell=53
     cs=0
     For play.player=Each player
      If cs=CurrentPlayer
       play\align=play\align+100
       AddCom("You feel more Lawful")
       err=0
      EndIf
      cs=cs+1
     Next
    EndIf
    If castspell=54
     cs=0
     For play.player=Each player
      If cs=CurrentPlayer
       play\align=play\align-100
       AddCom("You feel more Chaotic")
       err=0
      EndIf
      cs=cs+1
     Next
    EndIf
    If castspell=55
     ;subversion
     For create.creations=Each creations
      If create\x=selx And create\y=sely And create\owner<>CurrentPlayer
       If create\wp<10 And create\real=1
        WizWp=7+Rnd(10)
        CreWp=create\wp+Rnd(10)
        If WizWp>CreWp 
         create\owner=CurrentPlayer
         AddCom("You subvert the creature to your cause")
        Else
         AddCom("Spell Failed")
        EndIf
       EndIf
      EndIf
     Next
    EndIf
    If castspell=56
     cs=0
     For play.player=Each player
      If cs=CurrentPlayer
       AddCom("You create a Magical Bow")
       play\Bow=1 
       play\maxshots=1
       play\shotsleft=1
       shotsavail=1
       err=0
      EndIf
      cs=cs+1
     Next
    EndIf
    If castspell=58
     mon(selx,sely)=9
     err=0
    EndIf
    If castspell=59
     Bolt(castinfo(10),playat(selx,sely),59)
     err=0
    EndIf
    If castspell=60
     Bolt(5000,playat(selx,sely),60)
     err=0    
    EndIf
   EndIf
   If err=0
    castno=castno-1
    If castno<1
     ;if last cast then reset system to stop further casting
     castrange=-1
     castspell=-1
     castno=-1
     cs=0
	 If castinfo( 17 ) = 1
	     manaavail=manaavail-castinfo(0)
	 Else
		manaavail = manaavail - ( ( castinfo( 0 ) +1 ) /2 )
	 EndIf
     For play.player=Each player
      If cs=CurrentPlayer
       play\mana=manaavail
       play\align=play\align+castinfo(0)
       If play\align>2500 Then play\align=2500
       If play\align<-2500 Then play\align=-2500
      EndIf
      cs=cs+1
     Next
    EndIf
   EndIf
  Else
   AddCom("You do not have enough Mana to cast the spell")
  EndIf
 Else
  AddCom("You must cast the spell within range of your wizard")
 EndIf
End Function

Function Box(tx,ty,bx,by,filled)
 If filled=1
  Rect tx,ty,bx-tx,by-ty
 Else
  Line tx,ty,bx,ty
  Line tx,ty,tx,by
  Line bx,ty,bx,by
  Line tx,by,bx,by
 EndIf
End Function

Function Cast(playerno)
	firearrow=-1
	;Currently selected spell
	css=0
	;start spell (where the spell list starts)
	ss=0
	;selected casting spell
	scs=-1
	;ct is the cast type (real or illusion, illusions are half the cost but can be dispelled)
	ct=1
	;search type for player and get the Mana available
	ni=0
	For play.player=Each player
		If ni=playerno 
			ma=play\mana
		EndIf
		ni=ni+1
	Next
	back=CreateImage(640,480)
	GrabImage back,0,0
 Repeat
  ok=0
  DrawImage back,0,0
  Color 255,255,255
  Text 320,30,"CAST A SPELL",1,1
  Color 50,50,50
  Rect 170,60,300,360
  Color 25,25,25
  Line 170,60,470,60
  Line 170,60,170,420
  Line 171,61,469,61
  Line 171,61,171,421
  Color 150,150,150
  Line 470,420,470,60
  Line 470,420,170,420
  Line 172,84,469,84
  Line 172,395,469,395
  Color 255,255,255
  Text 174,64,"Spell"
  Text 360,64,"Cost"
  Text 420,64,"Range"
  Box(408,399,464,416,0)
  Text 410,400,"Cancel"
  If ss+18<59 
   Text 455,375,"+"
   Box(453,375,465,390,0)
  EndIf
  If ss>0 
   Text 455,86,"-"
   Box(453,86,465,101,0)
  EndIf
  If scs>-1 Then Color 255,255,255 Else Color 100,100,100
  Box(290,399,350,416,0)
  Text 320,400,"Cast",1
  If ct=1 Then Text 178,400,"Real" Else Text 178,400,"Illusion"
  Box(176,399,252,416,0)
  Color 255,255,255
  ln=88
  For n=ss To ss+18
   a=0
   For spell.spel=Each spel
    If n=a
     If n=scs Then Color 255,0,0 Else Color 255,255,255
     Text 174,ln,spell\name$
     If ct=1 Then Text 360,ln,spell\Cost 
     If ct=0 Then Text 360,ln,(spell\Cost+1)/2
     Text 420,ln,spell\range
     If spell\align>0 Then Text 340,ln,"^"
     If spell\align=0 Then Text 340,ln,"-"
     If spell\align<0 Then Text 340,ln,"*"    
     ln=ln+16
    EndIf
    a=a+1 
   Next
  Next 
  showmouse()
  If mousetime>0 Then mousetime=mousetime-1
  If MouseDown(1)=1 And mousetime<=0
   Repeat
   Until MouseDown(1)=0
   mx=MouseX()
   my=MouseY()
   If mx>170 And mx<452 And my>84 And my<395
    ssx=mx-170
    ssy=my-84
    If ssy>0 Then scs=(ssy/16)+ss Else scs=ss
   EndIf
   If mx>290 And mx<350 And my>399 And my<416 Then ok=1
   If mx>176 And mx<252 And my>399 And my<416 And ct=1 Then ct=0 Else ct=1
   If mx>453 And mx<465 And my>375 And my<390 And ss<42 Then ss=ss+1
   If mx>453 And mx<465 And my>86 And my<101 And ss>0 Then ss=ss-1
   If mx>408 And mx<464 And my>399 And my<416 Then ok=-1 ;cancel
   mousetime=Tier
  EndIf
  Flip
 Until KeyDown(1)=1 Or ok<>0
 mousetime=Tier*10
 If scs>-1 And ok<>-1
  AddCom("Select location to cast or press K to cancel")
  castspell=scs
  casting=1
  cs=0
  For spell.spel=Each spel
   If cs=scs
    If ct=1 Then castinfo(0)=spell\Cost   
    If ct=0 Then castinfo(0)=(spell\Cost+1)/2
    castinfo(1)=spell\award  
    castinfo(2)=spell\range
    castinfo(3)=spell\hp
    castinfo(4)=spell\mp
    castinfo(5)=spell\ranged
    castinfo(6)=spell\undead
    castinfo(7)=spell\flying
    castinfo(8)=spell\mount
    castinfo(9)=spell\wp
    castinfo(10)=spell\attack
    castinfo(11)=spell\movement
    castinfo(12)=spell\shot
    castinfo(13)=spell\shotrange
    castinfo(14)=spell\castno
    castinfo(15)=spell\attack
    castinfo(16)=spell\align
    castinfo(17)=ct
    castno=spell\castno
    castrange=spell\range
    startcastno=castno
   EndIf
   cs=cs+1
  Next
 EndIf 
 FreeImage back
 If KeyDown(1)=1 Then End
End Function

Function Winner(playerno)
 showmap(selox,seloy,0,0,1)
 back=CreateImage(640,480)
 GrabImage back,0,0
 Repeat
  DrawImage back,0,0
  Color 0,0,0
  Text 321,261,"The winner is player "+(playerno+1)+"!!!",1,1
  Color 255,255,255
  Text 320,260,"The winner is player "+(playerno+1)+"!!!",1,1
  Color 0,0,0
  Text 321,441,"Press Enter to continue",1,1
  Color 255,255,255
  Text 320,440,"Press Enter to continue",1,1
  Flip
 Until KeyDown(28)=1
 FreeImage back
 Cls
 Flip
 Cls
 ;set endgame flag to 1 so game ends
 EndGame=1
 ;release all the Type lists (to free up memory used by the old creatures and NPC's)
 For create.creations=Each creations
  Delete create
 Next
 For play.player=Each player
  Delete play
 Next
 For civl.civilian=Each civilian
  Delete civl
 Next
 ;jump to start of main game to return to main menu
End Function

Function EndTurn(playerno)
 ;update positions of wizard characters and creations
 sp=0
 For play.player=Each player
  If sp=CurrentPlayer
   px=play\x
   py=play\y
  EndIf
  sp=sp+1
 Next
 If playat(px,py)=-1
  For create.creations=Each creations
   If create\owner=CurrentPlayer And playat(create\x,create\y)=3 
    px=create\x
    py=create\y
   EndIf
  Next
 EndIf
 sp=0
 For play.player=Each player
  If sp=CurrentPlayer
   play\x=px
   play\y=py
  EndIf
  sp=sp+1
 Next
 For create.creations = Each creations
  playat(create\x,create\y)=2
 Next
 For play.player=Each player
  If playat(play\x,play\y)=2 Then playat(play\x,play\y)=3 Else playat(play\x,play\y)=1
 Next
 ;end turn sequence goes here
 back=CreateImage(640,480)
 GrabImage back,0,0
 Repeat
  DrawImage back,0,0
  Color 0,0,0
  Text 321,241,"End your turn ?",1,1
  Color 255,255,255
  Text 320,240,"End your turn ?",1,1
  showmouse()
  Flip
 Until KeyDown(49)=1 Or KeyDown(21)=1
 If KeyDown(21)=1 
  ni=0 
  For play.player=Each player
   If ni=playerno 
    play\mana=manaavail
   EndIf
   ni=ni+1
  Next
  Repeat 
   ok=0
   playerno=playerno+1
   ni=0 
   For play.player=Each player
    If ni=playerno 
     If play\hp>0 Then ok=1
    EndIf
    ni=ni+1
   Next
  Until ok=1 Or playerno>MaxPlayers-1
  If (playerno>MaxPlayers-1) 
   If MapOp(3)=1 Then NPCmove()
   playerno=-1
   pa=0
   lp=-1
   si=0
   For play.player=Each player
    If play\hp>0 
     pa=pa+1
     lp=si
    EndIf
    si=si+1
   Next
   If pa=1 Then Winner(lp)
   Repeat 
    ok=0
    playerno=playerno+1
    ni=0 
    For play.player=Each player
     If ni=playerno 
      If play\hp>0 Then ok=1
     EndIf
     ni=ni+1
    Next
   Until ok=1 Or playerno>MaxPlayers-1
   If playerno>MaxPlayers Then End
  EndIf
  Selectplayer(playerno)
 EndIf
 FreeImage back
End Function

Function Selectplayer(playerno)
 CurrentPlayer=playerno
 ni=0
 manaavail=25
 For play.player=Each player
  If ni=playerno 
   plx=play\x
   ply=play\y
   ali=play\align
   selox=play\x
   seloy=play\y
   heartbeat=play\hp*1.2
   play\moveleft=play\maxmove
   play\shotsleft=play\maxshots
   manaavail=manaavail+play\mana
  EndIf
  ni=ni+1
 Next
 cs=0
 For create.creations=Each creations
  If create\owner=playerno 
   If create\real=1 Then manaavail=manaavail+5
   create\moveleft=create\maxmove
   create\shotsleft=create\maxshots
  EndIf
 Next
 If playat(plx,ply)=-1
  ;if player not updated when riding creature then update
  For create.creations=Each creations
   If create\owner=playerno And playat(create\x,create\y)=3
    plx=create\x
    ply=create\y
    selox=create\x
    seloy=create\y
   EndIf
  Next
  sp=0
  For play.player=Each player
   If sp=playerno
    play\x=plx
    play\y=ply
   EndIf   
  Next
 EndIf
 If mon(plx,ply)=7 ;magic castle
  If ali>0
   ali=ali+10
   manaavail=manaavail+(((ali/500)+1)*50)
   sp=0
   For play.player=Each player
    If sp=playerno Then play\align=ali
    sp=sp+1
   Next
  EndIf
 EndIf
 If mon(plx,ply)=8 ;dark citadel
  If ali<0
   ali=ali-10
   manaavail=manaavail+(((ali/500)+1)*50)
   sp=0
   For play.player=Each player
    If sp=playerno Then play\align=ali
    sp=sp+1
   Next
  EndIf
 EndIf
 AddCom("Player "+(playerno+1)+"'s Turn ")
End Function

Function CheckEvent()
	e=WaitEvent()
	Select e
	Case $803 : If (Confirm( "Quit: Are you sure?", True )=1) Then End
	End Select
End Function

;starting sequence for system (included Title menu system)
Function Titlesequence()
	Local TWindow = CreateWindow( "Main Menu", ( ClientWidth( Desktop() )/2 )-105, ( ClientHeight( Desktop() )/2 )-165, 210, 330 )
	Local StartButton = CreateButton( "Start Game", 0, 0, 200, 40, TWindow )
	Local LoadButton = CreateButton( "Load Game", 0, 40, 200, 40, TWindow )
	Local CreditsButton = CreateButton( "Credits", 0, 80, 200, 40, TWindow )
	Local InstructButton = CreateButton( "Instructions", 0, 120, 200, 40, TWindow )
	Local QuitButton = CreateButton( "Quit", 0, 200, 200, 40, TWindow )

	ShowGadget Twindow

	Local tok=1

	While tok=1
		ShowMap( 50, 50, 0, 0, 0)
		e = WaitEvent()
		Select e
		Case $803: End
		End Select
		
		Select EventID()	;see what gadget has been clicked on
		Case $401 : Select EventSource()	;$401 a button has been clicked, check which one
					Case StartButton: tok=1 : Goto startjump
					Case LoadButton: tok=3 : Goto startjump
					Case CreditsButton: Credits()
					Case InstructButton: Instructions()
					Case QuitButton: End
					End Select
		End Select
	Wend

	.startjump

	FreeGadget startbutton
	FreeGadget loadbutton
	FreeGadget creditsbutton
	FreeGadget instructbutton
	FreeGadget quitbutton
	FreeGadget twindow

	Select tok
	Case 1 : Options()
	Case 3 : DiskAccess(1)
	End Select
End Function

Function setsounds()
 If Sounds>0
  If Sounds-4>=0
   a=Rnd(65500)
   If a<90 PlaySound BoarSound
   Sounds=Sounds-4
  EndIf
  If Sounds-2>=0
   a=Rnd(65500)
   If a<90 Then PlaySound Moo
   Sounds=Sounds-2
  EndIf
  If Sounds-1>=0
   a=Rnd(65500)
   If a<90 Then PlaySound Baa
   Sounds=Sounds-1
  EndIf
 EndIf
End Function

Function SelectSeed(oldseed)
 ;Text entry system to stop players enter incorrect or invalid seeds
 Newseed=oldseed
 backg=CreateImage(640,480)
 GrabImage backg,0,0
 seedstr$=" "
 noc=0
 Repeat
  DrawImage backg,0,0
  Color 50,50,50
  Box(150,200,490,280,1)
  Color 25,25,25
  Box(160,240,480,260,1)
  Color 255,255,255
  Text 320,220,"PLEASE ENTER THE NEW SEED",1,1
  Text 320,250,seedstr$,1,1
  Flip
  If GetKey()<>0 And noc<20
   If KeyDown(2)=1 Then seedstr$=seedstr$+"1"
   If KeyDown(3)=1 Then seedstr$=seedstr$+"2"
   If KeyDown(4)=1 Then seedstr$=seedstr$+"3"
   If KeyDown(5)=1 Then seedstr$=seedstr$+"4"
   If KeyDown(6)=1 Then seedstr$=seedstr$+"5"
   If KeyDown(7)=1 Then seedstr$=seedstr$+"6"
   If KeyDown(8)=1 Then seedstr$=seedstr$+"7"
   If KeyDown(9)=1 Then seedstr$=seedstr$+"8"
   If KeyDown(10)=1 Then seedstr$=seedstr$+"9"
   If KeyDown(11)=1 Then seedstr$=seedstr$+"0"
   If KeyDown(14)=0 noc=noc+1
   If KeyDown(14)=1
    Newstr$=""
    If Len(seedstr$)-1>2
     For n=1 To Len(seedstr$)-1
      Newstr$=Newstr$+Mid$(seedstr$,n,1)
     Next
     seedstr$=""
     seedstr$=seedstr$+Newstr$
     noc=noc-1
    Else
     seedstr$=" "
     noc=0
    EndIf
   EndIf
  EndIf
 Until KeyDown(28)=1
 FreeImage backg
 If seedstr$<>" " Then Newseed=seedstr$
 Return Newseed
End Function

Function Options()
 ;options to setup game
 mtime=0
 seed=Rnd(65500)
 showmap(Mapsize/2,Mapsize/2,0,0,1)
 back = CreateImage(640,480)
 GrabImage back,0,0
 Repeat
  ok=0
  DrawImage back,0,0
  Color 25,25,25
  Box(50,50,589,439,1)
  DrawImage OptionsTitle,220,70
  Color 255,255,255
  Text 320,200,"NUMBER OF PLAYERS ["+Nop+"]",1,1
  If MapOp(0)=1 Then Text 320,230,"MAP WILL HAVE FORESTS",1,1 Else Text 320,230,"NO FORESTS",1,1
  If MapOp(1)=1 Then Text 320,260,"MAP WILL HAVE TOWNS",1,1 Else Text 320,260,"NO TOWNS",1,1
  If MapOp(2)=1 Then Text 320,290,"MAP WILL HAVE RIVERS",1,1 Else Text 320,290,"NO RIVERS",1,1
  If MapOp(3)=1 Then Text 320,320,"MAP WILL HAVE NON PLAYER CHARACTERS",1,1 Else Text 320,320,"NO NON PLAYER CHARACTERS",1,1
  Text 320,350,"SELECT MAP SEED",1,1
  showmouse()
  Text 320,419,"Continue",1,1
  mx=MouseX()
  my=MouseY()
  If mtime>0 Then mtime=mtime-1
  If mx>160 And mx<479
   Color 255,255,255
   If my>190 And my<211
    Box(170,190,469,211,0)
    If MouseDown(1)=1 And mtime<=0 
     Nop=Nop+1
     If Nop>8 Then Nop=2
     mtime=10
    EndIf
   EndIf
   If my>220 And my<241
    Box(150,220,489,241,0)
    If MouseDown(1)=1 And mtime<=0
     If MapOp(0)=0 Then MapOp(0)=1 Else MapOp(0)=0
     mtime=10
    EndIf
   EndIf
   If my>250 And my<271
    Box(150,250,489,271,0)
    If MouseDown(1)=1 And mtime<=0
     If MapOp(1)=0 Then MapOp(1)=1 Else MapOp(1)=0
     mtime=10
    EndIf
   EndIf
   If my>280 And my<301
    Box(150,280,489,301,0)
    If MouseDown(1)=1 And mtime<=0
     If MapOp(2)=0 Then MapOp(2)=1 Else MapOp(2)=0
     mtime=10
    EndIf
   EndIf
   If my>310 And my<331
    Box(150,310,489,331,0)
    If MouseDown(1)=1 And mtime<=0
     If MapOp(3)=0 Then MapOp(3)=1 Else MapOp(3)=0
     mtime=10
    EndIf
   EndIf
   If my>340 And my<361
    Box(150,340,489,361,0)
    If MouseDown(1)=1 Then seed=SelectSeed(seed)
   EndIf
   If my>409 And my<430
    Box(150,409,489,430,0)
    If MouseDown(1)=1 Then ok=1
   EndIf
  EndIf
  Repeat
  Until MouseDown(1)=0
  Flip
 Until KeyDown(1)=1 Or ok<>0
 If KeyDown(1)=1 Then End
 ;reset player type so there are no illegal players
 For play.player = Each player
  Delete play
 Next
 createmap(seed,1)
 CurrentPlayer=0
 sp=0
 For play.player=Each player
  If sp=0
   plx=play\x
   ply=play\y
   selox=plx
   seloy=ply
  EndIf
  sp=sp+1
 Next
 FreeImage back
 Selectplayer(0)
End Function

Function Credits()
	Local Notif$=""

	Notif$ = "Wizard Wars"+Chr$(10)
	Notif$ = Notif$+ "Copyright ©2000 EdzUp"+Chr$(10)
	Notif$ = Notif$+ ""+Chr$(10)
	Notif$ = Notif$+ "Written by"+Chr$(10)
	Notif$ = Notif$+ "Ed Upton"+Chr$(10)
	Notif$ = Notif$+ ""+Chr$(10)
	Notif$ = Notif$+ "Ideas by"+Chr$(10)
	Notif$ = Notif$+ "Ed Upton"+Chr$(10)
	Notif$ = Notif$+ "Rue Upton"+Chr$(10)
	Notif$ = Notif$+ ""+Chr$(10)
	Notif$ = Notif$+ "Tested by"+Chr$(10)
	Notif$ = Notif$+ "Ed Upton"+Chr$(10)
	Notif$ = Notif$+ "Rue Upton"+Chr$(10)
	Notif$ = Notif$+ "The BlitzBeta crew"+Chr$(10)

	Notify Notif$
End Function

Function capturelogo()
 Cls
 ca=0
 DrawImage title,0,0
 For yy=0 To 100
  li(yy)=CreateImage(200,1)
  GrabImage li(yy),0,yy
  lo(yy)=Sin((2*ca)*Pi/360)*3
  ca=ca+910
 Next
 Cls
 Color 255,255,255
End Function

Function placecivilians()
 For ns=0 To 1000
  civl.civilian = New civilian
  civl\x=Rnd(Mapsize)
  civl\y=Rnd(Mapsize)
  civl\xoff=0
  civl\yoff=0
  civl\st=0
  civl\face=0
  civl\movetime=5
  civl\tp=Rnd(5)
  mon(civl\x,civl\y)=1
 Next
End Function

Function placeplayers(maxplayers)
 For ns=0 To maxplayers-1
  play.player = New player
  If ns=0
   play\x=50
   play\y=50
  EndIf
  If ns=1 
   play\x=Mapsize-50
   play\y=Mapsize-50
  EndIf
  If ns=2
   play\x=Mapsize-50
   play\y=50
  EndIf
  If ns=3
   play\x=50
   play\y=Mapsize-50
  EndIf
  If ns=4
   play\x=Mapsize/2
   play\y=50
  EndIf
  If ns=5
   play\x=Mapsize/2
   play\y=Mapsize-50
  EndIf
  If ns=6
   play\x=50
   play\y=Mapsize/2
  EndIf
  If ns=7
   play\x=Mapsize-50
   play\y=Mapsize/2
  EndIf
  play\Wings=0
  play\Sword=0
  play\Armour=0
  play\ShadowForm=0
  play\Shield=0
  play\Bow=0
  play\tp=ns
  play\xoff=0
  play\yoff=0
  playat(play\x,play\y)=1
  play\align=0
  play\mana=0
  play\maxmove=1
  play\hp=100
  play\wp=7
  play\maxshots=0
 Next
End Function

Function AlignmentIndicator(playerno)
 DrawImage Indicate,0,420
 ni=0
 For play.player=Each player
  If ni=playerno 
   If play\align>0 Then al=play\align/500 Else al=0
   ma=play\mana
  EndIf
  ni=ni+1
 Next
 ma=manaavail
 ca=Abs(al)
 cx=2
 If al<>0
  For n=1 To ca
   If al<0 Then DrawImage Chaos,cx,430 Else DrawImage Law,cx,430
   cx=cx+30
  Next
 EndIf
 Color 255,255,255
 Text 75,458,ma,1
 Text 75,400,"Player "+(playerno+1),1
End Function

;function to move civs across landscape
Function movecivs()
 For civl.civilian=Each civilian
  If civl\movetime<=0
   mon(civl\x,civl\y)=0
   If civl\face=0
    civl\xoff=civl\xoff+4
    civl\yoff=civl\yoff-2
    civl\st=civl\st+1
    If civl\st>3
     civl\st=-3
     civl\xoff=-12
     civl\yoff=6
     civl\y=civl\y-1
     If civl\y<0 Then civl\y=0
    EndIf
   EndIf
   If civl\face=1
    civl\xoff=civl\xoff+8
    civl\st=civl\st+1
    If civl\st>3
     civl\st=-3
     civl\xoff=-24
     civl\x=civl\x+1
     civl\y=civl\y-1
     If civl\x>Mapsize Then civl\x=Mapsize
     If civl\y<0 Then civl\y=0
    EndIf  
   EndIf
   If civl\face=2
    civl\xoff=civl\xoff+4
    civl\yoff=civl\yoff+2
    civl\st=civl\st+1
    If civl\st>3
     civl\st=-3
     civl\xoff=-12
     civl\yoff=-6
     civl\x=civl\x+1
     If civl\x>Mapsize Then civl\x=Mapsize
    EndIf
   EndIf
   If civl\face=3
    civl\yoff=civl\yoff+4
    civl\st=civl\st+1
    If civl\st>3
     civl\st=-3
     civl\yoff=-12
     civl\x=civl\x+1
     civl\y=civl\y+1
     If civl\x>Mapsize Then civl\x=Mapsize
     If civl\y>Mapsize Then civl\y=Mapsize
    EndIf  
   EndIf
   If civl\face=4
    civl\xoff=civl\xoff-4
    civl\yoff=civl\yoff+2
    civl\st=civl\st+1
    If civl\st>3
     civl\st=-3
     civl\xoff=12
     civl\yoff=-6
     civl\y=civl\y+1
     If civl\y>Mapsize Then civl\y=Mapsize
    EndIf
   EndIf
   If civl\face=5
    civl\xoff=civl\xoff-8
    civl\st=civl\st+1
    If civl\st>3
     civl\st=-3
     civl\xoff=24
     civl\x=civl\x-1
     civl\y=civl\y+1
     If civl\x<0 Then civl\x=0
     If civl\y>Mapsize Then civl\y=Mapsize
    EndIf  
   EndIf
   If civl\face=6
    civl\xoff=civl\xoff-4
    civl\yoff=civl\yoff-2
    civl\st=civl\st+1
    If civl\st>3
     civl\st=-3
     civl\xoff=12
     civl\yoff=6
     civl\x=civl\x-1
     If civl\x<0 Then civl\x=0
    EndIf
   EndIf
   If civl\face=7
    civl\yoff=civl\yoff-4
    civl\st=civl\st+1
    If civl\st>3
     civl\st=-3
     civl\yoff=12
     civl\x=civl\x-1
     civl\y=civl\y-1
     If civl\x<0 Then civl\x=0
     If civl\y<0 Then civl\y=0
    EndIf  
   EndIf
   If civl\st=0 
    civl\face=Rnd(7)
    civl\xoff=0
    civl\yoff=0
   EndIf
   mon(civl\x,civl\y)=1
   civl\movetime=5
  Else
   civl\movetime=civl\movetime-1
  EndIf
 Next
 For n=0 To 500
  xx=Rnd(Mapsize)
  yy=Rnd(Mapsize)
  If mon(xx,yy)=5 Or mon(xx,yy)=6 Or mon(xx,yy)=9
   xa=xx-Rnd(2)
   ya=yy-Rnd(2)
   xa=xa+Rnd(2)
   ya=ya+Rnd(2)
   If xa<0 Then xa=0
   If ya<0 Then ya=0
   If xa>Mapsize Then xa=Mapsize
   If ya>Mapsize Then ya=Mapsize
   If map(xx,yy)>22 And map(xx,yy)<27
    If mon(xx,yy)=5 Then mon(xa,ya)=5
    If mon(xx,yy)=6 Then mon(xa,ya)=6
    If mon(xx,yy)=9 
     mon(xa,ya)=9
     mon(xx,yy)=0
     ;Vortex's can change creatures when they move from thier area (ONLY IF A WIZARD IS NOT
     ;RIDING THEM)
     If playat(xx,yy)=2
      For create.creations=Each creations
       If create\x=xx And create\y=yy 
        create\tp=Rnd(40)
        create\flying=Rnd(2)
        create\undead=Rnd(2)
        create\ranged=0
        create\hp=Rnd(100)
        create\mp=0
        create\maxmove=Rnd(6)+1
        create\moveleft=0
       EndIf
      Next
     EndIf
    EndIf
   EndIf
  EndIf
 Next
End Function

Function showlogo(x,y)
 in=0
 ca=ang
 For yy=y-50 To y+50
  DrawImage li(in),x-lo(in),yy
  lo(in)=Sin((2*ca)*Pi/360)*3
  in=in+1
  ca=ca+910
 Next
 Color 255,255,255
 Text x+100,y+50,"Copyright ©2000 EdzUp",1,1
 Text x+100,y+65,"Written in Blitz Basic by Ed Upton",1,1
 ang=ang+1820
End Function

Function showmouse()
 DrawImage mousecursor,MouseX(),MouseY()
End Function

Function createmap(level,outside)
 SeedRnd level

 For yy=0 To Mapsize
  For xx=0 To Mapsize
    If outside=0 
     map(xx,yy)=1
    Else
     a=Rnd(50)
     If a<50 
      map(xx,yy)=Rnd(3)+3
     Else
      a=Rnd(1)
      If a=0 Then map(xx,yy)=Rnd(2)+9 Else map(xx,yy)=7
     EndIf
    EndIf
    xoff(xx,yy)=Rnd(24)
    xoff(xx,yy)=xoff(xx,yy)-Rnd(24)
    yoff(xx,yy)=Rnd(12)
    yoff(xx,yy)=yoff(xx,yy)-Rnd(12)
    height(xx,yy)=0
  Next
 Next
 outside=1
 If outside=1
  For yy=1 To Mapsize
   For xx=1 To Mapsize
    map(xx,yy)=Rnd(3)+3
    mon(xx,yy)=0
    playat(xx,yy)=-1
   Next
  Next
  Flip
  If MapOp(0)=1
   Text 0,20,"Creating woods"
   Flip
   For n=0 To Rnd(Mapsize/33)+(Mapsize/50)
    timer=Rnd(5000)
    ax=Rnd(Mapsize)
    ay=Rnd(Mapsize)
    Repeat
     xx=ax-Rnd(50)
     xx=xx+Rnd(50)
     yy=ay-Rnd(50)
     yy=yy+Rnd(50)
     If xx<1 Then xx=Mapsize
     If xx>Mapsize Then xx=1
     If yy<1 Then yy=Mapsize
     If yy>Mapsize Then yy=1
     at=Rnd(100)
     timer=timer-1
     If at<100 Then map(xx,yy)=7 Else map(xx,yy)=8
    Until timer<1
   Next
   Flip
  EndIf
  If MapOp(1)=1
   Text 0,40,"Creating rivers"
   Flip
   xx=Rnd(Mapsize)
   rw=Rnd(3)+1
   d=Rnd(1)
   For n=0 To Mapsize
    a=Rnd(1)
    If a=0 Then xx=xx+Rnd(2) Else xx=xx-Rnd(2)
    For yy=xx-rw To xx+rw
     If d=0 
      If yy>-1 And yy<Mapsize Then map(n,yy)=Rnd(3)+23
     Else
      If yy>-1 And yy<Mapsize Then map(yy,n)=Rnd(3)+23
     EndIf
    Next
   Next
   Flip
  EndIf
  If MapOp(2)=1
   Text 0,60,"Creating populated areas"
   Flip
   For n=0 To Rnd(Mapsize/25)+(Mapsize/100)
    timer=Rnd(25)+5
    xx=Rnd(Mapsize)
    yy=Rnd(Mapsize)
    ;limit the number of shops and inns in towns (so they dont end up like markets)
    shops=(timer/5)
    inns=1+(timer/15)
    If n=0
     xx=Mapsize/2
     yy=Mapsize/2
    EndIf
    Repeat
     px=xx+Rnd(8)
     px=px-Rnd(8)
     py=yy+Rnd(8)
     py=py-Rnd(8)
     If px<1 Then px=1
     If px>Mapsize Then px=Mapsize
     If py<1 Then py=1
     If py>Mapsize Then py=Mapsize
     If shops>0 And inns>0 Then a=Rnd(5) Else
     If shops>0 And inns<=0 Then a=Rnd(3) Else 
     If shops<=0 And inns<=0 Then a=Rnd(1)
     If shops<=0 And inns>0
      Repeat
       a=Rnd(5)
      Until a<>2 And a<>3
     EndIf
     If map(px,py)<23 Or map(px,py)>26
      If a=0 Then map(px,py)=12 Else 
      If a=1 Then map(px,py)=13 Else
      If a=2 Then map(px,py)=14 Else
      If a=3 Then map(px,py)=15 Else
      If a=4 Then map(px,py)=16 Else
      If a=5 Then map(px,py)=17
      If a=2 Or a=3 Then shops=shops-1
      If a=4 Or a=5 Then inns=inns-1
      timer=timer-1
     EndIf
    Until timer<1
    map(xx,yy)=22
    For py=yy-7 To yy+7
     For px=xx-7 To xx+7
      If px>-1 And py>-1 And px<Mapsize And py<Mapsize
       If map(px,py)>11 And map(px,py)<23
        For ay=py-1 To py+1
         For ax=px-1 To px+1
          If ax>-1 And ay>-1 And ax<Mapsize And ay<Mapsize
           If map(ax,ay)<7 Or map(ax,ay)>23 Then map(ax,ay)=0
          EndIf
         Next
        Next
       EndIf
      EndIf
     Next
    Next
   Next
  EndIf
 EndIf
 If MapOp(3)=1 Then placecivilians()
 placeplayers(Nop)
End Function

Function showciv(xx,yy,sx,sy)
 cs=-1
 t=0
 n=0
 xo=0
 yo=0
 For civl.civilian=Each civilian
  If civl\x=xx And civl\y=yy
   cs=n
   t=civl\tp
   xo=civl\xoff
   yo=civl\yoff
  EndIf
  n=n+1
 Next
 If cs<>-1
  If t=0 Then DrawImage MaleCiv,(sx-16)+xo,(sy-32)+yo
  If t=1 Then DrawImage FemaleCiv,(sx-16)+xo,(sy-32)+yo
  If t=2 Then DrawImage sheep,(sx-16)+xo,(sy-32)+yo
  If t=3 Then DrawImage Cow,(sx-16)+xo,(sy-32)+yo
  If t>3 Then DrawImage Boar,(sx-16)+xo,(sy-32)+yo
  If t=2 Then Sounds=Sounds+1
  If t=3 Then Sounds=Sounds+2
  If t>3 Then Sounds=Sounds+4
 EndIf
End Function

Function showplayer(xx,yy,sx,sy)
 cs=-1
 t=-1
 n=0
 xo=0
 yo=0
 playwiz=-1
 If playat(xx,yy)=1
  For play.player=Each player
   If play\x=xx And play\y=yy
    If n=PlayerID Then DrawImage cursor3,sx-16,sy
    cs=n
    t=play\tp
    If playat(play\x,play\y)=3 Then t=-1
    xo=play\xoff
    yo=play\yoff
    If play\Shield=1 Then t=10
    If play\Sword=1 Then t=9
    If play\Armour=1 Then t=11
    If play\Bow=1 Then t=12
    If play\Wings=1 Then t=8
    If n=CurrentPlayer Then playwiz=t
   EndIf
   n=n+1
  Next
  If playwiz<>-1 Then t=playwiz
  If cs<>-1
   If t=0 Then DrawImage wizard1,(sx-16)+xo,(sy-32)+yo
   If t=1 Then DrawImage wizard2,(sx-16)+xo,(sy-32)+yo
   If t=2 Then DrawImage wizard3,(sx-16)+xo,(sy-32)+yo
   If t=3 Then DrawImage wizard4,(sx-16)+xo,(sy-32)+yo
   If t=4 Then DrawImage wizard5,(sx-16)+xo,(sy-32)+yo
   If t=5 Then DrawImage wizard6,(sx-16)+xo,(sy-32)+yo
   If t=6 Then DrawImage wizard7,(sx-16)+xo,(sy-32)+yo
   If t=7 Then DrawImage wizard8,(sx-16)+xo,(sy-32)+yo 
   If t=8 Then DrawImage WingsImage,(sx-16)+xo,(sy-32)+yo
   If t=9 Then DrawImage SwordImage,(sx-16)+xo,(sy-32)+yo
   If t=10 Then DrawImage ShieldImage,(sx-16)+xo,(sy-32)+yo
   If t=11 Then DrawImage ArmourImage,(sx-16)+xo,(sy-32)+yo
   If t=12 Then DrawImage BowImage,(sx-16)+xo,(sy-32)+yo
  EndIf
 EndIf
 If playat(xx,yy)=2 Or playat(xx,yy)=3
  For create.creations=Each creations
   If create\x=xx And create\y=yy
    If create\owner=PlayerID Then DrawImage cursor3,sx-16,sy
    cs=n
    t=create\tp
   EndIf
   n=n+1
  Next
  ;spells 19 and 21 are special spells (Magic Fire and Gooey Blob)
  If t=0 Then DrawImage Bat,(sx-16)+xo,(sy-32)+yo 
  If t=1 Then DrawImage Rat,(sx-16)+xo,(sy-32)+yo
  If t=2 Then DrawImage Cobra,(sx-16)+xo,(sy-32)+yo
  If t=3 Then DrawImage Wolf,(sx-16)+xo,(sy-32)+yo
  If t=4 Then DrawImage Knight,(sx-16)+xo,(sy-32)+yo
  If t=5 Then DrawImage Unicorn,(sx-16)+xo,(sy-32)+yo
  If t=6 Then DrawImage Horse,(sx-16)+xo,(sy-32)+yo 
  If t=7 Then DrawImage Pegasus,(sx-16)+xo,(sy-32)+yo 
  If t=8 Then DrawImage Gryphon,(sx-16)+xo,(sy-32)+yo 
  If t=9 Then DrawImage Giant,(sx-16)+xo,(sy-32)+yo
  If t=10 Then DrawImage Wraith,(sx-16)+xo,(sy-32)+yo
  If t=11 Then DrawImage Spectre,(sx-16)+xo,(sy-32)+yo
  If t=12 Then DrawImage Ghost,(sx-16)+xo,(sy-32)+yo
  If t=13 Then DrawImage Orc,(sx-16)+xo,(sy-32)+yo
  If t=14 Then DrawImage Goblin,(sx-16)+xo,(sy-32)+yo
  If t=15 Then DrawImage Ogre,(sx-16)+xo,(sy-32)+yo
  If t=16 Then DrawImage Skeleton,(sx-16)+xo,(sy-32)+yo
  If t=17 Then DrawImage Faun,(sx-16)+xo,(sy-32)+yo
  If t=18 Then DrawImage shadowwood,(sx-16)+xo,(sy-32)+yo
  If t=20 Then DrawImage magicwood,(sx-16)+xo,(sy-32)+yo
  If t=22 Then DrawImage RedDragon,(sx-16)+xo,(sy-32)+yo
  If t=23 Then DrawImage GreenDragon,(sx-16)+xo,(sy-32)+yo
  If t=24 Then DrawImage GoldDragon,(sx-16)+xo,(sy-32)+yo
  If t=25 Then DrawImage PlatinumDragon,(sx-16)+xo,(sy-32)+yo
  If t=26 Then DrawImage BlueDragon,(sx-16)+xo,(sy-32)+yo
  If t=27 Then DrawImage UndeadDragon,(sx-16)+xo,(sy-32)+yo
  If t=28 Then DrawImage Eagle,(sx-16)+xo,(sy-32)+yo
  If t=29 Then DrawImage Vampire,(sx-16)+xo,(sy-32)+yo
  If t=30 Then DrawImage Elf,(sx-16)+xo,(sy-32)+yo
  If t=31 Then DrawImage Troll,(sx-16)+xo,(sy-32)+yo
  If t=32 Then DrawImage Ghoul,(sx-16)+xo,(sy-32)+yo
  If t=33 Then DrawImage Manticore,(sx-16)+xo,(sy-32)+yo
  If t=34 Then DrawImage Harpy,(sx-16)+xo,(sy-32)+yo
  If t=35 Then DrawImage Centaur,(sx-16)+xo,(sy-32)+yo
  If t=36 Then DrawImage Bear,(sx-16)+xo,(sy-32)+yo
  If t=37 Then DrawImage Lion,(sx-16)+xo,(sy-32)+yo
  If t=38 Then DrawImage Gorilla,(sx-16)+xo,(sy-32)+yo
  If t=39 Then DrawImage Hydra,(sx-16)+xo,(sy-32)+yo
  If t=40 Then DrawImage Zombie,(sx-16)+xo,(sy-32)+yo
  If t=57 
   DrawImage wallend,(sx-16)+xo,(sy-70)+yo
   DrawImage cobble2,(sx-16)+xo,(sy-86)+yo
  EndIf
 EndIf
End Function

Function showmap(cx,cy,offx,offy,titlelogo)
 Sounds=0
 mx=MouseX()
 my=MouseY()
 sx=336-offx
 sy=-230-offy
 ox=sx
 oy=sy
 For yy=cy-15 To cy+15
  For xx=cx-15 To cx+15
    If xx>-1 And xx<Mapsize And yy>-1 And yy<Mapsize
     If sx>-50 And sx<690 And sy>-50 And sy<530
      sq=map(xx,yy)
      If sq=0 Then DrawImage cobble1,sx-16,sy
      If sq=1
       DrawImage wallend,sx-16,sy-48
      EndIf
      If sq=2
      EndIf
      If sq=3 Or sq>28 And sq<69 Then DrawImage grass1,sx-16,sy
      If sq=4 Then DrawImage grass2,sx-16,sy
      If sq=5 Then DrawImage grass3,sx-16,sy
      If sq=6 Then DrawImage grass4,sx-16,sy
      If sq>22 And sq<27
      a=Rnd(3)
      If a=0 Then DrawImage water1,sx-16,sy
      If a=1 Then DrawImage water2,sx-16,sy
      If a=2 Then DrawImage water3,sx-16,sy
      If a=3 Then DrawImage water4,sx-16,sy
     EndIf
     If sq>6 
      If sq>6 And sq<12 Then DrawImage grass1,sx-16,sy
      If sq>11 And sq<23 Then DrawImage cobble1,sx-16,sy
     EndIf
     If mx-16>sx-16 And my-16>sy-16 And mx-16<sx+16 And my-16<sy+16
      If castrange=-1 And FlyRange=-1
       DrawImage cursor1,sx-16,sy 
      EndIf
      If castrange<>-1
       If xx>=selox-castrange And xx<=selox+castrange And yy>=seloy-castrange And yy<=seloy+castrange
        DrawImage cursor1,sx-16,sy 
       Else
        DrawImage cursor2,sx-16,sy 
       EndIf
      EndIf
      If FlyRange<>-1
       If xx>=selox-FlyRange And xx<=selox+FlyRange And yy>=seloy-FlyRange And yy<=seloy+FlyRange
        DrawImage cursor1,sx-16,sy 
       Else
        DrawImage cursor2,sx-16,sy 
       EndIf
      EndIf
      selx=xx
      sely=yy
     EndIf
     If sq=7 Then DrawImage tree,(sx-16)-xoff(xx,yy),(sy-32)-yoff(xx,yy)
     If sq=8 Then DrawImage magicwood,(sx-16)-xoff(xx,yy),(sy-32)-yoff(xx,yy)
     If sq=9 Then DrawImage rock,(sx-16)-xoff(xx,yy),(sy-32)-yoff(xx,yy)
     If sq=10 Then DrawImage rock2,(sx-16)-xoff(xx,yy),(sy-32)-yoff(xx,yy)
     If sq=11 Then DrawImage rock3,(sx-16)-xoff(xx,yy),(sy-32)-yoff(xx,yy)
     If sq=12 Then DrawImage house1,sx-16,sy-32
     If sq=13 Then DrawImage house2,sx-16,sy-32
     If sq=14 Then DrawImage shop1,sx-16,sy-32
     If sq=15 Then DrawImage shop2,sx-16,sy-32
     If sq=16 Then DrawImage inn1,sx-16,sy-32 
     If sq=17 Then DrawImage inn2,sx-16,sy-32
     If sq=22 Then DrawImage square,sx-16,sy-32
     If sq>29 And sq<33 Or sq=43 Then DrawImage DeadSmall,sx-16,sy-32
     If sq>32 And sq<38 Or sq>38 And sq<43 Or sq>43 And sq<51 Or sq>56 And sq<69 Then DrawImage DeadMedium,sx-16,sy-32
     If sq=38 Or sq>50 And sq<57 Then DrawImage DeadLarge,sx-16,sy-32
     If mon(xx,yy)=5 Or mon(xx,yy)=6 Or mon(xx,yy)=9
      If mon(xx,yy)=9
       DrawImage Vortex,sx-16,sy-32
      EndIf
      If mon(xx,yy)=5
       DrawImage Blob,sx-16,sy-32
      EndIf
      If mon(xx,yy)=6
       a=Rnd(2)
       If titlelogo=1
        If FireChannel=-1 Then FireChannel=PlaySound(FireSound)
        If ChannelPlaying(FireChannel)=0 Then FireChannel=PlaySound(Firesound)
       EndIf
       If a=0 Then DrawImage Fire,sx-16,sy-32 Else DrawImage Fire2,sx-16,sy-32
      EndIf     
     Else
      If mon(xx,yy)<>0
       showciv(xx,yy,sx,sy)
      EndIf 
     EndIf
     If playat(xx,yy)<>-1 And mon(xx,yy)<>7 And mon(xx,yy)<>8
      showplayer(xx,yy,sx,sy)
     EndIf
     If mon(xx,yy)=7 Then DrawImage Castle,sx-16,sy-32
     If mon(xx,yy)=8 Then DrawImage Citadel,sx-16,sy-32
    EndIf
   EndIf
   sx=sx+34
   sy=sy+16
  Next 
  sx=ox-34
  sy=oy+16
  ox=ox-34
  oy=oy+16
 Next 
 If titlelogo=0
  movecivs()
  showlogo(220,50)
 EndIf
End Function