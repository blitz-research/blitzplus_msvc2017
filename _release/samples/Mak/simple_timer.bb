
window=CreateWindow( "Timer demo",0,0,160,120 )

label=CreateLabel( "",ClientWidth(window)/2-64,ClientHeight(window)/2-12,128,24,window )

timer=CreateTimer( 10 )

While WaitEvent(1000)<>$803
	If EventID()=$4001
		SetGadgetText label,EventData()
	EndIf
Wend

End