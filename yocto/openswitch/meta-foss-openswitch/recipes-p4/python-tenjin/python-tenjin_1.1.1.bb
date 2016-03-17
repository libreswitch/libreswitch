DESCRIPTION = "a fast and full-featured template engine based on embedded Python"
HOMEPAGE = "https://pypi.python.org/pypi/Tenjin/"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://MIT-LICENSE;md5=22f394329e0a797afdde1365a5bafec5"

SECTION = "devel"

SRCNAME = "Tenjin"

SRC_URI = "https://pypi.python.org/packages/source/T/${SRCNAME}/${SRCNAME}-${PV}.tar.gz"
SRC_URI[md5sum] = "61ee558b14ed9c845a9894ad9d56cbbf"
SRC_URI[sha256sum] = "a5e044f3e2c0fc5f5f1162273d541b8f592036c7f9ef99444ff540704e404697"

DEPENDS = "python-native"

inherit setuptools

S = "${WORKDIR}/${SRCNAME}-${PV}"

BBCLASSEXTEND = "native"
