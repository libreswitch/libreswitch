SUMMARY = "Halon system iniitilization script."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

RDEPENDS_${PN} = "bash"

SRC_URI = "file://halon-init.service \
           file://halon-init.sh \
           file://halon-profile.sh \
"

S = "${WORKDIR}"

do_install () {
    install -d ${D}${sbindir}
    install -d ${D}${systemd_unitdir}/system
    install -d ${D}${sysconfdir}/profile.d

    install -m 0644 ${WORKDIR}/halon-init.service ${D}${systemd_unitdir}/system
    install -m 0755 ${WORKDIR}/halon-init.sh ${D}${sbindir}/halon-init
    install -m 0755 ${WORKDIR}/halon-profile.sh ${D}${sysconfdir}/profile.d/halon-profile
}

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "halon-init.service"

inherit systemd
