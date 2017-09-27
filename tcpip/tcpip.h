
#ifndef TCPIP_H
#define TCPIP_H

#include "../app/app.h"

class UDPStream;
class TCPStream;
class TCPServer;

int			bbCountHostIPs( BBString *host );
int			bbHostIP( int index );
UDPStream*	bbCreateUDPStream( int port );
void		bbCloseUDPStream( UDPStream *p );
int			bbRecvUDPMsg( UDPStream *p );
void		bbSendUDPMsg( UDPStream *p,int ip,int port );
int			bbUDPStreamIP( UDPStream *p );
int			bbUDPStreamPort( UDPStream *p );
int			bbUDPMsgIP( UDPStream *p );
int			bbUDPMsgPort( UDPStream *p );
void		bbUDPTimeouts( int rt );
BBString*	bbDottedIP( int ip );
TCPStream*	bbOpenTCPStream( BBString *server,int port,int local_port );
void		bbCloseTCPStream( TCPStream *p );
TCPServer*	bbCreateTCPServer( int port );
void		bbCloseTCPServer( TCPServer *p );
TCPStream*	bbAcceptTCPStream( TCPServer *server );
int			bbTCPStreamIP( TCPStream *p );
int			bbTCPStreamPort( TCPStream *p );
void		bbTCPTimeouts( int rt,int at );

class TCPIPModule : public BBModule{
public:
	TCPIPModule();

	bool startup();
	void shutdown();
};

extern TCPIPModule tcpipModule;

#endif