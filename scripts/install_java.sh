#!/bin/bash
set -e

sudo apt-get update


# Install OpenJDK 17
sudo apt-get install -y openjdk-17-jdk

# Install unzip tool
sudo apt-get install -y unzip

# Check Java version
java -version


echo "Java prepared"
