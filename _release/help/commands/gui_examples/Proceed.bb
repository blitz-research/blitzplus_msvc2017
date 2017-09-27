; Example provided by Mag
result=Proceed("Are you sure?",True) 
If result=1 Then Notify "I know that you're sure, I just thought I'd ask to annoy you!" 
If result=0 Then Notify "You're not sure!" 
If result=-1 Then Notify "You cancelled the process!" 
End