SUMMARY = "A comprehensive HTTP client library"
HOMEPAGE = "https://github.com/jcgregorio/httplib2"

SECTION = "devel/python"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=56e5e931172b6164b62dc7c4aba6c8cf"

SRCNAME = "sphinx"

SRC_URI = "git://github.com/jcgregorio/httplib2;protocol=https"
SRCREV = "010b4e399c04a199e7ca25995286f3db0337500d"

S = "${WORKDIR}/git"

inherit setuptools

CLEANBROKEN = "1"
BBCLASSEXTEND = "native"
