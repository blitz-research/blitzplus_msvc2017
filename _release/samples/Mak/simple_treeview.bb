
window=CreateWindow( "Simple TreeView demo",0,0,640,480 )

treeview=CreateTreeView( 0,0,ClientWidth(window),ClientHeight(window),window )
SetGadgetLayout treeview,1,1,1,1

root=TreeViewRoot( treeview )

hello=AddTreeViewNode( "Hello!",root )
	test1=AddTreeViewNode( "Test1",hello )
	test2=AddTreeViewNode( "Test2",hello )
	test3=AddTreeViewNode( "Test3",hello )
	test4=AddTreeViewNode( "Test4",hello )
		test5=AddTreeViewNode( "Test5",test4 )
		test6=AddTreeViewNode( "Test6",test4 )
		test7=AddTreeViewNode( "Test7",test4 )
		
While WaitEvent()<>$803

	If EventID()=$401 And EventSource()=treeview
		Select SelectedTreeViewNode( treeview )
		Case hello Notify "Hello hit"
		Case test1 Notify "Test1 hit"
		Case test2 Notify "Test2 hit"
		Case test3 Notify "Test3 hit"
		Case test4 Notify "Test4 hit"
		Case test5 Notify "Test5 hit"
		Case test6 Notify "Test6 hit"
		Case test7 Notify "Test7 hit"
		End Select
	EndIf

Wend

End