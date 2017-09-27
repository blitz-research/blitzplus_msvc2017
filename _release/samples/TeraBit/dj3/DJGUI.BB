Global DJ
Global BackDrop
Global FileMenu
Global SetFolder
Global ExitDJ
Global Vol
Global Selection
Global SelTex
Global perc
; ----Function Begins----
Function CreateDJ()
DJ = CreateWindow("DJ3 - MP3 Media Player for DJ's",XPS,YPS,640,480,Desktop(),7)
BackDrop = CreateCanvas(0,0,640,480,DJ,0)
SetGadgetLayout BackDrop,1,1,1,1
Setbuffer Canvasbuffer(BackDrop)
ClsColor 34,85,136
Cls
FlipCanvas BackDrop
Vol = CreateSlider(6,407,591,17,BackDrop,1)
SetGadgetLayout Vol,1,1,0,1
Selection = CreateListBox(109,104,391,215,BackDrop,0)
AddGadgetItem Selection,""
SelectGadgetItem Selection,0
SetGadgetLayout Selection,1,1,1,1
SelTex = CreateTextField(109,322,391,19,BackDrop,0)
SetGadgetText SelTex,""
SetGadgetLayout SelTex,1,1,0,1
perc = CreateLabel("99%",603,408,21,14,BackDrop,0)
SetGadgetLayout perc,0,1,0,1
FileMenu = CreateMenu("&File",1,WindowMenu(DJ))
SetFolder = CreateMenu("&Select New Folder",2,FileMenu)

ExitDJ = CreateMenu("E&xit",3,FileMenu)

UpdateWindowMenu DJ
End Function