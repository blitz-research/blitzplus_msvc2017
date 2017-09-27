
#include "stream.h"

#include <string>

using std::string;

int  bbEof( BBStream *s ){
	s->debug();
	return s->eof();
}

int  bbReadAvail( BBStream *s ){
	s->debug();
	return s->avail();
}

int  bbReadByte( BBStream *s ){
	s->debug();
	int n=0;
	s->read( (char*)&n,1 );
	return n;
}

int  bbReadShort( BBStream *s ){
	s->debug();
	int n=0;
	s->read( (char*)&n,2 );
	return n;
}

int  bbReadInt( BBStream *s ){
	s->debug();
	int n=0;
	s->read( (char*)&n,4 );
	return n;
}

float  bbReadFloat( BBStream *s ){
	s->debug();
	float n=0;
	s->read( (char*)&n,4 );
	return n;
}

BBString * bbReadString( BBStream *s ){
	s->debug();
	int len;
	BBString *str=0;
	if( s->read( (char*)&len,4 ) ){
		char *buff=new char[len];
		if( s->read( buff,len ) ){
			str=new BBString( buff,len );
		}
		delete[] buff;
	}
	return str ? str : BBString::null();
}

BBString *bbReadLine( BBStream *s ){
	s->debug();
	unsigned char c;
	string t;
	for(;;){
		if( s->read( (char*)&c,1 )!=1 ) break;
		if( c=='\n' ) break;
		if( c!='\r' ) t+=c;
	}
	return new BBString( t.data(),t.size() );
}

BBString *bbReadCString( BBStream *s ){
	s->debug();
	unsigned char c;
	string t;
	for(;;){
		if( s->read( (char*)&c,1 )!=1 ) break;
		if( !c ) break;
	}
	return new BBString( t.data(),t.size() );
}

void  bbWriteByte( BBStream *s,int n ){
	s->debug();
	s->write( (char*)&n,1 );
}

void  bbWriteShort( BBStream *s,int n ){
	s->debug();
	s->write( (char*)&n,2 );
}

void  bbWriteInt( BBStream *s,int n ){
	s->debug();
	s->write( (char*)&n,4 );
}

void  bbWriteFloat( BBStream *s,float n ){
	s->debug();
	s->write( (char*)&n,4 );
}

void  bbWriteString( BBStream *s,BBString *t ){
	s->debug();
	int n=t->size();
	s->write( (char*)&n,4 );
	s->write( t->data(),t->size() );
}

void  bbWriteLine( BBStream *s,BBString *t ){
	s->debug();
	s->write( t->data(),t->size() );
	s->write( "\r\n",2 );
}

void bbWriteCStr( BBStream *s,const char *t ){
	s->debug();
	s->write( t,strlen(t) );
}

void  bbCopyStream( BBStream *s,BBStream *d,int buff_size ){
	s->debug();
	d->debug();
	if( buff_size<1 || buff_size>1024*1024 ) bbError( "Illegal CopyStream buffer size" );

	char *buff=new char[buff_size];
	while( s->eof()==0 && d->eof()==0 ){
		int n=s->read( buff,buff_size );
		d->write( buff,n );
		if( n<buff_size ) break;
	}
	delete buff;
}
