DESCRIPTION = "A docutils-compatibility bridge to CommonMark"
HOMEPAGE = "https://github.com/rtfd/recommonmark"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://license.md;md5=5e65ef1f0d4e1a71254a98b0ed7e9101"

SRC_URI = "git://github.com/rtfd/recommonmark;protocol=https"
SRCREV = "e81b0a0de13be3c5effe26e531df17c16e87329a"
S = "${WORKDIR}/git"

PYPI_PACKAGE_HASH = "aa1085573adf3dc7b164ae8569d57b1af5e98922e40345bb7efffed5ad2e"

inherit setuptools

BBCLASSEXTEND = "native"
