DESCRIPTION = "World timezone definitions, modern and historical"
HOMEPAGE = "http://pythonhosted.org/pytz/"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=22b38951eb857cf285a4560a914b7cd6"

SRC_URI = "https://pypi.python.org/packages/5d/8e/6635d8f3f9f48c03bb925fab543383089858271f9cfd1216b83247e8df94/pytz-2016.6.1.tar.gz"
SRC_URI[md5sum] = "b6c28a3b968bc1d8badfb61b93874e03"
SRC_URI[sha256sum] = "6f57732f0f8849817e9853eb9d50d85d1ebb1404f702dbc44ee627c642a486ca"
S = "${WORKDIR}/pytz-2016.6.1"

PYPI_PACKAGE_HASH = "6635d8f3f9f48c03bb925fab543383089858271f9cfd1216b83247e8df94"

inherit setuptools

BBCLASSEXTEND = "native"
