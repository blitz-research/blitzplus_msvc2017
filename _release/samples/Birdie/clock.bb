Global Desk_Width=ClientWidth(Desktop())
Global Desk_Height=ClientHeight(Desktop())
winwid=200
winhgt=230
Global Main_Window=CreateWindow("BlitzPlus Clock",(Desk_Width-winwid)/2,(Desk_Height-winhgt)/2,winwid,winhgt,0,7)
Global Main_Canvas=CreateCanvas(0,0,ClientWidth(Main_Window),ClientHeight(Main_Window),main_Window)
SetBuffer CanvasBuffer(Main_Canvas)
menu=WindowMenu(Main_window)
file=CreateMenu("&File",-1,menu)
CreateMenu("E&xit",100,file)
UpdateWindowMenu main_window

timer=CreateTimer(120)
Repeat
	Select WaitEvent()
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
		Case $2001	;suspend!
			While WaitEvent()<>$2002
			Wend
		Case $4001
			Draw_Dial
			FlipCanvas Main_Canvas
			FlushEvents $4001
	End Select
Forever
End

Function Draw_Dial()
	Cls
	w=ClientWidth(Main_Canvas)
	h=ClientHeight(Main_Canvas)
	x=w/2
	y=h/2
	Oval 0,0,w,h,0
	For a=0 To 355 Step 6
		;5 mins
		If b=0 Then 
			Line x+(x*.80)*Cos(a),y+(y*.80)*Sin(a),x+(x*.95)*Cos(a),y+(y*.95)*Sin(a)
			b=b+1
		Else
			b=b+1
			If b=5 Then b=0
			;seconds/mins
			Line x+(x*.90)*Cos(a),y+(y*.90)*Sin(a),x+(x*.95)*Cos(a),y+(y*.95)*Sin(a)
		EndIf
	Next
	Local get$[2]
	b=0
	t$=CurrentTime$()
	For a=1 To Len(t$)
		If Mid$(t$,a,1)=":" Then
			b=b+1
		Else
			get[b]=get[b]+Mid$(t$,a,1)
		EndIf
	Next
	Plot x,y
	;Draw Seconds Hand
		sec=get[2]
		Line x+(x*.01)*Cos(-90+(sec*6)),y+(y*.01)*Sin(-90+(sec*6)),x+(x*.95)*Cos(-90+(sec*6)),y+(y*.95)*Sin(-90+(sec*6))
	;Draw Minute Hand
		min#=get[1]
		min=min+(Float(sec)/Float(60))
		Line x+(x*.01)*Cos(-90+(min*6)),y+(y*.01)*Sin(-90+(min*6)),x+(x*.80)*Cos(-90+(min*6)),y+(y*.80)*Sin(-90+(min*6))
	;Draw Hour
		hour#=get[0]
		hour=hour+(min/Float(60))
		Line x+(x*.01)*Cos(-90+(hour*Float(30))),y+(y*.01)*Sin(-90+(hour*Float(30))),x+(x*.60)*Cos(-90+(hour*Float(30))),y+(y*.60)*Sin(-90+(hour*Float(30)))
	Text x,y+20,CurrentTime$(),1,1	
End Function

