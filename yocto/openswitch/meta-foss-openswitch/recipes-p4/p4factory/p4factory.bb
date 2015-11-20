SUMMARY = "Programs used to build P4 for the P4 Behavioral Model"
HOMEPAGE = "https://github.com/p4lang/p4factory"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2f3453ba8e98aaed11a290758a999e65"

# This will point to a forked repository
# https://git.openswitch.net/openswitch/ops-p4factory but we need to
# sort out how to do that first (because they are using submodules).
SRC_URI = "gitsm://github.com/p4lang/p4factory;protocol=https \
	file://patches/include_stdio.patch \
	file://patches/use_cross_compiler.patch \
	file://patches/no_redis.patch \
	file://patches/fix_cpp_compilation.patch \
"
SRCREV = "2b69702c678df8e5542bbdc2686cc706b12b6335"
PV = "git${SRCPV}"
S = "${WORKDIR}/git"

DEPENDS = "thrift judy libedit thrift-native python-native p4-hlir python-pyyaml-native"
RDEPENDS_${PN} = "thrift libedit judy libpcap"

inherit pythonnative

do_compile() {
	make VERBOSE=1 -C targets/basic_routing
}

do_install() {
}
