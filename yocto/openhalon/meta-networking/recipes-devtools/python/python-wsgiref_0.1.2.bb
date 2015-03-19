DESCRIPTION = "Web Server Gateway Interface (WSGI) Utilities and Reference Implementation"
SECTION = "devel/python"
HOMEPAGE = "https://pypi.python.org/pypi/wsgiref"
LICENSE = "PSF "
LIC_FILES_CHKSUM="file://${COMMON_LICENSE_DIR}/Python-2.0;md5=a5c8025e305fb49e6d405769358851f6"
PRIORITY = "optional"
SRCNAME = "wsgiref"
PR = "discovery"

SRC_URI = "https://pypi.python.org/packages/source/w/wsgiref/wsgiref-0.1.2.zip"
SRC_URI[md5sum] = "29b146e6ebd0f9fb119fe321f7bcf6cb"
SRC_URI[sha256sum] = "c7e610c800957046c04c8014aab8cce8f0b9f0495c8cd349e57c1f7cabf40e79"

S = "${WORKDIR}/wsgiref-0.1.2"

RDEPENDS_${PN} = "\
  python-netclient \
  python-netserver \
  python-re \
"

inherit setuptools 
