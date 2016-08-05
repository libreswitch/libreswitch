SUMMARY = "Internationalization utilities"
HOMEPAGE = "https://pypi.python.org/pypi/Babel/"

SECTION = "devel/python"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e6eeaabc92cfc2d03f53e789324d7292"

SRCNAME = "babel"
SRC_URI = "git://github.com/python-babel/babel;protocol=https"
SRCREV = "d277c12319ceeb6066898fca64a0381ca5f5a40c"

S = "${WORKDIR}/git"

inherit setuptools

CLEANBROKEN = "1"
BBCLASSEXTEND = "native"
