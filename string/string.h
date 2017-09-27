
#ifndef STRING_H
#define STRING_H

#include "../module/module.h"

class BBString : public BBObject{
	int _size;
	char *_data;
	BBString( int sz );
protected:
	~BBString();
public:
	BBString();
	BBString( const char *t );
	BBString( const char *t,int sz );
	BBString( const BBString *t );

	int			size()const{ return _size; }
	const char*	data()const{ return _data; }
	const char*	c_str()const{ return _data; }

	BBString*	copy(){ retain();return this; }

	BBString*	concat( BBString *t );
	int			compare( BBString *t );

	int			find( BBString *t );
	int			find( BBString *t,int pos );

	BBString*	substr( int pos );
	BBString*	substr( int pos,int n );
	BBString*	trim();
	BBString*	left( int n );
	BBString*	right( int n );
	BBString*	lset( int n );
	BBString*	rset( int n );
	BBString*	toUpper();
	BBString*	toLower();
	BBString*	replace( BBString *from,BBString *to );
	BBString*	extractFile( bool ext );
	BBString*	extractPath( bool sep );

	int			toInt();
	float		toFloat();
	double		toDouble();

	static BBString *fromInt( int n );
	static BBString *fromFloat( float n );
	static BBString *fromDouble( double n );

	static BBString *chr( int n );
	static BBString *rep( BBString *t,int n );

	static BBString *null();
};

class BBStringModule : public BBModule{
public:
	BBStringModule();

	bool startup();
	void shutdown();
};

extern BBStringModule bbStringModule;

#include "tmpstr.h"

BBString*	bbStrStr( BBString *s,int n );
BBString*	bbLeft( BBString *s,int n );
BBString*	bbRight( BBString *s,int n );
BBString*	bbReplace( BBString *s,BBString *from,BBString *to );
int			bbInstr( BBString *s,BBString *t,int from );
BBString* 	bbMid( BBString *s,int o,int n );
BBString*  	bbUpper( BBString *s );
BBString* 	bbLower( BBString *s );
BBString*  	bbTrim( BBString *s );
BBString*  	bbLSet( BBString *s,int n );
BBString*  	bbRSet( BBString *s,int n );
BBString*  	bbChr( int n );
int			bbAsc( BBString *s );
int			bbLen( BBString *s );
BBString* 	bbHex( int n );
BBString*  	bbBin( int n );

#endif