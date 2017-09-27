
#ifndef TIME_H
#define TIME_H

#include "../app/app.h"

int			bbMilliSecs();
void		bbDelay( int ms );

BBString*  	bbCurrentDate();
BBString*  	bbCurrentTime();

#endif