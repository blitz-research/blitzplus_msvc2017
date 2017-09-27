; This example saves a copy of the desktop to c:\MyDesktop,bmp
;
; First of all, create an empty buffer of the same size as the desktop
; The easyiest way to determine the Desktop size is to use the Desktop()
; object, which, being a Windows element can be treated as a Gadget.
; Therefore, we get the size of the desktop using GadgetWidth and GadgetHeight.

w = GadgetWidth(Desktop())
h = GadgetHeight(Desktop())
img = CreateImage(w, h)

; Now we need to copy the desktop contents to our image

CopyRect 0,0,w,h,0,0,DesktopBuffer(),ImageBuffer(img)

; Finally we save the image

SaveImage img,"c:\MyDesktop.bmp"

; And whilst we're here, let's report a friendly confirmation message!

Notify "Desktop saved to c:\MyDesktop.bmp"
End