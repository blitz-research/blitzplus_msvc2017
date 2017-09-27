
#ifndef WIN32ICONSTRIP_H
#define WIN32ICONSTRIP_H
#define WIN32_LEAN_AND_MEAN

#include "../gui/iconstrip.h"

#include "../graphics/graphics.h"

#include <vector>

#include <windows.h>
#include <commctrl.h>

class Win32IconStrip : public BBIconStrip{
	int _n,_w,_h;
	std::vector<bool> _blanks;
	HIMAGELIST _imagelist;
protected:
	~Win32IconStrip();
public:
	Win32IconStrip( BBGraphics *graphics );

	int		icons();
	int		iconWidth();
	int		iconHeight();
	bool	iconIsBlank( int i );

	HIMAGELIST himagelist()const{ return _imagelist; }
};

#endif