DESCRIPTION = "Utilities for generating documentation from source code"
HOMEPAGE = "http://www.doxygen.org/"
SECTION = "console/utils"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b380c86cea229fa42b9e543fc491f5eb"

DEPENDS = "flex-native bison-native"

SRC_URI = "http://ftp.stack.nl/pub/users/dimitri/${BP}.src.tar.gz"
SRC_URI[md5sum] = "9385dc52f0627875f8fa758e754ec674"
SRC_URI[sha256sum] = "6a718625f0c0c1eb3dee78ec1f83409b49e790f4c6c47fd44cd51cb92695535f"

EXTRA_OECONF = "--prefix ${prefix}"

do_configure () {
	./configure ${EXTRA_OECONF}

        echo "TMAKE_CC=${CC}" >> tmake/lib/linux-g++/tmake.conf
        echo "TMAKE_CXX=${CXX}" >> tmake/lib/linux-g++/tmake.conf
        echo "TMAKE_CFLAGS=${CFLAGS}" >> tmake/lib/linux-g++/tmake.conf
        echo "TMAKE_CXXFLAGS=${CXXFLAGS}" >> tmake/lib/linux-g++/tmake.conf
        echo "TMAKE_LINK=${CXX}" >> tmake/lib/linux-g++/tmake.conf
        echo "TMAKE_LFLAGS=${LDFLAGS}" >> tmake/lib/linux-g++/tmake.conf
}

do_install() {
	oe_runmake install DESTDIR=${D} MAN1DIR=share/man/man1
}

BBCLASSEXTEND = "native"
