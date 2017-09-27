
#include "link.h"

void sys_link( void (*sym)( const char *t_sym,void *pc ) ){

	//APP
	sym( "%ExecFile$file",bbExecFile );
	sym( "$GetEnv$var",bbGetEnv );
	sym( "SetEnv$var$value",bbSetEnv );
	sym( "$CommandLine",bbCommandLine );
	sym( "Write$text",bbWrite );
	sym( "Print$text",bbPrint );
	sym( "RuntimeError$error",bbRuntimeError );
	sym( "$Input$prompt=\"\"",bbInput );
	sym( "Notify$text%serious=0",bbNotify );
	sym( "%Confirm$text%serious=0",bbConfirm );
	sym( "%Proceed$text%serious=0",bbProceed );
	sym( "AppTitle$str",bbSetAppTitle );
	sym( "$SystemProperty$property",bbSystemProperty );
	sym( "End",bbEnd );

	//OBJECT
	sym( "DebugObjects",bbDebugObjects );
	sym( "%ActiveObjects",bbActiveObjects );
	sym( "%QueryObject%object%query_id",bbQueryObject );

	//MODULE

	//DEBUG
	sym( "Stop",bbDebugStop );
	sym( "DebugLog$message",bbDebugLog );
	sym( "_bbDebugStmt",bbDebugStmt );
	sym( "_bbDebugEnter",bbDebugEnter );
	sym( "_bbDebugLeave",bbDebugLeave );

	//MATH
	sym( "#Sin#degrees",bbSin );
	sym( "#Cos#degrees",bbCos );
	sym( "#Tan#degrees",bbTan );
	sym( "#ASin#float",bbASin );
	sym( "#ACos#float",bbACos );
	sym( "#ATan#float",bbATan );
	sym( "#ATan2#floata#floatb",bbATan2 );
	sym( "#Sqr#float",bbSqr );
	sym( "#Floor#float",bbFloor );
	sym( "#Ceil#float",bbCeil );
	sym( "#Exp#float",bbExp );
	sym( "#Log#float",bbLog );
	sym( "#Log10#float",bbLog10 );
	sym( "#Rnd#from#to=0",bbRnd );
	sym( "%Rand%from%to=1",bbRand );
	sym( "SeedRnd%seed",bbSeedRnd );
	sym( "%RndSeed",bbRndSeed );

	//STRING
	sym( "$String$string%repeat",bbStrStr );
	sym( "$Left$string%count",bbLeft );
	sym( "$Right$string%count",bbRight );
	sym( "$Replace$string$from$to",bbReplace );
	sym( "%Instr$string$find%from=1",bbInstr );
	sym( "$Mid$string%start%count=-1",bbMid );
	sym( "$Upper$string",bbUpper );
	sym( "$Lower$string",bbLower );
	sym( "$Trim$string",bbTrim );
	sym( "$LSet$string%size",bbLSet );
	sym( "$RSet$string%size",bbRSet );
	sym( "$Chr%ascii",bbChr );
	sym( "%Asc$string",bbAsc );
	sym( "%Len$string",bbLen );
	sym( "$Hex%value",bbHex );
	sym( "$Bin%value",bbBin );

	//BANK
	sym( "%CreateBank%size=0",bbCreateBank );
	sym( "FreeBank%bank",bbFreeBank );
	sym( "%BankSize%bank",bbBankSize );
	sym( "ResizeBank%bank%size",bbResizeBank );
	sym( "CopyBank%src_bank%src_offset%dest_bank%dest_offset%count",bbCopyBank );
	sym( "%PeekByte%bank%offset",bbPeekByte );
	sym( "%PeekShort%bank%offset",bbPeekShort );
	sym( "%PeekInt%bank%offset",bbPeekInt );
	sym( "#PeekFloat%bank%offset",bbPeekFloat );
	sym( "PokeByte%bank%offset%value",bbPokeByte );
	sym( "PokeShort%bank%offset%value",bbPokeShort );
	sym( "PokeInt%bank%offset%value",bbPokeInt );
	sym( "PokeFloat%bank%offset#value",bbPokeFloat );
	sym( "%ReadBytes%bank%file%offset%count",bbReadBytes );
	sym( "%WriteBytes%bank%file%offset%count",bbWriteBytes );
	sym( "%CallDLL$dll_name$func_name%in_bank=0%out_bank=0",bbCallDLL );

	//EVENT
	sym( "AutoSuspend%enable",bbAutoSuspend );
	sym( "FlushEvents%kind=-1%source=0",bbFlushEvents );
	sym( "HotKeyEvent%rawkey%modifier%ev_id%ev_data=0%ev_x=0%ev_y=0%ev_z=0%ev_source=0",bbHotKeyEvent );

	//STREAM
	sym( "%Eof%stream",bbEof );
	sym( "%ReadAvail%stream",bbReadAvail );
	sym( "%ReadByte%stream",bbReadByte );
	sym( "%ReadShort%stream",bbReadShort );
	sym( "%ReadInt%stream",bbReadInt );
	sym( "#ReadFloat%stream",bbReadFloat );
	sym( "$ReadString%stream",bbReadString );
	sym( "$ReadLine%stream",bbReadLine );
	sym( "WriteByte%stream%byte",bbWriteByte );
	sym( "WriteShort%stream%short",bbWriteShort );
	sym( "WriteInt%stream%int",bbWriteInt );
	sym( "WriteFloat%stream#float",bbWriteFloat );
	sym( "WriteString%stream$string",bbWriteString );
	sym( "WriteLine%stream$string",bbWriteLine );
	sym( "CopyStream%src_stream%dest_stream%buffer_size=16384",bbCopyStream );

	//FILESYS
	sym( "%OpenFile$filename",bbOpenFile );
	sym( "%ReadFile$filename",bbReadFile );
	sym( "%WriteFile$filename",bbWriteFile );
	sym( "CloseFile%file_stream",bbCloseFile );
	sym( "%FilePos%file_stream",bbFilePos );
	sym( "%SeekFile%file_stream%pos",bbSeekFile );
	sym( "%FileSize$file",bbFileSize );
	sym( "%FileType$file",bbFileType );
	sym( "CopyFile$file$to",bbCopyFile );
	sym( "DeleteFile$file",bbDeleteFile );
	sym( "%ReadDir$dirname",bbReadDir );
	sym( "CloseDir%dir",bbCloseDir );
	sym( "%MoreFiles%dir",bbMoreFiles );
	sym( "$NextFile%dir",bbNextFile );
	sym( "$CurrentDir",bbCurrentDir );
	sym( "ChangeDir$dir",bbChangeDir );
	sym( "CreateDir$dir",bbCreateDir );
	sym( "DeleteDir$dir",bbDeleteDir );

	//TIME
	sym( "%MilliSecs",bbMilliSecs );
	sym( "Delay%millisecs",bbDelay );
	sym( "$CurrentDate",bbCurrentDate );
	sym( "$CurrentTime",bbCurrentTime );

	//TIMER
	sym( "%CreateTimer#hertz",bbCreateTimer );
	sym( "FreeTimer%timer",bbFreeTimer );
	sym( "%TimerTicks%timer",bbTimerTicks );
	sym( "%WaitTimer%timer",bbWaitTimer );
	sym( "ResetTimer%timer",bbResetTimer );
	sym( "PauseTimer%timer",bbPauseTimer );
	sym( "ResumeTimer%timer",bbResumeTimer );

	//PROCESS
	sym( "%CreateProcess$command",bbCreateProcess );

	//FONT
	sym( "%LoadFont$fontname%height%bold=0%italic=0%underline=0",bbLoadFont );
	sym( "FreeFont%font",bbFreeFont );

	sym( "$FontName%font",bbFontName );
	sym( "%FontSize%font",bbFontSize );
	sym( "%FontStyle%font",bbFontStyle );
	sym( "%FontAscent%font",bbFontAscent );
	sym( "%FontDescent%font",bbFontDescent );

	//TCPIP
	sym( "$DottedIP%IP",bbDottedIP );
	sym( "%CountHostIPs$host_name",bbCountHostIPs );
	sym( "%HostIP%host_index",bbHostIP );
	sym( "%CreateUDPStream%port=0",bbCreateUDPStream );
	sym( "CloseUDPStream%udp_stream",bbCloseUDPStream );
	sym( "SendUDPMsg%udp_stream%dest_ip%dest_port=0",bbSendUDPMsg );
	sym( "%RecvUDPMsg%udp_stream",bbRecvUDPMsg );
	sym( "%UDPStreamIP%udp_stream",bbUDPStreamIP );
	sym( "%UDPStreamPort%udp_stream",bbUDPStreamPort );
	sym( "%UDPMsgIP%udp_stream",bbUDPMsgIP );
	sym( "%UDPMsgPort%udp_stream",bbUDPMsgPort );
	sym( "UDPTimeouts%recv_timeout",bbUDPTimeouts );
	sym( "%OpenTCPStream$server%server_port%local_port=0",bbOpenTCPStream );
	sym( "CloseTCPStream%tcp_stream",bbCloseTCPStream );
	sym( "%CreateTCPServer%port",bbCreateTCPServer );
	sym( "CloseTCPServer%tcp_server",bbCloseTCPServer );
	sym( "%AcceptTCPStream%tcp_server",bbAcceptTCPStream );
	sym( "%TCPStreamIP%tcp_stream",bbTCPStreamIP );
	sym( "%TCPStreamPort%tcp_stream",bbTCPStreamPort );
	sym( "TCPTimeouts%read_millis%accept_millis",bbTCPTimeouts );

	//AUDIO
	sym( "%LoadSound$filename",bbLoadSound );
	sym( "FreeSound%sound",bbFreeSound );

	sym( "LoopSound%sound",bbLoopSound );
	sym( "SoundPitch%sound%pitch",bbSoundPitch );
	sym( "SoundVolume%sound#volume",bbSoundVolume );
	sym( "SoundPan%sound#pan",bbSoundPan );

	sym( "%PlaySound%sound%flags=0",bbPlaySound );
	sym( "%PlayMusic$file%flags=0",bbPlayMusic );
	sym( "%PlayCDTrack%track%flags=0",bbPlayCDTrack );

	sym( "StopChannel%channel",bbStopChannel );
	sym( "PauseChannel%channel",bbPauseChannel );
	sym( "ResumeChannel%channel",bbResumeChannel );
	sym( "ChannelPitch%channel%pitch",bbChannelPitch );
	sym( "ChannelVolume%channel#volume",bbChannelVolume );
	sym( "ChannelPan%channel#pan",bbChannelPan );
	sym( "%ChannelPlaying%channel",bbChannelPlaying );

	//NETGAME
	/*
	sym( "%StartNetGame",bbStartNetGame );
	sym( "%HostNetGame$game_name",bbHostNetGame );
	sym( "%JoinNetGame$game_name$ip_address",bbJoinNetGame );
	sym( "StopNetGame",bbStopNetGame );
	sym( "%CreateNetPlayer$name",bbCreateNetPlayer );
	sym( "DeleteNetPlayer%player",bbDeleteNetPlayer );
	sym( "$NetPlayerName%player",bbNetPlayerName );
	sym( "%NetPlayerLocal%player",bbNetPlayerLocal );
	sym( "%SendNetMsg%type$msg%from_player%to_player=0%reliable=1",bbSendNetMsg );
	sym( "%RecvNetMsg",bbRecvNetMsg );
	sym( "%NetMsgType",bbNetMsgType );
	sym( "%NetMsgFrom",bbNetMsgFrom );
	sym( "%NetMsgTo",bbNetMsgTo );
	sym( "$NetMsgData",bbNetMsgData );
	*/

	//JOYSTICK
	sym( "%JoyType%port=0",bbJoyType );
	sym( "%JoyDown%button%port=0",bbJoyDown );
	sym( "%JoyHit%button%port=0",bbJoyHit );
	sym( "#JoyX%port=0",bbJoyX );
	sym( "#JoyY%port=0",bbJoyY );
	sym( "#JoyZ%port=0",bbJoyZ );
	sym( "#JoyU%port=0",bbJoyU );
	sym( "#JoyV%port=0",bbJoyV );
	sym( "#JoyPitch%port=0",bbJoyPitch );
	sym( "#JoyYaw%port=0",bbJoyYaw );
	sym( "#JoyRoll%port=0",bbJoyRoll );
	sym( "%JoyHat%port=0",bbJoyHat );
	sym( "%JoyXDir%port=0",bbJoyXDir );
	sym( "%JoyYDir%port=0",bbJoyYDir );
	sym( "%JoyZDir%port=0",bbJoyZDir );
	sym( "%JoyUDir%port=0",bbJoyUDir );
	sym( "%JoyVDir%port=0",bbJoyVDir );
	sym( "FlushJoy",bbFlushJoy );
}
