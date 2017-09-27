
#include "win32dsmovie.h"

Win32DSMovieDriver win32DSMovieDriver;

Win32DSMovie::Win32DSMovie( Win32DDGraphics *g,IMultiMediaStream *mm,IDirectDrawMediaStream *dd ):
graphics(g),mm_stream(mm),dd_stream(dd){

	dd_stream->CreateSample( graphics->graphicsSurface(),0,0,&dd_sample );

	mm_stream->SetState( STREAMSTATE_RUN );
	play=true;
}

Win32DSMovie::~Win32DSMovie(){

	mm_stream->SetState( STREAMSTATE_STOP );

	dd_sample->Release();
	dd_stream->Release();
	mm_stream->Release();

	graphics->release();
}

int  Win32DSMovie::width(){
	return graphics->width();
}

int  Win32DSMovie::height(){
	return graphics->height();
}

bool Win32DSMovie::render( BBGraphics *dst,int x,int y,int w,int h ){
	Win32DDGraphics *t=dynamic_cast<Win32DDGraphics*>(dst);
	if( !t ) return false;

	render( t,x,y,w,h );

	return true;
}

bool Win32DSMovie::playing(){
	return play;
}

void Win32DSMovie::render( Win32DDGraphics *dst,int x,int y,int w,int h ){

	if( !play ) return;

	IDirectDrawSurface *d=dst->graphicsSurface();
	if( !d ) return;

	IDirectDrawSurface *g=graphics->graphicsSurface();
	if( !g ) return;

	if( dd_sample->Update( 0,0,0,0 )!=S_OK ){
		play=false;
		return;
	}
	RECT dst_rect={x,y,x+w,y+h};

	HRESULT res=d->Blt( &dst_rect,g,0,DDBLT_WAIT,0 );

	if (res) {	//failed blit try BitBlt which will format convert
		HDC hdc;			
		if (d->GetDC(&hdc)==DD_OK)
		{
			HDC	moviedc=graphics->lockHdc();

			int sw=graphics->width();
			int sh=graphics->height();
			if (w==sw && h==sh)
			{
				BitBlt(hdc,x,y,w,h,moviedc,0,0,SRCCOPY);
			}
			else
			{
				StretchBlt(hdc,x,y,w,h,moviedc,0,0,sw,sh,SRCCOPY);
			}
			graphics->unlockHdc();
			d->ReleaseDC(hdc);
		}
	}

}

Win32DSMovieDriver::Win32DSMovieDriver(){
	reg( "Win32DSMovieDriver","Graphics","DirectDraw" );
}

bool Win32DSMovieDriver::startup(){

	startModule( "Win32DD" );

	bbSetMovieDriver( this );
	return true;
}

void Win32DSMovieDriver::shutdown(){
}

BBMovie *Win32DSMovieDriver::loadMovie( BBString *file ){

	IAMMultiMediaStream *mm_stream;

	if( CoCreateInstance(
		CLSID_AMMultiMediaStream,NULL,CLSCTX_INPROC_SERVER,
        IID_IAMMultiMediaStream,(void **)&mm_stream )!=S_OK ) return 0;

	if( mm_stream->Initialize( STREAMTYPE_READ,AMMSF_NOGRAPHTHREAD,NULL )==S_OK ){

		IMediaStream *vid_stream;

		if( mm_stream->AddMediaStream( win32DD.directDraw(),&MSPID_PrimaryVideo,0,&vid_stream )==S_OK ){

			mm_stream->AddMediaStream( NULL,&MSPID_PrimaryAudio,AMMSF_ADDDEFAULTRENDERER,0 );

			WCHAR *path=new WCHAR[ file->size()+1 ];
			MultiByteToWideChar( CP_ACP,0,file->c_str(),-1,path,sizeof(WCHAR)*(file->size()+1) );
			int n=mm_stream->OpenFile( path,0 );
			delete path;

			if( n==S_OK ){

				IDirectDrawMediaStream *dd_stream;

				vid_stream->QueryInterface( IID_IDirectDrawMediaStream,(void**)&dd_stream );

				DDSURFACEDESC desc={sizeof(desc)};
				dd_stream->GetFormat( &desc,0,0,0 );

				Win32DDGraphics *t=new Win32DDGraphics( desc.dwWidth,desc.dwHeight,Win32DDGraphics::TYPE_VIDMEM );

				if( !t->restore() ){
					bbError( "Failed to create DirectDraw graphics" );
				}

				return new Win32DSMovie( t,mm_stream,dd_stream );
			}
		}
		mm_stream->Release();
	}
	return 0;
}