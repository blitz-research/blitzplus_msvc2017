
;Declarations file for demolib.dll
;
;Adds the following commands:
;
;DemoLib_InvertBits( bank,offset,count )
;DemoLib_ShuffleString$( str$ )

.lib "demolib.dll"

DemoLib_InvertBits( bank*,offset,byte_count):"_InvertBits@12"
DemoLib_ShuffleString$( str$ ):"_ShuffleString@4"
