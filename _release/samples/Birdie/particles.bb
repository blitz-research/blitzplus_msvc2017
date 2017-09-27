;BlitzPlus Particles color control
;by David Bird
Global Desk_Width=ClientWidth(Desktop())
Global Desk_Height=ClientHeight(Desktop())
winwid=600
winhgt=400
Global Main_Window=CreateWindow("BlitzPlus Particles",210,100,winwid,winhgt,0,7)
Global Main_Canvas=CreateCanvas(0,0,ClientWidth(Main_Window),ClientHeight(Main_Window),main_Window)
SetBuffer CanvasBuffer(Main_Canvas)
menu=WindowMenu(Main_window)
file=CreateMenu("&File",-1,menu)
CreateMenu("E&xit",100,file)
UpdateWindowMenu main_window

ColorWindow=CreateWindow("Color",100,100,100,winhgt,Main_Window)

Global Random=CreateButton("Random Color",5,10,80,15,ColorWindow)
Global PickNew=CreateButton("Pick Color",5,30,80,15,ColorWindow)
Global sliderRed=CreateSlider(20,50,10,100,ColorWindow,2)
Global slidergrn=CreateSlider(40,50,10,100,ColorWindow,2)
Global sliderblu=CreateSlider(60,50,10,100,ColorWindow,2)
SetSliderRange sliderred,0,255
SetSliderRange slidergrn,0,255
SetSliderRange sliderblu,0,255

ColorPanel=CreatePanel(5,160,80,60,ColorWindow)

Type part
	Field col
	Field x#,y#,r#,g#,b#
	Field dx#,dy#,lf#
End Type

Const Max_Particles=1000
Const grav#=.098

;timer=CreateTimer(60)

Global angd=2
Global ang
Global G_mode=2	;0 white only 1 selected 2 random
Global Last_Red,Last_Green,Last_Blue

Repeat
	Select WaitEvent(15)

		Case $401
			Select EventSource()
				Case picknew
					If RequestColor (Last_Red,Last_green,Last_blue)
						Last_Red=RequestedRed()
						Last_green=RequestedGreen()
						Last_blue=RequestedBlue()
						SetButtonState Random,0
						SetSliderValue SliderRed,Last_Red
						SetSliderValue Slidergrn,Last_green
						SetSliderValue Sliderblu,Last_blue
					EndIf
					G_mode = 1
				Case random
					G_Mode=2
				Case sliderred,slidergrn,sliderblu
					G_Mode = 1
			End Select

		Case $802
			FreeGadget Main_Canvas
			Main_Canvas=CreateCanvas(0,0,ClientWidth(Main_Window),ClientHeight(Main_Window),main_Window)			
			SetBuffer CanvasBuffer(Main_Canvas)

		Case $803	;window close
			If Confirm( "Really Quit?" ) 
				Exit
			EndIf

		Case $1001	;menu action!
			Select EventData()
				Case 100
					If Confirm("Exit Clock ?")
						End
					EndIf
			End Select

;		Case $2001	;suspend!
;			While WaitEvent()<>$2002
;			Wend

; do update on timer event
;		Case $4001
;			Do_Update
;			FlipCanvas Main_Canvas
;			FlushEvents $4001

	End Select	

; do update outside of message loop

	Do_Update
	FlipCanvas Main_Canvas


	SetPanelColor ColorPanel,SliderValue(SliderRed),SliderValue(SliderGrn),SliderValue(SliderBlu)
Forever

End

Function Do_Update()
	x=(ClientWidth(Main_Canvas)/2)+(ClientWidth(Main_Canvas)/2.5)*Cos(ang)
	y=(ClientHeight(Main_Canvas)/2)-(ClientHeight(Main_Canvas)/2.5)*Sin(ang)
	addnew_Particle Main_Canvas,x,y,50
	ang=ang+angd
	If ang>180 Or ang<0 Then angd=-angd

	Update_Particles Main_canvas
	drawParticles Main_canvas
	
End Function

Function Update_Particles(canvas)
	For p.part=Each part
		p\x=p\x+p\dx
		p\y=p\y+p\dy
		p\dy=p\dy+grav
		p\r=p\r-1
		p\g=p\g-1
		p\b=p\b-1
		e=0
		If p\r<0 Then p\r=0:E=e+1
		If p\g<0 Then p\g=0:e=e+1
		If p\b<0 Then p\b=0:e=e+1
		If p\y>ClientHeight(canvas) Or e=3
			Delete p
			Part_Count=Part_Count-1
;			AddNew_Particle(Rnd(0,ClientWidth(canvas)),Rnd(0,ClientWidth(canvas)))
		ElseIf p\x<0 Then 
			p\x=ClientWidth(canvas)-5
		ElseIf p\x>ClientWidth(canvas)-5
			p\x=5
		EndIf
	Next
End Function

Function AddNew_Particle(canvas,x#,y#,count)
	For a=1 To count
		If Part_Count>=MAX_PARTICLES Then Return
		Part_Count=Part_Count+1
		p.part=New part
		p\x=x
		p\y=y
		w#=ClientWidth(Canvas)/150
		p\dx=Rnd(-w,w)
		p\dy=Rnd(-w,-.01)
		Select G_Mode
			Case 2
				p\r=Rnd(255)
				p\g=Rnd(255)
				p\b=Rnd(255)
			Case 1
				p\r=SliderValue(SliderRed)
				p\g=SliderValue(SliderGrn)
				p\b=SliderValue(SliderBlu)
			Case 0
				p\r=255
				p\g=255
				p\b=255
		End Select
	Next
End Function

Function DrawParticles(canvas)
	Cls
	LockBuffer CanvasBuffer(canvas)
	For p.part=Each part
		If p\x>0 And p\x<ClientWidth(canvas)-1
			If p\y>0 And p\y<ClientHeight(canvas)-1
				rgb=(((p\r Shl 8) Or p\g) Shl 8) Or p\b
				WritePixelFast p\x,p\y,rgb
			EndIf
		EndIf
	Next
	UnlockBuffer CanvasBuffer(canvas)
End Function