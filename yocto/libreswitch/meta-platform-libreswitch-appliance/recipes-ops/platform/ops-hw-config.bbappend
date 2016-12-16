# Copyright (C) 2015 Hewlett Packard Enterprise Development LP

PR_append = "_appliance"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

PLATFORM_PATH = "OpenSwitch/Appliance"

do_install_append() {
    # We need an alias since the ops-sysd is hardwired in simulation
    # TODO: replace for a fake FRU
    install -d ${D}${sysconfdir}/libreswitch/platform/Generic-x86/
    ln -sf /etc/libreswitch/platform/${PLATFORM_PATH} ${D}${sysconfdir}/libreswitch/platform/Generic-x86/X86-64
}
