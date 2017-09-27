
#include "win32textarea.h"

#include <vector>

using std::vector;

static DWORD CALLBACK streamIn( DWORD cookie,BYTE *buff,LONG n,LONG *n_out ){
	const char **p=(const char**)cookie;
	const char *t=*p;
	while( n-- && *t ) *buff++=*t++;
	*n_out=t-*p;
	*p=t;
	return 0;
}

static DWORD CALLBACK streamOut( DWORD cookie,BYTE *buf,LONG n,LONG *n_out ){
	vector<char> *vec=(vector<char>*)cookie;
	for( int k=0;k<n;++k ){
		vec->push_back( *buf++ );
	}
	return 0;
}

Win32TextArea::Win32TextArea( BBGroup *group,int style ):BBTextArea(group,style),
_prot(false),_locked(0){

	HWND parent=(HWND)group->query( BBQID_WIN32CLIENTHWND );

	int xstyle=WS_EX_CLIENTEDGE;
	int wstyle=WS_CHILD|WS_VSCROLL;
	wstyle|=ES_MULTILINE|ES_NOHIDESEL|ES_AUTOVSCROLL;

	if( !(style&1) ){	//word wrap?
		wstyle|=WS_HSCROLL|ES_AUTOHSCROLL;
	}

	HWND hwnd=CreateWindowEx( xstyle,"RichEdit","",wstyle,0,0,0,0,parent,0,GetModuleHandle(0),0 );

	SendMessage( hwnd,EM_SETLIMITTEXT,1024*1024,0 );
	SendMessage( hwnd,EM_SETEVENTMASK,0,(LPARAM)(ENM_CHANGE|ENM_SELCHANGE|ENM_PROTECTED) );

	CHARFORMAT cf={sizeof(cf)};
	cf.dwMask=CFM_PROTECTED;
	cf.dwEffects=CFE_PROTECTED;

	lockAll();

	SendMessage( hwnd,EM_SETCHARFORMAT,SCF_SELECTION,(LPARAM)&cf );

	unlock();

	_gadget.setHwnd(hwnd);
	_gadget.setWndProc(this);
}

int Win32TextArea::adjustRange( int &pos,int &len,int units ){
	int t=length(units);
	if( pos<0 ) pos=0;
	else if( pos>t ) pos=t;
	if( len<0 || pos+len>t ) len=t-pos;
	if( !len ) return 0;
	if( units==UNITS_LINES ){
		int n=pos;
		pos=findChar(pos);
		len=findChar(n+len)-pos;
	}
	return len;
}

void *Win32TextArea::query( int qid ){
	if( void *p=_gadget.query( qid ) ) return p;
	return BBTextArea::query( qid );
}

void Win32TextArea::setFont( BBFont *font ){
	_gadget.setFont(font);
	BBTextArea::setFont(font);
}

void Win32TextArea::setText( BBString *text ){
	_gadget.setText( text );
	BBGadget::setText( text );
}

void Win32TextArea::setShape( int x,int y,int w,int h ){
	_gadget.setShape(x,y,w,h);
	BBTextArea::setShape(x,y,w,h); 
}

void Win32TextArea::setVisible( bool visible ){
	_gadget.setVisible(visible);
	BBTextArea::setVisible(visible);
}

void Win32TextArea::setEnabled( bool enabled ){
	_gadget.setEnabled(enabled);
	BBTextArea::setEnabled(enabled);
}

void Win32TextArea::activate(){
	_gadget.activate();
	BBTextArea::activate();
}

void Win32TextArea::setTabs( int tabs ){

	int tabTwips=1440*8/GetDeviceCaps( GetDC(0),LOGPIXELSX ) * tabs;
	PARAFORMAT pf={sizeof(pf)};
	pf.dwMask=PFM_TABSTOPS;
	pf.cTabCount=MAX_TAB_STOPS;
	for( int k=0;k<MAX_TAB_STOPS;++k ){
		pf.rgxTabs[k]=k*tabTwips;
	}

	lockAll();

	SendMessage( _gadget.hwnd(),EM_SETPARAFORMAT,0,(LPARAM)&pf );

	unlock();
}

void Win32TextArea::setTextColor( int r,int g,int b ){
	CHARFORMAT cf={sizeof(cf)};
	cf.dwMask=CFM_COLOR|CFM_PROTECTED;
	cf.dwEffects=CFE_PROTECTED;
	cf.crTextColor=(b<<16)|(g<<8)|r;

	lockAll();

	SendMessage( _gadget.hwnd(),EM_SETCHARFORMAT,SCF_SELECTION,(LPARAM)&cf );

	unlock();
}

void Win32TextArea::setBackgroundColor( int r,int g,int b ){
	SendMessage( _gadget.hwnd(),EM_SETBKGNDCOLOR,0,(LPARAM)((b<<16)|(g<<8)|r) );
}

int Win32TextArea::length( int units ){
	if( units==UNITS_LINES ){
		return SendMessage( _gadget.hwnd(),EM_GETLINECOUNT,0,0 );
	}
	return SendMessage( _gadget.hwnd(),WM_GETTEXTLENGTH,0,0 );
}

//return SOL pos
int Win32TextArea::findChar( int lin ){
	if( lin<0 ) return 0;
	if( lin>=length(UNITS_LINES) ) return length(UNITS_CHARS);
	return SendMessage( _gadget.hwnd(),EM_LINEINDEX,lin,0 );
}

int Win32TextArea::findLine( int pos ){
	if( pos<0 ) return 0;
	if( pos>length(UNITS_CHARS) ) return length(UNITS_LINES);
	return SendMessage( _gadget.hwnd(),EM_EXLINEFROMCHAR,0,pos );
}

int Win32TextArea::selLength( int units ){
	CHARRANGE cr;
	SendMessage( _gadget.hwnd(),EM_EXGETSEL,0,(LPARAM)&cr );
	if( units!=UNITS_LINES ){
		return cr.cpMax-cr.cpMin;
	}
	return findLine(cr.cpMax)-findLine(cr.cpMin)+1;
}

int Win32TextArea::cursor( int units ){
	CHARRANGE cr;
	SendMessage( _gadget.hwnd(),EM_EXGETSEL,0,(LPARAM)&cr );
	if( units!=UNITS_LINES ){
		return cr.cpMin;
	}
	return findLine( cr.cpMin );
}

void Win32TextArea::addText( BBString *text ){
	bool rtf=false;

	int fmt=rtf ? SF_RTF : SF_TEXT;
	const char *tp=text->c_str(),**p=&tp;
	EDITSTREAM es={(DWORD)p,0,streamIn};

	lock( length(UNITS_CHARS),0 );

	SendMessage( _gadget.hwnd(),EM_STREAMIN,fmt|SFF_SELECTION,(LPARAM)&es );

	unlock();

	CHARRANGE cr={length(UNITS_CHARS),length(UNITS_CHARS)};
	SendMessage( _gadget.hwnd(),EM_EXSETSEL,0,(LPARAM)&cr );
}

BBString* Win32TextArea::getText( int pos,int len,int units ){
	bool rtf=false;

	if( !adjustRange( pos,len,units ) ) return BBString::null();

	char *buf=new char[len+1];
	TEXTRANGE tr={{pos,pos+len},buf};

	SendMessage( _gadget.hwnd(),EM_GETTEXTRANGE,0,(LPARAM)&tr );

	BBString *t=new BBString( buf,len );
	delete[] buf;
	return t;
}

void Win32TextArea::setText( BBString *text,int pos,int len,int units ){
	bool rtf=false;

	adjustRange( pos,len,units );

	int fmt=rtf ? SF_RTF : SF_TEXT;
	const char *tp=text->c_str(),**p=&tp;
	EDITSTREAM es={(DWORD)p,0,streamIn};

	lock( pos,len );

	SendMessage( _gadget.hwnd(),EM_STREAMIN,fmt|SFF_SELECTION,(LPARAM)&es );

	unlock();
}

void Win32TextArea::formatText( int r,int g,int b,int flags,int pos,int len,int units ){

	if( !adjustRange( pos,len,units ) ) return;

	CHARFORMAT cf={sizeof(cf)};
	cf.dwMask=CFM_COLOR|CFM_BOLD|CFM_ITALIC|CFM_PROTECTED;
	cf.crTextColor=(b<<16)|(g<<8)|r;
	if( flags & BBTextArea::FORMAT_BOLD ) cf.dwEffects|=CFE_BOLD;
	if( flags & BBTextArea::FORMAT_ITALIC ) cf.dwEffects|=CFE_ITALIC;
	cf.dwEffects|=CFE_PROTECTED;

	lock( pos,len );

	CHARFORMAT of={sizeof(of)};
	SendMessage( _gadget.hwnd(),EM_GETCHARFORMAT,SCF_SELECTION,(DWORD)&of );

	if( ((cf.dwEffects^of.dwEffects)&(CFE_BOLD|CFE_ITALIC)) || cf.crTextColor!=of.crTextColor ){

		SendMessage( _gadget.hwnd(),EM_SETCHARFORMAT,SCF_SELECTION,(DWORD)&cf );
	}

	unlock();
}

void Win32TextArea::lock(){
	if( !_locked++ ){
		SendMessage( _gadget.hwnd(),EM_HIDESELECTION,1,0 );
		SendMessage( _gadget.hwnd(),EM_EXGETSEL,0,(LPARAM)&_lockedcr );
	}
}

void Win32TextArea::unlock(){
	if( !--_locked ){
		SendMessage( _gadget.hwnd(),EM_EXSETSEL,0,(LPARAM)&_lockedcr );
		SendMessage( _gadget.hwnd(),EM_HIDESELECTION,0,0 );
	}
}

void Win32TextArea::lock( int pos,int len ){
	lock();
	CHARRANGE cr={pos,pos+len};
	SendMessage( _gadget.hwnd(),EM_EXSETSEL,0,(LPARAM)&cr );
}

void Win32TextArea::lockAll(){
	lock();
	selectAll();
}

void Win32TextArea::selectAll(){
	SendMessage( _gadget.hwnd(),EM_SETSEL,0,-1 );
}

LRESULT Win32TextArea::wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc ){

	switch( msg ){
	case WM_COMMAND:
		switch( HIWORD(wp) ){
		case EN_CHANGE:
			if( _locked ) return 0;
			if( _prot ){
				emit( BBEvent::GADGET_ACTION,1 );
				_prot=false;
			}
			break;
		}
		return 0;
	case WM_NOTIFY:
		switch( ((NMHDR*)lp)->code ){
		case EN_SELCHANGE:
			if( _locked ) return 0;
			if( !_prot ){
				emit( BBEvent::GADGET_ACTION,0 );
			}
			break;
		case EN_PROTECTED:
			if( _locked ) return 0;
			_prot=true;
			break;
		}
		return 0;
	}
	return CallWindowProc( proc,hwnd,msg,wp,lp );
}
