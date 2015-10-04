SUMMARY = "OpenSwitch OVS Simulator plugin"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "ops-ovsdb"

SRC_URI = "git://git.openswitch.net/openswitch/ops-switchd-container-plugin;protocol=http \
"
FILES_${PN} = "${libdir}/openvswitch/plugins"

SRCREV = "92d20b6c05551951a5dc9bf5fd934005772567f5"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

inherit openswitch cmake
