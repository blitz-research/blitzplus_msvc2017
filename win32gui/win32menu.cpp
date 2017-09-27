
#include "win32menu.h"

HMENU win32CreateMenu( const BBMenu *menu,bool pop ){

	HMENU hmenu=pop ? CreatePopupMenu() : CreateMenu();

	for( const BBMenu *child=menu->children();child;child=child->successor() ){

		BBString *txt=child->text();

		if( child->children() ){

			HMENU t=win32CreateMenu( child,true );

			int flags=MF_STRING;
			if( child->checked() ) flags|=MF_CHECKED;
			if( !child->enabled() ) flags|=MF_GRAYED;

			AppendMenu( hmenu,flags|MF_POPUP,(UINT)t,txt->c_str() );

		}else if( txt->size() ){

			int flags=MF_STRING;
			if( child->checked() ) flags|=MF_CHECKED;
			if( !child->enabled() ) flags|=MF_GRAYED;

			AppendMenu( hmenu,flags,child->tag()+100,txt->c_str() );

		}else{

			AppendMenu( hmenu,MF_SEPARATOR,child->tag()+100,txt->c_str() );
		}

		txt->release();
	}
	return hmenu;
}

/*
HMENU win32CreateMenu( const BBMenu *menu,bool pop ){
	HMENU hmenu=pop ? CreatePopupMenu() : CreateMenu();
}
*/