# Copyright (C) 2016 Hewlett Packard Enterprise Development LP

PR_append = "_as7712"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://Makefile-modules"

OPENNSL_PLATFORM = "as7712"
OPENNSL_PLATFORM_BUILD = "4c90aba"
GPL_MODULES_DIR = "sdk-6.4.10-gpl-modules"

SRC_URI[md5sum] = "8f563251b349d71184166c622b9c0f21"
SRC_URI[sha256sum] = "7f034885ff4182a5a7400d51fe8b2b7bcc9052716dfbe5a580ece0580c172bc1"
