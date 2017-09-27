; Demos the Input_GetText function (see below) 


; ----------------------------------------------------------------------------- 
; Event constants...snagged this from the forum...was under simon@acid's name 
; ----------------------------------------------------------------------------- 

Const EVENT_None = $0 ; No event (eg. a WaitEvent timeout) 
Const EVENT_KeyDown = $101 ; Key pressed 
Const EVENT_KeyUp = $102 ; Key released 
Const EVENT_ASCII = $103 ; ASCII key pressed 
Const EVENT_MouseDown = $201 ; Mouse button pressed 
Const EVENT_MouseUp = $202 ; Mouse button released 
Const EVENT_MouseMove = $203 ; Mouse moved 
Const EVENT_Gadget = $401 ; Gadget clicked 
Const EVENT_Move = $801 ; Window moved 
Const EVENT_Size = $802 ; Window resized 
Const EVENT_Close = $803 ; Window closed 
Const EVENT_Front = $804 ; Window brought to front 
Const EVENT_Menu = $1001 ; Menu item selected 
Const EVENT_LostFocus = $2001 ; App lost focus 
Const EVENT_GotFocus = $2002 ; App got focus 
Const EVENT_Timer = $4001 ; Timer event occurred 


Const KB_Enter = 13 ; the carriage return key 


;;;;;; DEMO STUFF ;;;;;; 
Graphics 640,480,32,3 

Cls 
TextValue$ = Input_GetText$("Test","Enter some value here!","",25,400,400) 
Text 0,0,"Value entered: " + TextValue$ 
Text 0,16,"Press a key for the next input field..." 
WaitKey 
TextValue$ = Input_GetText$("Test","Enter some value here, notice your last text is in here and the window is bigger cause of this huge label!",TextValue$,25,400,500) 
Text 0,32,"Value entered: " + TextValue$ 
Text 0,48,"Press a key to exit" 
WaitKey 
End 
;;;;; END DEMO ;;;;;; 




;*************************************************************************** 
;* Title: Input_GetText 
;* 
;* Description: Puts up an input box for text. Allows the user to name 
;* the window, put up a label, and set the maximum size of 
;* the text coming in. It also has an OK and Cancel button. 
;* 
;*************************************************************************** 
;* Coded By: Krylar (with help from the integerfield.bb sample!) 
;* Last Updated: 2/5/03 
;* 
;*************************************************************************** 
;* Arguments: 
;* Input_Window_Name$ = What the name will be called 
;* Input_Label_Text$ = What the text will say in the window...label text 
;* Input_Default_Text$ = If there is any default text to show in the field 
;* Input_MaxTextSize = What the max number of chars to accept as input 
;* Input_X = The X location of the window 
;* Input_Y = The Y location of the window 
;* 
;* Returns: 
;* Default Text, updated text, or "" 
;* 
;*************************************************************************** 

Const INPUT_FONT_WIDTH = 5 ; until FontWidth() is working again...adjust accordingly 

Function Input_GetText$(Input_WindowName$,Input_LabelText$,Input_Default_Text$,Input_MaxTextSize,Input_X,Input_Y) 

; make sure our text is blank to start with 
Input_TextValue$ = "" 

; Since we want the label to show properly, figure out it's length 
; BEFORE creating the window size 
Input_Label_Size = Len(Input_LabelText$) * INPUT_FONT_WIDTH 
If Input_Label_Size < 150 
Input_Label_Size = 150 
EndIf 

; Set it up so the field width is equal to the size of the max allowed chars 
Input_Text_Width = (Input_MaxTextSize + INPUT_FONT_WIDTH) * INPUT_FONT_WIDTH 

; create a window with the width being the label size + 10 (for some buffer room) 
Input_Text_Window = CreateWindow(Input_WindowName$,Input_X,Input_Y,Input_Label_Size + 10,150) 

; slap up the label text that the user wanted 
Input_Label_Text = CreateLabel(Input_LabelText$,1,1,Input_Label_Size,FontHeight(),Input_Text_Window) 

; Create a text field for entry. Should sit in the center horizontally and be 
; about the width of the max allowed chars (unless you use all M's or W's, of course) 
Input_Text_Field = CreateTextField( ClientWidth(Input_Text_Window)/2 - (Input_Text_Width/2),ClientHeight(Input_Text_Window)/2-12,Input_Text_Width,24,Input_Text_Window ) 

; create OK and Cancel buttons 
Input_OK_Button = CreateButton("Ok",ClientWidth(Input_Text_Window)/2-60,ClientHeight(Input_Text_Window)-22,50,20,Input_Text_Window) 
Input_Cancel_Button = CreateButton("Cancel",ClientWidth(Input_Text_Window)/2+10,ClientHeight(Input_Text_Window)-22,50,20,Input_Text_Window) 

; format, activate, and fill in any default text in the text field 
SetGadgetLayout Input_Text_Field,1,1,0,0 
ActivateGadget Input_Text_Field 
SetGadgetText Input_Text_Field,Input_Default_Text$ 

While WaitEvent()<>EVENT_Close 

If EventID()=EVENT_Gadget 

; if the user hits enter or the okay button 
If EventData()=KB_Enter Or EventSource()=Input_OK_Button 
; release the gadgets and return the value 
FreeGadget Input_Text_Field 
FreeGadget Input_Text_Window 
Return Input_TextValue$ 
EndIf 

; if the user hits the cancel button 
If EventSource()=Input_Cancel_Button 
; release the gadgets 
FreeGadget Input_Text_Field 
FreeGadget Input_Text_Window 
; see if the Default text sent across had something in it 
If Len(Input_Default_Text$) > 0 
; if so, return that value 
Return(Input_Default_Text$) 
Else 
; otherwise return blank 
Return "" 
EndIf 
EndIf 

; check the text field to see if it was active 
If EventSource()=Input_Text_Field 
; grab the latest text 
Input_TextValue$=TextFieldText$(Input_Text_Field) 
; see if any text has been entered 
If Len(Input_TextValue$) > 0 
; make sure it's not larger than the Max allowable chars 
If Len(Input_TextValue$) > Input_MaxTextSize 
; if it is, cut it back 
Input_TextValue$ = Left$(Input_TextValue,Input_MaxTextSize) 
EndIf 
; reset the gadget text 
SetGadgetText Input_Text_Field,Input_TextValue$ 
Else 
; set the gadget text to blank 
SetGadgetText Input_Text_Field,"" 
EndIf 
EndIf 
EndIf 
Wend 

; the user must have clicked the "x" to close the window 
; free the gadgets 
FreeGadget Input_Text_Field 
FreeGadget Input_Text_Window 
; see if the Default text sent across had something in it 
If Len(Input_Default_Text$) > 0 
; if so, return that value 
Return(Input_Default_Text$) 
Else 
; otherwise return blank 
Return "" 
EndIf 

End Function 