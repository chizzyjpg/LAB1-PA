@echo off
setlocal
set "BASE=%~dp0"
set "CATALINA_HOME=%BASE%apache-tomcat-10.1.36"

call "%CATALINA_HOME%\bin\shutdown.bat"
endlocal
pause
