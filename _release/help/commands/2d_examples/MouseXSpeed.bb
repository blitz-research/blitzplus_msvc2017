; MouseXSpeed()/MouseYSpeed() examples
 
 ; Set graphics mode and double buffering
 Graphics 800,600,16
 SetBuffer BackBuffer()
 
 ; repeat until right mouse button is pressed
 Repeat
 Cls ; Clear screen
 Rect MouseX(),MouseY(),2,2,1 ; draw a small box where the mouse is
 ; if user hits left mouse, take note of where it is and call mousexspeed/mouseyspeed
 If MouseHit(1) Then startx=MouseXSpeed():starty=MouseYSpeed()
 
 ; When user hits right mouse button, record the difference between last call
 ; and this call to the mousey/xspeed commands.
 If MouseHit(2) Then endx=MouseXSpeed():endy=MouseYSpeed()
 Flip ; flip screen into view
 Until endx ; end the loop when we have a value for endx
 
 ; display results
 Text 0,0,"Changes in mouse coordinates: " + endx + "," + endy
 Flip ; flip changes into view
 
 ; Wait for escape
 While Not KeyHit(1)
 Wend
 