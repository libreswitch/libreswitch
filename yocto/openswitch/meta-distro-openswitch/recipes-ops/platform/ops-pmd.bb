SUMMARY = "OpenSwitch Pluggable Module Daemon"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "ops-hw-config ops-ovsdb ops-supportability"

SRC_URI = "git://git.openswitch.net/openswitch/ops-pmd;protocol=http;branch=rel/dill \
           file://ops-pmd.service \
"

SRCREV = "6eeae60c0ced9eb6ece9bf7e0fed09a10571f1f0"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

do_install_append() {
     install -d ${D}${systemd_unitdir}/system
     install -m 0644 ${WORKDIR}/ops-pmd.service ${D}${systemd_unitdir}/system/
}

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "ops-pmd.service"

inherit openswitch cmake systemd
