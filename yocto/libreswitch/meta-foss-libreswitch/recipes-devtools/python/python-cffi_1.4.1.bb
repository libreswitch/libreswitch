SUMMARY = "Foreign Function Interface for Python calling C code."
HOMEPAGE = "https://pypi.python.org/pypi/cffi"

SECTION = "devel/python"

LICENSE = "MIT"
LIC_FILES_CHKSUM="file://LICENSE;md5=5677e2fdbf7cdda61d6dd2b57df547bf"

SRCNAME = "cffi"
DEPENDS ="python-pycparser"
RDEPENDS_${PN} ="python-pycparser"
SRC_URI[md5sum] = "73c2047f598ac7d8b7a5cd8e6d835c42"
SRC_URI[sha256sum] = "61fa9e32cc91744b92b3ec96f74c5910aa6c2f0a9fbba939fbad71dab558b974"

S = "${WORKDIR}/${SRCNAME}-${PV}"

inherit pypi

CLEANBROKEN = "1"
BBCLASSEXTEND = "native"
