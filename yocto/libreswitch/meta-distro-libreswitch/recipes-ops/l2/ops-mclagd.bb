SUMMARY = "MCLAG Daemon"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "ops-ovsdb"

SRC_URI = "git://git.libreswitch.net/libreswitch/ops-mclagd;protocol=http\
           file://ops-mclagd.service\
           file://ops-mclagkad.service"

SRCREV = "871b0f663e12ca24eb9e9ce612ca62161f7ddcf1"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

do_install_append() {
     install -d ${D}${systemd_unitdir}/system
     install -m 0644 ${WORKDIR}/ops-mclagd.service ${D}${systemd_unitdir}/system/
     install -m 0644 ${WORKDIR}/ops-mclagkad.service ${D}${systemd_unitdir}/system/
}

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "ops-mclagd.service ops-mclagkad.service"

inherit libreswitch cmake systemd
