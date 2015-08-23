SUMMARY = "OpenSwitch Fan Daemon"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://CMakeLists.txt;beginline=0;endline=14;md5=9bf02f5d4de26e44a8954673dead2ee0"

DEPENDS = "config-yaml ops-ovsdb"

SRC_URI = "git://git.openhalon.io/openhalon/fand;protocol=http \
           file://fand.service \
"

SRCREV = "${AUTOREV}"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

do_install_append() {
     install -d ${D}${systemd_unitdir}/system
     install -m 0644 ${WORKDIR}/fand.service ${D}${systemd_unitdir}/system/
}

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "fand.service"

inherit openswitch cmake systemd
