SUMMARY = "OpenSwitch Pluggable Module Daemon"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "ops-hw-config ops-ovsdb"

SRC_URI = "git://git.openswitch.net/openswitch/ops-pmd;protocol=http \
           file://ops-pmd.service \
"

SRCREV = "d42be01b69c5960e2eba08b4134aaa280e55ac7a"

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
