# Copyright (C) 2015 Hewlett Packard Enterprise Development LP

PR_append = "_as6712"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://Makefile-modules"

OPENNSL_PLATFORM = "as6712"
OPENNSL_PLATFORM_BUILD = "3d77f88"
GPL_MODULES_DIR = "sdk-6.4.10-gpl-modules"

SRC_URI[md5sum] = "0b9c959fa94d23825e9bf0c1fe5b1166"
SRC_URI[sha256sum] = "9920eef7e337be1188d243ca52213f94d0f5362e224f2bee95d1427d65e1d699"
