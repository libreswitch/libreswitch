SUMMARY = "OpenSwitch Buffer Monitor Daemon"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "ops-ovsdb"

RDEPENDS_${PN} = "python-argparse python-json python-ops-ovsdb python-distribute python-pyyaml"

SRC_URI = "git://git.openswitch.net/openswitch/ops-bufmond;protocol=https;branch=release \
    file://bufmond.service \
"

SRCREV = "82ff830544a9d1ed97a7fcb3b6b57cbefa9df3ed"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

do_install_prepend() {
     install -d ${D}${systemd_unitdir}/system
     install -m 0644 ${WORKDIR}/bufmond.service ${D}${systemd_unitdir}/system/
}

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "bufmond.service"

inherit openswitch setuptools systemd
