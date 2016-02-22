SUMMARY = "OpenSwitch Fan Daemon"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "ops-config-yaml ops-ovsdb ops-cli"

SRC_URI = "git://git.openswitch.net/openswitch/ops-fand;protocol=http \
           file://ops-fand.service \
"

SRCREV = "ba3097cbf2aa74545c2684d9909b900c95c22710"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

do_install_append() {
     install -d ${D}${systemd_unitdir}/system
     install -m 0644 ${WORKDIR}/ops-fand.service ${D}${systemd_unitdir}/system/
}

FILES_${PN} += "/usr/lib/cli/plugins/"
SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "ops-fand.service"

inherit openswitch cmake systemd
