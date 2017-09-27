
// http://www.codeproject.com/dialog/win32_dialoghelpers.asp

//#import "e:\windows\system32\shdocvw.dll"		// web browser control

#include <comdef.h>

#include <Mshtml.h>

struct __declspec(uuid("eab22ac2-30c1-11cf-a7eb-0000c05bae0b")) DWebBrowserEvents;

typedef DWebBrowserEvents* DWebBrowserEventsPtr;

#include <activscp.h>


#include <windows.h>
#include <exdisp.h>
#include <mshtmhst.h>

#include <exdispid.h>
#include <shellapi.h>

#include "win32htmlview.h"

#define TODO _asm{ int 3 };return E_NOTIMPL;

struct CNullStorage : public IStorage{
	// IUnknown
	STDMETHODIMP QueryInterface(REFIID riid,void ** ppvObject);
	STDMETHODIMP_(ULONG) AddRef(void);
	STDMETHODIMP_(ULONG) Release(void);
	// IStorage
	STDMETHODIMP CreateStream(const WCHAR * pwcsName,DWORD grfMode,DWORD reserved1,DWORD reserved2,IStream ** ppstm);
	STDMETHODIMP OpenStream(const WCHAR * pwcsName,void * reserved1,DWORD grfMode,DWORD reserved2,IStream ** ppstm);
	STDMETHODIMP CreateStorage(const WCHAR * pwcsName,DWORD grfMode,DWORD reserved1,DWORD reserved2,IStorage ** ppstg);
	STDMETHODIMP OpenStorage(const WCHAR * pwcsName,IStorage * pstgPriority,DWORD grfMode,SNB snbExclude,DWORD reserved,IStorage ** ppstg);
	STDMETHODIMP CopyTo(DWORD ciidExclude,IID const * rgiidExclude,SNB snbExclude,IStorage * pstgDest);
	STDMETHODIMP MoveElementTo(const OLECHAR * pwcsName,IStorage * pstgDest,const OLECHAR* pwcsNewName,DWORD grfFlags);
	STDMETHODIMP Commit(DWORD grfCommitFlags);
	STDMETHODIMP Revert(void);
	STDMETHODIMP EnumElements(DWORD reserved1,void * reserved2,DWORD reserved3,IEnumSTATSTG ** ppenum);
	STDMETHODIMP DestroyElement(const OLECHAR * pwcsName);
	STDMETHODIMP RenameElement(const WCHAR * pwcsOldName,const WCHAR * pwcsNewName);
	STDMETHODIMP SetElementTimes(const WCHAR * pwcsName,FILETIME const * pctime,FILETIME const * patime,FILETIME const * pmtime);
	STDMETHODIMP SetClass(REFCLSID clsid);
	STDMETHODIMP SetStateBits(DWORD grfStateBits,DWORD grfMask);
	STDMETHODIMP Stat(STATSTG * pstatstg,DWORD grfStatFlag);
};

struct CMyFrame : public IOleInPlaceFrame{

	Win32HtmlViewRep *rep;
	// webhostwnd* host;

	// IUnknown
	STDMETHODIMP QueryInterface(REFIID riid,void ** ppvObject);
	STDMETHODIMP_(ULONG) AddRef(void);
	STDMETHODIMP_(ULONG) Release(void);
	// IOleWindow
	STDMETHODIMP GetWindow(HWND FAR* lphwnd);
	STDMETHODIMP ContextSensitiveHelp(BOOL fEnterMode);
	// IOleInPlaceUIWindow
	STDMETHODIMP GetBorder(LPRECT lprectBorder);
	STDMETHODIMP RequestBorderSpace(LPCBORDERWIDTHS pborderwidths);
	STDMETHODIMP SetBorderSpace(LPCBORDERWIDTHS pborderwidths);
	STDMETHODIMP SetActiveObject(IOleInPlaceActiveObject *pActiveObject,LPCOLESTR pszObjName);
	// IOleInPlaceFrame
	STDMETHODIMP InsertMenus(HMENU hmenuShared,LPOLEMENUGROUPWIDTHS lpMenuWidths);
	STDMETHODIMP SetMenu(HMENU hmenuShared,HOLEMENU holemenu,HWND hwndActiveObject);
	STDMETHODIMP RemoveMenus(HMENU hmenuShared);
	STDMETHODIMP SetStatusText(LPCOLESTR pszStatusText);
	STDMETHODIMP EnableModeless(BOOL fEnable);
	STDMETHODIMP TranslateAccelerator(  LPMSG lpmsg,WORD wID);
};

struct CMySite : public IOleClientSite,public IOleInPlaceSite,public IDocHostUIHandler
{
	Win32HtmlViewRep *rep;

	// IUnknown
	STDMETHODIMP QueryInterface(REFIID riid,void ** ppvObject);
	STDMETHODIMP_(ULONG) AddRef(void);
	STDMETHODIMP_(ULONG) Release(void);
	// IOleClientSite
	STDMETHODIMP SaveObject();
	STDMETHODIMP GetMoniker(DWORD dwAssign,DWORD dwWhichMoniker,IMoniker ** ppmk);
	STDMETHODIMP GetContainer(LPOLECONTAINER FAR* ppContainer);
	STDMETHODIMP ShowObject();
	STDMETHODIMP OnShowWindow(BOOL fShow);
	STDMETHODIMP RequestNewObjectLayout();
	// IOleWindow
	STDMETHODIMP GetWindow(HWND FAR* lphwnd);
	STDMETHODIMP ContextSensitiveHelp(BOOL fEnterMode);
	// IOleInPlaceSite methods
	STDMETHODIMP CanInPlaceActivate();
	STDMETHODIMP OnInPlaceActivate();
	STDMETHODIMP OnUIActivate();
	STDMETHODIMP GetWindowContext(LPOLEINPLACEFRAME FAR* lplpFrame,LPOLEINPLACEUIWINDOW FAR* lplpDoc,LPRECT lprcPosRect,LPRECT lprcClipRect,LPOLEINPLACEFRAMEINFO lpFrameInfo);
	STDMETHODIMP Scroll(SIZE scrollExtent);
	STDMETHODIMP OnUIDeactivate(BOOL fUndoable);
	STDMETHODIMP OnInPlaceDeactivate();
	STDMETHODIMP DiscardUndoState();
	STDMETHODIMP DeactivateAndUndo();
	STDMETHODIMP OnPosRectChange(LPCRECT lprcPosRect);
	// idochost methods
	STDMETHODIMP ShowContextMenu( DWORD dwID,POINT __RPC_FAR *ppt,IUnknown __RPC_FAR *pcmdtReserved,IDispatch __RPC_FAR *pdispReserved) ;
	STDMETHODIMP GetHostInfo( DOCHOSTUIINFO __RPC_FAR *pInfo);
	STDMETHODIMP ShowUI( DWORD dwID,IOleInPlaceActiveObject __RPC_FAR *pActiveObject,IOleCommandTarget __RPC_FAR *pCommandTarget,IOleInPlaceFrame __RPC_FAR *pFrame,IOleInPlaceUIWindow __RPC_FAR *pDoc);
	STDMETHODIMP HideUI( void);
	STDMETHODIMP UpdateUI( void);
	STDMETHODIMP OnDocWindowActivate(  BOOL fActivate);
	STDMETHODIMP OnFrameWindowActivate(  BOOL fActivate);
	STDMETHODIMP ResizeBorder( LPCRECT prcBorder,IOleInPlaceUIWindow __RPC_FAR *pUIWindow,BOOL fRameWindow);
	STDMETHODIMP TranslateAccelerator( LPMSG lpMsg,const GUID __RPC_FAR *pguidCmdGroup,DWORD nCmdID);
	STDMETHODIMP GetOptionKeyPath( LPOLESTR __RPC_FAR *pchKey,DWORD dw);
	STDMETHODIMP GetDropTarget( IDropTarget __RPC_FAR *pDropTarget,IDropTarget __RPC_FAR *__RPC_FAR *ppDropTarget);
	STDMETHODIMP GetExternal(  IDispatch __RPC_FAR *__RPC_FAR *ppDispatch);
	STDMETHODIMP TranslateUrl(DWORD dwTranslate,OLECHAR __RPC_FAR *pchURLIn,OLECHAR __RPC_FAR *__RPC_FAR *ppchURLOut);
	STDMETHODIMP EnableModeless(  BOOL fEnable);
	STDMETHODIMP FilterDataObject( IDataObject __RPC_FAR *pDO,IDataObject __RPC_FAR *__RPC_FAR *ppDORet);
};

struct CMyContainer : public IOleContainer{
	// IUnknown
	STDMETHODIMP QueryInterface(REFIID riid,void ** ppvObject);
	STDMETHODIMP_(ULONG) AddRef(void);
	STDMETHODIMP_(ULONG) Release(void);
	// IParseDisplayName
	STDMETHODIMP ParseDisplayName(IBindCtx *pbc,LPOLESTR pszDisplayName,ULONG *pchEaten,IMoniker **ppmkOut);
	// IOleContainer
	STDMETHODIMP EnumObjects(DWORD grfFlags,IEnumUnknown **ppenum);
	STDMETHODIMP LockContainer(BOOL fLock);
};

struct DWebBrowserEventsImpl : public DWebBrowserEvents
{
	Win32HtmlViewRep *rep;

// IUnknown methods
    STDMETHOD(QueryInterface)(REFIID riid, LPVOID* ppv);
    STDMETHOD_(ULONG, AddRef)();
    STDMETHOD_(ULONG, Release)();
// IDispatch methods
	STDMETHOD(GetTypeInfoCount)(UINT* pctinfo);
	STDMETHOD(GetTypeInfo)(UINT iTInfo,LCID lcid,ITypeInfo** ppTInfo);
	STDMETHOD(GetIDsOfNames)(REFIID riid,LPOLESTR* rgszNames,UINT cNames,LCID lcid,DISPID* rgDispId);	
	STDMETHOD(Invoke)(DISPID dispIdMember,
            REFIID riid,
            LCID lcid,
            WORD wFlags,
            DISPPARAMS __RPC_FAR *pDispParams,
            VARIANT __RPC_FAR *pVarResult,
            EXCEPINFO __RPC_FAR *pExcepInfo,
            UINT __RPC_FAR *puArgErr);
// events
    HRESULT BeforeNavigate (
        _bstr_t URL,
        long Flags,
        _bstr_t TargetFrameName,
        VARIANT * PostData,
        _bstr_t Headers,
        VARIANT_BOOL * Cancel );

    HRESULT NavigateComplete ( _bstr_t URL );
    HRESULT StatusTextChange ( _bstr_t Text );
    HRESULT ProgressChange (
        long Progress,
        long ProgressMax );
    HRESULT DownloadComplete();
    HRESULT CommandStateChange (
        long Command,
        VARIANT_BOOL Enable );
    HRESULT DownloadBegin ();
    HRESULT NewWindow (
        _bstr_t URL,
        long Flags,
        _bstr_t TargetFrameName,
        VARIANT * PostData,
        _bstr_t Headers,
        VARIANT_BOOL * Processed );
    HRESULT TitleChange ( _bstr_t Text );
    HRESULT FrameBeforeNavigate (
        _bstr_t URL,
        long Flags,
        _bstr_t TargetFrameName,
        VARIANT * PostData,
        _bstr_t Headers,
        VARIANT_BOOL * Cancel );
    HRESULT FrameNavigateComplete (
        _bstr_t URL );
    HRESULT FrameNewWindow (
        _bstr_t URL,
        long Flags,
        _bstr_t TargetFrameName,
        VARIANT * PostData,
        _bstr_t Headers,
        VARIANT_BOOL * Processed );
    HRESULT Quit (
        VARIANT_BOOL * Cancel );
    HRESULT WindowMove ( );
    HRESULT WindowResize ( );
    HRESULT WindowActivate ( );
    HRESULT PropertyChange (
        _bstr_t Property );
};

BBString *createbbstring(unsigned short *s)
{
	char	*buff;
	int		i,len;
	
	len=0;
	while (len<16384 && s[len]) len++;
	if (len==0) return BBString::null();
	buff=new char[len];
	for (i=0;i<len;i++) buff[i]=(char)s[i];
	return new BBString( buff,len );
}

struct Win32HtmlViewRep{

	Win32HtmlView			*owner;

	HWND					hwnd;
	CMySite					site;
	CMyFrame				frame;
	CNullStorage			storage;
	DWebBrowserEventsImpl	eventsink;

	IOleObject					*oleObject;
	IWebBrowser2				*iBrowser;
	IOleInPlaceObject			*inPlaceObject;
	IConnectionPointContainer	*iConnection;
	IConnectionPoint			*iConnectionPoint;

	DWORD						dwCookie;
// blitz interface 

	int			viewstyle;
	BBString	*currenturl,*eventurl;
	bool		emitNavEvent;

	Win32HtmlViewRep( Win32HtmlView *view, HWND hw,int style ):hwnd(hw){

		owner=view;
		site.rep=this;
		eventsink.rep=this;
		frame.rep=this;
		
		viewstyle=style;
		emitNavEvent=!!(viewstyle & BBHtmlView::NONAVIGATE);

		currenturl=new BBString("");
		eventurl=new BBString("");

		OleCreate( CLSID_WebBrowser,IID_IOleObject,OLERENDER_DRAW,0,&site,&storage,(void**)&oleObject );

		OleSetContainedObject( oleObject,TRUE);

		oleObject->SetHostNames(L"Web Host",L"Web View");
		oleObject->QueryInterface(IID_IWebBrowser2,(void**)&iBrowser);
		oleObject->QueryInterface(IID_IOleInPlaceObject,(void**)&inPlaceObject );

		oleObject->QueryInterface(IID_IConnectionPointContainer,(void**)&iConnection);
		iConnection->FindConnectionPoint(DIID_DWebBrowserEvents, &iConnectionPoint);
		iConnectionPoint->Advise((LPUNKNOWN)&eventsink, &dwCookie);

		RECT rect;
		::GetClientRect( hwnd,&rect );
		oleObject->DoVerb(OLEIVERB_SHOW,NULL,&site,-1,hwnd,&rect);
		go( "about:blank" );
	}

	~Win32HtmlViewRep(){
		inPlaceObject->Release();
		iBrowser->Release();
		oleObject->Close(OLECLOSE_NOSAVE);
		oleObject->Release();
	}

	void setcurrenturl(unsigned short *url)
	{
		currenturl->release();
		currenturl=createbbstring(url);
	}

	void seteventurl(unsigned short *url)
	{
		eventurl->release();
		eventurl=createbbstring(url);
	}
	
	void on_message( UINT msg,WPARAM wp,LPARAM lp ){
		if( msg==WM_SIZE ){
			RECT rect;
			GetClientRect( hwnd,&rect );
			inPlaceObject->SetObjectRects( &rect,&rect );
		}
	}
	
	void go( const char *url ){
		wchar_t *buf;
		int sz=MultiByteToWideChar( CP_ACP,0,url,-1,0,0 );
		buf=new wchar_t[sz];
		MultiByteToWideChar( CP_ACP,0,url,-1,buf,sz );
		BSTR bstr=SysAllocString(buf);
		emitNavEvent=false;
		iBrowser->Navigate( bstr,0,0,0,0 );
		SysFreeString(bstr);
		delete[] buf;
	}

	void run( const char *script ){
		IDispatch			*disp;
		IHTMLDocument2		*doc;
		IHTMLWindow2		*win;
		HRESULT				res;
		VARIANT				result;
		wchar_t *buf;
		int sz=MultiByteToWideChar( CP_ACP,0,script,-1,0,0 );
		buf=new wchar_t[sz];
		MultiByteToWideChar( CP_ACP,0,script,-1,buf,sz );
		BSTR bstr=SysAllocString(buf);
		res=iBrowser->get_Document(&disp);
		if (res==S_OK)
		{
			res=disp->QueryInterface(IID_IHTMLDocument2,(void**)&doc);
			res=doc->get_parentWindow(&win);
			result.vt=VT_EMPTY;
			res=win->execScript(bstr,0,&result);
		}
		SysFreeString(bstr);
		delete[] buf;
	}

// todo - dump source code of current document into gadget _text

	void settext(){
/*
		IDispatch			*disp;
		IHTMLDocument2		*doc;
		HRESULT				res;
		BSTR				content;
		BBString			*bbs;

		res=iBrowser->get_Document(&disp);
		res=disp->QueryInterface(IID_IHTMLDocument2,(void**)&doc);
		res=doc->toString(&content);
		bbs=createbbstring(content);
		owner->setText(bbs);
*/
	}

	int getstate()
	{
		int		state;
		short	busy;
		state=0;
		iBrowser->get_Busy(&busy);
		if (busy) state|=1;
		return state;
	}

};

STDMETHODIMP CNullStorage::QueryInterface(REFIID riid,void ** ppvObject){
	TODO
}
STDMETHODIMP_(ULONG) CNullStorage::AddRef(void){
	return 1;
}
STDMETHODIMP_(ULONG) CNullStorage::Release(void){
	return 1;
}
STDMETHODIMP CNullStorage::CreateStream(const WCHAR * pwcsName,DWORD grfMode,DWORD reserved1,DWORD reserved2,IStream ** ppstm){
	TODO
}
STDMETHODIMP CNullStorage::OpenStream(const WCHAR * pwcsName,void * reserved1,DWORD grfMode,DWORD reserved2,IStream ** ppstm){
	TODO
}
STDMETHODIMP CNullStorage::CreateStorage(const WCHAR * pwcsName,DWORD grfMode,DWORD reserved1,DWORD reserved2,IStorage ** ppstg){
	TODO
}
STDMETHODIMP CNullStorage::OpenStorage(const WCHAR * pwcsName,IStorage * pstgPriority,DWORD grfMode,SNB snbExclude,DWORD reserved,IStorage ** ppstg){
	TODO
}
STDMETHODIMP CNullStorage::CopyTo(DWORD ciidExclude,IID const * rgiidExclude,SNB snbExclude,IStorage * pstgDest){
	TODO
}
STDMETHODIMP CNullStorage::MoveElementTo(const OLECHAR * pwcsName,IStorage * pstgDest,const OLECHAR* pwcsNewName,DWORD grfFlags){
	TODO
}
STDMETHODIMP CNullStorage::Commit(DWORD grfCommitFlags){
	TODO
}
STDMETHODIMP CNullStorage::Revert(void){
	TODO
}
STDMETHODIMP CNullStorage::EnumElements(DWORD reserved1,void * reserved2,DWORD reserved3,IEnumSTATSTG ** ppenum){
	TODO
}
STDMETHODIMP CNullStorage::DestroyElement(const OLECHAR * pwcsName){
	TODO
}
STDMETHODIMP CNullStorage::RenameElement(const WCHAR * pwcsOldName,const WCHAR * pwcsNewName){
	TODO
}
STDMETHODIMP CNullStorage::SetElementTimes(const WCHAR * pwcsName,FILETIME const * pctime,FILETIME const * patime,FILETIME const * pmtime){
	TODO
}
STDMETHODIMP CNullStorage::SetClass(REFCLSID clsid){
	return S_OK;
}
STDMETHODIMP CNullStorage::SetStateBits(DWORD grfStateBits,DWORD grfMask){
	TODO
}
STDMETHODIMP CNullStorage::Stat(STATSTG * pstatstg,DWORD grfStatFlag){
	TODO
}
STDMETHODIMP CMySite::QueryInterface(REFIID riid,void ** ppvObject){
	if( riid == IID_IUnknown || riid == IID_IOleClientSite ){
		*ppvObject = (IOleClientSite*)this;
	}else if(riid == IID_IOleInPlaceSite){
		*ppvObject = (IOleInPlaceSite*)this;
	}else if(riid == IID_IDocHostUIHandler){
		*ppvObject = (IDocHostUIHandler*)this;
	}else{
		*ppvObject = NULL;
		return E_NOINTERFACE;
	}
	return S_OK;
}
STDMETHODIMP_(ULONG) CMySite::AddRef(void){
	return 1;
}
STDMETHODIMP_(ULONG) CMySite::Release(void){
	return 1;
}
STDMETHODIMP CMySite::SaveObject(){
	TODO
}
STDMETHODIMP CMySite::GetMoniker(DWORD dwAssign,DWORD dwWhichMoniker,IMoniker ** ppmk){
	TODO
}
STDMETHODIMP CMySite::GetContainer(LPOLECONTAINER FAR* ppContainer){
	*ppContainer = NULL;
	return E_NOINTERFACE;
}
STDMETHODIMP CMySite::ShowObject(){
	return NOERROR;
}
STDMETHODIMP CMySite::OnShowWindow(BOOL fShow){
	TODO
}
STDMETHODIMP CMySite::RequestNewObjectLayout(){
	TODO
}
STDMETHODIMP CMySite::GetWindow(HWND FAR* lphwnd){
	*lphwnd=rep->hwnd;
	return S_OK;
}
STDMETHODIMP CMySite::ContextSensitiveHelp(BOOL fEnterMode){
	TODO
}


STDMETHODIMP CMySite::CanInPlaceActivate(){
	return S_OK;
}
STDMETHODIMP CMySite::OnInPlaceActivate(){
	return S_OK;
}
STDMETHODIMP CMySite::OnUIActivate(){
	return S_OK;
}


STDMETHODIMP CMySite::GetWindowContext(
	LPOLEINPLACEFRAME FAR* ppFrame,
	LPOLEINPLACEUIWINDOW FAR* ppDoc,
	LPRECT prcPosRect,
	LPRECT prcClipRect,
	LPOLEINPLACEFRAMEINFO lpFrameInfo){

	*ppFrame=&rep->frame;
	*ppDoc = NULL;
	GetClientRect(rep->hwnd,prcPosRect);
	GetClientRect(rep->hwnd,prcClipRect);

	lpFrameInfo->fMDIApp=FALSE;
	lpFrameInfo->hwndFrame=rep->hwnd;
	lpFrameInfo->haccel=NULL;
	lpFrameInfo->cAccelEntries=0;

	return S_OK;
}
STDMETHODIMP CMySite::Scroll(SIZE scrollExtent){
	TODO
}
STDMETHODIMP CMySite::OnUIDeactivate(BOOL fUndoable){
	return S_OK;
}
STDMETHODIMP CMySite::OnInPlaceDeactivate(){
	return S_OK;
}
STDMETHODIMP CMySite::DiscardUndoState(){
	TODO
}


STDMETHODIMP CMySite::DeactivateAndUndo(){
	TODO
}
STDMETHODIMP CMySite::OnPosRectChange( const RECT *rect ){
	TODO
}

STDMETHODIMP CMySite::EnableModeless(  BOOL fEnable) {return S_OK;}	//[in]

STDMETHODIMP CMySite::ShowContextMenu( 
    /* [in] */ DWORD dwID,
    /* [in] */ POINT __RPC_FAR *ppt,
    /* [in] */ IUnknown __RPC_FAR *pcmdtReserved,
    /* [in] */ IDispatch __RPC_FAR *pdispReserved) 
{
	if (rep->viewstyle&BBHtmlView::NOCONTEXTMENU) return S_OK;
	return S_FALSE;
}

STDMETHODIMP CMySite::ShowUI( 
    /* [in] */ DWORD dwID,
    /* [in] */ IOleInPlaceActiveObject __RPC_FAR *pActiveObject,
    /* [in] */ IOleCommandTarget __RPC_FAR *pCommandTarget,
    /* [in] */ IOleInPlaceFrame __RPC_FAR *pFrame,
    /* [in] */ IOleInPlaceUIWindow __RPC_FAR *pDoc)
{
//	pCommandTarget->Exec(0,IDM_DISABLEMODELESS,OLECMDEXECOPT_DODEFAULT,0,0);
//	if (rep->style&SHOWUI) return S_FALSE;
	return S_OK;
}

STDMETHODIMP CMySite::HideUI( void) {return S_OK;}
STDMETHODIMP CMySite::UpdateUI( void) {return S_OK;}

STDMETHODIMP CMySite::OnDocWindowActivate( /* [in] */ BOOL fActivate) {return S_OK;}
STDMETHODIMP CMySite::OnFrameWindowActivate( /* [in] */ BOOL fActivate){return S_OK;}

STDMETHODIMP CMySite::ResizeBorder( 
    /* [in] */ LPCRECT prcBorder,
    /* [in] */ IOleInPlaceUIWindow __RPC_FAR *pUIWindow,
    /* [in] */ BOOL fRameWindow) {return S_OK;}

STDMETHODIMP CMySite::TranslateAccelerator( 
    /* [in] */ LPMSG lpMsg,
    /* [in] */ const GUID __RPC_FAR *pguidCmdGroup,
    /* [in] */ DWORD nCmdID) {return E_NOTIMPL;}

STDMETHODIMP CMySite::GetOptionKeyPath( 
    /* [out] */ LPOLESTR __RPC_FAR *pchKey,
    /* [in] */ DWORD dw) {return E_NOTIMPL;}

STDMETHODIMP CMySite::GetDropTarget( 
    /* [in] */ IDropTarget __RPC_FAR *pDropTarget,
    /* [out] */ IDropTarget __RPC_FAR *__RPC_FAR *ppDropTarget) {return E_NOTIMPL;}

STDMETHODIMP CMySite::GetExternal( /* [out] */ IDispatch __RPC_FAR *__RPC_FAR *ppDispatch) {return -1;}

STDMETHODIMP CMySite::TranslateUrl(
    /* [in] */ DWORD dwTranslate,
    /* [in] */ OLECHAR __RPC_FAR *pchURLIn,
    /* [out] */ OLECHAR __RPC_FAR *__RPC_FAR *ppchURLOut)
{
	return S_FALSE;
//	ppchURLOut=0;
//	return S_OK;
}

STDMETHODIMP CMySite::GetHostInfo( 
    /* [out][in] */ DOCHOSTUIINFO __RPC_FAR *pInfo)
{
//	static DOCHOSTUIINFO	pi;
//	pi.cbSize=sizeof(pi);
//	pi.dwFlags|=DOCHOSTUIFLAG_SCROLL_NO;
//	pi.dwDoubleClick=0;
//	pInfo=&pi;
//	pinfo.dwFlags|=DOCHOSTUIFLAG_SCROLL_NO;
//	return S_OK;
	return E_NOTIMPL;
}

STDMETHODIMP CMySite::FilterDataObject( 
    /* [in] */ IDataObject __RPC_FAR *pDO,
    /* [out] */ IDataObject __RPC_FAR *__RPC_FAR *ppDORet) {return S_FALSE;}


STDMETHODIMP CMyFrame::QueryInterface(REFIID riid,void ** ppvObject){
	TODO
}
STDMETHODIMP_(ULONG) CMyFrame::AddRef(void){
	return 1;
}
STDMETHODIMP_(ULONG) CMyFrame::Release(void){
	return 1;
}
STDMETHODIMP CMyFrame::GetWindow(HWND FAR* lphwnd){
	*lphwnd=rep->hwnd;
	return S_OK;
}
STDMETHODIMP CMyFrame::ContextSensitiveHelp(BOOL fEnterMode){
	TODO
}
STDMETHODIMP CMyFrame::GetBorder(LPRECT lprectBorder){
	TODO
}
STDMETHODIMP CMyFrame::RequestBorderSpace(LPCBORDERWIDTHS pborderwidths){
	TODO
}
STDMETHODIMP CMyFrame::SetBorderSpace(LPCBORDERWIDTHS pborderwidths){
	TODO
}
STDMETHODIMP CMyFrame::SetActiveObject(IOleInPlaceActiveObject *pActiveObject,LPCOLESTR pszObjName){
	return S_OK;
}
STDMETHODIMP CMyFrame::InsertMenus(HMENU hmenuShared,LPOLEMENUGROUPWIDTHS lpMenuWidths){
	TODO
}
STDMETHODIMP CMyFrame::SetMenu(HMENU hmenuShared,HOLEMENU holemenu,HWND hwndActiveObject){
	return S_OK;
}
STDMETHODIMP CMyFrame::RemoveMenus(HMENU hmenuShared){
	TODO
}
STDMETHODIMP CMyFrame::SetStatusText(LPCOLESTR pszStatusText){
	return S_OK;
}
STDMETHODIMP CMyFrame::EnableModeless(BOOL fEnable){
	return S_OK;
}
STDMETHODIMP CMyFrame::TranslateAccelerator(LPMSG lpmsg,WORD wID){
	TODO
}

ULONG __stdcall DWebBrowserEventsImpl::AddRef() { return 1;}
ULONG __stdcall DWebBrowserEventsImpl::Release() { return 0;}

HRESULT __stdcall DWebBrowserEventsImpl::QueryInterface(REFIID riid, LPVOID* ppv)
{
	*ppv = NULL;

	if (IID_IUnknown == riid || __uuidof(DWebBrowserEventsPtr) == riid)	//was PTR
	{
		*ppv = (LPUNKNOWN)(DWebBrowserEventsPtr*)this;
		AddRef();
		return NOERROR;
	}
	else if (IID_IOleClientSite == riid)
	{
		*ppv = (IOleClientSite*)this;
		AddRef();
		return NOERROR;
	}
	else if (IID_IDispatch == riid)
	{
		*ppv = (IDispatch*)this;
		AddRef();
		return NOERROR;
	}
	else
	{
		return E_NOTIMPL;
	}
}

HRESULT __stdcall DWebBrowserEventsImpl::Invoke(DISPID dispIdMember,
            REFIID riid,
            LCID lcid,
            WORD wFlags,
            DISPPARAMS __RPC_FAR *pDispParams,
            VARIANT __RPC_FAR *pVarResult,
            EXCEPINFO __RPC_FAR *pExcepInfo,
            UINT __RPC_FAR *puArgErr)
{
	switch (dispIdMember)
	{
	case DISPID_BEFORENAVIGATE:
	    BeforeNavigate(_bstr_t(pDispParams->rgvarg[5].bstrVal),0,_bstr_t(pDispParams->rgvarg[3].bstrVal),&pDispParams->rgvarg[2],_bstr_t(pDispParams->rgvarg[1]),pDispParams->rgvarg[0].pboolVal);
		break;
	case DISPID_NAVIGATECOMPLETE:
		NavigateComplete(_bstr_t(pDispParams->rgvarg[0].bstrVal));
		break;
	}
	return NOERROR;
}

// IDispatch methods
HRESULT __stdcall DWebBrowserEventsImpl::GetTypeInfoCount(UINT* pctinfo)
{ 
	return E_NOTIMPL; 
}

HRESULT __stdcall DWebBrowserEventsImpl::GetTypeInfo(UINT iTInfo,
            LCID lcid,
            ITypeInfo** ppTInfo)
{ 
	return E_NOTIMPL; 
}

HRESULT __stdcall DWebBrowserEventsImpl::GetIDsOfNames(REFIID riid,
            LPOLESTR* rgszNames,
            UINT cNames,
            LCID lcid,
            DISPID* rgDispId)
{ 
	return E_NOTIMPL; 
}

// Methods:
HRESULT DWebBrowserEventsImpl::BeforeNavigate (_bstr_t URL,long Flags,_bstr_t TargetFrameName,VARIANT * PostData,_bstr_t Headers,VARIANT_BOOL * Cancel )
{
	if( rep->emitNavEvent ){
		*Cancel=VARIANT_TRUE;
		rep->seteventurl( (unsigned short*)(wchar_t*)URL );
		PostMessage( rep->hwnd,WM_USER+100,1,0 );
	}else{
		*Cancel=VARIANT_FALSE;
		rep->emitNavEvent=!!(rep->viewstyle & BBHtmlView::NONAVIGATE);
	}
	return S_OK;
}

HRESULT DWebBrowserEventsImpl::NavigateComplete ( _bstr_t URL ) 
{
	rep->seteventurl( (unsigned short*)(wchar_t*)URL );
	rep->setcurrenturl((unsigned short*)(wchar_t*)URL);
	PostMessage( rep->hwnd,WM_USER+100,0,0 );
	return S_OK; 
}

HRESULT DWebBrowserEventsImpl::StatusTextChange ( _bstr_t Text ) { return S_OK; }
HRESULT DWebBrowserEventsImpl::ProgressChange (
    long Progress,
    long ProgressMax )  { return S_OK; }
HRESULT DWebBrowserEventsImpl::DownloadComplete()  { return S_OK; }
HRESULT DWebBrowserEventsImpl::CommandStateChange (
    long Command,
    VARIANT_BOOL Enable ) { return S_OK; }
HRESULT DWebBrowserEventsImpl::DownloadBegin () { return S_OK; }
HRESULT DWebBrowserEventsImpl::NewWindow (
    _bstr_t URL,
    long Flags,
    _bstr_t TargetFrameName,
    VARIANT * PostData,
    _bstr_t Headers,
    VARIANT_BOOL * Processed ) { return S_OK; }
HRESULT DWebBrowserEventsImpl::TitleChange ( _bstr_t Text ) { return S_OK; }
HRESULT DWebBrowserEventsImpl::FrameBeforeNavigate (
    _bstr_t URL,
    long Flags,
    _bstr_t TargetFrameName,
    VARIANT * PostData,
    _bstr_t Headers,
    VARIANT_BOOL * Cancel ) { return S_OK; }
HRESULT DWebBrowserEventsImpl::FrameNavigateComplete (
    _bstr_t URL ) { return S_OK; }
HRESULT DWebBrowserEventsImpl::FrameNewWindow (
    _bstr_t URL,
    long Flags,
    _bstr_t TargetFrameName,
    VARIANT * PostData,
    _bstr_t Headers,
    VARIANT_BOOL * Processed ) { return S_OK; }
HRESULT DWebBrowserEventsImpl::Quit (
    VARIANT_BOOL * Cancel ) { return S_OK; }
HRESULT DWebBrowserEventsImpl::WindowMove ( ) { return S_OK; }
HRESULT DWebBrowserEventsImpl::WindowResize ( ) { return S_OK; }
HRESULT DWebBrowserEventsImpl::WindowActivate ( ) { return S_OK; }
HRESULT DWebBrowserEventsImpl::PropertyChange (
    _bstr_t Property ) { return S_OK; }



// blitz interface
// simon was here

Win32HtmlView::Win32HtmlView( BBGroup *group,int style ):BBHtmlView(group,style),_rep(0){

	HWND parent=(HWND)group->query( BBQID_WIN32CLIENTHWND );

	int xstyle=0;
	int wstyle=WS_CHILD;

	HWND hwnd=CreateWindowEx( xstyle,Win32Hwnd::className(),0,wstyle,0,0,0,0,parent,0,GetModuleHandle(0),0 );

	_rep=new Win32HtmlViewRep( this,hwnd,style );

	_gadget.setHwnd(hwnd);
	_gadget.setWndProc(this);
}

Win32HtmlView::~Win32HtmlView(){
	delete _rep;
}

void *Win32HtmlView::query( int qid ){
	if( void *p=_gadget.query( qid ) ) return p;
	return BBHtmlView::query( qid );
}

void Win32HtmlView::setFont( BBFont *font ){
	_gadget.setFont(font);
	BBHtmlView::setFont(font);
}

void Win32HtmlView::setText( BBString *text ){
//	_gadget.setText(text);
	BBHtmlView::setText(text);
}

void Win32HtmlView::setShape( int x,int y,int w,int h ){
	_gadget.setShape(x,y,w,h);
	BBHtmlView::setShape(x,y,w,h); 
}

void Win32HtmlView::setVisible( bool visible ){
	_gadget.setVisible(visible);
	BBHtmlView::setVisible(visible);
}

void Win32HtmlView::setEnabled( bool enabled ){
	_gadget.setEnabled(enabled);
	BBHtmlView::setEnabled(enabled);
}

void Win32HtmlView::activate(){
	_gadget.activate();
	BBHtmlView::activate();
}

void Win32HtmlView::go( BBString *t ){
	_rep->go( t->c_str() );
}

void Win32HtmlView::run( BBString *t ){
	_rep->run( t->c_str() );
}

void Win32HtmlView::back(){
	_rep->iBrowser->GoBack();
}

void Win32HtmlView::forward(){
	_rep->iBrowser->GoForward();
}

int Win32HtmlView::getstatus()
{
	return _rep->getstate();
}

BBString *Win32HtmlView::getcurrenturl()
{
	_rep->currenturl->retain();
	return _rep->currenturl;
}

BBString *Win32HtmlView::geteventurl()
{
	_rep->eventurl->retain();
	return _rep->eventurl;
}


LRESULT Win32HtmlView::wndProc( HWND hwnd,UINT msg,WPARAM wp,LPARAM lp,WNDPROC proc ){
	_rep->on_message( msg,wp,lp );
	switch( msg ){
	case WM_ERASEBKGND:
		return 1;
	case WM_SIZE:
		return 0;
	case WM_USER+100:
		emit(BBEvent::GADGET_ACTION,wp);
		return 0;
	}
	return CallWindowProc( proc,hwnd,msg,wp,lp );
}



//		LPCONNECTIONPOINTCONTAINER pCPC = NULL;
//		LPCONNECTIONPOINT pCP = NULL;
//		iConnection->FindConnectionPoint(__uuidof(SHDocVw::DWebBrowserEventsPtr), &pCP);



/*
	IActiveScript		*script; 
	IActiveScriptParse	*parser; 
	hres=script->QueryInterface(IID_IActiveScriptParse,(void**)&parser);if(hres!=S_OK)return;
1) Get IWebBrowser2 interface - according to Q257717 "HOWTO: Retrieve theTop-Level IWebBrowser2 Interface from an
ActiveX Control".
2) Get IHTMLDocument interface, by IWebBrowser2::get_Document.
3) Get interface pointer to script engine object through
IHTMLDocument::get_Script.
4) Get function DISPID via IDispatch::GetIDsOfNames.
5) Execute function via IDispatch::Invoke.

	
	[B] - Execute function through IHTMLWindow2::execScript, ( e.g. use
IHTMLDocument::get_parentWindow to get IHTMLWindow2 interface
	*/

/*
		IHTMLDocument		*doc;
		IDispatch			*mydisp;
		IActiveScript		*ascript; 
		IActiveScriptParse	*parser;
		VARIANT				result;

		IHTMLDocument		*doc;
//		IDispatch			*disp,*sdisp;
//		IActiveScript		*ascript; 
//		IActiveScriptParse	*parser;
//		IActiveScriptSite	*site;
		HRESULT				res;
//		VARIANT				result;
//		DISPID				dispid;

		res=iBrowser->get_Document(&disp);
		res=disp->QueryInterface(IID_IHTMLDocument,(void**)&doc);
		res=doc->get_Script(&disp);

//		res=disp->GetIDsOfNames(IID_NULL,names,count,LOCALE_SYSTEM_DEFAULT,&dispid);
//		res=disp->Invoke(dispid,IID_NULL,LOCALE_SYSTEM_DEFAULT,DISPATCH_METHOD,params,&result,NULL,NULL);


//		res=sdisp->QueryInterface(IID_IActiveScriptSite,(void**)&disp);

//		res=sdisp->QueryInterface(IID_IActiveScript,(void**)&ascript);

//		res=ascript->QueryInterface(IID_IActiveScriptParse,(void**)&parser);

//		if (res==S_OK) res=parser->ParseScriptText(bstr,0,0,0,0,0,SCRIPTTEXT_ISEXPRESSION,&result,0);			//|SCRIPTTEXT_ISPERSISTENT SCRIPTTEXT_ISVISIBLE|SCRIPTTEXT_ISPERSISTENT|

//		res=disp->QueryInterface(IID_IActiveScriptSite,(void**)&ascript);
//		res=ascript->QueryInterface(IID_IActiveScriptParse,(void**)&parser);

//		res=oleObject->get_Container(&container);
//		res=oleObject->QueryInterface(IID_IActiveScript,(void**)&ascript);
//		res=ascript->QueryInterface(IID_IActiveScriptParse,(void**)&parser);

//		res=container->get_Script(&mydisp);		
//		res=ascript->QueryInterface(IID_IActiveScriptParse,(void**)&parser);
//		res=mydisp->QueryInterface(IID_IActiveScriptParse,(void**)&parser);
//		res=parser->ParseScriptText(bstr,0,0,0,0,0,SCRIPTTEXT_ISEXPRESSION,&result,0);			//|SCRIPTTEXT_ISPERSISTENT SCRIPTTEXT_ISVISIBLE|SCRIPTTEXT_ISPERSISTENT|


		char buff[256];
		sprintf(buff,"result.vt=%x",result.vt);
		owner->setText(new BBString(buff));

//		if (result.vt==VT_BSTR) owner->setText(new BBString(buff));
//		if (result.vt==(VT_BSTR|VTYPE_BYREF)) owner->setText(new BBString("script string!"));

*/


/*
  // proces OnBeforeNavigate
  if (dispIdMember == DISPID_BEFORENAVIGATE)
  {
    // call BeforeNavigate
    // (parameters are on stack, thus on reverse order)
    BeforeNavigate(  _bstr_t( pDispParams->rgvarg[5].bstrVal ),	//url
                    0,
                    _bstr_t( pDispParams->rgvarg[3].bstrVal ),
                    NULL,
                    _bstr_t(""),
                    pDispParams->rgvarg[0].pboolVal);
	pDispParams->rgvarg[0].lVal=VARIANT_TRUE;
  }
  else if (dispIdMember == DISPID_NAVIGATECOMPLETE)
  {
    NavigateComplete( _bstr_t( pDispParams->rgvarg[0].bstrVal ) );
  }
  else
  {
//     ... // implement all event handlers of interest to you
  }

*/

/* example code
SHDocVw::IWebBrowserAppPtr pWebBrowser = NULL;
HRESULT hr = m_cpParent->GetDlgControl(IDC_EXPLORER1, 
__uuidof(SHDocVw::IWebBrowserAppPtr), 
(void**)&pWebBrowser);

// get the html document
MSHTML::IHTMLDocument2Ptr doc( pWebBrowser->Document );
MSHTML::IHTMLElementPtr htmlbody( doc->body );

BSTR content = NULL;
htmlbody->get_innerHTML(&content);
_bstr_t bcontent(content);
*/

