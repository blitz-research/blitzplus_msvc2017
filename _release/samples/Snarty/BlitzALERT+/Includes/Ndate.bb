; Numeric Date Functions
; Written By Paul Snart (Snarty)
; Copyright Pacific Software

; ConvertDate$(Convert$,Mode)
; Convert$ must be in the BlitzBasic command CurrentDate$() format into days elapsed since 0/0/0000.
; eg: "10 Dec 2001" / "DD MMM YYYY"
; This will not work if spaces are omitted or less than 2 digits are used for day
; Mode=0-2: 0=Days elapsed returned
;			1=Date Returned (ie 1-31)
;			2=Year Returned

; RevertDate$(NumericDate%,Mode)
; NumericDate is the number of Days past 0/0/0000
; Mode=0-3: 0=Returns CurrentDate$() Style string ie "DD MMM YYYY"
;			1=Returns Date of Month
;			2=Returns Month in numeric form 1-12
;			3=Retunrs Year

; Weekday$=Day(NumericDate%,mode)
; Returns 1-7: 1=Sunday, 7=Saturday etc
; Mode=0-2: 0=Numeric Returned, 
;			1=Ddd String Returned, ie Mon
;			2=Full String Returned

; Month$($)=Month(Convert$,mode) < As ConvertDate
; Returns 1-12: 1=January, 12=December etc
; Mode 0-3: 0=Numeric Returned
;			1=Mmm String Returned
;			2=Full String Returned
;			3=Total Days in the month returned (If Feb it will return 29 if a leap year)

; IsLeap=CheckLeap(Year%)
; Return True/False

; nth$(Number)
; Returns the written form of a number ie... 1 = 1st, 2 = 2nd, 3 = 3rd etc.

Const BenchDate=731342

Dim Months(12),AMonth$(12),ADay$(7),ADate(2,4)

Restore MonthsList
For t=1 To 12
	Read Months(t)
Next
For t=1 To 7
	Read ADay(t)
Next 
For t=1 To 12
	Read AMonth(t)
Next

Global cdt$=CurrentDate()
Global ndt=ConvertDate(cdt,0)
If ndt<BenchDate 
	ndt=BenchDate
	cdt$=RevertDate(ndt,0)
EndIf
ADate(2,0)=ConvertDate(cdt,0)
ADate(2,1)=Day(ndt,0)
ADate(2,2)=ConvertDate(cdt,1)
ADate(2,3)=Month(cdt,0)
ADate(2,4)=ConvertDate(cdt,2)

For t=0 To 4
	ADate(0,t)=ADate(2,t)
Next
	
Function RevertDate$(Convert%,mode%)

	Year#=Floor(Convert/365.25)
	DaysIn=Convert-((Year*365)+Floor((Year-1)/4))
	chkDays%=DaysIn
	For m=1 To 12
		chkDays=chkDays-Months(m)
		If chkDays<1
			Month=m
			chkDays=chkDays+Months(m)
			Exit
		EndIf
	Next
	Date=chkDays
	Select Mode
		Case 0
			Return String("0",2-Len(Str(Date)))+Date+" "+Mid$(AMonth(Month),1,3)+" "+Int(Year)
	
		Case 1
			Return Date
			
		Case 2
			Return Month
			
		Case 3
			Return Int(Year)
			
	End Select

End Function

Function ConvertDate$(Convert$,mode)

	Date%=Mid(Convert,1,2)
	Month%=Month(Convert,0)
	Year#=Mid(Convert,8,4)
	num%=(Year*365)+Floor((Year-1)/4)
	If Month>1
		For m=1 To Month-1
			If m=2 And CheckLeap(year)
				num=num+1
			EndIf
			num=num+Months(m)
		Next
		num=num+date
	Else
		num=num+date
	EndIf
	
	Select mode
		Case 0
			Return num
		Case 1
			Return nth(Date)
		Case 2
			Return  Int(Year)
	End Select
	

End Function

Function Month$(Convert$,mode)

	CMonth$=Upper(Mid(Convert,4,3))
	For t=1 To 12
		If CMonth=Upper(Mid(AMonth(t),1,3))
			Month%=t
			Exit
		EndIf 
	Next
	Select Mode
		Case 0
			Return Month
		Case 1
			Return Mid(AMonth$(Month),1,3)
		Case 2
			Return AMonth(Month)
		Case 4
			If CheckLeap(Mid(Convert,8,4)) And Month=2
				Return 29
			Else
				Return Months(Month)
			EndIf
	End Select
				
End Function

Function CheckLeap(Year)

	past=Abs(2000-year)
	If (past Mod 4)=0 Return True Else Return False

End Function

Function Day$(NDate%,mode)

	If NDate<BenchDate
		RDay=7-(Abs(NDate-BenchDate) Mod 7)+1
		If RDay=8
			RDay=1
		EndIf
	Else
		RDay=(Abs(NDate-BenchDate) Mod 7)+1
		If RDay=8
			RDay=1
		EndIf
	EndIf
	Select Mode
		Case 0
			Return RDay
		Case 1
			Return Mid(ADay(RDay),1,3)
		Case 2
			Return ADay(RDay)
	End Select
	
End Function

Function nth$(Num)

	chk$=Num
	If (Num>3 And Num<21) Or (Num>23 And Num<>31)
		chk=chk+"th"
	ElseIf Mid(chk,Len(chk),1)=1
		chk=chk+"st"
	ElseIf Mid(chk,Len(chk),1)=2
		chk=chk+"nd"
	ElseIf Mid(chk,Len(chk),1)=3
		chk=chk+"rd"
	EndIf
	Return chk
	
End Function

.MonthsList
Data 31,28,31,30,31,30,31,31,30,31,30,31
.DateList
Data "Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"
Data "January","February","March","April","May","June","July","August","September","October","November","December"