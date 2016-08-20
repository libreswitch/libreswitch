DESCRIPTION = "BCC - Tools for BPF-based Linux IO analysis, networking, monitoring, and more"
HOMEPAGE = "https://github.com/iovisor/bcc"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://COPYRIGHT.txt;md5=a8b22b89b2aca325897ce26415485212"

DEPENDS = "llvm-native bison-native flex-native llvm elfutils flex"

SRC_URI = "git://github.com/iovisor/bcc.git;protocol=http"
SRCREV = "d1a0e7f2c1c3c6c20712e9c2d23304ce2eccf01b"

inherit cmake setuptools

S = "${WORKDIR}/git"

EXTRA_OECMAKE = " -DREVISION=${PV} "

do_compile() {
    cmake_do_compile
    cd ${B}/src/python/
    distutils_do_compile
}

do_install() {
    cmake_do_install
    cd ${B}/src/python/
    distutils_do_install
}

RDEPENDS_${PN} = "python-core"

PACKAGES += "${PN}-examples ${PN}-tools"

FILES_${PN}-doc += "/usr/share/bcc/man"
RDEPENDS_${PN}-examples = "python-core bcc"
FILES_${PN}-examples = "/usr/share/bcc/examples"
RDEPENDS_${PN}-tools = "python-core bcc"
FILES_${PN}-tools = "/usr/share/bcc/tools"

BBCLASSEXTEND = "native nativesdk"
