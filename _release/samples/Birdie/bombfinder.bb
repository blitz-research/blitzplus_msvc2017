;
;	BombFinder
;	by David Bird(Birdie)
;

;create main window
w=ClientWidth(Desktop())
h=ClientHeight(Desktop())
Global window=CreateWindow( "Bomb Finder",(w-276)/2,(h-310)/2,276,310,0,5)
;Add some menus
menu=CreateMenu( "&File",0,WindowMenu(window) )
CreateMenu "&New",1,menu
CreateMenu "",2,menu
CreateMenu "E&xit",3,menu
menu2=CreateMenu( "&Help",0,WindowMenu(window))
CreateMenu( "&About",40,menu2)
UpdateWindowMenu window

Type point
	Field but
	Field x,y
	Field cnt
	Field bomb
	Field done
End Type

Dim grid.point(16,16)
resetgrid Rand(10,100)

;wait for an event...
While WaitEvent()<>$803
	evsource=EventSource()
	EvID=EventID()
	EvData=EventData()
	Select EvID
		Case $401
			For g.point=Each point
				If EvSource=g\but
					CheckGrid g
				EndIf
			Next
		Case $1001
			Select EvData
				Case 1	;new game
					If Confirm("New Game. ?")
						Resetgrid Rand(10,100)
					EndIf
				Case 3	;exit
					If Confirm("Do you really want to Exit. ?")
						End
					EndIf
				Case 40
					Notify "BombFinder written for BlitzPlus"+Chr$(13)+"by David Bird"
			End Select
				
	End Select
Wend
End

Function CheckGrid(g.point)
	If g=Null Then Return
	FreeGadget g\but
	g\but=CreateCanvas(5+(16*g\x),5+(16*g\y),15,15,window)
	SetGadgetLayout g\but,1,0,1,0
	SetBuffer CanvasBuffer(g\but)
	ClsColor 170,170,170
	Cls
	;what is it
	If g\bomb=True Then
		Color 255,0,0
		Text 8,8,"*",1,1	;BOOOOOOOOOOOOM
			Notify "Your Dead!!!!!!!!"
			ResetGrid 20
			Return
	Else
		If g\cnt>0 Then
			drawcount g\cnt
		EndIf
	EndIf
	g\done=True
	If g\cnt=0 Then CheckCollapse g\x,g\y
End Function

Function CheckCollapse(x,y)
	For xx=x-1 To x+1
		For yy=y-1 To y+1
			If xx<>x Or yy<>y Then
				If xx>-1 And xx<16
					If yy>-1 And yy<16
						If grid(xx,yy)\cnt=0 And grid(xx,yy)\done=False And grid(xx,yy)\bomb=False Then
							FreeGadget grid(xx,yy)\but
							grid(xx,yy)\but=CreateCanvas(5+(16*grid(xx,yy)\x),5+(16*grid(xx,yy)\y),15,15,window)
							SetGadgetLayout grid(xx,yy)\but,1,0,1,0

							SetBuffer CanvasBuffer(grid(xx,yy)\but)
							ClsColor 170,170,170
							Cls
							;what is it
							If grid(xx,yy)\cnt>0 Then
								drawcount grid(xx,yy)\cnt
							EndIf
							grid(xx,yy)\done=True
							If grid(xx,yy)\bomb=0 Then
								CheckCollapse xx,yy
							EndIf
						EndIf
					EndIf
				EndIf
			EndIf
		Next
	Next
End Function

Function DrawCount(cnt)
	Select cnt
		Case 1
			Color 0,100,0
		Case 2
			Color 0,155,0
		Case 3
			Color 0,255,0
		Case 4
			Color 0,0,100
		Case 5
			Color 0,0,155
		Case 6
			Color 0,0,200
		Case 7
			Color 0,0,255
		Case 8
			Color 200,200,0
	End Select
	Text 8,8,cnt,1,1
End Function

Function ResetGrid(numbombs)
	For g.point=Each point
		FreeGadget g\but
		Delete g
	Next
	For x=0 To 15
		For y=0 To 15
			g.point=New point
			grid(x,y)=g
			g\but=CreateButton( "",5+(16*x),5+(16*y),15,15,window)
			SetGadgetLayout g\but,1,0,1,0

			g\x=x
			g\y=y
		Next
	Next
	
	While cnt<numbombs
		x=Rand(0,15)
		y=Rand(0,15)
		If grid(x,y)\cnt=0 Then
			grid(x,y)\bomb=True
			cnt=cnt+1
		EndIf
	Wend
	
	For x=0 To 15
		For y=0 To 15
			If grid(x,y)\bomb=False
				cnt=0
				For yy=-1 To 1
					For xx=-1 To 1
						If x+xx>-1 And x+xx<16
							If y+yy>-1 And y+yy<16
								If grid(x+xx,y+yy)\bomb=True Then
									cnt=cnt+1
								EndIf
							EndIf
						EndIf
					Next
				Next
				grid(x,y)\cnt=cnt
			EndIf
		Next
	Next
End Function