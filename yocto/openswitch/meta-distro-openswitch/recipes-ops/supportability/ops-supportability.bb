SUMMARY = "OpenSwitch Supportability Features"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "ops-ovsdb libyaml ops-cli"

RDEPENDS_${PN} = "python-pyinotify python-xattr python-argparse python-json python-ops-ovsdb python-distribute python-pyyaml python-systemd"

BRANCH ?= "${OPS_REPO_BRANCH}"

SRC_URI = "${OPS_REPO_BASE_URL}/ops-supportability;protocol=${OPS_REPO_PROTOCOL};branch=${BRANCH} \
           file://ops-supportability.service \
"

SRCREV = "691dc9745dd3b6d5d5928ca9ce5d1d523b090c90"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

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
   install -d   ${D}${libdir}/sysusers.d
   install -d   ${D}${sysconfdir}/tmpfiles.d

   install -c -m 0644 ${WORKDIR}/ops-supportability.service ${D}${systemd_unitdir}/system/
   install -c -m 0644 ${S}/conf/*.yaml ${D}/etc/openswitch/supportability/
   install -c -m 0644 ${S}/conf/ops_journal.conf ${D}/etc/openswitch/supportability/ops_journal.conf
   install -c -m 0444 ${S}/conf/ops_showtech.yaml ${D}/etc/openswitch/supportability/ops_showtech.defaults.yaml
   install -c -m 0644 ${S}/conf/ops_coredump.conf ${D}${libdir}/sysusers.d/ops_coredump.conf
   install -c -m 0644 ${S}/conf/ops_supportability_dir.conf  ${D}${sysconfdir}/tmpfiles.d/ops_supportability_dir.conf
   install -c -m 755 ${S}/scripts/*   ${D}/usr/bin/
}

FILES_${PN} += "/usr/lib/cli/plugins/ \
                /etc/openswitch/supportability \
                ${libdir}/sysusers.d \
                ${sysconfdir}/tmpfiles.d \
               "

SYSTEMD_PACKAGES = "${PN}"

SYSTEMD_SERVICE_${PN} = "ops-supportability.service"

inherit openswitch setuptools systemd
