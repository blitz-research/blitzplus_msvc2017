; Bomb Trail by Simon Harrison (si@si-design.co.uk)
;verified 1.48 4/18/2001

; Set display mode values
Const width=640,height=480

; Set display mode
Graphics 640,480

; Draw to back buffer
SetBuffer BackBuffer()

; Load bomb image
bomb=LoadImage("bomb.bmp")

; Load spark anim image
spark=LoadAnimImage("spark.bmp",32,32,0,3)

; Mask bomb and spark images so that pink background colour is invisible
MaskImage bomb,255,0,255
MaskImage spark,255,0,255

; Load boom sound
Global boom=LoadSound("boom.wav")

; Create fuse custom type
Type fuse
Field x#,y#
End Type

; Set initial countdown value to 10 seconds
countdown=10

; Set initial old mouse values
oldmousex=1
oldmousey=1

; Get current timer value - soon to be old...
oldmilli=MilliSecs()

; Repeat following loop until escape key is pressed
While Not KeyDown(1)

; Clear screen
Cls

; If mouse position isn't the same as last loop (and so is moving), create new fuse object at same position as mouse
If oldmousex<>MouseX() Or oldmousey<>MouseY()
a.fuse=New fuse 
a\x#=MouseX()
a\y#=MouseY()
EndIf

; Get current mouse position values - soon to be old
oldmousex=MouseX()
oldmousey=MouseY()

; Draw each spot of fuse to the screen
For a=Each fuse
Color 255,255,255
Plot a\x#,a\y#
Next

; If lit flag=0...
If lit=0

; If 2000 millisecs (2 secs) have passed since last getting timer value, then deduct 1 sec off timer. Update timer value.
If MilliSecs()-oldmilli>1000 Then countdown=countdown-1 : oldmilli=MilliSecs()

EndIf

; If countdown=0 then set lit flag to 1...
If countdown=0 Then lit=1

; If lit flag=1...
If lit=1

; Get the first fuse object in the list (the oldest one)
a=First fuse

; Draw spark anim image to screen using current frame value
DrawImage spark,a\x#-16,a\y#-16,frame

; Increase frame value by 1, if frame value exceeds maximum number of frames then reset frame value to 0
frame=frame+1 : If frame=3 Then frame=0

; If the first object exists then delete it
If a<>Null Then Delete a

EndIf

; Get the last fuse object in the list (the newest one)
a=Last fuse

; If the last object exists...
If a<>Null 

; Draw bomb image to screen
DrawImage bomb,a\x#,a\y#

; Print countdown text to screen just above bomb
Color 255,255,255
Text a\x#+24,a\y#-16,countdown,1

EndIf

; If coundown reaches 0...
If countdown=0

; Get first object in list
a=First fuse

; If first object in list doesn't exist (i.e. list is empty) then call explode function
If a=Null Then Explode()

EndIf

; Flip screen buffers
Flip

Wend

; *************************************************************************************************************************

; Explode function
Function Explode()

; Play boom sound
PlaySound boom

; Repeat the following loop until the fade colour values have finally gone from black to yellow to white to black
Repeat

; Increase yellow colour value
yellow=yellow+10

; If yellow colour value exceeds maximum (255), then start increasing white colour value
If yellow>255 Then yellow=255 : white=white+10

; If white colour value exceeds maximum, then start increasing black colour value
If white>255 Then black=black+10 : white=255

; Set clear screen colour
ClsColor yellow-black,yellow-black,white-black

; Clear screen
Cls

; Flip screen bufffers
Flip

Until black>255

; End program
End

End Function

; *************************************************************************************************************************