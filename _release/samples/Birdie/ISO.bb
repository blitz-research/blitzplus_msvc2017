;
;
; ISO Engine in a window by David Bird
;
; quill3d@btopenworld.com
;


w=ClientWidth(Desktop())
h=ClientHeight(Desktop())
Global window=CreateWindow( "ISO by David Bird",(w-640)/2,(h-480)/2,640,480,0,32+7)

canvas=CreateCanvas(0,0,ClientWidth(window),ClientHeight(window),window)
SetGadgetLayout canvas,1,1,1,1
ActivateGadget canvas

menu=CreateMenu("&File",0,WindowMenu(window))
CreateMenu("E&xit",1,menu)
UpdateWindowMenu window


SetBuffer CanvasBuffer(canvas)

Global tiles=LoadAnimImage("tempblocks.bmp",64,64,0,14)
Global objects=LoadAnimImage("objects.bmp",64,32,0,1)
HandleImage objects,23,32
MaskImage objects,255,0,255

HandleImage tiles,32,64

Global G_objectsthatfall=9
Type s_object
	Field drawindex			;0=not draw. next ordered drawing
	Field typeofobject		;define the type of object
	
	Field control
	Field nextobj.s_object
	Field clipped			;true if on screen else false
	Field widx				;used for bounding box width in x
	Field widy				;used for bounding box width in y
	Field hgt				;used for bounding box height
	Field sx,sy				;converted screen position
	Field mx,my,mz			;iso map position
	Field image				;object type
	Field jump
	Field falling#
End Type

Type box
	Field min_x
	Field min_y
	Field min_z
	Field max_x
	Field max_y
	Field max_z
End Type

Global levx,levy

Data 144,144
Data 1,1,1,1,1,1,1,1,1,1
Data 0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0
Data 0,9,9,9,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0

Data 0,0,9,0,9,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0
Data 9,9,9,9,0,9,9,9,9,0
Data 9,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,9,0
Data 0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0

Data 0,0,9,0,9,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0
Data 9,9,9,9,9,9,9,0,0,0
Data 0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0
Data 0,0,0,0,0,0,0,0,0,0

Read levx,levy

;
; No need for each level to have so much data assigned to them
; I will change this. Just for testing it works well..
;
For z=0 To 128 Step 64
	For y=0 To 9
		For x=0 To 9
			Read t
			If t>0 Then			
				obj.s_object=New s_object
				If t=9 Then obj\typeofobject=9 Else obj\typeofobject=1
				obj\image=0
				If t=10 Then 
					obj\image=1:obj\typeofobject=9
					obj\mx=(x*16)
					obj\my=y*16
					obj\mz=z
					obj\widx=9
					obj\widy=13
					obj\hgt=1
				Else 
					obj\mx=x*16
					obj\my=y*16
					obj\mz=z
					obj\widx=15
					obj\widy=15
					obj\hgt=31
				End If
			End If
		Next
	Next
Next
Global coll.s_object		;// used for pushing objects

Global player.s_object=New s_object
player\typeofobject=999
player\mz=128
player\mx=0
player\my=0
player\image=501
player\widx=15
player\widy=15
player\hgt=63

Global p2.s_object=New s_object
Global jump

p2\typeofobject=998
p2\image=500

timer=CreateTimer(120)
Repeat
	Select WaitEvent()
		Case $802	;window size
			While WindowMinimized(window)
				WaitEvent()	;should handle window close here...
			Wend
		Case $803	;window close
			If Confirm( "Really Quit?" ) 
				Exit
			EndIf
		Case $1001	;menu action!
			Select EventData()
			Case 1
				If Confirm( "Really Quit?" ) 
					Exit
				EndIf
			Case 101
				Notify "Insectoids! (in a window...)"
			End Select
		Case $2001	;suspend!
			While WaitEvent()<>$2002
			Wend
		Case $4001
			Updateit
			FlipCanvas canvas
			FlushEvents $4001
	End Select
Forever

Delete Each s_object
Delete Each box
FreeImage tiles
End

Function Updateit()
	Cls
	Sort_N_Draw()	;sort all objects into order and draw them to screen

	kdone=False
	coll=Null
	If KeyDown(203) And player\mx>0 And kdone=False
		If retcheckmove(-1,0,0,player) Then 
			kdone=True
			player\mx=player\mx-1
		Else
			;check for push object
			If coll<>Null Then
				If coll\mx>0 Then
					checkmove(-1,0,0,coll)
				End If
			End If
		End If
	End If

	If KeyDown(205) And player\mx<levx And kdone=False
		If retcheckmove(1,0,0,player) Then 
			kdone=True
			player\mx=player\mx+1
		Else
			;check for push object
			If coll<>Null Then
				If coll\mx<levx Then
					checkmove(1,0,0,coll)
				End If
			End If
		End If
	End If

	If KeyDown(200) And player\my>0 And kdone=False
		If retcheckmove(0,-1,0,player) Then 
			kdone=True
			player\my=player\my-1
		Else
			;check for push object
			If coll<>Null Then
				If coll\my>0 Then
					checkmove(0,-1,0,coll)
				End If
			End If
		End If
	End If

	If KeyDown(208) And player\my<levy And kdone=False
		If retcheckmove(0,1,0,player) Then 
			kdone=True
			player\my=player\my+1
		Else
			;check for push object
			If coll<>Null Then
				If coll\my<levy Then
					checkmove(0,1,0,coll)
				End If
			End If
		End If
	End If


	If KeyDown(57) And jump=0 And (retcheckmove(0,0,-1,player)=False Or player\mz=32) Then 
		player\jump=32
		player\falling=8
	EndIf
;
	If player\jump>0 Then
		player\falling=player\falling-.25
		If player\falling=-1 Then player\falling=0
		If retcheckmove(0,0,player\falling,player) Then
			player\mz=player\mz+player\falling
		Else
			If coll<>Null Then
				If coll\my<levy Then
					checkmove(0,0,player\falling,coll)
				End If
			End If
		End If
		player\jump=player\jump-1
	End If
	check_objects_fall()
	
	;
	;	draw bottom of object
	;
	p2\mx=player\mx
	p2\my=player\my
	p2\mz=player\mz-32

End Function

;
;	transpose iso x,y & z to screen x,y
;
Function RetScnX(mapx,mapy,mapz)
	Return 320+(mapx*2)-(mapy*2)
End Function

Function RetScnY(mapx,mapy,mapz)
	Return 192+mapx+mapy
End Function
;
; function to do all culling and ordering
; of each obect in game. This works very well for most instances
; any object that is off screen then it will
; be excluded from calcuations speeds things up a bit
;
; draws all at end of sorting
Function Sort_N_Draw()
	object_count=0							;how many objects are on screen
	For a.s_object=Each s_object
		a\drawindex=0
		a\sx=RetScnX(a\mx,a\my,a\mz)		;converts all object map coords to screen coords
		a\sy=RetScnY(a\mx,a\my,a\mz)
		If a\sx>-32 And a\sx<672 Then 		;check if within screen rect x
			If a\sy>-64 And a\sy<542 Then	;check if within screen rect y
				a\clipped=True				;set object true if within screen rect
				object_count=object_count+1	;increase object count
			Else
				a\clipped=False
			End If
		Else
			a\clipped=False
		End If
	Next
	For b=1 To object_count+1
		ymax=999999999
		For a.s_object=Each s_object
			If a\clipped=True Then
				If a\drawindex=0 Then
					If a\sy+a\mz<=ymax Then 
						ymax=a\sy+a\mz
						thisone.s_object=a
					End If
				End If
			End If
		Next
		If thisone<>Null Then
			If b=1 Then
				lastone.s_object=thisone
				firstone.s_object=thisone
			Else
				lastone\nextobj.s_object=thisone
				lastone.s_object=thisone
			End If	
			thisone\drawindex=b
			thisone=Null
		End If
	Next
	For b=1 To object_count
		draw_block_image(firstone\sx,firstone\sy-firstone\mz,firstone\image)
		firstone.s_object=firstone\nextobj
	Next
End Function
;
; Do to boxes collide 
;
Function BoxColl(ob1.s_object,d1x,d1y,d1z,ob2.s_object)
	t1.box=New box
	t1\min_x=ob1\mx+d1x:t1\max_x=ob1\mx-ob1\widx+d1x
	t1\min_y=ob1\my+d1y:t1\max_y=ob1\my-ob1\widy+d1y
	t1\min_z=ob1\mz+d1z:t1\max_z=ob1\mz-ob1\hgt+d1z
	t2.box=New box
	t2\min_x=ob2\mx:t2\max_x=ob2\mx-ob2\widx
	t2\min_y=ob2\my:t2\max_y=ob2\my-ob2\widy
	t2\min_z=ob2\mz:t2\max_z=ob2\mz-ob2\hgt
	havetouched=False
	If (((t1\min_x>=t2\min_x) And (t1\max_x<=t2\min_x)) Or ( (t1\min_x>=t2\max_x) And (t1\max_x<=t2\max_x))) Then
		If (((t1\min_y>=t2\min_y) And (t1\max_y<=t2\min_y)) Or ( (t1\min_y>=t2\max_y) And (t1\max_y<=t2\max_y))) Then
			If (((t1\min_z>=t2\min_z) And (t1\max_z<=t2\min_z)) Or ( (t1\min_z>=t2\max_z) And (t1\max_z<=t2\max_z))) Then
				havetouched=True
			End If
		End If
	End If
	Delete Each box
	Return havetouched
End Function
;
;	Check a movement of a box
;	returns true if ok to move
;	also moves object after check
;	stops pushing of illigal ojects
;
Function checkmove(x,y,z,obj.s_object)
	canmove=True
	coll=Null
	For a.s_object=Each s_object
		If a<>obj Then
			If a\typeofobject<>998 Then 
				If BoxColl(obj,x,y,z,a)=True Then canmove=False:coll=a
			End If
		End If
	Next
	If canmove=True Then
		If obj\typeofobject>=G_objectsthatfall
			obj\mx=obj\mx+x
			obj\my=obj\my+y
			obj\mz=obj\mz+z
		End If
	End If
	Return canmove
End Function
;
;	Check a movement of a box
;	returns true if ok to move
;
Function retcheckmove(x,y,z,obj.s_object)
	canmove=True
	coll=Null
	For a.s_object=Each s_object
		If a<>obj Then
			If a\typeofobject<>998 Then
				If BoxColl(obj,x,y,z,a)=True Then canmove=False:coll=a
			End If
		End If
	Next
	Return canmove
End Function
;
;	Check if objects that can fall
;	are in a position to fall
;
Function Check_Objects_Fall()
	; First check for player
	If player\mz>32 And player\jump=0 Then 
		player\falling=player\falling+.25
		If player\falling=9 Then player\falling=8
		If retcheckmove(0,0,-player\falling,player) Then
			player\mz=player\mz-player\falling
		Else
			player\falling=0
		End If
	Else 
		If player\mz<32 Then player\mz=32
	End If
	;check all other objects
	For a.s_object=Each s_object
		If a<>player 
			If a\typeofobject>=G_objectsthatfall Then
				If a\mz>0 And a\jump=0 Then 
					a\falling=a\falling+.25
					If a\falling=9 Then a\falling=8
					If retcheckmove(0,0,-a\falling,a) Then
						a\mz=a\mz-a\falling
					Else
						a\falling=0
					End If
				Else 
					If a\mz<0 Then a\mz=0
				End If
			End If
		End If
	Next
End Function
;
;	Draw a image
;
Function Draw_Block_Image(x,y,typ)
	Select typ
		Case 0
			DrawImage tiles,x,y,0
		Case 1
			DrawImage objects,x,y,0
		Case 2
			DrawImage tiles,x,y,2
		Case 3
			DrawImage tiles,x,y,3
		Case 4
			DrawImage tiles,x,y,4
;// Next two are for players image
		Case 500
			DrawImage tiles,x,y,0
		Case 501
			DrawImage tiles,x,y,0
	End Select
End Function