# Copyright Mellanox Technologies, Ltd. 2001-2016.

PR_append = "_mlnx_sn2xxx"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append = "\
    file://defconfig \
    file://pmbus.patch \
    file://ucd9200.patch \
"
