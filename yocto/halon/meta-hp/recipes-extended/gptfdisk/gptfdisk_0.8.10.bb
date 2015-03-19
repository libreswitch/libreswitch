SUMMARY = "Disk partitioning tool for GUID Partition Table"
DESCRIPTION = "\
  GPT fdisk is a disk partitioning tool loosely modeled on \
  Linux fdisk, but used for modifying GUID Partition Table (GPT) disks. \
  The related FixParts utility fixes some common problems on Master Boot \
  Record (MBR) disks."
SECTION = "console/utils"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552"

PR = "r0"

SRC_URI = "http://sourceforge.net/projects/${PN}/files/${PN}/${PV}/${PN}-${PV}.tar.gz \
           file://fix-host-contamination.patch \
"

SRC_URI[md5sum] = "9cf4246c181c324bdbd553fe9b348373"
SRC_URI[sha256sum] = "73e64151203ae0c347c488358e71ca582bb7fb7f0d66df86b71c42050390eb9b"

do_install () {
    install -d ${D}${sbindir}
    install -m 755 ${S}/gdisk ${D}${sbindir}/
    install -m 755 ${S}/cgdisk ${D}${sbindir}/
    install -m 755 ${S}/sgdisk ${D}${sbindir}/
    install -m 755 ${S}/fixparts ${D}${sbindir}/
}

FILES_${PN} += "${sbindir}/* \
"

DEPENDS += "util-linux"
