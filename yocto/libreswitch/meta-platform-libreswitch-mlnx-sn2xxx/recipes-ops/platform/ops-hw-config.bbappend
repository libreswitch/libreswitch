# Copyright Mellanox Technologies, Ltd. 2001-2016.

PR_append = "_mlnx_sn2xxx"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SYSTEM_MANUFACTURER ?= "Mellanox"
PLATFORM_PATH = "${SYSTEM_MANUFACTURER}/MSN2700-CS2F/"

# Workaround for bitbake issue with pathes that contains spaces
do_install_append() {
        install -d ${D}${sysconfdir}/openswitch/platform/Mellanox\ Technologies\ Ltd./
        cp -R ${D}${sysconfdir}/openswitch/platform/${SYSTEM_MANUFACTURER}/* ${D}${sysconfdir}/openswitch/platform/Mellanox\ Technologies\ Ltd./
        rm -rf {D}${sysconfdir}/openswitch/platform/${SYSTEM_MANUFACTURER}/
}
