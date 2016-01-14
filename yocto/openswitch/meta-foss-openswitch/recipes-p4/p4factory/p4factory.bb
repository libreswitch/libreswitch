SUMMARY = "Programs used to build P4 for the P4 Behavioral Model"
HOMEPAGE = "https://github.com/p4lang/p4factory"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2f3453ba8e98aaed11a290758a999e65"

SRC_URI = "\
	git://git.openswitch.net/openswitch/ops-p4factory;protocol=https;branch=feature/p4 \
"
SRCREV = "${AUTOREV}"
PV = "git${SRCPV}"
S = "${WORKDIR}/git"

DEPENDS = "\
	judy \
	libedit \
	nanomsg \
	p4-hlir \
	python-native \
	python-pyyaml-native \
	python-tenjin \
	thrift \
	thrift-native \
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

LIBTOOL = "${B}/${HOST_SYS}-libtool"
EXTRA_OEMAKE = "'LIBTOOL=${LIBTOOL}'"

do_install() {
}
