SUMMARY = "Getting image size from png/jpeg/jpeg2000/gif file"
HOMEPAGE = "https://pypi.python.org/pypi/imagesize/"

SECTION = "devel/python"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.rst;md5=0c128f0f7e8a02e1b83884c0b5a41cda"

SRCNAME = "imagesize"
SRC_URI = "git://github.com/shibukawa/imagesize_py;protocol=https"
SRCREV = "cd9c06aec23edf01753d4e45d8db3f95fce73dad"

S = "${WORKDIR}/git"

inherit setuptools

CLEANBROKEN = "1"
BBCLASSEXTEND = "native"
