#!/bin/bash
set -e

# Create local non-login user and group
sudo groupadd csye6225
sudo useradd --system --no-create-home --shell /usr/sbin/nologin -g csye6225 csye6225

# Create webapp directory and move the zip file
sudo mkdir /opt/webapp
sudo cp /tmp/webapp.zip /opt/webapp/webapp.zip
# for .env file
sudo mkdir /etc/webapp
#sudo cp /tmp/.env /etc/webapp/.env
cd /opt/webapp
sudo unzip webapp.zip -d /opt/webapp/

# Change ownership of the webapp directory and its contents
sudo chown -R csye6225:csye6225 /opt/webapp

