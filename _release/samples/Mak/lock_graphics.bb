
Const FORMAT_RGB565=1
Const FORMAT_XRGB1555=2
Const FORMAT_RGB888=3
Const FORMAT_XRGB8888=4

Graphics 640,480,16,1	;fullscreen!

timer=CreateTimer(60)

Repeat

	Select WaitEvent()
	
	Case $103	;key down
		If EventData()=27 End
		
	Case $4001	;timer tick
	
		tm=MilliSecs()
		LockBuffer
		bank=LockedPixels()
		
		For y=0 To 479
			offset=y*LockedPitch()
		
			Select LockedFormat()
			Case FORMAT_RGB565
				For x=0 To 319
					PokeInt bank,offset+x*4,$f800f800
				Next
			Case FORMAT_XRGB1555
				For x=0 To 319
					PokeInt bank,offset+x*4,$7c007c00
				Next
			Case FORMAT_RGB888
				For x=0 To 639
					PokeInt bank,offset+x*4,$00ff0000
				Next
			Case FORMAT_XRGB8888
				For x=0 To 639
					PokeInt bank,offset+x*4,$00ff0000
				Next
			End Select
		Next
		
		UnlockBuffer
		tm=MilliSecs()-tm
		
		Text 0,0,tm
		Flip
	
	End Select
	
Forever

End