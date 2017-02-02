# Copyright Mellanox Technologies, Ltd. 2001-2016.

SUMMARY = "Hardware Management kernel drivers"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

inherit module systemd libreswitch mellanox

SRC_URI = " \
    ${MLNX_HW_MGMT_URI}/mlnx-hw-mgmt-${MLNX_HW_MGMT_VERSION}.tar.gz;subdir=mlnx-hw-mgmt-${MLNX_HW_MGMT_VERSION} \
    file://mlnx-bsp.service \
    file://0001-mlnx-asic-drv-update-to-kernel-4.x.patch \
    file://0002-lpc_i2c-udpate-to-kernel-4.x.patch \
"

SRC_URI[md5sum] = "41a7d9c878742a36d57eb47b6044f85d"
SRC_URI[sha256sum] = "3161490aa96e6c43267f44c4152963b3c2614be6cd9bbb6334dfd609abdbc3c2"

S = "${WORKDIR}/mlnx-hw-mgmt-${MLNX_HW_MGMT_VERSION}/hw-management/"

do_install_append() {
    install -d ${D}${sysconfdir}/mlnx/
    install -d ${D}${prefix}/sbin/
    install -d ${D}${prefix}/bin/
    install -d ${D}/lib/lsb/
    install -d ${D}${systemd_unitdir}/system/

    cp -R ${S}/usr/etc/mlnx/* ${D}${sysconfdir}/mlnx/
    chmod -R 755 ${D}${sysconfdir}/mlnx/

    cp -R ${S}/usr/usr/sbin/* ${D}${prefix}/sbin/
    chmod -R 755 ${D}${prefix}/sbin/

    cp -R ${S}/usr/usr/bin/* ${D}${prefix}/bin/
    chmod -R 755 ${D}${prefix}/bin/

    cp -R ${S}/usr/lib/lsb/* ${D}/lib/lsb/
    chmod -R 755 ${D}/lib/lsb/

    install -m 0644 ${WORKDIR}/mlnx-bsp.service ${D}${systemd_unitdir}/system
}

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "mlnx-bsp.service"

FILES_${PN} = "${sysconfdir}/mlnx/ ${prefix}/sbin/ ${prefix}/bin/ /lib/lsb ${systemd_unitdir}/system/mlnx-bsp.service"
