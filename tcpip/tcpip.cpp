
#include "tcpip.h"

#define WIN32_LEAN_AND_MEAN

#pragma warning(disable:4786)

#include <windows.h>
#include <winsock.h>

#include "../time/time.h"
#include "../stream/stream.h"

#include <set>
#include <vector>

using namespace std;

TCPIPModule tcpipModule;

static bool socks_ok;
static WSADATA wsadata;
static int recv_timeout;
static int read_timeout;
static int accept_timeout;

static void close( SOCKET sock,int e ){
	if( e<0 ){
		int opt=1;
		setsockopt( sock,SOL_SOCKET,SO_DONTLINGER,(char*)&opt,sizeof(opt) );
	}
	closesocket( sock );
}

class UDPStream;
class TCPStream;
class TCPServer;

class UDPStream : public BBStream{
protected:
	~UDPStream();
public:
	UDPStream( SOCKET s );

	int read( char *buff,int size );
	int write( const char *buff,int size );
	int avail();
	int eof();

	int recv();
	int send( int ip,int port );
	int getIP();
	int getPort();
	int getMsgIP();
	int getMsgPort();

	void debug(){ _debug(this,"UDP Stream"); }

private:
	SOCKET sock;
	vector<char> in_buf,out_buf;
	sockaddr_in addr,in_addr,out_addr;
	int in_get,e;
};

UDPStream::UDPStream( SOCKET s ):sock(s),in_get(0),e(0){
	int len=sizeof(addr);
	getsockname( s,(sockaddr*)&addr,&len );
	in_addr=out_addr=addr;
}

UDPStream::~UDPStream(){
	close( sock,e );
}

int UDPStream::read( char *buff,int size ){
	if( e ) return 0;
	int n=in_buf.size()-in_get;
	if( n<size ) size=n;
	memcpy( buff,&in_buf[in_get],size );
	in_get+=size;
	return size;
}

int UDPStream::write( const char *buff,int size ){
	if( e ) return 0;
	out_buf.insert( out_buf.end(),buff,buff+size );
	return size;
}

int UDPStream::avail(){
	if( e ) return 0;
	return in_buf.size()-in_get;
}

int UDPStream::eof(){
	return e ? e : in_get==in_buf.size();
}

//fill buffer, return sender
int UDPStream::recv(){
	if( e ) return 0;
	int tout;
	if( recv_timeout ) tout=bbMilliSecs()+recv_timeout;
	for(;;){
		int dt=0;
		if( recv_timeout ){
			dt=tout-bbMilliSecs();
			if( dt<0 ) dt=0;
		}
		fd_set fd={ 1,sock };
		timeval tv={ dt/1000,(dt%1000)*1000 };
		int n=::select( 0,&fd,0,0,&tv );
		if( !n ) return 0;
		if( n!=1 ){ e=-1;return 0; }
		unsigned long sz=-1;
		if( ioctlsocket( sock,FIONREAD,&sz ) ){ e=-1;return 0; }
		in_buf.resize( sz );in_get=0;
		int len=sizeof(in_addr);

		n=sz ? ::recvfrom(sock,&in_buf[0], sz, 0, (sockaddr*)&in_addr, &len) : 0;

		if( n==SOCKET_ERROR ) continue;	//{ e=-1;return 0; }
		in_buf.resize( n );
		return getMsgIP();
	}
	return 0;
}

//send, empty buffer
int UDPStream::send( int ip,int port ){
	if( e ) return 0;
	int sz=out_buf.size();
	out_addr.sin_addr.S_un.S_addr=htonl( ip );
	out_addr.sin_port=htons( port ? port : addr.sin_port );

	int n=sz ? ::sendto( sock,&out_buf[0],sz,0,(sockaddr*)&out_addr,sizeof(out_addr) ) : 0;

	if( n!=sz ) return e=-1;
	out_buf.clear();
	return sz;
}

int UDPStream::getIP(){
	return ntohl( addr.sin_addr.S_un.S_addr );
}

int UDPStream::getPort(){
	return ntohs( addr.sin_port );
}

int UDPStream::getMsgIP(){
	return ntohl( in_addr.sin_addr.S_un.S_addr );
}

int UDPStream::getMsgPort(){
	return ntohs( in_addr.sin_port );
}

class TCPStream : public BBStream{
protected:
	~TCPStream();
public:
	TCPStream( SOCKET s,TCPServer *t );

	int read( char *buff,int size );
	int write( const char *buff,int size );
	int avail();
	int eof();

	int getIP();
	int getPort();

	void debug(){ _debug(this,"TCP Stream"); }

private:
	SOCKET sock;
	TCPServer *server;
	int e,ip,port;
};

class TCPServer : public BBResource{
protected:
	~TCPServer();
public:
	TCPServer( SOCKET S );

	TCPStream *accept();

	void remove( TCPStream *s );

	void debug(){ _debug(this,"TCP Server"); }

private:
	int e;
	SOCKET sock;
	set<TCPStream*> accepted_set;
};

TCPStream::TCPStream( SOCKET s,TCPServer *t ):sock(s),server(t),e(0){
	sockaddr_in addr;
	int len=sizeof(addr);
	if( getpeername( s,(sockaddr*)&addr,&len ) ){
		ip=port=0;
		return;
	}
	ip=ntohl(addr.sin_addr.S_un.S_addr);
	port=ntohs(addr.sin_port);
}

TCPStream::~TCPStream(){
	if( server ) server->remove( this );
	close( sock,e );
}

int TCPStream::read( char *buff,int size ){
	if( e ) return 0;
	char *b=buff,*l=buff+size;
	int tout;
	if( read_timeout ) tout=bbMilliSecs()+read_timeout;
	while( b<l ){
		int dt=0;
		if( read_timeout ){
			dt=tout-bbMilliSecs();
			if( dt<0 ) dt=0;
		}
		fd_set fd={ 1,sock };
		timeval tv={ dt/1000,(dt%1000)*1000 };
		int n=::select( 0,&fd,0,0,&tv );
		if( n!=1 ){ e=-1;break; }
		n=::recv( sock,b,l-b,0 );
		if( n==0 ){ e=1;break; }
		if( n==SOCKET_ERROR ){ e=-1;break; }
		b+=n;
	}
	return b-buff;
}

int TCPStream::write( const char *buff,int size ){
	if( e ) return 0;
	int n=::send( sock,buff,size,0 );
	if( n==SOCKET_ERROR ){ e=-1;return 0; }
	return n;
}

int TCPStream::avail(){
	unsigned long t;
	int n=::ioctlsocket( sock,FIONREAD,&t );
	if( n==SOCKET_ERROR ){ e=-1;return 0; }
	return t;
}

int TCPStream::eof(){
	if( e ) return e;
	fd_set fd={ 1,sock };
	timeval tv={ 0,0 };
	switch( ::select( 0,&fd,0,0,&tv ) ){
	case 0:break;
	case 1:if( !avail() ) e=1;break;
	default:e=-1;
	}
	return e;
}

int TCPStream::getIP(){
	return ip;
}

int TCPStream::getPort(){
	return port;
}

TCPServer::TCPServer( SOCKET s ):sock(s),e(0){
}

TCPServer::~TCPServer(){
	close( sock,e );
}

TCPStream *TCPServer::accept(){
	if( e ) return 0;
	fd_set fd={ 1,sock };
	timeval tv={ accept_timeout/1000,(accept_timeout%1000)*1000 };
	int n=::select( 0,&fd,0,0,&tv );
	if( n==0 ) return 0;
	if( n!=1 ){ e=-1;return 0; }
	SOCKET t=::accept( sock,0,0 );
	if( t==INVALID_SOCKET ){ e=-1;return 0; }
	TCPStream *s=new TCPStream( t,this );
	attach( s );
	return s;
}

void TCPServer::remove( TCPStream *s ){
	accepted_set.erase( s );
}

static vector<int> host_ips;

static int findHostIP( const string &t ){
	int ip=inet_addr( t.c_str() );
	if( ip!=INADDR_NONE ) return ip;
	HOSTENT *h=gethostbyname( t.c_str() );
	if( !h ) return -1;
	char *p;
	for( char **list=h->h_addr_list;p=*list;++list ){
		return *(int*)p;
	}
	return 0;
}

int  bbCountHostIPs( BBString *host ){
	host_ips.clear();
	HOSTENT *h=gethostbyname( host->c_str() );
	if( !h ) return 0;
	char **p=h->h_addr_list;
	while( char *t=*p++ ) host_ips.push_back( ntohl(*(int*)t) );
	return host_ips.size();
}

int  bbHostIP( int index ){
	if( index<1 || index>host_ips.size() ){
		bbError( "Host index out of range" );
	}
	return host_ips[index-1];
}

UDPStream * bbCreateUDPStream( int port ){
	if( !socks_ok ) return 0;
	SOCKET s=::socket( AF_INET,SOCK_DGRAM,0 );
	if( s!=INVALID_SOCKET ){
		sockaddr_in addr={AF_INET,htons(port)};
		if( !::bind( s,(sockaddr*)&addr,sizeof(addr) ) ){
			UDPStream *p=new UDPStream( s );
			return p;
		}
		::closesocket( s );
	}
	return 0;
}

void  bbCloseUDPStream( UDPStream *p ){
	if( !p ) return;
	p->debug();
	p->release();
}

int  bbRecvUDPMsg( UDPStream *p ){
	p->debug();
	return p->recv();
}

void  bbSendUDPMsg( UDPStream *p,int ip,int port ){
	p->debug();
	p->send( ip,port );
}

int  bbUDPStreamIP( UDPStream *p ){
	p->debug();
	return p->getIP();
}

int  bbUDPStreamPort( UDPStream *p ){
	p->debug();
	return p->getPort();
}

int  bbUDPMsgIP( UDPStream *p ){
	p->debug();
	return p->getMsgIP();
}

int  bbUDPMsgPort( UDPStream *p ){
	p->debug();
	return p->getMsgPort();
}

void  bbUDPTimeouts( int rt ){
	recv_timeout=rt;
}

BBString * bbDottedIP( int ip ){
	char buff[64];
	sprintf( buff,"%i.%i.%i.%i",(ip>>24)&0xff,(ip>>16)&0xff,(ip>>8)&0xff,ip&0xff );
	return new BBString( buff );
}

TCPStream * bbOpenTCPStream( BBString *server,int port,int local_port ){
	if( !socks_ok ) return 0;
	int ip=findHostIP( server->c_str() );
	if( ip==-1 ) return 0;
	SOCKET s=::socket( AF_INET,SOCK_STREAM,0 );
	if( s!=INVALID_SOCKET ){
		if( local_port ){
			sockaddr_in addr={AF_INET,htons(local_port)};
			if( ::bind( s,(sockaddr*)&addr,sizeof(addr) ) ){
				::closesocket( s );
				return 0;
			}
		}
		sockaddr_in addr={AF_INET,htons(port)};
		addr.sin_addr.S_un.S_addr=ip;
		if( !::connect( s,(sockaddr*)&addr,sizeof(addr) ) ){
			TCPStream *p=new TCPStream( s,0 );
			return p;
		}
		::closesocket( s );
	}
	return 0;
}

void  bbCloseTCPStream( TCPStream *p ){
	if( !p ) return;
	p->debug();
	p->release();
}

TCPServer *  bbCreateTCPServer( int port ){
	SOCKET s=::socket( AF_INET,SOCK_STREAM,0 );
	if( s!=INVALID_SOCKET ){
		sockaddr_in addr={AF_INET,htons(port)};
		if( !::bind( s,(sockaddr*)&addr,sizeof(addr) ) ){
			if( !::listen( s,SOMAXCONN ) ){
				TCPServer *p=new TCPServer( s );
				return p;
			}
		}
		::closesocket(s);
	}
	return 0;
}

void  bbCloseTCPServer( TCPServer *p ){
	if( !p ) return;
	p->debug();
	p->release();
}

TCPStream *  bbAcceptTCPStream( TCPServer *server ){
	server->debug();
	if( TCPStream *tcp=server->accept() ){
		return tcp;
	}
	return 0;
}

int  bbTCPStreamIP( TCPStream *p ){
	p->debug();
	return p->getIP();
}

int  bbTCPStreamPort( TCPStream *p ){
	p->debug();
	return p->getPort();
}

void  bbTCPTimeouts( int rt,int at ){
	read_timeout=rt;
	accept_timeout=at;
}

TCPIPModule::TCPIPModule(){
	reg( "BBTcpIp" );
}

bool TCPIPModule::startup(){
	recv_timeout=0;
	read_timeout=10000;
	accept_timeout=0;
	socks_ok=WSAStartup( 0x0101,&wsadata )==0;
	return socks_ok;
}

void TCPIPModule::shutdown(){
	WSACleanup();
}