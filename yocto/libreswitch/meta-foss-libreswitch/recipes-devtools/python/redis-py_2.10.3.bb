DESCRIPTION = "Python client for Redis key-value store"
HOMEPAGE = "http://github.com/andymccurdy/redis-py"
SECTION = "libs"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=51d9ad56299ab60ba7be65a621004f27"

RDEPENDS_${PN} = "python python-datetime python-netclient python-threading \
    python-misc python-json \
"

SRC_URI = "https://github.com/andymccurdy/redis-py/archive/${PV}.tar.gz \
"

SRC_URI[md5sum] = "67b95628d31167605d63eecb74d1834f"
SRC_URI[sha256sum] = "7aefb80a8c2edca566b8d2465c832c70ec61fcc7cf3cf70951bf9d2fdf52fb36"

S = "${WORKDIR}/redis-py-${PV}/"

inherit distutils

BBCLASSEXTEND = "native nativesdk"
