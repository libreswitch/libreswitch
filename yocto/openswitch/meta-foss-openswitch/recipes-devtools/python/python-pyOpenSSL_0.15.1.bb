SUMMARY = "Python wrapper module around the OpenSSL library"
HOMEPAGE = "https://pypi.python.org/pypi/pyOpenSSL"

SECTION = "devel/python"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRCNAME = "pyOpenSSL"
DEPENDS = "python-six python-cryptography openssl"
RDEPENDS_${PN} = "python-six python-cryptography openssl"
SRC_URI[md5sum] = "f447644afcbd5f0a1f47350fec63a4c6"
SRC_URI[sha256sum] = "f0a26070d6db0881de8bcc7846934b7c3c930d8f9c79d45883ee48984bc0d672"

S = "${WORKDIR}/${SRCNAME}-${PV}"

inherit pypi

CLEANBROKEN = "1"
BBCLASSEXTEND = "native"
