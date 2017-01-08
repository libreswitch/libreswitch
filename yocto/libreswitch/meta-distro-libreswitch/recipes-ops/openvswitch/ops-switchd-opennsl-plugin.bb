SUMMARY = "LibreSwitch vswitchd Broadcom plugin"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "ops-ovsdb virtual/opennsl ops-switchd ops-supportability ops-classifierd"

PROVIDES += "virtual/ops-switchd-switch-api-plugin"
RPROVIDES_${PN} += "virtual/ops-switchd-switch-api-plugin"

BRANCH ?= "${LBS_REPO_BRANCH}"

SRC_URI = "${LBS_REPO_BASE_URL}/switchd-opennsl-plugin;protocol=${LBS_REPO_PROTOCOL};branch=${BRANCH}"

FILES_${PN} = "${libdir}/openvswitch/plugins"

SRCREV = "5dbefbe80b393eff2747273eca09aeb44f54a44c"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

inherit libreswitch cmake
