
#include "../gui/gui.h"

void gui_link( void (*sym)( const char *t_sym,void *pc ) ){

	//gadget
	sym( "FreeGadget%gadget",bbFreeGadget );
	sym( "SetGadgetFont%gadget%font",bbSetGadgetFont );
	sym( "SetGadgetText%gadget$text",bbSetGadgetText );
	sym( "SetGadgetShape%gadget%x%y%width%height",bbSetGadgetShape );
	sym( "SetGadgetLayout%gadget%left%right%top%bottom",bbSetGadgetLayout );
	sym( "ShowGadget%gadget",bbShowGadget );
	sym( "HideGadget%gadget",bbHideGadget );
	sym( "EnableGadget%gadget",bbEnableGadget );
	sym( "DisableGadget%gadget",bbDisableGadget );
	sym( "ActivateGadget%gadget",bbActivateGadget );

	sym( "%GadgetFont%gadget",bbGadgetFont );
	sym( "$GadgetText%gadget",bbGadgetText );
	sym( "%GadgetX%gadget",bbGadgetX );
	sym( "%GadgetY%gadget",bbGadgetY );
	sym( "%GadgetWidth%gadget",bbGadgetWidth );
	sym( "%GadgetHeight%gadget",bbGadgetHeight );
	sym( "%GadgetGroup%gadget",bbGadgetGroup );

	//group
	sym( "%ClientWidth%group",bbClientWidth );
	sym( "%ClientHeight%group",bbClientHeight );

	//menu
	sym( "%CreateMenu$text%menu_id%parent_menu",bbCreateMenu );
	sym( "SetMenuText%menu$text",bbSetMenuText );
	sym( "CheckMenu%menu",bbCheckMenu );
	sym( "UncheckMenu%menu",bbUncheckMenu );
	sym( "EnableMenu%menu",bbEnableMenu );
	sym( "DisableMenu%menu",bbDisableMenu );
	sym( "%MenuChecked%menu",bbMenuChecked );
	sym( "%MenuEnabled%menu",bbMenuEnabled );
	sym( "$MenuText%menu",bbMenuText );

	//iconstrip
	sym( "%LoadIconStrip$file",bbLoadIconStrip );
	sym( "FreeIconStrip%icon_strip",bbFreeIconStrip );
	sym( "SetGadgetIconStrip%gadget%icon_strip",bbSetGadgetIconStrip );

	//label
	sym( "%CreateLabel$text%x%y%width%height%group%style=0",bbCreateLabel );

	//button
	sym( "%CreateButton$text%x%y%width%height%group%style=1",bbCreateButton );
	sym( "SetButtonState%button%state",bbSetButtonState );
	sym( "%ButtonState%button",bbButtonState );

	//textfield
	sym( "%CreateTextField%x%y%width%height%group%style=0",bbCreateTextField );
	sym( "$TextFieldText%textfield",bbTextFieldText );

	//combobox
	sym( "%CreateComboBox%x%y%width%height%group%style=0",bbCreateComboBox );

	//listbox
	sym( "%CreateListBox%x%y%width%height%group%style=0",bbCreateListBox );

	//treeview
	sym( "%CreateTreeView%x%y%width%height%group%style=0",bbCreateTreeView );
	sym( "%TreeViewRoot%treeview",bbTreeViewRoot );
	sym( "%SelectedTreeViewNode%treeview",bbSelectedTreeViewNode );
	sym( "%CountTreeViewNodes%parent_node",bbCountTreeViewNodes );

	sym( "%AddTreeViewNode$text%parent_node%icon=-1",bbAddTreeViewNode );
	sym( "%InsertTreeViewNode%index$text%parent_node%icon=-1",bbInsertTreeViewNode );
	sym( "ModifyTreeViewNode%node$text%icon=-1",bbModifyTreeViewNode );

	sym( "SelectTreeViewNode%node",bbSelectTreeViewNode );
	sym( "ExpandTreeViewNode%node",bbExpandTreeViewNode );
	sym( "CollapseTreeViewNode%node",bbCollapseTreeViewNode );
	sym( "FreeTreeViewNode%node",bbFreeTreeViewNode );
	sym( "$TreeViewNodeText%node",bbTreeViewNodeText );

	//progbar
	sym( "%CreateProgBar%x%y%width%height%group%style=0",bbCreateProgBar );
	sym( "UpdateProgBar%progbar#progress",bbUpdateProgBar );

	//slider
	sym( "%CreateSlider%x%y%width%height%group%style=1",bbCreateSlider );
	sym( "SetSliderRange%slider%visible%total",bbSetSliderRange );
	sym( "SetSliderValue%slider%value",bbSetSliderValue );
	sym( "%SliderValue%slider",bbSliderValue );

	//window
	sym( "%CreateWindow$text%x%y%width%height%group=0%style=15",bbCreateWindow );
	sym( "%ActiveWindow",bbActiveWindow );
	sym( "ActivateWindow%window",bbActivateWindow );
	sym( "UpdateWindowMenu%window",bbUpdateWindowMenu );
	sym( "%SetStatusText%window$text",bbSetStatusText );
	sym( "SetMinWindowSize%window%width=-1%height=-1",bbSetMinWindowSize );
	sym( "MinimizeWindow%window",bbMinimizeWindow );
	sym( "MaximizeWindow%window",bbMaximizeWindow );
	sym( "RestoreWindow%window",bbRestoreWindow );
	sym( "%WindowMinimized%window",bbWindowMinimized );
	sym( "%WindowMaximized%window",bbWindowMaximized );
	sym( "%WindowMenu%window",bbWindowMenu );

	//tabber
	sym( "%CreateTabber%x%y%width%height%group%style=0",bbCreateTabber );

	//panel
	sym( "%CreatePanel%x%y%width%height%group%style=0",bbCreatePanel );
	sym( "SetPanelImage%panel$file",bbSetPanelImage );
	sym( "SetPanelColor%panel%red%green%blue",bbSetPanelColor );

	/*
	//splitter
	sym( "%CreateSplitter%x%y%width%height%group%style=1",bbCreateSplitter );
	sym( "%SplitterPanel%splitter%pane",bbSplitterPanel );
	*/

	//textarea
	sym( "%CreateTextArea%x%y%width%height%group%style=0",bbCreateTextArea );
	sym( "SetTextAreaTabs%textarea%tabs",bbSetTextAreaTabs );
	sym( "SetTextAreaFont%textarea%font",bbSetTextAreaFont );
	sym( "SetTextAreaColor%textarea%red%green%blue%background=0",bbSetTextAreaColor );
	sym( "%TextAreaLen%textarea%units=1",bbTextAreaLen );
	sym( "%TextAreaLineLen%textarea%line",bbTextAreaLineLen );
	sym( "%TextAreaChar%textarea%line",bbTextAreaChar );
	sym( "%TextAreaLine%textarea%char",bbTextAreaLine );
	sym( "%TextAreaCursor%textarea%units=1",bbTextAreaCursor );
	sym( "%TextAreaSelLen%textarea%units=1",bbTextAreaSelLen );
	sym( "AddTextAreaText%textarea$text",bbAddTextAreaText );
	sym( "SetTextAreaText%textarea$text%pos=0%len=-1%units=1",bbSetTextAreaText );
	sym( "FormatTextAreaText%textarea%red%green%blue%flags%pos=0%len=-1%units=1",bbFormatTextAreaText );
	sym( "$TextAreaText%textarea%pos=0%len=-1%units=1",bbTextAreaText );
	sym( "LockTextArea%textarea",bbLockTextArea );
	sym( "UnlockTextArea%textarea",bbUnlockTextArea );

	//toolbar
	sym( "%CreateToolBar$icons%x%y%width%height%group%style=0",bbCreateToolBar );
	sym( "EnableToolBarItem%toolbar%index",bbEnableToolBarItem );
	sym( "DisableToolBarItem%toolbar%index",bbDisableToolBarItem );
	sym( "SetToolBarTips%toolbar$tips",bbSetToolBarTips );

	//htmlview
	/*
	sym( "%CreateHtmlView%x%y%width%height%group%style=0",bbCreateHtmlView );
	sym( "HtmlViewGo%htmlview$URL",bbHtmlViewGo );
	sym( "HtmlViewRun%htmlview$URL",bbHtmlViewRun );
	sym( "HtmlViewBack%htmlview",bbHtmlViewBack );
	sym( "HtmlViewForward%htmlview",bbHtmlViewForward );
	sym( "%HtmlViewStatus%htmlview",bbHtmlViewStatus );
	sym( "$HtmlViewCurrentURL%htmlview",bbHtmlViewCurrentURL );
	sym( "$HtmlViewEventURL%htmlview",bbHtmlViewEventURL );
	*/

	//desktop
	sym( "%Desktop",bbDesktop );

	//gadgetlists
	sym( "ClearGadgetItems%gadget",bbClearGadgetItems );
	sym( "AddGadgetItem%gadget$item%select=0%icon=-1",bbAddGadgetItem );
	sym( "InsertGadgetItem%gadget%index$item%icon=-1",bbInsertGadgetItem );
	sym( "ModifyGadgetItem%gadget%index$item%icon=-1",bbModifyGadgetItem );
	sym( "RemoveGadgetItem%gadget%index",bbRemoveGadgetItem );
	sym( "SelectGadgetItem%gadget%index",bbSelectGadgetItem );
	sym( "$GadgetItemText%gadget%index",bbGadgetItemText );
	sym( "%CountGadgetItems%gadget",bbCountGadgetItems );
	sym( "%SelectedGadgetItem%gadget",bbSelectedGadgetItem );

	//requesters
	sym( "%RequestFont",bbRequestFont );
	sym( "%RequestColor%red=255%green=255%blue=255",bbRequestColor );
	sym( "%RequestedRed",bbRequestedRed );
	sym( "%RequestedGreen",bbRequestedGreen );
	sym( "%RequestedBlue",bbRequestedBlue );
	sym( "$RequestDir$prompt=\"\"$path=\"\"",bbRequestDir );
	sym( "$RequestFile$prompt=\"\"$exts=\"\"%save=0$defname=\"\"",bbRequestFile );
}
