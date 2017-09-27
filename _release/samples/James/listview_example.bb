
; Quick listview thing...

Const EVENT_Gadget		= $401		; Gadget clicked
Const EVENT_Close		= $803		; Window closed

Type File
	Field filename$
End Type

window = CreateWindow ("List o' files...", 320, 200, 640, 480, 0, 1 + 2 + 8)

box = CreateListBox (0, 0, ClientWidth (window), ClientHeight (window), window)
SetGadgetLayout box, 1, 1, 1, 1
DisableGadget box

pic$ = RequestDir ("Select a folder to sort and list...")
If pic$ = "" Then Notify ("You cancelled the request!"): End

dir = ReadDir (pic$)

; Create a list of .File entries from the selected folder...

Repeat
	f$ = NextFile (dir)
	If (f$ = ".") Or (f$ = "..")
	Else
		p.File = New File
		p\filename = f$
	EndIf
Until Len (f$) = 0
Delete p

; Sort 'em...

SortFiles ()

; Add each one in the list to the listview...

For p.File = Each File
	AddGadgetItem (box, p\filename)
Next

EnableGadget box

Repeat
	e = WaitEvent ()
	If e = EVENT_Gadget
		; Retrieve filename from listview...
		f$ = GadgetItemText (box, SelectedGadgetItem (box))
		Notify "You selected " + f$
	EndIf
Until e = EVENT_Close

End

Function SortFiles ()
	; Modified from Floyd's code...
	nextitem.File = After First File
	While nextitem <> Null
		item.File = nextitem
		nextitem = After item
		p.File = item
		Repeat
			q.File = Before p
			If q = Null Then Exit
			If Lower (item\filename) >= Lower (q\filename) Then Exit
			p = q
		Forever
		q = item
		Insert q Before p
	Wend
End Function

