
#ifndef JOYSTICK_H
#define JOYSTICK_H

#include "../app/app.h"

int		 bbJoyType( int port );
int		 bbJoyDown( int n,int port );
int		 bbJoyHit( int n,int port );
float	 bbJoyX( int port );
float	 bbJoyY( int port );
float	 bbJoyZ( int port );
float	 bbJoyU( int port );
float	 bbJoyV( int port );
float	 bbJoyPitch( int port );
float	 bbJoyYaw( int port );
float	 bbJoyRoll( int port );
int		 bbJoyHat( int port );
int		 bbJoyXDir( int port );
int		 bbJoyYDir( int port );
int		 bbJoyZDir( int port );
int		 bbJoyUDir( int port );
int		 bbJoyVDir( int port );
void	 bbFlushJoy();

#endif