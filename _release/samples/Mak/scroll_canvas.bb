
;create main window
window=CreateWindow( "Scrolly canvas - use mouse to paint",0,0,400,300 )

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
canvas=CreateCanvas( 0,0,640,480,panel )
SetGadgetLayout canvas,1,0,1,0
SetBuffer CanvasBuffer(canvas)

;draw a simple border
Color 255,0,0
Rect 0,0,GadgetWidth(canvas),8
Rect 0,0,8,GadgetHeight(canvas)
Rect GadgetWidth(canvas)-8,0,8,GadgetHeight(canvas)
Rect 0,GadgetHeight(canvas)-8,GadgetWidth(canvas),8

Color 255,255,255

;setup sliders
SetSliderRange hslider,ClientWidth(panel),GadgetWidth(canvas)
SetSliderRange vslider,ClientHeight(panel),GadgetHeight(canvas)

While WaitEvent()<>$803

	Select EventID()
	Case $401	;gadget action
		;slider moved! reposition canvas
		SetGadgetShape canvas,-SliderValue(hslider),-SliderValue(vslider),GadgetWidth(canvas),GadgetHeight(canvas)
	Case $201	;mouse down
		mouse_down=True
	Case $202	;mouse up
		mouse_down=False
	Case $802	;mouse move
		;adjust slider ranges and reposition canvas
		SetSliderRange hslider,ClientWidth(panel),GadgetWidth(canvas)
		SetSliderRange vslider,ClientHeight(panel),GadgetHeight(canvas)
		SetGadgetShape canvas,-SliderValue(hslider),-SliderValue(vslider),GadgetWidth(canvas),GadgetHeight(canvas)
	End Select
	
	mx=MouseX(canvas)
	my=MouseY(canvas)
	
	SetStatusText window,mx+","+my
	If mouse_down
		Line px,py,mx,my
	EndIf
	px=mx:py=my
	
	FlipCanvas canvas
	
Wend

End