# Copyright (C) 2015 Hewlett Packard Enterprise Development LP

PR_append = "_as5712"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://Makefile-modules"

OPENNSL_PLATFORM = "as5712"
OPENNSL_PLATFORM_BUILD = "5e55299"
GPL_MODULES_DIR = "sdk-6.4.10-gpl-modules"

SRC_URI[md5sum] = "189ad3ccf8fbad6848b740283ea3d656"
SRC_URI[sha256sum] = "27acaebb8c295a643abdb0ff6537b0df9962d97f5d12ff5fd60621d61a509fe0"
