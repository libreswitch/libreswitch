# Copyright Mellanox Technologies, Ltd. 2001-2016.

SUMMARY = "SX Examples"
LICENSE = "Proprietary"

inherit libreswitch mellanox

RDEPENDS_${PN} += "mlnx-applibs bash"

SRC_URI = " \
    ${SX_SDK_URI}/mlnx-sx-examples-${SX_SDK_VERSION}.tar.gz;subdir=mlnx-sx-examples-${SX_SDK_VERSION} \
"

SRC_URI[md5sum] = "098070f9f2bfe225f997e35a8244b1fe"
SRC_URI[sha256sum] = "fc22a3032dfd2f4de7dc244b4f9de5e3c8d229d4dc3f89aeaa6347e49fb39117"

S = "${WORKDIR}/mlnx-sx-examples-${SX_SDK_VERSION}/"

LIC_FILES_CHKSUM = " \
    file://${S}/License.txt;md5=801f80980d171dd6425610833a22dbe6;  \
"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${prefix}/bin/

    cp -R ${S}/usr/bin/dvs_stop.sh ${D}${prefix}/bin/
    chmod -R 755 ${D}${prefix}/bin/
}

FILES_${PN} += "${prefix}/bin/"
