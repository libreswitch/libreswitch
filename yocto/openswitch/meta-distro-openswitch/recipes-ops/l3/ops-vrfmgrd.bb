SUMMARY = "OpenSwitch VRF Manager Daemon"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "ops-utils ops-ovsdb ops-cli"

SRC_URI = "git://git.openswitch.net/openswitch/ops-vrfmgrd;protocol=http;branch=rel/dill \
           file://ops-vrfmgrd.service \
           "

SRCREV = "176f8e48d76cb46d095839796fd1683670354089"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

do_install_append() {
#     install -d ${D}${systemd_unitdir}/system
#     install -m 0644 ${WORKDIR}/ops-vrfmgrd.service ${D}${systemd_unitdir}/system/
}

FILES_${PN} += "/usr/lib/cli/plugins/"
#SYSTEMD_PACKAGES = "${PN}"
#SYSTEMD_SERVICE_${PN} = "ops-vrfmgrd.service"

inherit openswitch systemd
