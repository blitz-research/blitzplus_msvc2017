; create a window
win=CreateWindow("Move the mouse over the window",100,100,300,200,0,9)
cnv=CreateCanvas(50,30,200,100,win)

; and set the status text
SetStatusText win,"Move the mouse over the window"

; a small event loop
Repeat

   id = WaitEvent()

   Select id
      Case $803 ; exit on window close event
         Exit		

      Case $203 ; update the status on gadget action
         SetStatusText win,EventX() + " x " + EventY()	

   End Select

Forever

End ; bye!