
#ifndef STREAM_H
#define STREAM_H

#include "../app/app.h"

class BBStream : public BBResource{
public:
	enum{
		EOF_ERROR=-1,EOF_NOT=0,EOF_OK=1
	};

	//returns chars read
	virtual int read( char *buff,int size )=0;

	//returns chars written
	virtual int write( const char *buff,int size )=0;

	//returns chars avilable for reading
	virtual int avail()=0;

	//returns EOF status
	virtual int	eof()=0;

	void debug(){ _debug(this,"Stream"); }
};

int			bbEof( BBStream *stream );
int			bbReadAvail( BBStream *stream );
int			bbReadByte( BBStream *stream );
int			bbReadShort( BBStream *stream );
int			bbReadInt( BBStream *stream );
float		bbReadFloat( BBStream *stream );
BBString*	bbReadString( BBStream *stream );
BBString*	bbReadLine( BBStream *stream );

void		bbWriteByte( BBStream *stream,int _byte );
void		bbWriteShort( BBStream *stream,int _short );
void		bbWriteInt( BBStream *stream,int _int );
void		bbWriteFloat( BBStream *stream,float _float );
void		bbWriteString( BBStream *stream,BBString *str );
void		bbWriteLine( BBStream *stream,BBString *str );

void		bbWriteCStr( BBStream *stream,const char *str );

void		bbCopyStream( BBStream *src,BBStream *dst,int buf_sz );

#endif
