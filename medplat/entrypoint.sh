#!/bin/bash

cd /usr/ui/medplat-ui

npm install --legacy-peer-deps
bower install

cd /usr/web
mvn clean install -P docker -Dmaven.test.skip=true

mv /usr/web/target /usr/target/
cd /usr/target/
java -jar medplat-web-2.0.jar
