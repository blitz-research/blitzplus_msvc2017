
#include "math.h"

#include <math.h>

static int rnd_state=0x1234;
static const int RND_A=48271;
static const int RND_M=2147483647;
static const int RND_Q=44488;
static const int RND_R=3399;

static const float dtor=0.0174532925199432957692369076848861f;
static const float rtod=57.2957795130823208767981548141052f;

float bbSin( float n ){ return (float)sin(n*dtor); }
float bbCos( float n ){ return (float)cos(n*dtor); }
float bbTan( float n ){ return (float)tan(n*dtor); }
float bbASin( float n ){ return (float)asin(n)*rtod; }
float bbACos( float n ){ return (float)acos(n)*rtod; }
float bbATan( float n ){ return (float)atan(n)*rtod; }
float bbATan2( float n,float t ){ return (float)atan2(n,t)*rtod; }
float bbSqr( float n ){ return (float)sqrt(n); }
float bbFloor( float n ){ return (float)floor(n); }
float bbCeil( float n ){ return (float)ceil(n); }
float bbExp( float n ){ return (float)exp(n); }
float bbLog( float n ){ return (float)log(n); }
float bbLog10( float n ){ return (float)log10(n); }

#define FLT_MIN 1.175494351e-38F

static float rnd(){
	rnd_state=RND_A*(rnd_state%RND_Q)-RND_R*(rnd_state/RND_Q);
	if( rnd_state<0 ) rnd_state+=RND_M;

	return (rnd_state & 0xffffff0) / 268435456.0f;    // divide by 2^28
}

static double rnd2(){
	const double TWO27 = 134217728.0;	//  2^27
	const double TWO29 = 536870912.0;

	double rHi,rLo;	// two 'halves' of the fraction

	rnd_state=RND_A*(rnd_state%RND_Q)-RND_R*(rnd_state/RND_Q);
	if( rnd_state<0 ) rnd_state+=RND_M;

	rHi = (double)(rnd_state & 0x1ffffffc);		// 27 bits

	rnd_state=RND_A*(rnd_state%RND_Q)-RND_R*(rnd_state/RND_Q);
	if( rnd_state<0 ) rnd_state+=RND_M;

	rLo = (double)(rnd_state & 0x1ffffff8);		// 26 bits

	return ( rHi + rLo/TWO27 ) / TWO29;	// 27+29 = 53 + 3

}

float bbRnd( float from,float to ){
	return rnd()*(to-from)+from;
}

int bbRand( int from,int to ){
	if( from>to ){
		int t=from;
		from=to;
		to=t;
	}
	int r=int( rnd2() * (double)(to-from+1) ) + from;
	if( r>to ) r=to;
	return r;
}

void bbSeedRnd( int seed ){
	seed&=0x7fffffff;
	if( seed==0 || seed==RND_M ) seed=0x1234;
	rnd_state=seed;
}

int  bbRndSeed(){
	return rnd_state;
}

/*

// Dec 18, 2002
// 
// Improved random number functions for bbmath.cpp.
//
// Existing functions rnd() and bbRand() have been updated.
// rnd2() is new, double precision version of rnd().


// random float in [0,1)         = (24-bit integer) / (2^24)
static inline float rnd(){
	rnd_state=RND_A*(rnd_state%RND_Q)-RND_R*(rnd_state/RND_Q);
	if( rnd_state<0 ) rnd_state+=RND_M;

	return (rnd_state & 0xffffff0) / 268435456.0f;    // divide by 2^28
}

// random double in [0,1)        = (53-bit integer) / (2^53)
double rnd2(){

	const double TWO27 = 134217728.0;	//  2^27
	const double TWO29 = 536870912.0;

	double rHi, rLo;	// two 'halves' of the fraction

	rnd_state=RND_A*(rnd_state%RND_Q)-RND_R*(rnd_state/RND_Q);
	if( rnd_state<0 ) rnd_state+=RND_M;

	rHi = (double)(rnd_state & 0x1ffffffc);		// 27 bits

	rnd_state=RND_A*(rnd_state%RND_Q)-RND_R*(rnd_state/RND_Q);
	if( rnd_state<0 ) rnd_state+=RND_M;

	rLo = (double)(rnd_state & 0x1ffffff8);		// 26 bits

	return ( rHi + rLo/TWO27 ) / TWO29;		// 27+29 = 53 + 3
}

// Random 32-bit signed integer in [from,to].
int bbRand( int from,int to ){
	if( to<from ){
		int tmp=to;to=from;from=tmp;
	}

	return int( rnd2() * ( (double)to - (double)from + 1.0 ) ) + from;
}

float bbRnd( float from,float to ){
	return rnd()*(to-from)+from;
}

void bbSeedRnd( int seed ){
	seed&=0x7fffffff;
	rnd_state=seed ? seed : 1;
}

int  bbRndSeed(){
	return rnd_state;
}

*/
