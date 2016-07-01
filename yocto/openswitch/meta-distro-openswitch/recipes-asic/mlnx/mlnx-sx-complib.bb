# Copyright Mellanox Technologies, Ltd. 2001-2016.

SUMMARY = "Mellanox SX SDK"
LICENSE = "GPLv2 | BSD"

inherit autotools mellanox

DEPENDS += "autogen-native libxml2-native"

LIC_FILES_CHKSUM = " \
    file://${COMMON_LICENSE_DIR}/BSD;md5=3775480a712fc46a69647678acb234cb;  \
    file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6;  \
"

SRC_URI = " \
    ${SX_SDK_URI}/mlnx-sx-complib-${SX_SDK_VERSION}.tar.gz; \
"

SRC_URI[md5sum] = "4b024525dd63b254411623d39db951ea"
SRC_URI[sha256sum] = "0383aeaffc6ac892d928c8e3b96d06776032978e288ae0ce4fb0aa162a577c95"

S = "${WORKDIR}/sx_complib-${SX_SDK_VERSION}-0/"
B = "${S}"
