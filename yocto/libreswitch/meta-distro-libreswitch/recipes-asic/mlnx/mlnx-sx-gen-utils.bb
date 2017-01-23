# Copyright Mellanox Technologies, Ltd. 2001-2016.

SUMMARY = "SX Manager userspace libraries and applications"
LICENSE = "Proprietary"

inherit libreswitch mellanox

DEPENDS += "mlnx-sx-complib"

SRC_URI = " \
    ${SX_SDK_URI}/mlnx-sx-gen-utils-${SX_SDK_VERSION}.tar.gz;subdir=mlnx-sx-gen-utils-${SX_SDK_VERSION} \
"

SRC_URI[md5sum] = "4e860f0537769a2004b43a0edccdb94d"
SRC_URI[sha256sum] = "2b465edd03b758ae99b722d0ca6e1d40933de3e4ee9371ada8a3107cc222c5d1"

S = "${WORKDIR}/mlnx-sx-gen-utils-${SX_SDK_VERSION}/"

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

FILES_${PN} += "${libdir}"

# Avoid QA Issue: already-stripped
INSANE_SKIP_${PN} += "already-stripped"
