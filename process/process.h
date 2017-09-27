
#ifndef PROCESS_H
#define PROCESS_H

#include "../stream/stream.h"

BBString*	bbGetEnv( BBString *var );
void		bbSetEnv( BBString *var,BBString *val );

int			bbExecFile( BBString *file );

BBStream*	bbCreateProcess( BBString *cmd );

#endif