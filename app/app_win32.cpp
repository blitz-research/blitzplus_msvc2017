
#include "../stdc/stdc.h"

#if HOST_W32

#define WIN32_LEAN_AND_MEAN
#include <windows.h>
#include "app.h"
#include <stdio.h>
#include <stdlib.h>

static BBString *apptitle;

static FILE *con_in=stdin,*con_out=stdout;

static bool init_console(){
	if( AllocConsole() ){
		BBString *t=bbAppTitle();
		SetConsoleTitle( t->c_str() );
		t->release();
		con_in=fopen( "CONIN$","r" );
		con_out=fopen( "CONOUT$","w" );
		return true;
	}
	return false;
}

void bbWrite( BBString *str ){
	init_console();

	fputs( str->c_str(),con_out );
	fflush( con_out );
}

void bbPrint( BBString *str ){
	init_console();

	fputs( str->c_str(),con_out );
	fputc( '\n',con_out );
	fflush( con_out );
}

BBString* bbInput( BBString *prompt ){
	init_console();

	char buf[256];
	fputs( prompt->c_str(),con_out );
	fflush( con_out );
	if( !fgets( buf,256,con_in ) ) return BBString::null();
	BBString *t=new BBString(buf);
	BBString *p=t->trim();
	t->release();
	return p;
}

void bbSetAppTitle( BBString *title ){
	if( apptitle ) apptitle->release();
	if( apptitle=title ) apptitle->retain();
}

BBString *bbAppTitle(){
	if( !apptitle ){
		char buff[256]={0};
		GetModuleFileName( 0,buff,256 );
		char *p=strrchr(buff,'\\');
		if( p ) ++p;
		else p=buff;
		if( char *q=strrchr(buff,'.') ) *q=0;
		apptitle=new BBString(p);
	}
	return apptitle->copy();
}

static BBString *toDir( char *p ){
	if( int sz=strlen(p) ){
		if( p[sz-1]!='\\' ){
			p[sz]='\\';p[sz+1]=0;
		}
	}
	return new BBString( p );
}

BBString* bbSystemProperty( BBString *opt ){

	char buff[MAX_PATH+2];

	const char *p=opt->c_str();

	if( !stricmp(p,"cpu") ){
		return new BBString( "Intel" );
	}else if( !stricmp(p,"os") ){
		OSVERSIONINFO os={sizeof(os)};
		if( GetVersionEx( &os ) ){
			switch( os.dwMajorVersion ){
			case 3:
				switch( os.dwMinorVersion ){
				case 51:return new BBString( "Windows NT 3.1" );
				}
				break;
			case 4:
				switch( os.dwMinorVersion ){
				case 0:return new BBString( "Windows 95" );
				case 10:return new BBString( "Windows 98" );
				case 90:return new BBString( "Windows ME" );
				}
				break;
			case 5:
				switch( os.dwMinorVersion ){
				case 0:return new BBString( "Windows 2000" );
				case 1:return new BBString( "Windows XP" );
				}
				break;
			}
		}
	}else if( !stricmp(p,"appdir") ){
		if( GetModuleFileName( 0,buff,MAX_PATH ) ){
			if( char *t=strrchr(buff,'\\') ) t[1]=0;
			return new BBString( buff );
		}
	}else if( !stricmp(p,"windowsdir") ){
		if( GetWindowsDirectory( buff,MAX_PATH ) ) return toDir(buff);
	}else if( !stricmp(p,"systemdir") ){
		if( GetSystemDirectory( buff,MAX_PATH ) ) return toDir(buff);
	}else if( !stricmp(p,"tempdir") ){
		if( GetTempPath( MAX_PATH,buff ) ) return toDir(buff);
	}
	return BBString::null();
}

void _cdecl bbAbortf( const char *msg,... ){

	char buff[256];

	va_list args;
	va_start( args,msg );
	vsprintf( buff,msg,args );

	MessageBox( GetActiveWindow(),buff,"***** INTERNAL ERROR *****",MB_OK|MB_TOPMOST|MB_SETFOREGROUND );

	exit(0);
}

#endif
