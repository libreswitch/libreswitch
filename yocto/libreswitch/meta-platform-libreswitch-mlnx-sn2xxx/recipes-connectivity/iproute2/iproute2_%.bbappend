# Copyright Mellanox Technologies, Ltd. 2001-2016.

PR_append = "_mlnx_sn2xxx"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://mlnx_ip_route.patch \
"

EXTRA_OEMAKE = "EXCLUDE_ARPD_BUILD=1"
