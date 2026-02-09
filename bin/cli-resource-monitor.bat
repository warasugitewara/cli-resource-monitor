@echo off
REM CLI Resource Monitor wrapper script for Windows
set JAR_PATH=%~dp0..\build\libs\cli-resource-monitor-0.1.0.jar

if not exist "%JAR_PATH%" (
    echo Error: JAR file not found at %JAR_PATH%
    echo Please run 'gradle build' first
    exit /b 1
)

java -jar "%JAR_PATH%" %*
