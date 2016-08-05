SUMMARY = "Library of OpenSwitch utils"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "ops-openvswitch"

BRANCH ?= "${OPS_REPO_BRANCH}"

SRC_URI = "${OPS_REPO_BASE_URL}/ops-utils;protocol=${OPS_REPO_PROTOCOL};branch=${BRANCH}"

SRCREV = "14fdf16f7bf302f412b5c052b406217fcd104d3e"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

inherit openswitch cmake
