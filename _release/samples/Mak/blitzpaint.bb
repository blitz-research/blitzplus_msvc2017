
;size of graphics
Global gfx_width=640,gfx_height=480

;drawing primtives
Const DRAW_FREEHAND=1,DRAW_CONTINUOUS=2
Const DRAW_LINE=3,DRAW_RECT=4,DRAW_OVAL=5

Global draw_mode=DRAW_CONTINUOUS
Global draw_x1,draw_y1,draw_x2,draw_y2
Global draw_solid=True;False

Global mag_zoom=12
Global mag_on,mag_coords,sel_mag

Global mag_x,mag_y,mag_w,mag_h

Global mag_vx,mag_vy,mag_vw,mag_vh,mag_ox,mag_oy

;create main window
x=GadgetWidth(Desktop())/2-320
y=GadgetHeight(Desktop())/2-240

window=CreateWindow( "BlitzPaint!",x,y,640,480,Desktop(),32+7 )

SetMinWindowSize window,320,240

file_menu=CreateMenu( "File",0,WindowMenu(window) )
	CreateMenu "New",1,file_menu
	CreateMenu "Open",2,file_menu
	CreateMenu "Save",3,file_menu
	CreateMenu "Save As",4,file_menu
	CreateMenu "",0,file_menu
	CreateMenu "Quit",99,file_menu
	
UpdateWindowMenu window

;create horizontal slider
hslider=CreateSlider( 0,ClientHeight(window)-16,ClientWidth(window)-80,16,window,1 )
SetGadgetLayout hslider,1,1,0,1

;create vertical slider
vslider=CreateSlider( ClientWidth(window)-80,0,16,ClientHeight(window)-16,window,2 )
SetGadgetLayout vslider,0,1,1,1

;create panel to clip canvas
Global panel=CreatePanel( 0,0,ClientWidth(window)-80,ClientHeight(window)-16,window )
SetGadgetLayout panel,1,1,1,1
SetPanelColor panel,128,128,128

;color panel
Global fg_color=CreatePanel( ClientWidth(window)-64,0,32,32,window )
SetPanelColor fg_color,255,255,255

Global bg_color=CreatePanel( ClientWidth(window)-32,0,32,32,window )
SetPanelColor bg_color,0,0,0

;setup sliders
SetSliderRange hslider,ClientWidth(panel),gfx_width
SetSliderRange vslider,ClientHeight(panel),gfx_height

;create image
Global gfx_image=CreateImage( gfx_width,gfx_height )

;magnify image
Global mag_image=CreateImage( gfx_width,gfx_height )

;create canvas
Global canvas=CreateCanvas( 0,0,gfx_width,gfx_height,panel )
SetGadgetLayout canvas,1,0,1,0
SetBuffer CanvasBuffer(canvas)

RethinkMag()

Repeat

	refresh=0
	
	Viewport 0,0,gfx_width-mag_ox,gfx_height
	
	Select WaitEvent()
	Case $103	;key down
		If MouseDown(1)
		Else
			Select Chr$(EventData())
			Case "="
				If mag_on Or sel_mag
					mag_zoom=mag_zoom+1
					RethinkMag()
					refresh=1
				EndIf
			Case "-"
				If mag_on Or sel_mag
					mag_zoom=mag_zoom-1
					RethinkMag()
					refresh=1
				EndIf
			Case "f"
				draw_mode=DRAW_FREEHAND
			Case "c"
				draw_mode=DRAW_CONTINUOUS
			Case "l"
				draw_mode=DRAW_LINE
			Case "r"
				draw_mode=DRAW_RECT
			Case "o"
				draw_mode=DRAW_OVAL				
			Case "m"
				If mag_on Or sel_mag
					mag_on=False
					sel_mag=False
					RethinkMag()
					refresh=1
				Else
					sel_mag=True
					mag_x=MouseX(canvas)-mag_w/2
					mag_y=MouseY(canvas)-mag_h/2
					RethinkMag()
					refresh=1
				EndIf
			End Select
		EndIf
	Case $201	;mouse down
		Select EventSource()
		Case canvas
			If sel_mag
			Else
				mag_coords=False
				draw_x1=MouseX(canvas)
				draw_y1=MouseY(canvas)
				If mag_on
					If draw_x1>=mag_vx
						draw_x1=(draw_x1-mag_vx)/mag_zoom+mag_x-mag_ox
						draw_y1=(draw_y1-mag_vy)/mag_zoom+mag_y
						mag_coords=True
					EndIf
				EndIf
				draw_x2=draw_x1
				draw_y2=draw_y1
				Select draw_mode
				Case DRAW_FREEHAND,DRAW_CONTINUOUS
					Plot draw_x1,draw_y1
					SetBuffer ImageBuffer(gfx_image)
					Plot draw_x1,draw_y1
					SetBuffer CanvasBuffer( canvas )
					refresh=2
				End Select
			EndIf
		End Select
	Case $202	;mouse up
		Select EventSource()
		Case canvas
			If sel_mag
				mag_on=True
				sel_mag=False
				RethinkMag()
				refresh=1
			Else
				Select draw_mode
				Case DRAW_FREEHAND,DRAWCONTINUOUS
				Default
					SetBuffer ImageBuffer( gfx_image )
					DrawPrimitive()
					SetBuffer CanvasBuffer( canvas )
					refresh=1
				End Select
			EndIf
		End Select
	Case $203	;mouse move
		Select EventSource()
		Case canvas
			If sel_mag
				mag_x=MouseX(canvas)-mag_w/2
				mag_y=MouseY(canvas)-mag_h/2
				If mag_x<0 mag_x=0 Else If mag_x+mag_w>gfx_width mag_x=gfx_width-mag_w
				If mag_y<0 mag_y=0 Else If mag_y+mag_h>gfx_height mag_y=gfx_height-mag_h
				refresh=1
			Else
				If MouseDown(1)
					mx=MouseX(canvas)
					my=MouseY(canvas)
					If mag_coords
						mx=(mx-mag_vx)/mag_zoom+mag_x-mag_ox
						my=(my-mag_vy)/mag_zoom+mag_y
					EndIf
					Select draw_mode
					Case DRAW_FREEHAND
						Plot mx,my
						SetBuffer ImageBuffer( gfx_image )
						Plot mx,my
						SetBuffer CanvasBuffer( canvas )
						refresh=2
					Case DRAW_CONTINUOUS
						Line draw_x1,draw_y1,mx,my
						SetBuffer ImageBuffer( gfx_image )
						Line draw_x1,draw_y1,mx,my
						SetBuffer CanvasBuffer( canvas )
						draw_x1=mx
						draw_y1=my
						refresh=2
					Default
						draw_x2=mx
						draw_y2=my
						DrawBlock gfx_image,0,0
						DrawPrimitive()
						refresh=2
					End Select
				EndIf
			EndIf
		End Select
	Case $401	;gadget action
		Select EventSource()
		Case hslider,vslider
			;slider moved! reposition canvas
			SetGadgetShape canvas,-SliderValue(hslider),-SliderValue(vslider),GadgetWidth(canvas),GadgetHeight(canvas)
			If mag_on
				RethinkMag()
				refresh=1
			EndIf
		End Select
	Case $802	;window size
		Select EventSource()
		Case window
			;adjust slider ranges and reposition canvas
			SetSliderRange hslider,ClientWidth(panel),GadgetWidth(canvas)
			SetSliderRange vslider,ClientHeight(panel),GadgetHeight(canvas)
			SetGadgetShape canvas,-SliderValue(hslider),-SliderValue(vslider),GadgetWidth(canvas),GadgetHeight(canvas)
			If mag_on
				RethinkMag()
				refresh=1
			EndIf
		End Select
	Case $803	;window close
		End
	Case $1001	;menu action
		Select EventData()
		Case 1	;new
		Case 2	;open
		Case 3	;save
		Case 4	;save as
		Case 99	;quit
		End Select
	End Select
	
	Viewport 0,0,gfx_width,gfx_height
	
	If refresh=1
		ClsColor 128,128,128
		Cls
		DrawBlock gfx_image,0,0
	EndIf
	
	If refresh
		If sel_mag
			Rect mag_x,mag_y,mag_w,mag_h,False
		Else If mag_on
			RefreshMag()
		EndIf
		FlipCanvas canvas
	EndIf

Forever

End

Function RethinkMag()

	w1=ClientWidth(panel)
	w2=gfx_width+GadgetX(canvas)
	min_w=w1
	If w2<min_w min_w=w2
	
	mag_vw=min_w/2
	mag_vh=ClientHeight(panel)
	
	mag_vx=min_w-mag_vw-GadgetX(canvas)
	mag_vy=ClientHeight(panel)-mag_vh-GadgetY(canvas)
	
	mag_w=(mag_vw+mag_zoom-1)/mag_zoom
	mag_h=(mag_vh+mag_zoom-1)/mag_zoom
	
	If mag_on
		mag_ox=(mag_x+mag_w/2)-(mag_vx-mag_vw/2)
		If mag_ox<0 mag_ox=0
	Else
		mag_ox=0
	EndIf
		
	buf=SetBuffer( ImageBuffer(gfx_image) )
	Origin mag_ox,0
	HandleImage gfx_image,mag_ox,0
	SetBuffer buf
	
End Function

Function RefreshMag()

	buf=ImageBuffer(mag_image)
	
	CopyRect mag_x-mag_ox,mag_y,mag_w,mag_h,0,0,src,buf
	
	LockBuffer buf
	
	dy=mag_vy
	For y=0 To mag_h-1
		dx=mag_vx
		For x=0 To mag_w-1
			pixel=ReadPixelFast( x,y,buf )
			Color pixel Shr 16 And 255,pixel Shr 8 And 255,pixel And 255
			Rect dx,dy,mag_zoom,mag_zoom
			dx=dx+mag_zoom
		Next
		dy=dy+mag_zoom
	Next
	
	UnlockBuffer buf
	
	Color 255,255,255
	Rect mag_vx-1,mag_vy,1,mag_vh
	
End Function


Function DrawPrimitive()

	If draw_mode=DRAW_LINE
		Line draw_x1,draw_y1,draw_x2,draw_y2
		Return
	EndIf
	
	Local x1=draw_x1,y1=draw_y1
	Local x2=draw_x2,y2=draw_y2
	
	If x2<x1 tx=x1:x1=x2:x2=tx
	If y2<y1 ty=y1:y1=y2:y2=ty
	
	Select draw_mode
	Case DRAW_RECT
		Rect x1,y1,x2-x1+1,y2-y1+1,draw_solid
	Case DRAW_OVAL
		Oval x1,y1,x2-x1+1,y2-y1+1,draw_solid
	End Select
	
End Function