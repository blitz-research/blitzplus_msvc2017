;
;	RPG Item Editor
;	Coded by Ed Upton
;

window = CreateWindow( "RPG Item Editor", (ClientWidth( Desktop() )/2)-225, (ClientHeight( Desktop() ) /2 )-280, 450, 570 )

AppTitle "RPG Item Editor"

Global ModelFileName$ = ""
Global TextureFileName$ = ""
Global OpenedFileName$ = ""
Global MainMenu = WindowMenu( window )
Global FileMenu = CreateMenu( "&File", 0, MainMenu )
Global FileMenuNew = CreateMenu( "&New Item", 1, FileMenu )
Global FileMenuLoad = CreateMenu( "&Load Item", 2, FileMenu )
Global FileMenuSave = CreateMenu( "&Save Item", 3, FileMenu )
Global FileMenuSaveAs = CreateMenu( "Save &As Item", 4, FileMenu )
Global FileMenuS = CreateMenu( "", 277, FileMenu )
Global FileMenuExit = CreateMenu( "E&xit", 5, FileMenu )

Global ItemMenu = CreateMenu( "&Item", 6, MainMenu )
Global ItemMenuModel = CreateMenu( "Select &model", 7, ItemMenu )
Global ItemMenuTexture = CreateMenu( "Select &texture", 8, ItemMenu )

Global HelpMenu = CreateMenu( "&Help", 9, MainMenu )
Global HelpMenuAbout = CreateMenu( "&About", 10, HelpMenu )

Global FileHandle = 0
Global ReadText$ = ""

UpdateWindowMenu window

Global ItemNameText = CreateLabel( "Item name", 5, 3, 60, 20, window )
Global itemname=CreateTextField( 70, 0, 360, 20, window )
SetGadgetLayout Itemname, 1, 1, 0, 0

Dim Property( 14 )
Dim PropertyText( 14 )
Dim PropertyEditBox( 14 )

y=25
Global ItemClassText = CreateLabel( "Class", 5, y+3, 60, 20, window )
Global ItemClassCombo = CreateComboBox( 70, y, 360, 20, window )
AddGadgetItem ItemClassCombo, "None"

FileHandle = ReadFile( "ItemClass.txt" )
If FileHandle<>0
	Repeat
		ReadText$ = ReadLine$( FileHandle )
		AddGadgetItem ItemClassCombo, ReadText$
	Until Eof( FileHandle )=1
	CloseFile FileHandle
	FileHandle =0
EndIf
SelectGadgetItem ItemClassCombo, 0

y=y+25
For a=0 To 14
	PropertyText( a )=CreateLabel( Str$( a+1 ), 5, y+3, 60, 20, window )
	Property( a ) = CreateComboBox( 70, y, 250, 20, window )
	PropertyEditBox( a )=CreateTextField( 330, y, 100, 20, window )
	y=y+25
Next

Global CursedText = CreateLabel( "Cursed?", 5, y+3, 60, 20, window )
Global CursedCombo = CreateComboBox( 70, y, 100, 20, window )
AddGadgetItem CursedCombo, "No"
AddGadgetItem CursedCombo, "Yes"
SelectGadgetItem CursedCombo, 0

Global WeightText = CreateLabel( "Weight (in stones)", 200, y+3, 90, 20, window )
Global WeightEdit = CreateTextField( 300, y, 130, 20, window )
y=y+25

Global ClassText = CreateLabel( "Skill", 5, y+3, 60, 20, window )
Global ClassCombo = CreateComboBox( 70, y, 360, 20, window )
AddGadgetItem ClassCombo, "None"

FileHandle = ReadFile( "Class.txt" )
If FileHandle<>0
	Repeat
		ReadText$ = ReadLine$( FileHandle )
		AddGadgetItem ClassCombo, ReadText$
	Until Eof( FileHandle )=1
	CloseFile FileHandle
	FileHandle =0
EndIf
SelectGadgetItem ClassCombo, 0
y=y+25

Global RaceText = CreateLabel( "Race", 5, y+3, 60, 20, window )
Global RaceCombo = CreateComboBox( 70, y, 360, 20, window )
AddGadgetItem RaceCombo, "None"
FileHandle = ReadFile( "Race.txt" )
If FileHandle<>0
	Repeat
		ReadText$ = ReadLine$( FileHandle )
		AddGadgetItem RaceCombo, ReadText$
	Until Eof( FileHandle )=1
	CloseFile FileHandle
	FileHandle =0
EndIf
SelectGadgetItem RaceCombo, 0
y=y+25

For b=0 To 14
	AddGadgetItem Property( b ), "None"

	FileHandle = ReadFile( "Properties.txt" )
	If FileHandle<>0
		Repeat
			ReadText$ = ReadLine$( FileHandle )
			AddGadgetItem Property( b ), ReadText$
		Until Eof( FileHandle )=1
		CloseFile FileHandle
		FileHandle =0
	EndIf

	SelectGadgetItem Property( b ), 0
Next

While kkk=0
	If KeyDown(1) Then End

	e= WaitEvent()
	Select e
	Case $803 : If (Confirm( "Quit: Are you sure?", True )=1) Then End
	Case $1001: Select EventData()
				Case 1
					;new item
					Ok = Confirm( "Are you sure?", True )
					If Ok=1
						For a=0 To 14
							SetGadgetText PropertyEditBox( a ), ""
							SelectGadgetItem Property( a ), 0
						Next
						SetGadgetText itemname, ""
						SelectGadgetItem CursedCombo, 0
						SelectGadgetItem ItemClassCombo, 0
						SelectGadgetItem ClassCombo, 0
						SelectGadgetItem RaceCombo, 0
						SetGadgetText WeightEdit, ""
						ModelFileName$ = ""
						TextureFileName$ = ""
						OpenFileName$ = ""
					EndIf
					
			  	Case 2
					OpenFileName$ = RequestFile$( "Load item", ".eif" )
					If OpenFileName$<>"" Then LoadItem( OpenFileName$ )

				Case 3
					;Save item system
					If OpenFileName$<>""
						SaveItem( OpenFileName$ )
					Else
						OpenFileName$ = RequestFile$( "Save As", ".eif", True )
						If OpenFileName$<>"" Then SaveItem( OpenFileName$ )
					EndIf

				Case 4
					OpenFileName$ = RequestFile$( "Save As", ".eif", True )
					If OpenFileName$<>"" Then SaveItem( OpenFileName$ )

				Case 5
					If (Confirm( "Quit: Are you sure?", True )=1) Then End

			  	Case 7
					ModelFileName$ = RequestFile$( "Load item", ".x,.3ds,.b3d" )

			  	Case 8
					TextureFileName$ = RequestFile$( "Load texture", ".bmp,.tga,.jpg,.jpeg,.png,.gif" )

				Case 10
					Notify "RPG Item editor"+Chr$(10)+"Coded by Ed Upton"

				End Select
	End Select
Wend
End

Function SaveItem( FileName$ )
	Local Handl=0

	Handl = WriteFile( FileName$ )
	If Handl<>0 
		WriteLine Handl, TextFieldText$( itemname )						;ItemName
		For a=0 To 14
			WriteInt Handl, SelectedGadgetItem( Property( a ) )
			WriteLine Handl, TextFieldText$( PropertyEditBox( a ) )	;PropertyEditBox
		Next
		WriteInt Handl, SelectedGadgetItem( ItemClassCombo )
		WriteInt Handl, SelectedGadgetItem( ClassCombo )
		WriteInt Handl, SelectedGadgetItem( RaceCombo )
		WriteInt Handl, TextFieldText$( WeightEdit )
		WriteInt Handl, SelectedGadgetItem( CursedCombo )
		WriteLine Handl, ModelFileName$
		WriteLine Handl, TextureFileName$
		CloseFile Handl
	Else
		Notify "Could not save file", True
	EndIf
End Function

Function LoadItem( FileName$ )
	Local Handl = 0
	
	Handl = ReadFile( FileName$ )
	If Handl<>0
		SetGadgetText ItemName, Str$( ReadLine$( Handl ) )
		For a=0 To 14
			SelectGadgetItem Property( a ), ReadInt( Handl )
			SetGadgetText PropertyEditBox( a ), ReadLine$( Handl )
		Next
		SelectGadgetItem ItemClassCombo, ReadInt( Handl )
		SelectGadgetItem ClassCombo, ReadInt( Handl )
		SelectGadgetItem RaceCombo, ReadInt( Handl )
		SetGadgetText WeightEdit, ReadInt( Handl )
		SelectGadgetItem CursedCombo, ReadInt( Handl )
		ModelFileName$ = ReadLine$( Handl )
		TextureFileName$ = ReadLine$( Handl )
		CloseFile Handl
	Else
		Notify "Could not load file", True
	EndIf
End Function