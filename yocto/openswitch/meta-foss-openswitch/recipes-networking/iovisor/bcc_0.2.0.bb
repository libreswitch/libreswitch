DESCRIPTION = "BCC - Tools for BPF-based Linux IO analysis, networking, monitoring, and more"
HOMEPAGE = "https://github.com/iovisor/bcc"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://COPYRIGHT.txt;md5=a8b22b89b2aca325897ce26415485212"

DEPENDS = "llvm-native bison-native flex-native llvm elfutils flex"

SRC_URI = "\
    git://github.com/iovisor/bcc.git;protocol=http \
    file://fix-kernel-loc.patch \
"
SRCREV = "63656c26f603a878f015e97923a4b47f4be1b7f3"

inherit cmake setuptools

S = "${WORKDIR}/git"

EXTRA_OECMAKE = " -DREVISION=${PV} -DBCC_KERNEL_MODULES_DIR=/usr/src/kernel -DBCC_KERNEL_MODULES_SUFFIX=''"

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

RDEPENDS_${PN} = "python-core python-ctypes python-json python-multiprocessing kernel-devsrc"

PACKAGES_prepend = " ${PN}-examples ${PN}-tools "

FILES_${PN} += "/lib/modules/${LINUX_VERSION}${LINUX_VERSION_EXTENSION}/build"
FILES_${PN}-doc += "/usr/share/bcc/man"
RDEPENDS_${PN}-examples = "python-core bcc bash"
FILES_${PN}-examples = "/usr/share/bcc/examples"
RDEPENDS_${PN}-tools = "python-core bcc"
FILES_${PN}-tools = "/usr/share/bcc/tools"

# Prevent debian naming rules
AUTO_LIBNAME_PKGS = ""

BBCLASSEXTEND = "native nativesdk"
