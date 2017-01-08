SUMMARY = "P4 Data Plane Simulator (behavioral model)"
HOMEPAGE = "https://github.com/p4lang/p4factory"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2f3453ba8e98aaed11a290758a999e65"

LBS_P4DP_REPO = "github.com/p4lang/behavioral-model.git"

SRC_URI = "git://${LBS_P4DP_REPO};protocol=https;branch=ops \
           file://simple_switch.service \
           file://0001-Remove-warnings-when-compiling-in-GCC-5.patch \
           "

SRCREV = "2c4a6cd0d407c890ca00c898fe16c5ea5058e469"
PV = "git${SRCPV}"
S = "${WORKDIR}/git"

DEPENDS = " judy libedit nanomsg p4-hlir thrift gmp libpcap \
"
DEPENDS_class-native = "judy-native libedit-native nanomsg-native \
  p4-hlir-native thrift-native gmp-native libpcap-native \
"

# C libraries are automatically calculated by Yocto
RDEPENDS_${PN} = "\
	python-thrift python-pyyaml python-tenjin \
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

inherit pythonnative libreswitch autotools-brokensep systemd

LIBTOOL = "${B}/${HOST_SYS}-libtool"
EXTRA_OEMAKE = "'LIBTOOL=${LIBTOOL}'"

EXTRA_OEMAKE_class-native = "LIBTOOL=${BUILD_SYS}-libtool"

BBCLASSEXTEND = "native"
