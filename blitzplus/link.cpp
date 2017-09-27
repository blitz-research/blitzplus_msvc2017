
#pragma warning(disable:4786)

#include "link.h"
#include "basic.h"

void gui_link( void (*sym)( const char *t_sym,void *pc ) );
void b2d_link( void (*sym)( const char *t_sym,void *pc ) );
void sys_link( void (*sym)( const char *t_sym,void *pc ) );

void bpLink( void (*sym)( const char *t_sym,void *pc ) ){

	//BASIC
	basic_link( sym );
	sys_link( sym );
	gui_link( sym );
	b2d_link( sym );

}

