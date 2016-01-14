SUMMARY = "P4 Data Plane"
HOMEPAGE = "https://github.com/p4lang/p4factory"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2f3453ba8e98aaed11a290758a999e65"

# This is the actual repository, but until the infrastructure issues are
# sorted out, we can host the code on Github
#
# OPS_P4DP_REPO = "git.openswitch.net/openswitch/ops-p4dp"
OPS_P4DP_REPO = "github.com/ops-p4/ops-p4dp.git"

SRC_URI = "\
	git://${OPS_P4DP_REPO};protocol=https;branch=master \
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

FILES_${PN} += "/usr/share/p4/switch_bmv2.json"

inherit pythonnative
inherit autotools-brokensep

LIBTOOL = "${B}/${HOST_SYS}-libtool"
EXTRA_OEMAKE = "'LIBTOOL=${LIBTOOL}'"
