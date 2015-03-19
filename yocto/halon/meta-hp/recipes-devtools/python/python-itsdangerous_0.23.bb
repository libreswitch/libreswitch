DESCRIPTION = "Various helpers to pass trusted data to untrusted environments and back"
HOMEPAGE = "https://pypi.python.org/pypi/itsdangerous/"
SECTION = "devel/python"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b61841e2bf5f07884148e2a6f1bcab0c"

PR = "r0"
SRCNAME = "itsdangerous"

SRC_URI = "https://pypi.python.org/packages/source/i/${SRCNAME}/${SRCNAME}-${PV}.tar.gz"

SRC_URI[md5sum] = "985e726eb76f18aca81e703f0a6c6efc"
SRC_URI[sha256sum] = "71c0bf6bde4dcc93d838415e728a961a7b16f324b8ae182525fbdadde9f55d62"

S = "${WORKDIR}/${SRCNAME}-${PV}"

inherit setuptools

RDEPENDS_${PN} += "python-json python-netclient python-zlib python-datetime python-lang python-crypt"
