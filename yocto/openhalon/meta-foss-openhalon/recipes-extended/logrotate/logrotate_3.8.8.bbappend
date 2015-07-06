# Copyright 2015 Hewlett-Packard Development Company, L.P.
PR_append = "_openhalon"


FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://logrotate_default.ovs \
           "
RDEPENDS_${PN} = "python-halon-ovsdb"


do_install_append () {
    install -m 0644 ${WORKDIR}/logrotate_default.ovs ${D}/etc
}
