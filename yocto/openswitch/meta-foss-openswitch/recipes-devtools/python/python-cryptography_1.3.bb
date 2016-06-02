SUMMARY = "cryptography is a package which provides cryptographic recipes and primitives to Python developers"
HOMEPAGE = "https://pypi.python.org/pypi/cryptography"

SECTION = "devel/python"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.APACHE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRCNAME = "cryptography"
DEPENDS = "python-cffi-native"
RDEPENDS_${PN} = "python-cffi python-enum34"
SRC_URI[md5sum] = "eb9c78f094bf7fd9517b19fa2593fbb5"
SRC_URI[sha256sum] = "5dcc8d0ba790db6b36067c0db0fa9076de58bec5f9bc6d1c4f2fec8f06dcf0f4"
S = "${WORKDIR}/${SRCNAME}-${PV}"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP = "1"

inherit pypi

CLEANBROKEN = "1"
BBCLASSEXTEND = "native"
