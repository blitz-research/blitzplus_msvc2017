
#ifndef APP_H
#define APP_H

#include "../stdc/stdc.h"

#include	"app_mac.h"
#include	"app_win32.h"

#include	"debugger.h"
#include	"../string/string.h"

enum{
	BBAPP_READY,
	BBAPP_STARTING,
	BBAPP_RUNNING,
	BBAPP_ENDING
};

class		BBString;
class		BBDebugger;

int			bbAppState();

void		bbRun( void (*pc)(),const char *cmd_line,BBDebugger *dbg );

BBString*	bbAppTitle();
void		bbSetAppTitle( BBString *title );
BBString*	bbCommandLine();

void		bbLogf( const char *msg,... );
void		bbError( const char *err,... );
void		bbAbortf( const char *err,... );

void		bbWrite( BBString *str );
void		bbPrint( BBString *str );
BBString*	bbInput( BBString *prompt );
void		bbRuntimeError( BBString *err );

BBString*	bbSystemProperty( BBString *opt );

void		bbEnd();

#endif
