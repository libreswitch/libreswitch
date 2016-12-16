SUMMARY = "A configurable sidebar-enabled Sphinx theme"
HOMEPAGE = "https://pypi.python.org/pypi/alabaster/"

SECTION = "devel/python"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=91a0fdc82c1f9cb0689e845487137a2f"

SRCNAME = "alabaster"
SRC_URI = "git://github.com/bitprophet/alabaster;protocol=https"
SRCREV = "1c3393da3a99eb0ef3792b922866daf5559bcbf9"

S = "${WORKDIR}/git"

inherit setuptools

CLEANBROKEN = "1"
BBCLASSEXTEND = "native"
