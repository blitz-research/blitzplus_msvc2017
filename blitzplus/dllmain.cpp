
#pragma warning(disable:4786)

#include "link.h"
#include "config.h"

#include <float.h>

#include <map>
#include <string>

using namespace std;

class Runtime{
public:
	virtual int version();
	virtual const char *nextSym();
	virtual int symValue( const char *sym );
	virtual void startup( void *hinstance );
	virtual void shutdown();
	virtual void asyncStop();
	virtual void asyncRun();
	virtual void asyncEnd();
	virtual void checkmem( void *streambuf );

	virtual void execute( void (*pc)(),const char *args,BBDebugger *dbg );
};

static map<const char*,void*> _syms;
static map<const char*,void*>::const_iterator _symit;

static void rtSym( const char *sym,void *val ){
	_syms.insert( make_pair(sym,val) );
}

int Runtime::version(){
	return VERSION;
}

const char *Runtime::nextSym(){
	if( !_syms.size() ){
		bpLink( rtSym );
		_symit=_syms.begin();
	}
	if( _symit==_syms.end() ){
		_syms.clear();
		return 0;
	}
	return (_symit++)->first;
}

int Runtime::symValue( const char *sym ){
	map<const char*,void*>::iterator it=_syms.find( sym );
	if( it!=_syms.end() ) return (int)it->second;
	return -1;
}

void Runtime::startup( void *hinstance ){
}

void Runtime::shutdown(){
	_syms.clear();
}

void Runtime::checkmem( void *buf ){
}

void Runtime::execute( void (*pc)(),const char *args,BBDebugger *debugger ){

	_control87( _RC_NEAR|_PC_24|_EM_INVALID|_EM_ZERODIVIDE|_EM_OVERFLOW|_EM_UNDERFLOW|_EM_INEXACT|_EM_DENORMAL,0xfffff );

	bbRun( pc,args,debugger );

	_control87( _CW_DEFAULT,0xfffff );
}

void Runtime::asyncStop(){
}

void Runtime::asyncRun(){
}

void Runtime::asyncEnd(){
}

extern "C" _declspec(dllexport) Runtime *_cdecl runtimeGetRuntime(){
	static Runtime runtime;
	return &runtime;
}

//************************** WinMain Entry ****************************

extern "C" _declspec(dllexport) int _stdcall bbWinMain();
extern "C" BOOL _stdcall _DllMainCRTStartup( HANDLE,DWORD,LPVOID );

bool WINAPI DllMain( HANDLE module,DWORD reason,void *reserved ){
	return TRUE;
}

static void *module_pc;
static map<string,int> module_syms;
static map<string,int> runtime_syms;
static Runtime *runtime;

static void fail( const char *err ){
	MessageBox( 0,err,"Unable to run Blitz Basic module",0 );
	ExitProcess(-1);
}

struct Sym{
	string name;
	int value;
};

static Sym getSym( void **p ){
	Sym sym;
	char *t=(char*)*p;
	while( char c=*t++ ) sym.name+=c;
	sym.value=*(int*)t+(int)module_pc;
	*p=t+4;return sym;
}

static int findSym( const string &t ){
	map<string,int>::iterator it;

	it=module_syms.find( t );
	if( it!=module_syms.end() ) return it->second;
	it=runtime_syms.find( t );
	if( it!=runtime_syms.end() ) return it->second;

	string err="Can't find symbol: "+t;
	fail( err.c_str() );
	return 0;
}

static string tolower( const string &t ){
	string o=t;
	for( int k=0;k<o.size();++k ) o[k]=tolower(o[k]);
	return o;
}

static void link(){

	while( const char *sc=runtime->nextSym() ){
		string t(sc);
		if( t[0]=='_' ){
			runtime_syms["_"+t]=runtime->symValue(sc);
			continue;
		}
		if( !isalnum(t[0]) ) t=t.substr(1);
		for( int k=0;k<t.size();++k ){
			if( isalnum(t[k]) || t[k]=='_' ) continue;
			t=t.substr( 0,k );break;
		}
		runtime_syms["_f"+tolower(t)]=runtime->symValue(sc);
	}

	//RT_RCDATA== 10 

	HRSRC hres=FindResource( 0,MAKEINTRESOURCE(1111),RT_RCDATA );
	if( !hres ) fail( "Can't find resource" );
	HGLOBAL hglo=LoadResource( 0,hres );
	if( !hglo ) fail( "Can't load resource" );
	void *p=LockResource( hglo );
	if( !p ) fail( "Can't lock resource" );

	int sz=*(int*)p;p=(int*)p+1;

	//replace malloc for service pack 2 Data Execution Prevention (DEP).
	module_pc=VirtualAlloc( 0,sz,MEM_COMMIT|MEM_RESERVE,PAGE_EXECUTE_READWRITE );

	memcpy( module_pc,p,sz );
	p=(char*)p+sz;

	int k,cnt;

	cnt=*(int*)p;p=(int*)p+1;
	for( k=0;k<cnt;++k ){
		Sym sym=getSym( &p );
		if( sym.value<(int)module_pc || sym.value>=(int)module_pc+sz ){
			fail( "Illegal symbol value" );
		}
		module_syms[sym.name]=sym.value;
	}

	cnt=*(int*)p;p=(int*)p+1;
	for( k=0;k<cnt;++k ){
		Sym sym=getSym( &p );
		int *pp=(int*)sym.value;
		int dest=findSym( sym.name );
		*pp+=dest-(int)pp;
	}

	cnt=*(int*)p;p=(int*)p+1;
	for( k=0;k<cnt;++k ){
		Sym sym=getSym( &p );
		int *pp=(int*)sym.value;
		int dest=findSym( sym.name );
		*pp+=dest;
	}

	runtime_syms.clear();
	module_syms.clear();
}

int __stdcall bbWinMain(){

//	MessageBox( 0,"bbWinMain","",MB_OK );

	HINSTANCE inst=GetModuleHandle( 0 );

	_DllMainCRTStartup( inst,DLL_PROCESS_ATTACH,0 );

	runtime=runtimeGetRuntime();
	runtime->startup( inst );

	link();

	//get cmd_line and params
	string cmd=GetCommandLine(),params;
	while( cmd.size() && cmd[0]==' ' ) cmd=cmd.substr( 1 );
	if( cmd.find( '\"' )==0 ){
		int n=cmd.find( '\"',1 );
		if( n!=string::npos ){
			params=cmd.substr( n+1 );
			cmd=cmd.substr( 1,n-1 );
		}
	}else{
		int n=cmd.find( ' ' );
		if( n!=string::npos ){
			params=cmd.substr( n+1 );
			cmd=cmd.substr( 0,n );
		}
	}

	runtime->execute( (void(*)())module_pc,params.c_str(),0 );

	runtime->shutdown();

	_DllMainCRTStartup( inst,DLL_PROCESS_DETACH,0 );

	ExitProcess(0);
	return 0;
}
