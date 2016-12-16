DESCRIPTION = "Python Documentation Utililities."
HOMEPAGE = "http://docutils.sourceforge.net/"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://licenses/BSD-2-Clause.txt;md5=a8243e503f772119249d1e6a35fd9211"

SRC_URI = "https://pypi.python.org/packages/37/38/ceda70135b9144d84884ae2fc5886c6baac4edea39550f28bcd144c1234d/docutils-0.12.tar.gz"
SRC_URI[md5sum] = "4622263b62c5c771c03502afa3157768"
SRC_URI[sha256sum] = "c7db717810ab6965f66c8cf0398a98c9d8df982da39b4cd7f162911eb89596fa"
S = "${WORKDIR}/docutils-0.12"

PYPI_PACKAGE_HASH = "ceda70135b9144d84884ae2fc5886c6baac4edea39550f28bcd144c1234d"

inherit setuptools

BBCLASSEXTEND = "native"
