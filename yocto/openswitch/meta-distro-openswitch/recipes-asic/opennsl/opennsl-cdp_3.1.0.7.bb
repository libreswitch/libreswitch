SUMMARY = "OpenNSL CDP Release"
LICENSE = "Proprietary & Apache-2.0 & GPLv2"
LIC_FILES_CHKSUM = "file://bin/LICENSE;md5=e4111c2d8b944da9d44a0c458635d87f \
                    file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6 \
                   "

DEPENDS_${PN} += "systemd"

PROVIDES = "virtual/opennsl"
RPROVIDES_${PN} = "virtual/opennsl"

OPENNSL_PLATFORM ?= "undefined"
OPENNSL_PLATFORM_BUILD ?= "unknown"
GPL_MODULES_DIR ?= "undefined"

SRC_URI = "http://archive.openswitch.net/opennsl/opennsl-${PV}-cdp-${OPENNSL_PLATFORM}-${OPENNSL_PLATFORM_BUILD}.tar.bz2 \
"

inherit module-base
inherit kernel-module-split

addtask make_scripts after do_patch before do_compile
do_make_scripts[lockfiles] = "${TMPDIR}/kernel-scripts.lock"
do_make_scripts[depends] += "virtual/kernel:do_shared_workdir"
# add all splitted modules to PN RDEPENDS
KERNEL_MODULES_META_PACKAGE = "${PN}"

S = "${WORKDIR}/opennsl-${PV}-cdp"
EXTERNALSRC_BUILD??="${S}/output"

# Avoid running make clean during configuration stage
CLEANBROKEN = "1"

CFLAGS += "--sysroot=${STAGING_DIR_TARGET}"
do_compile() {
    export CROSS_COMPILE="${TARGET_PREFIX}"
    export KERNEL_SRC="${STAGING_KERNEL_DIR}"
    export KBUILD_OUTPUT="${STAGING_KERNEL_BUILDDIR}"
    export TARGET_SYSROOT="${STAGING_DIR_TARGET}"
    export SDK="${S}/${GPL_MODULES_DIR}"
    install -d ${S}/${GPL_MODULES_DIR}/systems/linux/user/${OPENNSL_PLATFORM}
    cp ${WORKDIR}/Makefile-modules ${S}/${GPL_MODULES_DIR}/systems/linux/user/${OPENNSL_PLATFORM}/Makefile
    cd ${S}/${GPL_MODULES_DIR}/systems/linux/user/${OPENNSL_PLATFORM}
    make
}

do_compile[depends] += "virtual/kernel:do_shared_workdir"

do_install() {
    # Installing headers
    install -d ${D}/${includedir}
    cp -Rp ${S}/include/* ${D}/${includedir}

    # Installing library
    install -d ${D}/usr/lib/pkgconfig
    install -m 0755 ${S}/bin/${OPENNSL_PLATFORM}/libopennsl.so.1 ${D}/usr/lib/
    ln -s libopennsl.so.1 ${D}/usr/lib/libopennsl.so
    install -m 0655 ${S}/openswitch/opennsl.pc ${D}/usr/lib/pkgconfig/opennsl.pc

    # Installing netserve utility
    install -d ${D}/usr/bin/
    install -m 0755 ${S}/bin/${OPENNSL_PLATFORM}/netserve ${D}/usr/bin/

    # Installing kernel modules
    install -d ${D}/lib/modules/${KERNEL_VERSION}/extra/opennsl
    install -m 0644 ${S}/${GPL_MODULES_DIR}/build/linux/user/${OPENNSL_PLATFORM}/linux-kernel-bde.ko ${D}/lib/modules/${KERNEL_VERSION}/extra/opennsl
    install -m 0644 ${S}/${GPL_MODULES_DIR}/build/linux/user/${OPENNSL_PLATFORM}/linux-bcm-knet.ko ${D}/lib/modules/${KERNEL_VERSION}/extra/opennsl
    install -m 0644 ${S}/${GPL_MODULES_DIR}/build/linux/user/${OPENNSL_PLATFORM}/linux-user-bde.ko ${D}/lib/modules/${KERNEL_VERSION}/extra/opennsl
    install -d ${D}${sysconfdir}/modules-load.d/
    install -m 0655 ${S}/openswitch/bcm-modules.conf ${D}${sysconfdir}/modules-load.d/
    install -d ${D}${sysconfdir}/modprobe.d/
    install -m 0655 ${S}/openswitch/bcm-options.conf ${D}${sysconfdir}/modprobe.d/
    install -d ${D}${sysconfdir}/udev/rules.d
    install -m 0644 ${S}/openswitch/bcm.rules ${D}${sysconfdir}/udev/rules.d/70-bcm.rules
    install -m 0755 ${S}/openswitch/bcm_devices.sh ${D}${sysconfdir}/udev/rules.d/
}

INSANE_SKIP_${PN} += "already-stripped"

inherit openswitch perlnative
