
#include "win32gui.h"

#include <shlwapi.h>
#include <shlobj.h>
#include <commdlg.h>
#include <commctrl.h>

Win32Desktop *_desktop;

Win32GuiDriver win32GuiDriver;

Win32GuiDriver::Win32GuiDriver(){
	reg( "Win32GuiDriver","Gui","Native" );
}

static BBFont *_guifont;

bool Win32GuiDriver::startup(){

	startDrivers( "Screen" );

//	InitCommonControls();

	INITCOMMONCONTROLSEX icc={sizeof(icc)};

	icc.dwICC=
		ICC_WIN95_CLASSES|
		ICC_USEREX_CLASSES;

	InitCommonControlsEx( &icc );

	LoadLibrary( "riched32.dll" );

	_guifont=new Win32Font( (HFONT)GetStockObject( DEFAULT_GUI_FONT ),12 );
	autoRelease( _guifont );

	_desktop=new Win32Desktop();
	autoRelease( _desktop );

	bbSetGuiDriver(this);

	return true;
}

void Win32GuiDriver::shutdown(){
}

BBLabel *Win32GuiDriver::createLabel( BBGroup *group,int style ){
	BBLabel *t=new Win32Label(group,style);
	t->setFont(_guifont);
	return t;
}

BBButton *Win32GuiDriver::createButton( BBGroup *group,int style ){
	BBButton *t=new Win32Button(group,style);
	t->setFont(_guifont);
	return t;
}

BBTextField *Win32GuiDriver::createTextField( BBGroup *group,int style ){
	BBTextField *t=new Win32TextField(group,style);
	t->setFont(_guifont);
	return t;
}

BBSlider *Win32GuiDriver::createSlider( BBGroup *group,int style ){
	BBSlider *t=new Win32Slider(group,style);
	t->setFont(_guifont);
	return t;
}

BBProgBar *Win32GuiDriver::createProgBar( BBGroup *group,int style ){
	BBProgBar *t=new Win32ProgBar(group,style);
	t->setFont(_guifont);
	return t;
}

BBComboBox *Win32GuiDriver::createComboBox( BBGroup *group,int style ){
	BBComboBox *t=new Win32ComboBox(group,style);
	t->setFont(_guifont);
	return t;
}

BBListBox *Win32GuiDriver::createListBox( BBGroup *group,int style ){
	BBListBox *t=new Win32ListBox(group,style);
	t->setFont(_guifont);
	return t;
}

BBTreeView *Win32GuiDriver::createTreeView( BBGroup *group,int style ){
	BBTreeView *t=new Win32TreeView(group,style);
	t->setFont(_guifont);
	return t;
}

BBTextArea *Win32GuiDriver::createTextArea( BBGroup *group,int style ){
	BBTextArea *t=new Win32TextArea(group,style);
	t->setFont(_guifont);
	return t;
}

BBToolBar *Win32GuiDriver::createToolBar( BBGroup *group,int style ){
	BBToolBar *t=new Win32ToolBar( group,style );
	t->setFont(_guifont);
	return t;
}

BBHtmlView *Win32GuiDriver::createHtmlView( BBGroup *group,int style ){
	BBHtmlView *t=new Win32HtmlView( group,style );
	t->setFont(_guifont);
	return t;
}

BBWindow *Win32GuiDriver::createWindow( BBGroup *group,int style ){
	BBWindow *t=new Win32Window(group,style);
	if( !group ) autoRelease(t);
	t->setFont(_guifont);
	return t;
}

BBTabber *Win32GuiDriver::createTabber( BBGroup *group,int style ){
	BBTabber *t=new Win32Tabber(group,style);
	t->setFont(_guifont);
	return t;
}

BBPanel *Win32GuiDriver::createPanel( BBGroup *group,int style ){
	BBPanel *t=new Win32Panel(group,style);
	t->setFont(_guifont);
	return t;
}

BBSplitter *Win32GuiDriver::createSplitter( BBGroup *group,int style ){
	BBSplitter *t=new Win32Splitter(group,style);
	t->setFont(_guifont);
	return t;
}

BBDesktop *Win32GuiDriver::desktop(){
	return _desktop;
}

BBIconStrip *Win32GuiDriver::createIconStrip( BBGraphics *graphics ){
	BBIconStrip *t=new Win32IconStrip( graphics );
	autoRelease(t);
	return t;
}

void Win32GuiDriver::notify( BBString *msg,bool serious ){
	int flags=(serious?MB_ICONWARNING:MB_ICONINFORMATION)|MB_OK|MB_APPLMODAL|MB_TOPMOST;
	HWND hwnd=GetFocus();
	MessageBox( GetActiveWindow(),msg->c_str(),bbAppTitle()->c_str(),flags );
	SetFocus(hwnd);
}

bool Win32GuiDriver::confirm( BBString *msg,bool serious ){
	int flags=(serious?MB_ICONWARNING:MB_ICONINFORMATION)|MB_OKCANCEL|MB_APPLMODAL|MB_TOPMOST;
	HWND hw=GetFocus();
	int n=MessageBox( GetActiveWindow(),msg->c_str(),bbAppTitle()->c_str(),flags );
	SetFocus(hw);
	return n==IDOK;
}

int Win32GuiDriver::proceed( BBString *msg,bool serious ){
	int flags=(serious?MB_ICONWARNING:MB_ICONINFORMATION)|MB_YESNOCANCEL|MB_APPLMODAL|MB_TOPMOST;
	HWND hw=GetFocus();
	int n=MessageBox( GetActiveWindow(),msg->c_str(),bbAppTitle()->c_str(),flags );
	SetFocus(hw);
	if( n==IDYES ) return 1;
	if( n==IDNO ) return 0;
	return -1;
}

BBFont *Win32GuiDriver::requestFont(){

	LOGFONT lf={0};
	CHOOSEFONT cf={sizeof(cf)};

	cf.hwndOwner=GetActiveWindow();
	cf.lpLogFont=&lf;
	cf.Flags=CF_BOTH;

	HWND hw=GetFocus();
	int n=ChooseFont( &cf );
	SetFocus(hw);
	if( !n ) return 0;

	HFONT hfont=CreateFontIndirect( &lf );
	if( !hfont ) return 0;

	Win32Font *f=new Win32Font( hfont , lf.lfHeight);
	autoRelease(f);
	return f;
}

bool Win32GuiDriver::requestColor( unsigned *rgb ){
	static unsigned long cust[16];

	CHOOSECOLOR cc={sizeof(cc)};
	cc.hwndOwner=GetActiveWindow();
	cc.rgbResult=((*rgb>>16) & 0xff)|(*rgb & 0xff00)|((*rgb<<16)&0xff0000);
	cc.lpCustColors=cust;
	cc.Flags=CC_RGBINIT|CC_FULLOPEN|CC_ANYCOLOR;

	HWND hw=GetFocus();
	int n=ChooseColor( &cc );
	SetFocus(hw);

	if( !n ) return false;

	unsigned res=cc.rgbResult;
	*rgb=((res>>16) & 0xff)|(res & 0xff00)|((res<<16)&0xff0000);
	return true;
}

int CALLBACK BrowseForFolderCallback(HWND hwnd,UINT uMsg,LPARAM lp, LPARAM pData)
{
	char szPath[MAX_PATH];

	switch(uMsg)
	{
		case BFFM_INITIALIZED:
			SendMessage(hwnd, BFFM_SETSELECTION, TRUE, pData);
			break;

		case BFFM_SELCHANGED: 
			if (SHGetPathFromIDList((LPITEMIDLIST) lp ,szPath)) 
			{
				SendMessage(hwnd, BFFM_SETSTATUSTEXT,0,(LPARAM)szPath);	

			}
			break;
	}

	return 0;
}

BBString *Win32GuiDriver::requestDir( BBString *prompt,BBString *path ){

	BROWSEINFO bi={0};
	bi.hwndOwner=GetActiveWindow();
	bi.lpszTitle=prompt->c_str();
	bi.ulFlags=BIF_RETURNONLYFSDIRS;
	
	bi.lpfn = BrowseForFolderCallback;
	bi.lParam = (LPARAM)path->c_str();

	LPMALLOC shm;
	if( SUCCEEDED(SHGetMalloc(&shm)) ){

		HWND hw=GetFocus();
		ITEMIDLIST *idlist=SHBrowseForFolder(&bi);
		SetFocus(hw);

		if( idlist ){
			static char buff[MAX_PATH];
			int n=SHGetPathFromIDList(idlist,buff);
			shm->Free(idlist);
			shm->Release();
			return n ? new BBString(buff) : 0;
		}
		shm->Release();
	}
	return 0;
}

BBString *Win32GuiDriver::requestFile( BBString *prompt,BBString *exts,bool save,BBString *defname ){

	char defext[MAX_PATH]={"*.*"};
	char filters[MAX_PATH]={0};

	char *p=filters;
	if( exts ){
		int n=0;
		BBString *comma=BBString::chr(',');
		while( n<exts->size() ){
			int i=exts->find( comma,n );
			if( i==-1 ) i=exts->size();
			int sz=i-n;
			if( sz<=0 ) break;
			BBString *t=exts->substr(n,sz);
			strcpy( p,t->c_str() );p+=strlen(p);
			strcpy( p," Files" );p+=strlen(p);*p++=0;
			strcpy( p,"*." );p+=strlen(p);
			strcpy( p,t->c_str() );p+=strlen(p);*p++=0;
			t->release();
			n=i+1;
		}
	}
	else
	{
		strcpy( p,"All files" );p+=strlen(p);*p++=0;
		strcpy( p,"*.*" );p+=strlen(p);*p++=0;
	}
	*p++=0;*p++=0;

	char file[MAX_PATH];
	strcpy(file,defname->c_str());
	p=file;while (*p) {if (*p=='/') *p='\\';p++;}
//	file[0]=0;

	OPENFILENAME of={sizeof(of)};
	of.hwndOwner=GetActiveWindow();
	of.lpstrTitle=prompt->c_str();
	of.lpstrFilter=filters;
	of.lpstrFile=file;
	of.nMaxFile=MAX_PATH;
	of.Flags=OFN_HIDEREADONLY|OFN_NOCHANGEDIR;
	if( exts && exts->size() ){
		int sz=exts->find( BBString::chr(',') );
		if( sz==-1 ) sz=exts->size();
		BBString *t=exts->substr(0,sz );
		strcpy( defext,t->c_str() );
		if (!*file) {strcpy( file,"*.");strcat( file,t->c_str() );}
		t->release();
		of.lpstrDefExt=defext;
	}

	int n=0;
	HWND hw=GetFocus();
	if( save ){
		of.Flags|=OFN_OVERWRITEPROMPT;
		n=GetSaveFileName( &of );
	}else{
		of.Flags|=OFN_FILEMUSTEXIST;
		n=GetOpenFileName( &of );
	}
	SetFocus(hw);
	if( !n ) return 0;

	return new BBString( file );
}

HBITMAP Win32GuiDriver::createBitmap( BBGraphics *graphics ){

	int w=graphics->width(),h=graphics->height();

	HBITMAP bm=CreateCompatibleBitmap( GetDC(0),w,h );

	if( !bm ){
		bbError( "CreateCompatibleBitmap failed" );
	}

	BBPixmap *tmp=new BBPixmap( w,h,BB_RGB888,4 );
	tmp->blit( 0,0,w,h,graphics,0,0,0 );

	BITMAPINFOHEADER bi={sizeof(bi)};
	bi.biWidth=w;
	bi.biHeight=-h;
	bi.biPlanes=1;
	bi.biBitCount=24;
	bi.biCompression=BI_RGB;

	void *src;
	int src_pitch,src_fmt;
	tmp->lock( &src,&src_pitch,&src_fmt );

	if( !SetDIBits( GetDC(0),bm,0,h,src,(BITMAPINFO*)&bi,DIB_RGB_COLORS ) ){
		bbError( "SetDIBits failed" );
	}

	tmp->unlock();
	tmp->release();

	return bm;
}

class widget : public BBGadget,public Win32WndProc{
	Win32Gadget		_gadget;
	LRESULT			(*callback)(HWND,UINT,WPARAM,LPARAM,WNDPROC);
public:
	widget( BBGroup *group,int style, HWND hwnd , void *handler):BBGadget(group,style)
	{
		HWND parent=(HWND)group->query( BBQID_WIN32CLIENTHWND );
		callback=(LRESULT(*)(HWND,UINT,WPARAM,LPARAM,WNDPROC))handler;
		_gadget.setHwnd( hwnd );
		_gadget.setWndProc( this );
	}

	void *query( int qid )
	{
		if( void *p=_gadget.query( qid ) ) return p;
		return BBGadget::query( qid );
	}

	LRESULT wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc )
	{
		int		res;
		if (callback)
		{
			res=callback(hwnd,msg,wp,lp,proc);
			if (res==0) return 0;
		}
		return CallWindowProc( proc,hwnd,msg,wp,lp );
	}

	void setFont( BBFont *font ){}
	void setText( BBString *text ){}
	void setShape( int x,int y,int w,int h ){}
	void setVisible( bool visible ){}
	void setEnabled( bool enabled ){}
	void activate(){}
};

struct widgetfactory
{
	char	buffer[256];

	widgetfactory()
	{
		sprintf(buffer,"simon=%d",(int)this);
		putenv(buffer);
	}

	widget *create(int bbgroup,int style,HWND hwnd,LRESULT(*handler)(HWND,UINT,WPARAM,LPARAM,WNDPROC))
	{
		BBGroup		*group;
		widget		*w;
		w=new widget(group,style,hwnd,handler);
		return w;
	}

	void emit(widget *gad,int eventid,int eventdata,int x,int y,int z)
	{
		gad->debug();
		gad->emit(eventid,eventdata,x,y,z);
	}
};

widgetfactory simon;


/*

int Win32GuiDriver::requestSimon( ){
	return 0;
}
  
PRINTDLG	prtdlg;

void fcsPrintBlit(FocusPrint& fp,const Cord& d,Bitmap* bm,const Cord& s,bool frm)
{
	PRINTDLG		*ppd;
	canvas			*dest;
	int				res;

	dest=(canvas*)bm->nitrocanvas;
	if (dest==0) return;
	res=nitro->printcanvas(dest,ppd,s.x,s.y,s.w,s.h,d.x,d.y,d.w,d.h);
	if (res) reportn("fcsPrintBlit: nitro->printcanvas() failed");
	if (frm) fcsPrintFrame(fp,d);
}


bool Win32GuiDriver::printBuffer( BBGraphics *graphics )
{
	PRINTDLG	*printdlg;
	HDC			hdc;
	BITMAPINFO	bminfo;
	int			res;
	int			width,height,size;
	void		*bits;

// setup printer dialog debug test graphics param...

	prtdlg.Flags=0;
	prtdlg.lStructSize=sizeof(dlg);
	prtdlg.hwndOwner=0;

	ppd=(PRINTDLG*)fp.pd;
	if (!ppd || !bm || !fp.ready_to_print) return;

	width=graphics->width();
	height=graphics->width();

	if (width==0 || height==0 || printer==0) return -1;

	if (sw==0||sh==0) {sx=0;sy=0;sw=can->width;sh=can->height;}
	
	printdlg=(PRINTDLG*)printer;
	hdc=printdlg->hDC;
	if (hdc==0) return -1;
	
	width=(sw+3)&0xfffc;
	height=sh;
	size=width*height*3;

	bminfo.bmiHeader.biSize=sizeof(bminfo);
	bminfo.bmiHeader.biWidth=width;
	bminfo.bmiHeader.biHeight=-height;
	bminfo.bmiHeader.biPlanes=1;
	bminfo.bmiHeader.biBitCount=24;
	bminfo.bmiHeader.biCompression=BI_RGB;
	bminfo.bmiHeader.biSizeImage=size;
	bminfo.bmiHeader.biXPelsPerMeter=100;
	bminfo.bmiHeader.biYPelsPerMeter=100;
	bminfo.bmiHeader.biClrUsed=0;
	bminfo.bmiHeader.biClrImportant=0;
	
	bits=new char[size];
	if (bits==0) return -1;

	nitro->lock(can,READLOCK);
	can->readpixels(sx,sy,sw,sh,bits,RGB888,width*3);
	nitro->release();
	
	res=StretchDIBits(hdc,dx,dy,dw,dh,0,0,sw,sh,bits,&bminfo,DIB_RGB_COLORS,SRCCOPY);
	delete bits;
	return res==GDI_ERROR;
}
*/

//		int xstyle=0;
//		int wstyle=WS_CHILD;
//		HWND hwnd=CreateWindowEx( xstyle,"BUTTON","",wstyle,0,0,0,0,parent,0,GetModuleHandle(0),0 );
//	int  state();
//	void setState( int state );


//	IDirect3D8	*(__stdcall *dx8create)(u32);	//dll interface
//	dx8create=(IDirect3D8* (__stdcall *)(u32))GetProcAddress(dxinst,"Direct3DCreate8");



/*
// from BBEventSource

	void		emit( int kind,int data=0,int x=0,int y=0,int z=0 );

// from BBGadget

	void		setFont( BBFont *font );
	void		setText( BBString *text );
	void		setShape( int x,int y,int w,int h );
	void		setVisible( bool visible );
	void		setEnabled( bool enabled );
	void		activate();

	int			x(){ return _x; }
	int			y(){ return _y; }
	int			width(){ return _w; }
	int			height(){ return _h; }
	int			clientX(){ return _cx; }
	int			clientY(){ return _cy; }
	int			clientWidth(){ return _cw; }
	int			clientHeight(){ return _ch; }
	bool		visible(){ return _visible; }
	bool		enabled(){ return _enabled; }
	int			style(){ return _style; }
	BBGadget*	group(){ return _group; }
	BBScreen*	screen(){ return _screen; }
	BBString*	text(){ return _text->copy(); }
*/

