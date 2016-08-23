SUMMARY = "OpenNSL Switch Specification and Software"
LICENSE = "Proprietary & Apache-2.0 & GPLv2"

PROVIDES = "virtual/opennsl"
RPROVIDES_${PN} = "virtual/opennsl"

SRC_URI = " \
    file://opennsl.pc.in \
    file://bcm-modules.conf \
    file://bcm-options.conf \
    file://bcm.rules \
    file://bcm_devices.sh \
"

inherit module-base
inherit kernel-module-split

addtask make_scripts after do_patch before do_compile
do_make_scripts[lockfiles] = "${TMPDIR}/kernel-scripts.lock"
do_make_scripts[depends] += "virtual/kernel:do_shared_workdir"
# add all splitted modules to PN RDEPENDS
KERNEL_MODULES_META_PACKAGE = "${PN}"

OPENNSL_PLATFORM ?= "${MACHINE}"

S = "${WORKDIR}/opennsl-${PV}-odp"
EXTERNALSRC_BUILD??="${S}/output"

# Avoid running make clean during configuration stage
CLEANBROKEN = "1"


CFLAGS += "--sysroot=${STAGING_DIR_TARGET}"
do_compile() {
    export CROSS_COMPILE="${TARGET_PREFIX}"
    export KERNEL_SRC="${STAGING_KERNEL_DIR}"
    export KBUILD_OUTPUT="${STAGING_KERNEL_BUILDDIR}"
    export TARGET_SYSROOT="${STAGING_DIR_TARGET}"
    cd ${S}/output/${OPENNSL_PLATFORM}/
    # The build system has a bug where the CDP flags may be stale on this directory
    rm -Rf opennsldriver
    make sdk ${PARALLEL_MAKE}
    make
}

do_compile[depends] += "virtual/kernel:do_shared_workdir"

do_install() {
    # Installing headers
    install -d ${D}/${includedir}
    cp -Rp ${S}/include/* ${D}/${includedir}
    if ! test -d ${D}/${includedir}/shared ; then
      ln -sf odp ${D}/${includedir}/shared
    fi

    # Installing library
    install -d ${D}/usr/lib/pkgconfig
    install -m 0755 ${S}/output/${OPENNSL_PLATFORM}/build/libopennsl.so.1 ${D}/usr/lib/
    ln -s libopennsl.so.1 ${D}/usr/lib/libopennsl.so
    sed -e "s/@VERSION@/${PV}/" ${WORKDIR}/opennsl.pc.in > ${WORKDIR}/opennsl.pc
    install -m 0655 ${WORKDIR}/opennsl.pc ${D}/usr/lib/pkgconfig/opennsl.pc

    # Installing netserve utility
    install -d ${D}/usr/bin/
    install -m 0755 ${S}/output/${OPENNSL_PLATFORM}/sdk/linux/user/*/netserve ${D}/usr/bin/

    # Installing kernel modules
    install -d ${D}/lib/modules/${KERNEL_VERSION}/extra/opennsl
    install -m 0644 ${S}/output/${OPENNSL_PLATFORM}/sdk-gpl/linux/user/*/linux-kernel-bde.ko ${D}/lib/modules/${KERNEL_VERSION}/extra/opennsl
    install -m 0644 ${S}/output/${OPENNSL_PLATFORM}/sdk-gpl/linux/user/*/linux-bcm-knet.ko ${D}/lib/modules/${KERNEL_VERSION}/extra/opennsl
    install -m 0644 ${S}/output/${OPENNSL_PLATFORM}/sdk-gpl/linux/user/*/linux-user-bde.ko ${D}/lib/modules/${KERNEL_VERSION}/extra/opennsl
    install -d ${D}${sysconfdir}/modules-load.d/
    install -m 0655 ${WORKDIR}/bcm-modules.conf ${D}${sysconfdir}/modules-load.d/
    install -d ${D}${sysconfdir}/modprobe.d/
    install -m 0655 ${WORKDIR}/bcm-options.conf ${D}${sysconfdir}/modprobe.d/
    install -d ${D}${sysconfdir}/udev/rules.d
    install -m 0644 ${WORKDIR}/bcm.rules ${D}${sysconfdir}/udev/rules.d/70-bcm.rules
    install -m 0755 ${WORKDIR}/bcm_devices.sh ${D}${sysconfdir}/udev/rules.d/
}

DEPLOYDIR = "${WORKDIR}/deploy-${PN}"

do_cdp () {
    # We rebuild now for CDP mode
    export CROSS_COMPILE="${TARGET_PREFIX}"
    export KERNEL_SRC="${STAGING_KERNEL_DIR}"
    export KBUILD_OUTPUT="${STAGING_KERNEL_BUILDDIR}"
    export TARGET_SYSROOT="${STAGING_DIR_TARGET}"
    cd ${S}/output/${OPENNSL_PLATFORM}/
    make clean-all
    make cdp-bin

    OPENNSL_PV=`cat RELEASE | cut -d'-' -f 2`
    cd ${S}
    GIT_REV=`git log -1 --pretty=format:%h`
    install -d ${DEPLOYDIR}
    tar -xvf ${S}/output/${OPENNSL_PLATFORM}/packages/opennsl-${OPENNSL_PV}-cdp-${OPENNSL_PLATFORM}.tar.bz2 -C ${DEPLOYDIR}
    ${STRIP} ${DEPLOYDIR}/opennsl-${OPENNSL_PV}-cdp/bin/${OPENNSL_PLATFORM}/libopennsl.so.1
    ${STRIP} ${DEPLOYDIR}/opennsl-${OPENNSL_PV}-cdp/bin/${OPENNSL_PLATFORM}/netserve
    rm ${DEPLOYDIR}/opennsl-${OPENNSL_PV}-cdp/bin/${OPENNSL_PLATFORM}/*.ko
    mkdir -p ${DEPLOYDIR}/opennsl-${OPENNSL_PV}-cdp/openswitch
    sed -e 's/^Cflags:/Cflags: -DCDP_EXCLUDE /' ${WORKDIR}/opennsl.pc > ${DEPLOYDIR}/opennsl-${OPENNSL_PV}-cdp/openswitch/opennsl.pc
    cp ${WORKDIR}/bcm-modules.conf ${DEPLOYDIR}/opennsl-${OPENNSL_PV}-cdp/openswitch
    cp ${WORKDIR}/bcm-options.conf ${DEPLOYDIR}/opennsl-${OPENNSL_PV}-cdp/openswitch
    cp ${WORKDIR}/bcm.rules ${DEPLOYDIR}/opennsl-${OPENNSL_PV}-cdp/openswitch
    cp ${WORKDIR}/bcm_devices.sh ${DEPLOYDIR}/opennsl-${OPENNSL_PV}-cdp/openswitch
    cp -Rp ${S}/sdk-*-gpl-modules ${DEPLOYDIR}/opennsl-${OPENNSL_PV}-cdp/
    cp -Rp ${S}/include ${DEPLOYDIR}/opennsl-${OPENNSL_PV}-cdp/
    # Remove proprietary headers
    rm -f ${DEPLOYDIR}/opennsl-${OPENNSL_PV}-cdp/include/shared/shell.h
    cd ${DEPLOYDIR}
    tar -cvjf opennsl-${OPENNSL_PV}-cdp-${OPENNSL_PLATFORM}-${GIT_REV}.tar.bz2 opennsl-${OPENNSL_PV}-cdp
    rm -Rf ${DEPLOYDIR}/opennsl-${OPENNSL_PV}-cdp
    cp opennsl-${OPENNSL_PV}-cdp-${OPENNSL_PLATFORM}-${GIT_REV}.tar.bz2 ${DEPLOY_DIR_IMAGE}
}

addtask cdp
do_cdp[nostamp] = "1"

inherit openswitch perlnative
