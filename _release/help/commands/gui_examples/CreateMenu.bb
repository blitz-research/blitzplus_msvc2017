; This example is intended to demonstrate the following commands:
;    CreateMenu, WindowMenu, UpdateWindowMenu, DisableMenu, EnableMenu, CheckMenu and UncheckMenu

; Firstly, create the window to which our menus will be attached
WinHandle=CreateWindow("Menu Example",0,0,400,200)

; Identify the 'root' menu for the window that we just created.  All our menus must be attached to this
menu=WindowMenu(WinHandle)

; Now create a whole bunch of menus and sub-items - first of all the FILE menu
file=CreateMenu("File",0,menu) ; main menu
CreateMenu "Open",1,file ; child menu 
CreateMenu "",0,file ; Use an empty string to generate separator bars
CreateMenu "Quit",3,file ; another child menu

; Now the Edit menu
edit=CreateMenu("&Edit",0,menu) ; Main menu with Alt Shortcut - Use & to specify the shortcut key
CreateMenu "Cu&t"+Chr$(8)+"Ctrl-X",6,edit ; Another Child menu with Alt Shortcut
CreateMenu "&Copy"+Chr$(8)+"Ctrl-C",4,edit ; Child menu with Alt Shortcut
CreateMenu "&Paste"+Chr$(8)+"Ctrl-V",5,edit ; Another Child menu with Alt Shortcut
HotKeyEvent 46,2,$1001,4
HotKeyEvent 47,2,$1001,5
HotKeyEvent 45,2,$1001,6

; Now a menu to demo disabled items
future1=CreateMenu("Disabled Items",0,menu) ; Main menu
Item7=CreateMenu("ITEM",7,future1) ; This menu we create with handle in order to control it.
CreateMenu "Disable ITEM",8,future1 ; Will be used to disable ITEM
CreateMenu "Enable ITEM",9,future1 ; Will be used to enable ITEM
DisableMenu Item7 ; we wish to start with the ITEM menu item disabled.

; And a menu to demo checked items
future2=CreateMenu("Check Items",0,menu) ; Main menu
Item10=CreateMenu("ITEM",10,future2) ; This menu we create with handle in order to control it.
CreateMenu "Check ITEM",11,future2;Check demo
CreateMenu "Uncheck ITEM",12,future2;Check demo
CheckMenu Item10 ; we wish to start with the ITEM menu item checked

; Finally, once all menus are set up / updated, we call UpdateWindowMenu to tell the OS about the menu
UpdateWindowMenu WinHandle

; At last!  We can now start up a simple application loop to control the demo application.
Repeat
	; Wait for an event to occur...
	id=WaitEvent()
	
	; exit on a window close event
	If ID=$803 Then End 

	; handle a menu event
	If ID=$1001 Then
		EID=EventData() ; Event data contains the menu ID specified when creating the menu items
		Select EID
			Case 1
				Notify "Open selected"
			Case 3
				End
			Case 4
				Notify "Copy selected"
			Case 5
				Notify "Paste selected"
			Case 6
				Notify "Cut selected"
			Case 7
				Notify "ITEM selected"
			Case 8
				; disable the menu item and notify the OS of the update to the window menu
				DisableMenu Item7
				UpdateWindowMenu WinHandle 
			Case 9
				; enable the menu item and notify the OS of the update to the window menu
				EnableMenu Item7
				UpdateWindowMenu WinHandle
			Case 10
				Notify "ITEM selected"
				; note that the item is NOT automatically checked / unchecked by selecting it
			Case 11
				; Check the menu item
				CheckMenu Item10
				UpdateWindowMenu WinHandle
			Case 12
				; Uncheck the menu item
				UncheckMenu Item10
				UpdateWindowMenu WinHandle
		End Select
	End If
Forever
End  ; bye!