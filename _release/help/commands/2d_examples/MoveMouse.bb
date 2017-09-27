; MoveMouse Example
 
 Graphics 640,480
 For t = 1 To 2
 Print "Move the mouse around and push the left button."
 WaitMouse()
 Print "The mouse is currently located at: " + MouseX() + "," + MouseY() + "."
 Next
 Print "Now I'll move the mouse ..."
 MoveMouse 320,320
 Print "The mouse is NOW located at: " + MouseX() + "," + MouseY() + "."
 Print "Click mouse button to quit."
 WaitMouse()
 