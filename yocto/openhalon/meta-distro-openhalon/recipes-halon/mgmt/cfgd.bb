SUMMARY = "Halon Configuration Daemon"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://setup.py;beginline=1;endline=15;md5=718b8f9952f79dfe2d10ad2e7e01f255"

RDEPENDS_${PN} = "python-argparse python-json python-halon-ovsdb python-distribute"

SRC_URI = "git://git.openhalon.io/openhalon/cfgd;protocol=http;preserve_origin=1 \
           file://cfgd.service \
"

SRCREV="${AUTOREV}"

S = "${WORKDIR}/git"

do_install_prepend() {
     install -d ${D}${systemd_unitdir}/system
     install -m 0644 ${WORKDIR}/cfgd.service ${D}${systemd_unitdir}/system/
}

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "cfgd.service"

inherit halon setuptools systemd
