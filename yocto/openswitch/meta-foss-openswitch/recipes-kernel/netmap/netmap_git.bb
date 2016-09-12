DESCRIPTION = "netmap - very fast packet I/O from userspace (FreeBSD/Linux)"
SECTION = "BSP"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9"

PR = "r0"
PV = "git"

SRCREV="30da094081157b2318d94783940435bdba062b71"

SRC_URI = " \
    git://github.com/luigirizzo/netmap.git;protocol=https \
    file://add-modules-install-target.patch \
"

S = "${WORKDIR}/git"
B = "${S}"

PACKAGES += "python-${PN} ${PN}-vale ${PN}-bridge ${PN}-pkt-gen ${PN}-tools"
FILES_python-${PN} = "${libdir}/${PYTHON_DIR}/*"
RRECOMMENDS_python-${PN} = "kernel-module-netmap"
FILES_${PN}-vale = "${bindir}/vale*"
RRECOMMENDS_${PN}-vale = "kernel-module-netmap"
FILES_${PN}-bridge = "${bindir}/bridge*"
RRECOMMENDS_${PN}-bridge = "kernel-module-netmap"
FILES_${PN}-pkt-gen = "${bindir}/pkt-gen*"
RRECOMMENDS_${PN}-pkt-gen = "kernel-module-netmap"
FILES_${PN}-tools = "${bindir}/test*"
RRECOMMENDS_${PN}-tools = "kernel-module-netmap"

do_configure() {
    cd ${S}/LINUX
    ./configure \
         --kernel-dir=${STAGING_KERNEL_BUILDDIR} \
         --prefix=${prefix} \
         --no-drivers
}

inherit setuptools module

do_compile() {
     cd ${S}/LINUX
     module_do_compile
     make apps
     cd ${S}/extra/python/
     distutils_do_compile
}

do_install() {
     cd ${S}/LINUX
     module_do_install
     make install-apps DESTDIR=${D}
     cd ${S}/extra/python/
     distutils_do_install
     cd ${S}
     install -d ${D}/${includedir}/net
     install -m 0664 sys/net/netmap.h ${D}/${includedir}/net
     install -m 0664 sys/net/netmap_user.h ${D}/${includedir}/net
     install -m 0664 sys/net/netmap_virt.h ${D}/${includedir}/net
}
