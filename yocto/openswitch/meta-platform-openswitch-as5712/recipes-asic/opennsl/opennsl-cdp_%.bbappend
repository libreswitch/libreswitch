# Copyright (C) 2015 Hewlett Packard Enterprise Development LP

PR_append = "_as5712"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://Makefile-modules"

OPENNSL_PLATFORM = "as5712"
OPENNSL_PLATFORM_BUILD = "04b2579"
GPL_MODULES_DIR = "sdk-6.4.10-gpl-modules"

SRC_URI[md5sum] = "f0e09ee38b4c88ffa80cd93200eac725"
SRC_URI[sha256sum] = "71da537e2f8fe40f176474e02da053bd66ca630b048c85238a2c8999cace5628"
