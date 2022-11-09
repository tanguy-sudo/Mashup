#!/bin/bash

# internalCRM
xterm -hold -e "java -jar internalCRM-2.7.4.jar" &

# rss
xterm -hold -e "java -jar rss-2.7.4.jar java" &

# virtualCRM
xterm -hold -e "java -jar virtualCRM-2.7.4.jar" &

# client
java -jar client-1.0-SNAPSHOT-jar-with-dependencies.jar
