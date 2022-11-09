#!/bin/bash

# internalCRM
cd ../internalCRM/
mvn install
mvn clean package spring-boot:repackage

# virtualCRM
cd ../virtualCRM/
mvn install
mvn clean package spring-boot:repackage

# rss
cd ../rss/
mvn install
mvn clean package spring-boot:repackage

# client
cd ../client/
mvn install
mvn clean package


cd ../executable/
xterm -hold -e "java -jar ../internalCRM/target/internalCRM-2.7.4.jar" &
xterm -hold -e "java -jar ../rss/target/rss-2.7.4.jar java" &
xterm -hold -e "java -jar ../virtualCRM/target/virtualCRM-2.7.4.jar" &
java -jar ../client/target/client-1.0-SNAPSHOT-jar-with-dependencies.jar