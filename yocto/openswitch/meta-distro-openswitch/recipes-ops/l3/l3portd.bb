SUMMARY = "OpenSwitch L3 Port Manager Daemon"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "ops-utils ops-ovsdb"

SRC_URI = "git://git.openhalon.io/openhalon/l3portd;protocol=http\
           file://l3portd.service"

SRCREV = "${AUTOREV}"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

do_install_append() {
     install -d ${D}${systemd_unitdir}/system
     install -m 0644 ${WORKDIR}/l3portd.service ${D}${systemd_unitdir}/system/
}

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "l3portd.service"

inherit openswitch cmake systemd
