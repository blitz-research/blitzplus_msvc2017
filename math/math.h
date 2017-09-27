
#ifndef MATH_H
#define MATH_H

#include "../string/string.h"

float		bbSin( float n );
float		bbCos( float n );
float		bbTan( float n );
float		bbASin( float n );
float		bbACos( float n );
float		bbATan( float n );
float		bbATan2( float n,float t );
float		bbSqr( float n );
float		bbFloor( float n );
float		bbCeil( float n );
float		bbExp( float n );
float		bbLog( float n );
float		bbLog10( float n );
float		bbRnd( float from,float to );
int			bbRand( int from,int to );
void		bbSeedRnd( int seed );
int			bbRndSeed();

#endif
