#!/bin/sh

cd $(dirname $0)

java -Dfile.encoding=UTF8 -cp "./lib/*:./classes" com.norconex.jefmon.server.JEFMonServer "$@"
