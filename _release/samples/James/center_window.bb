
; Convenience function to open a centered window on the desktop...

Function CenterWindow (title$, width, height, group = 0, style = 15)
	Return CreateWindow (title$, (ClientWidth (Desktop ()) / 2) - (width / 2), (ClientHeight (Desktop ()) / 2) - (height / 2), width, height, group, style)
End Function

window = CenterWindow ("I'm centered!", 640, 480)

Repeat
Until WaitEvent () = $803

End