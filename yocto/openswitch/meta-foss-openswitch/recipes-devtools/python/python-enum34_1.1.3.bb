SUMMARY = "Python 3.4 Enum backported"
HOMEPAGE = "https://pypi.python.org/pypi/enum34"

SECTION = "devel/python"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://enum/LICENSE;md5=0a97a53a514564c20efd7b2e8976c87e"

SRCNAME = "enum34"

SRC_URI[md5sum] = "f31c81947ff8e54ec0eb162633d134ce"
SRC_URI[sha256sum] = "865506c22462236b3a2e87a7d9587633e18470e7a93a79b594791de2d31e9bc8"

S = "${WORKDIR}/${SRCNAME}-${PV}"

INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP = "1"

inherit pypi

CLEANBROKEN = "1"
BBCLASSEXTEND = "native"
