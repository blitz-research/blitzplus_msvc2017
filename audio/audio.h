
#ifndef AUDIO_H
#define AUDIO_H

#include "../app/app.h"

class BBSound;
class BBMusic;

enum{
	BBAUDIO_PLAY_LOOP=1,
	BBAUDIO_PLAY_PAUSE=2
};

BBSound*bbLoadSound( BBString *file );
void	bbFreeSound( BBSound *sound );

void	bbLoopSound( BBSound *sound );
void	bbSoundPitch( BBSound *sound,int pitch );
void	bbSoundVolume( BBSound *sound,float volume );
void	bbSoundPan( BBSound *sound,float pan );

int		bbPlayMusic( BBString *file,int flags );
int		bbPlayCDTrack( int track,int flags );
int		bbPlaySound( BBSound *sound,int flags );

void	bbStopChannel( int channel );
void	bbPauseChannel( int channel );
void	bbResumeChannel( int channel );

void	bbChannelPitch( int channel,int pitch );
void	bbChannelVolume( int channel,float volume );
void	bbChannelPan( int channel,float pan );
int		bbChannelPlaying( int channel );

class BBAudioDriver : public BBModule{
	BBMusic*	loadMusic( BBString *file );
	int			playStream( BBString *file,int flags );
	int			playMusic( BBMusic *music,int flags );
public:
	BBAudioDriver();

	bool startup();
	void shutdown();

	BBSound*	loadSound( BBString *file );
	int			playMusic( BBString *file,int flags );
};

extern BBAudioDriver bbAudioDriver;
static BBAudioDriver *_bbAudioDriver=&bbAudioDriver;

#endif