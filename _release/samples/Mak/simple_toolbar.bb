
window=CreateWindow( "ToolBar sample",0,0,640,480 )

;toolbar items must be square!
;also, width and height parameters are currently ignored,
;as the toolbar auto-sizes to fit the image.
toolbar=CreateToolBar( "toolbar.bmp",0,0,0,0,window )

;set toolbar tips
SetToolBarTips toolbar,"New,Open,Save,Close,Cut,Copy,Paste,Find,Run,Home,Back,Forward"

panel=CreatePanel( 0,GadgetHeight(toolbar),ClientWidth(window),ClientHeight(window)-GadgetHeight(toolbar),window )
SetGadgetLayout panel,1,1,1,1
SetPanelColor panel,128,128,128

While WaitEvent()<>$803

	If EventID()=$401
		If EventSource()=toolbar
			Notify "ToolBar item "+EventData()+" hit!"
		EndIf
	EndIf

Wend

End