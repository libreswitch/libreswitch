DESCRIPTION = "netmap and VALE - very fast packet I/O from userspace (FreeBSD/Linux)"
SECTION = "BSP"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9"

PR = "r0"
PV = "git"

SRCREV="${AUTOREV}"

SRC_URI = " \
    git://code.google.com/p/netmap/;protocol=https \
    file://add-modules-install-target.patch \
    file://remove-warnings.patch \
"

S = "${WORKDIR}/git"

MAKE_TARGETS="NODRIVERS=1 KSRC=${STAGING_KERNEL_DIR}"

do_compile_prepend() {
	cd LINUX
	make apps
}

do_install_prepend() {
	cd examples
	install -d ${D}/usr/bin
	install -m 755 bridge ${D}/usr/bin
	install -m 755 pkt-gen ${D}/usr/bin
	install -m 755 test_select ${D}/usr/bin
	install -m 755 testmmap ${D}/usr/bin
	install -m 755 vale-ctl ${D}/usr/bin
	cd ../LINUX
}

inherit module

FILES_${PN} = "/usr/bin/*"
RDEPENDS_${PN} = "kernel-module-netmap-lin"
