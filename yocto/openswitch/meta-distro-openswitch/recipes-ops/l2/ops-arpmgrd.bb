SUMMARY = "OpenSwitch ARP Manager Daemon"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "ops-utils ops-ovsdb"

SRC_URI = "git://git.openswitch.net/openswitch/ops-arpmgrd;protocol=http;branch=release\
           file://ops-arpmgrd.service"

SRCREV = "6732de13103857ab3e06c0d92a45673a1365252f"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

do_install_append() {
     install -d ${D}${systemd_unitdir}/system
     install -m 0644 ${WORKDIR}/ops-arpmgrd.service ${D}${systemd_unitdir}/system/
}

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "ops-arpmgrd.service"

inherit openswitch cmake systemd
