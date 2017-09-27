
#include "stdafx.h"
#include "debugger.h"
#include "debuggerapp.h"
#include "resource.h"
#include "prefs.h"

DebuggerApp debuggerApp;

DebuggerApp::~DebuggerApp(){
}

BOOL DebuggerApp::InitInstance(){

	AfxInitRichEdit();

	m_pMainWnd=main_frame=new MainFrame();

	RECT rect;
	SystemParametersInfo( SPI_GETWORKAREA,0,&rect,0 );

	int x=rect.left;
	int w=rect.right-x;
	int h=240;
	int y=rect.bottom-h;

	main_frame->Create( 0,"Blitz Debugger",
		WS_OVERLAPPEDWINDOW|WS_CLIPCHILDREN,
		CRect( x,y,x+w,y+h ) );
	main_frame->ShowWindow( SW_SHOW );
	main_frame->UpdateWindow();

	return TRUE;
}

int DebuggerApp::ExitInstance(){
	return 0;
//	main_frame->DestroyWindow();		//???WTF???
//	return 0;
//	return CWinApp::ExitInstance();
}

MainFrame *DebuggerApp::mainFrame(){
	return debuggerApp.main_frame;
}

Debugger * _cdecl debuggerGetDebugger( void *mod,void *env ){

	debuggerApp.mainFrame()->setRuntime( mod,env );
	return debuggerApp.mainFrame();
}