#!/bin/bash
set -e

sudo apt-get update

# Create local non-login user and group
sudo groupadd csye6225
sudo useradd --system --no-create-home --shell /usr/sbin/nologin -g csye6225 csye6225

# Install OpenJDK 17
sudo apt-get install -y openjdk-17-jdk

# Install unzip tool
sudo apt-get install -y unzip

# Check Java version
java -version

# Create webapp directory and move the zip file
sudo mkdir /opt/webapp
sudo cp /tmp/webapp.zip /opt/webapp/webapp.zip
cd /opt/webapp
sudo unzip webapp.zip -d /opt/webapp/

# Change ownership of the webapp directory and its contents
sudo chown -R csye6225:csye6225 /opt/webapp

echo "Java JAR file prepared"
