SUMMARY = "Host sFlow agent"
HOMEPAGE = "http://www.sflow.net/"
LICENSE = "APL-1.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3e784d8af30c680a4ddd4ddf341d9b92"

SRC_URI = "https://github.com/sflow/host-sflow/archive/v1.28.3.tar.gz \
           file://0001-Fix-makefiles-to-support-cross-compiling.patch \
           "
SRC_URI[md5sum] = "3e8fe900c8991a24077c1876c4982ff8"
SRC_URI[sha256sum] = "fdfb22cd90da819eeaa6548f2aa40cf233b50bb4121bb1566f700cec6ec1b05c"

EXTRA_OEMAKE += "SYSTEM_SLICE=yes WITH_SFLOWOVSD=no LIBVIRT=no XEN_DDK=no NVML=no DEBIAN=no REDHAT=no NFLOG=no"

do_compile_prepend() {
    export TARGET_SYSROOT=${STAGING_DIR_TARGET}
}

do_install() {
    make install DESTDIR=${D}
}
