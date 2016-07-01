# Copyright Mellanox Technologies, Ltd. 2001-2016.

SUMMARY = "Switch low level drivers"
LICENSE = "BSD & GPLv2"

inherit module openswitch mellanox

SRC_URI = " \
    ${SX_SDK_URI}/mlnx-sx-kernel-${SX_SDK_VERSION}.tar.gz;subdir=mlnx-sx-kernel-${SX_SDK_VERSION} \
"

SRC_URI[md5sum] = "d050e52ccc493ee8d23599d9fdfabc05"
SRC_URI[sha256sum] = "12311b30e30ed1a402808fb90d71412e8f02639f8b5160aa807625c8515b62b5"

PROVIDES = "virtual/mlnx-sx-kernel"
RPROVIDES_${PN} = "virtual/mlnx-sx-kernel"

S = "${WORKDIR}/mlnx-sx-kernel-${SX_SDK_VERSION}/sx_kernel-${SX_SDK_VERSION}-0/"

LIC_FILES_CHKSUM = " \
    file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6;  \
    file://${COMMON_LICENSE_DIR}/BSD;md5=3775480a712fc46a69647678acb234cb;  \
"

do_configure_prepend() {
    ${S}/configure --kernel-version=${LINUX_VERSION} --modules-dir=${D} --kernel-sources=${STAGING_KERNEL_DIR}
}

do_install() {
    install -d ${D}${sysconfdir}/mlnx
    install -d ${D}${sysconfdir}/modprobe.d
    install -d ${D}${sysconfdir}/udev/rules.d
    install -d ${D}/lib/modules/${LINUX_VERSION}/updates/kernel/drivers/net/mlx_sx
    install -d ${D}/lib/modules/${LINUX_VERSION}/updates/kernel/drivers/net/sx_netdev

    install -m 0755 ${S}/sx_scripts/sxdkernel ${D}/etc/mlnx/
    install -m 0644 ${S}/sx_scripts/sx.modprobe.conf ${D}/etc/modprobe.d/
    install -m 0644 ${S}/sx_scripts/blacklist-sx.conf ${D}/etc/modprobe.d/
    install -m 0644 ${S}/sx_scripts/91-sx.rules ${D}/etc/udev/rules.d/
    install -m 0644 ${S}/drivers/net/mlx_sx/sx_core.ko ${D}/lib/modules/${LINUX_VERSION}/updates/kernel/drivers/net/mlx_sx/
    install -m 0644 ${S}/drivers/net/sx_netdev/sx_netdev.ko ${D}/lib/modules/${LINUX_VERSION}/updates/kernel/drivers/net/sx_netdev/
}

FILES_${PN} += " \
    /etc/mlnx/* \
    /etc/modprobe.d/* \
    /etc/udev/rules.d/* \
"
