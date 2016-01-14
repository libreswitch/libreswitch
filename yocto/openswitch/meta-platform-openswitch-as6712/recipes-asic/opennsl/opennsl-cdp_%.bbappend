# Copyright (C) 2015 Hewlett Packard Enterprise Development LP

PR_append = "_as6712"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://Makefile-modules"

OPENNSL_PLATFORM = "as6712"
OPENNSL_PLATFORM_BUILD = "11383a2"
GPL_MODULES_DIR = "sdk-6.4.8-gpl-modules"

SRC_URI[md5sum] = "e72fe014c21b6c839bd3e4da7f7b333d"
SRC_URI[sha256sum] = "8d7f9cfeb5613ac55e046cd53debf4747bb2b9e063285874017dd21fb0187d4a"
