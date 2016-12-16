SUMMARY = "Python documentation generator"
HOMEPAGE = "https://pypi.python.org/pypi/Sphinx/"

SECTION = "devel/python"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=51935905ca4aa737a038ba690fbdbdd2"

SRCNAME = "sphinx-rtd-theme"

DEPENDS = "python-sphinx-native"

SRC_URI = "git://github.com/snide/sphinx_rtd_theme;protocol=https"
SRCREV = "26c82648c42bd422df48220cfbdbef36b4a165a6"

S = "${WORKDIR}/git"

inherit setuptools

CLEANBROKEN = "1"
BBCLASSEXTEND = "native"
