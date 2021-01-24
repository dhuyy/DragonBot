; Script generated by the Inno Setup Script Wizard.
; SEE THE DOCUMENTATION FOR DETAILS ON CREATING INNO SETUP SCRIPT FILES!

#define MyAppName "DragonBot"
#define MyAppVersion "1.9.5"
#define MyAppPublisher "Vandhuy Martins"
#define MyAppExeName "DragonBot.exe"

[Setup]
; NOTE: The value of AppId uniquely identifies this application. Do not use the same AppId value in installers for other applications.
; (To generate a new GUID, click Tools | Generate GUID inside the IDE.)
AppId={{FE4198FF-EB67-4955-B429-08D04841D0BE}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
;AppVerName={#MyAppName} {#MyAppVersion}
AppPublisher={#MyAppPublisher}
DefaultDirName={autopf}\{#MyAppName}
DisableProgramGroupPage=yes
; Uncomment the following line to run in non administrative install mode (install for current user only.)
;PrivilegesRequired=lowest
OutputDir=C:\Users\vandh\Workspace\DragonBot\setup
OutputBaseFilename=dragonbot-setup
SetupIconFile=C:\Users\vandh\Workspace\DragonBot\icon.ico
Compression=lzma
SolidCompression=yes
WizardStyle=modern

[Languages]
Name: "brazilianportuguese"; MessagesFile: "compiler:Languages\BrazilianPortuguese.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked

[Files]
Source: "C:\Users\vandh\Workspace\DragonBot\setup\dragonbot.exe"; DestDir: "{app}"; Flags: ignoreversion  
Source: "C:\Users\vandh\Workspace\DragonBot\config.xml"; DestDir: "{app}"; Flags: ignoreversion
Source: "C:\Users\vandh\Workspace\DragonBot\images\*"; DestDir: "{app}\images"; Flags: ignoreversion
Source: "C:\Users\vandh\Workspace\DragonBot\logs\*"; DestDir: "{app}\logs"; Flags: ignoreversion
Source: "C:\Users\vandh\Workspace\DragonBot\scripts\*"; DestDir: "{app}\scripts"; Flags: ignoreversion    
Source: "C:\Users\vandh\Workspace\DragonBot\xml\*"; DestDir: "{app}\xml"; Flags: ignoreversion
Source: "C:\Users\vandh\Workspace\DragonBot\xml\ENHANCED\*"; DestDir: "{app}\xml\ENHANCED"; Flags: ignoreversion  
Source: "C:\Users\vandh\Workspace\DragonBot\Tesseract\tessdata\*"; DestDir: "C:\Tesseract\tessdata"; Flags: ignoreversion
; NOTE: Don't use "Flags: ignoreversion" on any shared system files

[InstallDelete]
Type: files; Name: "{app}\character.xml"

[Icons]
Name: "{autoprograms}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"
Name: "{autodesktop}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; Tasks: desktopicon

[Run]
Filename: "{app}\{#MyAppExeName}"; Description: "{cm:LaunchProgram,{#StringChange(MyAppName, '&', '&&')}}"; Flags: nowait postinstall skipifsilent

