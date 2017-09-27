
#ifdef WIN32

#include "process.h"

#include <stdio.h>
#include <windows.h>

class ProcStream : public BBStream{
	PROCESS_INFORMATION _pi;
	HANDLE _istr,_ostr;
	int _eof;
	CRITICAL_SECTION _lock;
protected:
	~ProcStream(){
		CloseHandle(_istr);
		CloseHandle(_ostr);
	}
public:
	ProcStream( PROCESS_INFORMATION pi,HANDLE istr,HANDLE ostr ):
	  _pi(pi),_istr(istr),_ostr(ostr),_eof(0){
	}

	int read( char *buf,int sz ){
		if( _eof ) return 0;
		DWORD n;
		if( !ReadFile( _istr,buf,sz,&n,0 ) ){
			_eof=GetLastError()==ERROR_BROKEN_PIPE ? 1 : -1;
		}
		return n;
	}

	int write( const char *buf,int sz ){
		if( _eof ) return 0;
		DWORD n;
		if( !WriteFile( _ostr,buf,sz,&n,0 ) ){
			_eof=GetLastError()==ERROR_BROKEN_PIPE ? 1 : -1;
		}
		return n;
	}

	int avail(){
		if( _eof ) return 0;
		DWORD n;
		if( !PeekNamedPipe( _istr,0,0,0,&n,0 ) ){
			_eof=GetLastError()==ERROR_BROKEN_PIPE ? 1 : -1;
		}
		return n;
	}

	int eof(){
		if( _eof ) return _eof;
		avail();return _eof;
	}
};

int bbExecFile( BBString *file ){

	const char *cmd=file->c_str();

	while( *cmd && *cmd==' ' ) ++cmd;
	if( !*cmd ) return -1;

	int sz;
	const char *p=cmd;
	if( *cmd=='\"' ){
		p=strchr( ++cmd,'\"' );
		if( !p ) return -1;
		sz=p++ - cmd;
	}else{
		while( *p && *p!=' ' ) ++p;
		sz=p-cmd;
	}
	if( sz<=0 || sz>255 ) return -1;

	char t_cmd[256];
	memcpy( t_cmd,cmd,sz );
	t_cmd[sz]=0;

	while( *p && *p==' ' ) ++p;

	return (int)ShellExecute( GetActiveWindow(),0,t_cmd,p,0,SW_SHOWDEFAULT )>32;
}

BBStream *bbCreateProcess( BBString *cmd ){

	SECURITY_ATTRIBUTES sa={sizeof(sa),0,true};

	HANDLE istr,p_ostr;	//our in-stream, process out-stream
	if( !CreatePipe( &istr,&p_ostr,&sa,0 ) ){
		//unable to create pipe
		return 0;
	}

	HANDLE ostr,p_istr;	//our out-stream, process in-stream
	if( !CreatePipe( &p_istr,&ostr,&sa,0 ) ){
		CloseHandle( istr );
		CloseHandle( p_ostr );
		//ditto
		return 0;
	}

	STARTUPINFO si={sizeof(si)};
	si.dwFlags=STARTF_USESTDHANDLES;
	si.hStdInput=p_istr;
	si.hStdOutput=p_ostr;
	si.hStdError=p_ostr;

	PROCESS_INFORMATION pi;

	if( !CreateProcess(
		0,
		(char*)cmd->c_str(),
		0,0,
		true,
		DETACHED_PROCESS,
		0,0,
		&si,
		&pi ) 
	){
		CloseHandle( istr );
		CloseHandle( ostr );
		CloseHandle( p_istr );
		CloseHandle( p_ostr );
		return 0;
	}

	CloseHandle( p_istr );
	CloseHandle( p_ostr );

	return new ProcStream( pi,istr,ostr );
}

#endif
