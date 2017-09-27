
#ifndef B2DAUDIO_H
#define B2DAUDIO_H

#include "../app/app.h"

class B2DSound;
class B2DMusic;

enum{
	B2DAUDIO_PLAY_LOOP=1,
	B2DAUDIO_PLAY_PAUSE=2
};

B2DSound*b2dLoadSound( BBString *file );
void	 b2dFreeSound( B2DSound *sound );

void	 b2dLoopSound( B2DSound *sound );
void	 b2dSoundPitch( B2DSound *sound,int pitch );
void	 b2dSoundVolume( B2DSound *sound,float volume );
void	 b2dSoundPan( B2DSound *sound,float pan );

int		 b2dPlayMusic( BBString *file,int flags );
int		 b2dPlayCDTrack( int track,int flags );
int		 b2dPlaySound( B2DSound *sound,int flags );

void	 b2dStopChannel( int channel );
void	 b2dPauseChannel( int channel );
void	 b2dResumeChannel( int channel );

void	 b2dChannelPitch( int channel,int pitch );
void	 b2dChannelVolume( int channel,float volume );
void	 b2dChannelPan( int channel,float pan );
int		 b2dChannelPlaying( int channel );

class B2DAudioDriver : public BBModule{
	B2DMusic*	loadMusic( BBString *file );
	int			playStream( BBString *file,int flags );
	int			playMusic( B2DMusic *music,int flags );
public:
	B2DAudioDriver();

	bool startup();
	void shutdown();

	B2DSound*	loadSound( BBString *file );
	int			playMusic( BBString *file,int flags );
};

extern B2DAudioDriver b2dAudioDriver;
static B2DAudioDriver *_b2dAudioDriver=&b2dAudioDriver;

#endif
