DESCRIPTION = "Python Lex-Yacc"
HOMEPAGE = "http://www.dabeaz.com/ply/"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://README.md;beginline=3;endline=30;md5=8fbb823071bdbb0fe2338e4252aabc51"

SECTION = "devel"

SRCNAME = "ply"

SRC_URI = "http://www.dabeaz.com/${SRCNAME}/${SRCNAME}-${PV}.tar.gz"
SRC_URI[md5sum] = "94726411496c52c87c2b9429b12d5c50"
SRC_URI[sha256sum] = "e7d1bdff026beb159c9942f7a17e102c375638d9478a7ecd4cc0c76afd8de0b8"

inherit setuptools

S = "${WORKDIR}/${SRCNAME}-${PV}"

BBCLASSEXTEND = "native"
