#!/bin/bash
set -e

sudo apt-get update

# install cloudwatch agent
wget https://s3.amazonaws.com/amazoncloudwatch-agent/ubuntu/amd64/latest/amazon-cloudwatch-agent.deb
sudo dpkg -i -E ./amazon-cloudwatch-agent.deb
rm -f amazon-cloudwatch-agent.deb # delete package

# for cloudwatch json
# folder will be created after install
sudo cp /tmp/amazon-cloudwatch-agent.json /opt/aws/amazon-cloudwatch-agent/etc/amazon-cloudwatch-agent.json
#make sure user have the right
sudo chown -R csye6225:csye6225 /opt/aws/amazon-cloudwatch-agent/etc/amazon-cloudwatch-agent.json
echo "CloudWatch Agent configuration copied"

# init the json file
sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -a fetch-config -m ec2 -c file:/opt/aws/amazon-cloudwatch-agent/etc/amazon-cloudwatch-agent.json -s

# check
sudo systemctl status amazon-cloudwatch-agent

echo "Done with the install of CloudWatch Agent."