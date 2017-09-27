; Calendar Functions
; Written By Paul Snart (Snarty)
; Pacific Software
;

Dim Cgrid(5,7,1)

Global NormalFont=LoadFont("Tahoma",24,True)
Global TitleFont=LoadFont("Tahoma",30,True)
Global SmallFont=LoadFont("Tahoma",18,True)
Global cld=LoadImage("Media\cdl.png")

Function DrawCalendar()

	SetBuffer CanvasBuffer(DateArea)
	DrawBlock cld,12,12
	;DrawImage cldnav,175,344
	SetFont TitleFont
	ShadowText 0,15,DateW,0,AMonth$(ADate(0,3))+" "+ADate(0,4),255,255,0
	Color 0,0,0
	Rect (DateW-244)/2,50,244,5,1
	StDate$="01 "+Mid$(AMonth$(ADate(0,3)),1,3)+" "+ADate(0,4)
	StN%=ConvertDate(StDate,0)
	StDay%=Day(StN,0)
	If CheckLeap(ADate(0,4))=True And ADate(0,3)=2 Add=1 Else Add=0
	EnDate$=(Months(ADate(0,3))+add)+" "+Mid$(AMonth$(ADate(0,3)),1,3)+" "+ADate(0,4)
	EnN%=ConvertDate(EnDate,0)
	EnDay%=Day(EnN,0)
	For W=1 To 5
		For D=1 To 7
			Cgrid(W,D,0)=0
			Cgrid(W,D,1)=0
		Next
	Next
	W=1:D=StDay
	For dt=1 To Months(ADate(0,3))+Add
		Cgrid(W,D,0)=dt
		If ReadFile("Data\"+(StN+(dt-1))+".DAT")<>0
			Cgrid(W,D,1)=True
		EndIf
		D=D+1
		If D=8
			D=1
			W=W+1
			If W=6 W=1
		EndIf
	Next
	SetFont NormalFont
	For D=1 To 7
		For W=0 To 5
			If D>5
				Color 255,0,0
			Else
				Color 0,0,0
			EndIf
			If W=0
				SetFont NormalFont
				Text 25+((D-1)*36)+18,60+(W*36)+14,Mid$(ADay$(D),1,1),True,True
			Else
				SetFont SmallFont
				If Cgrid(W,D,0)<>0
					Text 25+((D-1)*36)+18,60+(W*36)+18,Cgrid(W,D,0),True,True
				EndIf
			EndIf
			If Cgrid(W,D,0)=Adate(2,2) And ADate(0,3)=ADate(2,3) And ADate(0,4)=ADate(2,4)
				Color 0,0,255
				Rect 25+((D-1)*36),60+(W*36),34,34,0
			EndIf
			If Cgrid(W,D,1)=True
				DrawImage note,196+((D-1)*36),116+(W*36)
			EndIf
			If Cgrid(W,D,0)=ADate(1,2) And ADate(1,3)=ADate(0,3) And ADate(1,4)=ADate(0,4)
				retpos=(W Shl 8)+D
			EndIf
		Next
	Next
	
	Return retpos

End Function

Function ShadowText(x,y,w,h,txt$,r,g,b)

	If w=False 
		cx=False
		xp=x
	Else
		cx=True
		xp=x+(w/2)
	EndIf
	If h=False
		cy=False
		yp=y
	Else
		cy=True
		yp=y+(h/2)
	EndIf
	Color 0,0,0
	Text xp,yp,txt,cx,cy
	Text xp-2,yp,txt,cx,cy
	Text xp-2,yp-2,txt,cx,cy
	Text xp,yp-2,txt,cx,cy
	Color r,g,b
	Text xp-1,yp-1,txt,cx,cy

End Function