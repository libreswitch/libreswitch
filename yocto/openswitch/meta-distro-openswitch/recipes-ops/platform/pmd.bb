SUMMARY = "OpenSwitch Pluggable Module Daemon"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://CMakeLists.txt;beginline=0;endline=14;md5=90baf0d21acabd54e99d75cfac782f1a"

DEPENDS = "config-yaml ops-ovsdb"

SRC_URI = "git://git.openhalon.io/openhalon/pmd;protocol=http \
           file://pmd.service \
"

SRCREV="${AUTOREV}"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

do_install_append() {
     install -d ${D}${systemd_unitdir}/system
     install -m 0644 ${WORKDIR}/pmd.service ${D}${systemd_unitdir}/system/
}

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "pmd.service"

inherit openswitch cmake systemd
