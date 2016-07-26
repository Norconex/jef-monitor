#!/bin/sh

cd $(dirname $0)
export ROOT_DIR=$(realpath $(dirname $0))

java -Dlog4j.configuration="file:///${ROOT_DIR}/classes/log4j.properties" -Dfile.encoding=UTF8 -cp "./lib/*:./classes" com.norconex.jefmon.server.JEFMonServer "$@"
