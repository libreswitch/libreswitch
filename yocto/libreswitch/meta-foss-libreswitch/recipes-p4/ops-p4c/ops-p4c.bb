SUMMARY = "P4 Compiler for P4 simulator target"
HOMEPAGE = "https://github.com/p4lang/p4factory"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2f3453ba8e98aaed11a290758a999e65"

LBS_P4C_REPO = "github.com/p4lang/p4c-bm.git"

SRC_URI = "git://${LBS_P4C_REPO};protocol=https;branch=ops \
           "
SRCREV = "a82c029c91f37950e8d51572d093dfda2c178711"
PV = "git${SRCPV}"
S = "${WORKDIR}/git"

PACKAGES_prepend = "${PN}-libpd ${PN}-libpd-dev ${PN}-libpd-staticdev ${PN}-libpd-dbg "
PROVIDES = "${PACKAGES}"

FILES_${PN}-libpd = "/usr/lib/libpd*.so.?.?.?"
FILES_${PN}-libpd-staticdev = "/usr/lib/libpd*.a"
FILES_${PN}-libpd-dev = "/usr/lib/libpd*.la /usr/lib/libpd*.so /usr/lib/libpd*.so.? /usr/lib/pkgconfig"
FILES_${PN}-dbg = ""
FILES_${PN}-libpd-dbg = "/usr/src/debug/ /usr/lib/.debug"

DEPENDS = "\
	judy \
	libedit \
	nanomsg \
	p4-hlir \
	python-ply \
	python-pyyaml-native \
	python-tenjin \
	thrift \
	thrift-native \
	ops-p4dp \
"

RDEPENDS_${PN} = "\
    python-ply \
    python-tenjin \
"

RDEPENDS_class-native = ""

inherit pythonnative autotools-brokensep setuptools

EXTRA_OECONF = "CPPFLAGS='${CPPFLAGS} -DHOST_BYTE_ORDER_CALLER'"

BBCLASSEXTEND = "native"

do_compile() {
    base_do_compile
    distutils_do_compile
}

do_install() {
    distutils_do_install
    autotools_do_install
}

LIBTOOL = "${B}/${HOST_SYS}-libtool"
EXTRA_OEMAKE = "'LIBTOOL=${LIBTOOL}'"
EXTRA_OEMAKE_class-native = "'LIBTOOL=${BUILD_SYS}-libtool'"
