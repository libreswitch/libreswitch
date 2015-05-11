SUMMARY = "Library of Halon utils"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://CMakeLists.txt;beginline=0;endline=14;md5=9bf02f5d4de26e44a8954673dead2ee0"

DEPENDS = "systemd"

SRC_URI = "git://git.openhalon.io/openhalon/halonutils;protocol=https"

CFLAGS += "-DHALON"
# For debugging/development purposes
EXTERNALSRC_BUILD="${S}/build"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

inherit cmake
