SUMMARY = "OpenSwitch Supportability Features"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "ops-ovsdb libyaml ops-cli"

RDEPENDS_${PN} = "python-argparse python-json python-ops-ovsdb python-distribute python-pyyaml python-systemd"

SRC_URI = "git://git.openswitch.net/openswitch/ops-supportability;protocol=https"

SRCREV = "074590d0d422fa724a41b9f0b2f52d4aad1cc0af"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
#PV = "git${SRCPV}"

S = "${WORKDIR}/git"

do_install_append(){
   install -d   ${D}/etc/openswitch/supportability
   install -c -m 755 ${S}/conf/*.yaml ${D}/etc/openswitch/supportability/
}

FILES_${PN} += "/usr/lib/cli/plugins/"

inherit openswitch cmake
