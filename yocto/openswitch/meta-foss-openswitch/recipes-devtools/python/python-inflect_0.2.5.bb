SUMMARY = "Correctly generate plurals, singular nouns, ordinals, indefinite articles; convert numbers to words."
HOMEPAGE = "https://pypi.python.org/pypi/inflect/"

SECTION = "devel/python"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRCNAME = "inflect"

SRC_URI[md5sum] = "ca9ecb838687253eedc6c81f59c656a7"
SRC_URI[sha256sum] = "2014c8dcb2114ebae2941ba3f0fbd98a02c846792a7b72f2da31eb9aa431a818"

S = "${WORKDIR}/${SRCNAME}-${PV}"

inherit pypi

CLEANBROKEN = "1"
