inherit kernel
require linux.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

KERNEL_RELEASE = "3.8.4"
S = "${WORKDIR}/linux-3.8.4"

PR = "r1"
PV = "${KERNEL_RELEASE}"

SRC_URI = "http://archive.openswitch.net/linux-3.8.4.tar.xz;name=kernel \
   file://fix-make-headers-install-when-path-too-long.patch \
   file://fix-race-condition-enc28j60.patch \
"

SRC_URI_append_powerpc = "\
   file://ppc/etsec-phy-bringup.patch \
   file://ppc/gianfar-tam.patch \
   file://ppc/i2c-mux-mmio-kbuild.patch \
   file://ppc/i2c-mux-mmio.patch \
   file://ppc/init-mem-1gb.patch \
   file://ppc/jtag-fix.patch \
   file://ppc/scripts-gdb.patch \
   file://ppc/simpleboot-makefile.patch \
"

SRC_URI[kernel.md5sum] = "ad19f1be181408124a7f9d8cf57b97b4"
SRC_URI[kernel.sha256sum] = "7e3bb07559ee9382ecf6babc5b5dab87f4fef6dbef3a9b014d361159b925b06b"

do_import_dts() {
   if test "${ARCH}" = "powerpc" ; then
      if test -n "${PLATFORM_DTS_FILE}" ; then
         echo "Updating in-kernel dts file with ${PLATFORM_DTS_FILE}"
         cp ${PLATFORM_DTS_FILE} ${S}/arch/powerpc/boot/dts/
      fi
   fi
}

do_install_append() {
   #remove empty directories to avoid errors during packaging
   find ${D}/lib/modules -empty | xargs rm -rf
}

addtask do_import_dts after do_patch before do_compile
