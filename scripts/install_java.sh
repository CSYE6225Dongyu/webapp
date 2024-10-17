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
sudo mkdir /home/ubuntu/webapp
sudo cp /tmp/webapp.zip /home/ubuntu/webapp/webapp.zip
cd /home/ubuntu/webapp
sudo unzip webapp.zip -d /home/ubuntu/webapp/

# Change ownership of the webapp directory and its contents
sudo chown -R csye6225:csye6225 /home/ubuntu/webapp

echo "Java JAR file prepared"
