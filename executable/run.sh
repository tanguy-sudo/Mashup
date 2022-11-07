#!/bin/bash
konsole -e "java -jar internalCRM-2.7.4.jar" &
konsole -e "java -jar rss-2.7.4.jar java" &
konsole -e "java -jar virtualCRM-2.7.4.jar" &
java -jar client-1.0-SNAPSHOT-jar-with-dependencies.jar
