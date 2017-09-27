
window=CreateWindow( "ComboBox demo",0,0,160,120 )

combobox=CreateComboBox( ClientWidth(window)/2-64,ClientHeight(window)/2-12,128,24,window )

For k=0 To 9
AddGadgetItem combobox,"Item "+k
Next

SelectGadgetItem combobox,0

While WaitEvent()<>$803
	If EventID()=$401
		If EventSource()=combobox
			Notify "Item selected:"+SelectedGadgetItem(combobox)
		EndIf
	EndIf
Wend

End