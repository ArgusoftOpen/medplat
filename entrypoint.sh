#!/bin/bash

cd /usr/ui/medplat-ui

npm install --legacy-peer-deps
bower install --allow-root

cd /usr/web

# Retry Maven build up to 3 times to handle network failures
MAX_RETRIES=3
RETRY_COUNT=0
until mvn clean install -P docker -Dmaven.test.skip=true; do
  RETRY_COUNT=$((RETRY_COUNT + 1))
  if [ $RETRY_COUNT -ge $MAX_RETRIES ]; then
    echo "Maven build failed after $MAX_RETRIES attempts"
    exit 1
  fi
  echo "Maven build failed. Retrying ($RETRY_COUNT/$MAX_RETRIES)..."
  sleep 5
done

mkdir -p /usr/target/
mv /usr/web/target/* /usr/target/
cd /usr/target/
java -jar medplat-web-2.0.jar
