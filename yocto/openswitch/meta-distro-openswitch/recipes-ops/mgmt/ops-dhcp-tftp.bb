SUMMARY = "OpenSwitch DHCP-TFTP Daemon"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://setup.py;beginline=1;endline=15;md5=66f387680cedd92d8e3c7a801744d54f"

RDEPENDS_${PN} = "python-argparse python-json python-ops-ovsdb python-distribute"
DEPENDS = "ops-cli"

SRC_URI = "git://git.openswitch.net/openswitch/ops-dhcp-tftp;protocol=http;branch=rel/dill \
           file://dhcp_tftp.service \
"

SRCREV = "6509dead6e739107e73a86d1d4d0cf35c7780428"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

# Mixing of two classes, the build happens on the source directory.
inherit openswitch cmake setuptools systemd

# Doing some magic here. We are inheriting cmake and setuptools classes, so we
# need to override the exported functions and call both by ourselves.
do_compile() {
     cd ${S}
     distutils_do_compile
     # Cmake compile changes to the B directory
     cmake_do_compile
}

do_install() {
     cd ${S}
     distutils_do_install
     # Cmake compile changes to the B directory
     cmake_do_install
     install -d ${D}${systemd_unitdir}/system
     install -m 0644 ${WORKDIR}/dhcp_tftp.service ${D}${systemd_unitdir}/system/
}

FILES_${PN} += "/usr/lib/cli/plugins/"
SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "dhcp_tftp.service"
