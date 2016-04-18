# Copyright (C) 2015 Hewlett Packard Enterprise Development LP

PR_append = "_as5712"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://Makefile-modules"

OPENNSL_PLATFORM = "as5712"
OPENNSL_PLATFORM_BUILD = "43520f0"
GPL_MODULES_DIR = "sdk-6.4.10-gpl-modules"

SRC_URI[md5sum] = "8e5ef4c8ed6b6f7bf8f999e630d4cb99"
SRC_URI[sha256sum] = "bb669e11a276d1737ca519a56ad2b6e90424fdc9229a1b1ec3fa6cb0240164e0"
