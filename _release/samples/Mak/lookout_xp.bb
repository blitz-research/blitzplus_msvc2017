; -----------------------------------------------------------------------------
; It's crude! It's ugly! It's Lookout Express !
; -----------------------------------------------------------------------------
; james @ hi - toro . com
; -----------------------------------------------------------------------------

; -----------------------------------------------------------------------------
; Event constants...
; -----------------------------------------------------------------------------

Const EVENT_KeyDown		= $101		; Key pressed
Const EVENT_KeyUp		= $102		; Key released
Const EVENT_KeyChar		= $103		; Key generated ASCII character
Const EVENT_MouseDown	= $201		; Mouse button pressed
Const EVENT_MouseUp		= $202		; Mouse button released
Const EVENT_MouseMove	= $203		; Mouse moved
Const EVENT_Gadget		= $401		; Gadget clicked
Const EVENT_Move		= $801		; Window moved
Const EVENT_Size		= $802		; Window resized
Const EVENT_Close		= $803		; Window closed
Const EVENT_Menu		= $1001		; Menu item selected

AppTitle "Lookout Express!"

; -----------------------------------------------------------------------------
; . User settings
; -----------------------------------------------------------------------------

	; ---------------------------------------------------------------------
	; SMTP server (eg. "smtp.your_isp.net")
	; ---------------------------------------------------------------------
		Global mailhost$

	; ---------------------------------------------------------------------
	; Email client name (this program)
	; ---------------------------------------------------------------------
		Global mailer$ = "Lookout Express"
		
	; ---------------------------------------------------------------------
	; Sender's email address
	; ---------------------------------------------------------------------
		Global mailfrom$

	; ---------------------------------------------------------------------
	; Sender's real name
	; ---------------------------------------------------------------------
		Global mailname$
		
settings = ReadFile ("bmp.dat")
If settings
	mailhost$ = ReadLine (settings)
	mailfrom$ = ReadLine (settings)
	mailname$ = ReadLine (settings)
	CloseFile settings
EndIf

window = CenterWindow ("Lookout Express", 640, 480)

SetMinWindowSize window

If window

	menu = CreateMenu ("&File", 0, WindowMenu (window))
	CreateMenu "&About...", 1, menu
	CreateMenu "E&xit", 2, menu
	UpdateWindowMenu window
	
	edit = CreateTextArea (0, 0, ClientWidth (window), ClientHeight (window) - 101, window, 1)
	SetGadgetLayout edit, 1, 1, 1, 1

	font=LoadFont( "courier",24, 1)
	SetTextAreaTabs edit,4
	SetTextAreaFont edit,font
	SetTextAreaColor edit,0,0,0
	SetTextAreaColor edit,249,249,200,True
	
	send = CreateButton ("Send email", 0, ClientHeight (window) - 100, 180, 100, window)
	
	user = CreateTextField (ClientWidth (window) - ClientWidth (window) / 2 - 1, ClientHeight (window) - 100, ClientWidth (window) / 2, 25, window)
	toa	 = CreateTextField (ClientWidth (window) - ClientWidth (window) / 2 - 1, ClientHeight (window) - 75, ClientWidth (window) / 2, 25, window)
	from = CreateTextField (ClientWidth (window) - ClientWidth (window) / 2 - 1, ClientHeight (window) - 50, ClientWidth (window) / 2, 25, window)
	config = CreateTextField (ClientWidth (window) - ClientWidth (window) / 2 - 1, ClientHeight (window) - 25, ClientWidth (window) / 2, 25, window)

	SetGadgetLayout send, 1, 0, 0, 1
	SetGadgetLayout user, 0, 1, 0, 1
	SetGadgetLayout toa, 0, 1, 0, 1
	SetGadgetLayout from, 0, 1, 0, 1
	SetGadgetLayout config, 0, 1, 0, 1
	
	conf$ = "SMTP server:"
;	width = StringWidth (conf$) ; Not working at time of writing!
	width = 70

	recip$ = "To address:"
	sender$ = "From address:"
	uname$ = "Sender name:"
	
	ulabel = CreateLabel (uname$, (GadgetX (user)) - width, ClientHeight (window) - 95, width, 25, window)
	rlabel = CreateLabel (recip$, (GadgetX (toa)) - width, ClientHeight (window) - 70, width, 25, window)
	flabel = CreateLabel (sender$, (GadgetX (from)) - width, ClientHeight (window) - 45, width, 25, window)
	clabel = CreateLabel (conf$, (GadgetX (config)) - width, ClientHeight (window) - 20, width, 25, window)
	
	SetGadgetText user, mailname$
	SetGadgetText from, mailfrom$
	SetGadgetText config, mailhost$
	
	SetGadgetLayout ulabel, 0, 1, 0, 1
	SetGadgetLayout rlabel, 0, 1, 0, 1
	SetGadgetLayout flabel, 0, 1, 0, 1
	SetGadgetLayout clabel, 0, 1, 0, 1
	
	ActivateGadget edit
	
	Repeat
		e = WaitEvent ()
		Select e
			Case EVENT_Close
				Goto quit
			Case EVENT_Gadget
				If EventSource () = send
					DisableGadget send
					mailhost$ = TextFieldText (config)
					mailto$ = TextFieldText (toa)
					mailfrom$ = TextFieldText (from)
					mailname$ = TextFieldText (user)
					message$ = TextAreaText (edit)
					If (mailhost$ = "") Or (mailto$ = "") Or (mailfrom$ = "") Or (mailname$ = "") Or (message$ = "")
						result$ = "Required field(s) empty!"
					Else
						SetStatusText window, "Sending..."
						subject$ = "This is from Lookout Express !"
						result$ = BlitzMail (mailto$, subject$, message$)
						If result$ = "BlitzMailed!" Then SetGadgetText edit, ""
						SetStatusText window, ""
					EndIf
					EnableGadget send
					Notify result$
				EndIf
			Case EVENT_Menu
				Select EventData ()
					Case 1
						Notify "Lookout Express !" +Chr (10) + Chr (10) + "A simple mail program by james @ hi - toro . com"
					Case 2
						Goto quit
				End Select
		End Select
	Forever

Else
	Notify "Failed to create Lookout Express window!", True
EndIf

.quit

settings = WriteFile ("bmp.dat")

If settings
	WriteLine settings, mailhost$
	WriteLine settings, mailfrom$
	WriteLine settings, mailname$
	CloseFile settings
EndIf

End

; -----------------------------------------------------------------------------
; Open a centered window...
; -----------------------------------------------------------------------------

Function CenterWindow (title$, width, height, group = 0, style = 0)
	Return CreateWindow (title$, (ClientWidth (Desktop ()) / 2) - (width / 2), (ClientHeight (Desktop ()) / 2) - (height / 2), width, height)
End Function

; -----------------------------------------------------------------------------
; . SMTP functions
; -----------------------------------------------------------------------------

Function BlitzMail$ (mailto$, subject$, message$)
	
	message$ = Replace$ (message$, "|", Chr (13) + Chr (10))
	error$ = "BlitzMailed!"
		
	t = OpenTCPStream (mailhost$, 25)

	If t

		; ---------------------------------------------------------------------
		; Service available?
		; ---------------------------------------------------------------------
		response$ = ReadLine (t)
		code$ = Code (response$)
		If code$ <> "220"
			If code$ = "421"
				error$ = "Service not available"
			Else
				error$ = response$
			EndIf		
			Goto abortSMTP
		EndIf
		
		; ---------------------------------------------------------------------
		; Say "Hi"
		; ---------------------------------------------------------------------
		WriteLine t, "HELO BlitzMail Deluxe"
		response$ = ReadLine (t)
		If Code (response$) <> "250"
			error$ = response$
			Goto abortSMTP
		EndIf

		; ---------------------------------------------------------------------
		; Tell server who's sending
		; ---------------------------------------------------------------------
		WriteLine t, "MAIL FROM: <" + mailfrom$ + ">"
		response$ = ReadLine (t)
		code$ = Code (response$)
		If code$ <> "250"
			If code$ = "501"
				error$ = "Email sender not specified (or invalid address)"
			Else
				error$ = response$
			EndIf
			Goto abortSMTP
		EndIf

		; ---------------------------------------------------------------------
		; Tell server who it's going to		
		; ---------------------------------------------------------------------
		WriteLine t, "RCPT TO: <" + mailto$ + ">"
		response$ = ReadLine (t)
		code$ = Code (response$)
		If code$ <> "250"
			If code$ = "501"
				error$ = "Email recipient not specified (or invalid address)"
			Else
				error$ = response$
			EndIf
			Goto abortSMTP
		EndIf

		; ---------------------------------------------------------------------
		; Email data
		; ---------------------------------------------------------------------
		WriteLine t, "DATA"
		response$ = ReadLine (t)
		If Code (response$) <> "354"
			error$ = response$
			Goto abortSMTP
		EndIf

		; ---------------------------------------------------------------------
		; Headers
		; ---------------------------------------------------------------------
		WriteLine t, "Date: "		+ CurrentDate$ ()
		WriteLine t, "From: "		+ mailname$ + " <" + mailfrom$ + ">"
		WriteLine t, "To: "			+ mailto$ + " <" + mailto$ + ">"
		WriteLine t, "Subject: "	+ subject$
		WriteLine t, "X-Mailer: "	+ mailer$

		; ---------------------------------------------------------------------
		; Email message
		; ---------------------------------------------------------------------
		WriteLine t, message$

		; ---------------------------------------------------------------------
		; End of message
		; ---------------------------------------------------------------------
		WriteLine t, ""
		WriteLine t, "."
		response$ = ReadLine (t)
		If Code (response$) <> "250"
			error$ = response$
		EndIf

		; ---------------------------------------------------------------------
		; Say "ciao"
		; ---------------------------------------------------------------------
		WriteLine t, "QUIT"
		response$ = ReadLine (t)
		If Code (response$) <> "221"
			error$ = response$
		EndIf

		; ---------------------------------------------------------------------
		; Return error message, if any
		; ---------------------------------------------------------------------
		.abortSMTP
		CloseTCPStream t
		If error$ = "" Then error$ = "Timeout error"
		Return error$
		
	Else

		; ---------------------------------------------------------------------
		; Oops. Forgot to go online (or server isn't there)	
		; ---------------------------------------------------------------------
		Return "Failed to connect to server at " + mailhost$
		
	EndIf
		
End Function

; -----------------------------------------------------------------------------
; Ask server for help (usually list of commands)... not much use, just example
; -----------------------------------------------------------------------------

Function GetHelp (server$)
	t = OpenTCPStream (mailhost$, 25)
	If t
		ReadLine (t) ; 220
		WriteLine t, "HELO BlitzMail Deluxe"
		ReadLine (t) ; 250
		WriteLine t, "HELP"
		response$ = ReadLine (t)
		error$ = Left (response$, 3)
		If error$ = "214"
			help$ = response$
			Repeat
				readhelp$ = ReadLine (t)
				help$ = help$ + Chr (10) + readhelp$
			Until readhelp$ = ""
			RuntimeError help$
		Else
			RuntimeError "Couldn't get help information!"
		EndIf
		CloseTCPStream (t)
	EndIf
End Function

; -----------------------------------------------------------------------------
; Return 3 digit code from server's response
; -----------------------------------------------------------------------------

Function Code$ (code$)
	Return Left (code$, 3)
End Function