#!/bin/bash
set -e

sudo apt-get update

# OpenJDK 17
sudo apt-get install -y openjdk-17-jdk

# Maven
sudo apt-get install -y maven

# check version
java -version
mvn -version

echo "Java and Maven installed and configured successfully."

ls -al

mkdir /home/ubuntu/webapp

sudo cp /tmp/webapp.zip /home/ubuntu/webapp/webapp.zip
cd /home/ubuntu/webapp
pwd
ls
unzip webapp.zip  -d /home/ubuntu/webapp/
pwd

echo "Java JAR file prepared"