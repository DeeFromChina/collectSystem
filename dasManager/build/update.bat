sc stop dasService
sc delete dasService 
copy /Y %~dp0\update\%1\dasService.jar %~dp0\lib\das_service\dasService.jar
copy /Y %~dp0\update\%1\dasManager.jar %~dp0\lib\das_manager\dasManager.jar
copy /Y %~dp0\update\%1%\dasService.exe %~dp0\dasService.exe
copy /Y %~dp0\update\%1%\dasManager.exe %~dp0\dasManager.exe
sc create dasService binPath= "%~dp0\dasService.exe" start= auto
sc start dasService