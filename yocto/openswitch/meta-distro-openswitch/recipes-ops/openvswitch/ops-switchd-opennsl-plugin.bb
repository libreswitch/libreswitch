SUMMARY = "OpenSwitch vswitchd Broadcom plugin"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "ops-ovsdb virtual/opennsl ops-switchd ops-supportability ops-classifierd"

PROVIDES += "virtual/ops-switchd-switch-api-plugin"
RPROVIDES_${PN} += "virtual/ops-switchd-switch-api-plugin"

SRC_URI = "git://git.openswitch.net/openswitch/ops-switchd-opennsl-plugin;protocol=http"

FILES_${PN} = "${libdir}/openvswitch/plugins"

SRCREV = "91fd9ab1ef4fe1ca20c58b1d8b0706edc25431e2"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

inherit openswitch cmake
