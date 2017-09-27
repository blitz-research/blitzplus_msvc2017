;--------------------------------------------------------------------------
; Blitz Grep
; (mass search and replace utility For source code And Text documents).
;
; By Rob Cummings (http://www.redflame.net)
;
; You may not resell this application without consent of the author
; and source is provided for educational purposes.
; This is only a rough example! DO NOT RELY on this without
; checking the code first...
;
;        ** I am Not responsible For loss of Data! **
;
; Grep? The program takes its name from the linux/unix text search utility.
; to do: upper/lowecase + file backups/security. recursive folders...etc.
;
;
; ** works on any file although files with ascii are reccomended. **
; You could use it to make mass changes in your sources.
;--------------------------------------------------------------------------

AutoSuspend False

Global window,windowpanel,dir,ext,matchtxt,replacetxt,casesens,logfile,searchbutton,replacebutton,tree,root,results
Global logfilehandle,logfilename$=CurrentDir()+"/log.txt",readfilehandle,writefilehandle,tmpname$=CurrentDir()+"/$.tmp"

Type file
	Field filename$,treenode,parent
End Type


; get/set desktop and window dimensions
dw=GadgetWidth(Desktop())
dh=GadgetHeight(Desktop())
w=640 : h=480
x=dw/2 - w/2 : y=dh/2 - h/2


;----------------------------------------------------------------
; create our application
;----------------------------------------------------------------

window = CreateWindow("Blitz Grep multiple document search and replace",x,y,w,h,0,1+2+4+8)
filemenu = CreateMenu ("?", 0, WindowMenu (window))
	CreateMenu ("About",   1, filemenu)
	CreateMenu ("Exit",    2, filemenu)
UpdateWindowMenu window

dirlabel = CreateLabel("  Directory",0,2,55,16,window)	: SetGadgetLayout dirlabel,1,0,1,0
dir = CreateTextField(60,0,w-108,22,window) 			: SetGadgetLayout dir,1,1,1,0
dirbutton = CreateButton("...",w-42,0,32,22,window) 	: SetGadgetLayout dirbutton,0,1,1,0
SetGadgetText dir,CurrentDir$()

extlabel = CreateLabel(" Extension",0,26,55,16,window)	: SetGadgetLayout extlabel,1,0,1,0
ext = CreateTextField(60,24,w-108,22,window)			: SetGadgetLayout ext,1,1,1,0
SetGadgetText ext,"txt"

matchtxtlabel = CreateLabel("   Search ",0,50,55,16,window) : SetGadgetLayout matchtxtlabel,1,0,1,0
matchtxt =  CreateTextField(60,48,w-108,22,window)			 : SetGadgetLayout matchtxt,1,1,1,0
SetGadgetText matchtxt,"search string"

replacetxtlabel = CreateLabel("  Replace ",0,74,55,16,window)	: SetGadgetLayout replacetxtlabel,1,0,1,0
replacetxt =  CreateTextField(60,72,w-108,22,window)			: SetGadgetLayout replacetxt,1,1,1,0
SetGadgetText replacetxt,"replace string"

;*to do*
casesens = CreateButton("Case sensitive",60,108,90,16,window,2): SetGadgetLayout casesens,1,0,1,0
SetButtonState casesens,1
DisableGadget casesens

logfile = CreateButton("Log results",160,108,90,16,window,2): SetGadgetLayout logfile,1,0,1,0

searchbutton = CreateButton("SEARCH NOW",270,105,100,22,window) : SetGadgetLayout searchbutton,1,0,1,0
replacebutton = CreateButton("REPLACE NOW",400,105,100,22,window) : SetGadgetLayout replacebutton,1,0,1,0

tree = CreateTreeView(0,140,w-8,270,window) : SetGadgetLayout tree,1,1,1,1
root=TreeViewRoot( tree )
results=AddTreeViewNode( "Search Results",root )


;----------------------------------------------------------------
; main loop
;----------------------------------------------------------------

While Not KeyHit(1)
	WaitEvent()
	
	Select EventID()
		Case $401 ; gadget event
			Select EventSource()
				Case dirbutton : ChooseDir()
				Case searchbutton : SearchTxt()
				Case replacebutton : ReplaceTxt()
			End Select
	
		Case $803 ; window close event
			Menu_Exit()
		
		Case $1001 ; a menu event
			Select EventData()
				Case 1 : Menu_About()
				Case 2 : Menu_Exit()
			End Select
			
	End Select
Wend
End

;----------------------------------------------------------------
; functions
;----------------------------------------------------------------

Function ChooseDir()
	ChangeDir RequestDir$("Choose search dir")
	SetGadgetText dir,CurrentDir$()
End Function

;----------------------------------------------------------------

Function SearchTxt()

	;if file logging is turned on, delete old file and log new
	If ButtonState(logfile)=True
		DeleteFile(logfilename$)
		logfilehandle=WriteFile(logfilename$)
		WriteLine logfilehandle,"Search Results"
		logging=1
	Else
		logging=0
	EndIf
	
	; get extension
	e$=TextFieldText(ext)
	e$=Trim(Replace(e$,".",""))
	e$=Trim(Replace(e$,"*",""))
	If Len(e$)=0
		AppTitle "Warning"
		Notify "File extension not specified."+Chr$(10)+"Please choose an extension.",1
		Return
	EndIf
		
	; get search text
	findtxt$=TextFieldText(matchtxt)
	
	; check folder
	folder$=TextFieldText(dir)
	mydir=ReadDir(folder$)
	If mydir=0
		AppTitle "Warning"
		Notify "Directory does not exist."+Chr$(10)+"Please choose another Folder.",1
		Return
	EndIf
	
	;clear old database
	For f.file=Each file
		;check if parent exists else delete
		If f\parent=results
			FreeTreeViewNode f\treenode
		EndIf
		Delete f
	Next
	
	;create new database
	numfiles = 0 : total=0
	Repeat 
		file$=NextFile$(mydir)
		If file$="" Then Exit 
		If FileType(folder$+"\"+file$)=1
			pos=Instr(file$,"."+e$)
			If pos
				; match wildcard with length of extension. 
				a$=Mid(file$,pos)
				a$=Trim(Replace(a$,".",""))
				If Len(a$)=Len(e$)
					
					; populate files
					f.file=New file
					f\filename$=file$
					f\treenode=AddTreeViewNode( f\filename$,results)
					f\parent=results
					parentnode=f\treenode
					If logging WriteLine logfilehandle,Chr$(9)+f\filename$
					
					
					; now search within found file for occurances
					
					currentline=0
					readfilehandle=ReadFile(f\filename$)
					Repeat
						numstrings=0
						a$=ReadLine(readfilehandle)
						pos = Instr(a$,findtxt$)
						; if there is a matching string then continue along the line.
						While pos
							numstrings=numstrings+1
							a$=Mid(a$,pos+1)
							pos = Instr(a$,findtxt$)
						Wend
						
						; populate database with results if found
						If numstrings>0
							f.file=New file
							If numstrings=1 f\filename=Chr(34)+findtxt$+Chr(34)+" found once on line "+currentline+"."
							If numstrings>1 f\filename=Chr(34)+findtxt$+Chr(34)+" found "+numstrings+" times on line "+currentline+"."
							f\treenode=AddTreeViewNode( f\filename$,parentnode)
							f\parent=parentnode
							
							If logging
								If numstrings=1 WriteLine logfilehandle,Chr(9)+Chr(9)+Chr(34)+findtxt$+Chr(34)+" found once on line "+currentline+"."
								If numstrings>1 WriteLine logfilehandle,Chr(9)+Chr(9)+Chr(34)+findtxt$+Chr(34)+" found "+numstrings+" times on line "+currentline+"."
							EndIf
								
						EndIf
						total=total+numstrings				
						currentline=currentline+1		
					Until Eof(readfilehandle)
					CloseFile  readfilehandle
					numfilematches=numfilematches+1
				EndIf
			EndIf
		EndIf
	Forever 

	; if no results...
	If numfilematches=0
		f.file=New file
		f\treenode=AddTreeViewNode( "No matching files were found.",results)
		f\parent=results
		If logging WriteLine logfilehandle,Chr$(9)+"No matching files were found."
	Else
		f.file=New file
		f\treenode=AddTreeViewNode( numfilematches+" file(s) with "+total+" matches were found.",results)
		f\parent=results
		If logging WriteLine logfilehandle,Chr$(9)+numfilematches+" file(s) with "+total+" matches were found."
	EndIf

	; clean up	
	CloseDir mydir
	If logging CloseFile logfilehandle
	ExpandTreeViewNode results
End Function


;---------------------------------------------------------------

Function ReplaceTxt()
	AppTitle "Warning"
	ans=Confirm("This could overwrite your files."+Chr(10)+"Do you want to continue?",1)
	If ans=0 Return

	;if file logging is turned on, delete old file and log new
	If ButtonState(logfile)=True
		DeleteFile(logfilename$)
		logfilehandle=WriteFile(logfilename$)
		WriteLine logfilehandle,"Search Results"
		logging=1
	Else
		logging=0
	EndIf
	
	; get extension
	e$=TextFieldText(ext)
	e$=Trim(Replace(e$,".",""))
	e$=Trim(Replace(e$,"*",""))
	If Len(e$)=0
		AppTitle "Warning"
		Notify "File extension not specified."+Chr$(10)+"Please choose an extension.",1
		Return
	EndIf
		
	; get search text
	findtxt$=TextFieldText(matchtxt)
	
	;get replace text
	newtxt$=TextFieldText(replacetxt)
	
	; check folder
	folder$=TextFieldText(dir)
	mydir=ReadDir(folder$)
	If mydir=0
		AppTitle "Warning"
		Notify "Directory does not exist."+Chr$(10)+"Please choose another Folder.",1
		Return
	EndIf
	
	;clear old database
	For f.file=Each file
		;check if parent exists else delete
		If f\parent=results
			FreeTreeViewNode f\treenode
		EndIf
		Delete f
	Next
	
	;create new database
	numfiles = 0 : total = 0
	Repeat 
		file$=NextFile$(mydir)
		If file$="" Then Exit 
		If FileType(folder$+"\"+file$)=1
			pos=Instr(file$,"."+e$)
			If pos
				; match wildcard with length of extension. 
				a$=Mid(file$,pos)
				a$=Trim(Replace(a$,".",""))
				If Len(a$)=Len(e$)
					
					; populate files
					f.file=New file
					f\filename$=file$
					f\treenode=AddTreeViewNode( f\filename$,results)
					f\parent=results
					parentnode=f\treenode
					If logging WriteLine logfilehandle,Chr$(9)+f\filename$
					
					
					; now search within found file for occurances
					currentline=0 : overwrite=0
					readfilehandle=ReadFile(f\filename$)
					writefilehandle=WriteFile(tmpname$)
					
					Repeat
						numstrings=0
						a$=ReadLine(readfilehandle)
						b$=a$
						
						;replace
						c$=Replace(b$,findtxt$,newtxt$)
						WriteLine writefilehandle,c$
						
						pos = Instr(a$,findtxt$)
						; if there is a matching string then continue along the line.
						While pos
							numstrings=numstrings+1 : overwrite=1
							a$=Mid(a$,pos+1)
							pos = Instr(a$,findtxt$)
						Wend
						
						; populate database with results if found
						If numstrings>0
							f.file=New file
							If numstrings=1 f\filename=Chr(34)+findtxt$+Chr(34)+" replaced once on line "+currentline+"."
							If numstrings>1 f\filename=Chr(34)+findtxt$+Chr(34)+" replaced "+numstrings+" times on line "+currentline+"."
							f\treenode=AddTreeViewNode( f\filename$,parentnode)
							f\parent=parentnode
							
							If logging
								If numstrings=1 WriteLine logfilehandle,Chr(9)+Chr(9)+Chr(34)+findtxt$+Chr(34)+" replaced once on line "+currentline+"."
								If numstrings>1 WriteLine logfilehandle,Chr(9)+Chr(9)+Chr(34)+findtxt$+Chr(34)+" replaced "+numstrings+" times on line "+currentline+"."
							EndIf
								
						EndIf
						total=total+numstrings				
						currentline=currentline+1	
					Until Eof(readfilehandle)
					
					; the file stuff
					CloseFile  readfilehandle
					CloseFile  writefilehandle
					
					; overwrite
					If overwrite
						DeleteFile CurrentDir()+"\"+file$
						CopyFile tmpname$,CurrentDir()+"\"+file$
						DeleteFile tmpname$
					EndIf
					
					numfilematches=numfilematches+1
				EndIf
			EndIf
		EndIf
	Forever 

	; if no results...
	If numfilematches=0
		f.file=New file
		f\treenode=AddTreeViewNode( "No matching files were found.",results)
		f\parent=results
		If logging WriteLine logfilehandle,Chr$(9)+"No matching files were found."
	Else
		f.file=New file
		f\treenode=AddTreeViewNode( numfilematches+" file(s) with "+total+" matches were replaced.",results)
		f\parent=results
		If logging WriteLine logfilehandle,Chr$(9)+numfilematches+" file(s) with "+total+" matches were replaced."
	EndIf

	; clean up	
	CloseDir mydir
	If logging CloseFile logfilehandle
	ExpandTreeViewNode results
End Function

;----------------------------------------------------------------

Function Menu_About()
	AppTitle "About"
	Notify " Blitz Grep"+Chr$(10)+"By Rob Cummings"
End Function

;----------------------------------------------------------------

Function Menu_Exit()
	AppTitle "Quit Blitz Grep?"
	ans=Confirm("Are you sure you want to quit?",1)
	If ans=1 Then End
End Function

;----------------------------------------------------------------
; EOF
;----------------------------------------------------------------