# Copyright Mellanox Technologies, Ltd. 2001-2016.

SUMMARY = "Libraries for SX device management"
LICENSE = "Proprietary"

inherit libreswitch mellanox

DEPENDS += "mlnx-sx-complib"
RDEPENDS_${PN} += "mlnx-sx-kernel"

SRC_URI = " \
    ${SX_SDK_URI}/mlnx-sxd-libs-${SX_SDK_VERSION}.tar.gz;subdir=mlnx-sxd-libs-${SX_SDK_VERSION} \
"

SRC_URI[md5sum] = "05053991d1220c97933e8dfef3a84bdb"
SRC_URI[sha256sum] = "77595f7c014a9aad6dc2e633ec2c371972cadd499e88749b0898a8d7921eeb57"

S = "${WORKDIR}/mlnx-sxd-libs-${SX_SDK_VERSION}/"

LIC_FILES_CHKSUM = " \
    file://${S}/License.txt;md5=801f80980d171dd6425610833a22dbe6;  \
"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${prefix}/include/
    install -d ${D}${libdir}

    cp -R ${S}/usr/include/* ${D}${prefix}/include/
    chmod -R 755 ${D}${prefix}/include/

    cp -R ${S}/usr/lib/* ${D}${libdir}
    chmod -R 755 ${D}${libdir}
}

FILES_${PN} += "${prefix}/include/ ${libdir}"

# Avoid QA Issue: already-stripped
INSANE_SKIP_${PN} += "already-stripped"
