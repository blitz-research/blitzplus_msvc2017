
#include "win32gl.h"

static HGLRC _sharedrc;

Win32GLGraphics::Win32GLGraphics( int w,int h ):BBGraphics(w,h),_glContext(0),_tex(0),_locked(0),_dirty(true){
	_dib=new Win32DIBGraphics( w,h,24 );
}

Win32GLGraphics::Win32GLGraphics( int w,int h,Win32GLContext *glContext ):BBGraphics(w,h),_glContext(glContext),_dib(0),_tex(0),_locked(0),_dirty(true){
	_glContext->retain();
}

void *Win32GLGraphics::query( int qid ){
	if( qid==BBQID_BBGLCONTEXT && _glContext ) return (BBGLContext*)_glContext;
	return BBGraphics::query( qid );
}

Win32GLGraphics::~Win32GLGraphics(){
	if( _glContext ) _glContext->release();
	if( _tex ) _tex->release();
	if( _dib ) _dib->release();
}

BBGraphics *Win32GLGraphics::createCopy( int w,int h ){
	return new Win32GLGraphics( w,h );
}

void Win32GLGraphics::setColorKey( BBColor key ){
	_dirty=true;
	BBGraphics::setColorKey( key );
}

void Win32GLGraphics::clear( int x,int y,int w,int h,BBColor color ){
	_dirty=true;
	BBGraphics::clear( x,y,w,h,color );
}

void Win32GLGraphics::write( int x,int y,int w,int h,const BBColor *colors,int pitch ){
	_dirty=true;
	BBGraphics::write( x,y,w,h,colors,pitch );
}

void Win32GLGraphics::blit( int x,int y,int w,int h,BBGraphics *src,int sx,int sy,int flags ){
	_dirty=true;
	BBGraphics::blit( x,y,w,h,src,sx,sy,flags );
}

void Win32GLGraphics::lock( void **pixels,int *pitch,int *format ){
	_dirty=true;
	if( !_locked++ && _glContext ){

		_dib=new Win32DIBGraphics( width(),height(),24 );

		bbPushGLContext( _glContext );

		glReadPixels( 0,0,width(),height(),GL_BGR_EXT,GL_UNSIGNED_BYTE,_dib->pixels() );
		bbFlipVertical( _dib );

		bbPopGLContext();
	}

	*pixels=_dib->pixels();
	*pitch= _dib->pitch();
	*format=_dib->format();
}

void Win32GLGraphics::unlock(){
	if( --_locked || !_glContext ) return;

	bbPushGLContext( _glContext );

	bbFlipVertical( _dib );
	glDrawPixels( width(),height(),GL_BGR_EXT,GL_UNSIGNED_BYTE,_dib->pixels() );

	bbPopGLContext();

	_dib->release();
}

HDC Win32GLGraphics::lockHdc(){
	_dirty=true;
	return _dib->lockHdc();
}

void Win32GLGraphics::unlockHdc(){
	_dib->unlockHdc();
}

Win32GLTex *Win32GLGraphics::tex(){

	if( _tex && !_dirty ) return _tex;

	if( _tex ) _tex->release();

	_tex=new Win32GLTex( this,0 );

	_dirty=false;

	return _tex;
}
