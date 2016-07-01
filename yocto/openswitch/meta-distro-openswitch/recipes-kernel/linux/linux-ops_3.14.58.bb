inherit kernel
require linux.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

KERNEL_RELEASE = "3.14.58"
LINUX_VERSION ?= "${KERNEL_RELEASE}"

S = "${WORKDIR}/linux-3.14.58"

PR = "r1"
PV = "${KERNEL_RELEASE}"

SRC_URI = "https://cdn.kernel.org/pub/linux/kernel/v3.x/linux-3.14.58.tar.gz;name=kernel \
"

SRC_URI[kernel.md5sum] = "43cc44f9e64f3f030c6429a23c9798fc"
SRC_URI[kernel.sha256sum] = "e6a235fefc9240705d1154db5b3d91faa927169c4959e2aa2eae65542a76ff42"

do_install_append() {
   #remove empty directories to avoid errors during packaging
   find ${D}/lib/modules -empty | xargs rm -rf
}