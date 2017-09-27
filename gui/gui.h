
#ifndef GUI_H
#define GUI_H

#include "iconstrip.h"
#include "label.h"
#include "button.h"
#include "textfield.h"
#include "slider.h"
#include "progbar.h"
#include "combobox.h"
#include "listbox.h"
#include "treeview.h"
#include "textarea.h"
#include "toolbar.h"
#include "htmlview.h"
#include "window.h"
#include "tabber.h"
#include "desktop.h"
#include "panel.h"
#include "splitter.h"

class BBGuiDriver : public BBModule{
public:
	virtual BBLabel*	createLabel( BBGroup *group,int style )=0;
	virtual BBButton*	createButton( BBGroup *group,int style )=0;
	virtual BBTextField*createTextField( BBGroup *group,int style )=0;
	virtual BBSlider*	createSlider( BBGroup *group,int style )=0;
	virtual BBProgBar*	createProgBar( BBGroup *group,int style )=0;
	virtual BBComboBox*	createComboBox( BBGroup *group,int style )=0;
	virtual BBListBox*	createListBox( BBGroup *group,int style )=0;
	virtual BBTreeView*	createTreeView( BBGroup *group,int style )=0;
	virtual BBTextArea*	createTextArea( BBGroup *group,int style )=0;
	virtual BBToolBar*	createToolBar( BBGroup *group,int style )=0;
	virtual BBHtmlView*	createHtmlView( BBGroup *group,int style )=0;

	virtual BBWindow*	createWindow( BBGroup *group,int style )=0;
	virtual BBTabber*	createTabber( BBGroup *group,int style )=0;
	virtual BBPanel*	createPanel( BBGroup *group,int style )=0;
	virtual BBSplitter*	createSplitter( BBGroup *group,int style )=0;

	virtual BBDesktop*	desktop()=0;

	virtual BBIconStrip*createIconStrip( BBGraphics *graphics )=0;

	virtual void		notify( BBString *msg,bool serious )=0;
	virtual bool		confirm( BBString *msg,bool serious )=0;
	virtual int			proceed( BBString *msg,bool serious )=0;

	virtual BBFont*		requestFont()=0;
	virtual bool		requestColor( unsigned *rgb )=0;
	virtual BBString*	requestDir( BBString *prompt,BBString *path )=0;
	virtual BBString*	requestFile( BBString *prompt,BBString *exts,bool save,BBString *defname )=0;
};

void		bbSetGuiDriver( BBGuiDriver *t );
BBGuiDriver*bbGuiDriver();

BBButton*	bbCreateButton( BBString *text,int x,int y,int w,int h,BBGroup *group,int style );
BBLabel*	bbCreateLabel( BBString *text,int x,int y,int w,int h,BBGroup *group,int style );
BBTextField*bbCreateTextField( int x,int y,int w,int h,BBGroup *group,int style );
BBSlider*	bbCreateSlider( int x,int y,int w,int h,BBGroup *group,int style );
BBProgBar*	bbCreateProgBar( int x,int y,int w,int h,BBGroup *group,int style );
BBComboBox*	bbCreateComboBox( int x,int y,int w,int h,BBGroup *group,int style );
BBListBox*	bbCreateListBox( int x,int y,int w,int h,BBGroup *group,int style );
BBTreeView*	bbCreateTreeView( int x,int y,int w,int h,BBGroup *group,int style );
BBTextArea*	bbCreateTextArea( int x,int y,int w,int h,BBGroup *group,int style );
BBToolBar*	bbCreateToolBar( BBString *icons,int x,int y,int w,int h,BBGroup *group,int style );
BBHtmlView*	bbCreateHtmlView( int x,int y,int w,int h,BBGroup *group,int style );
BBWindow*	bbCreateWindow( BBString *text,int x,int y,int w,int h,BBGroup *group,int style );
BBTabber*	bbCreateTabber( int x,int y,int w,int h,BBGroup *group,int style );
BBPanel*	bbCreatePanel( int x,int y,int w,int h,BBGroup *group,int style );
BBSplitter*	bbCreateSplitter( int x,int y,int w,int h,BBGroup *group,int style );

BBDesktop*	bbDesktop();

BBIconStrip*bbLoadIconStrip( BBString *file );

void		bbNotify( BBString *msg,bool serious );
int			bbConfirm( BBString *msg,bool serious );
int			bbProceed( BBString *msg,bool serious );

BBFont*		bbRequestFont();
int			bbRequestColor( int r,int g,int b );
int			bbRequestedRed();
int			bbRequestedGreen();
int			bbRequestedBlue();
BBString*	bbRequestDir( BBString *prompt,BBString *path );
BBString*	bbRequestFile( BBString *prompt,BBString *exts,bool save,BBString *defname );

#endif
