
#include "../app/app.h"

#include <string.h>
#include <stdio.h>
#include <stdlib.h>

#include <vector>

static BBModule *_mods;

struct Config{
	const char *subsys;
	char *config;
};

static int n_configs;
static Config configs[100];

static const char *getConfig( const char *subsys ){
	for( int k=0;k<n_configs;++k ){
		if( strcmp( configs[k].subsys,subsys ) ) continue;
		if( !configs[k].config ){
			bbError( "No config selected for subsystem '%s'",subsys );
			return 0;
		}
		return configs[k].config;
	}
	bbError( "Subsystem '%s' not found",subsys );
	return 0;
}

BBModule::BBModule():
_succ(_mods),
_name(0),_subsys(0),_config(0),_desc(0),
_state(STOPPED),_autorel(0){
	_mods=this;
}

void BBModule::reg( const char *name,const char *subsys,const char *config ){
	_name=name ? name : "";
	_subsys=subsys;
	_config=config;

	if( !_subsys ) return;

	for( int k=0;k<n_configs;++k ){
		if( !strcmp( configs[k].subsys,subsys ) ) return;
	}
	configs[n_configs].subsys=subsys;
	configs[n_configs].config=0;
	++n_configs;
}

bool BBModule::startup(){
	return true;
}

void BBModule::shutdown(){
}

void BBModule::autoRelease( BBResource *res ){
	if( !_autorel ) _autorel=new BBResource();
	_autorel->attach( res );
}

bool BBModule::start(){

	switch( _state ){
	case RUNNING:case STARTING:
		return true;
	}

	if( _state!=STOPPED ) return false;

//	bbLogf( "Starting module '%s'",_name );
	_state=STARTING;
	if( !startup() ){
		_state=FAILED;
		bbError( "Error starting module '%s'",_name );
		return false;
	}
	//move to start!
	BBModule **prev,*curr;
	for( prev=&_mods;(curr=*prev) && curr!=this;prev=&curr->_succ ){}
	if( !curr ) bbError( "BBModule!" );
	*prev=_succ;
	_succ=_mods;
	_mods=this;
	_state=RUNNING;
//	bbLogf( "Module '%s' started",_name );
	return true;
}

void BBModule::stop(){

	switch( _state ){
	case RUNNING:
//		bbLogf( "Stopping module '%s'",_name );
		_state=STOPPED;
		if( _autorel ){
			_autorel->release();
			_autorel=0;
		}
		shutdown();
//		bbLogf( "Module '%s' stopped",_name );
		return;
	}
}

void BBModule::debugModules(){

	puts( "********************************************" );
	puts( "* Module name                     State    *" );
	puts( "* ---------------------------------------- *" );

	for( BBModule *p=_mods;p;p=p->_succ ){

		const char *st="?????";
		switch( p->_state ){
		case FAILED:st="Failed";break;
		case STOPPED:st="Stopped";break;
		case STARTING:st="Starting";break;
		case RUNNING:st="Running";break;
		}

		char buf[45];
		memset( buf,' ',44 );
		memcpy( buf+2,p->_name,strlen(p->_name) );
		sprintf( buf+34,"%s",st );

		buf[strlen(buf)]=' ';
		buf[0]=buf[43]='*';
		buf[44]=0;
		puts(buf);
	}
	puts( "********************************************" );
}

//start system modules
void BBModule::startSystem(){
	BBModule *p;
	for( p=_mods;p;p=p->_succ ){
		if( p->_subsys ) continue;
		p->start();
	}
}

//stop ALL modules
void BBModule::stopSystem(){
	BBModule *p;
	for( p=_mods;p;p=p->_succ ){
		if( p->_subsys ) continue;
		p->stop();
	}
}

//start an individual module
bool BBModule::startModule( const char *name ){
	BBModule *p;
	for( p=_mods;p;p=p->_succ ){
		if( strcmp( name,p->_name ) ) continue;
		//start us up!
		p->start();
		return p->_state!=FAILED;
	}
	bbError( "Module '%s' not found",name );
	return false;
}

//stop an individual module
void BBModule::stopModule( const char *name ){
	BBModule *p;
	for( p=_mods;p;p=p->_succ ){
		if( strcmp( name,p->_name ) ) continue;
		p->stop();
		return;
	}
	bbError( "Module '%s' not found",name );
}

//set driver configuration
void BBModule::setDriver( const char *subsys,const char *config ){
	for( int k=0;k<n_configs;++k ){
		if( strcmp( configs[k].subsys,subsys ) ) continue;
		if( configs[k].config ) free( configs[k].config );
		configs[k].config=strdup( config );
		return;
	}
//	bbError( "Driver Subsystem '%s' not found",subsys );
}

//start ALL drivers!
bool BBModule::startDrivers(){
	BBModule *p;
	for( p=_mods;p;p=p->_succ ){
		if( !p->_subsys ) continue;
		if( p->_config ){
			if( strcmp( p->_config,getConfig(p->_subsys) ) ) continue;
		}
		p->start();
		if( p->_state==FAILED ) return false;
	}
	return true;
}

//start specific subsystem drivers
bool BBModule::startDrivers( const char *subsys ){
	BBModule *p;
	for( p=_mods;p;p=p->_succ ){
		if( !p->_subsys ) continue;
		if( strcmp( p->_subsys,subsys ) ) continue;
		if( p->_config ){
			if( strcmp( p->_config,getConfig(p->_subsys) ) ) continue;
		}
		p->start();
		if( p->_state==FAILED ) return false;
	}
	return true;
}

//stop all drivers
void BBModule::stopDrivers(){
	BBModule *p;
	for( p=_mods;p;p=p->_succ ){
		if( !p->_subsys ) continue;
		p->stop();
	}
}

//stop specific subsystem drivers
void BBModule::stopDrivers( const char *subsys ){
	BBModule *p;
	for( p=_mods;p;p=p->_succ ){
		if( !p->_subsys ) continue;
		if( strcmp( p->_subsys,subsys ) ) continue;
		p->stop();
	}
}

void bbDebugModules(){
	BBModule::debugModules();
}

