SUMMARY = "Python documentation generator"
HOMEPAGE = "https://pypi.python.org/pypi/Sphinx/"

SECTION = "devel/python"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=72f034adc6f7b05b09bc00d1a05bb065"

SRCNAME = "sphinx"

SRC_URI = "git://github.com/sphinx-doc/sphinx;protocol=https"
SRCREV = "84baf05c4f9c3ff3f17779fe522cec7aa444b2f8"

S = "${WORKDIR}/git"

DEPENDS += " \
     python-alabaster-native \
     python-babel-native \
     python-commonmark-native \
     python-docutils-native \
     python-future-native \
     python-imagesize-native \
     python-jinja2-native \
     python-markupsafe-native \
     python-pygments-native \
     python-pytz-native \
     python-recommonmark-native \
     python-requests-native \
     python-six-native \
     python-snowballstemmer-native \
"

inherit setuptools

CLEANBROKEN = "1"
BBCLASSEXTEND = "native"
