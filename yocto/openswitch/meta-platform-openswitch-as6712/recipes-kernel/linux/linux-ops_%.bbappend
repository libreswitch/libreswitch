# Copyright (C) 2015 Hewlett Packard Enterprise Development LP

PR_append = "_as6712"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append = "\
    file://defconfig \
    file://i2c-block-access.patch \
    file://driver-support-new-broadcom-phys.patch \
    file://driver-support-intel-avoton-ethernet-with-broadcom-phy.patch \
"
