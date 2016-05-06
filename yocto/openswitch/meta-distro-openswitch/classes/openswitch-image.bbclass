inherit core-image extrausers
EXTRA_USERS_PARAMS = "\
         useradd -N -M -r opsd; \
         usermod -s /bin/false opsd;\
         usermod -g ovsdb-client opsd;\
         useradd -N -P netop netop; \
         useradd -N -P admin admin; \
         useradd -N -P remote_user remote_user; \
         usermod -g ops_admin admin;\
         usermod -g ops_netop netop;\
         usermod -g ops_netop remote_user;\
         usermod -G ovsdb-client netop;\
         usermod -G ovsdb-client remote_user;\
         usermod -s /bin/bash admin;\
         usermod -s /usr/bin/vtysh netop;\
         usermod -s /usr/bin/vtysh remote_user;\
         "
IMAGE_FEATURES += "ssh-server-openssh"
IMAGE_FEATURES += "package-management"

IMAGE_GEN_DEBUGFS = "1"

IMAGE_FSTYPES_DEBUGFS = "tar"
