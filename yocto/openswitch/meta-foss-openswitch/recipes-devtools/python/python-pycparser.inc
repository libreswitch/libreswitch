SUMMARY = "C parser in Python"
HOMEPAGE = "https://pypi.python.org/pypi/pycparser"

SECTION = "devel/python"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=8fb364772b1441821d391591f340c35a"

SRCNAME = "pycparser"
SRC_URI = "git://github.com/eliben/pycparser.git;protocol=https"
SRCREV = "7f6b3404da3d3184560b04492b64acf5c0ca4ce2"

S = "${WORKDIR}/git"

CLEANBROKEN = "1"

#RDEPENDS_${PN}_class-native = ""

BBCLASSEXTEND = "native nativesdk"
