#!/bin/bash
xterm -hold -e "java -jar internalCRM-2.7.4.jar" &
xterm -hold -e "java -jar rss-2.7.4.jar java" &
xterm -hold -e "java -jar virtualCRM-2.7.4.jar" &
java -jar client-1.0-SNAPSHOT-jar-with-dependencies.jar
