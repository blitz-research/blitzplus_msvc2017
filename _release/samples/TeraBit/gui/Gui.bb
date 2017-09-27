;BlitzPlus GUI Designer
;Version 1.0
;Copyright Lee Page
;TeraBit Software 2002
;-------------------------

Global def.GUI, theform, sel, toolbox, help, state, creation, lock, unlock, panel
Global tbtipe,tbname,tbxp, tbyp, tbwd, tbht, tbvalue , tbmisc , Lockup, struct, RootStruct
Global tbStyle, tbEnabled, tbVisible, tbent, tbhead, create, destroy,delcon, codefile
Global StyleTitle, StyleSize, StyleMenu, StyleStatus, StyleTool, stylelabel

Global MenuEd, Panel20, Label1, Button1, Button2, Button3, Tree1
Global Label2, Label3, TextField1, root, rootmenu, window, menunum, acm

Type GUI
	Field tipe$
	Field name$
	Field hand
	Field xp, yp, wd, ht
	Field entries
	Field tex$[100]
	Field Style
	Field Parent
	Field ParentName$
	
	Field KillMe
	
	Field LockLeft
	Field LockRight
	Field LockTop
	Field LockBottom
	
	Field Create
	Field Node
	
	Field misc
	Field Enabled
	Field Visible
	Field value
	Field Thumb[4]
End Type

Unlock = LoadImage("Unlock.PNG")
Lock = LoadImage("Lock.PNG")

Splash = LoadImage("Splash.png")

Global sxps = (ClientWidth(Desktop())-400)/2
Global syps = (ClientHeight(Desktop())-300)/2

swin = CreateWindow("",sxps,syps,400,300,0,0)
splashwin = CreateCanvas(0,0,400,300,swin)
SetBuffer CanvasBuffer(splashwin)
DrawBlock splash,0,0
FlipCanvas splashwin

timer = CreateTimer(1)

While WaitEvent()<>$803
If EventID()=$4001 Then countup = countup+1

If countup = 4 Or EventID()=513 Then
Exit
EndIf
Wend

FreeTimer timer
FreeGadget splashwin
FreeGadget swin
FreeImage splash

window=CreateWindow( "BlitzPlus GUI Designer (1.0)",0,0,ClientWidth(Desktop()),98,0,5+8)

buf = GadgetHeight(window) - ClientHeight(Window)

SetGadgetShape window,0,0,GadgetWidth(window),buf+42

ToolBox = CreateWindow("Properties",ClientWidth(Desktop())-256,buf+40,256,555,Desktop(),17)
CreateLabel "Element:",5,10,50,16,ToolBox
sel = CreateComboBox(55,5,190,16,ToolBox)

ToolPanel = CreatePanel(0,0,ClientWidth(window),ClientHeight(window)+8,window,0)
toolbar=CreateToolBar( "gui.png",0,0,0,0,ToolPanel)

SetToolBarTips toolbar,"Label,TextField,Tabber,ComboBox,ListBox,UserCanvas,Button,Panel,ToolBar,CheckBox,Horizontal Slider,Vertical Slider,Radio Button,TreeView,Progress Bar, HTML Window, TextArea, Menu Item"

panel = CreatePanel(5,35,240,495,ToolBox,1)
tbname = CreateTextField(50,5,100,20,panel)
tbxp= CreateTextField(50,5+20,100,20,panel)
tbyp= CreateTextField(50,5+40,100,20,panel)
tbwd= CreateTextField(50,5+60,100,20,panel)
tbht= CreateTextField(50,5+80,100,20,panel)
tbvalue = CreateTextField(50,5+100,100,20,panel)
tbmisc = CreateTextField(50,5+120,100,20,panel)
tbHead = CreateTextField(50,5+235,100,20,panel)

StyleTitle = CreateButton("TitleBar",50,7+140,60,20,panel,2)
StyleSize = CreateButton("Sizeable",120,7+140,60,20,panel,2)
StyleMenu = CreateButton("Menu",185,7+140,60,20,panel,2)
StyleStatus = CreateButton("Status Bar",50,167,70,20,panel,2)
StyleTool = CreateButton("Tool Window",120,167,105,20,panel,2)

;1 : window has titlebar 
;2 : window is resizable 
;4 : window has a menu 
;8 : window has a status bar 
;16 : window is a 'tool' window - smaller title bar etc. 
;AddGadgetItem tbStyle,"Normal",True

tbEnabled = CreateComboBox(50,5+185,225-50,20,panel)
AddGadgetItem tbenabled,"False",False
AddGadgetItem tbenabled,"True",True
tbVisible = CreateComboBox(50,5+210,225-50,20,panel)
AddGadgetItem tbVisible,"False",False
AddGadgetItem tbvisible,"True",True
tbent= CreateListBox(50,5+255,225-50,54,panel)
create = CreateButton("Add Item", 50, 320,60,20,panel)
DelCon = CreateButton("Delete", 165,5,60,18,panel)
destroy = CreateButton("Remove Item", 120, 320,90,20,panel)
;bringtofront = CreateButton("Properties Window", ClientWidth(window)-100,(ClientHeight(window)/2)-21,100,40,toolbar)
;SetGadgetLayout bringtofront,0,1,1,0
;--
CreateLabel("Name:",5,7,45,20,panel)
CreateLabel("X-Pos:",5,7+20,45,20,panel)
CreateLabel("Y-Pos:",5,7+40,45,20,panel)
CreateLabel("Width:",5,7+60,45,20,panel)
CreateLabel("Height:",5,7+80,45,20,panel)
CreateLabel("Value:",5,7+100,45,20,panel)
CreateLabel("Misc:",5,7+120,45,20,panel)
stylelabel = CreateLabel("Window Style:",5,153,45,40,panel)
CreateLabel("Enabled:",5,7+185,45,20,panel)
CreateLabel("Visible:",5,7+210,45,20,panel)
CreateLabel("List:",5,7+235,45,64,panel)
CreateLabel("Structure:",3,347,45,64,panel)
;-------

FileMenu = CreateMenu( "&File",9990,WindowMenu(window))
CreateMenu "&Open Graphical User Interface Window",9991,filemenu
CreateMenu "&Save Graphical User Interface Window",9992,filemenu
CreateMenu "&Generate Code for current Window",9993,filemenu
CreateMenu "E&xit",9999,filemenu

ToolMenu = CreateMenu( "&Tools",9994,WindowMenu(window))
Global Med, Pro
pro = CreateMenu("&Properties Window",9995,ToolMenu)
CheckMenu pro
 
UpdateWindowMenu window

def.gui = New gui
def\hand = CreateWindow("Window1",50,150,ClientWidth(Desktop())-350,ClientHeight(Desktop())-240,Desktop(),3)
def\thumb[1] = def\hand
def\thumb[2] = def\hand
def\thumb[3] = def\hand
def\thumb[4] = def\hand

theform = def\hand
def\name$ = "Win1"
def\tipe = "WINDOW"
def\xp = 50
def\yp = 150
def\wd = ClientWidth(Desktop())-350
def\ht = ClientHeight(Desktop())-240
def\style = 3
def\Create = 0 : Creation = Creation +1
def\parent = 0
def\entries = 0
def\tex$[0]="Window1"
def\enabled = True
def\visible = True

AddGadgetItem sel,"Win1",True
Activate "Win1"

ShowStruct()

Lockup = CreateCanvas(163,43,62,64,Panel)
SetBuffer CanvasBuffer(Lockup)
Color 192,192,192
Rect 1,1,60,62

DrawBlock Unlock,0,16
DrawBlock Unlock,21,32
DrawBlock Unlock,21,0
DrawBlock Unlock,41,16

FlipCanvas lockup

;Notify "Beta GUI Designer"+Chr$(13)+"Version 0.5"+Chr$(13)+Chr$(13)+"Left Click and drag on Gadget Handles to move a Gadget, Right Click and drag to Size."
.upup
closeprog = 0
While closeprog<>1
WaitEvent()
If EventID()=$803 And EventSource()=window Then closeprog = 1
If EventID()=$803 And EventSource()=MenuEd Then HideGadget menued : UncheckMenu med : UpdateWindowMenu window
If EventID()=$803 And EventSource()=toolbox Then HideGadget toolbox : UncheckMenu pro : UpdateWindowMenu window

If EventID()=2052 And ( EventSource()=window Or EventSource() = theform ) Then 
ActivateGadget toolbox 
ActivateGadget window 
ActivateGadget theform 
ActivateGadget EventSource()
If EventSource() = theform Then activate theform
EndIf

SetStatusText window,"ID = "+EventID() + " - Data: " + EventData() + " (Mouse X: "+(MouseX())+" Mouse Y: "+(MouseY())+")."

If def\name$ <> GadgetItemText(sel,SelectedGadgetItem(sel)) Then activate GadgetItemText(sel,SelectedGadgetItem(sel))  

		Select EventID()
		Case 1025 ; Click
			Select EventSource()
			Case StyleTitle
			If def\tipe = "WINDOW" Then 
				def\Style = Def\Style Xor 1
				ReCreate()
				def = First GUI
				ClearGadgetItems Sel
				For ook.GUI = Each GUI
				AddGadgetItem sel,ook\name,False
				Next
				activate GadgetItemText(sel,0)
			EndIf
			Case StyleSize
			If def\tipe = "WINDOW" Then 
				def\Style = Def\Style Xor 2
				ReCreate()
				def = First GUI
				ClearGadgetItems Sel
				For ook.GUI = Each GUI
				AddGadgetItem sel,ook\name,False
				Next
				activate GadgetItemText(sel,0)
			EndIf
			Case StyleMenu
			If def\tipe = "WINDOW" Then 
				def\Style = Def\Style Xor 4
				acm = (def\style And 4)
				ReCreate()
				def = First GUI
				ClearGadgetItems Sel
				For ook.GUI = Each GUI
				AddGadgetItem sel,ook\name,False
				Next
				activate GadgetItemText(sel,0)
				showstruct()
			EndIf
			Case StyleStatus
				If def\tipe = "WINDOW" Then 
				def\Style = Def\Style Xor 8
			ReCreate()
				def = First GUI
				ClearGadgetItems Sel
				For ook.GUI = Each GUI
				AddGadgetItem sel,ook\name,False
				Next
				activate GadgetItemText(sel,0)
			EndIf
			Case StyleTool
			If def\tipe = "WINDOW" Then 
				def\Style = Def\Style Xor 16
				ReCreate()
				def = First GUI
				ClearGadgetItems Sel
				For ook.GUI = Each GUI
				AddGadgetItem sel,ook\name,False
				Next
				activate GadgetItemText(sel,0)
			EndIf
			Case toolbar
				Select EventData()
				Case 0
				AddLabel def\hand
				Case 1
				AddTextField def\hand
				Case 2
				AddTabber def\hand
				Case 3
				AddCombo def\hand
				Case 4
				AddListBox def\hand
				Case 5
				AddCanvas def\hand
				Case 6
				AddButton def\hand
				Case 7
				AddPanel def\hand
				Case 8 ; ToolBars
				AddToolBar Def\Hand
				Case 9 ; CheckBox
				AddCheckBox Def\hand
				Case 10 ; H-Slide
				AddHslide def\hand
				Case 11 ; V-Slide
				AddVslide def\hand
				Case 12 ; Radio
				AddRadioButton Def\hand
				Case 13 ; Tree
				AddTree Def\hand
				Case 14 ; Progress Bar
				AddPBar Def\hand
				Case 15 ; HTML
				AddHTML Def\hand
				Case 16 ; TextArea
				AddTextArea Def\Hand
				Case 17
				AddMenu Def\Hand
				End Select
				SetHandles()
				ShowStruct()	
				Case sel
				activate(GadgetItemText(sel,SelectedGadgetItem(sel)))
			Case Struct
			Activate WhatNode(SelectedTreeViewNode(Struct))
			Case tbname
			If EventData()=32 Then SetGadgetText tbname,Trim$(TextFieldText$(tbname))
			; - Add Change List Code
			If EventData()=13 Then
			For ook.gui = Each gui
			If Upper$(Trim$(ook\name$))=Upper$(Trim$(TextFieldText$(tbname))) Then abort = 1
			Next
			If abort = 1 Then
				SetStatusText window, "This name already Exists, please a unique name"
				Else
				For ook.gui = Each gui
					If Upper$(Trim$(ook\Parentname$))=Upper$(Trim$(def\name$)) Then ook\parentname$=Trim$(TextFieldText$(tbname))
				Next
				
				def\name$=Trim$(TextFieldText$(tbname))	
				RemoveGadgetItem sel,SelectedGadgetItem(sel)
				AddGadgetItem sel,def\name$,True
				activate def\hand
				showstruct
			EndIf
				
			EndIf
			abort = 0
			Case tbxp
			If EventData()=32 Then SetGadgetText tbxp,Trim$(TextFieldText$(tbxp))
			If EventData() = 13 Then
			def\xp = TextFieldText$(tbxp)
			If def\tipe<>"MENU" Then SetGadgetShape def\hand,def\xp,def\yp,def\wd,def\ht
			sethandles()
			EndIf
			Case tbyp
			If EventData()=32 Then SetGadgetText tbyp,Trim$(TextFieldText$(tbyp))
			If EventData() = 13 Then
			def\yp = TextFieldText$(tbyp)
			If def\tipe<>"MENU" Then SetGadgetShape def\hand,def\xp,def\yp,def\wd,def\ht
			sethandles()
			EndIf
			Case tbwd
			If EventData()=32 Then SetGadgetText tbwd,Trim$(TextFieldText$(tbwd))
			If EventData() = 13 Then
			def\wd = TextFieldText$(tbwd)
			If def\tipe<>"MENU" Then SetGadgetShape def\hand,def\xp,def\yp,def\wd,def\ht
			sethandles()
			EndIf
			Case tbht
			If EventData()=32 Then SetGadgetText tbht,Trim$(TextFieldText$(tbht))
			If EventData() = 13 Then
			If def\tipe<>"COMBO" Then def\ht = TextFieldText$(tbht) Else def\ht=20
			If def\tipe<>"MENU" Then SetGadgetShape def\hand,def\xp,def\yp,def\wd,def\ht
			sethandles()
			EndIf
			Case tbvalue
			If EventData()=32 Then SetGadgetText tbvalue,Trim$(TextFieldText$(tbvalue))
			If EventData() = 13 Then
			def\value = TextFieldText$(tbvalue)
			If def\tipe<>"MENU" Then SetGadgetShape def\hand,def\xp,def\yp,def\wd,def\ht
			sethandles()
			EndIf
			Case tbmisc
			If EventData()=32 Then SetGadgetText tbmisc,Trim$(TextFieldText$(tbmisc))
			If EventData() = 13 Then
			def\misc = TextFieldText$(tbmisc)
			If def\tipe<>"MENU" Then SetGadgetShape def\hand,def\xp,def\yp,def\wd,def\ht
			sethandles()
			EndIf
			Case tbhead
			If whattype(def\hand)<>"PANEL" And whattype(def\hand)<>"CANVAS" And whattype(def\hand)<>"TREE" And whattype(def\hand)<>"HSLIDE" And whattype(def\hand)<>"VSLIDE" And whattype(def\hand)<>"PBAR" And whattype(def\hand)<>"HTML" Then
			def\tex$[SelectedGadgetItem(tbent)] = TextFieldText$(tbhead)
			wti = SelectedGadgetItem(tbent)
			InsertGadgetItem tbent, SelectedGadgetItem(tbent),TextFieldText$(tbhead)
			RemoveGadgetItem tbent,SelectedGadgetItem(tbent)
			SelectGadgetItem tbent, wti
			If whattype(def\hand)="WINDOW" Or whattype(def\hand)="LABEL" Or whattype(def\hand)="TEXTFIELD" Or whattype(def\hand)="BUTTON" Or whattype(def\hand)="CHECKBOX" Or whattype(def\hand)="RADIO" Or whattype(def\hand)="MENU" Or whattype(def\hand)="TEXTAREA" Or whattype(def\hand)="HTML" Then
			
			If whattype(def\hand)="MENU" Then
			SetMenuText def\hand,TextFieldText$(tbhead)
			UpdateWindowMenu theform
			Else
			SetGadgetText def\hand,TextFieldText$(tbhead)
			EndIf
			Else
				InsertGadgetItem def\hand, SelectedGadgetItem(def\hand),TextFieldText$(tbhead)
				RemoveGadgetItem def\hand,SelectedGadgetItem(def\hand)
				SelectGadgetItem def\hand, wti
			EndIf
			EndIf
			Case tbent
			If whattype(def\hand)="LABEL" Or whattype(def\hand)="TEXTBOX" Or whattype(def\hand)="BUTTON" Or whattype(def\hand)="CHECKBOX" Or whattype(def\hand)="RADIO" Then
			
			EndIf
			If whattype(def\hand)="TABBER" Or whattype(def\hand)="LIST" Or whattype(def\hand)="COMBO" Then
			SetGadgetText tbhead,GadgetItemText(tbent,SelectedGadgetItem(tbent))
			SelectGadgetItem def\hand,SelectedGadgetItem(tbent)
			EndIf
			Case create
		 	If def\tipe = "TABBER" Or def\tipe = "LIST" Or def\tipe = "COMBO" Then
			def\entries = def\entries+1
			def\tex[def\entries]="..."
			AddGadgetItem def\hand,"..."
			AddGadgetItem tbent,"...",True
			SetGadgetText tbhead,GadgetItemText(tbent,CountGadgetItems(tbent)-1)
			SelectGadgetItem def\hand,CountGadgetItems(def\hand)-1
			EndIf

			Case destroy 
			If def\tipe = "TABBER" Or def\tipe = "LIST" Or def\tipe = "COMBO" Then
			If CountGadgetItems(def\hand)>1 Then
			def\entries = def\entries - 1
			
			RemoveGadgetItem def\hand,SelectedGadgetItem(def\hand)
			RemoveGadgetItem tbent,SelectedGadgetItem(tbent)
			
			SelectGadgetItem def\hand,0
			SelectGadgetItem tbent,0
			SetGadgetText tbhead,GadgetItemText(tbent,0)
			
			For t = 0 To CountGadgetItems(def\hand)-1
			def\tex$[t]=GadgetItemText$(def\hand,t)
			Next 
						
			;RemoveGadgetItem def\hand,CountGadgetItems(def\hand)-1
			;RemoveGadgetItem tbent,CountGadgetItems(tbent)-1
			Else
			;---------------
			SetGadgetText tbhead,""
			If whattype(def\hand)<>"PANEL" And whattype(def\hand)<>"CANVAS" And whattype(def\hand)<>"TREE" And whattype(def\hand)<>"HSLIDE" And whattype(def\hand)<>"VSLIDE" Then
			def\tex$[SelectedGadgetItem(tbent)] = TextFieldText$(tbhead)
			wti = SelectedGadgetItem(tbent)
			InsertGadgetItem tbent, SelectedGadgetItem(tbent),TextFieldText$(tbhead)
			RemoveGadgetItem tbent,SelectedGadgetItem(tbent)
			SelectGadgetItem tbent, wti
			If whattype(def\hand)="WINDOW" Or whattype(def\hand)="LABEL" Or whattype(def\hand)="TEXTFIELD" Or whattype(def\hand)="BUTTON" Or whattype(def\hand)="CHECKBOX" Or whattype(def\hand)="RADIO" Or whattype(def\hand)="MENU" Then
			
			If whattype(def\hand)="MENU" Then
			SetMenuText def\hand,TextFieldText$(tbhead)
			UpdateWindowMenu theform
			Else
			SetGadgetText def\hand,TextFieldText$(tbhead)
			EndIf
			Else
				InsertGadgetItem def\hand, SelectedGadgetItem(def\hand),TextFieldText$(tbhead)
				RemoveGadgetItem def\hand,SelectedGadgetItem(def\hand)
				SelectGadgetItem def\hand, wti
			EndIf
			EndIf
			
			;----------------
			EndIf
			EndIf
			
			Case delcon
			If def\tipe<>"WINDOW" Then
			res=Confirm("Are you sure you want to delete "+def\name$+"?"+Chr$(13)+"All dependant child objects will be deleted also."+Chr$(13)+"Do you want to Proceed?",True)
			If res = 1 Then
			def\KillMe = 1
			Recreate()
			def = First GUI
			ClearGadgetItems Sel
			For ook.GUI = Each GUI
				AddGadgetItem sel,ook\name,False
			Next
			activate GadgetItemText(sel,0)
			EndIf
			EndIf
			ShowStruct()
			Case bringtofront
			;ShowGadget ToolBox : ActivateGadget toolbox
			;ShowGadget theform : ActivateGadget theform
			;ReCreate()
			End Select
		Case 4097 ; Menus
			If EventData()>9989 Then
			Select EventData()
			Case 9991
			OpenGUI(RequestFile$("Open GUI","GUI",False))
			def = First GUI
			ClearGadgetItems Sel
			For ook.GUI = Each GUI
				AddGadgetItem sel,ook\name,False
			Next
			activate GadgetItemText(sel,0)
			Case 9992
			SaveGUI(RequestFile$("Save GUI","GUI",True))
			Case 9993
			Gencode$ = RequestFile$("Generate Code","BB",True)
			If gencode$<>"" Then
			SaveCode(gencode$)
			SetStatusText window, "Code Generated.."
			Else 
			SetStatusText window, "Code Generation Aborted"
			EndIf
			Case 9995
			;For ook.gui = Each gui
			;DebugLog ook\name$
			;Next
			ShowGadget toolbox
			CheckMenu pro
			UpdateWindowMenu window
			ActivateGadget toolbox
			Case 9999
			Goto enditall
			End Select
			Else
			For ook.gui = Each gui
			If ook\tipe = "MENU" And ook\value=EventData() Then
			activate ook\name
			Exit
			EndIf
			Next
			EndIf
		Case 515 ; Mouse Move
			For thup=1 To 4
			If EventSource() = def\thumb[thup] Then 
				If MouseDown(1) Then
				hideHandles()
			
				movx = MouseX(def\thumb[thup]) - stx : Movy = MouseY(def\thumb[thup])-sty
				def\xp = def\xp + movx
				def\yp = def\yp + movy
				SetGadgetShape def\hand, def\xp,def\yp,GadgetWidth(def\hand),GadgetHeight(def\hand)
				stx = MouseX(def\thumb[thup]) : Sty = MouseY(def\thumb[thup])
				
				SetGadgetShape def\hand,GadgetX(def\hand),GadgetY(def\hand),GadgetWidth(def\hand)-1,GadgetHeight(def\hand)-1
				SetGadgetShape def\hand,GadgetX(def\hand),GadgetY(def\hand),GadgetWidth(def\hand)+1,GadgetHeight(def\hand)+1
				
				EndIf
				
				If MouseDown(2) Then
				Hidehandles()
			
				movx = MouseX(def\thumb[thup]) - stx : Movy = MouseY(def\thumb[thup])-sty
				
				
				If def\tipe<>"COMBO" Then
				
				Select Thup
				Case 1
					SetGadgetShape def\hand, GadgetX(def\hand)+movx,GadgetY(def\hand)+movy, GadgetWidth(def\hand)-movx,GadgetHeight(def\hand)-movy
				Case 2
					SetGadgetShape def\hand, GadgetX(def\hand)+movx,GadgetY(def\hand), GadgetWidth(def\hand)-movx,GadgetHeight(def\hand)+movy
				Case 3
					SetGadgetShape def\hand, GadgetX(def\hand),GadgetY(def\hand), GadgetWidth(def\hand)+movx,GadgetHeight(def\hand)+movy
				Case 4
					SetGadgetShape def\hand, GadgetX(def\hand),GadgetY(def\hand)+movy, GadgetWidth(def\hand)+movx,GadgetHeight(def\hand)-movy
				End Select
				Else
				Select Thup
				Case 1
					SetGadgetShape def\hand, GadgetX(def\hand)+movx,GadgetY(def\hand), GadgetWidth(def\hand)-movx,GadgetHeight(def\hand)
				Case 2
					SetGadgetShape def\hand, GadgetX(def\hand)+movx,GadgetY(def\hand), GadgetWidth(def\hand)-movx,GadgetHeight(def\hand)
				Case 3
					SetGadgetShape def\hand, GadgetX(def\hand),GadgetY(def\hand), GadgetWidth(def\hand)+movx,GadgetHeight(def\hand)
				Case 4
					SetGadgetShape def\hand, GadgetX(def\hand),GadgetY(def\hand), GadgetWidth(def\hand)+movx,GadgetHeight(def\hand)
				End Select
				EndIf
				
				def\xp = GadgetX(def\hand)
				def\yp = GadgetY(def\hand)
				def\wd = GadgetWidth(def\hand)
				def\ht = GadgetHeight(def\hand)
				
				stx = MouseX(def\thumb[thup]) : Sty = MouseY(def\thumb[thup])
				EndIf
				
				SetGadgetText tbxp,GadgetX(def\hand)
				SetGadgetText tbyp,GadgetY(def\hand)
				SetGadgetText tbwd,GadgetWidth(def\hand)
				SetGadgetText tbht,GadgetHeight(def\hand)
				WaitEvent(0)
				If EventID()<>515 Then Goto un
			Exit
			EndIf
			Next
		Case 514 ; unclick
		.un
		For thup = 1 To 4
		For ook.gui = Each gui
		If ook\tipe$<>"WINDOW" And ook\tipe$<>"TOOLBAR" And ook\tipe$<>"MENU" Then 
		If EventSource() = ook\thumb[thup] Then
		stx = MouseX(ook\thumb[thup]) : Sty = MouseY(ook\thumb[thup])
		state = 0  : FlushMouse
		sethandles()
		EndIf
		EndIf
		Next
		Next
		RefreshSize()
		Case 513 ; Click
		If EventSource()=Lockup Then
			If inrect(MouseX(Lockup),MouseY(lockup),0,16,21,32) Then Def\LockLeft = Def\LockLeft Xor 1
			If inrect(MouseX(Lockup),MouseY(lockup),21,32,21,32) Then Def\LockBottom = Def\LockBottom Xor 1
			If inrect(MouseX(Lockup),MouseY(lockup),21,0,21,32) Then Def\LockTop = Def\LockTop Xor 1
			If inrect(MouseX(Lockup),MouseY(lockup),41,16,21,32) Then Def\LockRight = Def\LockRight Xor 1
			If def\tipe<>"MENU" Then SetGadgetLayout def\hand,def\LockLeft,def\LockRight,def\LockTop,def\LockBottom
			LockStatus()
			SetHandles()
		Else
		For thup = 1 To 4
		For ook.gui = Each gui
		If ook\tipe$<>"WINDOW" And ook\tipe$<>"TOOLBAR" And ook\tipe$<>"MENU" Then 
		If EventSource() = ook\thumb[thup] Then
		If GadgetItemText(sel,SelectedGadgetItem(sel))<>ook\name Then activate ook\name
		stx = MouseX(ook\thumb[thup]) : Sty = MouseY(ook\thumb[thup])
		state = EventData()
		sethandles()
		EndIf
		EndIf
		Next
		Next
		EndIf
		Case 2050
		If EventSource() = Window Then
		If WindowMinimized(window) Then
		HideGadget toolbox
		HideGadget theform
		Else		  
		ShowGadget theform : ActivateGadget theform
		ShowGadget ToolBox : ActivateGadget toolbox
		EndIf
		EndIf
		End Select
		If EventID() = 4097 Then Goto bypars
		For ook.gui = Each GUI
		If EventSource()= Ook\hand
		
			If GadgetItemText(sel,SelectedGadgetItem(sel))<>ook\name And EventID()<>515 Then activate ook\name
		
			If ook\tipe$="TEXTFIELD" And EventID()=1025 Then
			ook\tex$[0]=TextFieldText$(ook\hand)
			SetGadgetText tbhead,ook\tex$[0]
			wti = SelectedGadgetItem(tbent)
			InsertGadgetItem tbent, SelectedGadgetItem(tbent),ook\tex$[0]
			RemoveGadgetItem tbent, SelectedGadgetItem(tbent)
			SelectGadgetItem tbent, wti
			EndIf
			
			If ook\tipe$="TABBER" And EventID()=1025 Then
			SelectGadgetItem tbent,SelectedGadgetItem(ook\hand)
			SetGadgetText tbhead,GadgetItemText(tbent,SelectedGadgetItem(tbent))
			EndIf
			
			If ook\tipe$="COMBO" And EventID()=1025 Then
			SelectGadgetItem tbent,SelectedGadgetItem(ook\hand)
			SetGadgetText tbhead,GadgetItemText(tbent,SelectedGadgetItem(tbent))
			EndIf
			
			;Activate Ook\name$
			
			If EventID()<>515 Then
			ook\xp = GadgetX(ook\hand)
			ook\yp = GadgetY(ook\hand)
			ook\wd = GadgetWidth(ook\hand)
			ook\ht = GadgetHeight(ook\hand)

			SetGadgetText tbxp,GadgetX(ook\hand)
			SetGadgetText tbyp,GadgetY(ook\hand)
			SetGadgetText tbwd,GadgetWidth(ook\hand)
			SetGadgetText tbht,GadgetHeight(ook\hand)
			Sethandles()
			EndIf
		Exit
	EndIf
Next
.bypars
	Wend
.enditall
res=Confirm("Are you sure you want exit? ",True)
If res<>1 Then Goto upup

End

Function Addlabel(Parent)
If WhatType$(Parent)<>"WINDOW" And WhatType$(Parent)<>"FRAME" And WhatType$(Parent)<>"TABBER" And WhatType$(Parent)<>"PANEL" And WhatType$(Parent)<>"CANVAS" Then parent = theForm
def.gui = New gui

def\thumb[1] = CreateCanvas(0,0,5,5,parent)
def\thumb[2] = CreateCanvas(0,0,5,5,parent)
def\thumb[3] = CreateCanvas(0,0,5,5,parent)
def\thumb[4] = CreateCanvas(0,0,5,5,parent)

def\hand = CreateLabel("Label...",5,5,100,20,parent,0)
def\name$ = "Label"+GUID("Label")
def\tipe = "LABEL"
def\xp = 5
def\yp = 5
def\wd = 100
def\ht = 20
def\style = 0
def\parent = parent
def\ParentName$ = Whatname$(Parent)
def\entries = 0
def\tex$[0]="Label..."
def\enabled = True
def\visible = True

def\LockLeft = 1
def\LockRight = 0
def\LockTop = 1
def\LockBottom = 0
SetGadgetLayout def\hand,def\LockLeft,def\LockRight,def\LockTop,def\LockBottom

AddGadgetItem sel,def\name$,True
Activate def\name$
Def\Create = Creation : Creation = Creation + 1
End Function

Function AddMenu(Parent)
If WhatType$(Parent)<>"MENU" And WhatType$(Parent)<>"WINDOW" Then 
	SetStatusText window,"You can only add Menu Structures to a window or other menus!"
	Return
EndIf
If WhatType$(Parent)="WINDOW" And (def\style And 4)=0 Then 
SetStatusText window,"This window is not set to receive a menu." : Return 
EndIf
If WhatType$(Parent)<>"MENU" Then
	parent = WindowMenu(parent)
EndIf

def.gui = New gui
menunum = menunum +1
menuid = GUID("Menu")

def\hand = CreateMenu("Menu"+menuid,menunum,parent)
UpdateWindowMenu theform

def\name$ = "Menu"+menuid
def\tipe = "MENU"
def\xp = 5
def\yp = 5
def\wd = 100
def\ht = 20
def\style = 0
def\parent = parent
def\ParentName$ = Whatname$(Parent)
def\entries = 0
def\tex$[0]="Menu"+menuid
def\enabled = True
def\visible = True
def\value = menunum
def\LockLeft = 1
def\LockRight = 0
def\LockTop = 1
def\LockBottom = 0
AddGadgetItem sel,def\name$,True
Activate def\name$
Def\Create = Creation : Creation = Creation + 1
End Function

Function AddToolBar(Parent)
If WhatType$(Parent)<>"WINDOW" And WhatType$(Parent)<>"FRAME" And WhatType$(Parent)<>"TABBER" And WhatType$(Parent)<>"PANEL" And WhatType$(Parent)<>"CANVAS" Then parent = theForm

req$ = RequestFile$("Select Image for Toolbar..","JPG,BMP,PNG")

If REq$="" Or FileType(req$)=0 Then 
SetStatusText window, "Aborted"
Return
EndIf

def.gui = New gui

def\hand = CreateToolBar(req$,5,5,100,20,parent)

def\thumb[1] = def\hand
def\thumb[2] = def\hand
def\thumb[3] = def\hand
def\thumb[4] = def\hand

def\name$ = "ToolBar"+GUID("ToolBar")
def\tipe = "TOOLBAR"
def\xp = 5
def\yp = 5
def\wd = 100
def\ht = 20
def\style = 0
def\parent = parent
def\ParentName$ = Whatname$(Parent)
def\entries = 0
def\tex$[0]=req$
def\enabled = True
def\visible = True

AddGadgetItem sel,def\name$,True
Activate def\name$
Def\Create = Creation : Creation = Creation + 1
End Function


Function AddCombo(Parent)
If WhatType$(Parent)<>"WINDOW" And WhatType$(Parent)<>"FRAME" And WhatType$(Parent)<>"TABBER"  And WhatType$(Parent)<>"PANEL" And WhatType$(Parent)<>"CANVAS" Then parent = theForm
def.gui = New gui
def\thumb[1] = CreateCanvas(0,0,5,5,parent)
def\thumb[2] = CreateCanvas(0,0,5,5,parent)
def\thumb[3] = CreateCanvas(0,0,5,5,parent)
def\thumb[4] = CreateCanvas(0,0,5,5,parent)

def\hand = CreateComboBox(5,5,100,20,parent)
def\name$ = "Combo"+GUID("Combo")
def\tipe = "COMBO"
def\xp = 5
def\yp = 5
def\wd = 100
def\ht = 20
def\style = 0
def\parent = parent
def\ParentName$ = Whatname$(Parent)
def\entries = 0
def\tex$[0]="Combo"
def\enabled = True
def\visible = True
def\LockLeft = 1
def\LockRight = 0
def\LockTop = 1
def\LockBottom = 0
SetGadgetLayout def\hand,def\LockLeft,def\LockRight,def\LockTop,def\LockBottom


AddGadgetItem def\hand,"Combo",True
AddGadgetItem sel,def\name$,True

SetHandles()

Activate def\name$
Def\Create = Creation : Creation = Creation + 1
End Function

Function AddListBox(Parent)
If WhatType$(Parent)<>"WINDOW" And WhatType$(Parent)<>"FRAME" And WhatType$(Parent)<>"TABBER"  And WhatType$(Parent)<>"CANVAS" And WhatType$(Parent)<>"PANEL" Then parent = theForm
def.gui = New gui

def\thumb[1] = CreateCanvas(0,0,5,5,parent)
def\thumb[2] = CreateCanvas(0,0,5,5,parent)
def\thumb[3] = CreateCanvas(0,0,5,5,parent)
def\thumb[4] = CreateCanvas(0,0,5,5,parent)

def\hand = CreateListBox(5,5,100,120,parent)
def\name$ = "ListBox"+GUID("ListBox")
def\tipe = "LIST"
def\xp = 5
def\yp = 5
def\wd = 100
def\ht = 120
def\style = 0
def\parent = parent
def\ParentName$ = Whatname$(Parent)
def\entries = 0
def\tex$[0]="ListBox"
def\enabled = True
def\visible = True
def\LockLeft = 1
def\LockRight = 0
def\LockTop = 1
def\LockBottom = 0
SetGadgetLayout def\hand,def\LockLeft,def\LockRight,def\LockTop,def\LockBottom
AddGadgetItem def\hand,"Listbox",True

AddGadgetItem sel,def\name$,True
Activate def\name$
Def\Create = Creation : Creation = Creation + 1
End Function

Function AddCanvas(Parent)
If WhatType$(Parent)<>"WINDOW" And WhatType$(Parent)<>"FRAME" And WhatType$(Parent)<>"TABBER"  And WhatType$(Parent)<>"CANVAS"And WhatType$(Parent)<>"PANEL" Then parent = theForm
def.gui = New gui

def\thumb[1] = CreateCanvas(0,0,5,5,parent)
def\thumb[2] = CreateCanvas(0,0,5,5,parent)
def\thumb[3] = CreateCanvas(0,0,5,5,parent)
def\thumb[4] = CreateCanvas(0,0,5,5,parent)

def\hand = CreateCanvas(5,5,100,120,parent)
SetBuffer CanvasBuffer(def\hand)
ClsColor 34,85,136
Cls
FlipCanvas def\hand

def\name$ = "Canvas"+GUID("Canvas")
def\tipe = "CANVAS"
def\xp = 5
def\yp = 5
def\wd = 100
def\ht = 120
def\style = 0
def\parent = parent
def\ParentName$ = Whatname$(Parent)
def\entries = 0
def\tex$[0]=""
def\enabled = True
def\visible = True
def\LockLeft = 1
def\LockRight = 0
def\LockTop = 1
def\LockBottom = 0
SetGadgetLayout def\hand,def\LockLeft,def\LockRight,def\LockTop,def\LockBottom



AddGadgetItem sel,def\name$,True
Activate def\name$
Def\Create = Creation : Creation = Creation + 1
End Function

Function AddPanel(Parent)
If WhatType$(Parent)<>"WINDOW" And WhatType$(Parent)<>"FRAME" And WhatType$(Parent)<>"TABBER" And WhatType$(Parent)<>"CANVAS" And WhatType$(Parent)<>"PANEL" Then parent = theForm
def.gui = New gui

def\thumb[1] = CreateCanvas(0,0,5,5,parent)
def\thumb[2] = CreateCanvas(0,0,5,5,parent)
def\thumb[3] = CreateCanvas(0,0,5,5,parent)
def\thumb[4] = CreateCanvas(0,0,5,5,parent)

def\hand = CreatePanel(5,5,100,120,parent,1)
def\name$ = "Panel"+GUID("Panel")
def\tipe = "PANEL"
def\xp = 5
def\yp = 5
def\wd = 100
def\ht = 120
def\style = 1
def\parent = parent
def\ParentName$ = Whatname$(Parent)
def\entries = 0
def\tex$[0]=""
def\enabled = True
def\visible = True
def\LockLeft = 1
def\LockRight = 0
def\LockTop = 1
def\LockBottom = 0
SetGadgetLayout def\hand,def\LockLeft,def\LockRight,def\LockTop,def\LockBottom

AddGadgetItem sel,def\name$,True
Activate def\name$
Def\Create = Creation : Creation = Creation + 1
End Function

Function AddHTML(Parent)
If WhatType$(Parent)<>"WINDOW" And WhatType$(Parent)<>"FRAME" And WhatType$(Parent)<>"TABBER" And WhatType$(Parent)<>"CANVAS" And WhatType$(Parent)<>"PANEL" Then parent = theForm
def.gui = New gui

def\thumb[1] = CreateCanvas(0,0,5,5,parent)
def\thumb[2] = CreateCanvas(0,0,5,5,parent)
def\thumb[3] = CreateCanvas(0,0,5,5,parent)
def\thumb[4] = CreateCanvas(0,0,5,5,parent)

def\hand = CreateHtmlView(5,5,100,120,parent)
def\name$ = "HTML"+GUID("HTML")
def\tipe = "HTML"
def\xp = 5
def\yp = 5
def\wd = 100
def\ht = 120
def\style = 0
def\parent = parent
def\ParentName$ = Whatname$(Parent)
def\entries = 0
def\tex$[0]=""
def\enabled = True
def\visible = True
def\LockLeft = 1
def\LockRight = 0
def\LockTop = 1
def\LockBottom = 0
SetGadgetLayout def\hand,def\LockLeft,def\LockRight,def\LockTop,def\LockBottom

AddGadgetItem sel,def\name$,True
Activate def\name$
Def\Create = Creation : Creation = Creation + 1
End Function

Function AddTextArea(Parent)
If WhatType$(Parent)<>"WINDOW" And WhatType$(Parent)<>"FRAME" And WhatType$(Parent)<>"TABBER" And WhatType$(Parent)<>"CANVAS" And WhatType$(Parent)<>"PANEL" Then parent = theForm
def.gui = New gui

def\thumb[1] = CreateCanvas(0,0,5,5,parent)
def\thumb[2] = CreateCanvas(0,0,5,5,parent)
def\thumb[3] = CreateCanvas(0,0,5,5,parent)
def\thumb[4] = CreateCanvas(0,0,5,5,parent)

def\hand = CreateTextArea(5,5,100,120,parent)
def\name$ = "TEXTAREA"+GUID("TEXTAREA")
def\tipe = "TEXTAREA"
def\xp = 5
def\yp = 5
def\wd = 100
def\ht = 120
def\style = 0
def\parent = parent
def\ParentName$ = Whatname$(Parent)
def\entries = 0
def\tex$[0]=""
def\enabled = True
def\visible = True
def\LockLeft = 1
def\LockRight = 0
def\LockTop = 1
def\LockBottom = 0
SetGadgetLayout def\hand,def\LockLeft,def\LockRight,def\LockTop,def\LockBottom

AddGadgetItem sel,def\name$,True
Activate def\name$
Def\Create = Creation : Creation = Creation + 1
End Function


Function AddHSlide(Parent)
If WhatType$(Parent)<>"WINDOW" And WhatType$(Parent)<>"FRAME" And WhatType$(Parent)<>"TABBER" And WhatType$(Parent)<>"CANVAS" And WhatType$(Parent)<>"PANEL" Then parent = theForm
def.gui = New gui

def\thumb[1] = CreateCanvas(0,0,5,5,parent)
def\thumb[2] = CreateCanvas(0,0,5,5,parent)
def\thumb[3] = CreateCanvas(0,0,5,5,parent)
def\thumb[4] = CreateCanvas(0,0,5,5,parent)

def\hand = CreateSlider(5,5,100,20,parent,1)
def\name$ = "HSlide"+GUID("HSlide")
def\tipe = "HSLIDE"
def\xp = 5
def\yp = 5
def\wd = 100
def\ht = 20
def\style = 1
def\parent = parent
def\ParentName$ = Whatname$(Parent)
def\entries = 0
def\tex$[0]=""
def\enabled = True
def\visible = True
def\LockLeft = 1
def\LockRight = 0
def\LockTop = 1
def\LockBottom = 0
SetGadgetLayout def\hand,def\LockLeft,def\LockRight,def\LockTop,def\LockBottom

AddGadgetItem sel,def\name$,True
Activate def\name$
Def\Create = Creation : Creation = Creation + 1
End Function

Function AddPbar(Parent)
If WhatType$(Parent)<>"WINDOW" And WhatType$(Parent)<>"FRAME" And WhatType$(Parent)<>"TABBER" And WhatType$(Parent)<>"CANVAS" And WhatType$(Parent)<>"PANEL" Then parent = theForm
def.gui = New gui

def\thumb[1] = CreateCanvas(0,0,5,5,parent)
def\thumb[2] = CreateCanvas(0,0,5,5,parent)
def\thumb[3] = CreateCanvas(0,0,5,5,parent)
def\thumb[4] = CreateCanvas(0,0,5,5,parent)

def\hand = CreateProgBar(5,5,100,20,parent,1)
def\name$ = "PBAR"+GUID("PBAR")
def\tipe = "PBAR"
def\xp = 5
def\yp = 5
def\wd = 100
def\ht = 20
def\style = 1
def\parent = parent
def\ParentName$ = Whatname$(Parent)
def\entries = 0
def\tex$[0]=""
def\enabled = True
def\visible = True
def\LockLeft = 1
def\LockRight = 0
def\LockTop = 1
def\LockBottom = 0
SetGadgetLayout def\hand,def\LockLeft,def\LockRight,def\LockTop,def\LockBottom

AddGadgetItem sel,def\name$,True
Activate def\name$
Def\Create = Creation : Creation = Creation + 1
End Function


Function AddVSlide(Parent)
If WhatType$(Parent)<>"WINDOW" And WhatType$(Parent)<>"FRAME" And WhatType$(Parent)<>"TABBER" And WhatType$(Parent)<>"CANVAS" And WhatType$(Parent)<>"PANEL" Then parent = theForm
def.gui = New gui

def\thumb[1] = CreateCanvas(0,0,5,5,parent)
def\thumb[2] = CreateCanvas(0,0,5,5,parent)
def\thumb[3] = CreateCanvas(0,0,5,5,parent)
def\thumb[4] = CreateCanvas(0,0,5,5,parent)

def\hand = CreateSlider(5,5,20,100,parent,0)
def\name$ = "VSlide"+GUID("VSlide")
def\tipe = "VSLIDE"
def\xp = 5
def\yp = 5
def\wd = 20
def\ht = 100
def\style = 0
def\parent = parent
def\ParentName$ = Whatname$(Parent)
def\entries = 0
def\tex$[0]=""
def\enabled = True
def\visible = True
def\LockLeft = 1
def\LockRight = 0
def\LockTop = 1
def\LockBottom = 0
SetGadgetLayout def\hand,def\LockLeft,def\LockRight,def\LockTop,def\LockBottom

AddGadgetItem sel,def\name$,True
Activate def\name$
Def\Create = Creation : Creation = Creation + 1
End Function

Function AddTree(Parent)
If WhatType$(Parent)<>"WINDOW" And WhatType$(Parent)<>"FRAME" And WhatType$(Parent)<>"TABBER" And WhatType$(Parent)<>"CANVAS" And WhatType$(Parent)<>"PANEL" Then parent = theForm
def.gui = New gui

def\thumb[1] = CreateCanvas(0,0,5,5,parent)
def\thumb[2] = CreateCanvas(0,0,5,5,parent)
def\thumb[3] = CreateCanvas(0,0,5,5,parent)
def\thumb[4] = CreateCanvas(0,0,5,5,parent)

def\hand = CreateTreeView(5,5,150,120,parent)
def\name$ = "Tree"+GUID("Tree")
def\tipe = "TREE"
def\xp = 5
def\yp = 5
def\wd = 150
def\ht = 120
def\style = 0
def\parent = parent
def\ParentName$ = Whatname$(Parent)
def\entries = 0
def\tex$[0]=""
def\enabled = True
def\visible = True
def\LockLeft = 1
def\LockRight = 0
def\LockTop = 1
def\LockBottom = 0
SetGadgetLayout def\hand,def\LockLeft,def\LockRight,def\LockTop,def\LockBottom



AddGadgetItem sel,def\name$,True
root=TreeViewRoot(def\hand)
any = AddTreeViewNode("TreeView Gadget",root)
any = AddTreeViewNode(Def\ParentName$,any)
any = AddTreeViewNode(def\name$,any)
Activate def\name$
Def\Create = Creation : Creation = Creation + 1
End Function

Function AddTextField(Parent)
If WhatType$(Parent)<>"WINDOW" And WhatType$(Parent)<>"FRAME" And WhatType$(Parent)<>"TABBER" And WhatType$(Parent)<>"CANVAS" And WhatType$(Parent)<>"PANEL" Then parent = theForm
def.gui = New gui

def\thumb[1] = CreateCanvas(0,0,5,5,parent)
def\thumb[2] = CreateCanvas(0,0,5,5,parent)
def\thumb[3] = CreateCanvas(0,0,5,5,parent)
def\thumb[4] = CreateCanvas(0,0,5,5,parent)

def\hand = CreateTextField(5,5,100,20,parent)
def\name$ = "TextField"+GUID("TextField")
def\tipe = "TEXTFIELD"
def\xp = 5
def\yp = 5
def\wd = 100
def\ht = 20
def\style = 0
def\parent = parent
def\ParentName$ = Whatname$(Parent)
def\entries = 0
def\tex$[0]="Text"
def\enabled = True
def\visible = True

def\LockLeft = 1
def\LockRight = 0
def\LockTop = 1
def\LockBottom = 0
SetGadgetLayout def\hand,def\LockLeft,def\LockRight,def\LockTop,def\LockBottom

AddGadgetItem sel,def\name$,True

Activate def\name$
Def\Create = Creation : Creation = Creation + 1
End Function

Function AddButton(Parent)
If WhatType$(Parent)<>"WINDOW" And WhatType$(Parent)<>"FRAME" And WhatType$(Parent)<>"TABBER" And WhatType$(Parent)<>"CANVAS" And WhatType$(Parent)<>"PANEL" Then parent = theForm
def.gui = New gui
def\thumb[1] = CreateCanvas(0,0,5,5,parent)
def\thumb[2] = CreateCanvas(0,0,5,5,parent)
def\thumb[3] = CreateCanvas(0,0,5,5,parent)
def\thumb[4] = CreateCanvas(0,0,5,5,parent)

def\hand = CreateButton("Button",5,5,100,20,parent)
def\name$ = "Button"+GUID("Button")
def\tipe = "BUTTON"
def\xp = 5
def\yp = 5
def\wd = 100
def\ht = 20
def\style = 0
def\parent = parent
def\ParentName$ = Whatname$(Parent)
def\entries = 0
def\tex$[0]="Button"
def\enabled = True
def\visible = True
def\LockLeft = 1
def\LockRight = 0
def\LockTop = 1
def\LockBottom = 0
SetGadgetLayout def\hand,def\LockLeft,def\LockRight,def\LockTop,def\LockBottom


AddGadgetItem sel,def\name$,True
Activate def\name$
Def\Create = Creation : Creation = Creation + 1
End Function

Function AddCheckBox(Parent)
If WhatType$(Parent)<>"WINDOW" And WhatType$(Parent)<>"FRAME" And WhatType$(Parent)<>"TABBER" And WhatType$(Parent)<>"CANVAS" And WhatType$(Parent)<>"PANEL" Then parent = theForm
def.gui = New gui
def\thumb[1] = CreateCanvas(0,0,5,5,parent)
def\thumb[2] = CreateCanvas(0,0,5,5,parent)
def\thumb[3] = CreateCanvas(0,0,5,5,parent)
def\thumb[4] = CreateCanvas(0,0,5,5,parent)

def\hand = CreateButton("CheckBox",5,5,100,20,parent,2)
def\name$ = "CheckBox"+GUID("CheckBox")
def\tipe = "CHECKBOX"
def\xp = 5
def\yp = 5
def\wd = 100
def\ht = 20
def\style = 2
def\parent = parent
def\ParentName$ = Whatname$(Parent)
def\entries = 0
def\tex$[0]="CheckBox"
def\enabled = True
def\visible = True
def\LockLeft = 1
def\LockRight = 0
def\LockTop = 1
def\LockBottom = 0
SetGadgetLayout def\hand,def\LockLeft,def\LockRight,def\LockTop,def\LockBottom


AddGadgetItem sel,def\name$,True
Activate def\name$
Def\Create = Creation : Creation = Creation + 1
End Function

Function AddRadioButton(Parent)
If WhatType$(Parent)<>"WINDOW" And WhatType$(Parent)<>"FRAME" And WhatType$(Parent)<>"TABBER" And WhatType$(Parent)<>"CANVAS" And WhatType$(Parent)<>"PANEL" Then parent = theForm
def.gui = New gui
def\thumb[1] = CreateCanvas(0,0,5,5,parent)
def\thumb[2] = CreateCanvas(0,0,5,5,parent)
def\thumb[3] = CreateCanvas(0,0,5,5,parent)
def\thumb[4] = CreateCanvas(0,0,5,5,parent)

def\hand = CreateButton("Radio",5,5,100,20,parent,3)
def\name$ = "RadioButton"+GUID("RadioButton")
def\tipe = "RADIO"
def\xp = 5
def\yp = 5
def\wd = 100
def\ht = 20
def\style = 3
def\parent = parent
def\ParentName$ = Whatname$(Parent)
def\entries = 0
def\tex$[0]="Radio"
def\enabled = True
def\visible = True
def\LockLeft = 1
def\LockRight = 0
def\LockTop = 1
def\LockBottom = 0
SetGadgetLayout def\hand,def\LockLeft,def\LockRight,def\LockTop,def\LockBottom


AddGadgetItem sel,def\name$,True
Activate def\name$
Def\Create = Creation : Creation = Creation + 1
End Function

Function AddTabber(Parent)
SetStatusText window, "Tab Gadgets to follow in a future update" : Return
If WhatType$(Parent)<>"WINDOW" And WhatType$(Parent)<>"FRAME" And WhatType$(Parent)<>"TABBER" And WhatType$(Parent)<>"PANEL" And WhatType$(Parent)<>"CANVAS" Then parent = theForm

def.gui = New gui

def\thumb[1] = CreateCanvas(0,0,5,5,parent)
def\thumb[2] = CreateCanvas(0,0,5,5,parent)
def\thumb[3] = CreateCanvas(0,0,5,5,parent)
def\thumb[4] = CreateCanvas(0,0,5,5,parent)

def\hand = CreateTabber(5,5,150,200,parent)
def\name$ = "Tabber"+GUID("Tabber")
def\tipe = "TABBER"
def\xp = 5
def\yp = 5
def\wd = 150
def\ht = 200
def\style = 0
def\parent = parent
def\ParentName$ = Whatname$(Parent)
def\entries = 2
def\tex$[0]="Tab1"
def\tex$[1]="Tab2"
def\tex$[2]="Tab3"

AddGadgetItem def\hand,"Tab1",True
AddGadgetItem def\hand,"Tab2",False
AddGadgetItem def\hand,"Tab3",False

def\enabled = True
def\visible = True

def\LockLeft = 1
def\LockRight = 0
def\LockTop = 1
def\LockBottom = 0
SetGadgetLayout def\hand,def\LockLeft,def\LockRight,def\LockTop,def\LockBottom

AddGadgetItem sel,def\name$,True
Activate def\name$
Def\Create = Creation : Creation = Creation + 1
End Function

Function GUID(IDENT$)
Local IDAP=0
Local srch.gui
.again
IDAP=IDAP+1
For srch.gui = Each gui
If Upper$(srch\name$)=Upper$(IDENT$+IDAP) Then Goto again
Next
Return IDAP
End Function

Function WhatType$(handy)
Local srch.gui
For srch.gui = Each GUI
If srch\hand = handy Then 
Return srch\tipe
EndIf
Next
Return "Unknown"
End Function

Function NameType$(handy$)
Local srch.gui
For srch.gui = Each GUI
If Upper$(srch\name$) = Upper$(handy$) Then 
Return srch\tipe
EndIf
Next
Return "Unknown"
End Function

Function WhatNode$(handy)
Local srch.gui
For srch.gui = Each GUI
If srch\node = handy Then 
Return srch\name$
EndIf
Next
Return "Unknown"
End Function


Function WhatName$(handy)
Local srch.gui
For srch.gui = Each GUI
If srch\hand = handy Then 
Return srch\name$
EndIf
If srch\tipe="WINDOW" Then
If handy = WindowMenu(srch\hand) Then Return srch\name$
EndIf
Next
Return "Unknown"
End Function

Function activate(gadname$)
	state = 0
	Local any.gui
	
	For any.gui = Each gui
	If any\tipe<>"WINDOW" And any\tipe<>"TOOLBAR" And any\tipe<>"MENU"
	
	ShowGadget any\thumb[1]
	ShowGadget any\thumb[2]
	ShowGadget any\thumb[3]
	ShowGadget any\thumb[4]
		
	
	For thup = 1 To 4
	SetBuffer CanvasBuffer(any\thumb[thup])
	ClsColor 180,180,180
	Cls
	FlipCanvas any\thumb[thup]
	Next
	EndIf
	Next
	
	For any.gui = Each gui
		If any\name$=gadname$ Then
		def.gui = any.gui
		If any\tipe<>"MENU" Then ActivateGadget any\hand
		
		If any\tipe<>"MENU" Then
		any\xp = GadgetX(any\hand)
		any\yp = GadgetY(any\hand)
		any\wd = GadgetWidth(any\hand)
		any\ht = GadgetHeight(any\hand)
		EndIf
		
		;SetGadgetText tbtipe,any\tipe
		SetGadgetText tbname,any\name
		SetGadgetText tbxp,any\xp
		SetGadgetText tbyp,any\yp
		SetGadgetText tbwd,any\wd
		SetGadgetText tbht,any\ht
		SetGadgetText tbvalue,any\value
		SetGadgetText tbmisc,any\misc
		
		;SelectGadgetItem tbStyle,any\style
		SelectGadgetItem tbEnabled,any\enabled
		SelectGadgetItem tbVisible,any\visible
		ClearGadgetItems tbent 
		For t = 0 To any\entries
			AddGadgetItem tbent,any\tex$[t]
		Next
		SelectGadgetItem tbent, 0
		SetGadgetText tbhead, GadgetItemText$(tbent,0)
		
		SelectGadgetItem sel,selind(any\name)
		
		If any\tipe<>"WINDOW" And any\tipe<>"TOOLBAR" And any\tipe<>"MENU"
		For thup = 1 To 4
		SetBuffer CanvasBuffer(any\thumb[thup])
		ClsColor 0,0,0
		Cls
		FlipCanvas any\thumb[thup]
		Next
		SetHandles()
		EndIf
		
		If WhatType(any\hand)="WINDOW" Then
		ShowGadget StyleTitle
		ShowGadget StyleSize
		ShowGadget StyleMenu
		ShowGadget StyleStatus
		ShowGadget StyleTool
		ShowGadget stylelabel
		acm = (any\style And 4) 

		If (any\style And 1)<>0 Then SetButtonState StyleTitle,1 Else SetButtonState StyleTitle,0 
		If (any\style And 2)<>0 Then SetButtonState StyleSize,1 Else SetButtonState StyleSize,0 
		If (any\style And 4)<>0 Then SetButtonState StyleMenu,1 Else SetButtonState StyleMenu,0 
		If (any\style And 8)<>0 Then SetButtonState StyleStatus,1 Else SetButtonState StyleStatus,0 
		If (any\style And 16)<>0 Then SetButtonState StyleTool,1 Else SetButtonState StyleTool,0 		
		Else
		HideGadget StyleTitle
		HideGadget StyleSize
		HideGadget StyleMenu
		HideGadget StyleStatus
		HideGadget StyleTool 
		HideGadget stylelabel
		EndIf
		
		If any\node<>0 Then SelectTreeViewNode any\node 
		
		Select WhatType(any\hand)
		Case "WINDOW"
		SetStatusText Window,"Basic Window"+Chr$(13)+"Acts as Parent To all other gadgets. Can use the Style Flags. Receives Move, Size, minimise & Terminate Events."
		Case "LABEL"
		SetStatusText Window,"Text Label : Usually used to Title other Gadgets. It cannot act as a parent to other gadgets"
		Case "COMBO"
		SetStatusText Window,"Drop Down List : The Combo Box gadget allows the selection of a single item from a list. It cannot act as a parent to other gadgets"
		Case "LIST"
		SetStatusText Window,"List Gaddget : Allows the display and selection of a single item from a list. It cannot act as a parent to other gadgets."
		Case "CANVAS"
		SetStatusText Window,"Canvas Gadget : Versitile User Defined Gadget. Can act as parent to other gadgets and recieves click, key, and mouse movement events."
		Case "PANEL"
		SetStatusText Window,"Panel Gadget : Basic Container. Can act as parent to other gadgets."
		Case "TEXTBOX"
		SetStatusText Window,"Text Field Gadget : Text Entry gadget. Cannot parent other controls, receives key press events"
		Case "BUTTON"
		SetStatusText Window,"Basic Button Gadget : Action button. Cannot parent other controls, receives click events"
		Case "TABBER"
		SetStatusText Window,"Tabbed Dialog Gadget : Acts as parent to other gadgets, can receive click events on the tabs."
		Case "TOOLBAR"
		SetStatusText Window,"ToolBar Gadget : Can receive click events on the Buttons. Uses a single Image as the button faces."
		Case "HSLIDE"
		SetStatusText Window,"Horizontal Slider Gadget : Used to select a number between a minimum and maximum. Displays position in bar."
		Case "VSLIDE"
		SetStatusText Window,"Vertical Slider Gadget : Used to select a number between a minimum and maximum. Displays position in bar."
		Case "TREE"
		SetStatusText Window,"Tree View Gadget : Used to display Hierarchical information, such as folders on a hard drive."
		Case "CHECKBOX"
		SetStatusText Window,"CheckBox Gadget : A type of button. It revieves click events and marks the box accordingly. Returns True or False."
		Case "RADIO"
		SetStatusText Window,"Radio Button Gadget : A type of button. Used for multiple choice as all other Radio buttons in the Group will be cleared when another is clicked."
		Case "HTML"
		SetStatusText Window,"HTML View : A full featured HTML viewing window with both local and remote targetting."
		Case "TEXTAREA"
		SetStatusText Window,"TextArea : A text entry gadget, like TextField except multi line with RTF display capability."
		
		End Select
		LockStatus()
		Return
		EndIf
	Next
End Function

Function selind(nm$)
Local t
For t = 0 To CountGadgetItems(sel)
If GadgetItemText(sel,t)=nm$ Then Return t
Next
End Function



Function Sethandles()
ook.gui = def.GUI

For def.gui = Each gui
If def <> Null Then 
If def\tipe$<>"WINDOW" And def\tipe$<>"TOOLBAR" And def\tipe$<>"MENU" Then 

	SetGadgetShape def\thumb[1],GadgetX(def\hand)-5,GadgetY(def\hand)-5,5,5
	SetGadgetLayout def\thumb[1],def\LockLeft,def\LockRight,def\LockTop,def\LockBottom
	SetGadgetShape def\thumb[2],GadgetX(def\hand)-5,GadgetY(def\hand)+GadgetHeight(def\hand),5,5
	SetGadgetLayout def\thumb[2],def\LockLeft,def\LockRight,def\LockTop,def\LockBottom
	SetGadgetShape def\thumb[3],GadgetX(def\hand)+GadgetWidth(def\hand),GadgetY(def\hand)+GadgetHeight(def\hand),5,5
	SetGadgetLayout def\thumb[3],def\LockLeft,def\LockRight,def\LockTop,def\LockBottom
	SetGadgetShape def\thumb[4],GadgetX(def\hand)+GadgetWidth(def\hand),GadgetY(def\hand)-5,5,5
	SetGadgetLayout def\thumb[4],def\LockLeft,def\LockRight,def\LockTop,def\LockBottom

	SetGadgetShape theform,GadgetX(theform),GadgetY(theform),GadgetWidth(theform)-1,GadgetHeight(theform)-1
	SetGadgetShape theform,GadgetX(theform),GadgetY(theform),GadgetWidth(theform)+1,GadgetHeight(theform)+1
	
	ShowGadget def\thumb[1]
	ShowGadget def\thumb[2]
	ShowGadget def\thumb[3]
	ShowGadget def\thumb[4]

	FlipCanvas def\thumb[1]
	FlipCanvas def\thumb[2]
	FlipCanvas def\thumb[3]
	FlipCanvas def\thumb[4]
	
	EndIf
	EndIf
Next
def = ook
End Function

Function HideHandles()
For ook.gui = Each gui
If ook <> Null Then 
If ook\tipe$<>"WINDOW" And ook\tipe$<>"TOOLBAR" And ook\tipe$<>"MENU" Then 

	HideGadget ook\thumb[1]
	HideGadget ook\thumb[2]
	HideGadget ook\thumb[3]
	HideGadget ook\thumb[4]

	EndIf
	EndIf
Next
End Function

Function ReCreate()
RefreshSize()
KillEmAll()
For hoot.GUI = Each GUI
	If hoot\tipe = "WINDOW" Then 
	hoot\hand = CreateWindow(hoot\tex$[0],hoot\xp,hoot\yp,hoot\wd,hoot\ht,Desktop(),hoot\style)
	acm = (hoot\style And 4)
	hoot\thumb[1] = hoot\hand
	hoot\thumb[2] = hoot\hand
	hoot\thumb[3] = hoot\hand
	hoot\thumb[4] = hoot\hand
	theform = hoot\hand
	hoot\Create = Creation : Creation = Creation + 1
	GUIElement(hoot\name$, hoot\hand)
	EndIf
Next

End Function

Function KillEmAll()
Local a.GUI
For t=Creation To 0 Step -1
For a.gui = Each GUI
If a\create = t And a\hand<>0 Then
;Notify "C: "+a\create+" H: "+a\hand+" N: "+a\name$

If a\tipe<>"MENU" Then FreeGadget a\hand
If a\tipe<>"WINDOW" And a\tipe<>"TOOLBAR" And a\tipe$<>"MENU" Then
	FreeGadget a\thumb[1]
	FreeGadget a\thumb[2]
	FreeGadget a\thumb[3]
	FreeGadget a\thumb[4]
EndIf
EndIf
Next
Next

For a.gui = Each GUI
	If a\killme = 1 Then
		Orphan(a\name)
		Delete a
	EndIf
Next

Creation = 0 
End Function

Function Orphan(name$)
Local a.GUI
For a.GUI = Each GUI
If a\parentName$=Name$ Then 
	Orphan(a\name$)
	Delete a
EndIf
Next
End Function

Function GUIElement(name$, parent)
Local a.GUI

For a.GUI = Each GUI
If a\ParentName$ = name$ Then
Select a\tipe

Case "WINDOW"

a\hand = CreateWindow(a\tex[0],a\xp,a\yp,a\wd,a\ht,parent,a\style)
a\thumb[1] = a\hand
a\thumb[2] = a\hand
a\thumb[3] = a\hand
a\thumb[4] = a\hand

theform = a\hand
a\Create = Creation : Creation = Creation + 1
Case "TOOLBAR"
a\hand = CreateToolBar(a\tex[0],a\xp,a\yp,a\wd,a\ht,parent,a\style)
a\thumb[1] = a\hand 
a\thumb[2] = a\hand 
a\thumb[3] = a\hand 
a\thumb[4] = a\hand
a\Create = Creation : Creation = Creation + 1

Case "MENU"
If acm = 4 Then
If parent = theform Then parent = WindowMenu(theform)
a\hand = CreateMenu(a\tex[0],a\value,parent)
UpdateWindowMenu theform
a\thumb[1] = a\hand 
a\thumb[2] = a\hand 
a\thumb[3] = a\hand 
a\thumb[4] = a\hand
a\Create = Creation : Creation = Creation + 1
GUIElement(a\name$,a\hand)
EndIf
Case "LABEL"
a\thumb[1] = CreateCanvas(0,0,5,5,parent)
a\thumb[2] = CreateCanvas(0,0,5,5,parent)
a\thumb[3] = CreateCanvas(0,0,5,5,parent)
a\thumb[4] = CreateCanvas(0,0,5,5,parent)

a\hand = CreateLabel(a\tex[0],a\xp,a\yp,a\wd,a\ht,parent,a\style)
SetGadgetLayout a\hand,a\LockLeft,a\LockRight,a\LockTop,a\LockBottom
a\Create = Creation : Creation = Creation + 1

Case "COMBO"
a\thumb[1] = CreateCanvas(0,0,5,5,parent)
a\thumb[2] = CreateCanvas(0,0,5,5,parent)
a\thumb[3] = CreateCanvas(0,0,5,5,parent)
a\thumb[4] = CreateCanvas(0,0,5,5,parent)

a\hand = CreateComboBox(a\xp,a\yp,a\wd,a\ht,parent,a\style)

For t=0 To a\entries
	AddGadgetItem a\hand,a\tex$[t]
Next
SelectGadgetItem a\hand,0

SetGadgetLayout a\hand,a\LockLeft,a\LockRight,a\LockTop,a\LockBottom
a\Create = Creation : Creation = Creation + 1

Case "LIST"
a\thumb[1] = CreateCanvas(0,0,5,5,parent)
a\thumb[2] = CreateCanvas(0,0,5,5,parent)
a\thumb[3] = CreateCanvas(0,0,5,5,parent)
a\thumb[4] = CreateCanvas(0,0,5,5,parent)

a\hand = CreateListBox(a\xp,a\yp,a\wd,a\ht,parent,a\style)

For t=0 To a\entries
	AddGadgetItem a\hand,a\tex$[t]
Next
SelectGadgetItem a\hand,0

SetGadgetLayout a\hand,a\LockLeft,a\LockRight,a\LockTop,a\LockBottom
a\Create = Creation : Creation = Creation + 1
Case "CANVAS"
a\thumb[1] = CreateCanvas(0,0,5,5,parent)
a\thumb[2] = CreateCanvas(0,0,5,5,parent)
a\thumb[3] = CreateCanvas(0,0,5,5,parent)
a\thumb[4] = CreateCanvas(0,0,5,5,parent)

a\hand = CreateCanvas(a\xp,a\yp,a\wd,a\ht,parent,a\style)
SetGadgetLayout a\hand,a\LockLeft,a\LockRight,a\LockTop,a\LockBottom
SetBuffer CanvasBuffer(a\hand)
ClsColor 34,85,136
Cls
FlipCanvas a\hand
a\Create = Creation : Creation = Creation + 1

GUIElement(a\name$,a\hand)


Case "PBAR"
a\thumb[1] = CreateCanvas(0,0,5,5,parent)
a\thumb[2] = CreateCanvas(0,0,5,5,parent)
a\thumb[3] = CreateCanvas(0,0,5,5,parent)
a\thumb[4] = CreateCanvas(0,0,5,5,parent)

a\hand = CreateProgBar(a\xp,a\yp,a\wd,a\ht,parent,a\style)
SetGadgetLayout a\hand,a\LockLeft,a\LockRight,a\LockTop,a\LockBottom
a\Create = Creation : Creation = Creation + 1

Case "HTML"
a\thumb[1] = CreateCanvas(0,0,5,5,parent)
a\thumb[2] = CreateCanvas(0,0,5,5,parent)
a\thumb[3] = CreateCanvas(0,0,5,5,parent)
a\thumb[4] = CreateCanvas(0,0,5,5,parent)

a\hand = CreateHtmlView(a\xp,a\yp,a\wd,a\ht,parent,a\style)
SetGadgetLayout a\hand,a\LockLeft,a\LockRight,a\LockTop,a\LockBottom
a\Create = Creation : Creation = Creation + 1

Case "TEXTAREA"
a\thumb[1] = CreateCanvas(0,0,5,5,parent)
a\thumb[2] = CreateCanvas(0,0,5,5,parent)
a\thumb[3] = CreateCanvas(0,0,5,5,parent)
a\thumb[4] = CreateCanvas(0,0,5,5,parent)

a\hand = Createtextarea(a\xp,a\yp,a\wd,a\ht,parent,a\style)
SetGadgetLayout a\hand,a\LockLeft,a\LockRight,a\LockTop,a\LockBottom
a\Create = Creation : Creation = Creation + 1

Case "PANEL"
a\thumb[1] = CreateCanvas(0,0,5,5,parent)
a\thumb[2] = CreateCanvas(0,0,5,5,parent)
a\thumb[3] = CreateCanvas(0,0,5,5,parent)
a\thumb[4] = CreateCanvas(0,0,5,5,parent)

a\hand = CreatePanel(a\xp,a\yp,a\wd,a\ht,parent,a\style)
SetGadgetLayout a\hand,a\LockLeft,a\LockRight,a\LockTop,a\LockBottom
a\Create = Creation : Creation = Creation + 1

GUIElement(a\name$,a\hand)

Case "HSLIDE"
a\thumb[1] = CreateCanvas(0,0,5,5,parent)
a\thumb[2] = CreateCanvas(0,0,5,5,parent)
a\thumb[3] = CreateCanvas(0,0,5,5,parent)
a\thumb[4] = CreateCanvas(0,0,5,5,parent)

a\hand = CreateSlider(a\xp,a\yp,a\wd,a\ht,parent,a\style)
SetGadgetLayout a\hand,a\LockLeft,a\LockRight,a\LockTop,a\LockBottom
a\Create = Creation : Creation = Creation + 1

Case "VSLIDE"
a\thumb[1] = CreateCanvas(0,0,5,5,parent)
a\thumb[2] = CreateCanvas(0,0,5,5,parent)
a\thumb[3] = CreateCanvas(0,0,5,5,parent)
a\thumb[4] = CreateCanvas(0,0,5,5,parent)

a\hand = CreateSlider(a\xp,a\yp,a\wd,a\ht,parent,a\style)
SetGadgetLayout a\hand,a\LockLeft,a\LockRight,a\LockTop,a\LockBottom
a\Create = Creation : Creation = Creation + 1
Case "TREE"
a\thumb[1] = CreateCanvas(0,0,5,5,parent)
a\thumb[2] = CreateCanvas(0,0,5,5,parent)
a\thumb[3] = CreateCanvas(0,0,5,5,parent)
a\thumb[4] = CreateCanvas(0,0,5,5,parent)

a\hand = CreateTreeView(a\xp,a\yp,a\wd,a\ht,parent,a\style)

root=TreeViewRoot(a\hand)
any = AddTreeViewNode("TreeView Gadget",root)
any = AddTreeViewNode(a\ParentName$,any)
any = AddTreeViewNode(a\name$,any)

SetGadgetLayout a\hand,a\LockLeft,a\LockRight,a\LockTop,a\LockBottom
a\Create = Creation : Creation = Creation + 1
Case "TEXTFIELD"
a\thumb[1] = CreateCanvas(0,0,5,5,parent)
a\thumb[2] = CreateCanvas(0,0,5,5,parent)
a\thumb[3] = CreateCanvas(0,0,5,5,parent)
a\thumb[4] = CreateCanvas(0,0,5,5,parent)

a\hand = CreateTextField(a\xp,a\yp,a\wd,a\ht,parent,a\style)

SetGadgetText a\hand,a\tex$[0]

SetGadgetLayout a\hand,a\LockLeft,a\LockRight,a\LockTop,a\LockBottom
a\Create = Creation : Creation = Creation + 1
Case "BUTTON"
a\thumb[1] = CreateCanvas(0,0,5,5,parent)
a\thumb[2] = CreateCanvas(0,0,5,5,parent)
a\thumb[3] = CreateCanvas(0,0,5,5,parent)
a\thumb[4] = CreateCanvas(0,0,5,5,parent)

a\hand = CreateButton(a\tex[0],a\xp,a\yp,a\wd,a\ht,parent,a\style)
SetGadgetLayout a\hand,a\LockLeft,a\LockRight,a\LockTop,a\LockBottom
a\Create = Creation : Creation = Creation + 1
Case "CHECKBOX"
a\thumb[1] = CreateCanvas(0,0,5,5,parent)
a\thumb[2] = CreateCanvas(0,0,5,5,parent)
a\thumb[3] = CreateCanvas(0,0,5,5,parent)
a\thumb[4] = CreateCanvas(0,0,5,5,parent)

a\hand = CreateButton(a\tex[0],a\xp,a\yp,a\wd,a\ht,parent,a\style)
SetGadgetLayout a\hand,a\LockLeft,a\LockRight,a\LockTop,a\LockBottom
a\Create = Creation : Creation = Creation + 1
Case "RADIO"
a\thumb[1] = CreateCanvas(0,0,5,5,parent)
a\thumb[2] = CreateCanvas(0,0,5,5,parent)
a\thumb[3] = CreateCanvas(0,0,5,5,parent)
a\thumb[4] = CreateCanvas(0,0,5,5,parent)

a\hand = CreateButton(a\tex[0],a\xp,a\yp,a\wd,a\ht,parent,a\style)
SetGadgetLayout a\hand,a\LockLeft,a\LockRight,a\LockTop,a\LockBottom
a\Create = Creation : Creation = Creation + 1
Case "TABBER"
a\thumb[1] = CreateCanvas(0,0,5,5,parent)
a\thumb[2] = CreateCanvas(0,0,5,5,parent)
a\thumb[3] = CreateCanvas(0,0,5,5,parent)
a\thumb[4] = CreateCanvas(0,0,5,5,parent)

a\hand = CreateTabber(a\xp,a\yp,a\wd,a\ht,parent,a\style)

For t=0 To a\entries
	AddGadgetItem a\hand,a\tex$[t]
Next
SelectGadgetItem a\hand,0

SetGadgetLayout a\hand,a\LockLeft,a\LockRight,a\LockTop,a\LockBottom
a\Create = Creation : Creation = Creation + 1
GUIElement(a\name$,a\hand)
End Select

If a <> Null Then 
If a\tipe$<>"WINDOW" And a\TIPE$<>"TOOLBAR" And a\tipe$<>"MENU"Then 

	SetGadgetShape a\thumb[1],GadgetX(a\hand)-5,GadgetY(a\hand)-5,5,5
	SetGadgetLayout a\thumb[1],a\LockLeft,a\LockRight,a\LockTop,a\LockBottom
	SetGadgetShape a\thumb[2],GadgetX(a\hand)-5,GadgetY(a\hand)+GadgetHeight(a\hand),5,5
	SetGadgetLayout a\thumb[2],a\LockLeft,a\LockRight,a\LockTop,a\LockBottom
	SetGadgetShape a\thumb[3],GadgetX(a\hand)+GadgetWidth(a\hand),GadgetY(a\hand)+GadgetHeight(a\hand),5,5
	SetGadgetLayout a\thumb[3],a\LockLeft,a\LockRight,a\LockTop,a\LockBottom
	SetGadgetShape a\thumb[4],GadgetX(a\hand)+GadgetWidth(a\hand),GadgetY(a\hand)-5,5,5
	SetGadgetLayout a\thumb[4],a\LockLeft,a\LockRight,a\LockTop,a\LockBottom

	SetGadgetShape theform,GadgetX(theform),GadgetY(theform),GadgetWidth(theform)-1,GadgetHeight(theform)-1
	SetGadgetShape theform,GadgetX(theform),GadgetY(theform),GadgetWidth(theform)+1,GadgetHeight(theform)+1
	
	FlipCanvas a\thumb[1]
	FlipCanvas a\thumb[2]
	FlipCanvas a\thumb[3]
	FlipCanvas a\thumb[4]

EndIf
EndIf

EndIf
Next
 
End Function

Function LockStatus()
If lockup=0 Then Return
SetBuffer CanvasBuffer(Lockup)

Color 192,192,192
Rect 1,1,60,62

If def\LockLeft = 1 Then DrawBlock Lock,0,16 Else DrawBlock Unlock,0,16
If def\LockBottom = 1 Then DrawBlock Lock,21,32 Else DrawBlock Unlock,21,32
If def\LockTop = 1 Then DrawBlock Lock,21,0 Else DrawBlock Unlock,21,0
If def\LockRight = 1 Then DrawBlock Lock,41,16 Else DrawBlock Unlock,41,16

FlipCanvas lockup

End Function

Function Inrect(xpo,ypo,stx,sty,swd,sht)
If xpo>=stx Then
If ypo>=sty Then
If xpo<=(stx+swd) Then
If ypo<=(sty+sht) Then
Return True
EndIf
EndIf
EndIf
EndIf
Return False
End Function

Function ShowStruct()
If struct<>0 Then FreeGadget Struct
Struct = CreateTreeView(50,345,175,140,Panel)
RootStruct = TreeViewRoot(Struct)

For a.gui = Each gui
If a\tipe = "WINDOW" Then
	a\node = AddTreeViewNode(a\name,RootStruct)
	StItem(a\name,a\node)
EndIf
Next
For ook.gui = Each gui
If ook\node<>0 Then SelectTreeViewNode ook\node
Next
If def<> Null Then
	If def\node<>0 Then SelectTreeViewNode def\node 
EndIf

End Function

Function StItem(name$,parent)
Local a.gui

For a.gui = Each gui
If a\parentname$=name$ Then
If a\tipe = "MENU" And acm = 0 Then 
	a\node = 0
	Goto naah
EndIf
a\node = AddTreeViewNode(a\name,parent)
STItem(A\name,a\node)
.naah
EndIf
Next
End Function 

Function SaveCode(FileName$)
RefreshSize()
wongle$="Anything"
For hoot.GUI = Each GUI
	If hoot\tipe = "WINDOW" Then 
 	wongle$ = hoot\name$
	Exit
	EndIf
Next 
If filename$="" Then Return
If Right(Upper$(filename$),3)<>".BB" Then filename$=filename$+".BB"

;CodeFile = WriteFile(isopath(Filename$)+wongle$+".bb")
CodeFile = WriteFile(Filename$)

For hoot.gui = Each GUI
	WriteLine CodeFile,"Global "+hoot\name$
Next

For hoot.GUI = Each GUI
	If hoot\tipe = "WINDOW" Then 
	WriteLine CodeFile,"Create"+Hoot\name$+"()"
	EndIf
Next 
WriteLine CodeFile,"While WaitEvent()<>$803"
WriteLine CodeFile,"; Do Something"
WriteLine CodeFile,"Wend"



WriteLine CodeFile,"; ----Function Begins----"

For hoot.GUI = Each GUI
	If hoot\tipe = "WINDOW" Then 
	WriteLine CodeFile, "Function Create"+Hoot\name$+"()"
	WriteLine CodeFile,Hoot\Name$+" = CreateWindow("+Chr$(34)+hoot\tex$[0]+Chr$(34)+","+hoot\xp+","+hoot\yp+","+hoot\wd+","+hoot\ht+",Desktop(),"+hoot\style+")"
	CodeElement(hoot\name$, hoot\hand)
	EndIf
Next

WriteLine CodeFile,"End Function"
CloseFile CodeFile

End Function

Function CodeElement(name$, parent)
Local a.GUI

For a.GUI = Each GUI
If a\ParentName$ = name$ Then
Select a\tipe

Case "WINDOW"
WriteLine CodeFile, a\name$ +" = CreateWindow("+Chr$(34)+a\tex[0]+Chr$(34)+","+a\xp+","+a\yp+","+a\wd+","+a\ht+","+a\parentname+","+a\style+")"
Case "TOOLBAR"
WriteLine CodeFile, a\name$ +" = CreateToolBar("+Chr$(34)+a\tex[0]+Chr$(34)+","+a\xp+","+a\yp+","+a\wd+","+a\ht+","+a\parentname$+","+a\style+")"
Case "MENU"
DebugLog nameType(a\parentname$)
If nametype(a\parentname$)="WINDOW" Then 
WriteLine CodeFile, a\name$ +" = CreateMenu("+Chr$(34)+a\tex[0]+Chr$(34)+","+a\value+",WindowMenu("+a\parentname+"))"
rainbow$ = "UpdateWindowMenu "+a\parentname
Else
WriteLine CodeFile, a\name$ +" = CreateMenu("+Chr$(34)+a\tex[0]+Chr$(34)+","+a\value+","+a\parentname+")"
EndIf
CodeElement(a\name$,a\hand)
WriteLine CodeFile, rainbow$
Case "LABEL"
WriteLine CodeFile, a\name$ +" = CreateLabel("+Chr$(34)+a\tex[0]+Chr$(34)+","+a\xp+","+a\yp+","+a\wd+","+a\ht+","+a\parentname+","+a\style+")"
WriteLine CodeFile, "SetGadgetLayout "+a\name$+","+a\LockLeft+","+a\LockRight+","+a\LockTop+","+a\LockBottom
Case "COMBO"
WriteLine CodeFile, a\name$ +" = CreateComboBox("+a\xp+","+a\yp+","+a\wd+","+a\ht+","+a\parentname+","+a\style+")"
For t=0 To a\entries
WriteLine CodeFile, "AddGadgetItem "+a\name$+","+Chr$(34)+a\tex$[t]+Chr$(34)
Next
WriteLine CodeFile, "SelectGadgetItem "+a\name$+",0"
WriteLine CodeFile, "SetGadgetLayout "+a\name$+","+a\LockLeft+","+a\LockRight+","+a\LockTop+","+a\LockBottom
Case "LIST"
WriteLine CodeFile, a\name$ +" = CreateListBox("+a\xp+","+a\yp+","+a\wd+","+a\ht+","+a\parentname+","+a\style+")"
For t=0 To a\entries
WriteLine CodeFile, "AddGadgetItem "+a\name$+","+Chr$(34)+a\tex$[t]+Chr$(34)
Next
WriteLine CodeFile, "SelectGadgetItem "+a\name$+",0"
WriteLine CodeFile, "SetGadgetLayout "+a\name$+","+a\LockLeft+","+a\LockRight+","+a\LockTop+","+a\LockBottom
Case "CANVAS"
WriteLine CodeFile, a\name$ +" = CreateCanvas("+a\xp+","+a\yp+","+a\wd+","+a\ht+","+a\parentname+","+a\style+")"
WriteLine CodeFile, "SetGadgetLayout "+a\name$+","+a\LockLeft+","+a\LockRight+","+a\LockTop+","+a\LockBottom
WriteLine CodeFile, "Setbuffer Canvasbuffer("+a\name$+")"
WriteLine CodeFile, "ClsColor 34,85,136"
WriteLine CodeFile, "Cls"
WriteLine CodeFile, "FlipCanvas "+a\name$
CodeElement(a\name$,a\hand)
Case "PANEL"
WriteLine CodeFile, a\name$ +" = CreatePanel("+a\xp+","+a\yp+","+a\wd+","+a\ht+","+a\parentname$+","+a\style+")"
WriteLine CodeFile, "SetGadgetLayout "+a\name$+","+a\LockLeft+","+a\LockRight+","+a\LockTop+","+a\LockBottom
CodeElement(a\name$,a\hand)
Case "HTML"
WriteLine CodeFile, a\name$ +" = CreateHTMLView("+a\xp+","+a\yp+","+a\wd+","+a\ht+","+a\parentname$+","+a\style+")"
WriteLine CodeFile, "SetGadgetLayout "+a\name$+","+a\LockLeft+","+a\LockRight+","+a\LockTop+","+a\LockBottom
Case "TEXTAREA"
WriteLine CodeFile, a\name$ +" = CreateTextArea("+a\xp+","+a\yp+","+a\wd+","+a\ht+","+a\parentname$+","+a\style+")"
WriteLine CodeFile, "SetGadgetLayout "+a\name$+","+a\LockLeft+","+a\LockRight+","+a\LockTop+","+a\LockBottom
Case "HSLIDE"
WriteLine CodeFile, a\name$ +" = CreateSlider("+a\xp+","+a\yp+","+a\wd+","+a\ht+","+a\parentname+","+a\style+")"
WriteLine CodeFile, "SetGadgetLayout "+a\name$+","+a\LockLeft+","+a\LockRight+","+a\LockTop+","+a\LockBottom
Case "PBAR"
WriteLine CodeFile, a\name$ +" = CreateProgBar("+a\xp+","+a\yp+","+a\wd+","+a\ht+","+a\parentname+","+a\style+")"
WriteLine CodeFile, "SetGadgetLayout "+a\name$+","+a\LockLeft+","+a\LockRight+","+a\LockTop+","+a\LockBottom
Case "VSLIDE"
WriteLine CodeFile, a\name$ +" = CreateSlider("+a\xp+","+a\yp+","+a\wd+","+a\ht+","+a\parentname+","+a\style+")"
WriteLine CodeFile, "SetGadgetLayout "+a\name$+","+a\LockLeft+","+a\LockRight+","+a\LockTop+","+a\LockBottom
Case "TREE"
WriteLine CodeFile, a\name$ +" = CreateTreeView("+a\xp+","+a\yp+","+a\wd+","+a\ht+","+a\parentname+","+a\style+")"
WriteLine CodeFile, "SetGadgetLayout "+a\name$+","+a\LockLeft+","+a\LockRight+","+a\LockTop+","+a\LockBottom
Case "TEXTFIELD"
WriteLine CodeFile, a\name$ +" = CreateTextField("+a\xp+","+a\yp+","+a\wd+","+a\ht+","+a\parentname+","+a\style+")"
WriteLine CodeFile, "SetGadgetText "+a\name$+","+Chr$(34)+a\tex$[0]+Chr$(34)
WriteLine CodeFile, "SetGadgetLayout "+a\name$+","+a\LockLeft+","+a\LockRight+","+a\LockTop+","+a\LockBottom
Case "BUTTON"
WriteLine CodeFile, a\name$ +" = CreateButton("+Chr$(34)+a\tex[0]+Chr$(34)+","+a\xp+","+a\yp+","+a\wd+","+a\ht+","+a\parentname+","+a\style+")"
WriteLine CodeFile, "SetGadgetLayout "+a\name$+","+a\LockLeft+","+a\LockRight+","+a\LockTop+","+a\LockBottom
Case "CHECKBOX"
WriteLine CodeFile, a\name$ +" = CreateButton("+Chr$(34)+a\tex[0]+Chr$(34)+","+a\xp+","+a\yp+","+a\wd+","+a\ht+","+a\parentname+","+a\style+")"
WriteLine CodeFile, "SetGadgetLayout "+a\name$+","+a\LockLeft+","+a\LockRight+","+a\LockTop+","+a\LockBottom
Case "RADIO"
WriteLine CodeFile, a\name$ +" = CreateButton("+Chr$(34)+a\tex[0]+Chr$(34)+","+a\xp+","+a\yp+","+a\wd+","+a\ht+","+a\parentname+","+a\style+")"
WriteLine CodeFile, "SetGadgetLayout "+a\name$+","+a\LockLeft+","+a\LockRight+","+a\LockTop+","+a\LockBottom
Case "TABBER"
WriteLine CodeFile, a\name$ +" = CreateTabber("+a\xp+","+a\yp+","+a\wd+","+a\ht+","+a\parentname$+","+a\style+")"

For t=0 To a\entries
WriteLine CodeFile, "AddGadgetItem "+a\name$+","+Chr$(34)+a\tex$[t]+Chr$(34)
Next
WriteLine CodeFile, "SelectGadgetItem "+a\name$+",0"
WriteLine CodeFile, "SetGadgetLayout "+a\name$+","+a\LockLeft+","+a\LockRight+","+a\LockTop+","+a\LockBottom
CodeElement(a\name$,a\hand)
End Select
EndIf
Next
End Function

Function isopath$(wha$)
    For t=Len(wha$) To 1 Step -1
        If Mid$(wha$,t,1)="\" Then
            Return Left$(wha$,t)
        EndIf
    Next
    Return ""
End Function


Function OpenGUI(filename$)
If filename$="" Or FileType(filename$)=0 Then SetStatusText window, "Open aborted" : Return
size = FileSize(filename$)
infile = ReadFile(filename$)
csize = ReadInt(infile)
If size <> csize Then 
CloseFile infile
SetStatusText window, "This is not a Valid GUI File"
Return
EndIf
CloseFile infile
killemall()
Local hoot.gui
For hoot.gui = Each gui
Delete hoot
Next
infile = ReadFile(filename$)
size = ReadInt(infile)
counter = ReadInt(infile)
For t=1 To counter
	hoot.gui = New gui
	hoot\tipe$ = ReadString(infile)
	hoot\name$= ReadString(infile)
	hoot\xp= ReadInt(infile)
	hoot\yp= ReadInt(infile)
	hoot\wd= ReadInt(infile)
	hoot\ht= ReadInt(infile)
	hoot\entries= ReadInt(infile)
	For a = 0 To hoot\entries
		hoot\tex$[a] = ReadString(infile)
	Next 
	hoot\Style= ReadInt(infile)
	hoot\ParentName$ = ReadString(infile)
	hoot\LockLeft= ReadInt(infile)
	hoot\LockRight= ReadInt(infile)
	hoot\LockTop= ReadInt(infile)
	hoot\LockBottom= ReadInt(infile)
	hoot\Create= ReadInt(infile)
	hoot\misc= ReadInt(infile)
	hoot\Enabled= ReadInt(infile)
	hoot\Visible= ReadInt(infile)
	hoot\value= ReadInt(infile)
Next
CloseFile infile
recreate()
showstruct()
End Function

Function SaveGUI(filename$)
RefreshSize()
If filename$="" Then SetStatusText window, "Save aborted" : Return
Local hoot.gui
For hoot.gui = Each gui
counter = counter+1
Next
infile = WriteFile(filename$)
WriteInt Infile,0
WriteInt infile,counter 
For hoot.gui = Each gui

	WriteString infile,hoot\tipe$
	WriteString infile,hoot\name$
	WriteInt Infile, hoot\xp
	WriteInt Infile, hoot\yp
	WriteInt Infile, hoot\wd
	WriteInt Infile, hoot\ht
	WriteInt Infile, hoot\entries
	For a = 0 To hoot\entries
	WriteString infile,	hoot\tex$[a]
	Next 
	WriteInt Infile, hoot\Style
	WriteString infile,hoot\ParentName$
	WriteInt Infile, hoot\LockLeft
	WriteInt Infile, hoot\LockRight
	WriteInt Infile, hoot\LockTop
	WriteInt Infile, hoot\LockBottom
	WriteInt Infile, hoot\Create
	WriteInt Infile, hoot\misc
	WriteInt Infile, hoot\Enabled
	WriteInt Infile, hoot\Visible
	WriteInt Infile, hoot\value
Next
CloseFile infile
size = FileSize(filename$)
Infile = OpenFile (filename$)
WriteInt Infile,size
CloseFile infile
End Function

Function RefreshSize()
Return
For ook.GUI = Each gui
If ook\tipe<>"MENU" And ook\tipe<>"TOOLBAR"
	ook\xp = GadgetX(ook\hand)
	ook\yp = GadgetY(ook\hand)
	ook\wd = GadgetWidth(ook\hand)
	ook\ht = GadgetHeight(ook\hand)
EndIf
Next
End Function