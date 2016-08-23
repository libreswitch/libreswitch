SUMMARY = "Python implementation of Markdown."
HOMEPAGE = "https://pypi.python.org/pypi/Markdown"

SECTION = "devel/python"

LICENSE = "BSD"
LIC_FILES_CHKSUM="file://LICENSE.md;md5=bb64a898b2c7d3bbb677124ebd2ba57a"

SRCNAME = "Markdown"
SRC_URI[md5sum] = "d6737edcdb9a9aceaef0bf28455673be"

S = "${WORKDIR}/${SRCNAME}-${PV}"

inherit pypi

CLEANBROKEN = "1"
BBCLASSEXTEND = "native"
