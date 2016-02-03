inherit core-image extrausers
EXTRA_USERS_PARAMS = "\
         useradd -N -P netop netop; \
         useradd -N -P admin admin; \
         usermod -g ovsdb_users admin;\
         usermod -g ops_netop netop;\
         usermod -G ovsdb_users,ovsdb-client netop;\
         usermod -s /usr/bin/vtysh admin;\
         usermod -s /usr/bin/vtysh netop;\
         "
IMAGE_FEATURES += "ssh-server-openssh"

IMAGE_GEN_DEBUGFS = "1"
