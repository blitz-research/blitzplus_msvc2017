
window=CreateWindow( "Tabber demo",0,0,512,384 )

tabber=CreateTabber( 0,0,ClientWidth(window),ClientHeight(window),window )
SetGadgetLayout tabber,1,1,1,1

For k=0 To 9
	AddGadgetItem tabber,"Item "+k
Next

panel=CreatePanel( 0,0,ClientWidth(tabber),ClientHeight(tabber),tabber )
SetGadgetLayout panel,1,1,1,1
SetPanelColor panel,0,0,128

While WaitEvent()<>$803
	If EventID()=$401
		If EventSource()=tabber
			Notify "Item selected:"+SelectedGadgetItem(tabber)
		EndIf
	EndIf
Wend

End