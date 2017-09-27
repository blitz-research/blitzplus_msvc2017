
#ifndef BBDEBUG_H
#define BBDEBUG_H

#include "../app/app.h"

void	bbDebugStop();
void	bbDebugStmt( int pos,const char *file );
void	bbDebugEnter( void *frame,void *env,const char *func );
void	bbDebugLeave();
void	bbDebugLog( BBString *msg );

#endif