SUMMARY = "LibreSwitch Supportability Features"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "ops-ovsdb libyaml systemd ops-cli"

RDEPENDS_${PN} = "python-pyinotify python-xattr python-argparse python-json python-ops-ovsdb python-distribute python-pyyaml python-systemd"

BRANCH ?= "${LBS_REPO_BRANCH}"

SRC_URI = "${LBS_REPO_BASE_URL}/supportability;protocol=${LBS_REPO_PROTOCOL};branch=${BRANCH} \
           file://ops-supportability.service \
"

SRCREV = "239757935bef8d12107e73b0f350167729810297"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

# Mixing of two classes, the build happens on the source directory.
inherit libreswitch cmake setuptools systemd

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

   install -d   ${D}/etc/libreswitch/supportability
   install -d   ${D}/usr/bin
   install -d   ${D}${systemd_unitdir}/system
   install -d   ${D}${libdir}/sysusers.d
   install -d   ${D}${sysconfdir}/tmpfiles.d

   install -c -m 0644 ${WORKDIR}/ops-supportability.service ${D}${systemd_unitdir}/system/
   install -c -m 0644 ${S}/conf/*.yaml ${D}/etc/libreswitch/supportability/
   install -c -m 0644 ${S}/conf/ops_journal.conf ${D}/etc/libreswitch/supportability/ops_journal.conf
   install -c -m 0444 ${S}/conf/ops_showtech.yaml ${D}/etc/libreswitch/supportability/ops_showtech.defaults.yaml
   install -c -m 0644 ${S}/conf/ops_coredump.conf ${D}${libdir}/sysusers.d/ops_coredump.conf
   install -c -m 0644 ${S}/conf/ops_supportability_dir.conf  ${D}${sysconfdir}/tmpfiles.d/ops_supportability_dir.conf
   install -c -m 755 ${S}/scripts/*   ${D}/usr/bin/
}

FILES_${PN} += "/usr/lib/cli/plugins/ \
                /etc/libreswitch/supportability \
                ${libdir}/sysusers.d \
                ${sysconfdir}/tmpfiles.d \
               "

SYSTEMD_PACKAGES = "${PN}"

SYSTEMD_SERVICE_${PN} = "ops-supportability.service"

inherit libreswitch setuptools systemd
