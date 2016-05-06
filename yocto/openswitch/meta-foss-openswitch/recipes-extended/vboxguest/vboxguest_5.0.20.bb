SUMMARY = "VirtualBox Guest Additions"
HOMEPAGE = "http://virtualbox.org"
LICENSE = "GPLv2"

LIC_FILES_CHKSUM = "file://COPYING;md5=e197d5641bb35b29d46ca8c4bf7f2660"

SRC_URI = "http://download.virtualbox.org/virtualbox/${PV}/VirtualBox-${PV}.tar.bz2 \
	file://vboxguest.service \
"

SRC_URI[md5sum] = "2054e12c21cac025916df4162f18efd7"
SRC_URI[sha256sum] = "af9c0787aae22964a1192cefbf9aa91a40c1d41e5fea485adebf09ab922d4c2b"

S = "${WORKDIR}/VirtualBox-${PV}"

inherit module-base kernel-module-split systemd

addtask make_scripts after do_patch before do_compile
do_make_scripts[lockfiles] = "${TMPDIR}/kernel-scripts.lock"
do_make_scripts[depends] += "virtual/kernel:do_shared_workdir"

# Extract the kernel drivers
do_configure() {
	cd src/VBox/Additions/linux
	./export_modules ${WORKDIR}/modules.tar.gz
	mkdir -p ${WORKDIR}/modules
	cd ${WORKDIR}/modules
	tar xvzf ${WORKDIR}/modules.tar.gz
}

do_compile() {
	# Build mount.vboxsf
        cd ${S}/src/VBox/Additions/linux/sharedfolders
        ${CC} -o mount.vboxsf -D_GNU_SOURCE -I../../../../../include -DIN_RING3 mount.vboxsf.c vbsfmount.c
	# Now build modules
	cd ${WORKDIR}/modules
	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
        oe_runmake -C ${STAGING_KERNEL_DIR}   \
		   KERNEL_PATH=${STAGING_KERNEL_DIR}   \
                   KERNEL_SRC=${STAGING_KERNEL_DIR}    \
                   CC="${KERNEL_CC}" LD="${KERNEL_LD}" \
                   AR="${KERNEL_AR}" \
                   M=${WORKDIR}/modules modules
}

do_install() {
	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
        oe_runmake -C ${STAGING_KERNEL_DIR} DEPMOD=echo INSTALL_MOD_PATH="${D}" \
                   CC="${KERNEL_CC}" LD="${KERNEL_LD}" \
		   M=${WORKDIR}/modules modules_install

	install -d ${D}/sbin
	install -m 0755 ${S}/src/VBox/Additions/linux/sharedfolders/mount.vboxsf ${D}/sbin
        install -d ${D}${systemd_unitdir}/system
        install -m 644 ${WORKDIR}/*.service ${D}${systemd_unitdir}/system
}

# add all splitted modules to PN RDEPENDS, PN can be empty now
KERNEL_MODULES_META_PACKAGE = "${PN}"
FILES_${PN} = "/sbin/mount.vboxsf /etc/modules-load.d/vboxguest-modules.conf"

SYSTEMD_SERVICE_${PN} = "vboxguest.service"
