packer {
  required_plugins {
    amazon = {
      version = ">= 1.2.8, <=2.0.0"
      source  = "github.com/hashicorp/amazon"
    }
  }
}

variable "aws_region" {
  type    = string
  default = "us-east-1"
}

variable "ami_name" {
  type    = string
  default = "csye6225-webapp-image-{{timestamp}}"
}

variable "instance_type" {
  type    = string
  default = "t2.small"
}

variable "subnet_id" {
  type    = string
  default = "subnet-001826e44484995c0"
}

variable "source_ami" {
  type    = string
  default = "ami-0866a3c8686eaeeba"
}

variable "ssh_username" {
  type    = string
  default = "ubuntu"
}

variable "ami_users" {
  type    = list(string)
  default = [""]
}


# AWS constructor
source "amazon-ebs" "my-ami" {
  region          = var.aws_region
  ami_name        = var.ami_name
  ami_description = "AMI for CSYE6225 HW5"
  ami_users       = var.ami_users

  aws_polling {
    delay_seconds = 120
    max_attempts  = 50
  }

  instance_type = "${var.instance_type}"
  source_ami    = "${var.source_ami}"
  ssh_username  = "${var.ssh_username}"
  subnet_id     = "${var.subnet_id}"

  launch_block_device_mappings {
    delete_on_termination = true
    device_name           = "/dev/sda1"
    volume_size           = 8
    volume_type           = "gp2"
  }
}

build {
  sources = ["source.amazon-ebs.my-ami"]

  provisioner "file" {
    source      = "../webapp.zip"
    destination = "/tmp/webapp.zip"
  }

  #  temp .env for systemd
  provisioner "file" {
    source      = "../.env"
    destination = "/tmp/.env"
  }

  #  check the application properties
  provisioner "file" {
    source      = "../application.properties"
    destination = "/tmp/application.properties"
  }

  provisioner "file" {
    source      = "../scripts/webapp.service"
    destination = "/tmp/webapp.service"
  }

  provisioner "shell" {
    environment_vars = [
      "DEBIAN_FRONTEND=noninteractive",
      "CHECKPOINT_DISABLE=1",
    ]
    scripts = [
      #      "../scripts/install_mysql.sh", # for mysql, no local for mysql
      "../scripts/install_java.sh",   #for java and unzip jar
      "../scripts/prepare_folder.sh", # prepare folder for java env and jar
      "../scripts/systemd.sh"         # run web.service to start
    ]
  }
}