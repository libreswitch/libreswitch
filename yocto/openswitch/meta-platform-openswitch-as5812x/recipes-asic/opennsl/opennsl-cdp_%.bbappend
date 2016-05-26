# Copyright (C) 2016 Hewlett Packard Enterprise Development LP

PR_append = "_as5812x"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://Makefile-modules"

OPENNSL_PLATFORM = "as5812x"
OPENNSL_PLATFORM_BUILD = "4c90aba"
GPL_MODULES_DIR = "sdk-6.4.10-gpl-modules"

SRC_URI[md5sum] = "28d5545f9386e06420c8537b6247cb90"
SRC_URI[sha256sum] = "540db4e9684c91ae49e0d9fb2b70c992beb3d097f9705e829e6da197829544e9"
