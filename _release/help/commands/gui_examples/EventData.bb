; Example originally provided by Mag, tidied up by Mark Tiffany

; First, we create a window
WinHandle=CreateWindow("Window, Menu and EventData Example",0,0,400,200) 

; Now we set up the menus.  Two menus, File and Edit, with different sub menu items.
menu=WindowMenu(WinHandle) 

; Set up the first main menu.  
; Note that the second parameter specifies a unique ID For the menu item.  
; This will be returned to us in EventData() when the event occurs.
file=CreateMenu("File",0,menu) 
CreateMenu "Open",1,file ; the first child menu item
CreateMenu "Shortcut F5",2,file ;child menu with shortcut sample 
CreateMenu "",0,file ; a separator can be implemented by leaving the text blank
CreateMenu "Quit",3,file ; and another child menu item

; Now set up the second menu.  
; Note that ALT shortcuts have been enabled by prefixing the desired letter with an ampersand "&"
edit=CreateMenu("&Edit",0,menu)
CreateMenu "&Copy",4,edit 
CreateMenu "&Paste",5,edit
CreateMenu "Cu&t",6,edit
UpdateWindowMenu WinHandle 

; Now that we have set up the window and menus, we need to a main loop to handle their events
Repeat 
id=WaitEvent()
If ID=$803 Then Exit ; Handle the close gadget on the window being hit
If ID=$1001 Then ; Handle any menu item hit events
; extract the EventData as this will contain our unique id for the menu item
EID=EventData() 
If EID=1 Then Notify "Open selected" 
If EID=2 Then Notify "Shortcut selected" 
If EID=3 Then Notify "Quit selected": Exit
If EID=4 Then Notify "Copy selected" 
If EID=5 Then Notify "Paste selected" 
If EID=6 Then Notify "Cut selected"
End If 
Forever 
End ; bye!