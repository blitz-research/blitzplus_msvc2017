
#include "../blitz2d/blitz2d.h"

void b2d_link( void (*sym)( const char *t_sym,void *pc ) ){

	//driver stuff
	sym( "VWait%frames=1",bbVWait );
	sym( "%ScanLine",bbScanLine );
	sym( "%AvailVidMem",bbAvailVidMem );
	sym( "%TotalVidMem",bbTotalVidMem );

	//drivers/modes
	sym( "%CountGfxDrivers",b2dCountGfxDrivers );
	sym( "$GfxDriverName%driver",b2dGfxDriverName );
	sym( "SetGfxDriver%driver",b2dSetGfxDriver );
	sym( "%CountGfxModes",b2dCountGfxModes );
	sym( "%GfxModeWidth%mode",b2dGfxModeWidth );
	sym( "%GfxModeHeight%mode",b2dGfxModeHeight );
	sym( "%GfxModeDepth%mode",b2dGfxModeDepth );
	sym( "%GfxModeFormat%mode",b2dGfxModeFormat );
	sym( "%GfxModeExists%width%height%depth=0%format=0",b2dGfxModeExists );

	//graphics modes
	sym( "Graphics%width%height%depth=0%flags=0",b2dGraphics );
	sym( "EndGraphics",b2dEndGraphics );
	sym( "%BackBuffer",b2dBackBuffer );
	sym( "%FrontBuffer",b2dBackBuffer );
	sym( "%SetBuffer%buffer",b2dSetBuffer );
	sym( "%GraphicsBuffer",b2dGraphicsBuffer );
	sym( "%DesktopBuffer",b2dDesktopBuffer );
	sym( "%LoadBuffer%buffer$file",b2dLoadBuffer );
	sym( "%SaveBuffer%buffer$file",b2dSaveBuffer );
	sym( "%GraphicsWidth",b2dGraphicsWidth );
	sym( "%GraphicsHeight",b2dGraphicsHeight );
	sym( "%GraphicsDepth",b2dGraphicsDepth );
	sym( "%GraphicsFormat",b2dGraphicsFormat );
	sym( "Flip%vwait=1",b2dFlip );

	//locking stuff
	sym( "LockBuffer%buffer=0",b2dLockBuffer );
	sym( "UnlockBuffer%buffer=0",b2dUnlockBuffer );
	sym( "%LockedPitch%buffer=0",b2dLockedPitch );
	sym( "%LockedFormat%buffer=0",b2dLockedFormat );
	sym( "%LockedPixels%buffer=0",b2dLockedPixels );

	sym( "%ReadPixel%x%y%buffer=0",b2dReadPixel );
	sym( "%ReadPixelFast%x%y%buffer=0",b2dReadPixelFast );
	sym( "WritePixel%x%y%color%buffer=0",b2dWritePixel );
	sym( "WritePixelFast%x%y%color%buffer=0",b2dWritePixelFast );
	sym( "CopyPixel%src_x%src_y%src_buffer%dst_x%dst_y%dst_buffer=0",b2dCopyPixel );
	sym( "CopyPixelFast%src_x%src_y%src_buffer%dst_x%dst_y%dst_buffer=0",b2dCopyPixelFast );

	sym( "CopyRect%source_x%source_y%width%height%dest_x%dest_y%source_buffer=0%dest_buffer=0",b2dCopyRect );

	//canvas stuff
	sym( "%CreateCanvas%x%y%width%height%group%style=0",b2dCreateCanvas );
	sym( "%CanvasBuffer%canvas",b2dCanvasBuffer );
	sym( "FlipCanvas%canvas%vwait=0",b2dFlipCanvas );

	//settings
	sym( "Color%red%green%blue",b2dColor );
	sym( "ClsColor%red%green%blue",b2dClsColor );
	sym( "SetFont%font",b2dSetFont );
	sym( "Origin%x%y",b2dOrigin );
	sym( "Viewport%x%y%width%height",b2dViewport );

	//primitive rendering
	sym( "Cls",b2dCls );
	sym( "Plot%x%y",b2dPlot );
	sym( "Line%x1%y1%x2%y2",b2dLine );
	sym( "Rect%x%y%width%height%solid=1",b2dRect );
	sym( "Oval%x%y%width%height%solid=1",b2dOval );
	sym( "Text%x%y$text%center_x=0%center_y=0",b2dText );
	sym( "GetColor%x%y",b2dGetColor );
	sym( "%ColorRed",b2dColorRed );
	sym( "%ColorGreen",b2dColorGreen );
	sym( "%ColorBlue",b2dColorBlue );
	sym( "%StringWidth$str",b2dStringWidth );
	sym( "%StringHeight$str",b2dStringHeight );
	sym( "%FontWidth",b2dFontWidth );
	sym( "%FontHeight",b2dFontHeight );

	sym( "%OpenMovie$file",b2dOpenMovie );
	sym( "CloseMovie%movie",b2dCloseMovie );
	sym( "%MovieWidth%movie",b2dMovieWidth );
	sym( "%MovieHeight%movie",b2dMovieHeight );
	sym( "%MoviePlaying%movie",b2dMoviePlaying );
	sym( "%DrawMovie%movie%x%y%width=-1%height=-1",b2dDrawMovie );

	//mouse stuff
	sym( "%MouseX%canvas=0",b2dMouseX );
	sym( "%MouseY%canvas=0",b2dMouseY );
	sym( "%MouseXSpeed%canvas=0",b2dMouseXSpeed );
	sym( "%MouseYSpeed%canvas=0",b2dMouseYSpeed );
	sym( "MoveMouse%x%y%canvas=0",b2dMoveMouse );
	sym( "ShowPointer%canvas=0",b2dShowPointer );
	sym( "HidePointer%canvas=0",b2dHidePointer );
	/*
	sym( "SetPointer%id%canvas=0",b2dSetPointer );
	*/

	//gamma
	sym( "SetGamma%src_red%src_green%src_blue%dest_red%dest_green%dest_blue",b2dSetGamma );
	sym( "UpdateGamma%calibrate=0",b2dUpdateGamma );
	sym( "%GammaRed%red",b2dGammaRed );
	sym( "%GammaGreen%green",b2dGammaGreen );
	sym( "%GammaBlue%blue",b2dGammaBlue );

	//image manipulation
	sym( "%LoadImage$file%flags=1",b2dLoadImage );
	sym( "%LoadAnimImage$file%width%height%first%count%flags=1",b2dLoadAnimImage );
	sym( "%CreateImage%width%height%frames=1%flags=2",b2dCreateImage );
	sym( "%CopyImage%image",b2dCopyImage );
	sym( "FreeImage%image",b2dFreeImage );
	sym( "MaskImage%image%red%green%blue",b2dMaskImage );
	sym( "HandleImage%image%handle_x%handle_y",b2dHandleImage );
	sym( "MidHandle%image",b2dMidHandle );
	sym( "AutoMidHandle%enable",b2dAutoMidHandle );
	sym( "%ImageWidth%image",b2dImageWidth );
	sym( "%ImageHeight%image",b2dImageHeight );
	sym( "%ImageXHandle%image",b2dImageXHandle );
	sym( "%ImageYHandle%image",b2dImageYHandle );
	sym( "%ImageBuffer%image%frame=0",b2dImageBuffer );
	sym( "%SaveImage%image$file%frame=0",b2dSaveImage );

	//image transformation
	sym( "TFormFilter%enable",b2dTFormFilter );
	sym( "TFormImage%image#a#b#c#d",b2dTFormImage );
	sym( "ScaleImage%image#xscale#yscale",b2dScaleImage );
	sym( "ResizeImage%image#width#height",b2dResizeImage );
	sym( "RotateImage%image#angle",b2dRotateImage );

	//image rendering
	sym( "DrawImage%image%x%y%frame=0",b2dDrawImage );
	sym( "DrawBlock%image%x%y%frame=0",b2dDrawBlock );
	sym( "TileImage%image%x=0%y=0%frame=0",b2dTileImage );
	sym( "TileBlock%image%x=0%y=0%frame=0",b2dTileBlock );
	sym( "DrawImageRect%image%x%y%rect_x%rect_y%rect_width%rect_height%frame=0",b2dDrawImageRect );
	sym( "DrawBlockRect%image%x%y%rect_x%rect_y%rect_width%rect_height%frame=0",b2dDrawBlockRect );
	sym( "GrabImage%image%x%y%frame=0",b2dGrabImage );

	//image collisions
	sym( "%RectsOverlap%x1%y1%width1%height1%x2%y2%width2%height2",b2dRectsOverlap );
	sym( "%ImagesOverlap%image1%x1%y1%image2%x2%y2",b2dImagesOverlap );
	sym( "%ImagesCollide%image1%x1%y1%frame1%image2%x2%y2%frame2",b2dImagesCollide );
	sym( "%ImageRectOverlap%image%x%y%rect_x%rect_y%rect_width%rect_height",b2dImageRectOverlap );
	sym( "%ImageRectCollide%image%x%y%frame%rect_x%rect_y%rect_width%rect_height",b2dImageRectCollide );

	//event
	sym( "%PeekEvent",b2dPeekEvent );
	sym( "%WaitEvent%timeout=-1",b2dWaitEvent );
	sym( "%EventID",b2dEventID );
	sym( "%EventData",b2dEventData );
	sym( "%EventX",b2dEventX );
	sym( "%EventY",b2dEventY );
	sym( "%EventZ",b2dEventZ );
	sym( "%EventSource",b2dEventSource );
	sym( "%KeyDown%keycode",b2dKeyDown );
	sym( "%KeyHit%keycode",b2dKeyHit );
	sym( "%GetKey",b2dGetKey );
	sym( "%WaitKey",b2dWaitKey );
	sym( "%KeyWait",b2dWaitKey );
	sym( "FlushKeys",b2dFlushKeys );
	sym( "%MouseZ",b2dMouseZ );
	sym( "%MouseZSpeed",b2dMouseZSpeed );
	sym( "%MouseDown%button",b2dMouseDown );
	sym( "%MouseHit%button",b2dMouseHit );
	sym( "%GetMouse",b2dGetMouse );
	sym( "%WaitMouse",b2dWaitMouse );
	sym( "%MouseWait",b2dWaitMouse );
	sym( "FlushMouse",b2dFlushMouse );
}

