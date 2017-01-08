SUMMARY = "LibreSwitch Tunnel"
LICENSE = "Apache-2.0"

LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "ops-switchd ops-ovsdb"

BRANCH ?= "${LBS_REPO_BRANCH}"

SRC_URI = "${LBS_REPO_BASE_URL}/tunnel;protocol=${LBS_REPO_PROTOCOL};branch=${BRANCH} \
          "
SRCREV="0541e28688db6a004f69ef119603355b0a960a1d"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

SYSTEMD_PACKAGES = "${PN}"

inherit libreswitch cmake systemd
