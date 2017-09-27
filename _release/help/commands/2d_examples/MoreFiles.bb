;select a folder to read
folder$=CurrentDir$()

;open directory for reading
dir=ReadDir( folder$ )

;while more files in the directory...
While MoreFiles( dir )

 - ;print 'em out!
 - Print NextFile$( dir )

Wend

Stop