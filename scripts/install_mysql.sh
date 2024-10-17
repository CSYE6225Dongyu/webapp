#!/bin/bash
set -e

# Update the system
sudo apt-get update

# Install MySQL Server
sudo apt-get install -y mysql-server

# Enable and start the MySQL service
sudo systemctl enable mysql
sudo systemctl start mysql

# Set up MySQL database and user
sudo mysql -e "CREATE DATABASE myapp;"
sudo mysql -e "ALTER USER 'root'@'localhost' IDENTIFIED BY '12345678';"
sudo mysql -e "CREATE USER 'appuser'@'localhost' IDENTIFIED BY '12345678';"
sudo mysql -e "GRANT ALL PRIVILEGES ON myapp.* TO 'appuser'@'localhost';"
sudo mysql -e "FLUSH PRIVILEGES;"

echo "MySQL installed and configured successfully."