# Copyright (C) 2015 Hewlett Packard Enterprise Development LP

PR_append = "_as5712"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://Makefile-modules"

OPENNSL_PLATFORM = "as5712"
OPENNSL_PLATFORM_BUILD = "3d77f88"
GPL_MODULES_DIR = "sdk-6.4.10-gpl-modules"

SRC_URI[md5sum] = "a9adff6918b354d1de6aef54db9590f0"
SRC_URI[sha256sum] = "a0e2673b1abc4d4d9717b29384a31f9a8c1442c32d946db21feebc655ceb859b"
