@echo off
sc query |find /i "PanGPS" >nul 2>nul
if not errorlevel 1 (goto exist) else goto notexist

:exist
@echo VPN����������...���ڹرշ���...
@net stop "PanGPS"
@pause...
goto :eof

:notexist
@echo VPN������ֹͣ...������������...
@net start "PanGPS"
@start "C:\ProgramData\Microsoft\Windows\Start Menu\Programs\Palo Alto Networks\GlobalProtect\GlobalProtect.lnk" "cmd /c echo 2��ر� &ping -n 2 localhost>nul"
@pause...
goto :eof