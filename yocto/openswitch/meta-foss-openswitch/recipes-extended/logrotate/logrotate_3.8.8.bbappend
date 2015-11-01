# Copyright (C) 2015 Hewlett Packard Enterprise Development LP
PR_append = "_openswitch"


FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://logrotate-ops.conf \
           "
RDEPENDS_${PN} = "python-ops-ovsdb"


do_install_append () {
    install -m 0644 ${WORKDIR}/logrotate-ops.conf ${D}/etc
}
