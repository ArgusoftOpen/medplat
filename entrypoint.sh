#!/bin/bash

#mv apache-maven-3.2.5 /usr/web
#mv jdk-13.0.2 /usr/web

cd /usr/ui/medplat-ui

# Run grunt task
npm install
bower install
grunt medplat
# tail -f /dev/null

cd /usr/web
mvn clean install -P docker -Dmaven.test.skip=true

# cd /usr/web/target/
mv /usr/web/target /usr/target/
cd /usr/target/
java -jar medplat-web-2.0.jar
