# -*- mode: ruby -*-
# vi: set ft=ruby :

# Specify Vagrant version and Vagrant API version
Vagrant.require_version ">= 1.6.0"
VAGRANTFILE_API_VERSION = "2"

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|
  config.vm.network "private_network", type: "dhcp"
  config.vm.provider "docker" do |docker|
     docker.vagrant_vagrantfile = "host/Vagrantfile"
     docker.image = "openswitch/genericx86-64"
     docker.cmd = ["/sbin/init"]
     docker.create_args = [
        "--privileged",
        "--volume=\"/tmp:/tmp\"",
        "--volume=\"/dev/log:/dev/log\"",
        "--volume=\"/sys/fs/cgroup:/sys/fs/cgroup\""]
     docker.has_ssh = "false"
     docker.name = "ops"
  end
end
