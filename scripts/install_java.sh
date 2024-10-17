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