#!/bin/bash

# Fix any potential CRLF from the start
sed -i 's/\r$//' /usr/entrypoint.sh

cd /usr/ui/medplat-ui

npm install --legacy-peer-deps
bower install

cd /usr/web
mvn clean install -P docker -Dmaven.test.skip=true

mkdir -p /usr/target/
cp /usr/web/target/medplat-web-2.0.jar /usr/target/
cd /usr/target/
java -jar medplat-web-2.0.jar