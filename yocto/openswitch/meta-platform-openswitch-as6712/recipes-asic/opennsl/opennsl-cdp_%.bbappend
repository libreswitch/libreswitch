# Copyright (C) 2015 Hewlett Packard Enterprise Development LP

PR_append = "_as6712"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://Makefile-modules"

OPENNSL_PLATFORM = "as6712"
OPENNSL_PLATFORM_BUILD = "6d85ca1"
GPL_MODULES_DIR = "sdk-6.4.8-gpl-modules"

SRC_URI[md5sum] = "93c5b4d3105627b41bd2fe1bada935e2"
SRC_URI[sha256sum] = "d811ee540e73d65dbdf2e298abb2c498b960789e6e2ead3800c861a28c3c0b5e"
