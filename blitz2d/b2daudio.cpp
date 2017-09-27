
#include "b2daudio.h"

#include "../../fmod360src/src/fmod.h"

#pragma warning(disable:4786)

#include <map>
#include <string>

using namespace std;

B2DAudioDriver b2dAudioDriver;

static bool _ok;
static FSOUND_STREAM *_streams[4096];

static B2DMusic *_musics[4096];
static map<string,B2DMusic*> music_map;

class B2DSound : public BBResource{
	FSOUND_SAMPLE *_sample;
	mutable bool defs_valid;
	int def_freq,def_vol,def_pan,def_pri;
protected:
	~B2DSound(){
		FSOUND_Sample_Free( _sample );
	}
public:
	B2DSound( FSOUND_SAMPLE *sample ):_sample(sample),defs_valid(true){
		FSOUND_Sample_GetDefaults( sample,&def_freq,&def_vol,&def_pan,&def_pri );
	}

	void setLoop( bool loop ){
		FSOUND_Sample_SetMode( _sample,loop ? FSOUND_LOOP_NORMAL : FSOUND_LOOP_OFF );
	}

	void setPitch( int hertz ){
		def_freq=hertz;
		defs_valid=false;
	}

	void setVolume( float volume ){
		def_vol=volume*255.0f;
		defs_valid=false;
	}

	void setPan( float pan ){
		def_pan=(pan+1.0f)*127.5f;
		defs_valid=false;
	}

	FSOUND_SAMPLE *sample()const{
		if( !defs_valid ){
			FSOUND_Sample_SetDefaults( _sample,def_freq,def_vol,def_pan,def_pri );
			defs_valid=true;
		}
		return _sample; 
	}

#ifdef _DEBUG
	void debug(){ _debug(this,"Sound"); }
#else
	void debug(){}
#endif
};

class B2DMusic : public BBResource{
	int _channel;
	FMUSIC_MODULE *_module;
protected:
	~B2DMusic(){
		FMUSIC_FreeSong(_module);
		_musics[_channel]=0;
	}

public:
	B2DMusic( FMUSIC_MODULE *module,int channel ):_module(module),_channel(channel){
		_musics[_channel]=this;
	}

	int channel()const{ return _channel; }

	FMUSIC_MODULE *module()const{ return _module; }

#ifdef _DEBUG
	void debug(){ _debug(this,"Music"); }
#else
	void debug(){}
#endif
};

B2DAudioDriver::B2DAudioDriver(){
	reg( "B2DAudioDriver","Audio","Native" );
}

bool B2DAudioDriver::startup(){
	if( !FSOUND_Init( 44100,256,FSOUND_INIT_USEDEFAULTMIDISYNTH ) ){
		return _ok=false;
	}

	music_map.clear();
	memset( _musics,0,sizeof(_musics) );
	memset( _streams,0,sizeof(_streams) );

	return _ok=true;
}

void B2DAudioDriver::shutdown(){
	FSOUND_Close();
}

B2DSound* B2DAudioDriver::loadSound( BBString *file ){
	if( !_ok ) return 0;

	int mode=0;
	FSOUND_SAMPLE *sample=FSOUND_Sample_Load( FSOUND_FREE,file->c_str(),mode,0 );

	if( !sample ) return 0;

	B2DSound *sound=new B2DSound(sample);
	autoRelease(sound);
	return sound;
}

B2DMusic* B2DAudioDriver::loadMusic( BBString *file ){
	if( !_ok ) return 0;

	int n;
	for( n=0;n<4096 && _musics[n];++n ){}
	if( n==4096 ) return 0;

	FMUSIC_MODULE *module=FMUSIC_LoadSong( file->c_str() );
	file;if( !module ) return 0;

	FMUSIC_SetLooping( module,0 );
	B2DMusic *music=new B2DMusic( module,n );
	autoRelease(music);
	return music;
}

int B2DAudioDriver::playMusic( B2DMusic *music,int flags ){
	if( !music ) return 0;

	music->debug();

	FMUSIC_StopSong( music->module() );
	FMUSIC_SetLooping( music->module(),!!(flags&B2DAUDIO_PLAY_LOOP) );
	if( !FMUSIC_PlaySong( music->module() ) ) return 0;

	if( flags&B2DAUDIO_PLAY_PAUSE ) FMUSIC_SetPaused( music->module(),true );

	return music->channel()|0x80000000;
}

int B2DAudioDriver::playStream( BBString *file,int flags ){
	if( !_ok ) return 0;

	FSOUND_STREAM *stream=FSOUND_Stream_OpenFile( file->c_str(),0,0 );
	if( !stream ) return 0;

	bool paused=!!(flags&B2DAUDIO_PLAY_PAUSE);

	int channel=FSOUND_Stream_PlayEx( FSOUND_FREE,stream,0,paused );
	if( channel<0 ){
		FSOUND_Stream_Close(stream);
		return 0;
	}
	if( FSOUND_STREAM *t_stream=_streams[channel&4095] ){
		FSOUND_Stream_Close(t_stream);
	}
	_streams[channel&4095]=stream;
	return channel;
}

int B2DAudioDriver::playMusic( BBString *file,int flags ){
	if( !_ok ) return 0;

	string t=file->c_str();
	for( int k=0;k<file->size();++k ) t[k]=tolower(t[k]);

	if( t.find(".mod")!=string::npos ||
		t.find(".s3m")!=string::npos ||
		t.find(".xm" )!=string::npos ||
		t.find(".it" )!=string::npos ||
		t.find(".mid")!=string::npos ||
		t.find(".rmi")!=string::npos ||
		t.find(".sgt")!=string::npos ){

		//its a song!
		map<string,B2DMusic*>::iterator it=music_map.find(t);
		if( it!=music_map.end() ) return playMusic( it->second,flags );

		if( B2DMusic *music=loadMusic( file ) ){
			music_map.insert( make_pair(t,music) );
			return playMusic( music,flags );
		}
		return 0;
	}
	return playStream( file,flags );
}

B2DSound* b2dLoadSound( BBString *file ){
	return b2dAudioDriver.loadSound( file );
}

void	 b2dFreeSound( B2DSound *sound ){
	if( !sound ) return;
	sound->debug();
	sound->release();
}

void	 b2dLoopSound( B2DSound *sound ){
	if( !sound ) return;
	sound->debug();
	sound->setLoop(true);
}

void	 b2dSoundPitch( B2DSound *sound,int pitch ){
	if( !sound ) return;
	sound->debug();
	sound->setPitch(pitch);
}

void	 b2dSoundVolume( B2DSound *sound,float volume ){
	if( !sound ) return;
	sound->debug();
	sound->setVolume(volume);
}

void	 b2dSoundPan( B2DSound *sound,float pan ){
	if( !sound ) return;
	sound->debug();
	sound->setPan(pan);
}

int		 b2dPlayMusic( BBString *file,int flags ){
	return b2dAudioDriver.playMusic( file,flags );
}

void	 b2dFreeMusic( B2DMusic *music ){
	if( !music ) return;
	music->debug();
	music->release();
}

int		 b2dPlaySound( B2DSound *sound,int flags ){
	if( !sound ) return 0;
	sound->debug();

	bool paused=!!(flags&B2DAUDIO_PLAY_PAUSE);

	if( flags&B2DAUDIO_PLAY_LOOP ){
		FSOUND_Sample_SetMode( sound->sample(),FSOUND_LOOP_NORMAL );
	}

	int channel=FSOUND_PlaySoundEx( FSOUND_FREE,sound->sample(),0,paused );
	if( channel<0 ) return 0;

	if( FSOUND_STREAM *stream=_streams[channel&4095] ){
		FSOUND_Stream_Close(stream);
		_streams[channel&4095]=0;
	}

	return channel;
}

int		 b2dPlayCDTrack( int track,int flags ){
	return -1;
}

void	 b2dStopChannel( int channel ){
	if( !_ok ) return;

	if( channel>=0 ){
		FSOUND_StopSound( channel );
		if( FSOUND_STREAM *stream=_streams[channel&4095] ){
			FSOUND_Stream_Close(stream);
			_streams[channel&4095]=0;
		}
	}else if( B2DMusic *music=_musics[channel&0xfff] ){
		FMUSIC_StopSong( music->module() );
	}
}

void	 b2dPauseChannel( int channel ){
	if( !_ok ) return;

	if( channel>=0 ){
		FSOUND_SetPaused( channel,1 );
	}else if( B2DMusic *music=_musics[channel&0xfff] ){
		FMUSIC_SetPaused( music->module(),true );
	}
}

void	 b2dResumeChannel( int channel ){
	if( !_ok ) return;

	if( channel>=0 ){
		FSOUND_SetPaused( channel,0 );
	}else if( B2DMusic *music=_musics[channel&0xfff] ){
		FMUSIC_SetPaused( music->module(),false );
	}
}

void	 b2dChannelPitch( int channel,int pitch ){
	if( !_ok ) return;

	if( channel>=0 ){
		FSOUND_SetFrequency( channel,pitch );
	}else if( B2DMusic *music=_musics[channel&0xfff] ){
	}
}

void	 b2dChannelVolume( int channel,float volume ){
	if( !_ok ) return;

	if( channel>=0 ){
		FSOUND_SetVolume( channel,volume*255.0f );
	}else if( B2DMusic *music=_musics[channel&0xfff] ){
		FMUSIC_SetMasterVolume( music->module(),volume*256.0f );
	}
}

void	 b2dChannelPan( int channel,float pan ){
	if( !_ok ) return;

	if( channel>=0 ){
		FSOUND_SetPan( channel,(pan+1)*127.5f );
	}else if( B2DMusic *music=_musics[channel&0xfff] ){
	}
}

int		 b2dChannelPlaying( int channel ){
	if( !_ok ) return 0;

	if( channel>=0 ){
		return FSOUND_IsPlaying( channel );
	}else if( B2DMusic *music=_musics[channel&0xfff] ){
		return FMUSIC_IsPlaying( music->module() );
	}
	return 0;
}
