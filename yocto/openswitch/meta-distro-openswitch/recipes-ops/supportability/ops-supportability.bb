SUMMARY = "OpenSwitch Supportability Features"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "ops-ovsdb libyaml ops-cli"

RDEPENDS_${PN} = "python-pyinotify python-xattr python-argparse python-json python-ops-ovsdb python-distribute python-pyyaml python-systemd"

SRC_URI = "git://git.openswitch.net/openswitch/ops-supportability;protocol=https  file://ops-supportability.service"

SRCREV = "c4c379419fdbd3954283ebd376f40fbe2c719958"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
#PV = "git${SRCPV}"

S = "${WORKDIR}/git"

# Mixing of two classes, the build happens on the source directory.
inherit openswitch cmake setuptools systemd

# Doing some magic here. We are inheriting cmake and setuptools classes, so we
# need to override the exported functions and call both by ourselves.
do_compile() {
     cd ${S}/src/python/
     distutils_do_compile
     # Cmake compile changes to the B directory
     cmake_do_compile
}

do_install() {
     cd ${S}/src/python/
     distutils_do_install
     # Cmake compile changes to the B directory
     cmake_do_install
}

do_install_append(){

   install -d   ${D}/etc/openswitch/supportability
   install -d   ${D}/usr/bin
   install -d   ${D}${systemd_unitdir}/system

   install -c -m 0644 ${WORKDIR}/ops-supportability.service ${D}${systemd_unitdir}/system/
   install -c -m 0644 ${S}/conf/*.yaml ${D}/etc/openswitch/supportability/
   install -c -m 0444 ${S}/conf/ops_showtech.yaml ${D}/etc/openswitch/supportability/ops_showtech.defaults.yaml
   install -c -m 755 ${S}/scripts/*   ${D}/usr/bin/
}

FILES_${PN} += "/usr/lib/cli/plugins/ \
                /etc/openswitch/supportability"

SYSTEMD_PACKAGES = "${PN}"

SYSTEMD_SERVICE_${PN} = "ops-supportability.service"

inherit openswitch setuptools systemd
