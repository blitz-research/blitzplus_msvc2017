; BlitzALERT
; Written By Paul Snart (Snarty)
; Pacific Software
;

AutoSuspend False

Global DWidth=ClientWidth(Desktop())
Global DHeight=ClientHeight(Desktop())

Global WinW=640
Global WinH=480
Global WinX=(DWidth-WinW)/2
Global WinY=(DHeight-WinH)/2

Global MainWin=CreateWindow("BlitzALERT",WinX,WinY,WinW,WinH,0,%1001)
SetMinWindowSize MainWin
ActivateWindow MainWin

Global Close=CreateButton("Close",ClientWidth(MainWin)-68,ClientHeight(MainWin)-28,64,24,MainWin) 
Global Hide=CreateButton("Hide",ClientWidth(MainWin)-136,ClientHeight(MainWin)-28,64,24,MainWin)

Global Timer=CreateTimer(1)

Global Tabber=CreateTabber(4,4,ClientWidth(MainWin)-8,ClientHeight(MainWin)-36,MainWin)
SetGadgetLayout Tabber,2,2,2,2
AddGadgetItem Tabber,"Date/Time"
AddGadgetItem Tabber,"Agenda"

Include "Includes\NDate.bb"
Include "Includes\Calendar.bb"

Global DigiFont=LoadAnimImage("Media\digifont.png",32,64,0,10)

SetStatusText MainWin,"Todays Date: "+ADay(ADate(2,1))+", "+nth(ADate(2,2))+" "+AMonth(ADate(2,3))+" "+ADate(2,4)

; Date & Time Layout
Global DTGroup=CreatePanel(4,4,ClientWidth(Tabber)-8,ClientHeight(Tabber)-8,Tabber,%1)
Global DateW=(ClientWidth(DTGroup)/2)-8,DateH=ClientHeight(DTGroup)-64
Global DateArea=CreateCanvas(6,4,DateW,DateH,DTGroup)
Global TimeW=(ClientWidth(DTGroup)/2)-8,TimeH=ClientHeight(DTGroup)-8
Global TimeArea=CreateCanvas((ClientWidth(DTGroup)/2)+2,4,TimeW,TimeH,DTGroup)
Global ClockImage=CreateImage(TimeW,240)
CreateClock
DrawCalendar

Global SelMonth=CreateComboBox(6,DateH+6,150,0,DTGroup)
For t=1 To 12
	If t=ADate(2,3) Sel=1 Else Sel=0
	AddGadgetItem SelMonth,AMonth(t),Sel
Next
Global SelYear=CreateComboBox(160,DateH+6,144,0,DTGroup)
For t=0 To 9
	If t=0 Sel=1 Else Sel=0
	AddGadgetItem SelYear,(ADate(2,4)+t),Sel
Next

Global Alarm=CreateButton("Alarms",DateW-60,ClientHeight(DTGroup)-28,64,24,DTGroup)
DisableGadget Alarm
Global Plan=CreateButton("Planner",DateW-128,ClientHeight(DTGroup)-28,64,24,DTGroup)
DisableGadget Plan

; Agenda Listings
Global AgGroup=CreatePanel(4,4,ClientWidth(Tabber)-8,ClientHeight(Tabber)-8,Tabber,%1)

While WaitEvent()

	Select EventID()
	
		Case $401
			HandleGadgets
				
		Case $803
			FreeTimer Timer
			End
						
		Default
			If EventSource()=Timer
				HandleTimeDate
				FlipCanvas DateArea
				FlipCanvas TimeArea
			EndIf
		
	End Select

Wend

Function HandleGadgets()

	Select EventSource()
				
		Case Tabber
			If SelectedGadgetItem(Tabber)=0
				ShowGadget DTGroup
				HideGadget AgGroup
			ElseIf SelectedGadgetItem(Tabber)=1
				HideGadget DTGroup
				ShowGadget AgGroup
			EndIf
			
		Case Hide
			MinimizeWindow MainWin
			
		Case Close
			FreeTimer Timer
			End

		Case SelMonth
			ODate=ADate(0,3)
			ADate(0,3)=SelectedGadgetItem(SelMonth)+1
			If ODate<>ADate(0,3)
				DrawCalendar
				FlipCanvas DateArea
			EndIf
			
		Case SelYear
			ODate=ADate(0,4)
			ADate(0,4)=SelectedGadgetItem(SelYear)+ADate(2,4)
			If ODate<>ADate(0,4)
				DrawCalendar
				FlipCanvas DateArea
			EndIf
		
	End Select

End Function

Function HandleTimeDate()

	SetBuffer CanvasBuffer(TimeArea)
	Time$=CurrentTime()
	Hour=Left(Time,2)
	If Hour>12 SHour=Hour-12 Else SHour=Hour
	Min=Mid(Time,4,2)
	Secs=Right(Time,2)
	UpdateClocks SHour,Min,Secs,Time

End Function

Function SLine(stx#,sty#,enx#,eny#,r,g,b,Buffer)

	mvx#=Stx-enx:mvy#=sty-eny
	If mvx<0 mvx=-mvx
	If mvy<0 mvy=-mvy
	If mvy>mvx mv#=mvy Else mv#=mvx
	stpx#=(mvx/mv):If Stx>enx stpx=-stpx
	stpy#=(mvy/mv):If Sty>eny stpy=-stpy
	LockBuffer Buffer
	Col=ConvertRGB(r,g,b)
	For nc=0 To Floor(mv)
		WritePixelFast stx,sty,Col
		stx=stx+stpx:sty=sty+stpy
	Next		
	UnlockBuffer Buffer

End Function

Function ConvertRGB(r,g,b)

	Col=r Shl 16
	Col=Col+g Shl 8
	Col=Col+b
	Return Col

End Function

Function UpdateClocks(Hour#,Min#,Secs#,Time$)

	SetBuffer CanvasBuffer(TimeArea)
	DrawBlock ClockImage,0,0
	CClock=TimeW/2
	SLine CClock,120,CClock+(Sin((Hour*30)+(Min/2))*50),120-(Cos((Hour*30)+(Min/2))*50),0,200,0,CanvasBuffer(TimeArea)
	SLine CClock,120,CClock+(Sin((Min*6)+(Secs/10))*70),120-(Cos((Min*6)+(Secs/10))*70),200,0,0,CanvasBuffer(TimeArea)
	SLine CClock,120,CClock+(Sin(Secs*6)*90),120-(Cos(Secs*6)*90),200,200,200,CanvasBuffer(TimeArea)
	Off=70
	DrawBlock DigiFont,Off+0,260,Asc(Mid(Time,1,1))-48
	DrawBlock DigiFont,Off+36,260,Asc(Mid(Time,2,1))-48
	If Secs Mod 2
		Color 0,100,255
	Else
		Color 0,0,0
	EndIf
	Oval Off+74,282,6,6,1
	Oval Off+74,298,6,6,1
	DrawBlock DigiFont,Off+86,260,Asc(Mid(Time,4,1))-48
	DrawBlock DigiFont,Off+122,260,Asc(Mid(Time,5,1))-48
	FlipCanvas TimeArea

End Function

Function CreateClock()

	SetBuffer ImageBuffer(ClockImage)
	For Deg#=0 To 359 Step 6
		If Deg Mod 90 = 0
			Color 0,100,255
			Size=8
		ElseIf Deg Mod 30 = 0
			Color 0,100,255
			Size=4
		Else
			Color 180,180,180	
			Size=1
		EndIf
		Oval Floor((TimeW/2)+(Cos(Deg)*100)-(Size/2)),120+(Sin(Deg)*100)-(Size/2),Size,Size,1
	Next
	HandleTimeDate

End Function