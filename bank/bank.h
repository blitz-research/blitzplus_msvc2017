
#ifndef BANK_H
#define BANK_H

#include "../stream/stream.h"

class BBBank : public BBResource{
	void *_data;
	int _size,_capacity;
protected:
	~BBBank();
public:
	BBBank();
	BBBank( int size );

	void setData( void *data );

	void resize( int size );
	void reserve( int capacity );

	void *data()const{ return _data; }
	int size()const{ return _size; }
	int capacity()const{ return _capacity; }

#ifdef _DEBUG
	void debug(){ _debug(this,"Bank"); }
	void debug( int offset ){
		debug();
		if( !_size || (offset>=0 && offset<_size) ) return;
		bbError( "Bank offset out of range" );
	}
	void debug( int offset , int count){
		debug();
		if( !_size || (offset>=0 && offset+count<=_size) ) return;
		bbError( "Bank region out of range" );
	}
#else
	void debug(){}
	void debug( int offset ){}
	void debug( int offset , int count ){}
#endif
};

BBBank*	bbCreateBank( int size );
void	bbFreeBank( BBBank *b );
int		bbBankSize( BBBank *b );
void	bbResizeBank( BBBank *b,int size );
void	bbCopyBank( BBBank *src,int src_p,BBBank *dest,int dest_p,int count );
int		bbPeekByte( BBBank *b,int offset );
int		bbPeekShort( BBBank *b,int offset );
int		bbPeekInt( BBBank *b,int offset );
float	bbPeekFloat( BBBank *b,int offset );
void	bbPokeByte( BBBank *b,int offset,int value );
void	bbPokeShort( BBBank *b,int offset,int value );
void	bbPokeInt( BBBank *b,int offset,int value );
void	bbPokeFloat( BBBank *b,int offset,float value );
int		bbReadBytes( BBBank *b,BBStream *s,int offset,int count );
int		bbWriteBytes( BBBank *b,BBStream *s,int offset,int count );
int		bbCallDLL( BBString *lib,BBString *func,BBBank *in_bank,BBBank *out_bank );

#endif