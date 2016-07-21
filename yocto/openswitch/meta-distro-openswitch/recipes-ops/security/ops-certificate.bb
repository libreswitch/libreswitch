SUMMARY = "OpenSwitch system certificate generation"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

RDEPENDS_${PN} = "bash"

SRC_URI = "file://certificate.service \
           file://fix-certificate \
"

S = "${WORKDIR}"

do_install () {
    install -d ${D}${sbindir}
    install -d ${D}${systemd_unitdir}/system

    install -m 0644 ${WORKDIR}/certificate.service ${D}${systemd_unitdir}/system
    install -m 0755 ${WORKDIR}/fix-certificate ${D}${sbindir}/fix-certificate
}

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "certificate.service"

inherit systemd
