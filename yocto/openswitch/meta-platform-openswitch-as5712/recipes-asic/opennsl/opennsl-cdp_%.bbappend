# Copyright (C) 2015 Hewlett Packard Enterprise Development LP

PR_append = "_as5712"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://Makefile-modules"

OPENNSL_PLATFORM = "as5712"
OPENNSL_PLATFORM_BUILD = "e8bdb92"
GPL_MODULES_DIR = "sdk-6.4.10-gpl-modules"

SRC_URI[md5sum] = "ebfea2428c366d23a5680798b586dde1"
SRC_URI[sha256sum] = "3792acc59e1dfbbf7c971f7573ec0a763f007e0b1de648cc42f4339cf979f511"
