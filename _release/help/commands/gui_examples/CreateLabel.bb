; Create a window and some labels
win=CreateWindow("Test Labels",100,100,200,150,0,17)
CreateLabel("A normal label (style=0)",10,10,180,20,win,0)
CreateLabel("A Flat label (style=1)",10,40,180,20,win,1)
CreateLabel("Another 'normal' label (style=2)",10,70,180,20,win,2)
CreateLabel("A 3d sunken border (style=3)",10,100,180,20,win,3)

; The simplest event loop possible!
Repeat
Until WaitEvent()=$803
End ; bye!