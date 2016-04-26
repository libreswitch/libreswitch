# Copyright (C) 2015 Hewlett Packard Enterprise Development LP

PR_append = "_as6712"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://Makefile-modules"

OPENNSL_PLATFORM = "as6712"
OPENNSL_PLATFORM_BUILD = "c1e81c5"
GPL_MODULES_DIR = "sdk-6.4.10-gpl-modules"

SRC_URI[md5sum] = "25908b1f38e77791e214b27c6feeabeb"
SRC_URI[sha256sum] = "6162beee7f1ba61de700ff82db34f8c5120523c9f3c6019a98fa570af27c5487"
