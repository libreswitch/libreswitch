# Copyright 2014  Hewlett-Packard Development Company, L.P. 

PR_append = "_as5712"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append = "\
    file://defconfig \
    file://i2c-block-access.patch \
    file://driver-support-new-broadcom-phys.patch \
    file://driver-support-intel-avoton-ethernet-with-broadcom-phy.patch \
"

