
If CountGfxModes()=0 RuntimeError "No fullscreen gfx modes detected"

Global window,combo,button

CreateMainWindow()

While WaitEvent()<>$803

	If EventID()=$401 And EventSource()=button
	
		TestMode()
			
	EndIf

Wend

End

Function TestMode()
	mode=SelectedGadgetItem(combo)+1
	
	FreeGadget window
	
	Graphics GfxModeWidth(mode),GfxModeHeight(mode),GfxModeDepth(mode),1
	
	ClsColor 0,0,128
	Cls
	
	Text 0,0,"Mode:"+GfxModeWidth(mode)+"x"+GfxModeHeight(mode)+"x"+GfxModeDepth(mode)
	Text 0,FontHeight(),"Click mouse to exit..."
	
	Flip
	
	While WaitEvent()<>$201
	Wend
	
	EndGraphics
	
	CreateMainWindow()
	
End Function

Function CreateMainWindow()

	window=CreateWindow( "Graphics modes",ClientWidth(Desktop())/2-160,ClientHeight(Desktop())/2-120,320,240,0,1 )
	
	combo=CreateComboBox( 16,64,ClientWidth(window)-32,24,window )
	
	button=CreateButton( "Test mode",ClientWidth(window)/2-64,ClientHeight(window)-32,128,24,window )
	
	For k=1 To CountGfxModes()
		t$=GfxModeWidth(k)+"x"+GfxModeHeight(k)+"x"+GfxModeDepth(k)
		AddGadgetItem combo,t$
	Next
	
	SelectGadgetItem combo,0
	
End Function