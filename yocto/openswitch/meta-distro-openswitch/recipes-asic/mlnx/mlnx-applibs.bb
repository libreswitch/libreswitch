# Copyright Mellanox Technologies, Ltd. 2001-2016.

SUMMARY = "Mellanox SX manager userspace libraries and applications"
LICENSE = "Proprietary"

inherit openswitch mellanox

DEPENDS += "mlnx-sx-gen-utils mlnx-sxd-libs libnl mlnx-sx-complib"
RDEPENDS_${PN} += "mlnx-sx-kernel"

SRC_URI = " \
    ${SX_SDK_URI}/mlnx-applibs-${SX_SDK_VERSION}.tar.gz;subdir=mlnx-applibs-${SX_SDK_VERSION} \
"

SRC_URI[md5sum] = "062e094509bd72698355dbe9bfdf5805"
SRC_URI[sha256sum] = "0ca839caee1dd48b2383b72848f939690bbbfb863f5cfe4ef41a0cf2f9f78247"

S = "${WORKDIR}/mlnx-applibs-${SX_SDK_VERSION}/"

LIC_FILES_CHKSUM = " \
    file://${S}/License.txt;md5=801f80980d171dd6425610833a22dbe6;  \
"

# Skip the unwanted steps
do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${prefix}/bin/
    install -d ${D}${prefix}/include/
    install -d ${D}${libdir}

    cp -R ${S}/usr/bin/* ${D}${prefix}/bin/
    chmod -R 755 ${D}${prefix}/bin/

    cp -R ${S}/usr/include/* ${D}${prefix}/include/
    chmod -R 755 ${D}${prefix}/include/

    cp -R ${S}/usr/lib/* ${D}${libdir}
    chmod -R 755 ${D}${libdir}
}

FILES_${PN} = "${prefix}/bin/ ${prefix}/include/ ${libdir}"

# Avoid QA Issue: already-stripped
INSANE_SKIP_${PN} += "already-stripped"
