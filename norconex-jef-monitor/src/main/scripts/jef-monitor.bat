@echo off
cd %~dp0
set ROOT_DIR=%~dp0

java -Dlog4j.configuration="file:///%ROOT_DIR%classes/log4j.properties" -Dfile.encoding=UTF8 -cp "./lib/*;./classes" com.norconex.jefmon.server.JEFMonServer %*
