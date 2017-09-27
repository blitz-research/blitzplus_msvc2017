
#ifndef FILESYS_H
#define FILESYS_H

#include "../app/app.h"

#include "../stream/stream.h"

#include <stdio.h>

class BBDir : public BBResource{
public:
	virtual bool		moreFiles()=0;
	virtual BBString*	nextFile()=0;

	void debug(){ _debug(this,"Directory"); }
};

class BBFile : public BBStream{
	FILE *_file;
protected:
	~BBFile();
public:
	enum{
		READ=1,
		WRITE=2,
		READWRITE=3
	};

	BBFile( FILE *file );
	BBFile( BBString *file,int mode );

	//BBStream interface
	int read( char *buff,int size );
	int write( const char *buff,int size );
	int avail();
	int eof();

	virtual int pos();
	virtual int seek( int pos );

	void debug(){ _debug(this,"File"); }
};

//for bbFileType
enum{
	BBFILETYPE_NONE=0,
	BBFILETYPE_FILE=1,
	BBFILETYPE_DIRECTORY=2
};

BBFile*		bbOpenFile( BBString *file );
BBFile*		bbReadFile( BBString *file );
BBFile*		bbWriteFile( BBString *file );
void		bbCloseFile( BBFile *file );
int			bbFilePos( BBFile *file );
int			bbSeekFile( BBFile *file,int pos );

int			bbFileSize( BBString *file );
int			bbFileType( BBString *file );
int			bbCopyFile( BBString *from,BBString *to );
int			bbDeleteFile( BBString *file );

BBString*	bbCurrentDir();
int			bbChangeDir( BBString *dir );
int			bbCreateDir( BBString *dir );
int			bbDeleteDir( BBString *dir );

BBDir*		bbReadDir( BBString *dir );
int			bbMoreFiles( BBDir *dir );
void		bbCloseDir( BBDir *dir );
BBString*	bbNextFile( BBDir *dir );

#endif
