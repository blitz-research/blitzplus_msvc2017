;BLITZBROWSE by Beaker 2003



;SETUP WINDOW
Global window=CreateWindow( "Blitz Browse",0,0,800,600)
SetMinWindowSize window,200,0

;SETUP MENUS
filemenu = CreateMenu ("  &File  ",0,WindowMenu(window))
	openmenuitem = CreateMenu ("&Open...",1,filemenu)

CreateMenu ("",999,filemenu)
	exitmenuitem = CreateMenu ("E&xit",9,filemenu)

bookmenu = CreateMenu ("  &Bookmarks  ",0,WindowMenu(window))
	addbookmenuitem = CreateMenu ("&Add bookmark",21,bookmenu)
	organisebookmenuitem = CreateMenu ("&Organise bookmarks",22,bookmenu)
	DisableMenu organisebookmenuitem
	defaultmenuitem = CreateMenu ("Set as &default homepage",23,bookmenu)
	CreateMenu ("",999,bookmenu)



If FileType("bookmark.txt") <> 1	;then create a bookmark file with default bookmarks
	bookfile = WriteFile("bookmark.txt")
	Restore bookdata
	Read bookname$
	Read bookURL$
	While bookname$ <> "END"
		WriteLine bookfile,bookname$
		WriteLine bookfile,bookURL$
		Read bookname$
		Read bookURL
	Wend
	CloseFile bookfile
EndIf

;read the bookmarks into the bookmark menu
Global bookf=500
bookfile = ReadFile("bookmark.txt")
While Not Eof(bookfile)
	bookname$ = ReadLine(bookfile)
	bookURL$ = ReadLine(bookfile)
	If bookURL<>""
		CreateMenu (bookname,bookf,bookmenu)
		bookf = bookf +1
	EndIf
Wend
CloseFile bookfile
CreateMenu ("",999,bookmenu)

helpmenu = CreateMenu("  &Help  ",0,WindowMenu(window))
aboutmenu = CreateMenu("&About",1000,helpmenu)

UpdateWindowMenu window



If FileType("default.txt") <> 1	;then create the default.txt file with the default homepage
	deffile = WriteFile("default.txt")
		WriteLine deffile,"http://www.blitzbasic.co.nz"
	CloseFile deffile
EndIf

deffile = ReadFile("default.txt")
	Global defURL$ = ReadLine (deffile)
CloseFile deffile




;SETUP BUTTONS
panel = CreatePanel (0,0,800,40,window,0)
	SetGadgetLayout panel,1,0,1,0
	backbutt = CreateButton ("Back",5,5,60,30,panel)
	forebutt = CreateButton ("Forward",70,5,60,30,panel)
	refreshbutt = CreateButton ("Refresh",135,5,60,30,panel)
	homebutt = CreateButton ("Home",200,5,60,30,panel)
	Global URLfield = CreateTextField (265,10,350,20,panel)
	URLgo = CreateButton ("GO!",620,10,60,20,panel)


;SETUP HTML VIEW
Global html=CreateHtmlView( 0,45,ClientWidth(window),ClientHeight(window),window )
SetGadgetLayout html,1,1,1,1


Global current$
GoURL(defURL)


;MAIN LOOP
While WaitEvent()
	Select EventID()
		Case $1001	;MENU EVENTS
			Select EventData()
				Case 1	;Open Local File
					localURL$ = RequestFile("Open local file","htm,html,jpg,gif,png")
					If FileType (localURL) = 1
						GoURL(localURL)
					EndIf
				Case 9	;Close program
					End
				Case 21	;Add bookmark
					bookfile = OpenFile("bookmark.txt")
					found = False
					While Not Eof(bookfile)
						bookname = ReadLine(bookfile)
						bookURL = ReadLine(bookfile)
						If bookURL = current
							Notify "URL already in bookmarks"
							found = True
							Exit
						EndIf
					Wend
					If found = False
						WriteLine bookfile,current
						WriteLine bookfile,current
						CreateMenu (current,bookf,bookmenu)
						bookf = bookf +1
						UpdateWindowMenu window
					EndIf
					CloseFile bookfile
				Case 23	;Set as default homepage
					deffile = WriteFile("default.txt")
						WriteLine deffile,current$
					CloseFile deffile
				Case 1000	;About
					Notify "Blitz Browse by Beaker 2003"+Chr(13)+"(bookmarks are in the bookmark.txt file)"
			End Select
			If EventData() >= 500	;Jump to a specific bookmark
				f = 0
				bookfile = ReadFile("bookmark.txt")
				While Not Eof(bookfile)
					bookname$ = ReadLine (bookfile)
					bookURL = ReadLine (bookfile)
					If f = EventData()-500 Then
						GoURL(bookURL)
						Exit
					EndIf
					f = f +1
				Wend
			EndIf	
		Case $401	;BUTTON EVENTS
			Select EventSource()
				Case backbutt
						HtmlViewBack html
				Case forebutt
						HtmlViewForward html
				Case refreshbutt
						GoURL(current)
				Case homebutt
					deffile = ReadFile("default.txt")
						GoURL(ReadLine (deffile))
					CloseFile deffile
				Case URLgo
					GoURL(TextFieldText(URLfield))
					
				Case URLfield
					If EventData() = 13
						GoURL(TextFieldText(URLfield))
					EndIf
			End Select
					
		Case $803	;WINDOW CLOSED EVENT
			Select EventSource()
				Case window
					End
			End Select
	End Select
	

Wend
End



Function GoURL(URL$)
	current = URL$
	SetGadgetText URLfield,current
	HtmlViewGo html,current
	SetGadgetText window,"Blitz Browse - "+current
End Function




.bookdata
Data "Blitz Basic"
	Data "http://www.blitzbasic.co.nz"
Data "Blitz Coder"
	Data "http://www.blitzcoder.com"
Data "FONText bitmap font creation"
	Data "http://www.marina.cybermog.co.uk/fontext/"
Data "Redflame Games and Tools"
	Data "http://www.redflame.net"
Data "Blitz Base"
	Data "http://www.blitzbase.de/"
Data "Blitz3D.co.uk"
	Data "http://www.blitz3d.co.uk"
Data "END"
	Data "END"



