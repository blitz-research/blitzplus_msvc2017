
#include "movie.h"

static BBMovieDriver *_driver;

void bbSetMovieDriver( BBMovieDriver *t ){
	_driver=t;
}

BBMovie *bbLoadMovie( BBString *file ){
	return _driver->loadMovie( file );
}


