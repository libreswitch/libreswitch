# Copyright (C) 2015 Hewlett Packard Enterprise Development LP

PR_append = "_as5712"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://Makefile-modules"

OPENNSL_PLATFORM = "as5712"
OPENNSL_PLATFORM_BUILD = "b4c5954"
GPL_MODULES_DIR = "sdk-6.4.8-gpl-modules"

SRC_URI[md5sum] = "8ef9cc288e083b0ab63912badac96b3c"
SRC_URI[sha256sum] = "16b692fe3af99efe3b608d44dd59c985b5d7362804454a47cc59e26a951794d0"
