@echo off
set "BASE=%~dp0"
set "JAVA_HOME=%BASE%jdk-17"
set "PATH=%JAVA_HOME%\bin;%PATH%"

echo JAVA_HOME=%JAVA_HOME%
java -jar "%BASE%servidor.jar"
pause
