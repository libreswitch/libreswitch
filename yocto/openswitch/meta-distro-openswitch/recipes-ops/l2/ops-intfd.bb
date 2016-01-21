SUMMARY = "OpenSwitch Interface Daemon"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "ops-utils ops-config-yaml ops-ovsdb"

SRC_URI = "git://git.openswitch.net/openswitch/ops-intfd;protocol=http\
           file://ops-intfd.service"

SRCREV = "484d74eec3c438fc0d50f845545512080d0ac2c3"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

do_install_append() {
     install -d ${D}${systemd_unitdir}/system
     install -m 0644 ${WORKDIR}/ops-intfd.service ${D}${systemd_unitdir}/system/
}

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "ops-intfd.service"

inherit openswitch cmake systemd
