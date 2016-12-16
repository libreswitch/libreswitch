# Copyright (C) 2016 Hewlett Packard Enterprise Development LP

PR_append = "_as7712"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://Makefile-modules"

OPENNSL_PLATFORM = "as7712"
OPENNSL_PLATFORM_BUILD = "6217657"
GPL_MODULES_DIR = "sdk-6.4.11-gpl-modules"

SRC_URI[md5sum] = "c8c3ec5d62f4a627a3c1062a6709cb07"
SRC_URI[sha256sum] = "b44eafa4a2481a0e7e8886fcee3d1c2803751afcc3a508cff148695e6f2a69da"
