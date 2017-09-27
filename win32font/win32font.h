
#ifndef WIN32FONT_H
#define WIN32FONT_H
#define WIN32_LEAN_AND_MEAN

#include "../font/font.h"

#include <windows.h>

class Win32Font : public BBFont{
	HFONT _hfont;
	TEXTMETRIC _tm;
	BBString* _fname;
	int _fsize,_fstyle;
protected:
	~Win32Font();
	void ExamineFont();
public:
	Win32Font( HFONT font,int logsize );
	Win32Font( BBString *name,int size,int flags );

	void *query( int qid );

	int  width();
	int  height();
	int  ascent();
	int  descent();
	void measure( BBString *text,int *w,int *h );

	BBString *name();
	int  size();
	int  style();

	HFONT hfont()const{ return _hfont; }
};

class Win32FontDriver : public BBFontDriver{
public:
	Win32FontDriver();

	bool startup();
	void shutdown();

	BBFont *loadFont( BBString *name,int height,int flags );
};

extern Win32FontDriver win32FontDriver;
static Win32FontDriver *_win32FontDriver=&win32FontDriver;

#endif