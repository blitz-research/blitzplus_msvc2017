
#ifndef HTMLHELP_H
#define HTMLHELP_H

class Html_Help;

class HelpListener{
public:
	virtual void helpOpen( Html_Help *help,const string &file )=0;
	virtual void helpTitleChange( Html_Help *help,const string &title )=0;
};

class Html_Help : public CHtmlView{
public:
	Html_Help( HelpListener *l ):listener(l){}

	string getTitle();

DECLARE_DYNAMIC( Html_Help )
DECLARE_MESSAGE_MAP()

	afx_msg BOOL OnEraseBkgnd( CDC *dc );

private:
	virtual void OnTitleChange( LPCTSTR t );
	virtual void OnBeforeNavigate2( LPCTSTR lpszURL, DWORD nFlags, LPCTSTR lpszTargetFrameName, CByteArray& baPostedData, LPCTSTR lpszHeaders, BOOL* pbCancel );

	string title;
	HelpListener *listener;
};

#endif
