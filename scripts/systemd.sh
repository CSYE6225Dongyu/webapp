#!/bin/bash
set -e

# Copy the service file to the systemd directory
sudo cp /tmp/webapp.service /etc/systemd/system/webapp.service
sudo rm -rf /tmp/webapp.service
echo "Webapp service copied"

# Reload systemd to recognize the new service
sudo systemctl daemon-reload

# Enable the service to start on boot
sudo systemctl enable webapp.service

# Start the service
sudo systemctl start webapp.service

# Check the status of the service to ensure it started correctly
sudo systemctl status webapp.service

echo "Done with the configuration for spring boot."


# cloud watch
sudo systemctl enable amazon-cloudwatch-agent
sudo systemctl start amazon-cloudwatch-agent

sudo systemctl status amazon-cloudwatch-agent

echo "Done with the configuration for cloudwatch."