; BlitzPlus Demo
; DJ3 - MP3 Media Player for DJ's
; Copyright Lee Page
; TeraBit Software
; 2003

Include "DJGUI.bb"

Global xps = (ClientWidth(Desktop())-640)/2
Global yps = (ClientHeight(Desktop())-480)/2

Global exeDir$ = CurrentDir$()

If FileType(exedir$ + "\LastDIR.TXT")<>0 Then 
	in = ReadFile(exedir$ + "\LastDir.txt")
	SetDir$ = ReadLine$(in)
	CloseFile in
Else
	setdir$ = CurrentDir()
EndIf

Global filecount
Global mcnt

CreateDJ()

SetBuffer CanvasBuffer(BackDrop)
back = LoadImage("DJ3.PNG")
DrawBlock back,0,0
FlipCanvas backdrop

.restart

SetGadgetShape DJ,XPS,YPS,640,480

Dim file$(10000)
Dim match$(10000)
Dim word$(10000)

curdir = ReadDir(SetDir$)
If Not curdir RuntimeError "Unable to open dir"+setdir$

volume#=0.5
Repeat 
f$=NextFile$(curdir)
If f$<>"." And f$<>".." Then
If Upper$(Right(f$,4))=".MID" Or Upper$(Right(f$,4))=".MP3" Or Upper$(Right(f$,4))=".MOD" Or Upper$(Right(f$,4))=".WAV" Or Upper$(Right(f$,4))=".OGG"
file$(filecount)=f$
filecount=filecount+1
EndIf
EndIf
Until f$=""

CloseDir curdir

ChangeDir setdir$

SetSliderRange vol,1,100
SetSliderValue vol,75
SetGadgetText perc,SliderValue(vol)+"%"
Volume# = Float(SliderValue(vol))/100
Check("")

ActivateGadget SelTex

While WaitEvent()<>$803


If EventID()=4097 And EventData()=3 Then End

If EventID()=4097 And EventData()=2 Then
fold$ = RequestDir("Select Media Folder")
If fold$<>"" Then
setdir$ = Fold$
filecount = 0
in = WriteFile(exedir$ + "\LastDir.txt")
WriteLine in,SetDir$ 
CloseFile in
Goto restart
EndIf
EndIf

;If EventID()<>515 Then Notify "ID = " + EventID() + " Data: "+EventData()

ChannelVolume music,volume#

tex$ = TextFieldText$(SelTex)

If EventID()=1025 And EventSource()=VOL And SelectedGadgetItem(Selection)<>-1 And CountGadgetItems(Selection)>0 And Music<>0 Then 
SetGadgetText perc,SliderValue(vol)+"%"
cvol = WriteFile (Left$(GadgetItemText$(Selection,SelectedGadgetItem(Selection)), Len(GadgetItemText$(Selection,SelectedGadgetItem(Selection)))-3)+"VOL")
WriteInt cvol,SliderValue(vol)
CloseFile cvol
Volume# = Float(SliderValue(vol))/100
EndIf

If EventData()=13 And CountGadgetItems(Selection)>0 Then
	SelectGadgetItem selection,0 
	If music <>0 Then StopChannel music
	music = PlayMusic(GadgetItemText$(Selection,0))
	CheckVolume()
	ChannelVolume music,volume#
EndIf

If EventID()=1025 And EventSource()=Selection Then
	If music <>0 Then StopChannel music
	music = PlayMusic(GadgetItemText$(Selection,SelectedGadgetItem(Selection)))
	CheckVolume()
	ChannelVolume music,volume#
EndIf

If EventSource() = SelTex Then check(Tex$)
Wend

End

Function check(Text$)
cnt=1
pos=1
mcnt=0
Text$=Text$+" "
For t=1 To Len(Text$)

If Mid(Text$,t,1)=" " Then
word$(cnt)=Trim$(Mid(Text$,pos,t-pos))
cnt=cnt+1
pos=t+1
EndIf

Next

For item = 0 To filecount
For t=1 To cnt
If Instr(Upper$(file$(item)),Upper$(word$(t)))=0 Then
hoot=1
EndIf
Next
If hoot=0 Then 
match$(mcnt)=file$(item)
mcnt=mcnt+1
EndIf
hoot=0
Next

ClearGadgetItems Selection
For t=0 To mcnt-1
If Trim$(match$(t))<>"" Then AddGadgetItem Selection,match$(t)
Next 
If CountGadgetItems(Selection)<>0 Then SelectGadgetItem Selection,0
End Function

Function CheckVolume()
If FileType(Left$(GadgetItemText$(Selection,SelectedGadgetItem(Selection)), Len(GadgetItemText$(Selection,SelectedGadgetItem(Selection)))-3)+"VOL")<>0 Then
cvol = ReadFile(Left$(GadgetItemText$(Selection,SelectedGadgetItem(Selection)), Len(GadgetItemText$(Selection,SelectedGadgetItem(Selection)))-3)+"VOL")
SetSliderValue Vol,ReadInt(cvol)
CloseFile cvol
SetGadgetText perc,SliderValue(vol)+"%"
Volume# = Float(SliderValue(vol))/100
EndIf
End Function