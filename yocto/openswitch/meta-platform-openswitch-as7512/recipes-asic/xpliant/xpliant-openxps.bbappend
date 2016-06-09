# Copyright (C) 2016, Cavium, Inc. All Rights Reserved.

PR_append = "_as7512"

inherit module-base kernel-module-split

addtask make_scripts after do_patch before do_compile
do_make_scripts[lockfiles] = "${TMPDIR}/kernel-scripts.lock"
do_make_scripts[depends] += "virtual/kernel:do_shared_workdir"
# add all splitted modules to PN RDEPENDS
KERNEL_MODULES_META_PACKAGE = "${PN}"

EXTRA_OEMAKE += "KERNEL_SRC=${STAGING_KERNEL_DIR}"

do_compile_append() {
    # Compile kernel modules
    cd ${S}/xpnet
    unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
    oe_runmake KERNEL_PATH=${STAGING_KERNEL_DIR} KERNEL_VERSION=${KERNEL_VERSION} \
        CC="${KERNEL_CC}" LD="${KERNEL_LD}" AR="${KERNEL_AR}" \
        O=${STAGING_KERNEL_BUILDDIR} ${MAKE_TARGETS}
}

FILES_${PN} += "/lib/firmware"

do_install_append() {
    # Install serdes configuration files
    install -d ${D}/lib/firmware/Avago/serdes
    install -m 0644 ${S}/bin/serdes/serdes.0x105A_0045.swap ${D}/lib/firmware/Avago/serdes
    install -m 0644 ${S}/bin/serdes/serdes.0x105A_0045.rom ${D}/lib/firmware/Avago/serdes
    install -m 0644 ${S}/bin/serdes/sbus_master.0x101A_0001.rom ${D}/lib/firmware/Avago/serdes

    # Install kernel modules
    cd ${S}/xpnet
    unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
    oe_runmake DEPMOD=echo INSTALL_MOD_PATH="${D}" \
        CC="${KERNEL_CC}" LD="${KERNEL_LD}" \
        O=${STAGING_KERNEL_BUILDDIR} modules_install
}
