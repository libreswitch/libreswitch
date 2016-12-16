# Copyright Mellanox Technologies, Ltd. 2001-2016.

SUMMARY = "SX Manager userspace libraries and applications"
LICENSE = "Proprietary"

inherit libreswitch mellanox

DEPENDS += "mlnx-sx-complib"

SRC_URI = " \
    ${SX_SDK_URI}/mlnx-sx-gen-utils-${SX_SDK_VERSION}.tar.gz;subdir=mlnx-sx-gen-utils-${SX_SDK_VERSION} \
"

SRC_URI[md5sum] = "1ef4cb0d145e12c4e476700f6ee6da5e"
SRC_URI[sha256sum] = "ada612634db930f0290cb751576dd967686a87f93c50333d9125f338cc0dfff9"

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
