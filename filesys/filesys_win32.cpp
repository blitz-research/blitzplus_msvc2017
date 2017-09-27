
#ifdef WIN32

#define WIN32_LEAN_AND_MEAN

#include <windows.h>

#include "filesys.h"

class Win32Dir : public BBDir{
	bool _more;
	HANDLE _handle;
	WIN32_FIND_DATA _data;
protected:
	~Win32Dir();
public:
	Win32Dir( BBString *dir );

	bool moreFiles();
	BBString *nextFile();
};

Win32Dir::Win32Dir( BBString *dir ){
	char buf[MAX_PATH];
	strncpy( buf,dir->c_str(),MAX_PATH-1 );
	buf[MAX_PATH-1]=0;
	int sz=strlen(buf);
	if( sz && buf[sz-1]!='\\' && buf[sz-1]!='/' ) buf[sz++]='\\';
	buf[sz++]='*';
	buf[sz]=0;
	_handle=FindFirstFile( buf,&_data );
	_more=_handle!=INVALID_HANDLE_VALUE;
}

Win32Dir::~Win32Dir(){
	if( _handle!=INVALID_HANDLE_VALUE ) FindClose( _handle );
}

bool Win32Dir::moreFiles(){
	debug();
	return _more;
}

BBString *Win32Dir::nextFile(){
	debug();
	if( !_more ) return BBString::null();
	BBString *t=new BBString(_data.cFileName);
	_more=!!FindNextFile( _handle,&_data );
	return t;
}

int bbFileType( BBString *file ){
	DWORD t=GetFileAttributes( file->c_str() );
	return t==-1 ? BBFILETYPE_NONE :
	(t & FILE_ATTRIBUTE_DIRECTORY ? BBFILETYPE_DIRECTORY : BBFILETYPE_FILE);
}

int bbFileSize( BBString *file ){
	WIN32_FIND_DATA findData;
	HANDLE h=FindFirstFile( file->c_str(),&findData );
	if( h==INVALID_HANDLE_VALUE ) return 0;
	int n=findData.dwFileAttributes,sz=findData.nFileSizeLow;
	FindClose( h );return n & FILE_ATTRIBUTE_DIRECTORY ? 0 : sz;
}

int bbDeleteFile( BBString *file ){
	return !!DeleteFile( file->c_str() );
}

int bbCopyFile( BBString *from,BBString *to ){
//	return !!CopyFile( from->c_str(),to->c_str(),0 );
	if( !CopyFile( from->c_str(),to->c_str(),0 ) ) return 0;
	DWORD attr=GetFileAttributes( from->c_str() );
	attr&=~FILE_ATTRIBUTE_READONLY;
	SetFileAttributes( to->c_str(),attr );
	return 1;
}

int bbChangeDir( BBString *dir ){
	return !!SetCurrentDirectory( dir->c_str() );
}

BBString *bbCurrentDir(){
	char dir[MAX_PATH+1];

	dir[0]=0;
	GetCurrentDirectory( MAX_PATH,dir );

	int sz=strlen(dir);
	if( sz && dir[sz-1]!='\\' ){
		dir[sz]='\\';dir[sz+1]=0;
	}

	return new BBString(dir);
}

int bbCreateDir( BBString *dir ){
	return !!CreateDirectory( dir->c_str(),0 );
}

int bbDeleteDir( BBString *dir ){
	return !!RemoveDirectory( dir->c_str() );
}

BBDir*		bbReadDir( BBString *dir ){
	if( bbFileType(dir)!=BBFILETYPE_DIRECTORY ) return 0;
	return new Win32Dir( dir );
}

int		bbMoreFiles( BBDir *dir ){
	return dir->moreFiles();
}

void		bbCloseDir( BBDir *dir ){
	if( !dir ) return;
	dir->debug();
	dir->release();
}

BBString*	bbNextFile( BBDir *dir ){
	return dir->nextFile();
}

#endif