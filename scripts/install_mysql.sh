#!/bin/bash
set -e

sudo apt-get update

sudo apt-get install -y mysql-server

# mysql
sudo systemctl enable mysql
sudo systemctl start mysql

#  MySQL database and user
sudo mysql -e "CREATE DATABASE myapp;"
sudo mysql -e "CREATE USER 'appuser'@'localhost' IDENTIFIED BY '12345678';"
sudo mysql -e "GRANT ALL PRIVILEGES ON myapp.* TO 'appuser'@'localhost';"
sudo mysql -e "FLUSH PRIVILEGES;"