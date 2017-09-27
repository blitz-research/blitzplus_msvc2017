; WaitKey Example
;----------------

Graphics 640,480

Text 0,0,"Press any key to continue."
Flip

key=WaitKey()

Text 0,20,"The ASCII code of the key you pressed was: " + key
Text 0,40,"Now press a key to quit."
Flip

WaitKey()

End