SUMMARY = "L2MACD Daemon"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "ops-ovsdb ops-cli"

SRC_URI = "git://git.openswitch.net/openswitch/ops-l2macd;protocol=http;branch=rel/dill \
           file://ops-l2macd.service \
           "

SRCREV = "fc6763b9ed7bc36cd5a57c3f2a30535516841ab7"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

FILES_${PN} += "/usr/lib/cli/plugins/"

inherit openswitch cmake
