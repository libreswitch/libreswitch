DESCRIPTION = "Makedump file utility"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"
SRC_URI = "${SOURCEFORGE_MIRROR}/project/makedumpfile/makedumpfile/${PV}/${PN}-${PV}.tar.gz;name=makedumpfile"

SRC_URI[makedumpfile.md5sum] = "e44244db888acdb0e75962bb59feace2"
SRC_URI[makedumpfile.sha256sum] = "47d16312b3226f6d1a1e6548e22c33d00e8851fedab793d97da8d3c0a6205d4a"

DEPENDS = "zlib elfutils bzip2"

EXTRA_OEMAKE = "TARGET=${TARGET_ARCH}"

do_install() {
   install -d ${D}${bindir}/
   install -c -m 755 ${S}/makedumpfile ${D}${bindir}/
}
