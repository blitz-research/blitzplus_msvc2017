
window=CreateWindow( "HtmlView",0,0,640,480 )

html=CreateHtmlView( 0,0,ClientWidth(window),ClientHeight(window),window )
SetGadgetLayout html,1,1,1,1

HtmlViewGo html,"www.blitzbasic.com"

While WaitEvent()<>$803
Wend

End