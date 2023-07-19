#!/bin/bash

cd /usr/ui/medplat-ui

# Run grunt task
npm install
bower install
grunt medplat
tail -f /dev/null
# cd /usr/web
# mvn clean install -P docker -Dmaven.test.skip=true

# cd /usr/web/target/
# java -jar medplat-web-2.0.jar