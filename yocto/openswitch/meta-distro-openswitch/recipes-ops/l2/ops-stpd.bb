SUMMARY = "STP Daemon"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "ops-utils ops-ovsdb ops-cli"
FILES_${PN} += "/usr/lib/cli/plugins/"

SRC_URI = "git://git.openswitch.net/openswitch/ops-stpd;protocol=http\
           file://ops-stpd.service"

SRCREV = "0761cb5d0fb1568900ebd7ba79ce3ea1e6651828"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

do_install_append() {
     install -d ${D}${systemd_unitdir}/system
     install -m 0644 ${WORKDIR}/ops-stpd.service ${D}${systemd_unitdir}/system/
}

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "ops-stpd.service"

inherit openswitch cmake systemd
