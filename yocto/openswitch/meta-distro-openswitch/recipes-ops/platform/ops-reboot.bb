SUMMARY = "OpenSwitch system reboot script."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

RDEPENDS_${PN} = "bash"

SRC_URI = "file://switch_reboot@.service \
           file://switch_reboot.sh \
"

S = "${WORKDIR}"

do_install () {
    install -d ${D}${sbindir}
    install -d ${D}${systemd_unitdir}/system

    install -m 0644 ${WORKDIR}/switch_reboot@.service ${D}${systemd_unitdir}/system/switch-reboot@.service
    install -m 0755 ${WORKDIR}/switch_reboot.sh ${D}${sbindir}/ops-reboot
}

FILES_${PN} += "${systemd_unitdir}/system/switch-reboot@.service ${sbindir}/ops-reboot"
SYSTEMD_PACKAGES = "${PN}"

inherit systemd
