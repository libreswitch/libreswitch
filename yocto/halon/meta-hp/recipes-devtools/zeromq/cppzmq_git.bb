DESCRIPTION = "ZeroMQ C++ binding"
HOMEPAGE = "https://github.com/zeromq/cppzmq"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=db174eaf7b55a34a7c89551197f66e94"

SRC_URI = "git://github.com/zeromq/cppzmq.git;protocol=https"
SRCREV = "HEAD"
S = "${WORKDIR}/git"
DEPENDS = "zeromq"

PROVIDES = "cppzmq-dev"
RDEPENDS_${PN}-dev = ""

do_install() {
   mkdir -p ${D}${includedir}
   cp zmq.hpp ${D}${includedir}
}

BBCLASSEXTEND = "native nativesdk"
