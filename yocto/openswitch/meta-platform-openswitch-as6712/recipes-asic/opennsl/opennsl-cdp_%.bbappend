# Copyright (C) 2015 Hewlett Packard Enterprise Development LP

PR_append = "_as6712"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://Makefile-modules"

OPENNSL_PLATFORM = "as6712"
OPENNSL_PLATFORM_BUILD = "4c90aba"
GPL_MODULES_DIR = "sdk-6.4.10-gpl-modules"

SRC_URI[md5sum] = "e5bb6465b6bdc7d0b0a9fabb65aaa683"
SRC_URI[sha256sum] = "f74ec34dba1b1dcb3aeb73a274132c8300feb6c08367b1b7dc87e1d1b2b12442"
