DESCRIPTION = "Python xattr:Python wrapper for extended filesystem attributes"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=7ebb582f22ddff5dcb0bc33d04f7cbb8"

SRC_URI[md5sum] = "23e62facf421f13a555ed47fead58052"
SRC_URI[sha256sum] = "71d08712f78ed543215e4145779a336e42acb4003b97e16d493a680cf08f1d01"

DEPENDS += "python-cffi"

PYPI_PACKAGE = "xattr"
inherit pypi
