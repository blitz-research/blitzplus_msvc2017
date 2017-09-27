; Dungeon Master Take Off
; By Lee Page
; TeraBit Software

Global xps = (ClientWidth(Desktop())-640)/2
Global yps = (ClientHeight(Desktop())-480)/2

DmWin = CreateWindow("Dungeon Novice",xps,yps,640,500,0,17)

win = CreateCanvas(0,0,640,480,DmWin)
SetBuffer CanvasBuffer(win)

lft = CreateButton("Turn Left",20,400,80,20,win)
rit = CreateButton("Turn Right",540,400,80,20,win)
fwd = CreateButton("Forward",280,300,80,20,win)

Global posx,posy
Dim map(20,20)
Dim hold(1000)
For y=1 To 20
       Read a$
       For t=1 To 20
              If Mid$(a$,t,1)="*" Then
                     map(t,y)=1
              EndIf
       Next
Next
posx = 4
posy = 5
Floor1= LoadImage("Floor1.png")
Floor2= LoadImage("Floor2.png")
L1 = LoadImage("0.png") : R1 = LoadImage("1.png")
L2 = LoadImage("2.png") : M2 = LoadImage("3.png") : R2 = LoadImage("4.png")
L3 = LoadImage("6.png") : M3 = LoadImage("7.png") : R3 = LoadImage("8.png")
LL4 = LoadImage("10.png") : L4 = LoadImage("11.png")
M4 = LoadImage("12.png")
R4 = LoadImage("13.png")
RR4 = LoadImage("14.png")
LL3 = LoadImage("5.png")
RR3 = LoadImage("9.png")
MaskImage L1,255,0,255
MaskImage L2,255,0,255
MaskImage L3,255,0,255
MaskImage L4,255,0,255
MaskImage LL4,255,0,255
MaskImage R1,255,0,255
MaskImage R2,255,0,255
MaskImage R3,255,0,255
MaskImage R4,255,0,255
MaskImage RR4,255,0,255
MaskImage M2,255,0,255
MaskImage M3,255,0,255
MaskImage M4,255,0,255
MaskImage ll3,255,0,255
MaskImage rr3,255,0,255
direction =1
Repeat 
       If blk = 0 Then DrawBlock floor1,0,0 Else DrawBlock floor2,0,0
       If EventSource()=fwd And map(posx,posy-1)<>1 Then posy=posy-1 :  : blk = blk Xor 1
       If EventSource() = rit Then
              cnt=0
              For dw = 1 To 20
                     For ac = 1 To 20
                            hold(cnt)=map(ac,dw)
                            cnt=cnt+1
                     Next
              Next
              cnt=0
              For ac = 1 To 20
                     For dw = 1 To 20
                            map(ac,21-dw)=hold(cnt)
                            cnt=cnt+1
                     Next
              Next
              
              blk = blk Xor 1
              temp=21-posx
              posx=posy
              posy=temp
       EndIf
       If EventSource() = lft Then
              cnt=0
              For dw = 1 To 20
                     For ac = 1 To 20
                            hold(cnt)=map(ac,dw)
                            cnt=cnt+1
                     Next
              Next
              cnt=0
              For ac = 1 To 20
                     For dw = 1 To 20
                            map(21-ac,dw)=hold(cnt)
                            cnt=cnt+1
                     Next
              Next
              temp=posx
              posx=21-posy
              posy=temp
              blk = blk Xor 1
              
       EndIf

       ;If EventSource()=rit And map(posx,posy+1)<>1 Then posy=posy+1 :  : blk = blk Xor 1
       ;If EventSource()=lft And map(posx+1,posy)<>1 Then posx=posx+1 :  : blk = blk Xor 1
       ;If EventSource()=jup And map(posx-1,posy)<>1 Then posx=posx-1 :  : blk = blk Xor 1
       If map(posx-2,posy-3)=1 Then DrawImage LL4,0,92
       If map(posx+2,posy-3)=1 Then DrawImage RR4,559,92
       If map(posx-1,posy-3)=1 Then DrawImage L4,17,92
       If map(posx+1,posy-3)=1 Then DrawImage R4,400,92
       If map(posx,posy-3)=1 Then DrawImage M4,219,92
       If map(posx+2,posy-2)=1 Then DrawImage RR3,618,88
       If map(posx-2,posy-2)=1 Then DrawImage LL3,0,87
       If map(posx+1,posy-2)=1 Then DrawImage R3,420,62
       If map(posx-1,posy-2)=1 Then DrawImage L3,0,62
       If map(posx,posy-2)=1 Then DrawImage M3,188,62
       If map(posx+1,posy-1)=1 Then DrawImage r2,452,6
       If map(posx-1,posy-1)=1 Then DrawImage L2,1,7
       If map(posx-1,posy)=1 Then DrawImage L1,2,0
       If map(posx+1,posy)=1 Then DrawImage R1,515,-2
       If map(posx,posy-1)=1 Then DrawImage m2,121,7
       minimap()
       FlipCanvas win
Until WaitEvent()=$803
EndGraphics
End
Function minimap()
       For y=3 To 18
              For x=3 To 18
                     Color 255*map(x,y),255*map(x,y),255*map(x,y)
                     If x=posx And y = posy Then Color 0,255,0:Rect x*5+484,y*5,5,5
                     If map(x,y)<>0 Then Rect x*5+484,y*5,5,5
              Next
       Next
End Function
Data "********************"
Data "********************"
Data "********************"
Data "*** *            ***"
Data "*** **** ******* ***"
Data "*** *    *       ***"
Data "*** ** *** *********"
Data "*** *    *   *   ***"
Data "*** ******* **** ***"
Data "*** *            ***"
Data "*** * ****** *** ***"
Data "*** * *  *     * ***"
Data "*** **     *********"
Data "*** ****** * *   ***"
Data "*** *          * ***"
Data "*** ************ ***"
Data "***              ***"
Data "********************"
Data "********************"
Data "********************"