
#include "win32dpnetgame.h"

#include "resource.h"

#include <dplay.h>
#include <dplobby.h>

#pragma warning(disable:4786)

#include <map>
#include <list>
#include <string>
#include <vector>

using namespace std;

static IDirectPlay4 *dirPlay;

typedef HRESULT (WINAPI*DPCREATE)(GUID*,IDirectPlayLobby**,IUnknown*,void*,DWORD);

static DPCREATE dpcreate;

struct Connection{
	GUID guid;
	string name;
	void *data;

	Connection( const GUID &g,const string &n,void *d,int sz ):guid(g),name(n){
		data=new char[sz];memcpy( data,d,sz );
	}

	~Connection(){
		delete[] data;
	}
};

struct Session{
	GUID guid;
	string name;
	int max_players,curr_players,data1,data2;

	Session( const DPSESSIONDESC2 *desc ){
		guid=desc->guidInstance;
		name=string( desc->lpszSessionNameA );
		max_players=desc->dwMaxPlayers;
		curr_players=desc->dwCurrentPlayers;
		data1=desc->dwUser1;data2=desc->dwUser2;
	}
};

static int timer;
static vector<Connection*> connections;
static vector<Session*> sessions;

static void clearSessions(){
	for( ;sessions.size();sessions.pop_back() ) delete sessions.back();
}

static void clearConnections(){
	for( ;connections.size();connections.pop_back() ) delete connections.back();
}

static bool openDirPlay( HWND hwnd ){
	if( dirPlay ) return true;
	if( CoCreateInstance( CLSID_DirectPlay,0,CLSCTX_ALL,IID_IDirectPlay4A,(void**)&dirPlay )>=0 ) return true;
	MessageBox( hwnd,"Error opening DirectPlay","DirectPlay Error",MB_ICONWARNING );
	return false;
}

static bool closeDirPlay( HWND hwnd ){
	if( hwnd && timer ) KillTimer( hwnd,timer );
	timer=0;if( !dirPlay ) return true;
	dirPlay->Close();
	int n=dirPlay->Release();
	dirPlay=0;return n==0;
}

static BOOL FAR PASCAL enumConnection( LPCGUID guid,LPVOID conn,DWORD size,LPCDPNAME name,DWORD flags,LPVOID context ){
	IDirectPlay4 *dp;
	if( CoCreateInstance( CLSID_DirectPlay,0,CLSCTX_ALL,IID_IDirectPlay4A,(void**)&dp )<0 ) return FALSE;
	int n=dp->InitializeConnection( conn,0 );
	dp->Release();if( n<0 ) return TRUE;

	Connection *c=new Connection( *guid,string( strdup( name->lpszShortNameA ) ),conn,size );
	connections.push_back( c );

	return TRUE;
}

static BOOL FAR PASCAL enumSession( LPCDPSESSIONDESC2 desc,LPDWORD timeout,DWORD flags,LPVOID lpContext ){

	if( !desc ) return FALSE;
	sessions.push_back( new Session( desc ) );
	return TRUE;
}

static bool startGame( HWND hwnd ){
	if( !dirPlay ) return false;

	char buff[MAX_PATH];
	int n=GetWindowText( GetDlgItem( hwnd,IDC_GAMENAME ),buff,MAX_PATH );
	if( !n ){
		MessageBox( hwnd,"Please enter a name for the new game","DirectPlay Request",MB_SETFOREGROUND|MB_TOPMOST|MB_ICONINFORMATION|MB_OK );
		return false;
	}

	string name=string( buff )+'\0';

	DPSESSIONDESC2 desc;
	memset(&desc,0,sizeof(desc));
	desc.dwSize=sizeof(desc);
	desc.guidApplication=GUID_NULL;
	desc.dwFlags=
		DPSESSION_KEEPALIVE|
		DPSESSION_MIGRATEHOST|
		DPSESSION_NOMESSAGEID|
		DPSESSION_OPTIMIZELATENCY|
		DPSESSION_DIRECTPLAYPROTOCOL;
	desc.lpszSessionNameA=(char*)name.data();

	if( dirPlay->Open( &desc,DPOPEN_CREATE )<0 ){
		MessageBox( hwnd,"Unable to create new game","DirPlay Error",MB_ICONWARNING );
		return false;
	}
	return true;
}

static bool joinGame( HWND hwnd ){
	if( !dirPlay ) return false;

	int ses=SendDlgItemMessage( hwnd,IDC_GAMELIST,LB_GETCURSEL,0,0 );
	if( ses<0 || ses>=sessions.size() ) return false;

	DPSESSIONDESC2 desc;
	memset(&desc,0,sizeof(desc));
	desc.dwSize=sizeof(desc);
	desc.guidInstance=sessions[ses]->guid;

	if( dirPlay->Open( &desc,DPOPEN_JOIN )<0 ){
		MessageBox( hwnd,"Unable to join game","DirPlay Error",MB_ICONWARNING );
		return false;
	}
	return true;
}

static bool enumSessions( HWND hwnd ){
	if( !dirPlay ) return false;

	clearSessions();
	EnableWindow( GetDlgItem( hwnd,IDC_GAMELIST ),true );
	SendDlgItemMessage( hwnd,IDC_GAMELIST,LB_RESETCONTENT,0,0 );

	DPSESSIONDESC2 desc;
	memset(&desc,0,sizeof(desc));
	desc.dwSize=sizeof(desc);
	desc.guidApplication=GUID_NULL;

	int n=dirPlay->EnumSessions( &desc,0,enumSession,0,DPENUMSESSIONS_ASYNC );
	if( n>=0 ){
		if( !timer ) SetTimer( hwnd,timer=1,1000,0 );
		for( int k=0;k<sessions.size();++k ){
			SendDlgItemMessage( hwnd,IDC_GAMELIST,LB_ADDSTRING,0,(LPARAM)strdup( sessions[k]->name.c_str() ) );
		}
		if( !sessions.size() ){
			SendDlgItemMessage( hwnd,IDC_GAMELIST,LB_ADDSTRING,0,(LPARAM)"<no games found>" );
			EnableWindow( GetDlgItem( hwnd,IDC_GAMELIST ),false );
		}
		return true;
	}
	closeDirPlay( hwnd );
	if( n==DPERR_USERCANCEL ) return false;
	MessageBox( hwnd,"Unable to enumerate sessions","DirPlay Error",MB_ICONWARNING );
	return false;
}

static bool connect( HWND hwnd ){
	int con=SendDlgItemMessage( hwnd,IDC_CONNECTIONS,CB_GETCURSEL,0,0 );
	if( con<1 || con>=connections.size() ) return false;

	closeDirPlay( hwnd );
	if( openDirPlay( hwnd ) ){
		int n=dirPlay->InitializeConnection( connections[con]->data,0 );
		if( n>=0 ){
			if( enumSessions( hwnd ) ) return true;
		}else{
			if( n!=DPERR_USERCANCEL ){
				string t="Unable to open "+connections[con]->name;
				MessageBox( hwnd,t.c_str(),"DirPlay Error",MB_ICONWARNING );
			}
		}
		closeDirPlay( hwnd );
	}
	return false;
}

static void endDialog( HWND hwnd,int rc ){
	if( timer ) KillTimer( hwnd,timer );
	timer=0;
	if( !rc ) closeDirPlay( hwnd );
	EndDialog( hwnd,rc );
}

static BOOL CALLBACK dialogProc( HWND hwnd,UINT msg,WPARAM wparam,LPARAM lparam ){

	int k,lo=LOWORD(wparam),hi=HIWORD(wparam);

	bool reset=false;

	switch( msg ){
	case WM_INITDIALOG:
		SetForegroundWindow( hwnd );
		clearConnections();
		connections.push_back( new Connection( GUID_NULL,"<no connection>","",0 ) );
		if( openDirPlay( hwnd ) ){
			if( dirPlay->EnumConnections( 0,enumConnection,0,0 )<0 ){
				MessageBox( hwnd,"Failed to enumerate connections","DirectPlay Error",MB_SETFOREGROUND|MB_TOPMOST|MB_ICONWARNING|MB_OK );
			}
			closeDirPlay( hwnd );
		}
		for( k=0;k<connections.size();++k ){
			string t=connections[k]->name;
			SendDlgItemMessage( hwnd,IDC_CONNECTIONS,CB_ADDSTRING,0,(LPARAM)t.c_str() );
		}
		timer=0;
		reset=true;
		break;
	case WM_TIMER:	//refresh sessions list!
		if( timer && wparam==timer && !enumSessions( hwnd ) ) reset=true;
		break;
	case WM_CLOSE:
		endDialog( hwnd,0 );
		break;
	case WM_COMMAND:
		switch( hi ){
		case BN_CLICKED:
			switch( lo ){
			case IDC_CANCEL:
				endDialog( hwnd,0 );
				break;
			case IDC_GAMENAME:case IDC_HOSTGAME:
				if( startGame( hwnd ) ){
					endDialog( hwnd,2 );
				}
				break;
			}
			break;
		case LBN_DBLCLK:
			switch( lo ){
			case IDC_GAMELIST:
				if( joinGame( hwnd ) ){
					endDialog( hwnd,1 );
				}
				break;
			}
			break;
		case CBN_SELCHANGE:
			switch( lo ){
			case IDC_CONNECTIONS:
				if( connect( hwnd ) ){
					EnableWindow( GetDlgItem( hwnd,IDC_GAMENAME ),true );
					EnableWindow( GetDlgItem( hwnd,IDC_HOSTGAME ),true );
					break;
				}else{
					reset=true;
				}
				break;
			}
			break;
		}
		break;
	default:
		return 0;
	}

	if( reset ){
		closeDirPlay( hwnd );
		SendDlgItemMessage( hwnd,IDC_CONNECTIONS,CB_SETCURSEL,0,0 );
		EnableWindow( GetDlgItem( hwnd,IDC_GAMELIST ),false );
		EnableWindow( GetDlgItem( hwnd,IDC_HOSTGAME ),false );
		EnableWindow( GetDlgItem( hwnd,IDC_GAMENAME ),false );
		SetWindowText( GetDlgItem( hwnd,IDC_GAMENAME ),"" );
		SendDlgItemMessage( hwnd,IDC_GAMELIST,LB_RESETCONTENT,0,0 );
	}
	return 1;
}

static void multiplay_setup_close(){
	closeDirPlay( 0 );
}

static int multiplay_setup_open(){
#ifdef _DEBUG
	int n=DialogBox( GetModuleHandle( "runtime_dbg" ),MAKEINTRESOURCE( IDD_MULTIPLAYER ),GetDesktopWindow(),dialogProc );
#else
	int n=DialogBox( GetModuleHandle( "runtime" ),MAKEINTRESOURCE( IDD_MULTIPLAYER ),GetDesktopWindow(),dialogProc );
#endif

	if( n!=1 && n!=2 ) n=0;

	clearSessions();
	clearConnections();

	return n;
}

static int multiplay_setup_host( const string &game_name ){
	int ret=0;
	IDirectPlayLobby *lobby;
	IDirectPlayLobby3 *lobby3;
	if( CoCreateInstance( CLSID_DirectPlay,0,CLSCTX_ALL,IID_IDirectPlay4A,(void**)&dirPlay )>=0 ){
//		if( DirectPlayLobbyCreate( 0,&lobby,0,0,0 )>=0 ){
		if( dpcreate( 0,&lobby,0,0,0 )>=0 ){
			if( lobby->QueryInterface( IID_IDirectPlayLobby3,(void**)&lobby3 )>=0 ){
				//ok, create an address for initializeconnection
				string ip( "\0" );
				char address[256];DWORD sz=256;
				if( lobby3->CreateAddress( DPSPGUID_TCPIP,DPAID_INet,ip.data(),ip.size(),address,&sz )>=0 ){
					if( dirPlay->InitializeConnection( address,0 )>=0 ){
						string name=game_name+'\0';
						DPSESSIONDESC2 desc;
						memset(&desc,0,sizeof(desc));
						desc.dwSize=sizeof(desc);
						desc.guidApplication=GUID_NULL;
						desc.dwFlags=
							DPSESSION_KEEPALIVE|
							DPSESSION_MIGRATEHOST|
							DPSESSION_NOMESSAGEID|
							DPSESSION_OPTIMIZELATENCY|
							DPSESSION_DIRECTPLAYPROTOCOL;
						desc.lpszSessionNameA=(char*)name.data();
						if( dirPlay->Open( &desc,DPOPEN_CREATE )>=0 ){
							ret=2;
						}
					}
				}
				lobby3->Release();
			}
			lobby->Release();
		}
		if( !ret ){
			dirPlay->Release();
			dirPlay=0;
		}
	}
	return ret;
}

static int multiplay_setup_join( const string &game_name,const string &ip_add ){
	int ret=0;
	IDirectPlayLobby *lobby;
	IDirectPlayLobby3 *lobby3;
	if( CoCreateInstance( CLSID_DirectPlay,0,CLSCTX_ALL,IID_IDirectPlay4A,(void**)&dirPlay )>=0 ){
//		if( DirectPlayLobbyCreate( 0,&lobby,0,0,0 )>=0 ){
		if( dpcreate( 0,&lobby,0,0,0 )>=0 ){
			if( lobby->QueryInterface( IID_IDirectPlayLobby3,(void**)&lobby3 )>=0 ){
				//ok, create an address for initializeconnection
				string ip=ip_add+'\0';
				char address[256];DWORD sz=256;
				if( lobby3->CreateAddress( DPSPGUID_TCPIP,DPAID_INet,ip.data(),ip.size(),address,&sz )>=0 ){
					if( dirPlay->InitializeConnection( address,0 )>=0 ){
						DPSESSIONDESC2 desc;
						memset(&desc,0,sizeof(desc));
						desc.dwSize=sizeof(desc);
						desc.guidApplication=GUID_NULL;
						if( dirPlay->EnumSessions( &desc,0,enumSession,0,0 )>=0 ){
							for( int k=0;k<sessions.size();++k ){
								if( sessions[k]->name!=game_name ) continue;
								desc.guidInstance=sessions[k]->guid;
								if( dirPlay->Open( &desc,DPOPEN_JOIN )>=0 ){
									ret=1;
								}
								break;
							}
						}
						clearSessions();
					}
				}
				lobby3->Release();
			}
			lobby->Release();
		}
		if( !ret ){
			dirPlay->Release();
			dirPlay=0;
		}
	}
	return ret;
}

struct Player;

static bool host;

static map<DPID,Player*> player_map;
static list<Player*> players,new_players;

static int msg_type;
static string msg_data;
static DPID msg_from,msg_to;

static char *recv_buff;
static int recv_buff_sz;

static char *send_buff;
static int send_buff_sz;

#pragma pack( push,1 )
struct bbMsg{
	DPID from,to;
	char type;
};
#pragma pack( pop )

struct Player{
	DPID id;
	string name;
	bool remote;

	Player( DPID i,const string &n,bool r ):id(i),name(n),remote(r){
		players.push_back( this );
		if( remote ) new_players.push_back( this );
		player_map.clear();
	}

	Player::~Player(){
		new_players.remove( this );
		players.remove( this );
		player_map.clear();
	}
};

static void chk(){
	if( !dirPlay ){
		bbError( "Multiplayer game not started" );
	}
}

static void clearPlayers(){
	while( players.size() ) delete players.back();
	new_players.clear();
	player_map.clear();
}

static Player *findPlayer( DPID id ){
	if( !player_map.size() ){
		list<Player*>::iterator it;
		for( it=players.begin();it!=players.end();++it ){
			player_map.insert( pair<DPID,Player*>( (*it)->id,(*it) ) );
		}
	}
	map<DPID,Player*>::iterator it=player_map.find( id );
	return it==player_map.end() ? 0 : it->second;
}

static BOOL FAR PASCAL enumPlayer( DPID id,DWORD type,LPCDPNAME name,DWORD flags,LPVOID context ){
	Player *p=findPlayer( id );if( p ) return TRUE;
	p=new Player( id,string( name->lpszShortNameA ),true );
	return TRUE;
}

static void multiplay_setup_create(){
	dirPlay=0;
}

static void multiplay_setup_destroy(){
	multiplay_setup_close();
}

static int startGame( int n ){
	clearPlayers();
	if( !n ) return 0;
	if( dirPlay->EnumPlayers( 0,enumPlayer,0,0 )>=0 ){
		host=n==2;
		return n;
	}
	multiplay_setup_close();
	return 0;
}

class Win32DPNetGame : public BBModule{
	HMODULE _module;
public:
	Win32DPNetGame(){
		reg( "Win32DPNetGame" );
	}

	bool startup(){

		_module=LoadLibrary( "dplayx.dll" );
		if( !_module ) return false;

		dpcreate=(DPCREATE)GetProcAddress(_module,"DirectPlayLobbyCreateA");
		if( !dpcreate ){
			FreeLibrary( _module );
			return false;
		}

		recv_buff_sz=send_buff_sz=1024;
		recv_buff=new char[recv_buff_sz];
		send_buff=new char[send_buff_sz];

		multiplay_setup_create();

		return true;
	}

	void shutdown(){
		bbStopNetGame();

		multiplay_setup_destroy();

		delete[] recv_buff;recv_buff=0;
		delete[] send_buff;send_buff=0;

		FreeLibrary( _module );
	}
}win32DPNetGame;

int bbStartNetGame(){
	if( dirPlay ){
		bbError( "Multiplayer game already started" );
	}
	return startGame( multiplay_setup_open() );
}

int bbHostNetGame( BBString *name ){
	if( dirPlay ){
		bbError( "Multiplayer game already started" );
	}
	string n=name->c_str();
	return startGame( multiplay_setup_host( n ) );
}

int bbJoinNetGame( BBString *name,BBString *address ){
	if( dirPlay ){
		bbError( "Multiplayer game already started" );
	}
	string n=name->c_str(),a=address->c_str();
	return startGame( multiplay_setup_join( n,a ) );
}

void bbStopNetGame(){
	multiplay_setup_close();
	clearPlayers();
}

DPID bbCreateNetPlayer( BBString *nm ){
	chk();

	string t=nm->c_str();
	string t0=t+'\0';

	DPID id;
	DPNAME name;
	memset( &name,0,sizeof( name ) );
	name.dwSize=sizeof(name);name.lpszShortNameA=(char*)t0.data();

	if( dirPlay->CreatePlayer( &id,&name,0,0,0,0 )<0 ) return 0;

	Player *p=new Player( id,t,false );

	if( players.size()==1 ){
		if( dirPlay->EnumPlayers( 0,enumPlayer,0,0 )<0 ){
			dirPlay->DestroyPlayer( id );
			delete p;
			return 0;
		}
	}
	return id;
}

void bbDeleteNetPlayer( DPID player ){
	chk();

	if( Player *p=findPlayer( player ) ){
		dirPlay->DestroyPlayer( player );
		delete p;
	}
}

BBString *bbNetPlayerName( DPID player ){
	if( !player ) return new BBString( "<all>" );
	Player *p=findPlayer( player );
	return new BBString( p ? p->name.c_str() : "<unknown>" );
}

int bbNetPlayerLocal( DPID player ){
	if( Player *p=findPlayer( player ) ) return p->remote ? 0 : 1;
	return 0;
}

int bbRecvNetMsg(){
	chk();

	msg_type=0;
	msg_data.resize(0);
	msg_from=DPID_UNKNOWN;msg_to=DPID_ALLPLAYERS;

 	while( !msg_type ){

		if( new_players.size() ){
			msg_from=new_players.front()->id;
			new_players.pop_front();
			msg_type=100;
			return 1;
		}

		DPID from,to;
		DWORD sz=recv_buff_sz;
		int n=dirPlay->Receive( &from,&to,0,recv_buff,&sz );

		if( n==DPERR_BUFFERTOOSMALL ){
			sz=recv_buff_sz=sz/2+sz;
			delete[] recv_buff;recv_buff=new char[recv_buff_sz];
			n=dirPlay->Receive( &from,&to,0,recv_buff,&sz );
		}

		if( n!=DP_OK ) return 0;

		if( from==DPID_SYSMSG ){
			switch( *(DWORD*)recv_buff ){
			case DPSYS_CREATEPLAYERORGROUP:
				if( DPMSG_CREATEPLAYERORGROUP *msg=(DPMSG_CREATEPLAYERORGROUP*)recv_buff ){
					if( findPlayer( from=msg->dpId ) ) continue;
					new Player( from,string( msg->dpnName.lpszShortNameA ),true );
					continue;
				}
				break;
			case DPSYS_DESTROYPLAYERORGROUP:
				if( DPMSG_DESTROYPLAYERORGROUP *msg=(DPMSG_DESTROYPLAYERORGROUP*)recv_buff ){
					Player *p=findPlayer( msg->dpId );if( !p ) continue;
					delete p;msg_from=msg->dpId;msg_type=101;
				}
				break;
			case DPSYS_HOST:
				if( !host ){
					host=true;msg_type=102;
				}
				break;
			case DPSYS_SESSIONLOST:
				msg_type=200;
				break;
			}
		}else{
			bbMsg *m=(bbMsg*)recv_buff;
			Player *p=findPlayer( m->from );
			if( p && !p->remote ) continue;
			msg_data=string( (char*)(m+1),sz-sizeof(bbMsg) );
			msg_from=m->from;msg_to=m->to;
			msg_type=m->type;
		}
	}
	return 1;
}

int bbNetMsgType(){
	return msg_type;
}

BBString *bbNetMsgData(){
	return new BBString( msg_data.c_str() );
}

DPID bbNetMsgFrom(){
	return msg_from;
}

DPID bbNetMsgTo(){
	return msg_to;
}

int bbSendNetMsg( int type,BBString *msg,DPID from,DPID to,int reliable ){
	chk();

	int sz=msg->size()+sizeof(bbMsg);
	if( sz>send_buff_sz ){
		send_buff_sz=sz/2+sz;
		delete send_buff;send_buff=new char[send_buff_sz];
	}
	bbMsg *m=(bbMsg*)send_buff;
	m->type=type;m->from=from;m->to=to;

	memcpy( m+1,msg->c_str(),msg->size() );

	if( !to ) to=DPID_ALLPLAYERS;
	int n=dirPlay->Send( from,to,reliable ? DPSEND_GUARANTEED : 0,send_buff,sz );

	return n>=0;
}

void netgame_link( void(*rtSym)(const char*,void*) ){
	rtSym( "%StartNetGame",bbStartNetGame );
	rtSym( "%HostNetGame$game_name",bbHostNetGame );
	rtSym( "%JoinNetGame$game_name$ip_address",bbJoinNetGame );
	rtSym( "StopNetGame",bbStopNetGame );

	rtSym( "%CreateNetPlayer$name",bbCreateNetPlayer );
	rtSym( "DeleteNetPlayer%player",bbDeleteNetPlayer );
	rtSym( "$NetPlayerName%player",bbNetPlayerName );
	rtSym( "%NetPlayerLocal%player",bbNetPlayerLocal );

	rtSym( "%SendNetMsg%type$msg%from_player%to_player=0%reliable=1",bbSendNetMsg );

	rtSym( "%RecvNetMsg",bbRecvNetMsg );
	rtSym( "%NetMsgType",bbNetMsgType );
	rtSym( "%NetMsgFrom",bbNetMsgFrom );
	rtSym( "%NetMsgTo",bbNetMsgTo );
	rtSym( "$NetMsgData",bbNetMsgData );
}

