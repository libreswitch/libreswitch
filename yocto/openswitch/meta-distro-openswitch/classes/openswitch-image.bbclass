inherit core-image extrausers
EXTRA_USERS_PARAMS = "\
         useradd -N -P admin admin; \
         usermod -g ovsdb_users admin;\
         usermod -s /usr/bin/vtysh admin;\
         "
IMAGE_FEATURES += "ssh-server-openssh"

IMAGE_GEN_DEBUGFS = "1"
