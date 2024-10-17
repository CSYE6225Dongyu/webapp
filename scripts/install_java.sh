#!/bin/bash
set -e

sudo apt-get update

# local nonlogin user and group
sudo groupadd csye6225
sudo useradd --system --no-create-home --shell /usr/sbin/nologin -g csye6225 csye6225

# OpenJDK 17
sudo apt-get install -y openjdk-17-jdk

## Maven
#sudo apt-get install -y maven

# check version
java -version
#mvn -version

echo "Java installed and configured successfully."

# unzip tool
sudo apt-get install -y unzip

ls -al

mkdir /home/ubuntu/webapp

sudo cp /tmp/webapp.zip /home/ubuntu/webapp/webapp.zip
cd /home/ubuntu/webapp
pwd
ls
unzip webapp.zip  -d /home/ubuntu/webapp/
pwd

# set the owner
chown -R csye6225:csye6225 /home/ubuntu/webapp

echo "Java JAR file prepared"