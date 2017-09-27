
#ifndef WIN32DPNETGAME_H
#define WIN32DPNETGAME_H

#include "../app/app.h"

typedef unsigned long DPID;

int		 bbStartNetGame();
int		 bbHostNetGame( BBString *name );
int		 bbJoinNetGame( BBString *name,BBString *address );
void	 bbStopNetGame();

DPID	 bbCreateNetPlayer( BBString *name );
void	 bbDeleteNetPlayer( DPID player );
BBString*bbNetPlayerName( DPID player );
int		 bbNetPlayerLocal( DPID player );

int		 bbSendNetMsg( int type,BBString *msg,DPID from,DPID to,int reliable );

int		 bbRecvNetMsg();
int		 bbNetMsgType();
BBString*bbNetMsgData();
DPID	 bbNetMsgFrom();
DPID	 bbNetMsgTo();

#endif
