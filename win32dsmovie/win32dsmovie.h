
#ifndef WIN32DSMOVIE_H
#define WIN32DSMOVIE_H
#define WIN32_LEAN_AND_MEAN

#include "../movie/movie.h"

#include "../win32dd/win32dd.h"

#include <windows.h>
#include <mmsystem.h>
#include <mmstream.h>
#include <amstream.h>
#include <ddstream.h>

class Win32DSMovie : public BBMovie{
	bool play;
	RECT src_rect;

	Win32DDGraphics *graphics;

	IMultiMediaStream *mm_stream;
	IDirectDrawMediaStream *dd_stream;

	IDirectDrawStreamSample *dd_sample;
protected:
	~Win32DSMovie();
public:
	Win32DSMovie( Win32DDGraphics *g,IMultiMediaStream *mm,IDirectDrawMediaStream *dd );

	int  width();
	int  height();
	bool render( BBGraphics *dst,int x,int y,int w,int h );
	bool playing();

	void render( Win32DDGraphics *dst,int x,int y,int w,int h );
};

class Win32DSMovieDriver : public BBMovieDriver{
public:
	Win32DSMovieDriver();

	bool startup();
	void shutdown();

	BBMovie *loadMovie( BBString *file );
};

extern Win32DSMovieDriver win32DSMovieDriver;
static Win32DSMovieDriver *_win32DSMovieDriver=&win32DSMovieDriver;

#endif