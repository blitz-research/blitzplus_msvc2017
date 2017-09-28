[Setup]
OutputBaseFilename=BlitzPlusSetup147
OutputDir=.\

AppName=BlitzPlus
AppVerName=BlitzPlus 1.47
AppPublisher=Blitz Research Ltd
AppPublisherURL=http://www.blitzbasic.com
DefaultDirName={pf}\BlitzPlus
DefaultGroupName=BlitzPlus

AllowRootDirectory=yes
DisableStartupPrompt=yes

[Icons]
Name: "{group}\BlitzPlus"; Filename: "{app}\BlitzPlus.exe"; WorkingDir: "{app}";
Name: "{group}\Uninstall BlitzPlus"; Filename: "{uninstallexe}";

[Files]
Source: ".\_release\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs
Source: ".\_release\cfg\blitzide.prefs"; DestDir: "{app}\cfg"; Flags: ignoreversion deleteafterinstall

[Run]
Filename: "{app}\BlitzPlus.exe"; Description: "Launch BlitzPlus"; Flags: postinstall nowait skipifsilent
