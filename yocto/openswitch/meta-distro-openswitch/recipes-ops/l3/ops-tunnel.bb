SUMMARY = "Openswitch Tunnel"
LICENSE = "Apache-2.0"

LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "ops-switchd ops-ovsdb"

BRANCH ?= "${OPS_REPO_BRANCH}"

SRC_URI = "${OPS_REPO_BASE_URL}/ops-tunnel;protocol=${OPS_REPO_PROTOCOL};branch=${BRANCH} \
          "
SRCREV="e364575be8f6408d9286c84fbbd16c606c8c49f3"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

SYSTEMD_PACKAGES = "${PN}"

inherit openswitch cmake systemd
