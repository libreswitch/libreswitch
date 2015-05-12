SUMMARY = "Library for configuration from yaml"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://CMakeLists.txt;beginline=0;endline=14;md5=9bf02f5d4de26e44a8954673dead2ee0"

DEPENDS = "yaml-cpp gtest i2c-tools"

SRC_URI = "git://git.openhalon.io/openhalon/config-yaml;protocol=https;preserve_origin=1"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

inherit cmake halon
