sc create dasService binPath= "%~dp0\dasService.exe" start= auto
sc start dasService
sc create dasUpdateService binPath= "%~dp0\dasUpdateService.exe" start= auto
sc start dasUpdateService
pause