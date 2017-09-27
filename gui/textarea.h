
#ifndef TEXTAREA_H
#define TEXTAREA_H

#include "gadget.h"

class BBTextArea : public BBGadget{
public:
	enum{
		UNITS_CHARS=1,
		UNITS_LINES=2
	};

	enum{
		FORMAT_BOLD=1,
		FORMAT_ITALIC=2
	};

	BBTextArea( BBGroup *group,int style );

	virtual void	setTabs( int tabs )=0;
	virtual void	setTextColor( int r,int g,int b )=0;
	virtual void	setBackgroundColor( int r,int g,int b )=0;
	
	virtual int		length( int units )=0;
	virtual int		selLength( int units )=0;
	virtual int		cursor( int units )=0;

	virtual int		findLine( int pos )=0;
	virtual int		findChar( int lin )=0;

	virtual void	addText( BBString *text )=0;
	virtual void	setText( BBString *text,int pos,int len,int units )=0;

	virtual void	formatText( int r,int g,int b,int flags,int pos,int len,int units )=0;

	virtual BBString*getText( int pos,int len,int units )=0;

	virtual void	lock()=0;
	virtual void	unlock()=0;

	virtual int		lineLength( int line );

	void debug(){ _debug(this,"TextArea Gadget"); }
};

void		bbSetTextAreaTabs( BBTextArea *t,int tabs );
void		bbSetTextAreaFont( BBTextArea *t,BBFont *f );
void		bbSetTextAreaColor( BBTextArea *t,int r,int g,int b,bool bg );

int			bbTextAreaLen( BBTextArea *t,int units );
int			bbTextAreaSelLen( BBTextArea *t,int units );
int			bbTextAreaCursor( BBTextArea *t,int units );

int			bbTextAreaChar( BBTextArea *t,int lin );
int			bbTextAreaLine( BBTextArea *t,int chr );

void		bbAddTextAreaText( BBTextArea *t,BBString *text );
void		bbSetTextAreaText( BBTextArea *t,BBString *text,int pos,int len,int units );

void		bbFormatTextAreaText( BBTextArea *t,int r,int g,int b,int flags,int pos,int len,int units );

BBString*	bbTextAreaText( BBTextArea *t,int pos,int len,int units );

int			bbTextAreaLineLen( BBTextArea *t,int line );

void		bbLockTextArea( BBTextArea *t );
void		bbUnlockTextArea( BBTextArea *t );

#endif