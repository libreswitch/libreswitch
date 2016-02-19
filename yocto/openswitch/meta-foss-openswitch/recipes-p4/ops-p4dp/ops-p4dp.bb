SUMMARY = "P4 Data Plane Simulator (behavioral model)"
HOMEPAGE = "https://github.com/p4lang/p4factory"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2f3453ba8e98aaed11a290758a999e65"

OPS_P4DP_REPO = "github.com/p4lang/behavioral-model.git"

SRC_URI = "\
	git://${OPS_P4DP_REPO};protocol=https;branch=ops \
	file://simple_switch.service \
"

SRCREV = "e57dc31dd66ea79086c9fd0cb24f3cdafdb9075a"
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
	install -m 0755 ${S}/tools/runtime_CLI.py ${D}${bindir}/bm_tools
	install -m 0755 ${S}/tools/nanomsg_client.py ${D}${bindir}/bm_tools/
	cp -r ${S}/tools/bm_runtime/* ${D}${bindir}/bm_tools/bm_runtime/
}

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "simple_switch.service"

inherit pythonnative openswitch autotools-brokensep systemd

LIBTOOL = "${B}/${HOST_SYS}-libtool"
EXTRA_OEMAKE = "'LIBTOOL=${LIBTOOL}' PFX=${PKG_CONFIG_SYSROOT_DIR}"
