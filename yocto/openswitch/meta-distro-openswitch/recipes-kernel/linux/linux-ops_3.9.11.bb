inherit kernel
require linux.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

KERNEL_RELEASE = "3.9.11"
S = "${WORKDIR}/linux-3.9.11"

PR = "r1"
PV = "${KERNEL_RELEASE}"

SRC_URI = "http://archive.openswitch.net/linux-3.9.11.tar.xz;name=kernel \
    file://fix-make-headers-install-when-path-too-long.patch \
    file://fix-autoneg-100mbps-igb.patch \
"

SRC_URI[kernel.md5sum] = "edbf88eb7f7d34dbd5d3887726790755"
SRC_URI[kernel.sha256sum] = "d4b9e522925d9b1b3fd130c8b7690ef6af4faf118fc4c5e28dfcbb18de3cb234"

do_install_append() {
   #remove empty directories to avoid errors during packaging
   find ${D}/lib/modules -empty | xargs rm -rf
}

# Disabling this logic since it's not sstate aware and therefore triggers
# unrequired builds and slows CI
#
# do_import_dts() {
#   if test "${ARCH}" = "powerpc" ; then
#      if test -n "${PLATFORM_DTS_FILE}" ; then
#         echo "Updating in-kernel dts file with ${PLATFORM_DTS_FILE}"
#         cp ${PLATFORM_DTS_FILE} ${S}/arch/powerpc/boot/dts/
#      fi
#   fi
#}
#
#addtask do_import_dts after do_patch before do_compile
