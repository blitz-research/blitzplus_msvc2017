; Thanks to Semar for this example.
; Not a great example because "calc.exe" doesn't produce any output, but...

process$ = "calc.exe"
s = CreateProcess (process)

While Not Eof(s)
	If MilliSecs() - t >= 1000 Then
		DebugLog "process " + process + " running"
		t = MilliSecs()
	EndIf
Wend

DebugLog "process " + process + " terminated"
Stop

End