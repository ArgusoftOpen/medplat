#!/bin/bash
set -e

# Frontend dependencies
cd /usr/ui
if [ ! -d "node_modules" ]; then
    npm install --legacy-peer-deps
fi

# Always check bower if a bower.json exists and we added dependencies
if [ -f "bower.json" ]; then
    bower install --allow-root
fi

# Backend build
cd /usr/web
export MAVEN_OPTS="-Xmx2048m -Xms512m"

# Internal build is now always fast due to docker-compose volume changes
echo "Starting internal build (fast, no volume overhead for target)..."
mvn install -T 1C -P docker -Dmaven.test.skip=true -Dmaven.wagon.http.pool=false -Dhttp.keepAlive=false

# Final move and run
mkdir -p /usr/target
cp /usr/web/target/medplat-web-2.0.jar /usr/target/ || true
cd /usr/target/

echo "Starting Medplat application..."
java -jar medplat-web-2.0.jar
