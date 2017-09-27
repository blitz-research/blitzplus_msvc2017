
#include "../app/app.h"
#include "win32font.h"

Win32FontDriver win32FontDriver;

static HDC _hdc;

Win32Font::Win32Font( HFONT hfont,int size ):_hfont(hfont){
	ExamineFont();
	if (size<0) size=-size;
	size=MulDiv(size,72,GetDeviceCaps(_hdc,LOGPIXELSY));
	_fsize=size;
}

Win32Font::Win32Font( BBString *name,int height,int flags ){

	int i=name->find(BBTMPSTR("."));

	if( i!=-1 ){
		AddFontResource( name->c_str() );
		name=name->extractFile( false );
	}else{
		name->retain();
	}

	_hfont=CreateFont( 
		height,0,0,0,
		(flags & BBFont::FONT_BOLD) ? FW_BOLD : FW_REGULAR,
		(flags & BBFont::FONT_ITALIC) ? true : false,
		(flags & BBFont::FONT_UNDERLINE) ? true : false,0,
		ANSI_CHARSET,OUT_DEFAULT_PRECIS,
		CLIP_DEFAULT_PRECIS,
		DEFAULT_QUALITY,DEFAULT_PITCH|FF_DONTCARE,name->c_str() );

	name->release();
	ExamineFont();
	_fsize=height;
}

Win32Font::~Win32Font(){
	DeleteObject( _hfont );
}

void Win32Font::ExamineFont()
{
	HFONT t=(HFONT)SelectObject( _hdc,_hfont );

	GetTextMetrics( _hdc,&_tm );

	char buffer[512];
	int res=GetTextFace(_hdc,512,buffer);

	_fname=new BBString(buffer);
	_fname->retain();

	_fstyle=0;
	if (_tm.tmItalic) _fstyle|=BBFont::FONT_ITALIC;
	if (_tm.tmUnderlined) _fstyle|=BBFont::FONT_UNDERLINE;
	if (_tm.tmWeight) _fstyle|=BBFont::FONT_BOLD;

	SelectObject( _hdc,t );		//restore hdc
}

void *Win32Font::query( int qid ){
	if( qid==BBQID_WIN32HFONT ) return _hfont;
	return BBFont::query( qid );
}

int Win32Font::width(){
	return _tm.tmMaxCharWidth;
}

int Win32Font::height(){
	return _tm.tmHeight;
}

int Win32Font::ascent(){
	return _tm.tmAscent;
}

int Win32Font::descent(){
	return _tm.tmDescent;
}

void Win32Font::measure( BBString *str,int *w,int *h ){

	HFONT t=(HFONT)SelectObject( _hdc,_hfont );

	SIZE sz;
	GetTextExtentPoint32( _hdc,str->c_str(),str->size(),&sz );
	*w=sz.cx;*h=sz.cy;

	SelectObject( _hdc,t );
}

BBString *Win32Font::name(){
	return _fname;
}

int  Win32Font::size(){
	return _fsize;
}

int  Win32Font::style(){
	return _fstyle;
}

Win32FontDriver::Win32FontDriver(){
	reg( "Win32FontDriver" );
}

bool Win32FontDriver::startup(){

	_hdc=CreateCompatibleDC(0);

	bbSetFontDriver(this);
	return true;
}

void Win32FontDriver::shutdown(){

	DeleteDC( _hdc );
}

BBFont *Win32FontDriver::loadFont( BBString *name,int height,int flags ){

	Win32Font *t=new Win32Font( name,height,flags );

	autoRelease(t);

	return t;
}
