; Create a window and some labels
win=CreateWindow("Test Labels",100,100,200,100,0,49)
tab=CreateTabber(0,0,200,100,win)
; get bitmap icon from the Blitz directory 
appdir$=SystemProperty("appdir") 
blitzdir$=Left(appdir,Len(appdir)-5) 
icons=LoadIconStrip(blitzdir$+"\cfg\dbg_toolbar.bmp") 
SetGadgetIconStrip tab,icons
InsertGadgetItem(tab,0,"Tab 1",0)
InsertGadgetItem(tab,1,"Tab 2",1)

; The simplest event loop possible!
Repeat
Until WaitEvent()=$803
End ; bye!