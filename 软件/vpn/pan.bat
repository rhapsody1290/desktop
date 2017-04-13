@echo off
sc query |find /i "PanGPS" >nul 2>nul
if not errorlevel 1 (goto exist) else goto notexist

:exist
@echo VPN服务已启动...正在关闭服务...
@net stop "PanGPS"
@pause...
goto :eof

:notexist
@echo VPN服务已停止...正在启动服务...
@net start "PanGPS"
@start "C:\ProgramData\Microsoft\Windows\Start Menu\Programs\Palo Alto Networks\GlobalProtect\GlobalProtect.lnk" "cmd /c echo 2秒关闭 &ping -n 2 localhost>nul"
@pause...
goto :eof