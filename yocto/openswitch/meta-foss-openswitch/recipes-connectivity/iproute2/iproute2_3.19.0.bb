require iproute2.inc

SRC_URI = "${KERNELORG_MIRROR}/linux/utils/net/${BPN}/${BP}.tar.xz \
           file://configure-cross.patch \
           file://0001-iproute2-de-bash-scripts.patch \
          "
SRC_URI[md5sum] = "237083a1e3c388cde7a115a5724dc72a"
SRC_URI[sha256sum] = "e2f9f8c36e166f2ba6c0e1e7a9ad84cdf7c1615b93df49dac44563d7b57fd7b0"

# CFLAGS are computed in Makefile and reference CCOPTS
EXTRA_OEMAKE_append = " CCOPTS='${CFLAGS}'"

FILES_${PN} += "${libdir}/tc"
FILES_${PN}-dbg += "${libdir}/tc/.debug"
