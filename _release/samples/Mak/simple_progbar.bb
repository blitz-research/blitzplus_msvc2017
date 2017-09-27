
window=CreateWindow( "ProgBar demo",0,0,160,120 )

progbar=CreateProgBar( ClientWidth(window)/2-64,ClientHeight(window)/2-12,128,24,window )

timer=CreateTimer(1)

While WaitEvent()<>$803
	If EventID()=$4001
		If EventData()=11
			Notify "Finished!"
			End
		EndIf
		UpdateProgBar progbar,EventData()/10.0
	EndIf
Wend

End