; Example based on one provided by Mag, extended by Mark Tiffany

; first, let's create a window
WinHandle=CreateWindow("Dessert Menu",0,0,400,250) 

; now create some buttons
OptionButton1=CreateButton("Apple Pie",50,10,300,40,WinHandle,3)
OptionButton2=CreateButton("Cheesecake",50,40,300,40,WinHandle,3)
Checkbox=CreateButton("With Cream",50,70,300,40,WinHandle,2)
ExitButton=CreateButton("Place Order",50,120,300,40,WinHandle)

; now loop and deal with events as they arise
Repeat 
	If WaitEvent()=$401 Then 
		If EventSource()=ExitButton Then Exit 
	End If 
Forever 

; and report the selected options!
msg$ = "You selected "
If ButtonState(OptionButton1) Then
	msg$=msg$+"Apple Pie "
ElseIf ButtonState(OptionButton2) Then
	msg$=msg$+"Cheesecake "
Else
	msg$=msg$+"Nothing "
End If
If ButtonState(Checkbox) Then msg$=msg$+"with cream"
Notify msg$
End