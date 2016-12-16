SUMMARY = "Python implementation of cksum algorithm"
HOMEPAGE = "https://pypi.python.org/pypi/pycksum/"

SECTION = "devel/python"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=33f0dfb37fe1e02477b4a5b82d2daa45"

SRCNAME = "sphinx"

SRC_URI = "git://github.com/sobotklp/pycksum;protocol=https"
SRCREV = "d893d3bcdabf773eda6fb93c4222d442ee5a3939"

S = "${WORKDIR}/git"

inherit setuptools

CLEANBROKEN = "1"
BBCLASSEXTEND = "native"
