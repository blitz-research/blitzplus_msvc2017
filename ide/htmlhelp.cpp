
#include "stdafx.h"
#include "htmlhelp.h"
#include "mainframe.h"
#include "libs.h"

IMPLEMENT_DYNAMIC( Html_Help,CHtmlView )
BEGIN_MESSAGE_MAP( Html_Help,CHtmlView )
	ON_WM_ERASEBKGND()
END_MESSAGE_MAP()

string Html_Help::getTitle(){
	return title;
}

void Html_Help::OnTitleChange( LPCTSTR t ){
	listener->helpTitleChange( this,title=t );
}

void Html_Help::OnBeforeNavigate2( LPCTSTR url,DWORD flags,LPCTSTR target,CByteArray& posted,LPCTSTR headers,BOOL* cancel ){
	string t( url );
	int attr=GetFileAttributes( url );if( attr==-1 ) attr=0;
	if( (attr & FILE_ATTRIBUTE_DIRECTORY) ||
		(t.rfind( ".bb" )+3==t.size()) ||
		(isMediaFile( t )) ){

		listener->helpOpen( this,t );
		*cancel=true;
		return;

	}
	*cancel=false;
}

BOOL Html_Help::OnEraseBkgnd( CDC *dc ){
	return true;
}

