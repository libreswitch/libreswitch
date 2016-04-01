SUMMARY = "OpenSwitch Configuration Daemon"
LICENSE = "Apache-2.0"

LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PACKAGES += "ops-librbac ops-librbac-dev"
FILES_ops-librbac = "/usr/lib/librbac.so.*.*.*"
FILES_ops-librbac-dev = "/usr/lib/pkgconfig/rbac.pc /usr/lib/librbac.so*"

DEPENDS = "ops-ovsdb ops-cli ops-supportability"

RDEPENDS_${PN} = "python-argparse python-json python-ops-ovsdb python-distribute python-pam pam-plugin-radius-auth pam-plugin-radius-chap-auth"

SRC_URI = "git://git.openswitch.net/openswitch/ops-aaa-utils;protocol=http \
           file://aaautils.service \
           file://server \
           file://useradd \
         "

SRCREV = "c6341d86c8af333374ba55cfd8aa9fd7b68f1773"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

# Mixing of two classes, the build happens on the source directory.
inherit openswitch cmake setuptools systemd pkgconfig

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
     install -m 0644 ${WORKDIR}/aaautils.service ${D}${systemd_unitdir}/system/
     install -d ${D}${sysconfdir}/raddb
     install -m 0644 ${WORKDIR}/server ${D}${sysconfdir}/raddb/server
     install -d ${D}${sysconfdir}/sudoers.d
     install -m 0644 ${WORKDIR}/useradd ${D}${sysconfdir}/sudoers.d/useradd
}

FILES_${PN} += "/usr/lib/cli/plugins/"
FILES_${PN}   += "${sysconfdir}/raddb/ ${sysconfdir}/sudoers.d/"
SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "aaautils.service"
