SUMMARY = "Halon Configuration Daemon"
LICENSE = "Apache-2.0"

LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

RDEPENDS_${PN} = "python-argparse python-distribute"

SRC_URI = "git://git.openhalon.io/openhalon/aaa-utils;protocol=http \
         "

SRCREV="${AUTOREV}"
S = "${WORKDIR}/git"

inherit halon setuptools
