# Copyright Mellanox Technologies, Ltd. 2001-2016.

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
            file://switchd_sai.service \
            file://sx_cleanup.sh \
            "

do_install_append() {
    install -m 0644 ${WORKDIR}/switchd_sai.service ${D}${systemd_unitdir}/system/switchd.service

    install -d ${D}/usr
    install -d ${D}/usr/bin
    install -m 0755 ${WORKDIR}/sx_cleanup.sh ${D}/usr/bin/sx_cleanup.sh
}

FILES_${PN} += "${sbindir}/ops-switchd /usr/bin/sx_cleanup.sh"

SYSTEMD_SERVICE_${PN} += " \
                        switchd.service \
                        "
