DESCRIPTION = "High-level, multiplatform C++ network packet sniffing and crafting library."
SECTION = "devel"
HOMEPAGE = "http://http://libtins.github.io/"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD-2-Clause;md5=8bef8e6712b1be5aa76af1ebde9d6378"

DEPENDS = "openssl libpcap"

SRC_URI = "git://github.com/mfontanini/libtins.git;protocol=https"

PR = "3"
SRCREV = "bcd8cc58f7d016368cd33bc09c61563a3011932a"

S = "${WORKDIR}/git"

inherit cmake 

FILES_${PN}-dev += "/usr/CMake"

BBCLASSEXTEND = "native nativesdk"
