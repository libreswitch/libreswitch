DESCRIPTION = "Python Jinja2: A small but fast and easy to use stand-alone template engine written in pure python."
HOMEPAGE = "https://pypi.python.org/pypi/Jinja2"
SECTION = "devel/python"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=20c831f91dd3bd486020f672ba2be386"

PR = "r0"
SRCNAME = "Jinja2"

SRC_URI = "https://pypi.python.org/packages/source/J/${SRCNAME}/${SRCNAME}-${PV}.tar.gz"

SRC_URI[md5sum] = "282aed153e69f970d6e76f78ed9d027a"
SRC_URI[sha256sum] = "5cc0a087a81dca1c08368482fb7a92fe2bdd8cfbb22bc0fccfe6c85affb04c8b"

S = "${WORKDIR}/${SRCNAME}-${PV}"

inherit setuptools

RDEPENDS_${PN} += "python-io python-pickle python-crypt python-math python-netclient python-re python-textutils python-lang python-pprint python-shell python-markupsafe"

CLEANBROKEN = "1"
