# Copyright (C) 2015 Hewlett Packard Enterprise Development LP

PR_append = "_as6712"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://Makefile-modules"

OPENNSL_PLATFORM = "as6712"
OPENNSL_PLATFORM_BUILD = "43520f0"
GPL_MODULES_DIR = "sdk-6.4.10-gpl-modules"

SRC_URI[md5sum] = "f3a198e66190b0b2bfa0581d397c0f42"
SRC_URI[sha256sum] = "11f862e723e5a26b16a7eff6d9484c4835f69f554aa4958dce0d1b5be8693e64"
