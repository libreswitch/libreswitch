SUMMARY = "OpenSwitch Classifier Daemon"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = ""

SRC_URI = "git://git.openswitch.net/openswitch/ops-classifierd;protocol=http \
           "

SRCREV = "cf5532d59e1ead6baa0d3bc7de31534169e60749"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

inherit openswitch
