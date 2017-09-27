; Example provided by Mark Tiffany
CreateWindow("Test Window - Borderless (0)",0,0,400,100,0,0) 
CreateWindow("Test Window - Tool Window (17)",0,100,400,100,0,17) 
CreateWindow("Test Window - Basic (3)",0,200,400,100,0,3) 
CreateWindow("Test Window - With Menu (7)",0,300,400,100,0,7) 
CreateWindow("Test Window - With Status Bar (11)",0,400,400,100,0,11) 
CreateWindow("Test Window - Default (15)",0,500,400,100,0)

; wait until the user closes one of the windows
Repeat
	If WaitEvent()=$803 Then Exit
Forever

End ; bye!