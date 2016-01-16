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
	file://simple_switch.service \
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
	python-thrift \
"

RDEPENDS_${PN} = "\
	judy \
	libedit \
	libpcap \
	nanomsg \
	thrift \
	python-thrift \
	libcrypto \
	gmp \
	libssl \
"

do_install_append() {
	install -d ${D}${systemd_unitdir}/system
	install -m 0644 ${WORKDIR}/simple_switch.service ${D}${systemd_unitdir}/system/
	install -d ${D}${bindir}/bm_tools/bm_runtime/
	install -m 0755 ${S}/bm/tools/runtime_CLI.py ${D}${bindir}/bm_tools
	install -m 0755 ${S}/bm/tools/nanomsg_client.py ${D}${bindir}/bm_tools/
	cp -r ${S}/bm/tools/bm_runtime/* ${D}${bindir}/bm_tools/bm_runtime/
}

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "simple_switch.service"

inherit pythonnative openswitch autotools-brokensep systemd

LIBTOOL = "${B}/${HOST_SYS}-libtool"
EXTRA_OEMAKE = "'LIBTOOL=${LIBTOOL}' PFX=${PKG_CONFIG_SYSROOT_DIR}"
