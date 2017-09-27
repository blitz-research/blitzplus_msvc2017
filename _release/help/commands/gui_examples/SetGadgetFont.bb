; load some fonts
fntArial12 = LoadFont("Arial",12)
fntComicSans16BoldItalic = LoadFont("Comic Sans",16,True,True,False)
fntTahoma18Underline = LoadFont("Tahoma",18,False,False,True)

; create a basic window with some gadgets to play with
win=CreateWindow("SetGadgetFont Example",100,100,200,110,0,49)
label=CreateLabel("Arial 12",10,10,180,20,win)
button=CreateButton("Comic Sans 16 Bold Italic",10,40,180,20,win)
txt=CreateTextField(10,70,180,30,win)
SetGadgetText txt,"Tahoma 18 Underline"

; set the gadget fonts
SetGadgetFont label,fntArial12
SetGadgetFont button,fntComicSans16BoldItalic
SetGadgetFont txt,fntTahoma18Underline

; a very simple event loop
Repeat
	If WaitEvent()=$803 Then Exit
Forever
End ; bye!