# Copyright Mellanox Technologies, Ltd. 2001-2016.

SUMMARY = "SX ACL Resource Manager"
LICENSE = "Proprietary"

inherit libreswitch mellanox

SRC_URI = " \
    ${SX_SDK_URI}/mlnx-sx-acl-rm-${SX_SDK_VERSION}.tar.gz;subdir=mlnx-sx-acl-rm-${SX_SDK_VERSION} \
"

SRC_URI[md5sum] = "9d144e732a097c2bc598d18a22946c1a"
SRC_URI[sha256sum] = "b2a6c3c790bebefe697ef0716001fe8da68d273ac7027cb4b5bdadb36832fbf9"

S = "${WORKDIR}/mlnx-sx-acl-rm-${SX_SDK_VERSION}"

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

FILES_${PN} += "${prefix}/bin/ ${prefix}/include/ ${libdir}"

# Avoid QA Issue: already-stripped
INSANE_SKIP_${PN} += "already-stripped"
