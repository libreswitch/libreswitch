SUMMARY = "OpenSwitch DHCP-TFTP Daemon"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://setup.py;beginline=1;endline=15;md5=66f387680cedd92d8e3c7a801744d54f"

RDEPENDS_${PN} = "python-argparse python-json python-ops-ovsdb python-distribute"

SRC_URI = "git://git.openswitch.net/openswitch/ops-dhcp-tftp;protocol=http \
           file://dhcp_tftp.service \
"

SRCREV = "ac687d9596cc91f0d3e6e229b5bc4103a59303af"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

do_install_prepend() {
     install -d ${D}${systemd_unitdir}/system
     install -m 0644 ${WORKDIR}/dhcp_tftp.service ${D}${systemd_unitdir}/system/
}

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "dhcp_tftp.service"

inherit openswitch setuptools systemd
