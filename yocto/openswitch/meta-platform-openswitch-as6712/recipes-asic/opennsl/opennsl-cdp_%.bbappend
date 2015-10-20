# Copyright (C) 2015 Hewlett Packard Enterprise Development LP

PR_append = "_as6712"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://Makefile-modules"

OPENNSL_PLATFORM = "as6712"
OPENNSL_PLATFORM_BUILD = "b97666c"
GPL_MODULES_DIR = "sdk-6.4.6-gpl-modules"

SRC_URI[md5sum] = "c6c00066f8fb10127f120b76f8bd9977"
SRC_URI[sha256sum] = "e99c2ebac6c5b12cca80c8bedc19e7d8b442e02471ab7397e5bc5efc3a7fc7e8"
