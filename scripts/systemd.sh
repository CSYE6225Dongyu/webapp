
# Systemd stat
sudo cp /tmp/webapp.service /etc/systemd/system/webapp.service
sudo rm -rf /tmp/webapp.service
echo "Webapp service copied"

sudo systemctl daemon-reload
sudo systemctl enable webapp.service
sudo systemctl start webapp
sudo systemctl status webapp

echo "Done with the configuration for the AMI."