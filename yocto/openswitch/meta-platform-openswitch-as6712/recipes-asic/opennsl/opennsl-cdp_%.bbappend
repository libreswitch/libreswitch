# Copyright (C) 2015 Hewlett Packard Enterprise Development LP

PR_append = "_as6712"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://Makefile-modules"

OPENNSL_PLATFORM = "as6712"
OPENNSL_PLATFORM_BUILD = "5e55299"
GPL_MODULES_DIR = "sdk-6.4.10-gpl-modules"

SRC_URI[md5sum] = "d2c347acaf81d640ca9e964fb511302b"
SRC_URI[sha256sum] = "e82aba929d47a3b8151ac52c9f1967366f8ee417d9538130b99bbe8675b6d3bc"
