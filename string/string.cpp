
#include "../app/app.h"

#include <ctype.h>
#include <string.h>
#include <stdlib.h>
#include <stdio.h>

BBStringModule bbStringModule;

static BBString *_null;			//""
static BBString *_chrStrs[256];	//chr$(0) -> chr$(255)
static BBString *_nanStr,*_posInfStr,*_negInfStr;

BBStringModule::BBStringModule(){
	reg( "BBString" );
}

bool BBStringModule::startup(){

	_null=new BBString("");
	_nanStr=new BBString( "NaN" );
	_posInfStr=new BBString( "Infinity" );
	_negInfStr=new BBString( "-Infinity" );

	for( int k=0;k<256;++k ){
		char c=k;_chrStrs[k]=new BBString( &c,1 );
	}
	return true;
}

void BBStringModule::shutdown(){
}

BBString::BBString():_size(0),_data(0){
}

BBString::BBString( int sz ):_size(sz){
	_data=new char[_size+1];
	_data[_size]=0;
}

BBString::BBString( const char *t ):_size(strlen(t)){
	_data=new char[_size+1];
	memcpy(_data,t,_size);
	_data[_size]=0;
}

BBString::BBString( const char *t,int sz ):_size(sz){
	_data=new char[_size+1];
	memcpy(_data,t,_size);
	_data[_size]=0;
}

BBString::BBString( const BBString *t ):_size(t->_size){
	_data=new char[_size+1];
	memcpy(_data,t->_data,_size);
	_data[_size]=0;
}

BBString::~BBString(){
	delete[] _data;
}

BBString *BBString::concat( BBString *t ){
	if( !_size && !t->_size ) return null();
	BBString *p=new BBString( _size+t->_size );
	memcpy( p->_data,_data,_size );
	memcpy( p->_data+_size,t->_data,t->_size );
	return p;
}

int BBString::compare( BBString *t ){
	int n=_size;
	if( t->_size<n ) n=t->_size;
	if( int r=memcmp( _data,t->_data,n ) ) return r;
	return _size==t->_size ? 0 : (_size<t->_size ? -1 : 1);
}

int BBString::find( BBString *t ){
	if( !_size ) return -1;
	if( !t->_size ) return 0;
	int n=_size-t->_size+1;
	for( int k=0;k<n;++k ){
		if( !memcmp( _data+k,t->_data,t->_size ) ) return k;
	}
	return -1;
}

int BBString::find( BBString *t,int pos ){
	if( pos>=_size ) return -1;
	if( pos<0 ) pos=0;
	if( !t->_size ) return pos;
	int n=_size-t->_size+1;
	for( int k=pos;k<n;++k ){
		if( !memcmp( _data+k,t->_data,t->_size ) ) return k;
	}
	return -1;
}

BBString *BBString::substr( int pos ){
	if( pos<0 ) pos=0;
	if( pos>=_size ) return null();
	return new BBString( _data+pos,_size-pos );
}

BBString *BBString::substr( int pos,int n ){
	if( pos<0 ) {n+=pos;pos=0;}
	if( pos>=_size || n<=0 ) return null();
	if( pos+n>_size ) n=_size-pos;
	return new BBString( _data+pos,n );
}

BBString *BBString::trim(){
	const char *from=_data,*to=_data+_size;
	while( from<to && (unsigned char)from[0]<=' ' ) ++from;
	if( from==to ) return null();
	while( to>from && (unsigned char)to[-1]<=' ' ) --to;
	return new BBString( from,to-from );
}

BBString *BBString::left( int n ){
	if( n>_size ) n=_size;
	if( n<=0 ) return null();
	return new BBString( _data,n );
}

BBString *BBString::right( int n ){
	if( n>_size ) n=_size;
	if( n<=0 ) return null();
	return new BBString( _data+_size-n,n );
}

BBString *BBString::lset( int n ){
	if( n<=0 ) return null();
	BBString *p=new BBString(n);
	int c=n-_size;
	if( c>0 ){
		memcpy( p->_data,_data,_size );
		memset( p->_data+_size,' ',c );
	}else{
		memcpy( p->_data,_data,n );
	}
	return p;
}

BBString *BBString::rset( int n ){
	if( n<=0 ) return null();
	BBString *p=new BBString(n);
	int c=n-_size;
	if( c>0 ){
		memset( p->_data,' ',c );
		memcpy( p->_data+c,_data,_size );
	}else{
		memcpy( p->_data,_data-c,n );
	}
	return p;
}

BBString *BBString::toUpper(){
	if( !_size ) return null();
	BBString *p=new BBString( _size );
	for( int k=0;k<_size;++k ){
		p->_data[k]=toupper(_data[k]);
	}
	return p;
}

BBString *BBString::toLower(){
	if( !_size ) return null();
	BBString *p=new BBString( _size );
	for( int k=0;k<_size;++k ){
		p->_data[k]=tolower(_data[k]);
	}
	return p;
}

BBString *BBString::replace( BBString *from,BBString *to ){

	BBString *t=copy();

	int from_sz=from->_size,to_sz=to->_size;

	int i=0;
	while( (i=t->find(from,i))!=-1 ){

		int sz=t->size(),new_sz=sz-from_sz+to_sz;

		char *buf=new char[new_sz];

		memcpy( buf,t->data(),i );
		memcpy( buf+i,to->data(),to_sz );
		memcpy( buf+i+to_sz,t->data()+i+from_sz,sz-i-from_sz );

		t->release();
		t=new BBString( buf,new_sz );

		delete[] buf;

		i+=to_sz;
	}
	return t;
}

BBString *BBString::extractFile( bool ext ){

	const char *pos=_data;

	for(;;){
		const char *p=strchr(pos,'/');
		if( !p && !(p=strchr(pos,'\\')) ) break;
		pos=p+1;
	}

	int sz=_size-(pos-_data);

	if( !sz ) return BBString::null();

	if( !ext ){
		const char *p=pos+sz;
		while( --p>pos && *p!='.' ) {}
		if( *p=='.' ) sz=p-pos;
	}

	return new BBString( pos,sz );
}

BBString *BBString::extractPath( bool sep ){

	const char *pos=_data;

	for(;;){
		const char *p=strchr(pos,'/');
		if( !p && !(p=strchr(pos,'\\')) ) break;
		pos=p+1;
	}

	int sz=pos-_data;

	if( !sz ) return BBString::null();

	if( !sep ) --sz;

	return new BBString( _data,sz );
}

int BBString::toInt(){
	return atoi( _data );
}

float BBString::toFloat(){
	return (float)atof( _data );
}

double BBString::toDouble(){
	return atof( _data );
}

BBString *BBString::fromInt( int n ){
#ifdef __APPLE__
	char buf[32];
	sprintf( buf,"%i",n );
	return new BBString( buf );
#else
	char buf[32];
	itoa( n,buf,10 );
	return new BBString( buf );
#endif
}

BBString *BBString::fromFloat( float n ){
	return fromDouble(n);
}

BBString *BBString::chr( int n ){
	return _chrStrs[n&255]->copy();
}

BBString *BBString::rep( BBString *t,int n ){
	if( !t->_size || n<=0 ) return null();
	BBString *p=new BBString( t->_size*n );
	char *q=p->_data;
	while( n-- ){
		memcpy( q,t->_data,t->_size );
		q+=t->_size;
	}
	return p;
}

BBString *BBString::null(){
	return _null->copy();
}

BBString *bbStrStr( BBString *t,int n ){
	return BBString::rep( t,n );
}

BBString *bbLeft( BBString *t,int n ){
	return t->left(n);
}

BBString *bbRight( BBString *t,int n ){
	return t->right(n);
}

BBString *bbReplace( BBString *t,BBString *from,BBString *to ){
	return t->replace(from,to);
}

int	bbInstr( BBString *t,BBString *q,int from ){
	return t->find(q,from-1)+1;
}

BBString *bbMid( BBString *t,int pos,int n ){
	if( n<0 ) n=t->size();
	return t->substr(pos-1,n);
}

BBString *bbUpper( BBString *t ){
	return t->toUpper();
}

BBString *bbLower( BBString *t ){
	return t->toLower();
}

BBString *bbTrim( BBString *t ){
	return t->trim();
}

BBString *bbLSet( BBString *t,int n ){
	return t->lset(n);
}

BBString *bbRSet( BBString *t,int n ){
	return t->rset(n);
}

BBString *bbChr( int n ){
	return BBString::chr(n);
}

int bbAsc( BBString *t ){
	return t->size() ? t->data()[0] & 255 : -1;
}

int bbLen( BBString *t ){
	return t->size();
}

BBString *bbHex( int n ){
	char buff[8];
	for( int k=7;k>=0;n>>=4,--k ){
		int t=(n&15)+'0';
		buff[k]=t>'9' ? t+='A'-'9'-1 : t;
	}
	return new BBString( buff,8 );
}

BBString *bbBin( int n ){
	char buff[32];
	for( int k=31;k>=0;n>>=1,--k ){
		buff[k]=n&1 ? '1' : '0';
	}
	return new BBString( buff,32 );
}

static int _is_finite( double n ){		// definition: exponent anything but 2047.

	int e;					// 11 bit exponent
	const int eMax = 2047;	// 0x7ff, all bits = 1	
	
	int *pn = (int *) &n;

	e = *++pn;				// Intel order!
	e = ( e >> 20 ) & eMax;

	return e != eMax;
}

static int _is_nan( double n ){		// definition: exponent 2047, nonzero fraction.

	int e;					// 11 bit exponent
	const int eMax = 2047;	// 0x7ff, all bits = 1	
	
	int *pn = (int *) &n;

	e = *++pn;				// Intel order!
	e = ( e >> 20 ) & eMax;

	if ( e != 2047 ) return 0;	// almost always return here

	int fHi, fLo;				// 52 bit fraction

	fHi = ( *pn ) & 0xfffff;	// first 20 bits
	fLo = *--pn;				// last 32 bits

	return  ( fHi | fLo ) != 0;	// returns 0,1 not just 0,nonzero
}

///////////////////
//   By FLOYD!   //
///////////////////
BBString *BBString::fromDouble( double n ){

#ifdef __APPLE__
	char buf[64];
	sprintf( buf,"%g",n );
	return new BBString(buf);
#else
	static const int digits=6;			// something...
	static const int eNeg=-4,ePos=8;	// limits for e notation.

	if( !_is_finite(n) ){

		if( _is_nan(n) ) return _nanStr->copy();
		if( n>0.0 ) return _posInfStr->copy();
		if( n<0.0 ) return _negInfStr->copy();
		bbEnd();
	}

	int dec,sign;
	char buf[64];

	const char *tmp=_ecvt( n,digits,&dec,&sign );

	if( dec<=eNeg+1 || dec>ePos ){
		_gcvt( n,digits,buf );
		return new BBString( buf );

	}

	char *p=buf,*dp;
	*p++='-';

	if( dec<=0 ){

		*p++='0';
		*p++='.';dp=p+1;

		for( int k=0;k<-dec;++k ) *p++='0';

		while( *tmp ) *p++=*tmp++;

	}else if( dec<digits ){

		memcpy( p,tmp,dec );
		p+=dec;

		*p++='.';dp=p+1;

		tmp+=dec;
		while( *tmp ) *p++=*tmp++;

	}else{

		while( *tmp ) *p++=*tmp++;

		for( int k=0;k<dec-digits;++k ) *p++='0';

		*p++='.';dp=p+1;
		*p++='0';
	}

	while( p>dp && *(p-1)=='0' ) --p;

	if( sign ) return new BBString( buf,p-buf );

	return new BBString( buf+1,p-(buf+1) );
#endif
}
