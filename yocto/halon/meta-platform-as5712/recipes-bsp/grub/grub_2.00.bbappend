# Copyright 2014  Hewlett-Packard Development Company, L.P. 

PR_append = "_as5712"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://grub.cfg"

do_install_append () {
    install -d ${D}/boot/grub
    install -m 0755 ${WORKDIR}/grub.cfg ${D}/boot/grub/grub.cfg
}

FILES_${PN} += "/boot/grub/grub.cfg"

