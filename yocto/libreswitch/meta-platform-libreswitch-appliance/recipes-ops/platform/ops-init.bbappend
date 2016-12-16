# Copyright (C) 2016 Hewlett Packard Enterprise Development LP

PR_append = "_appliance"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

do_install_append() {
    # Do not move the interfaces into swns
    if ${@bb.utils.contains('IMAGE_FEATURES','ops-p4','true','false',d)}; then
        sed -i -e 's?/sbin/ip link set.*?echo Deferring moving interface eth$iface until bmv2 is up?g' ${D}${sbindir}/ops-init
    fi
}
