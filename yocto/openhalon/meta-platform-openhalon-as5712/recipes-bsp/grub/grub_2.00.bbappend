# Copyright 2014  Hewlett-Packard Development Company, L.P. 

PR_append = "_as5712"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://grub.cfg.in"

do_install_append () {
    install -d ${D}/boot/grub
    # These values may came from the environment, since we whitelisted them for bitbake
    test -n "$NFSROOTIP" || NFSROOTIP=`ip addr show dev eth0 scope global | awk '/inet/ { print $2 } ' | awk -F/ '{ print $1 }'`
    test -n "$NFSROOTPATH" || NFSROOTPATH=${BUILD_ROOT}/nfsroot-${MACHINE}
   sed -e "s?@@NFSROOTIP@@?$NFSROOTIP?" -e "s?@@NFSROOTPATH@@?$NFSROOTPATH?" \
      ${WORKDIR}/grub.cfg.in > ${D}/boot/grub/grub.cfg
}

FILES_${PN} += "/boot/grub/grub.cfg"

