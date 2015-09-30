SUMMARY = "Tornado is a Python web framework and asynchronous networking library, originally developed at FriendFeed"
HOMEPAGE = "https://pypi.python.org/pypi/tornado/"

SECTION = "devel/python"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRCNAME = "tornado"
RDEPENDS_${PN} = "python-pyOpenSSL"
SRC_URI[md5sum] = "d523204389cfb70121bb69709f551b20"
SRC_URI[sha256sum] = "a16fcdc4f76b184cb82f4f9eaeeacef6113b524b26a2cb331222e4a7fa6f2969"

S = "${WORKDIR}/${SRCNAME}-${PV}"

inherit pypi

CLEANBROKEN = "1"
BBCLASSEXTEND = "native"
