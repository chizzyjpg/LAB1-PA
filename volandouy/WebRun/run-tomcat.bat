@echo off
setlocal

REM carpeta donde está este .bat
set "BASE=%~dp0"

REM Tomcat portable
set "CATALINA_HOME=%BASE%apache-tomcat-10.1.36"
set "CATALINA_BASE=%CATALINA_HOME%"

REM Java portable (si existe carpeta jdk-17)
if exist "%BASE%jdk-17\bin\java.exe" (
  set "JAVA_HOME=%BASE%jdk-17"
  set "JRE_HOME=%JAVA_HOME%"
  set "PATH=%JAVA_HOME%\bin;%PATH%"
  echo Usando Java portable: %JAVA_HOME%
) else (
  echo Usando Java del sistema...
)

REM Mostrar variables para debug rápido
echo CATALINA_HOME=%CATALINA_HOME%
echo JAVA_HOME=%JAVA_HOME%

REM Arrancar tomcat en consola (para ver logs)
call "%CATALINA_HOME%\bin\startup.bat"

REM Pequeña espera para que Tomcat arranque
timeout /t 7 /nobreak >nul

REM Abro el navegador en la app web
start "" "http://localhost:8080/volandouy-oficial/home"

endlocal

