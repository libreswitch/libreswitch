# Copyright (C) 2016 Hewlett Packard Enterprise Development LP

PR_append = "_as7712"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://Makefile-modules"

OPENNSL_PLATFORM = "as7712"
OPENNSL_PLATFORM_BUILD = "04b2579"
GPL_MODULES_DIR = "sdk-6.4.10-gpl-modules"

SRC_URI[md5sum] = "c5f1f1a97d66d1bf84784ccdd9ea3304"
SRC_URI[sha256sum] = "27e6499f9c3a72aa540518d15c427e230a89cbfe5bf54306d2b553279c0d141e"
