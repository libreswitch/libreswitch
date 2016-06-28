# Copyright (C) 2015 Hewlett Packard Enterprise Development LP

PR_append = "_as6712"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://Makefile-modules"

OPENNSL_PLATFORM = "as6712"
OPENNSL_PLATFORM_BUILD = "e8bdb92"
GPL_MODULES_DIR = "sdk-6.4.10-gpl-modules"

SRC_URI[md5sum] = "3b60da197b2e64ac3caa7a4bd7ff0533"
SRC_URI[sha256sum] = "e7587dafe71847fb7b7707a56fc2da795de5f6161363f9d500d3b4e422eb6eb7"
