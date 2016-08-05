SUMMARY = "This package provides 16 stemmer algorithms (15 + Poerter English stemmer) generated from Snowball algorithms"
HOMEPAGE = "https://pypi.python.org/pypi/snowballstemmer"

SECTION = "devel/python"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE.rst;md5=1289deeefaa88f71a5fdee24350350ff"

SRCNAME = "snowballstemmer"
SRC_URI = "git://github.com/shibukawa/snowball_py;protocol=https"
SRCREV = "f7fb6afc03ee6069b79a3eed861019e12d168596"

S = "${WORKDIR}/git"

inherit setuptools

CLEANBROKEN = "1"
BBCLASSEXTEND = "native"
