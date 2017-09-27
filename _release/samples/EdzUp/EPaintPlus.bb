; 
; EPaintPlus.bb - Copyright ©2002 EdzUp 
; Coded by Ed Upton (using code segments by Mark Sibly and simon@acid) 
; 

;create main window 
Window=CreateWindow( "EPaint+",0,0,ClientWidth( Desktop() )-132,ClientHeight( Desktop() ) ) 

Dim VidArray( 1, 1 )

Type ants 
 Field x,y,cycle   ;used for the Fill routine
End Type

;create gadget window 
GadgetWindow = CreateWindow( "Gadgets", ClientWidth( Desktop() )-132, 120, 120, 446, 0, 0 ) 
;create buttons for different gadgets 
Global FreehandButton = CreateButton("Free hand", 0, 0, ClientWidth( GadgetWindow ), 20, GadgetWindow,3 ) 
Global CircleButton = CreateButton("Circle", 0, 20, ClientWidth( GadgetWindow ), 20, GadgetWindow,3 ) 
Global FilledCircleButton = CreateButton("Filled circle", 0, 40, ClientWidth( GadgetWindow ), 20, GadgetWindow,3 ) 
Global LineDrawButton = CreateButton("Line draw", 0, 60, ClientWidth( GadgetWindow ), 20, GadgetWindow,3 ) 
Global SquareButton = CreateButton("Rectangle", 0, 80, ClientWidth( GadgetWindow ), 20, GadgetWindow,3 ) 
Global FilledSquareButton = CreateButton("Filled rectangle", 0, 100, ClientWidth( GadgetWindow ), 20, GadgetWindow,3 ) 
Global PolyButton = CreateButton("Polygon", 0, 120, ClientWidth( GadgetWindow ), 20, GadgetWindow,3 ) 
Global SprayButton = CreateButton("Spraycan", 0, 140, ClientWidth( GadgetWindow ), 20, GadgetWindow,3 ) 
Global FloodfillButton = CreateButton("Flood fill", 0, 160, ClientWidth( GadgetWindow ), 20, GadgetWindow,3 ) 
Global TextButton = CreateButton("Text", 0, 180, ClientWidth( GadgetWindow ), 20, GadgetWindow,3 ) 
Global CutButton = CreateButton("Cut", 0, 200, ClientWidth( GadgetWindow ), 20, GadgetWindow,3 ) 
Global PasteButton = CreateButton("Paste", 0, 220, ClientWidth( GadgetWindow ), 20, GadgetWindow,3 ) 
Global MagnifyButton = CreateButton("Magnify", 0, 240, ClientWidth( GadgetWindow ), 20, GadgetWindow,3 ) 
ActivateGadget FreeHandButton				;so button is selected
ActivateGadget window 						;so main canvas is selected

;setup window menu 
MainMenu = WindowMenu( Window ) 
;layout menu1 
Global Menu1 = CreateMenu( "&File", 0, MainMenu ) 
Global New_Image = CreateMenu( "&New Image", 1, Menu1 ) 
Global Load_Image = CreateMenu( "&Load Image", 2, Menu1 ) 
Global Save_Image = CreateMenu( "&Save Image", 3, Menu1 ) 
Global Exit_EPaint = CreateMenu( "E&xit", 4, Menu1 ) 
;layout menu2 
Global Menu2 = CreateMenu( "&Help", 1, MainMenu ) 
Global About = CreateMenu( "&About", 101, Menu2 ) 
;update main window 
UpdateWindowMenu window 

;create horizontal slider 
hslider=CreateSlider( 0,ClientHeight(window)-16,ClientWidth(window)-16,16,window,1 ) 
SetGadgetLayout hslider,1,1,0,1 

;create vertical slider 
vslider=CreateSlider( ClientWidth(window)-16,0,16,ClientHeight(window)-16,window,2 ) 
SetGadgetLayout vslider,0,1,1,1 

;create panel to clip canvas 
panel=CreatePanel( 0,0,ClientWidth(window)-16,ClientHeight(window)-16,window ) 
SetGadgetLayout panel,1,1,1,1 

;create canvas 
canvas=CreateCanvas( 0,0,GadgetWidth( Panel ),ClientHeight( Panel ),panel ) 
SetGadgetLayout canvas,1,0,1,0 
SetBuffer CanvasBuffer(canvas) 

;Create color selector 
ColorWindow = CreateWindow( "Palette", ClientWidth( Desktop() )-132,0, 120, 120, 0, 0 ) 
ColorPanel=CreatePanel( 0, 0, ClientWidth( ColorWindow ), ClientHeight( ColorWindow )-20, ColorWindow ) 
Global ColorChanger = CreateButton( "Change Color", 0, ClientHeight( ColorWindow )-20, ClientWidth( ColorWindow ), 20, ColorWindow )
SetGadgetLayout ColorPanel,1, 1, ClientWidth( ColorWindow ), ClientHeight( ColorWindow ) 
ColorCanvas = CreateCanvas( 0, 0, ClientWidth( ColorWindow ), ClientHeight( ColorWindow ), ColorPanel )
Global VPer# = 768/GadgetHeight( ColorCanvas )
Global CPR#=0, CPG# = 0, CPB#=0
ActivateGadget ColorWindow
SetBuffer CanvasBuffer( ColorCanvas )
Color 255,255,255
Rect 0, 0, GadgetWidth( ColorCanvas ), GadgetHeight( ColorCanvas )

Global CC=0

FlipCanvas ColorCanvas

Color 255,255,255 

;setup sliders 
SetSliderRange hslider,ClientWidth(panel),GadgetWidth(canvas) 
SetSliderRange vslider,ClientHeight(panel),GadgetHeight(canvas) 

;global variables 
Global Mode=0 ;freehand 
Global PenR=255, PenG=255, PenB=255
Global BeginX, BeginY 
Global UndoBuffer = CreateImage( GadgetWidth( Canvas ), GadgetHeight( Canvas ) ) 
Global BackBuffera = CreateImage( GadgetWidth( Canvas ), GadgetHeight( Canvas ) ) 
Global TempImage =0 

Global LeftMouseDown=0

While WaitEvent()<>$803 
	SetBuffer CanvasBuffer( canvas ) 
	If Mode>0 And Mode<>7
		Cls
		DrawImage BackBuffera, 0, 0 
	EndIf

	Select EventID() 
	Case $401	;gadget action 
				;slider moved! reposition canvas 
				SetGadgetShape canvas,-SliderValue(hslider),-SliderValue(vslider),GadgetWidth(canvas),GadgetHeight(canvas) 

				Select EventSource()
				;Gadget selector buttons 
				Case ColorChanger: RequestColor PenR, PenG, PenB 
						PenR = RequestedRed()
						PenG = RequestedGreen()
						PenB = RequestedBlue()
						SetBuffer CanvasBuffer( ColorCanvas )
						Color PenR, PenG, PenB
						Rect 0, 0, GadgetWidth( ColorCanvas ), GadgetHeight( ColorCanvas )
						FlipCanvas ColorCanvas
						SetBuffer CanvasBuffer( Canvas )
						
				Case FreeHandButton:Mode=0 
				Case CircleButton: Mode=1 
				Case FilledCircleButton: Mode=2 
				Case LineDrawButton: Mode=3 
				Case SquareButton: Mode=4 
				Case FilledSquareButton: Mode=5 
				Case PolyButton: Mode=6 
				Case SprayButton: Mode=7 
				Case FloodFillButton: Mode=8 
				Case TextButton: Mode=9 
				Case CutButton: Mode=10 
				Case PasteButton: Mode=11 
				Case MagnifyButton: Mode=12 
				End Select 

	Case $201 ;mouse down 
				mouse_down=True 
	Case $202 ;mouse up 
				mouse_down=False 
	Case $802 ;mouse move 
				;adjust slider ranges and reposition canvas 
				SetSliderRange hslider,ClientWidth(panel),GadgetWidth(canvas) 
				SetSliderRange vslider,ClientHeight(panel),GadgetHeight(canvas) 
				SetGadgetShape canvas,-SliderValue(hslider),-SliderValue(vslider),GadgetWidth(canvas),GadgetHeight(canvas) 
	Case $1001 ;menu event 
				Select EventData() 
				Case 1 : ;New Image 
						If Confirm( "Delete current image?", True ) 
							SetBuffer( CanvasBuffer( canvas ) ) 
							Cls 
							FlipCanvas canvas 
							Cls 
						EndIf 

				Case 2 : ;Load File 
						;Loading system goes in here 
						FileName$ = RequestFile$( "Load a file", "bmp,jpg,png,gif,iff,tga,pcx" ) 
						TempImage = LoadImage( FileName$ ) 
						FreeGadget Canvas ;this is like EndGraphics 
						Canvas = CreateCanvas( 0, 0, ImageWidth( TempImage ), ImageHeight( TempImage ), Panel ) 
						SetBuffer CanvasBuffer( Canvas ) 
						DrawImage TempImage, 0, 0 
						FreeImage UndoBuffer 
						FreeImage BackBuffera 
						UndoBuffer = CreateImage( GadgetWidth( Canvas ), GadgetHeight( Canvas ) ) 
						BackBuffera = CreateImage( GadgetWidth( Canvas ), GadgetHeight( Canvas ) ) 
						GrabImage BackBuffera, 0, 0 

				Case 3 : ;Save File 
						;saving system goes in here 
						FileName$ = RequestFile$( "Save image", "bmp", True ) 
						If FileName$<>"" Then SaveBuffer( CanvasBuffer( Canvas ), FileName$ )

				Case 4 : If Confirm( "Really quit?", True ) End 
				Case 101: Notify( "EPaint+ Copyright ©2000-2002 EdzUp "+Chr(13)+Chr(13)+"Coded by Ed Upton and Mark Sibly"+Chr(13)+Chr(13)+"TGA saving function coded by simon@acid" ) 
				End Select 
	End Select 

	mx=MouseX(canvas) 
	my=MouseY(canvas)
	Select mode 
	Case 0 : ModeString$ = "[Free hand] - " 
	Case 1 : ModeString$ = "[Circle] - " 
	Case 2 : ModeString$ = "[Filled Circle] - " 
	Case 3 : ModeString$ = "[Line draw] - " 
	Case 4 : ModeString$ = "[Rectangle] - " 
	Case 5 : ModeString$ = "[Filled Rectangle] - " 
	Case 6 : ModeString$ = "[Polygon] - " 
	Case 7 : ModeString$ = "[Spraycan] - " 
	Case 8 : ModeString$ = "[Flood fill] - " 
	Case 9 : ModeString$ = "[Text] - " 
	Case 10: ModeString$ = "[Cut] - " 
	Case 11: ModeString$ = "[Paste] - " 
	Case 12: ModeString$ = "[Magnify] - " 
	End Select 
	ColorP=0
	SetStatusText window,ModeString$+mx+","+my
	If mouse_down
		Color PenR, PenG, PenB
		If mx<ClientWidth( canvas ) And my< ClientHeight( canvas ) And mx>-1 And my>-1
			Select mode 
			Case 0 :Line px,py,mx,my 
			Case 1 :If BeginX=-1 
						BeginX = mx 
						BeginY = my 
					Else 
						Oval BeginX, BeginY, mx-BeginX, my-BeginY, 0 
					EndIf 
			Case 2 :If BeginX=-1 
						BeginX = mx 
						BeginY = my 
					Else 
						Oval BeginX, BeginY, mx-BeginX, my-BeginY 
					EndIf 
			Case 3 :If BeginX=-1 
						BeginX = mx 
						BeginY = my 
					Else 
						Line BeginX, BeginY, mx, my 
					EndIf 
			Case 4 :If BeginX=-1 
						BeginX = mx 
						BeginY = my 
					Else 
						Line BeginX, BeginY, mx, BeginY 
						Line BeginX, BeginY, BeginX, my 
						Line mx, BeginY, mx, my 
						Line BeginX, my, mx, my 
					EndIf 
			Case 5 :If BeginX=-1 
						BeginX = mx 
						BeginY = my 
					Else 
						Rect BeginX, BeginY, mx-BeginX, my-BeginY 
					EndIf 
			Case 7 :For Counter = 0 To 4 
						Plot (mx-Rnd(5))+Rnd(5), (my-Rnd(5))+Rnd(5) 
					Next
			Case 8 :FloodFill( mx, my, PenR, PenG, PenB )
			End Select 
			LeftMouseDown=1
		EndIf
	Else 		
		If BeginX<>-1 
			Color PenR, PenG, PenB 
			Select Mode 
			Case 1 :Oval BeginX, BeginY, mx-BeginX, my-BeginY, 0 
			Case 2 :Oval BeginX, BeginY, mx-BeginX, my-BeginY 
			Case 3 :Line BeginX, BeginY, mx, my 
			Case 4 :Line BeginX, BeginY, mx, BeginY 
					Line BeginX, BeginY, BeginX, my 
					Line mx, BeginY, mx, my 
					Line BeginX, my, mx, my 
			Case 5 :Rect BeginX, BeginY, mx-BeginX, my-BeginY 
			End Select 
			If Mode>0 And Mode<>7
				GrabImage BackBuffera, 0, 0
			EndIf
			BeginX=-1 
		EndIf 
		If mouse_down
			GrabImage BackBuffera, 0, 0
		EndIf
		
	EndIf 
	If mx>-1 And my>-1 And mx<ClientWidth( Canvas ) And my<ClientHeight( Canvas ) Then px=mx:py=my 

	FlipCanvas canvas,0
Wend 
End 

Function createant( x, y, cyc )
	ant.ants=New ants
	ant\x=x
	ant\y=y
	ant\cycle=cyc
	antcount=antcount+1
	vidarray( x, y )=5
End Function

Function capture(r,g,b)
;	Cls

; Color r,g,b
; Rect 0,0,10,10
; GetColor 5,5
; nr=ColorRed()
; ng=ColorGreen()
; nb=ColorBlue()

	Cls
	DrawImage BackBuffera,0,0

	For y=0 To GadgetHeight( Canvas )
		For x=0 To GadgetWidth( Canvas )
			vidarray(x,y)=5
			GetColor x,y
			ok=0
			If ColorRed()>r-5 And ColorRed()<r+5 Then ok=ok+1
			If ColorGreen()>g-5 And ColorGreen()<g+5 Then ok=ok+1
			If ColorBlue()>b-5 And ColorBlue()<b+5 Then ok=ok+1
			If ok=3 Then vidarray(x,y)=0 ;border
		Next
	Next
End Function

Function Error( Er$ )
	RuntimeError Er$
End Function

Function FloodFill(x,y,r,g,b)
	Dim VidArray( GraphicsWidth( ), GraphicsHeight( ) )

	SetBuffer CanvasBuffer( canvas )
	Cls
	DrawImage BackBuffera,0,0
	GetColor x,y
	fr=ColorRed()
	fg=ColorGreen()
	fb=ColorBlue()
	capture(fr,fg,fb)
	;************************************************************************
	ant.ants = New ants
	ant\x=x
	ant\y=y
	ant\cycle=0
	cycl=0
	antcount=1
	Repeat
		ex=0
		For ant.ants=Each ants
			If ant\cycle=cycl
				Color r,g,b
				Plot ant\x,ant\y
				If ant\x>-1 And ant\y>-1 And ant\x<GraphicsWidth() And ant\y<GraphicsHeight()
					If ant\x-1>-1
					If vidarray(ant\x-1,ant\y)=0 Then createant(ant\x-1,ant\y,cycl+1)
				EndIf
				If ant\x+1<GraphicsWidth()
					If vidarray(ant\x+1,ant\y)=0 Then createant(ant\x+1,ant\y,cycl+1) 
				EndIf
				If ant\y-1>-1
					If vidarray(ant\x,ant\y-1)=0 Then createant(ant\x,ant\y-1,cycl+1)
				EndIf
				If ant\y+1<GraphicsHeight()
					If vidarray(ant\x,ant\y+1)=0 Then createant(ant\x,ant\y+1,cycl+1) 
				EndIf
			EndIf  
			antcount=antcount-1
			Delete ant
			If KeyDown(1)=1 Then End
			EndIf
		Next
		cycl=cycl+1
	Until antcount=0
	FlipCanvas Canvas
	GrabImage BackBuffera,0,0
	SetBuffer CanvasBuffer( Canvas )
	
	Dim VidArray( 0, 0 )
End Function