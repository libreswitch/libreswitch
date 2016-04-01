# Copyright (C) 2016, Cavium, Inc. All Rights Reserved.

PR_append = "_as7512"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append = "\
    file://defconfig \
    file://i2c-block-access.patch \
    file://driver-support-new-broadcom-phys.patch \
    file://driver-support-intel-avoton-ethernet-with-broadcom-phy.patch \
"
