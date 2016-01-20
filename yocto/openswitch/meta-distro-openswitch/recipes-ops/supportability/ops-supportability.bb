SUMMARY = "OpenSwitch Supportability Features"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "ops-ovsdb libyaml"

SRC_URI = "git://git.openswitch.net/openswitch/ops-supportability;protocol=https"

SRCREV  = "e8cb24a6e42f0f70d8728a3039010336fa84551d"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
#PV = "git${SRCPV}"

S = "${WORKDIR}/git"

do_install_append(){
   install -d   ${D}/etc/openswitch/supportability
   install -c -m 755 ${S}/conf/*.yaml ${D}/etc/openswitch/supportability/
}

inherit openswitch cmake
