
#include "filesys.h"

BBFile::BBFile( FILE *file ):_file(file){
}

BBFile::BBFile( BBString *file,int mode ):_file(0){
	const char *md;
	switch( mode ){
	case READ:md="rb";break;
	case WRITE:md="wb";break;
	case READWRITE:md="r+b";break;
	default:bbError( "Illegal file mode" );
	}

	_file=fopen( file->c_str(),md );
}

BBFile::~BBFile(){
	debug();
	if( _file ) fclose( _file );
}

int BBFile::read( char *buf,int sz ){
	return fread( buf,1,sz,_file );
}

int BBFile::write( const char *buf,int sz ){
	return fwrite( buf,1,sz,_file );
}

int BBFile::avail(){
	return 0;
}

int BBFile::eof(){
	int ch=getc(_file);
	if( ch!=EOF ) ungetc(ch,_file);
	return ch==EOF;
};

int BBFile::pos(){
	debug();
	return ftell(_file);
}

int BBFile::seek( int pos ){
	debug();
	fseek(_file,pos,SEEK_SET);
	return ftell(_file);
}

BBFile *bbOpenFile( BBString *file ){
	FILE *f=fopen( file->c_str(),"r+b" );
	return f ? new BBFile(f) : 0;
}

BBFile *bbReadFile( BBString *file ){
	FILE *f=fopen( file->c_str(),"rb" );
	return f ? new BBFile(f) : 0;
}

BBFile *bbWriteFile( BBString *file ){
	FILE *f=fopen( file->c_str(),"wb" );
	return f ? new BBFile(f) : 0;
}

void bbCloseFile( BBFile *file ){
	if( !file ) return;
	file->debug();
	file->release();
}

int	bbFilePos( BBFile *file ){
	return file->pos();
}

int	bbSeekFile( BBFile *file,int pos ){
	return file->seek(pos);
}
