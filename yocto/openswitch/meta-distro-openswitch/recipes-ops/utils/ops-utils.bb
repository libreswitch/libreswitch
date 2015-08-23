SUMMARY = "Library of OpenSwitch utils"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://CMakeLists.txt;beginline=0;endline=14;md5=9bf02f5d4de26e44a8954673dead2ee0"

SRC_URI = "git://git.openhalon.io/openhalon/halonutils;protocol=https"

SRCREV = "${AUTOREV}"

# When using AUTOREV, we need to force the package version to the revision of git
# in order to avoid stale shared states.
PV = "git${SRCPV}"

S = "${WORKDIR}/git"

inherit openswitch cmake
