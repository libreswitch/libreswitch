# Copyright (C) 2015 Hewlett Packard Enterprise Development LP

PR_append = "_as5712"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://Makefile-modules"

OPENNSL_PLATFORM = "as5712"
OPENNSL_PLATFORM_BUILD = "11383a2"
GPL_MODULES_DIR = "sdk-6.4.8-gpl-modules"

SRC_URI[md5sum] = "be5e0feeaa9a3c7a67b4e015e92bbb5b"
SRC_URI[sha256sum] = "e7eaf9d56bcff13096f21eed9b32287d06e8f84ae4a6a24e69aec2bd32ed3e8a"
