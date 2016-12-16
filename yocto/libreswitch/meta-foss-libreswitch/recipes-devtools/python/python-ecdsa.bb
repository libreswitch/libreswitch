DESCRIPTION = "Signativer verification protocol library"
HOMEPAGE = "https://github.com/warner/ecdsa/"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=66ffc5e30f76cbb5358fe54b645e5a1d"

PV = "0.13"
SRCNAME = "ecdsa"
SRC_URI = "http://pypi.python.org/packages/source/e/${SRCNAME}/${SRCNAME}-${PV}.tar.gz"

SRC_URI[md5sum] = "1f60eda9cb5c46722856db41a3ae6670"
SRC_URI[sha256sum] = "64cf1ee26d1cde3c73c6d7d107f835fed7c6a2904aef9eac223d57ad800c43fa"


S = "${WORKDIR}/${SRCNAME}-${PV}"

inherit setuptools

RDEPENDS_${PN} += ""
BBCLASSEXTEND = "native"
