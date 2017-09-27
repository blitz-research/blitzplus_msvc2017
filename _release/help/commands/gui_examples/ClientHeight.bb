; Example provided by Mark Tiffany

; create a resizable window
win=CreateWindow("test",0,0,100,100,0,11)

; and set the status text
SetStatusText win,"Resize the window!"

; a small event loop
Repeat
   id = WaitEvent()
   Select id
      Case $803 ; exit on window close event
         Exit
			
      Case $802 ; update the status on window resize
         SetStatusText win,ClientWidth(win) + " x " + ClientHeight(win)
			
   End Select
Forever

End ; bye!