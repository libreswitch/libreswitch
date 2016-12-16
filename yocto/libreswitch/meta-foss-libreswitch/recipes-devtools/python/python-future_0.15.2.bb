DESCRIPTION = "Clean single-source support for Python 3 and 2"
HOMEPAGE = "https://python-future.org"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=b585501b8fb9dd1da489903dd35e8a22"

SRC_URI = "https://pypi.python.org/packages/5a/f4/99abde815842bc6e97d5a7806ad51236630da14ca2f3b1fce94c0bb94d3d/future-0.15.2.tar.gz"
SRC_URI[md5sum] = "a68eb3c90b3b76714c5ceb8c09ea3a06"
SRC_URI[sha256sum] = "3d3b193f20ca62ba7d8782589922878820d0a023b885882deec830adbf639b97"
S = "${WORKDIR}/future-0.15.2"

PYPI_PACKAGE_HASH = "99abde815842bc6e97d5a7806ad51236630da14ca2f3b1fce94c0bb94d3d"

inherit setuptools

BBCLASSEXTEND = "native"
