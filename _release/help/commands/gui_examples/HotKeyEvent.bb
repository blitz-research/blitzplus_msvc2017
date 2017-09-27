;create a window
window=CreateWindow( "",0,0,640,480 )

;create file menu
file_menu=CreateMenu( "File",0,WindowMenu(window) )

;create open menu
open_menu=CreateMenu( "Open",1,file_menu )

;update menus
UpdateWindowMenu window

;create hotkey - F1 generates a menu_action event with EventData() of 1
HotKeyEvent 59,0,$1001,1

;another hotkey - ESC generates a window_close event from the main window
HotKeyEvent 1,0,$803,0,0,0,0,window

;let 'em know what we're up to!
Notify "Hot keys installed - ESC to close window, F1 to select File/Open menu"

While WaitEvent()<>$803		;loop until window closed
	If EventID()=$1001		;menu event?
		If EventData()=1	;file/open menu?
			Notify "File/Open selected!"
		EndIf
	EndIf
Wend