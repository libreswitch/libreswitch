# Copyright Mellanox Technologies, Ltd. 2001-2016.

SUMMARY = "Scew 1.1.2 Libraries required for switch sx_examples"
LICENSE = "LGPLv2.1"

inherit autotools openswitch mellanox

DEPENDS += "autogen-native"
RDEPENDS_${PN} += "expat"

LIC_FILES_CHKSUM = " \
    file://${COMMON_LICENSE_DIR}/LGPL-2.1;md5=1a6d268fd218675ffea8be556788b780;  \
"

SRC_URI = " \
    ${SX_SDK_URI}/mlnx-sx-scew-${SX_SDK_VERSION}.tar.gz; \
"
S = "${WORKDIR}/sx_scew-${SX_SDK_VERSION}-0/"

SRC_URI[md5sum] = "464b067a798a3c16baeed9d111a1b7fc"
SRC_URI[sha256sum] = "0fb7da11b44ceaeb04f62f55fa80b9885b161a70a80d3d5130c58e0b19c2bb54"
