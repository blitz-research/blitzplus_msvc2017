;
; 3D sorta thang.. 
; Cull backfaces with menu item in Edit
; by David Bird
;
Type vertex
	Field x#,y#,z#
	Field wx#,wy#,wz#
	Field sx#,sy#
End Type

Type triangle
	Field i[2]
End Type

Type surface
;	field b.brush
	Field vCnt
	Field v_list.vertex[1000]
	Field tCnt
	Field t_list.triangle[1000]
End Type

Type entity
	Field sCnt
	Field s.surface[100]
	Field class				;type of entity
	Field x#,y#,z#
	Field pitch#,roll#,yaw#
	Field scalex#,scaley#,scalez#
End Type

Global G_Width=ClientWidth(Desktop())-128
Global G_Height=ClientHeight(Desktop())-128
Global G_Window=CreateWindow("3d Application software. BlitzPlus",ClientWidth(Desktop())/2-G_Width/2,ClientHeight(Desktop())/2-G_Height/2,G_Width,G_Height)
Global G_WorkArea=CreateCanvas( 0,32,ClientWidth(G_Window),ClientHeight(G_Window),G_Window )
SetGadgetLayout G_WorkArea,1,1,1,1
Global G_Menu=WindowMenu(G_Window)
Setup_Menus
Global G_MidPointX#
Global G_MidPointY#
Global G_CullOn=True

Global CullMI
Global Add1

Global AddOne=CreateButton("Add a Cube",0,0,80,30,G_Window)
fps=60
period=1000/fps
time=MilliSecs()
e.entity=CreateCube()
positionentity e,0,0,350
scaleentity e,3,1,1

e2.entity=CreateCube()
positionentity e2,-10,0,350
scaleentity e2,1,3,1

SetBuffer CanvasBuffer (G_WorkArea)
	G_MidPointX=ClientWidth(G_Window)/2
	G_MidPointY=ClientHeight(G_Window)/2
Repeat
	
	time=time+period
	fire_hit=False
	Repeat
		wait=time-MilliSecs()
		If wait<0
			If wait<-100 time=time-wait
			wait=0
		EndIf
		If Not WaitEvent( wait ) Exit
		If KeyHit(1) End
		Select EventID()
		Case $401
			Select EventSource()
				Case AddOne
					e.entity=CreateCube()
					positionentity e,Rnd(-10,10),Rnd(-10,10),350
					scaleentity e,Rnd(1,4),Rnd(1,4),Rnd(1,4)
			End Select
					
					
		Case $802	;window size
			While WindowMinimized(G_window)
				WaitEvent()	;should handle window close here...
			Wend
		Case $803	;window close
			If Confirm( "Really Quit?" ) End
		Case $1001	;menu action!
			Select EventData()
			Case 11	;new
				EraseAll
			Case 14
				If Confirm( "Really Quit?" ) End

			Case 21;cull on/off
				G_CullOn=1-G_CullOn
				Select G_CullOn
					Case 1
						CheckMenu CullMI
				UpdateWindowMenu G_Window
					Case 0
						UncheckMenu CullMI
				UpdateWindowMenu G_Window
				End Select
			Case 22
				e.entity=CreateCube()
				positionentity e,Rnd(-10,10),Rnd(-10,10),350
				scaleentity e,Rnd(1,4),Rnd(1,4),Rnd(1,4)
			Case 101
				Notify "3D Application Viewer."
			End Select
		End Select
	Forever

	Cls
	DrawEntities
	For e.entity = Each entity
		Turnentity e,1,0,1
	Next
	FlipCanvas G_WorkArea
Forever
End


Function Setup_Windows()
	

End Function

Function Setup_Menus()
	File=	CreateMenu("&File",10,G_Menu)
			CreateMenu("&New",11,File)
  			CreateMenu("E&xit",14,File)

	Edit=	CreateMenu("&Edit",20,G_Menu)
	CullMI=	CreateMenu("&Cull BackFaces",21,Edit)
	Add1=	CreateMenu("&Add a cube",22,Edit)

	Help =	CreateMenu("&Help",100,G_Menu)
			CreateMenu("&About",101,Help)
	
	UpdateWindowMenu G_Window
End Function

Function check_cull(sx1#,sy1#,sx2#,sy2#,sx3#,sy3#)
	c=(sx1-sx2)*(sy3-sy2)-(sy1-sy2)*(sx3-sx2)
	If c<0 Then Return False
	Return True
End Function

Function DrawEntities()
	Local sx#[2],sy#[2]
	For e.entity = Each entity
		For sc=1 To e\sCnt
			s.surface=e\s[sc]
			For vc=0 To s\vCnt-1
				v.vertex=s\v_list[vc]
				v\wx=v\x*e\scaleX:v\wy=v\y*e\scaleY:v\wz=v\z*e\scaleZ
				;cheap rotation now Z X Y
				x#=v\wx*Cos(e\roll)+v\wy*Sin(e\roll)
				y#=v\wy*Cos(e\roll)-v\wx*Sin(e\roll)
				v\wx=x
				v\wy=y
				x#=v\wx*Cos(e\yaw)+v\wz*Sin(e\yaw)
				z#=v\wz*Cos(e\yaw)-v\wx*Sin(e\yaw)
				v\wx=x
				v\wz=z
				y#=v\wy*Cos(e\pitch)+v\wz*Sin(e\pitch)
				z#=v\wz*Cos(e\pitch)-v\wy*Sin(e\pitch)
				v\wy=y
				v\wz=z
				v\wx=v\wx+e\x
				v\wy=v\wy+e\y
				v\wz=v\wz+e\z;tempory
				;screen position
				v\sx=G_MidPointX-((v\wx*100/v\wz)*100)
				v\sy=G_MidPointY-((v\wy*100/v\wz)*100)
			Next
			;render the triangles
			For tc=0 To s\tcnt-1
				For b=0 To 2
					sx[b]=s\v_list[s\t_list[tc]\i[b]]\sx
					sy[b]=s\v_list[s\t_list[tc]\i[b]]\sy
				Next
				If check_cull(sx[0],sy[0],sx[1],sy[1],sx[2],sy[2])=0 Or G_Cullon=False
					Line sx[0],sy[0],sx[1],sy[1]
					Line sx[1],sy[1],sx[2],sy[2]
					Line sx[2],sy[2],sx[0],sy[0]
				EndIf
			Next
		Next
	Next
End Function

Function CreateCube.entity()
	e.entity=New entity
	e\sCnt=1
	s.surface=New surface
	e\s[1]=s
	Restore dat
	Read s\vCnt
	For b=0 To s\vCnt-1
		s\v_list[b]=New vertex
		Read s\v_list[b]\x
		Read s\v_list[b]\y
		Read s\v_list[b]\z
	Next
	Read s\tCnt
	For b=0 To s\tcnt-1
		s\t_list[b]=New triangle
		For c=0 To 2
			Read s\t_list[b]\i[c]
		Next
	Next	
	e\scalex=1
	e\scaley=1
	e\scalez=1
	Return e
End Function

.dat
Data 24
;back
Data 1,1,1
Data -1,1,1
Data -1,-1,1
Data 1,-1,1
;front
Data -1,1,-1
Data 1,1,-1
Data 1,-1,-1
Data -1,-1,-1

;left
Data -1,1,1
Data -1,1,-1
Data -1,-1,-1
Data -1,-1,1
;right
Data 1,1,-1
Data 1,1,1
Data 1,-1,1
Data 1,-1,-1

;top
Data -1,1,1
Data 1,1,1
Data 1,1,-1
Data -1,1,-1
;bottom
Data 1,-1,1
Data -1,-1,1
Data -1,-1,-1
Data 1,-1,-1

Data 12
Data 0,1,2
Data 0,2,3
Data 4,5,6
Data 4,6,7
Data 8,9,10
Data 8,10,11
Data 12,13,14
Data 12,14,15
Data 16,17,18
Data 16,18,19
Data 20,21,22
Data 20,22,23

Function Turnentity(e.entity,x#,y#,z#)
	e\pitch=e\pitch+x
	e\yaw=e\yaw+y
	e\roll=e\roll+z
End Function

Function Positionentity(e.entity,x#,y#,z#)
	e\x=x
	e\y=y
	e\z=z
End Function

Function ScaleEntity(e.entity,x#,y#,z#)
	e\scalex=x
	e\scaley=y
	e\scalez=z
End Function

Function EraseAll()
	Delete Each entity
	Delete Each vertex
	Delete Each triangle
	Delete Each surface
End Function
