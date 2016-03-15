SUMMARY = "P4 Compiler for P4 simulator target"
HOMEPAGE = "https://github.com/p4lang/p4factory"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2f3453ba8e98aaed11a290758a999e65"

OPS_P4C_REPO = "github.com/p4lang/p4c-bm.git"

SRC_URI = "\
	git://${OPS_P4C_REPO};protocol=https;branch=ops \
"
SRCREV = "e7901218fd74eeca2af6e8fbcf4d5916c0552069"
PV = "git${SRCPV}"
S = "${WORKDIR}/git"

DEPENDS = "\
	judy \
	libedit \
	nanomsg \
	p4-hlir \
	python-native \
	python-ply \
	python-pyyaml-native \
	python-tenjin \
	thrift \
	thrift-native \
	ops-p4dp \
"

RDEPENDS_${PN} = "\
	judy \
	libedit \
	libpcap \
	nanomsg \
	thrift \
"

inherit pythonnative
inherit autotools-brokensep
inherit setuptools

do_compile() {
    distutils_do_compile
    base_do_compile
}

do_install() {
    distutils_do_install
    autotools_do_install
}

EXTRA_OECONF = "CPPFLAGS='${CPPFLAGS} -DHOST_BYTE_ORDER_CALLER'"

export BUILD_SYS
export HOST_SYS
export STAGING_LIBDIR
export STAGING_INCDIR
export PYTHON_SITEPACKAGES_DIR

LIBTOOL = "${B}/${HOST_SYS}-libtool"
EXTRA_OEMAKE = "'LIBTOOL=${LIBTOOL}' PFX=${PKG_CONFIG_SYSROOT_DIR} STAGING_DIR=${STAGING_DIR_NATIVE}"
