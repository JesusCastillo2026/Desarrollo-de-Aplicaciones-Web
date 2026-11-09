@echo off
call "%~dp0..\mvnw.cmd" -f "%~dp0pom.xml" %*
exit /b %ERRORLEVEL%
