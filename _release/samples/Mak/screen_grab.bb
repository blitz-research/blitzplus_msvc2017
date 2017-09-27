
file$=RequestFile$( "Select filename to save as...","bmp",True )

If file$<>""
	If Not SaveBuffer( DesktopBuffer(),file$ )
		RuntimeError "Error saving file"
	EndIf
EndIf

End