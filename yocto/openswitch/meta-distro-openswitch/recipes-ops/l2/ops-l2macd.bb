SUMMARY = "L2MACD Daemon"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "ops-ovsdb ops-cli"

SRC_URI = "git://git.openswitch.net/cgit/openswitch/ops-l2macd;protocol=http\
           file://ops-l2macd.service"

SRCREV = "4afe7313dc43d665fae10cb392d5659073a47bed"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

FILES_${PN} += "/usr/lib/cli/plugins/"

inherit openswitch
