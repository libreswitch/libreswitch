SUMMARY = "LibreSwitch OVS Simulator plugin"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "ops-ovsdb ops-switchd ops-supportability ops-classifierd"
RDEPENDS_${PN} = "openvswitch-sim-switch"

PROVIDES += "virtual/ops-switchd-switch-api-plugin"
RPROVIDES_${PN} += "virtual/ops-switchd-switch-api-plugin"

BRANCH ?= "${LBS_REPO_BRANCH}"

SRC_URI = "${LBS_REPO_BASE_URL}/switchd-container-plugin;protocol=${LBS_REPO_PROTOCOL};branch=${BRANCH} \
"
FILES_${PN} = "${libdir}/openvswitch/plugins"

SRCREV = "8b3353beee15053822a60ef487b7564fa1245247"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

inherit libreswitch cmake
