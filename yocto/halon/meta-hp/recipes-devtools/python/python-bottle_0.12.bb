DESCRIPTION = "A fast, simple and lightweight WSGI micro web-framework for Python"
SECTION = "devel/python"
HOMEPAGE = "http://bottlepy.org"
LICENSE = "MIT"
LIC_FILES_CHKSUM="file://LICENSE;md5=9904c135f2b899cfe014805f75adf4c1"
PRIORITY = "optional"
SRCNAME = "bottle"
PR = "discovery"

SRC_URI = "git://github.com/defnull/bottle.git;protocol=https;tag=0.12.0"
SRC_REV = "HEAD"
S = "${WORKDIR}/git"

RDEPENDS_${PN} = "\
  python-netclient \
  python-netserver \
  python-email \
  python-lang \
  python-crypt \
  python-subprocess \
  python-threading \
  python-re \
"


inherit distutils
