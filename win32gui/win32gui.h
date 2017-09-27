
#ifndef WIN32GUI_H
#define WIN32GUI_H

#include "../gui/gui.h"

#include "win32iconstrip.h"
#include "win32label.h"
#include "win32button.h"
#include "win32textfield.h"
#include "win32slider.h"
#include "win32progbar.h"
#include "win32combobox.h"
#include "win32listbox.h"
#include "win32treeview.h"
#include "win32textarea.h"
#include "win32toolbar.h"
#include "win32htmlview.h"
#include "win32window.h"
#include "win32tabber.h"
#include "win32panel.h"
#include "win32splitter.h"

#include "win32desktop.h"

class Win32GuiDriver : public BBGuiDriver{
public:
	Win32GuiDriver();

	bool		startup();
	void		shutdown();

	BBLabel*	createLabel( BBGroup *group,int style );
	BBButton*	createButton( BBGroup *group,int style );
	BBTextField*createTextField( BBGroup *group,int style );
	BBSlider*	createSlider( BBGroup *group,int style );
	BBProgBar*	createProgBar( BBGroup *group,int style );
	BBComboBox*	createComboBox( BBGroup *group,int style );
	BBListBox*	createListBox( BBGroup *group,int style );
	BBTreeView*	createTreeView( BBGroup *group,int style );
	BBTextArea*	createTextArea( BBGroup *group,int style );
	BBToolBar*	createToolBar( BBGroup *group,int style );
	BBHtmlView*	createHtmlView( BBGroup *group,int style );
	BBWindow*	createWindow( BBGroup *group,int style );
	BBTabber*	createTabber( BBGroup *group,int style );
	BBPanel*	createPanel( BBGroup *group,int style );
	BBSplitter*	createSplitter( BBGroup *group,int style );

	BBDesktop*	desktop();

	BBIconStrip*createIconStrip( BBGraphics *graphics );

	void		notify( BBString *msg,bool serious );
	bool		confirm( BBString *msg,bool serious );
	int			proceed( BBString *msg,bool serious );

	BBFont*		requestFont();
	bool		requestColor( unsigned *rgb );
	BBString*	requestDir( BBString *prompt,BBString *path );
	BBString*	requestFile( BBString *prompt,BBString *exts,bool save,BBString *defname );

	HBITMAP		createBitmap( BBGraphics *graphics );

	bool		printBuffer( BBGraphics *graphics );
};

extern Win32GuiDriver win32GuiDriver;

static Win32GuiDriver *_win32GuiDriver=&win32GuiDriver;

#endif