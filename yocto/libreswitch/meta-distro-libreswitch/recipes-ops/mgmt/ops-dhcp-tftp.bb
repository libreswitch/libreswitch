SUMMARY = "LibreSwitch DHCP-TFTP Daemon"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://setup.py;beginline=1;endline=15;md5=66f387680cedd92d8e3c7a801744d54f"

RDEPENDS_${PN} = "python-argparse python-json python-ops-ovsdb python-distribute"
DEPENDS = "ops-cli ops-supportability"

BRANCH ?= "${LBS_REPO_BRANCH}"

SRC_URI = "${LBS_REPO_BASE_URL}/dhcp-tftp;protocol=${LBS_REPO_PROTOCOL};branch=${BRANCH} \
           file://dhcp_tftp.service \
"

SRCREV = "53e3919cb1bb851162d38786e25f3a1b6a9342d1"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

# Mixing of two classes, the build happens on the source directory.
inherit libreswitch cmake setuptools systemd

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
     # Code for copying to /usr/share/opsplugins.
     install -d ${D}/usr/share/opsplugins
     for plugin in $(find ${S}/opsplugins -name "*.py"); do \
         install -m 0644 ${plugin} ${D}/usr/share/opsplugins
     done
}

FILES_${PN} += "/usr/lib/cli/plugins/ /usr/share/opsplugins"
SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "dhcp_tftp.service"
